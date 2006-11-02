/**
 * Sep 3, 2006
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
package net.sf.uitags.build;

public final class StringUtils {
  /**
   * Escapes all regular expression special characters in a string to
   * allow it to be safely used as a regex pattern.
   *
   * @param str the string
   * @return the regex pattern
   */
  static String escapePattern(String str) {
    StringBuffer buffer = new StringBuffer();
    char curr;
    for (int i = 0; i < str.length(); i++) {
      curr = str.charAt(i);
      switch (curr) {
        case '\\':
          buffer.append("\\\\");
          break;
        default:
          buffer.append(curr);
      }
    }
    return String.valueOf(buffer);
  }
}
