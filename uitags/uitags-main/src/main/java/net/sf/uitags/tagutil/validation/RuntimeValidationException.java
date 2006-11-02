/**
 * Nov 28, 2004
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
package net.sf.uitags.tagutil.validation;


/**
 * Thrown to indicate an invalid usage of tag that can only be detected
 * during runtime.
 *
 * @see net.sf.uitags.tagutil.validation.TlvLeakageException
 * @author hgani
 * @version $Id$
 */
public class RuntimeValidationException extends RuntimeException {

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 52L;

  /**
   * See {@link RuntimeException#RuntimeException()}.
   */
  public RuntimeValidationException() {
    super();
  }

  /**
   * See {@link RuntimeException#RuntimeException(java.lang.String)}.
   *
   * @param message the detail message
   */
  public RuntimeValidationException(String message) {
    super(message);
  }

  /**
   * Contructs the exception object specifying the name of the tag that
   * has been misused.
   *
   * See {@link RuntimeException#RuntimeException(java.lang.String)}.
   * @param message the detail message
   * @param tagName the tag name
   */
  public RuntimeValidationException(String message, String tagName) {
    super(tagName + ": " + message);
  }

  /**
   * See
   * {@link RuntimeException#RuntimeException(java.lang.String, java.lang.Throwable)}.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public RuntimeValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * See
   * {@link RuntimeException#RuntimeException(java.lang.Throwable)}.
   *
   * @param cause the cause
   */
  public RuntimeValidationException(Throwable cause) {
    super(cause);
  }
}
