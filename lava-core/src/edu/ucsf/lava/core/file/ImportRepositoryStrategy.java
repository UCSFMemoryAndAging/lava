package edu.ucsf.lava.core.file;

import java.io.File;

import edu.ucsf.lava.core.file.ArchivingLocalFileSystemRepositoryStrategy;
import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.ImportDefinitionMappingFile;
import edu.ucsf.lava.core.file.model.LavaFile;

public class ImportRepositoryStrategy extends ArchivingLocalFileSystemRepositoryStrategy {
	public static String IMPORT_REPOSITORY_ID = "import_repository";
	public static String IMPORT_DATA_FILE_TYPE = "Import Data File";
	public static String IMPORT_DEF_MAPPING_FILE_TYPE = "Import Definition Mapping File";

	public ImportRepositoryStrategy() {
		super();
	}

	/**
     * FileManager supports a collection of repositories so when FileManager calls
	 * getRepository(file) to obtain the repository for a given file, FileRepositories
	 * iterates thru all of the repositories calling handlesFile on each one to 
	 * determine which repository to use for the file operation.
	 **/ 
	public boolean handlesFile(LavaFile file) {
		if (file.getRepositoryId().equals(IMPORT_REPOSITORY_ID)) {
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
		if (lavaFile.getContentType().equals(IMPORT_DATA_FILE_TYPE)) {
			location.append("data");
		}
		else if (lavaFile.getContentType().equals(IMPORT_DEF_MAPPING_FILE_TYPE)) {
			String definitionNameEncoded = ((ImportDefinitionMappingFile)lavaFile).getDefinitionName().replaceAll("[^a-zA-Z0-9]","").toLowerCase();
			location.append("mapping").append(File.separatorChar).append(definitionNameEncoded);
		}
		location.append(File.separatorChar);
		return location.toString();
	}
	
	
}
