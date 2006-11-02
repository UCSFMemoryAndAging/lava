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

import java.io.Serializable;

import javax.servlet.jsp.PageContext;


/**
 * Finds message from resource bundle. Different implementors have different
 * strategies for finding messages.
 *
 * @author jonni
 * @version $Id$
 */
public interface MessageFinder extends Serializable {
  /**
   * Name of the session/request-scoped attribute that is the
   * message finder instance to be reused by this web client.
   */
  String REUSED_INSTANCE =
      "net.sf.uitags.tagutil.MessageFinder.REUSED_INSTANCE";

  /**
   * Sets this page's <code>PageContext</code> to allow access to scoped
   * attributes. {@link MessageFinderFactory} invokes this method before
   * its <code>getInstance</code> returns this <code>MessageFinder</code>.
   *
   * @param pageContext the <code>PageContext</code> to set.
   */
  void setPageContext(PageContext pageContext);

  /**
   * Returns localized message for the given key.
   *
   * @param key the key for which a message is to be returned
   * @return localized message for the given key
   */
  String get(String key);

  /**
   * Returns localized message for the given key with 1 parametric replacement.
   *
   * @param key  the key for which a message is to be returned
   * @param arg0 parameter value
   * @return localized message for the given key
   */
  String get(String key, Object arg0);

  /**
   * Returns localized message for the given key with parametric replacements.
   *
   * @param key  the key for which a message is to be returned
   * @param args parameter values
   * @return localized message for the given key
   */
  String get(String key, Object[] args);
}
