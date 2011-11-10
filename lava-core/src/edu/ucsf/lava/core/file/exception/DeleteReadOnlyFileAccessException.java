package edu.ucsf.lava.core.file.exception;

import edu.ucsf.lava.core.file.model.LavaFile;


public class DeleteReadOnlyFileAccessException extends FileAccessException {

	public DeleteReadOnlyFileAccessException(String message, LavaFile file,
			Throwable throwable) {
		super(message, file, throwable);
	}

	public DeleteReadOnlyFileAccessException(String message, LavaFile file) {
		super(message, file);
	}

	public DeleteReadOnlyFileAccessException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public DeleteReadOnlyFileAccessException(String message) {
		super(message);
	}

	

}
