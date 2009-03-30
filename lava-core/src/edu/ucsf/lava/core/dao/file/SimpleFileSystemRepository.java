package edu.ucsf.lava.core.dao.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileCopyUtils;

import edu.ucsf.lava.core.model.EntityBase;

public class SimpleFileSystemRepository extends AbstractLavaFileRepository {

	protected final Log logger = LogFactory.getLog(getClass());
	protected String rootPath = "";
	

	public LavaFile get(String fileId) throws FileDaoException {
		LavaFile lavaFile = new LavaFile();
		lavaFile.setRepositoryId(this.getId());
		try{
			File file = getFileObject(fileId);
			if(file.exists() && file.canRead() && file.isFile()){
				lavaFile.setName(file.getName());
				//set content type
				String contentType = contentTypes.get(lavaFile.getName());
				InputStream in = new FileInputStream(file);
				byte[] buffer = new byte[(int) file.length()]; 
				in.read(buffer);
				lavaFile.setContent(buffer);
			}else{
				logger.error("Error reading file from repository:" + this.getId()+ "; Root: " + this.rootPath + "; File: " + fileId);
				throw new FileDaoException("There was a problem reading the file: " + fileId);
			}
			
		}catch(IOException ioe){
			logger.error("Error reading file from repositor:" + this.getId()+ "; Root: " + this.rootPath + "; File: " + fileId + "; Error: " + ioe.toString());
			throw new FileDaoException("There was a problem reading the file: " + fileId,ioe);
		}
		return lavaFile;
			
	}

	
	public void delete(String fileId) throws FileDaoException{
		LavaFile lavaFile = new LavaFile();
		lavaFile.setRepositoryId(this.getId());
		try{
			File file = getFileObject(fileId);
			if(file.exists() && file.isFile()){
				file.delete();
			}else{
				}
		}catch(SecurityException e){
			logger.error("Error deleting file from repository:" + this.getId()+ "; Root: " + this.rootPath + "; File: " + fileId + "; Error: " + e.toString());
			throw new FileDaoException("There was a problem deleting the file: " + fileId,e);
		}

	}
		

	public LavaFile store(LavaFile file, boolean overwrite) throws FileDaoException {
		File storeFile = getFileObject(file.getName());
		
		//handle an existing file with the name in the repository
		if(storeFile.exists()){
			if(overwrite == false){
				throw new AlreadyExistsFileDaoException("Error storing file '" + file.getName() +"' in repository.  The file aleady exists and overwrite set to 'false'");
			}else{
				delete(file.getName());
			}
		}
		//now store the file
		try{
			FileCopyUtils.copy(file.getContent(), storeFile);
		}catch(IOException e){
			throw new FileDaoException("Error storing file '" + file.getName() +"' in repository.",e);
		}
		//update lavafile object
		file.setRepositoryId(this.getId());
		file.setContentType(this.getContentType(file.getName()));
		return file;
	}


	public LavaFile store(LavaFile file) throws FileDaoException {
		return store(file,true);
	}


	public String getRootPath() {
		return rootPath;
	}


	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
	// override in subclass if fileId is not equal to file path relative to root path
	public String getFilePath(String fileId) {
		return fileId;
	}
	
	public String getAbsolutePath(String fileId) {
		File file = new File(getFilePath(fileId));
		if (file.isAbsolute()) {
			return fileId;
		} else {
		return new File(getRootPath(),fileId).getAbsolutePath();
		}
	}
	
	public String getFileId(EntityBase entity) {
		return getFileId(entity,null);
	}
	
	// override in subclass when fileId is derived from properties of an associated model object
	public String getFileId(EntityBase entity, String resourceType) {
		return "";
	}
	
	//by default verify that the object exists.  

	protected File getFileObject(String fileId) throws FileDaoException{
		return getFileObject(fileId,true);
	}
	
	protected File getFileObject(String path1, String path2) throws FileDaoException{
		return getFileObject(path1, path2,true);
	}
	
	protected File getFileObject(String path1, String path2, boolean verify) throws FileDaoException{
		File file = new File(path1,path2);
		if(verify &&(!file.exists() || !file.isFile())){
			logger.error("File does not exist, path=" + file.getAbsolutePath());
			throw new FileDaoException("There was a problem accessing the file: " + file.getAbsolutePath());
		}
		return file;
	}
	
	protected File getFileObject(String fileId, boolean verify) throws FileDaoException{
		File file = new File(getAbsolutePath(fileId));
		if(verify &&(!file.exists() || !file.isFile())){
			logger.error("File does not exist, path=" + file.getAbsolutePath());
			throw new FileDaoException("There was a problem accessing the file: " + file.getAbsolutePath());
		}
		return file;
	}
	
	public void createZipFile(List<LavaFile> files, File targetFile) throws FileDaoException {
	    
	    try {
	        // Create the ZIP file
	    	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetFile));
	    
	        // Compress the files
	        for (int i=0; i<files.size(); i++) {
	        	
	            // Add ZIP entry to output stream.
	            out.putNextEntry(new ZipEntry(files.get(i).getName()));
	    
	            // Transfer bytes to the ZIP file
	            out.write(files.get(i).getContent());
	    
	            // Complete the entry
	            out.closeEntry();
	        }
	    
	        // Complete the ZIP file contents
	        out.close();
	        
	    } catch (IOException e) {
	    	logger.error("Problem creating zip file " + targetFile.getPath());
			throw new FileDaoException("Problem creating zip file " + targetFile.getPath());
	    }
	}
	
}
