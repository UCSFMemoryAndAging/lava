/**
 * Feb 23, 2005
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

import junit.framework.TestCase;

/**
 * Tests {@link net.sf.uitags.util.UiString}.
 *
 * @author hgani
 * @version $Id$
 */
public class UiStringTest extends TestCase {
  /**
   * Instance of the class under test.
   */
  private UiString boundUiString;

  /**
   * Instance of the class under test.
   */
  private UiString mappedUiString;

  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(UiStringTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
    this.boundUiString = new UiString("{0} & {1}", UiString.OPTION_ALL);
    this.mappedUiString = new UiString(
        "{first} & {second}", UiString.OPTION_ALL);
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.boundUiString = null;
    this.mappedUiString = null;
  }

  /**
   * Ensures that {@link UiString#setDefaultOptions(int)}
   * does a proper validation for the options value.
   */
  public void testSetDefaultOptions() {
    try {
      this.boundUiString.setDefaultOptions(-1);
      fail("-1 is not supposed to be a valid option.");
    }
    catch (IllegalArgumentException e) {
      // as expected
    }

    try {
      this.mappedUiString.setDefaultOptions(UiString.OPTION_ALL + 1);
      fail("Options value is too big, should be invalid.");
    }
    catch (IllegalArgumentException e) {
      // as expected
    }
  }

  /**
   * Ensures that the parameters binding technique is working properly.
   */
  public void testBind() {
    this.boundUiString.bind("Tom");
    this.boundUiString.bind("Jerry");
    // both params are surrounded with quotes
    assertEquals("\"Tom\" & \"Jerry\"", this.boundUiString.construct());

    String tempString = UiString.simpleConstruct(
        "\"{0}\" {1} {2}", new String[] { "Tom", "&", "\"Jerry\"" });
    assertEquals(tempString, this.boundUiString.construct());

    bindTestWithoutOption();
    bindTestWithAutoSurround();
    bindTestWithHtmlEncoding();
    bindTestWithJsEscape();
  }

  /**
   * Ensures that the non-option binding is working properly.
   */
  private void bindTestWithoutOption() {
    this.boundUiString.setDefaultOptions(UiString.OPTION_NONE);
    this.boundUiString.clearBindParameters();
    this.boundUiString.bind("Tom");
    this.boundUiString.bind("Jerry");

    // no quotes
    assertEquals("Tom & Jerry", this.boundUiString.construct());
  }

  /**
   * Ensures that binding with <i>Auto Surround</i> is working properly.
   */
  private void bindTestWithAutoSurround() {
    this.boundUiString.setSurroundString("'");
    this.boundUiString.clearBindParameters();
    this.boundUiString.bind("Tom", UiString.OPTION_AUTO_SURROUND);
    this.boundUiString.bind("<cat>", UiString.OPTION_NONE);

    // second param not encoded
    assertEquals("'Tom' & <cat>", this.boundUiString.construct());

    this.boundUiString.clearBindParameters();
    this.boundUiString.bind("Tom");

    // second holder not bound
    assertEquals("Tom & {1}", this.boundUiString.construct());
  }

  /**
   * Ensures that binding with <i>HTML Encode</i> is working properly.
   */
  private void bindTestWithHtmlEncoding() {
    this.boundUiString.setDefaultOptions(UiString.OPTION_HTML_ENCODING);
    this.boundUiString.bind("<cat>");
    // bind more params than needed
    this.boundUiString.bind("others");

    // second param encoded
    assertEquals("Tom & &lt;cat&gt;", this.boundUiString.construct());
  }

  /**
   * Ensures that binding with <i>JS Escape</i> is working properly.
   */
  private void bindTestWithJsEscape() {
    this.boundUiString.clearBindParameters();
    this.boundUiString.setDefaultOptions(UiString.OPTION_JS_ESCAPE);
    this.boundUiString.bind("\"Tom's cat\"");
    this.boundUiString.bind("\\mouse\\");

    assertEquals("\\\"Tom\\\'s cat\\\" & \\\\mouse\\\\",
        this.boundUiString.construct());
  }

  /**
   * Ensures that the parameters mapping technique is working properly.
   */
  public void testMap() {
    this.mappedUiString.map("first", "Tom");

    // second holder not mapped
    assertEquals("\"Tom\" & {second}", this.mappedUiString.construct());

    this.mappedUiString.map("second", "Jerry");

    // both params quoted
    assertEquals("\"Tom\" & \"Jerry\"", this.mappedUiString.construct());

    try {
      this.mappedUiString.bind("Tom");
      fail("Binding and mapping technique cannot be used together.");
    }
    catch (IllegalStateException e) {
      // as expected
    }

    this.mappedUiString.map("second", "Jerry", UiString.OPTION_NONE);

    // second param not quoted
    assertEquals("\"Tom\" & Jerry", this.mappedUiString.construct());

    mapTestSetOptions();
  }

  /**
   * Ensures that all option-related methods change the <code>String</code>
   * construction behaviour correctly.
   */
  private void mapTestSetOptions() {
    this.mappedUiString.map("first", "Tom");
    this.mappedUiString.clearOptions();
    this.mappedUiString.map("second", "Jerry");

    assertEquals("\"Tom\" & Jerry", this.mappedUiString.construct());

    this.mappedUiString.jsEscape();
    this.mappedUiString.map("first", "\"Tom\"");
    this.mappedUiString.autoSurround();
    this.mappedUiString.map("second", "Jerry");

    assertEquals("\\\"Tom\\\" & \"Jerry\"", this.mappedUiString.construct());

    this.mappedUiString.htmlEncode();
    this.mappedUiString.map("second", "Jerry & friends");

    assertEquals("\\\"Tom\\\" & \"Jerry&nbsp;&amp;&nbsp;friends\"",
        this.mappedUiString.construct());

    this.mappedUiString.clearOptions();
    this.mappedUiString.map("first", "{second}");
    this.mappedUiString.map("second", "Jerry");

    // this is the expected behaviour
    assertEquals("Jerry & Jerry", this.mappedUiString.construct());

    this.mappedUiString.map("first", "\\{second\\}");
    this.mappedUiString.map("second", "Jerry");

    assertEquals("{second} & Jerry", this.mappedUiString.construct());

    this.mappedUiString = new UiString("\\{first\\} & {second}");

    this.mappedUiString.map("first", "Tom");
    this.mappedUiString.map("second", "\\\\{second\\\\}");

    assertEquals("{first} & \\{second\\}", this.mappedUiString.construct());

    // taken from UiString documentation
    this.mappedUiString = new UiString("{a} and {b} and {c}");

    this.mappedUiString.map("c", "{b}");
    this.mappedUiString.map("b", "bValue");
    this.mappedUiString.map("a", "{b}");
    this.mappedUiString.map("second", "\\\\{second\\\\}");

    assertEquals("{b} and bValue and bValue", this.mappedUiString.construct());
  }
}
