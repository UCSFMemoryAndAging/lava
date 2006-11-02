/**
 * Mar 25, 2005
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

import net.sf.uitags.util.Template;
import junit.framework.TestCase;

/**
 * Tests {@link net.sf.uitags.util.Template}.
 *
 * @author hgani
 * @version $Id$
 */
public class TemplateTest extends TestCase {

  /**
   * Instance of the class under test.
   */
  private Template tpl;

  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(TemplateTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
    this.tpl = Template.forName("test.vm");
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.tpl = null;
  }

  /**
   * Ensures that all <i>eval</i> methods return correct String value.
   */
  public void testEval() {
    assertEquals("Simple $template loaded!", this.tpl.evalToString().trim());
    assertEquals(this.tpl.eval().toString(), this.tpl.evalToString());
  }

  /**
   * Ensures that objects are mapped properly.
   */
  public void testMap() {
    this.tpl.map("template", "'String' mapped template");
    assertEquals(this.tpl.evalToString().trim(),
        "Simple 'String' mapped template loaded!");

    this.tpl.map("template", new Object() {
      public String toString() {
        return "'Object' mapped template";
      }
    });
    assertEquals(this.tpl.evalToString().trim(),
        "Simple 'Object' mapped template loaded!");
  }
}
