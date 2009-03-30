package edu.ucsf.lava.core.dao.file;



public class ReadOnlyFileSystemRepository extends SimpleFileSystemRepository {

	
	public void delete(String fileId) throws DeleteReadOnlyFileDaoException{
		if (!fileId.equals("")) {
			throw new DeleteReadOnlyFileDaoException("Attempt to delete read-only resource: FileId: " + fileId);
		}
	}

	public LavaFile store(LavaFile file, boolean overwrite) throws SaveReadOnlyFileDaoException {
		throw new SaveReadOnlyFileDaoException("Attempt to save to a read-only resource: File: " + file.getName());
	}

	public LavaFile store(LavaFile file) throws SaveReadOnlyFileDaoException {
		throw new SaveReadOnlyFileDaoException("Attempt to save to a read-only resource: File: " + file.getName());
	}

	
}
