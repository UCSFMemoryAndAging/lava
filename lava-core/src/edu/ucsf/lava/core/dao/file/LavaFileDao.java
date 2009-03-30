package edu.ucsf.lava.core.dao.file;

public interface LavaFileDao {
	public LavaFile getFile(String fileId) throws FileDaoException;
	public LavaFile getFile(String repositoryId, String fileId) throws FileDaoException;
	public void deleteFile(String fileId) throws FileDaoException;
	public void deleteFile(String repositoryId,String fileId) throws FileDaoException;
	public LavaFile storeFile(LavaFile file) throws FileDaoException;
	public LavaFile storeFile(LavaFile file,boolean overwrite) throws FileDaoException;
	public LavaFile storeFile(String repositoryId, LavaFile file) throws FileDaoException;
	public LavaFile storeFile(String repositoryId, LavaFile file,boolean overwrite) throws FileDaoException;
	public LavaFileRepository getRepository(String repositoryId) throws FileDaoException;
	public String getDefaultRepositoryId();
	public void setDefaultRepositoryId(String defaultRepositoryId);
}
