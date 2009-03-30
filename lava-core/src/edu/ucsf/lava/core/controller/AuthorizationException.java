package edu.ucsf.lava.core.controller;

public class AuthorizationException extends RuntimeException {

  public AuthorizationException(String message){
    super(message);
  }

  public AuthorizationException(String message, Throwable throwable){
    super(message, throwable);
  }
}



