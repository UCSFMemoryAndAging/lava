/**
 * Nov 14, 2004
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

import java.util.Arrays;
import java.util.Properties;

import junit.framework.TestCase;
import net.sf.uitags.testutil.PrivilegedAccessor;

/**
 * Tests {@link net.sf.uitags.tagutil.TaglibProperties}.
 *
 * @author jonni
 * @version $Id$
 */
public final class TaglibPropertiesTest extends TestCase {
  /**
   * Test fixture
   */
  private TaglibProperties props;

  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(TaglibPropertiesTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
    this.props = TaglibProperties.getInstance();
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.props = null;
  }

  /**
   * Tests {@link TaglibProperties#getInstance()} to make sure that
   * <code>siteWideProps</code> is initialized once.
   *
   * @throws IllegalAccessException if reflection failed
   * @throws NoSuchFieldException if reflection failed
   */
  public void testGetInstance()
      throws IllegalAccessException, NoSuchFieldException {
    TaglibProperties props1 = TaglibProperties.getInstance();
    Properties siteWide1 =
        (Properties) PrivilegedAccessor.getValue(props1, "mergedProps");

    TaglibProperties props2 = TaglibProperties.getInstance();
    Properties siteWide2 =
        (Properties) PrivilegedAccessor.getValue(props2, "mergedProps");

    assertSame(siteWide1, siteWide2);
  }

  /**
   * Tests using {@link TaglibProperties#setRuntimeProperty(String, String)}
   * and {@link TaglibProperties#get(String)} together to make
   * sure runtime properties are set and retrieved correctly.
   */
  public void testSetAndGetStringProperty() {
    String testValue = "dummy test value";
    this.props.setRuntimeProperty("testKey", testValue);
    String value = this.props.get("testKey");
    assertEquals(testValue, value);

    // null value should not override previous value
    this.props.setRuntimeProperty("testKey", (String) null);
    assertEquals(testValue, this.props.get("testKey"));
  }

  /**
   * Tests {@link TaglibProperties#get(String)} to make
   * sure returned value is never <code>null</code>.
   */
  public void testGetAsNonNullString() {
    String nonExistantKey = "This is so ridiculous it can never be a key";
    String value = this.props.get(nonExistantKey);
    assertNotNull(value);
  }

  /**
   * Tests using {@link TaglibProperties#setRuntimeProperty(String, String[])}
   * and {@link TaglibProperties#getAsArray(String)} together to make
   * sure runtime properties are set and retrieved correctly.
   */
  public void testSetAndGetStringArrayProperty() {
    String[] testValues = new String[] { "dummy", "test", "values" };
    this.props.setRuntimeProperty(
        "calendar.listMonths.monthLabels", testValues);
    assertEquals("dummy,test,values",
        this.props.get("calendar.listMonths.monthLabels"));
    assertEquals(Arrays.asList(testValues), Arrays.asList(
        this.props.getAsArray("calendar.listMonths.monthLabels")));

    // Null value should not override previous value
    this.props.setRuntimeProperty(
        "calendar.listMonths.monthLabels", (String[]) null);
    assertEquals(Arrays.asList(testValues), Arrays.asList(
        this.props.getAsArray("calendar.listMonths.monthLabels")));
  }

  /**
   * Tests {@link TaglibProperties#getAsArray(String)} to make
   * sure returned string is never <code>null</code>. Instead an empty array
   * should be returned.
   */
  public void testGetAsEmptyStringArray() {
    String nonExistantKey = "This is so ridiculous it can never be a key";
    String[] values = this.props.getAsArray(nonExistantKey);
    assertNotNull(values);
  }
}