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
 * Thrown to indicate runtime error that could have been detected at
 * compile-time, but due to some design constraints, validation has to
 * be deferred to runtime.
 *
 * @see net.sf.uitags.tagutil.validation.RuntimeValidationException
 * @see net.sf.uitags.tagutil.validation.TlvLeakageException
 * @author hgani
 * @version $Id$
 */
public class DeferredValidationException extends TlvLeakageException {

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 100L;

  /**
   * See {@link TlvLeakageException#TlvLeakageException()}.
   */
  public DeferredValidationException() {
    super();
  }

  /**
   * See {@link TlvLeakageException#TlvLeakageException(java.lang.String)}.
   *
   * @param message the detail message
   */
  public DeferredValidationException(String message) {
    super(message);
  }

  /**
   * See
   * {@link TlvLeakageException#TlvLeakageException(java.lang.String, java.lang.Throwable)}.
   *
   * @param message the detail message
   * @param cause the cause
   */
  public DeferredValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * See
   * {@link TlvLeakageException#TlvLeakageException(java.lang.Throwable)}.
   *
   * @param cause the cause
   */
  public DeferredValidationException(Throwable cause) {
    super(cause);
  }
}
