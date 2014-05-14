package edu.ucsf.lava.core.importer;

import edu.ucsf.lava.core.file.ImportRepositoryStrategy;

public class CrmsImportRepositoryStrategy extends ImportRepositoryStrategy {
	
	public CrmsImportRepositoryStrategy() {
		super();
	}

	/*
	TODO: re-implement generateLocation to do the following
	mapping file: add definition name as dir name
	data file: add instrtype as dir name (w no spaces?)
	To do this will need create a CrmsImportFile class with non-persistent fields
	 to store definitionName and instrType
	 
	 also need to configure to use CrmsImportRepositoryStrategy at crms import level
	 rather than ImportRepositoryStrategy
	 ng generateLocation(LavaFile lavaFile) throws FileAccessException {
		StringBuffer location = new StringBuffer();
		if (lavaFile.getContentType().equals(IMPORT_DATA_FILE_TYPE)) {
			location.append("data");
		}
		else if (lavaFile.getContentType().equals(IMPORT_DEF_MAPPING_FILE_TYPE)) {
			location.append("mapping");
		}
		location.append(File.separatorChar);
		return location.toString();
	}
	*/


}
