/**
 * May 6, 2005
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
package net.sf.uitags.tagutil.validation;

import javax.servlet.jsp.PageContext;

import net.sf.uitags.tagutil.ScopedIdGenerator;
import net.sf.uitags.util.UiString;

/**
 * Class that provides tag validations at runtime.
 *
 * @author hgani
 * @version $Id$
 */
public final class RuntimeValidator {
  /**
   * Utility classes should not have a public or default constructor.
   */
  private RuntimeValidator() {
    // hide the constructor
  }

  /**
   * Makes sure a tag only appears once in a page.
   *
   * @param tagId identifier of the tag
   * @param pageContext page context of the tag
   * @param tagName name of the tag
   * @throws RuntimeValidationException if the assertion failed
   */
  public static void assertSingleUse(
      String tagId, String tagName, PageContext pageContext) {
    long instanceId = ScopedIdGenerator.nextId(
        PageContext.REQUEST_SCOPE, tagId, pageContext);
    if (instanceId > 1) {
      throw new RuntimeValidationException(UiString.simpleConstruct(
          "'{0}' tag can only be used at most once in a request.",
          new String[] { tagName }));
    }
  }

  /**
   * Ensures that at most only one of the two attributes are specified.
   *
   * @param name1 the first attribute name
   * @param value1 the first attribute value
   * @param name2 the second attribute name
   * @param value2 the second attribute value
   * @throws DeferredValidationException if the assertion failed
   */
  public static void assertAttributeExclusive(
      String name1, String value1, String name2, String value2) {
    if ((value1 != null && value2 != null) &&
        (!value1.equals("") && !value2.equals(""))) {
      throw new DeferredValidationException("The attribute '" + name1 +
          "' and '" + name2 + "' are exclusive.");
    }
  }

  /**
   * Ensures that at least one of the two attributes are specified.
   *
   * @param name1 the first attribute name
   * @param value1 the first attribute value
   * @param name2 the second attribute name
   * @param value2 the second attribute value
   * @throws DeferredValidationException if the assertion failed
   */
  public static void assertEitherSpecified(
      String name1, String value1, String name2, String value2) {
    if ((isStringEmpty(value1) && isStringEmpty(value2))) {
      throw new DeferredValidationException("One of the attributes '" + name1 +
          "' and '" + name2 + "' must be specified.");
    }
  }

  /**
   * Checks if a string is empty -- either <code>null</code> or equals to
   * <code>""</code>.
   *
   * @param value the string
   * @return <code>true</code> if it is empty, <code>false</code> otherwise
   */
  private static boolean isStringEmpty(String value) {
    return value == null || value.equals("");
  }
}
