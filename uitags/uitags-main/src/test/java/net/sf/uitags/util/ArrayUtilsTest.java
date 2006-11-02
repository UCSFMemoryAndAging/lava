/**
 * May 7, 2006
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

public class ArrayUtilsTest extends TestCase {
  public void testToArrayReturnsNullIfGivenNull() {
    assertNull(ArrayUtils.toArray(null));
  }

  public void testToArray() {
    String elementString = "one,two,three";
    String[] elementArray = new String[] { "one", "two", "three" };

    assertTrue(ArrayUtils.equals(
        elementArray, ArrayUtils.toArray(elementString)));

    elementString = "one,two, three";
    assertFalse(ArrayUtils.equals(
        elementArray, ArrayUtils.toArray(elementString)));

    elementArray = new String[] { "one", "two", " three" };
    assertTrue(ArrayUtils.equals(
        elementArray, ArrayUtils.toArray(elementString)));
  }

  public void testToArrayOfTrimmedReturnsNullIfGivenNull() {
    assertNull(ArrayUtils.toArrayOfTrimmed(null));
  }

  public void testToArrayOfTrimmed() {
    String elementString = "one , two, three   ,four";
    String[] elementArray = new String[] { "one", "two", "three", "four" };

    assertTrue(ArrayUtils.equals(
        elementArray, ArrayUtils.toArrayOfTrimmed(elementString)));
  }
}
