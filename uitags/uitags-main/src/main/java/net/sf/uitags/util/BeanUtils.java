/**
 * Nov 23, 2004
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

import javax.servlet.jsp.JspException;

/**
 * Helper class that provides wrappers for Commons <code>BeanUtils</code>'s
 * methods. The wrapping methods free the calling code from having to handle
 * introspection-related checked-exceptions by wrapping them inside
 * <code>JspException</code>.
 *
 * @author jonni
 * @author hgani
 * @version $Id$
 */
public final class BeanUtils {
  /**
   * Non-instantiable by client.
   */
  private BeanUtils() {
    super();
  }

  /**
   * Wraps <code>BeanUtils</code>' method with the same name
   * and signature.
   *
   * @param bean the bean whose property value is to be returned
   * @param propertyName the name of the property whose value is to be retrieved
   * @return the value of the bean property
   * @throws JspException wraps exceptions thrown by <code>BeanUtils</code>
   */
  public static String getSimpleProperty(Object bean, String propertyName)
      throws JspException {
    try {
      return org.apache.commons.beanutils.BeanUtils.getSimpleProperty(
          bean, propertyName);
    }
    catch (Exception e) {
      throw new JspException(e);
    }
  }

  /**
   * Wraps <code>BeanUtils</code>' method with the same name
   * and signature.
   *
   * @param bean the bean whose property value is to be returned
   * @param propertyName the name of the property whose value is to be retrieved
   * @return the value of the bean property
   * @throws JspException wraps exceptions thrown by <code>BeanUtils</code>
   */
  public static String getProperty(Object bean, String propertyName)
      throws JspException {
    try {
      return org.apache.commons.beanutils.BeanUtils.getProperty(
          bean, propertyName);
    }
    catch (Exception e) {
      throw new JspException(e);
    }
  }
}
