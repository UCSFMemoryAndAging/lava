package edu.ucsf.lava.core.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.FileCopyUtils;

import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;

/**
 * Extension to LocalFileSystemRepository that makes a backup of any existing
 * file prior to deleting, or updating a file. 
 * 
 * @author jhesse
 *
 */
public class ArchivingLocalFileSystemRepositoryStrategy extends LocalFileSystemRepositoryStrategy {
	public static final int ARCHIVE_METHOD_APPEND_DATE = 1;
	protected String archiveRoot = "";
	protected int archiveMethod = ARCHIVE_METHOD_APPEND_DATE;
	
	
	/**
	 * Archive existing file in repository.
	 * @param file
	 * @throws FileAccessException
	 */
	protected void archiveFile(LavaFile file) throws FileAccessException{
		try{
			File fsFile = getFileSystemObject(file,true);
			
			File archiveFile = getArchiveFileSystemObject(file);
			if(archiveFile.exists()){
				this.logRepositoryError("Error archiving file, archive file already exists.", file);
				throw new FileAccessException("Error archiving file, archive file already exists.",file);
			}
			File dir = archiveFile.getParentFile();
			if (dir != null && !dir.exists()){
				dir.mkdirs();
			}
			FileCopyUtils.copy(fsFile, archiveFile);
			
		}catch(Exception e){
			this.logRepositoryError("Error archiving file, archive file already exists.", file);
			throw new FileAccessException("Error archiving file, archive file already exists.",file,e);
		}
	}
	
	/**
	 * Get a file system object for archiving the file contents.
	 * @param file
	 * @return
	 */
	protected File getArchiveFileSystemObject(LavaFile file){
		
		StringBuffer archiveLocation = new StringBuffer(this.archiveRoot).append(File.separatorChar)
						.append(file.getLocation()).append(File.separatorChar)
						.append(file.getFileId());
		if(archiveMethod == ARCHIVE_METHOD_APPEND_DATE){
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyddmm_HHmmss");
			archiveLocation.append("_").append(dateFormat.format(new Date()));
		}
		return new File(archiveLocation.toString());
	}
	
	
	public void deleteFile(LavaFile file) throws FileAccessException {
		this.archiveFile(file);
		super.deleteFile(file);
	}

	
	
	public LavaFile saveOrUpdateFile(LavaFile file) throws FileAccessException {
		this.archiveFile(file);
		return super.saveOrUpdateFile(file);
	}

	

	public String getArchiveRoot() {
		return archiveRoot;
	}


	public void setArchiveRoot(String archiveRoot) {
		this.archiveRoot = archiveRoot;
	}
	
	
}
