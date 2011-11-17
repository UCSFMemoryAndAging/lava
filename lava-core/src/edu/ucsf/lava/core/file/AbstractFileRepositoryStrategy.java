package edu.ucsf.lava.core.file;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;

public abstract class AbstractFileRepositoryStrategy implements FileRepositoryStrategy {

	protected final Log logger = LogFactory.getLog(getClass());
	protected FileRepository repository;

	
	abstract public boolean connect() throws FileAccessException;
	abstract public boolean disconnect() throws FileAccessException;
	abstract public boolean isConnected() throws FileAccessException;
	abstract public String generateFileId(LavaFile lavaFile) throws FileAccessException;
	abstract public String generateLocation(LavaFile lavaFile) throws FileAccessException;
	abstract public boolean fileExists(LavaFile lavaFile) throws FileAccessException;
	abstract public LavaFile getFile(LavaFile file) throws FileAccessException;
	abstract public void deleteFile(LavaFile file) throws FileAccessException;
	abstract public LavaFile saveFile(LavaFile file) throws FileAccessException;
	abstract public LavaFile saveOrUpdateFile(LavaFile file) throws FileAccessException;

	public void setRepository(FileRepository repository) {
		this.repository = repository;
	}

	public FileRepository getRepository() {
		return this.repository;
	}

	/**
	 * Do a save, get and delete using the testFile
	 */
	public boolean testRepository(LavaFile testFile) throws FileAccessException {
		//write, read, and delete a test file to the root directory
		try{
			saveFile(testFile);
			testFile = this.getFile(testFile);
			deleteFile(testFile);
		}catch(Exception e){
			this.logRepositoryError("Exception caught during repository test:" + e.toString(), testFile);
			return false;
		}
		return true;
	}

	public boolean isFileIdValid(LavaFile lavaFile) throws FileAccessException {
		String fileId = this.generateFileId(lavaFile);
		if(fileId.equalsIgnoreCase(lavaFile.getFileId())){
			return true;
		}
		return false;
	}

	
	public boolean isLocationValid(LavaFile lavaFile) throws FileAccessException {
		String location = this.generateLocation(lavaFile);
		if(location==null && lavaFile.getLocation()==null){
			return true;
		}else if(location.equalsIgnoreCase(lavaFile.getLocation())){
			return true;
		}
		return false;
	}

	/**
	 * TODO: implement a file search mechanism. 
	 */
	public List<LavaFile> findFiles(LavaFileFilter filter) throws FileAccessException {
		return new ArrayList<LavaFile>();
	}

	protected void logRepositoryError(String message, LavaFile file) {
		StringBuffer logMessage = new StringBuffer(message).append("\n").append(file.toLogString());
		logger.error(logMessage.toString());
	}

	

}
