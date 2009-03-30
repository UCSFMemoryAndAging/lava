package edu.ucsf.lava.core.dao.file;

public class DeleteReadOnlyFileDaoException extends FileDaoException {

	public DeleteReadOnlyFileDaoException(String message, Throwable throwable) {
		super(message, throwable);

	}

	public DeleteReadOnlyFileDaoException(String message) {
		super(message);
	
	}

}
