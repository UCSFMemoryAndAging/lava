package edu.ucsf.lava.core.file.exception;

import edu.ucsf.lava.core.file.model.LavaFile;

public class FileNotFoundException extends FileAccessException {

	public FileNotFoundException(String message) {
		super(message);
	}

	public FileNotFoundException(String message, LavaFile file) {
		super(message, file);
	}

	public FileNotFoundException(String message, LavaFile file,
			Throwable throwable) {
		super(message, file, throwable);
	}

	public FileNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
