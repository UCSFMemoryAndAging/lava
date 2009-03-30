package edu.ucsf.lava.core.dao.file;

import java.io.File;
import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.model.EntityBase;

public abstract class AbstractLavaFileRepository implements LavaFileRepository {
	public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
	protected String id;
	protected Map<String,String> contentTypes;
		
	public abstract LavaFile get(String fileId) throws FileDaoException;
	public abstract void delete(String fileId) throws FileDaoException;
	public abstract LavaFile store(LavaFile file) throws FileDaoException;
	public abstract LavaFile store(LavaFile file, boolean overwrite) throws FileDaoException;
	public abstract String getFileId(EntityBase entity);
	public abstract String getFileId(EntityBase entity, String resourceType);
	public abstract void createZipFile(List<LavaFile> files, File targetFile) throws FileDaoException;

	public String getContentType(String file){
		if(contentTypes == null || file == null){return DEFAULT_CONTENT_TYPE;}
		
		String contentType = contentTypes.get(getExtension(file));
		if(contentType == null){return DEFAULT_CONTENT_TYPE;}
		return contentType;
	}
	
	/*
	 * found this by googling...It should work...joe
	 */
	protected  String getExtension(String name){
			// filename without the extension
			String choppedFilename;
			//extension without the dot
			String ext;

			//where the last dot is. There may be more than one.
			int dotIndex = name.lastIndexOf ( '.' );

			if ( dotIndex >= 0 )
			   {
			   // possibly empty
			   choppedFilename = name.substring( 0, dotIndex );

			   // possibly empty
			   ext = name.substring( dotIndex + 1 );
			   }
			else
			   {
			   // was no extension
			   choppedFilename = name;
			   ext = "";
			   }
			return ext;
		}
	
	public Map<String, String> getContentTypes() {
		return contentTypes;
	}


	public void setContentTypes(Map<String, String> contentTypes) {
		this.contentTypes = contentTypes;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
