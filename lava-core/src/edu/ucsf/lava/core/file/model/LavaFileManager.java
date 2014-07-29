package edu.ucsf.lava.core.file.model;



import edu.ucsf.lava.core.file.FileManager;
import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.model.EntityManagerBase;
import edu.ucsf.lava.core.spring.LavaBeanUtils;

public class LavaFileManager extends EntityManagerBase{
	protected FileManager fileManager;
	
	
	
	public LavaFileManager(){
		super();
	}
	
	public LavaFile getFile(LavaFile file) throws FileAccessException {
		return this.getFileManager().getFile(file);
		}

	public void deleteFile(LavaFile file) throws FileAccessException {
		this.getFileManager().deleteFile(file);
	}
	
	public LavaFile saveFile(LavaFile file) throws FileAccessException {
		return this.getFileManager().saveFile(file);
	}

	public LavaFile saveOrUpdateFile(LavaFile file) throws FileAccessException {
		return this.getFileManager().saveOrUpdateFile(file);
	}

	protected FileManager getFileManager(){
		if(this.fileManager==null){ 
			this.fileManager = (FileManager) LavaBeanUtils.get(FileManager.FILE_MANAGER_NAME);
			}
		return this.fileManager;
	}
	
}


