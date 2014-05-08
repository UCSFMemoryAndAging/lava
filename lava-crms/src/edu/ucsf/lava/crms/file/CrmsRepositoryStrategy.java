package edu.ucsf.lava.crms.file;

import edu.ucsf.lava.core.file.ArchivingLocalFileSystemRepositoryStrategy;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.crms.file.model.CrmsFile;

public class CrmsRepositoryStrategy extends ArchivingLocalFileSystemRepositoryStrategy {

	public CrmsRepositoryStrategy() {
		super();
	}

	/**
     * FileManager supports a collection of repositories so when FileManager calls
	 * getRepository(file) to obtain the repository for a given file, FileRepositories
	 * iterates thru all of the repositories calling handlesFile on each one to 
	 * determine which repository to use for the file operation.
	 * 
	 * Here, handle all CrmsFiles. Could instead subclass this strategy class and use other criteria such 
	 * as testing against file repositoryId (which would have to be assigned on CrmsFile instantiation) or
	 * test against file contentType.
	 **/ 
	public boolean handlesFile(LavaFile file) {
		if (file instanceof CrmsFile) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * TODO: subclass this class with an application level repository strategy and override generateLocation 
	 * to create an organizational directory structure for the repository (in conjunction with the Spring bean
	 * settings for the Strategy rootPath and archiveRoot), and override generateFileId for file naming.
	 * 
	 * The implementation of generateLocation and generateFileId in LocalFileSystemRepositoryStrategy may 
	 * suffice, however may want to organize directories by module ("patient", "enrollment", "visit" and 
	 * "instrument") and below that a directory named after PIDN, to which uploaded files are written. Repo 
	 * files could have the same name they have in the user's file system. But e.g. if the filenames 
	 * themselves contain PHI, could go with LocalFileSystemRepository generateFileId method which generates 
	 * filenames.  
	 * 
	 * At a minimum, will want to define repository and strategy beans in the application using existing
	 * classes (e.g. BaseFileRepository and ArchivingLocalFileSystemRepositoryStrategy, respectively) to
	 * set the rootPath and archiveRoot properties on the strategy bean based on where files should be stored 
	 * in your system.
	 */

	
}
