package net.sf.uitags.util;


import javax.servlet.jsp.JspException;

/**
 * Helper class that provides wrappers for Commons <code>PropertyUtils
 * </code>'s methods. The wrapping methods free the calling code from having
 * to handle introspection-related checked-exceptions by wrapping them
 * inside <code>JspException</code>.
 *
 * @author hgani
 * @version $Id$
 */
public final class PropertyUtils {
  /**
   * Non-instantiable by client.
   */
  private PropertyUtils() {
    super();
  }

  /**
   * Wraps <code>PropertyUtils</code>' method with the same name
   * and signature.
   *
   * @param bean the bean whose property value is to be returned
   * @param propertyName the name of the property whose value is to be retrieved
   * @return the value of the bean property
   * @throws JspException wraps exceptions thrown by <code>PropertyUtils</code>
   */
  public static Object getSimpleProperty(Object bean, String propertyName)
      throws JspException {
    try {
      return org.apache.commons.beanutils.PropertyUtils.getSimpleProperty(
          bean, propertyName);
    }
    catch (Exception e) {
      throw new JspException(e);
    }
  }

  /**
   * Wraps <code>PropertyUtils</code>' method with the same name
   * and signature.
   *
   * @param bean the bean whose property value is to be returned
   * @param propertyName the name of the property whose value is to be retrieved
   * @return the value of the bean property
   * @throws JspException wraps exceptions thrown by <code>PropertyUtils</code>
   */
  public static Object getProperty(Object bean, String propertyName)
      throws JspException {
    try {
      return org.apache.commons.beanutils.PropertyUtils.getProperty(
          bean, propertyName);
    }
    catch (Exception e) {
      throw new JspException(e);
    }
  }
}
