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

public final class StringUtils {
  private StringUtils() {
    // Non-instantiable utility class
  }
  
  /**
   * Combines the string representation of the elements in the supplied array
   * into one string. Each value is separated by a comma.
   * If <code>elements</code> is <code>null</code>, returns an empty string.
   * 
   * @param elements the array elements whose string representation is to be
   *                 joined
   * @return join result
   */
  public static String join(Object[] elements) {
    if (elements == null) {
      return "";
    }
    
    StringBuffer joined = new StringBuffer();
    
    for (int i = 0; i < elements.length; i++) {
      joined.append(elements[i]);
      
      if (i < (elements.length - 1)) {
        joined.append(',');
      }
    }
    
    return joined.toString();
  }
}
