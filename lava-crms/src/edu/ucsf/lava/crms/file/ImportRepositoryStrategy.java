package edu.ucsf.lava.crms.file;

import java.io.File;

import edu.ucsf.lava.core.file.ArchivingLocalFileSystemRepositoryStrategy;
import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;

public class ImportRepositoryStrategy extends ArchivingLocalFileSystemRepositoryStrategy {

	public ImportRepositoryStrategy() {
		super();
	}

	public boolean handlesFile(LavaFile file) {
		if (file.getContentType().equals("Import Data File") 
				|| file.getContentType().equals("Import Template Mapping File")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public String generateFileId(LavaFile lavaFile) throws FileAccessException {
		
		// for import data files and import config mapping files the fileId can just be
		// the filename of the file, because no PHI in filename so do not need to hide it in 
		// the file system
		return lavaFile.getName();
	}
	
	public String generateLocation(LavaFile lavaFile) throws FileAccessException {
		StringBuffer location = new StringBuffer();
		if (lavaFile.getContentType().equals("Import Data File") 
				|| lavaFile.getContentType().equals("Import Definition Mapping File")) {
			location.append("data");
		}
		else {
			location.append("mapping");
		}
		location.append(File.separatorChar);
		return location.toString();
	}
	
	
}
