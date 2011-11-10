package edu.ucsf.lava.core.file.exception;

import edu.ucsf.lava.core.file.model.LavaFile;


public class SaveReadOnlyFileAccessException extends FileAccessException {

	public SaveReadOnlyFileAccessException(String message, LavaFile file,
			Throwable throwable) {
		super(message, file, throwable);
	}

	public SaveReadOnlyFileAccessException(String message, LavaFile file) {
		super(message, file);
	}

	public SaveReadOnlyFileAccessException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public SaveReadOnlyFileAccessException(String message) {
		super(message);
	}

	
}
