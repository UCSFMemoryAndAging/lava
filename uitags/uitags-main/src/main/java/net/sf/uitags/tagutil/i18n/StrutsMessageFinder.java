/**
 * Dec 30, 2004
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

import java.util.Locale;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;


import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;

/**
 * Struts message finder adapter.
 *
 * @author jonni
 * @version $Id$
 */
public final class StrutsMessageFinder implements MessageFinder {
  private static final long serialVersionUID = 100L;

  /**
   * Struts resource bundle
   */
  private MessageResources resources;
  /**
   * Locale to use
   */
  private Locale locale;

  /**
   * Default constructor.
   */
  public StrutsMessageFinder() {
    super();
  }

  /** {@inheritDoc} */
  public void setPageContext(PageContext pageContext) {
    this.resources = getMessageResources(pageContext);
    this.locale    = getLocale(pageContext);
  }

  /**
   * Looks up and returns Struts resourde bundle
   *
   * @param pageContext allows scoped-attribute lookup
   * @return Struts resource bundle
   */
  private MessageResources getMessageResources(PageContext pageContext) {
    return ((MessageResources)
        pageContext.getRequest().getAttribute(Globals.MESSAGES_KEY));
  }

  /**
   * Returns the locale object created by Struts, or client browser's locale
   * if the former's not available.
   *
   * @param pageContext allows scoped-attribute lookup
   * @return client's locale
   */
  private Locale getLocale(PageContext pageContext) {
    HttpSession session = pageContext.getSession();
    Locale ret = null;
    // See if a Locale has been set up for Struts
    if (session != null) {
      ret = (Locale) session.getAttribute(Globals.LOCALE_KEY);
    }

    // If we've found nothing so far, use client browser's Locale
    if (ret == null) {
      ret = pageContext.getRequest().getLocale();
    }
    return ret;
  }

  /** {@inheritDoc} */
  public String get(String key) {
    return this.resources.getMessage(this.locale, key);
  }

  /** {@inheritDoc} */
  public String get(String key, Object arg0) {
    return this.resources.getMessage(this.locale, key, arg0);
  }

  /** {@inheritDoc} */
  public String get(String key, Object[] args) {
    return this.resources.getMessage(this.locale, key, args);
  }
}
