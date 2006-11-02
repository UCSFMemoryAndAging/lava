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
 * Tests {@link net.sf.uitags.util.ValueLabelPair}.
 *
 * @author hgani
 * @version $Id$
 */
public class ValueLabelPairTest extends TestCase {

  /**
   * Instance of the class under test.
   */
  private ValueLabelPair pair;

  /**
   * Main method for the tests.
   *
   * @param args main arguments
   */
  public static void main(String[] args) {
    junit.textui.TestRunner.run(ValueLabelPairTest.class);
  }

  /** {@inheritDoc} */
  protected void setUp() throws Exception {
    super.setUp();
    this.pair = new ValueLabelPair("value1", "label1");
  }

  /** {@inheritDoc} */
  protected void tearDown() throws Exception {
    super.tearDown();
    this.pair = null;
  }

  /**
   * Ensures that all getters of this class return proper values.
   */
  public void testGetStrings() {
    assertEquals("value1", this.pair.getValueAsString());
    assertEquals("label1", this.pair.getLabelAsString());
    assertEquals(this.pair.getValueAsString(), this.pair.getKey());
    assertEquals(this.pair.getLabelAsString(), this.pair.getValue());
  }

  /**
   * Ensures that {@link ValueLabelPair#equals(Object)}
   * correctly compares two objects.
   */
  public void testEquals() {
    // same value, diff label
    ValueLabelPair pair1 = new ValueLabelPair("value1", "label2");
    // diff value, same label
    ValueLabelPair pair2 = new ValueLabelPair("value2", "label1");
    // same value, same label
    ValueLabelPair pair3 = new ValueLabelPair("value1", "label1");
    // diff value, diff label
    ValueLabelPair pair4 = new ValueLabelPair("value2", "label2");

    // use assertTrue instead of assertEquals, because in this case
    // we just want to know both are equal, we don't have certain
    // value that we expect to have
    assertTrue(this.pair.equals(pair1));
    assertTrue(!this.pair.equals(pair2));
    assertTrue(this.pair.equals(pair3));
    assertTrue(!this.pair.equals(pair4));
  }

  /**
   * Ensures that {@link ValueLabelPair#hashCode()}
   * returns the same hash code value when the objects are equal.
   */
  public void testHashCode() {
    // we can only test objects with the same hashcode, because
    // there is no guarantee that different objects will have
    // different hash code
    ValueLabelPair tempPair = new ValueLabelPair("value1", "label1");

    assertEquals(this.pair.hashCode(), tempPair.hashCode());
  }
}
