package edu.ucsf.lava.core.dao.file;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseLavaFileDao implements LavaFileDao {
	protected Map<String,LavaFileRepository> repositories = new LinkedHashMap<String,LavaFileRepository>();
	private String defaultRepositoryId;
	
	
	

	public BaseLavaFileDao(ArrayList<LavaFileRepository> repositories) {
		super();
		for(LavaFileRepository repo : repositories){
			this.repositories.put(repo.getId(),repo);
		}
		//first repository is the default so set the default respository id 
		this.setDefaultRepositoryId(this.repositories.keySet().iterator().next());
		
	}

	public LavaFile getFile(String fileId) throws FileDaoException{
		return getFile(getDefaultRepositoryId(),fileId);
	}

	
	public LavaFile getFile(String repositoryId, String fileId) throws FileDaoException{
		return getRepository(repositoryId).get(fileId);
	}

	public void deleteFile(String fileId) throws FileDaoException{
		 deleteFile(getDefaultRepositoryId(),fileId);
	}

	public void deleteFile(String repositoryId, String fileId) throws FileDaoException{
		getRepository(repositoryId).delete(fileId);
	}

	public String getDefaultRepositoryId() {
		return defaultRepositoryId;
	}
	
	

	public LavaFile storeFile(LavaFile file, boolean overwrite) throws FileDaoException {
		return storeFile(getDefaultRepositoryId(),file,overwrite);
	}

	public LavaFile storeFile(LavaFile file) throws FileDaoException {
		return storeFile(getDefaultRepositoryId(),file,true);
	}

	public LavaFile storeFile(String repositoryId, LavaFile file) throws FileDaoException {
		return storeFile(repositoryId,file,true);
	}

	
	public LavaFile storeFile(String repositoryId, LavaFile file, boolean overwrite) throws FileDaoException {
		return getRepository(repositoryId).store(file,overwrite);
	}


	public void setDefaultRepositoryId(String defaultRepositoryId) {
		this.defaultRepositoryId = defaultRepositoryId;
	}
	
	public LavaFileRepository getRepository(String repositoryId) throws FileDaoException{
		LavaFileRepository repository = repositories.get(repositoryId);
		if(repository == null){
			throw new FileDaoException("The file repository '" + repositoryId + "' was not found.");
			}
		return repository;
	}
	
}
