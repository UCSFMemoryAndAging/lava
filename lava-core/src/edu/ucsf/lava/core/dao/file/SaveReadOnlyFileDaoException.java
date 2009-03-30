package edu.ucsf.lava.core.dao.file;

public class SaveReadOnlyFileDaoException extends FileDaoException {

	public SaveReadOnlyFileDaoException(String message, Throwable throwable) {
		super(message, throwable);

	}

	public SaveReadOnlyFileDaoException(String message) {
		super(message);
	
	}

}
