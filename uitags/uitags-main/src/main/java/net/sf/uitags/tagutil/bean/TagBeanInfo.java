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
package net.sf.uitags.tagutil.bean;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides explicit JavaBean information about a particular tag handler.
 * Tag handlers whose JavaBean properties are to be explicitly
 * described should have a <code>BeanInfo</code> class defined.
 * It's enough to simply have such <code>BeanInfo</code> class extend
 * this class without providing further implementation, leaving the child
 * class empty. The name of the child class must be the same as the name of
 * the tag handler it describes and end with the string "BeanInfo".
 *
 * @see #getPropertyDescriptors()
 * @author jonni
 * @version $Id$
 */
public abstract class TagBeanInfo extends SimpleBeanInfo {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /**
   * The number of characters in the string "BeanInfo": 8!
   */
  private static final int BEANINFO_CHAR_COUNT = 8;

  /**
   * The string that prefixes the name of setter methods.
   */
  private static final String SETTER_PREFIX = "set";

  /**
   * The length of <code>SETTER_PREFIX</code>.
   */
  private static final int SETTER_PREFIX_LENGTH = SETTER_PREFIX.length();

  /**
   * How many parameters does a setter method have?
   */
  private static final int SETTER_PARAM_COUNT = 1;



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * Logger
   */
  private static Log log = LogFactory.getLog(TagBeanInfo.class);



  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public TagBeanInfo() {
    super();
  }



  ////////////////////////////////////////
  ////////// Overridden methods //////////
  ////////////////////////////////////////

  /**
   * Exposes only the setter methods. This is necessary to hide tag
   * handlers' <code>Object.getClass</code> method. Without this, it's
   * impossible to have a tag attribute called "class".
   *
   * @return descriptor for the setter methods
   */
  // This implementation depends on {@link #getTagHandlerSetters()} to
  // get a list of <code>Method</code>s to convert to
  // <code>PropertyDescriptor</code>s.
  public final PropertyDescriptor[] getPropertyDescriptors() {
    List methods = getTagHandlerSetters();

    List props = new ArrayList();
    for (Iterator i = methods.iterator(); i.hasNext(); ) {
      props.add(createPropertyDescriptor((Method) i.next()));
    }

    if (log.isDebugEnabled()) {
      log.debug("Property descriptors: '" + props + "'.");
    }

    return (PropertyDescriptor[])
       props.toArray(new PropertyDescriptor[props.size()]);
  }



  ////////////////////////////////////
  ////////// Helper methods //////////
  ////////////////////////////////////

  /**
   * Returns setter methods of the tag handler.
   *
   * @return setter methods of the tag handler (list of <code>Method</code>s)
   * @throws RuntimeException if the tag handler class was not found
   */
  List getTagHandlerSetters() {
    String beanInfoClassName = getClass().getName();

    // Name of the tag handler is without the trailing "BeanInfo" string
    String tagHandlerClassName = beanInfoClassName.substring(
        0, beanInfoClassName.length() - BEANINFO_CHAR_COUNT);

    Class tagHandlerClass;
    try {
      tagHandlerClass = Class.forName(tagHandlerClassName);
    }
    catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    Method[] methods = tagHandlerClass.getMethods();
    List setters = new ArrayList();
    for (int i = 0; i < methods.length; i++) {
      if (isSetter(methods[i])) {
        setters.add(methods[i]);
      }
    }

    return setters;
  }

  /**
   * Returns <code>true</code> if the supplied method is a setter method.
   *
   * @param method the method to test
   * @return <code>true</code> if the supplied method is a setter method,
   *     <code>false</code> otherwise
   */
  private boolean isSetter(Method method) {
    String methodName = method.getName();
    return methodName.length() > SETTER_PREFIX_LENGTH &&
        methodName.startsWith(SETTER_PREFIX) &&
        Character.isUpperCase(methodName.charAt(SETTER_PREFIX_LENGTH)) &&
        method.getParameterTypes().length == SETTER_PARAM_COUNT;
  }

  /**
   * Returns a property descriptor for the given <i>setter</i> method.
   *
   * @param setterMethod the setter method for which a property descriptor is
   *     to be created
   * @return a property descriptor for the supplied method
   * @throws RuntimeException if introspection failed
   */
  private PropertyDescriptor createPropertyDescriptor(Method setterMethod) {
    String methodName = setterMethod.getName();
    String propName   = String.valueOf(
        Character.toLowerCase(methodName.charAt(SETTER_PREFIX_LENGTH))) +
        methodName.substring(SETTER_PREFIX_LENGTH + 1);
    try {
      return new PropertyDescriptor(propName, null, setterMethod);
    }
    catch (IntrospectionException e) {
      throw new RuntimeException(e);
    }
  }
}