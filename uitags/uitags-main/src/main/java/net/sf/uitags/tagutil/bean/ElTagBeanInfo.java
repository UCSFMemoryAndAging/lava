/**
 * Dec 9, 2004
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A class that adapts {@link net.sf.uitags.tagutil.bean.TagBeanInfo}
 * for EL support.
 *
 * @author jonni
 * @version $Id$
 */
public abstract class ElTagBeanInfo extends TagBeanInfo {
  /**
   * If more than one setter under the same name exist, this method chooses
   * one that deals with <code>String</code>. Tag handlers with EL support
   * may overload setters defined in the parent with ones that take
   * <code>String</code>. This method explicitly hides those overloaded
   * setters.
   *
   * @return setter methods of the tag handler (list of <code>Method</code>s)
   * @throws RuntimeException if the tag handler class was not found
   */
  List getTagHandlerSetters() {
    List methods = super.getTagHandlerSetters();

    // Map key is the method name
    Map methodMap = new HashMap();
    for (Iterator i = methods.iterator(); i.hasNext(); ) {
      Method currMethod = (Method) i.next();
      String methodName = currMethod.getName();

      Method similarlyNamed = (Method) methodMap.get(methodName);
      // If method under the same name hasn't existed, add to map
      if (similarlyNamed == null) {
        methodMap.put(methodName, currMethod);
      }
      // If method under the same name exists, we want one that deals
      // with String object
      else {
        if (currMethod.getParameterTypes()[0].equals(String.class)) {
          methodMap.put(methodName, currMethod);
        }
      }
    }

    return new ArrayList(methodMap.values());
  }
}
