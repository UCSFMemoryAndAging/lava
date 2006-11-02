/**
 * Mar 28, 2005
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
import net.sf.uitags.util.UiString;
import net.sf.uitags.util.VelocityAdapter;

/**
 * Tests {@link net.sf.uitags.util.VelocityAdapter}.
 * The fact that this class compiles indicates
 * that the class under test and the utility classes it uses
 * are publicly accessible, which is <i>really important</i> in order
 * to allow <i>Velocity</i> template to use the classes.
 *
 * @author hgani
 * @version $Id$
 */
public class VelocityAdapterTest extends TestCase {
  /**
   * Instance of the class under test.
   */
  private VelocityAdapter velocityAdapter;

  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(VelocityAdapterTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
    this.velocityAdapter = new VelocityAdapter();
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.velocityAdapter = null;
  }

  /**
   * Ensures that {@link VelocityAdapter#createUiString(String)}
   * returns appropriate instance.
   */
  public void testCreateUiString() {
    UiString str = this.velocityAdapter.createUiString("");
    assertNotSame(this.velocityAdapter.createUiString(""), str);
  }

  /**
   * Ensures that {@link VelocityAdapter#getArrayLength(Object[])}
   * returns correct array length.
   */
  public void testGetArrayLength() {
    String[] testArray = new String[] { "a", "b", "c", "d"};
    assertEquals(
        testArray.length, this.velocityAdapter.getArrayLength(testArray));
  }
}
