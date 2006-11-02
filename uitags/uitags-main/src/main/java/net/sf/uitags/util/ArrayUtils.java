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

/**
 * Contains array related utility methods.
 *
 * @author jonni
 * @version $Id$
 */
public final class ArrayUtils {
  private ArrayUtils() {
    // Non-instantiable utility class.
  }

  /**
   * An empty immutable <code>String</code> array.
   */
  private static final String[] EMPTY_STRING_ARRAY = new String[] {};

  public static final String[] toArray(String stringValue) {
    if (stringValue == null) {
      return null;
    }

    return (stringValue.length() == 0)?
        ArrayUtils.EMPTY_STRING_ARRAY : stringValue.split(",");
  }

  public static final String[] toArrayOfTrimmed(String stringValue) {
    if (stringValue == null) {
      return null;
    }

    String[] valueAsArray = toArray(stringValue);
    for (int i = 0; i < valueAsArray.length; i++) {
      valueAsArray[i] = valueAsArray[i].trim();
    }

    return valueAsArray;
  }

  public static final boolean equals(Object[] array1, Object[] array2) {
    if (array1.length != array2.length) {
      return false;
    }

    for (int i = 0; i < array1.length; ++i) {
      if (!array1[i].equals(array2[i])) {
        return false;
      }
    }
    return true;
  }
}
