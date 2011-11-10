package edu.ucsf.lava.core.file;

import java.util.List;

import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;

/**
 * Defines delegated implementation details for a FileRepository.  The delegated 
 * details include organizational pattern (directory structure in the case of 
 * file system repositories) and methods for connecting and testing 
 * to repositories.  
 * @author jhesse
 *
 */
public interface FileRepositoryStrategy {
	
	/**
	 * Set the object reference back to the repository instance.
	 * @param repository
	 */
	public void setRepository(FileRepository repository);
	
	/**
	 * Gets the object reference to the repository instance.
	 * @return
	 */
	public FileRepository getRepository();
	

	/**
	 * Connect to repository.
	 * @return true if connection succeeds or already connected
	 * @throws FileAccessException
	 */
	public boolean connect() throws FileAccessException;
	
	/**
	 * Disconnect from repository
	 * @return
	 * @throws FileAccessException
	 */
	public boolean disconnect() throws FileAccessException;
	
	/**
	 * Whether there is an active connection to the repository
	 * @return
	 * @throws FileAccessException
	 */
	public boolean isConnected() throws FileAccessException;
	
	/**
	 * Test basic functionality of repository using testFile.  Implementation
	 * should perform whatever tests are necessary to satisfy that repository
	 * can perform supported methods (e.g. saveFile,deleteFile).
	 * @return
	 * @throws FileAccessException
	 */
	public boolean testRepository(LavaFile testFile) throws FileAccessException;
	
	
	/** 
	 * Does this repository handle the file.
	 * @param file
	 * @return
	 */
	public boolean handlesFile(LavaFile file);
	
	/**
	 * Whether the fileId in the lavaFile matches what the repository would
	 * generate for fileId based on the properties of the lavaFile. 
	 * @param lavaFile
	 * @return
	 * @throws FileAccessException
	 */
	public boolean isFileIdValid(LavaFile lavaFile) throws FileAccessException;
	
	
	/**
	 * Generate the repository specific fileId based on the properties of the lavaFile.
	 * @param lavaFile
	 * @return
	 * @throws FileAccessException
	 */
	public String generateFileId(LavaFile lavaFile) throws FileAccessException;
	
	/**
	 * Whether the location in the lavaFile matches what the repository would
	 * generate for location based on the properties of the lavaFile. 
	 * @param lavaFile
	 * @return
	 * @throws FileAccessException
	 */
	public boolean isLocationValid(LavaFile lavaFile) throws FileAccessException;
	
	/**
	 * Generate the repository specific location based on the properties of the lavaFile.
	 * @param lavaFile
	 * @return
	 * @throws FileAccessException
	 */
	public String generateLocation(LavaFile lavaFile) throws FileAccessException;
	
	/**
	 * The repository file referenced by the fileId and/or location property
	 * in lavaFile exists and is a file. 
	 * 
	 * @param lavaFile
	 * @return
	 * @throws LavaDaoException
	 */
	public boolean fileExists(LavaFile lavaFile) throws FileAccessException;


	
	
	/**
	 * Get the file contents from the repository.
	 * @param file
	 * @return
	 * @throws FileAccessException
	 */
	public LavaFile getFile(LavaFile file) throws FileAccessException;

	
	/**
	 * Delete the file contents from the repository
	 * @param file
	 * @throws FileAccessException
	 */
	
	public void deleteFile(LavaFile file) throws FileAccessException;
	/**
	 * Save the file contents to the repository
	 * @param file
	 * @return
	 * @throws FileAccessException
	 */
	
	public LavaFile saveFile(LavaFile file) throws FileAccessException;
	/**
	 * Save the file contents to the repository overwriting any existing contents.
	 * @param file
	 * @return
	 * @throws FileAccessException
	 */
	
	public LavaFile saveOrUpdateFile(LavaFile file) throws FileAccessException;
	/**
	 * Return a list of LavaFile objects describing file contents found in the 
	 * repository matching the filter template.
	 * @param filter
	 * @return
	 * @throws FileAccessException
	 */
	public List<LavaFile> findFiles(LavaFileFilter filter) throws FileAccessException;
	 
}
