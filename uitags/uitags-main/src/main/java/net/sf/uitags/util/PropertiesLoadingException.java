/**
 * Nov 12, 2004
 *
 * Copyright 2004 uitags
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.uitags.util;


/**
 * Thrown to indicate that a properties file could not be loaded.
 *
 * @author jonni
 * @version $Id$
 */
public final class PropertiesLoadingException extends RuntimeException {
  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 52L;

  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The name of the file which could not be loaded
   */
  private String filename;



  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * See {@link RuntimeException#RuntimeException()}.
   */
  public PropertiesLoadingException() {
    super();
  }

  /**
   * See {@link RuntimeException#RuntimeException(java.lang.String)}.
   *
   * @param message the detail message
   */
  public PropertiesLoadingException(String message) {
    super(message);
  }

  /**
   * See {@link RuntimeException#RuntimeException(java.lang.Throwable)}.
   *
   * @param cause the cause
   */
  public PropertiesLoadingException(Throwable cause) {
    super(cause);
  }

  /**
   * See
   * {@link RuntimeException#RuntimeException(java.lang.String, java.lang.Throwable)}.
   *
   * @param message the detail message
   * @param cause   the cause
   */
  public PropertiesLoadingException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates an instance, setting the cause and the name of the file
   * which could not be loaded.
   *
   * @param cause    the cause
   * @param filename the name of the file which could not be loaded
   */
  public PropertiesLoadingException(Throwable cause, String filename) {
    super("Failed to load '" + filename + "'.", cause);
    this.filename = filename;
  }



  /////////////////////////////
  ////////// Methods //////////
  /////////////////////////////

  /**
   * Returns the name of the file which could not be loaded.
   *
   * @return the name of the file which could not be loaded
   */
  public String getFilename() {
    return this.filename;
  }
}
