package edu.ucsf.lava.core.file;

import java.io.File;
import java.util.List;

import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.model.EntityBase;

public interface FileRepository {
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
	/**
	 * Return the string id of the repository.
	 * @return
	 */
	public String getId();
	/** 
	 * Does the repository handle the file.
	 * @param file
	 * @return
	 */
	public boolean handlesFile(LavaFile file);
	/**
	 * Perform tests of the repository functionality (get, save, delete).
	 * @return true if repository passes all tests.
	 */
	public boolean test();
	

	/**
	 * Returns the order (precedence) of the file repository.  Each repository that handles files 
	 * will be called in order (lowest to highest order). 
	 * @return
	 */
	public Long getOrder();
}

	
