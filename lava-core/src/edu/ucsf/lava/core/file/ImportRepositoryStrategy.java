package edu.ucsf.lava.core.file;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.ucsf.lava.core.file.ArchivingLocalFileSystemRepositoryStrategy;
import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.ImportFile;
import edu.ucsf.lava.core.file.model.LavaFile;

/**
 * Applications will typically want to configure application specific Spring beans for
 * repository and respositoryStrategy to define rootPath and archiveRoot properties 
 * specific to the application. These beans are already defined in lava-core core-import.xml so
 * those definitions can be referenced by the application definitions.
 * 
 * The repository bean can use the lava-core Repository class (e.g. BaseFileRepository) by designating
 * the "importLavaFileRepository" bean as a parent and only defining the "strategy" property to be
 * the application defined strategy bean.
 * 
 * The strategy bean can use this class, ImportRepositoryStrategy, by designating "importRepositoryStrategy"
 * bean as a parent and defining the "rootPath" and "archiveRoot" properties.
 * 
 * Typically the only need to subclass this class in the application would be if the application
 * had a different file naming and directory path naming setup. generateLocation would be overridden to
 * define a new directory path naming system, and generateFileId would be overridden to define
 * a new file naming system.
 * 
 * @author ctoohey
 *
 */

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
		// import definition mapping files can just retain their name since there should only be one
		// instance of such a file (there may be multiple edits of the file over time in which case
		// the file is replaced in the definition and the prior versions are archived)
		
		// in contrast, import data files may have the same name but different data (or incremental
		// data additions), so these files should have unique names, so append a timestamp after
		// their name
		StringBuffer filename = new StringBuffer(lavaFile.getName());
		if (lavaFile.getContentType().equals(IMPORT_DATA_FILE_TYPE)) {
			DateFormat formatter = new SimpleDateFormat("yyyymmdd_HHmmss_SSS", Locale.getDefault());
			filename.append("_").append(formatter.format(new Date()));
		}
		return filename.toString();
	}
	
	public String generateLocation(LavaFile lavaFile) throws FileAccessException {
		String definitionNameEncoded = ((ImportFile)lavaFile).getDefinitionName().replaceAll("[^a-zA-Z0-9]","").toLowerCase();
		StringBuffer location = new StringBuffer(definitionNameEncoded).append(File.separatorChar);
		// mapping files and data files go directly under a folder named after the definition
		location.append(File.separatorChar);
		return location.toString();
	}
	
	
}
