/**
 * Jun 25, 2005
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

import junit.framework.TestCase;

/**
 * Tests {@link net.sf.uitags.tagutil.AttributeSupportHelper}.
 *
 * @author hgani
 * @version $Id$
 */
public final class AttributeSupportHelperTest extends TestCase {
  /**
   * Instance of the class under test
   */
  private AttributeSupportHelper helper;

  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(AttributeSupportHelperTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
    this.helper = new AttributeSupportHelper();
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.helper = null;
  }

  /**
   * Ensures that {@link AttributeSupportHelper#eval()} returns
   * the correct HTML code.
   */
  public void testToString() {
    this.helper.addAttribute("name1", "value1");
    this.helper.addAttribute("name2", "value2");
    this.helper.addAttribute("name3", "value3");

    String htmlCode = this.helper.eval();
    // make sure that the first character is a space, because the
    // method guarantees that the generated String can be appended
    // to a HTMl tag without any modification
    assertTrue(htmlCode.charAt(0) == ' ');
    // we can only test whether a specific attribute exist in the String,
    // because the helper does not guarantee that all attributes are in
    // certain order
    assertTrue(htmlCode.indexOf("name1=\"value1\"") >= 0);
    assertTrue(htmlCode.indexOf("name2=\"value2\"") >= 0);
    assertTrue(htmlCode.indexOf("name3=\"value3\"") >= 0);
    assertTrue(htmlCode.indexOf("name1=value1") < 0);
    assertTrue(htmlCode.indexOf("name1=\"value3\"") < 0);
    assertTrue(htmlCode.indexOf("name4=\"value4\"") < 0);
  }
}
