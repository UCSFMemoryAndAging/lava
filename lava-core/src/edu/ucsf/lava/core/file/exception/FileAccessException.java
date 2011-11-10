package edu.ucsf.lava.core.file.exception;

import edu.ucsf.lava.core.file.model.LavaFile;


/**
 * A base exception class for errors that arise when interacting with 
 * file repositories.  
 * 
 * @author jhesse
 *
 */
public class FileAccessException extends RuntimeException {
	
	 	  public FileAccessException(String message){
		    super(message);
		  }
	
	 	  public FileAccessException(String message, LavaFile file){
			  super(formatMessage(message,file));
	 	  }
	
	 	  
	 	  public FileAccessException(String message, LavaFile file, Throwable throwable){
			    super(formatMessage(message,file), throwable);
		  }
	 	  
		  public FileAccessException(String message, Throwable throwable){
		    super(message, throwable);
		  }
		  
		  public static String formatMessage(String message,LavaFile file){
			  return new StringBuffer(message).append("\n").append(file.toLogString()).toString();
			  	
		  }
		  
	}
