package edu.ucsf.lava.crms.file;

import edu.ucsf.lava.core.file.ImportRepositoryStrategy;

public class CrmsImportRepositoryStrategy extends ImportRepositoryStrategy {
	
	public CrmsImportRepositoryStrategy() {
		super();
	}

	/*
	TODO: override or augment generateLocation to do the following
	data file: add instrtype||instrVer as dir name (w no spaces?)
	To do this will need create a CrmsImportDefinitionFile class with non-persistent fields
	 to store instrType,instrVer just as ImportDefinitionFile stores the definitionName
	 
	 CrmsImportDefinitionHandler will have to override initializeNewCommandInstance to
	 instantiate CrmsImportDefinitionFile
	 CrmsImportDefinitionHandler will have to override saveAddFileCallback AND saveFileCallback to save instrType
	 and instrVer in the CrmsImportDefinitionFile
	 
	 Plan right now is a single repo for both mapping and data files so will prepend
	 the above with "mapping" and "data" for mapping file and data file respectively
	 NOTE: ImportRepositoryStrategy generateLocation is doing this
	 
	 also need to configure to use CrmsImportRepositoryStrategy at crms import level
	 rather than ImportRepositoryStrategy
	 String generateLocation(LavaFile lavaFile) throws FileAccessException {
		StringBuffer location = new StringBuffer(super.generateLocation());
		if (lavaFile.getContentType().equals(IMPORT_DATA_FILE_TYPE)) {
			String instrTypeEncoded = Instrument.getInstrTypeEncoded(
			 ((CrmsImportDefinitionFile)lavaFile).getInstrType(), 
			 ((CrmsImportDefinitionFile)lavaFile).getInstrVer()); 
			location.append(instrTypeEncoded);
			location.append(File.separatorChar);
		}
		return location.toString();
	}
	*/


}
