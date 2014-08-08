package edu.ucsf.lava.crms.file;

import java.io.File;

/**
 * Applications will typically want to configure application specific Spring beans for
 * repository and respositoryStrategy to define rootPath and archiveRoot properties 
 * specific to the application. These beans are already defined in lava-crms crms-file.xml so
 * those definitions can be referenced by the application definitions.
 * 
 * 
 * The repository bean can use a lava-core Repository class (e.g. BaseFileRepository) by designating
 * the "crmsFileRepository" bean as a parent and only defining the "strategy" property to be
 * the application defined strategy bean.
 * 
 * The strategy bean can use this class, CrmsRepositoryStrategy, by designating "crmsRepositoryStrategy"
 * bean as a parent and defining the "rootPath" and "archiveRoot" properties.
 * 
 * Typically the only need to subclass this class in the application would be if the application
 * had a different file naming and directory path naming setup. generateLocation would be overridden to
 * define a new directory path naming system, and generateFileId would be overridden to define
 * a new file naming system.
 */

import edu.ucsf.lava.core.file.ArchivingLocalFileSystemRepositoryStrategy;
import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.crms.file.model.CrmsFile;

public class CrmsRepositoryStrategy extends ArchivingLocalFileSystemRepositoryStrategy {
	public static String CRMS_REPOSITORY_ID = "lava_crms";

	public CrmsRepositoryStrategy() {
		super();
	}

	/**
     * FileManager supports a collection of repositories so when FileManager calls
	 * getRepository(file) to obtain the repository for a given file, FileRepositories
	 * iterates thru all of the repositories calling handlesFile on each one to 
	 * determine which repository to use for the file operation.
	 **/ 
	public boolean handlesFile(LavaFile file) {
		if (file.getRepositoryId().equals(CRMS_REPOSITORY_ID)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Utilize the lava-core LocalFileSystemRepositoryStrategy generateLocation, but
	 * prepend with a folder indicating the type of entity under which files of that
	 * type are stored.
	 */

	public String generateLocation(LavaFile lavaFile) throws FileAccessException {
		StringBuffer path = new StringBuffer();
		String subLocation = super.generateLocation(lavaFile);
		CrmsFile crmsFile = (CrmsFile) lavaFile;
		if (crmsFile.getInstrId() != null) {
			path.append("instr").append(File.separatorChar);
		}
		else if (crmsFile.getVisitId() != null) {
			path.append("visit").append(File.separatorChar);
		}
		else if (crmsFile.getEnrollStatId() != null) {
			path.append("enroll").append(File.separatorChar);
		}
		else if (crmsFile.getConsentId() != null) {
			path.append("consent").append(File.separatorChar);
		}
		else if (crmsFile.getPidn() != null) {
			path.append("patient").append(File.separatorChar);
		}
		path.append(subLocation);
		return path.toString();
	}
	
}
