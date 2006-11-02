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
package net.sf.uitags.tagutil;

import java.beans.PropertyDescriptor;

import net.sf.uitags.tagutil.bean.TagBeanInfo;

import junit.framework.TestCase;

/**
 * Tests {@link net.sf.uitags.tagutil.bean.TagBeanInfo}.
 *
 * @author jonni
 * @version $Id$
 */
public final class TagBeanInfoTest extends TestCase {
  /**
   * The test fixture.
   */
  private JavaBeanClassBeanInfo info;

  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(TagBeanInfoTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
    this.info = new JavaBeanClassBeanInfo();
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.info = null;
  }



  //////////////////////////////////
  ////////// Test methods //////////
  //////////////////////////////////

  /**
   * Tests {@link TagBeanInfo#getPropertyDescriptors()} to make sure
   * that only setter methods are recognized.
   */
  public void testGetPropertyDescriptors() {
    PropertyDescriptor[] props = this.info.getPropertyDescriptors();

    assertTrue(props.length == 2);

    for (int i = 0; i < props.length; i++) {
      assertTrue(
          props[i].getName().equals("firstProp") ||
          props[i].getName().equals("secondProp"));
    }
  }


  //////////////////////////////////////////////////////////
  ////////// Helper classes to facilitate testing //////////
  //////////////////////////////////////////////////////////

  /**
   * Introspected by {@link TagBeanInfo}.
   */
  public static class JavaBeanClass {
    String s;
    Long l;

    public String getFirstProp() { return ""; }
    public void setFirstProp(String s) {
      this.s = s;
    }

    public Long getSecondProp() { return null; }
    public void setSecondProp(Long l) {
      this.l = l;
    }

    public String getThirdProp() { return ""; }
  }

  /**
   * Exposes only the setter methods of
   * {@link TagBeanInfoTest.JavaBeanClass}.
   */
  private static class JavaBeanClassBeanInfo extends TagBeanInfo {
    // The inherited getPropertyDescriptors() does all the job.
  }
}
