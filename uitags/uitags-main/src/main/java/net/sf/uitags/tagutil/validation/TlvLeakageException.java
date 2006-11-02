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
 * Thrown to indicate that an error should have been caught by
 * TLV (Tag Library Validator) but somehow leaks into being a runtime
 * error. Throw this when you detect an error that you feel should have
 * been dealt with by TLV.
 *
 * @see net.sf.uitags.tagutil.validation.RuntimeValidationException
 * @author hgani
 * @version $Id$
 */
public class TlvLeakageException extends RuntimeException {

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 52L;

  /**
   * See {@link RuntimeException#RuntimeException()}.
   */
  public TlvLeakageException() {
    super();
  }

  /**
   * See {@link RuntimeException#RuntimeException(java.lang.String)}.
   *
   * @param message the detail message
   */
  public TlvLeakageException(String message) {
    super(message);
  }

  /**
   * See
   * {@link RuntimeException#RuntimeException(java.lang.String, java.lang.Throwable)}.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public TlvLeakageException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * See
   * {@link RuntimeException#RuntimeException(java.lang.Throwable)}.
   *
   * @param cause the cause
   */
  public TlvLeakageException(Throwable cause) {
    super(cause);
  }

  /** {@inheritDoc} */
  public String toString() {
    return super.toString();
  }
}
