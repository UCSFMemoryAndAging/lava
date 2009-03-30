package edu.ucsf.lava.core.dao.file;

public class AlreadyExistsFileDaoException extends FileDaoException {

	public AlreadyExistsFileDaoException(String message, Throwable throwable) {
		super(message, throwable);

	}

	public AlreadyExistsFileDaoException(String message) {
		super(message);
	
	}

}
