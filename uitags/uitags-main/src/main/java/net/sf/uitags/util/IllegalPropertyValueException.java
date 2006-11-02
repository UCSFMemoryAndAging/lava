/**
 * Nov 22, 2004
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
 * Thrown when an illegal property value is encountered.
 *
 * @author jonni
 * @version $Id$
 */
public final class IllegalPropertyValueException extends RuntimeException {
  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 52L;

  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The name of the property whose value is illegal
   */
  private String propKey;
  /**
   * The offending value
   */
  private String propValue;
  /**
   * The reason the property is illegal
   */
  private String reason;



  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * See {@link RuntimeException#RuntimeException()}.
   */
  public IllegalPropertyValueException() {
    super();
  }

  /**
   * See {@link RuntimeException#RuntimeException(java.lang.String)}.
   *
   * @param message the detail message
   */
  public IllegalPropertyValueException(String message) {
    super(message);
  }

  /**
   * See {@link RuntimeException#RuntimeException(java.lang.Throwable)}.
   *
   * @param cause the cause
   */
  public IllegalPropertyValueException(Throwable cause) {
    super(cause);
  }

  /**
   * See
   * {@link RuntimeException#RuntimeException(java.lang.String, java.lang.Throwable)}.
   *
   * @param message the detail message
   * @param cause   the cause
   */
  public IllegalPropertyValueException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates an instance, setting the name and value of the offending
   * property, and a message explaining why it's illegal.
   *
   * @param propKey   the name of the property whose value is illegal
   * @param propValue the offending value
   * @param reason    the reason the property is illegal
   */
  public IllegalPropertyValueException(
      String propKey, String propValue, String reason) {
    super("Illegal value for property '" + propKey + "': '" + propValue + "'.");
    this.propKey = propKey;
    this.propValue = propValue;
    this.reason = reason;
  }



  /////////////////////////////
  ////////// Methods //////////
  /////////////////////////////

  /**
   * Returns the name of the property whose value is illegal.
   *
   * @return the name of the property whose value is illegal
   */
  public String getPropertyKey() {
    return this.propKey;
  }

  /**
   * Returns the offending value.
   *
   * @return the offending value
   */
  public String getPropertyValue() {
    return this.propValue;
  }

  /**
   * Returns the reason why the property value is illegal.
   *
   * @return the reason why the property value is illegal
   */
  public String getReason() {
    return this.reason;
  }
}
