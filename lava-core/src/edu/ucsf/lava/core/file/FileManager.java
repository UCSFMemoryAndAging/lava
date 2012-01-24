package edu.ucsf.lava.core.file;

import java.util.List;

import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.manager.LavaManager;
/**
 * This manager centralizes all configuration, access, and management related 
 * to  file repositories.
 * @author jhesse
 *
 */
public class FileManager extends LavaManager  {
		
		public static String FILE_MANAGER_NAME="fileManager";
		
	
		public FileManager() {
			super(FILE_MANAGER_NAME);
		}

		protected FileRepositories repositories;

		public FileRepositories getRepositories() {
			return repositories;
		}

		public void setRepositories(FileRepositories repositories) {
			this.repositories = repositories;
		}

		/**
		 * Load the actual file contents into the file object from the underlying
		 * file repository. 
		 * 
		 * @param file the file object to load contents from the repository.
		 * @return file object with the content loaded from repository
		 * @throws FileAccessException
		 */
		public LavaFile getFile(LavaFile file) throws FileAccessException {
			return getRepository(file).getFile(file);
		}

		/**
		 * Delete the file contents from the repository.
		 * @param file the file object for deleting file contents.
		 * @throws FileAccessException
		 */
		public void deleteFile(LavaFile file) throws FileAccessException {
			getRepository(file).deleteFile(file);
		}

		/**
		 * Save the file contents to the repository.
		 * @param file the file to save. 
		 * @return the file object
		 * @throws FileAccessException
		 */
		public LavaFile saveFile(LavaFile file) throws FileAccessException {
			return getRepository(file).saveFile(file);
		}
	
		/**
		 * Save the file contents to the repository overwriting any 
		 * existing file contents. 
		 * @param file
		 * @return the file object
		 * @throws FileAccessException
		 */
		public LavaFile saveOrUpdateFile(LavaFile file) throws FileAccessException {
			 return getRepository(file).saveOrUpdateFile(file);
		}
		

		/**
		 * Retrieve a list of files based on the values in the filter.
		 * @param filter
		 * @return
		 */
		public List<LavaFile> findFiles(LavaFileFilter filter) throws FileAccessException {
			return getRepository(filter.getTemplate()).findFiles(filter);
		}
		
		/** 
		 * Repository access method that includes a call to verify the correctness of the 
		 * repository id in the lavaFile.
		 * @param lavaFile
		 * @return
		 * @throws FileAccessException
		 */
		protected FileRepository getRepository(LavaFile lavaFile) throws FileAccessException {
			FileRepository repository = this.repositories.getRepository(lavaFile);
			this.checkRepositoryAndFileConsistency(repository, lavaFile);
			return repository;
		}
		
		/**
		 * Standard method to ensure that the repository id in the file matches the
		 * repository that "handles" the file.  
		 * 
		 * Changing the id of a repository after it has already stored files without
		 * updating the database records will trigger an exception.
		 * 
		 * Similarly introducing a new repository that handles files already stored by
		 * another repository will trigger an exception.
		 * 
		 * TODO: code that moves a file from one repository to another.  That would then
		 * be an option when a new repository is introduced rather than to throw an exception.
		 * @param lavaFile
		 * @throws FileAccessException
		 */
		protected void checkRepositoryAndFileConsistency(FileRepository repository, LavaFile lavaFile) throws FileAccessException{
			if (lavaFile.getRepositoryId() == null){
				lavaFile.setRepositoryId(repository.getId());
			}else if(!lavaFile.getRepositoryId().equalsIgnoreCase(repository.getId())) {
				throw new FileAccessException("File Access Error: file repository id mismatch.  Repository id==" + repository.getId(),lavaFile);
			}
		}
}
