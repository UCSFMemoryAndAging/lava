package edu.ucsf.lava.core.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;

import edu.ucsf.lava.core.file.exception.AlreadyExistsFileAccessException;
import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.exception.FileNotFoundException;
import edu.ucsf.lava.core.file.model.LavaFile;

public class LocalFileSystemRepositoryStrategy extends AbstractFileRepositoryStrategy {
	public static String DEFAULT_REPOSITORY_ID = "lava_default";

	protected String rootPath = "";
	public static String MISSING_ID_FOLDER = "NO_ID";
	
	
	/**
	 * with a local file system repository there is no connect / disconnect
	 */
	
	public boolean connect() throws FileAccessException {
		return true;
	}
	/**
	 * with a local file system repository there is no connect / disconnect
	 */
	public boolean disconnect() throws FileAccessException {
		return true;
	}
	
	/**
	 * with a local file system repository there is no connect / disconnect
	*/	
	@Override
	public boolean isConnected() throws FileAccessException {
		return true;
	}

	
	/**
	 * Default behavior is to handle files for the default repository so 
	 * subclasses should override to handle files belonging to a specific 
     * repository or content type etc.
	 */
	public boolean handlesFile(LavaFile file) {
		// alternatively could handle files based on contentType or something else
		if (file.getRepositoryId().equals(DEFAULT_REPOSITORY_ID)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Default implementation is to use the record id of the FileEntity.  If for
	 * some reason this is called prior to persistence of the FileEntity, then
	 * use checksum of file contents or current timestamp (neither of these is
	 * guaranteed to be unique, but will be close to unique in most use cases).
	 */
	@Override
	public String generateFileId(LavaFile lavaFile) throws FileAccessException {
		if(lavaFile.getId()!=null){
			return lavaFile.getId().toString();
		}else{
			//should not find ourselves here as the FileEntity should be persisted 
			//before the file save.
			if(lavaFile.getChecksum()!=null){
				return lavaFile.getChecksum();
			}else{
				//use date/time
				DateFormat formatter = new SimpleDateFormat("yyyymmdd_HHmmss_SSS", Locale.getDefault());
				return formatter.format(new Date());
			}
		}
	}
	
	/**
	 * Default implementation is to organize files into levels of folders each with 
	 * 256 subfolders and 256 files.  
	 * 
	 */
	@Override
	public String generateLocation(LavaFile lavaFile) throws FileAccessException {
		
		StringBuffer location = new StringBuffer();
		
		//build subfolders
		try{
			long dividend = Long.valueOf(lavaFile.getFileId()).longValue();
			long divisor = (long)256;
			// put files with id < 256 in a subfolder labeled '0'
			if(dividend < divisor){
				location.append("0").append(File.separatorChar);
			}
			
			while(dividend > divisor){
				location.append(divisor - (dividend % divisor)).append(File.separatorChar);
				dividend = dividend/divisor;
			}
			
		}catch(NumberFormatException nfe){
			location.append(MISSING_ID_FOLDER);
		}
		return location.toString();
	}

	/**
	 * Check to see if the file exists.  If location / file id aren't set then 
	 * set them. 
	 * 
	 */
	@Override
	public boolean fileExists(LavaFile lavaFile) throws FileAccessException {
		File fsFile;
		try{
			fsFile = getFileSystemObject(lavaFile,false);
		}catch (Exception e){
			return false;
		}
		return fsFile != null && fsFile.exists() && fsFile.isFile();
	}
	
	@Override
	public LavaFile getFile(LavaFile lavaFile) throws FileAccessException {
		try {
			File fsFile = getFileSystemObject(lavaFile,true);
			InputStream in = new FileInputStream(fsFile);
			byte[] buffer = new byte[(int) fsFile.length()];
			in.read(buffer);
			lavaFile.setContent(buffer);
			return lavaFile;
		} catch (IOException ioe) {
			this.logRepositoryError("Error reading file from repository",lavaFile);
			throw new FileAccessException(
				"There was a problem reading the file.",lavaFile);
		}
	}

	@Override
	public void deleteFile(LavaFile lavaFile) throws FileAccessException {
		try {
			File fsFile = getFileSystemObject(lavaFile,false);
			if (fsFile.exists() && fsFile.isFile()) {
				fsFile.delete();
				lavaFile.setFileId(null);
				lavaFile.setLocation(null);
			}else{
				this.logRepositoryError("Warning: attempt to delete non-existent file", lavaFile);
				return; 
			} 
		}catch(SecurityException e){
			this.logRepositoryError("Error deleting file from repository", lavaFile);
			throw new FileAccessException("There was a problem deleting the file.",lavaFile,e); 
		}
	}

	@Override
	public LavaFile saveFile(LavaFile lavaFile) throws FileAccessException {
		return this.saveFile(lavaFile,false);
	}

	
	@Override
	public LavaFile saveOrUpdateFile(LavaFile lavaFile) throws FileAccessException {
		return this.saveFile(lavaFile, true);
		}

	protected LavaFile saveFile(LavaFile lavaFile, boolean overwrite) throws FileAccessException{
		File fsFile = getFileSystemObject(lavaFile,false);
		if(fsFile.exists() && !overwrite){
			this.logRepositoryError("Error saving, file already exists", lavaFile);
			throw new AlreadyExistsFileAccessException("Error storing file, it already exists.'",lavaFile);
		}
		try{
			File dir = fsFile.getParentFile();
			if (dir != null && !dir.exists()){
				dir.mkdirs();
			}
			FileCopyUtils.copy(lavaFile.getContent(), fsFile); 
			return lavaFile;
			
		}catch(IOException e){
			this.logRepositoryError("Error saving file in repository", lavaFile);
			throw new FileAccessException("Error saving file in repository",lavaFile,e);	
		}
	}
	/**
	 * Get the file system object reference.  Standard implementation finds file based on
	 * location property of lavaFile.  If fileId and location not set, generates them from 
	 * other properties in a repository specific manner. 
	 * 
	 * @throws FileAccessException
	 */
	protected File getFileSystemObject(LavaFile lavaFile, boolean verify) throws FileAccessException{
		if(lavaFile.getFileId()==null){
			lavaFile.setFileId(this.generateFileId(lavaFile));
			if(lavaFile.getFileId()==null){
				this.logRepositoryError("Missing fileId when locating file on file system.", lavaFile);
				throw new FileAccessException("Missing fileId when locating file on file system.",lavaFile);
			}
		}
		if(lavaFile.getLocation()==null){
			lavaFile.setLocation(this.generateLocation(lavaFile));
			if(lavaFile.getLocation()==null){
				this.logRepositoryError("Unable to determine location of file on file system.", lavaFile);
				throw new FileAccessException("Unable to determine location of file on file system.",lavaFile);
			}
		}
		
		File file = new File(getFileSystemObjectLocation(lavaFile));
		if(verify &&(!file.exists() || !file.isFile())){
			logger.error("File does not exist, path=" + file.getAbsolutePath());
			throw new FileAccessException("There was a problem accessing the file: " + file.getAbsolutePath());
		}
		return file;
	}
	
	/**
	 * Return the absolute file system path to the file.  Assumes that fileId and Location
	 * have already been set / verified. 
	 * @param lavaFile 
	 * @return the file system location of the file. 
	 */
	public String getFileSystemObjectLocation(LavaFile lavaFile) throws FileAccessException{
		return new StringBuffer(this.rootPath).append(File.separatorChar)
			.append(lavaFile.getLocation()).append(File.separatorChar)
			.append(lavaFile.getFileId()).toString();
		}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
	
}
