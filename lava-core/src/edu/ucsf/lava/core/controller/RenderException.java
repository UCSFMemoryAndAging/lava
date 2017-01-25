package edu.ucsf.lava.core.controller;

/**
 * Exceptions that occur on a GET request to render the view are thrown and then presented to the user via the 
 * CustomExceptionResolver or CustomFlowExceptionHandler. If the exception is a RenderException then
 * those handlers know how to parse out the meaningful error text to display to the user instead of displaying
 * the exception stack trace.
 * 
 * @author ctoohey
 *
 */

public class RenderException extends RuntimeException {

  public RenderException(String message){
    super(message);
  }

  public RenderException(String message, Throwable throwable){
    super(message, throwable);
  }
}



