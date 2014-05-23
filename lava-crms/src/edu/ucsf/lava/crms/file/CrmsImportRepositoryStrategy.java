package edu.ucsf.lava.crms.file;

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
	 to store definitionName and instrType so they are available here
	 
	 Plan right now is a single repo for both mapping and data files so will prepend
	 the above with "mapping" and "data" for mapping file and data file respectively
	 
	 also need to configure to use CrmsImportRepositoryStrategy at crms import level
	 rather than ImportRepositoryStrategy
	 String generateLocation(LavaFile lavaFile) throws FileAccessException {
		StringBuffer location = new StringBuffer();
		if (lavaFile.getContentType().equals(IMPORT_DATA_FILE_TYPE)) {
			location.append("data").append(SEPARATOR).append(crmsImportFile.getDefinitionName());
		}
		else if (lavaFile.getContentType().equals(IMPORT_DEF_MAPPING_FILE_TYPE)) {
			location.append("mapping").append(SEPARATOR).append(crmsImportFile.getInstrType());
		}
		location.append(File.separatorChar);
		return location.toString();
	}
	*/


}
