package edu.ucsf.lava.core.file.exception;

import edu.ucsf.lava.core.file.model.LavaFile;


public class AlreadyExistsFileAccessException extends FileAccessException {

	public AlreadyExistsFileAccessException(String message, LavaFile file,
			Throwable throwable) {
		super(message, file, throwable);
		
	}

	public AlreadyExistsFileAccessException(String message, LavaFile file) {
		super(message, file);
		
	}

	public AlreadyExistsFileAccessException(String message, Throwable throwable) {
		super(message, throwable);
		
	}

	public AlreadyExistsFileAccessException(String message) {
		super(message);
		
	}

		

}
