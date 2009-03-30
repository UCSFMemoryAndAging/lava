package edu.ucsf.lava.core.dao.file;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.FileCopyUtils;


public class ArchivingFileSystemRepository extends SimpleFileSystemRepository {
	public static final int ARCHIVE_METHOD_APPEND_DATE = 1;
	protected String archiveRoot = "";
	protected int archiveMethod = ARCHIVE_METHOD_APPEND_DATE;
	
	
	
	protected void archiveFile(String fileId) throws FileDaoException{
		try{
			File file = getFileObject(fileId);
			File archiveFile = getFileObject(getArchivePath(),getArchiveFileName(getFilePath(fileId)),false);
			FileCopyUtils.copy(file, archiveFile);
		}catch(Exception e){
			throw new FileDaoException("There was a problem archiving the file: " + fileId,e);
		}
	}
	
	
	protected String getArchivePath(){
		return this.archiveRoot;  
	}
	
	protected String getArchiveFileName(String fileName){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyddmm_HHmmss");
		
		if(archiveMethod == ARCHIVE_METHOD_APPEND_DATE){
			StringBuffer archiveName = new StringBuffer(fileName).append("_")
									.append(dateFormat.format(new Date()));
			return archiveName.toString();
		}
		return fileName;
	}
	public void delete(String fileId, boolean archive) throws FileDaoException{
		if(archive == false){
			super.delete(fileId);
		}
		this.delete(fileId);
	}
	
	public void delete(String fileId) throws FileDaoException{
		archiveFile(fileId);
		super.delete(fileId);
	}



	public LavaFile store(LavaFile file, boolean overwrite) throws FileDaoException {
		try
		{
			//try to save without overwrite turned on.  This will raise an exception if
			//the existing file needs to be archived
			return super.store(file, false);
		}catch(AlreadyExistsFileDaoException e){
			//there is an existing file so pass the error if overwrite is false
			if(overwrite == false){throw e;}
			//otherwise, lets archive the file
			archiveFile(file.getName());
		}
		//now store the new file
		return super.store(file,overwrite);
		
	}


	public String getArchiveRoot() {
		return archiveRoot;
	}


	public void setArchiveRoot(String archiveRoot) {
		this.archiveRoot = archiveRoot;
	}
	
	
}
