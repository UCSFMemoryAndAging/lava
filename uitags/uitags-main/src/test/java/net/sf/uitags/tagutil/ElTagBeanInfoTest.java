/**
 * Jun 27, 2005
 *
 * Copyright 2004 - 2005 uitags
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
package net.sf.uitags.tagutil;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;
import java.util.TreeMap;

import net.sf.uitags.tagutil.bean.ElTagBeanInfo;

import junit.framework.TestCase;

/**
 * Tests {@link net.sf.uitags.tagutil.bean.ElTagBeanInfo}.
 *
 * @author jonni
 * @version $Id$
 */
public final class ElTagBeanInfoTest extends TestCase {
  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(ElTagBeanInfoTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  /**
   * Ensures that {@link ElTagBeanInfo#getTagHandlerSetters()} prioritizes
   * <code>String</code> properties over non-<code>String</code> properties
   * if they are of the same name.
   *
   * @throws Exception if an introspection error occurs
   */
  public void testGetTagHandlerSetters() throws Exception {
    BeanInfo beanInfo = Introspector.getBeanInfo(BeanUnderTest.class);
    PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
    Map sortedProps = mapPropertyDescriptors(props);

    // prop0 is an overloaded property, the String type should be favored
    PropertyDescriptor prop0 = ((PropertyDescriptor) sortedProps.get("prop0"));
    assertEquals(prop0.getPropertyType(), String.class);

    // Make sure other properties are successfully read
    assertNotNull(sortedProps.get("prop1"));
    assertNotNull(sortedProps.get("prop2"));

    // We should have 3 properties whose name starts with "prop"
    assertTrue(sortedProps.size() == 3);
  }

  /**
   * Places supplied <code>PropertyDescriptor</code> into a <code>Map</code>,
   * keyed by property name.
   *
   * @param props the property descriptors to be stored
   * @return a <code>Map</code> containing <code>PropertyDescriptor</code>s
   */
  private Map mapPropertyDescriptors(PropertyDescriptor[] props) {
    Map ret = new TreeMap();
    for (int i = 0; i < props.length; i++) {
      if (props[i].getName().startsWith("prop")) {
        ret.put(props[i].getName(), props[i]);
      }
    }

    return ret;
  }

  /**
   * The bean class to be introspected.
   */
  public static final class BeanUnderTest {
    public void setProp0(Long nonStringProp) {
      // no implementation needed
    }
    public void setProp0(String stringProp) {
      // no implementation needed
    }
    public void setProp1(String prop) {
      // no implementation needed
    }
    public void setProp2(Integer prop) {
      // no implementation needed
    }
  }

  /**
   * Explicit information about <code>BeanUnderTest</code>.
   */
  public static final class BeanUnderTestBeanInfo extends ElTagBeanInfo {
    // no implementation needed
  }
}