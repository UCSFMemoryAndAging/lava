package edu.ucsf.lava.core.file.exception;

import edu.ucsf.lava.core.file.model.LavaFile;

public class NoFileSelectedException extends FileAccessException {

	public NoFileSelectedException(String message) {
		super(message);
	}

	public NoFileSelectedException(String message, LavaFile file) {
		super(message, file);
	}

	public NoFileSelectedException(String message, LavaFile file,
			Throwable throwable) {
		super(message, file, throwable);
	}

	public NoFileSelectedException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
