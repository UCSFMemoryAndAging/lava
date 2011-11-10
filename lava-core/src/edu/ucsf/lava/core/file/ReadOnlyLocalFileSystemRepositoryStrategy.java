package edu.ucsf.lava.core.file;

import edu.ucsf.lava.core.file.exception.DeleteReadOnlyFileAccessException;
import edu.ucsf.lava.core.file.exception.SaveReadOnlyFileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;



public class ReadOnlyLocalFileSystemRepositoryStrategy extends LocalFileSystemRepositoryStrategy {

	
	public void deleteFile(LavaFile file) throws DeleteReadOnlyFileAccessException {
		this.logRepositoryError("Attempt to delete read-only resource from repository:", file);
		throw new DeleteReadOnlyFileAccessException("Attempt to delete read-only resource from repository",file);
		}

	public LavaFile saveFile(LavaFile file) throws SaveReadOnlyFileAccessException {
		this.logRepositoryError("Attempt to save to a read-only resource from repository:", file);
		throw new SaveReadOnlyFileAccessException("Attempt to save to a read-only resource from repository.",file);
		}

	public LavaFile saveOrUpdateFile(LavaFile file) throws SaveReadOnlyFileAccessException {
		this.logRepositoryError("Attempt to save to a read-only resource from repository:", file);
		throw new SaveReadOnlyFileAccessException("Attempt to save to a read-only resource from repository.", file);
	}

	
}
