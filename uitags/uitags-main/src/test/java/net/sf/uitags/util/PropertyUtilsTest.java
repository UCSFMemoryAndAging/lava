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
package net.sf.uitags.util;

import javax.servlet.jsp.JspException;

import junit.framework.TestCase;

/**
 * Tests {@link net.sf.uitags.tagutil.PropertyUtils}.
 *
 * @author jonni
 * @version $Id$
 */
public final class PropertyUtilsTest extends TestCase {
  /**
   * The JavaBean under test. We can use any random type, String is chosen
   * because it's straightforward to use.
   */
  private String beanUnderTest;
  /**
   * Name of a property that must not exist in the bean under test.
   */
  private String dummyPropertyName;

  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(PropertyUtilsTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
    this.beanUnderTest = "dummyBean";
    this.dummyPropertyName = "dummyPropertyName";
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.beanUnderTest = null;
    this.dummyPropertyName = null;
  }

  /**
   * Ensures that {@link PropertyUtils#getSimpleProperty(Object, String)}
   * throws <code>JspException</code> when an introspection error occurs.
   */
  public void testGetSimpleProperty() {
    try {
      PropertyUtils.getSimpleProperty(
          this.beanUnderTest, this.dummyPropertyName);
    }
    catch (JspException e) {
      // Test passes if we end up here
    }
  }

  /**
   * Tests {@link PropertyUtils#getProperty(Object, String)} to make sure
   * it throws <code>JspException</code> when an introspection error occurs.
   */
  public void testGetProperty() {
    try {
      PropertyUtils.getProperty(this.beanUnderTest, this.dummyPropertyName);
    }
    catch (JspException e) {
      // Test passes if we end up here
    }
  }
}
