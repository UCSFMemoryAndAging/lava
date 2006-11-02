/**
 * Jun 28, 2005
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

import javax.servlet.jsp.PageContext;

import junit.framework.TestCase;
import net.sf.uitags.tagutil.validation.DeferredValidationException;
import net.sf.uitags.tagutil.validation.RuntimeValidationException;
import net.sf.uitags.tagutil.validation.RuntimeValidator;

import org.springframework.mock.web.MockPageContext;

/**
 * Tests {@link net.sf.uitags.tagutil.validation.RuntimeValidator}.
 *
 * @author hgani
 * @version $Id$
 */
public class RuntimeValidatorTest extends TestCase {
  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(RuntimeValidatorTest.class);
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
   * Ensures that
   * {@link RuntimeValidator#assertSingleUse(String, String, PageContext)
   * complains when a tag ID is used more than once.
   */
  public void testAssertSingleUse() {
    MockPageContext pageContext = new MockPageContext();
    RuntimeValidator.assertSingleUse("id1", "name", pageContext);
    RuntimeValidator.assertSingleUse("id2", "name", pageContext);
    try {
      RuntimeValidator.assertSingleUse("id1", "name", pageContext);
      fail("Tag single use assertion failed.");
    }
    catch (RuntimeValidationException e) {
      // as expected
    }
  }

  /**
   * Ensures that
   * {@link RuntimeValidator#assertAttributeExclusive(String, String, String, String)}
   * complains when two exclusive attributes are specified.
   */
  public void testAssertAttributeExclusive() {
    RuntimeValidator.assertAttributeExclusive("name1", "value1", "name2", "");
    RuntimeValidator.assertAttributeExclusive("name1", "value1", "name2", null);
    RuntimeValidator.assertAttributeExclusive("name1", "", "name2", "value2");
    RuntimeValidator.assertAttributeExclusive("name1", null, "name2", "value2");

    RuntimeValidator.assertAttributeExclusive("name1", "", "name2", "");
    RuntimeValidator.assertAttributeExclusive("name1", "", "name2", null);
    RuntimeValidator.assertAttributeExclusive("name1", null, "name2", "");
    RuntimeValidator.assertAttributeExclusive("name1", null, "name2", null);

    try {
      RuntimeValidator.assertAttributeExclusive("name1", "value1", "name2", "value2");
      fail("Exclusive attribute assertion failed");
    }
    catch (DeferredValidationException e) {
      // as expected
    }
  }

  /**
   * Ensures that
   * {@link RuntimeValidator#assertAttributeExclusive(String, String, String, String)}
   * complains when both attributes are specified.
   */
  public void testAssertEitherSpecified() {
    RuntimeValidator.assertEitherSpecified("name1", "value1", "name2", "value2");
    RuntimeValidator.assertEitherSpecified("name1", "value1", "name2", "");
    RuntimeValidator.assertEitherSpecified("name1", "value1", "name2", null);
    RuntimeValidator.assertEitherSpecified("name1", "", "name2", "value2");
    RuntimeValidator.assertEitherSpecified("name1", null, "name2", "value2");

    assertEitherSpecified("name1", "", "name2", "");
    assertEitherSpecified("name1", "", "name2", null);
    assertEitherSpecified("name1", null, "name2", "");
    assertEitherSpecified("name1", null, "name2", null);
  }

  private void assertEitherSpecified(
      String name1, String value1, String name2, String value2) {
    try {
      RuntimeValidator.assertEitherSpecified(name1, value1, name2, value2);
      fail("Either specified assertion failed");
    }
    catch (DeferredValidationException e) {
      // as expected
    }
  }
}
