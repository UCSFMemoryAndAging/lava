/**
 * Nov 22, 2004
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
package net.sf.uitags.tagutil.i18n;

import javax.servlet.jsp.PageContext;

import net.sf.uitags.util.UiString;

/**
 * A message finder with no I18N support. It does not perform message
 * look up; it simply returns whatever string is given to it.
 *
 * @author jonni
 * @version $Id$
 */
public final class I18nIgnorantMessageFinder implements MessageFinder {
  private static final long serialVersionUID = 100L;

  /**
   * Default constructor.
   */
  public I18nIgnorantMessageFinder() {
    super();
  }

  /** {@inheritDoc} */
  public void setPageContext(PageContext pageContext) {
    // Do nothing
  }

  /** {@inheritDoc} */
  public String get(String key) {
    return key;
  }

  /** {@inheritDoc} */
  public String get(String key, Object arg0) {
    return UiString.simpleConstruct(key, new String[] { String.valueOf(arg0) });
  }

  /** {@inheritDoc} */
  public String get(String key, Object[] args) {
    String[] params = new String[args.length];
    for (int i = 0; i < args.length; i++) {
      params[i] = String.valueOf(args[i]);
    }
    return UiString.simpleConstruct(key, params);
  }
}
