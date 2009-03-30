package edu.ucsf.lava.core.dao.file;

import java.io.File;
import java.util.List;

import edu.ucsf.lava.core.model.EntityBase;

public interface LavaFileRepository {
	public LavaFile get(String fileId) throws FileDaoException;
	public void delete(String fileId) throws FileDaoException;
	public LavaFile store(LavaFile file) throws FileDaoException;
	public LavaFile store(LavaFile file, boolean overwrite) throws FileDaoException; 
	public String getId();
	public String getContentType(String file);
	public String getFileId(EntityBase entity);
	public String getFileId(EntityBase entity, String resourceType);
	public void createZipFile(List<LavaFile> files, File targetFile) throws FileDaoException;
}
