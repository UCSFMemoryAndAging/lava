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
 * Tests {@link net.sf.uitags.util.ObjectPair}.
 *
 * @author hgani
 * @version $Id$
 */
public class ObjectPairTest extends TestCase {

  /**
   * Instance of the class under test.
   */
  private ObjectPair pair;

  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(ObjectPairTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
    this.pair = new ObjectPair("first1", "second1");
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.pair = null;
  }

  /**
   * Ensures that all getters of this class return proper objects.
   */
  public void testGetObjects() {
    assertEquals("first1", this.pair.getFirstObject());
    assertEquals("second1", this.pair.getSecondObject());
  }

  /**
   * Ensures that {@link ObjectPair#equals(Object)}
   * correctly compares two objects.
   */
  public void testEquals() {
    // same first object, diff second object
    ObjectPair pair1 = new ObjectPair("first1", "second2");
    // diff first object, same second object
    ObjectPair pair2 = new ObjectPair("first2", "second1");
    // same first object, same second object
    ObjectPair pair3 = new ObjectPair("first1", "second1");
    // diff first object, diff second object
    ObjectPair pair4 = new ObjectPair("first2", "second2");

    assertTrue(!this.pair.equals(pair1));
    assertTrue(!this.pair.equals(pair2));
    assertTrue(this.pair.equals(pair3));
    assertTrue(!this.pair.equals(pair4));
  }

  /**
   * Ensures that {@link ObjectPair#hashCode()}
   * returns the same hash code value when the objects are equal.
   */
  public void testHashCode() {
    // we can only test objects with the same hashcode, because
    // there is no guarantee that different objects will have
    // different hash code
    ObjectPair tempPair = new ObjectPair("first1", "second1");

    assertEquals(this.pair.hashCode(), tempPair.hashCode());
  }
}
