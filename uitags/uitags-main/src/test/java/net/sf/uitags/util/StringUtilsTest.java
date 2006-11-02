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

public class StringUtilsTest extends TestCase {
  public void testJoinNull() {
    assertEquals("", StringUtils.join(null));
  }
  
  public void testJoinOneElement() {
    assertEquals("1", StringUtils.join(new Long[] { new Long(1) } ));
  }
  
  public void testJoinMultipleElements() {
    String[] elements = new String[] { "one", "two", "three" };
    assertEquals("one,two,three", StringUtils.join(elements));
  }
}
