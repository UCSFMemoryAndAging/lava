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

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.uitags.tagutil.TaglibProperties;
import net.sf.uitags.util.IllegalPropertyValueException;

/**
 * Returns a message finder instance of the appropriate type.
 *
 * @author jonni
 * @version $Id$
 */
public final class MessageFinderFactory {
  /**
   * Logger.
   */
  private static final Log log = LogFactory.getLog(MessageFinderFactory.class);
  /**
   * The key of the property which specifies the type of the message finder
   * to use
   */
  private static final String MESSAGE_FINDER = "common.message.finder";
  /**
   * Shared message finder class
   */
  private static Class finderClass;

  /**
   * Non-instantiable by client.
   */
  private MessageFinderFactory() {
    super();
  }

  /**
   * Returns a message finder whose actual type is specified in the
   * configuration properties file. This factory reuses and stores a message
   * finder in session. If session doesn't exist, it reads and writes to
   * request instead.
   *
   * @param pageContext to initialize the <code>MessageFinder</code> to be
   *     returned
   * @return a message finder
   * @throws ClassCastException if the message finder specified in the
   *     configuration file does not implement <code>MessageFinder</code>.
   * @throws IllegalPropertyValueException if the message finder specified
   *     in the configuration file is not instantiable for any reason
   */
  public static MessageFinder getInstance(PageContext pageContext) {
    // Return a message finder stored in the session/request scope if one exists
    MessageFinder finder = findFromScope(pageContext);

    if (finder == null) {
      // No message finder found in scope, instantiate one and store
      // in appropriate scope.
      try {
        if (finderClass == null) {
          String finderName = TaglibProperties.MERGED.get(MESSAGE_FINDER);
          finderClass = Class.forName(finderName);

          if (log.isDebugEnabled()) {
            log.debug("Message finder found: '" + finderName + "'.");
          }
        }

        finder = (MessageFinder) finderClass.newInstance();
        saveToScope(pageContext, finder);
      }
      catch (ClassNotFoundException e) {
        new IllegalPropertyValueException(e);
      }
      catch (IllegalAccessException e) {
        new IllegalPropertyValueException(e);
      }
      catch (InstantiationException e) {
        new IllegalPropertyValueException(e);
      }
    }

    finder.setPageContext(pageContext);
    return finder;
  }

  /**
   * Attemps to find a message finder from the session scope. If session
   * doesn't exist, request scope is attempted. If that also fails, returns
   * <code>null</code>.
   *
   * @param pageContext allows access to scopes
   * @return message finder from session/request, or <code>null</code> if
   *     not found
   */
  private static MessageFinder findFromScope(PageContext pageContext) {
    HttpSession session = pageContext.getSession();
    MessageFinder ret = null;
    if (session != null) {
      ret = (MessageFinder) session.getAttribute(MessageFinder.REUSED_INSTANCE);
    }

    if (ret == null) {
      ret = (MessageFinder) pageContext.getRequest().
          getAttribute(MessageFinder.REUSED_INSTANCE);
    }

    return ret;
  }

  /**
   * Attempts to save a message finder to session scope. If session doesn't
   * exist, saves to the request scope.
   *
   * @param pageContext allows access to scopes
   * @param msgFinder message finder to save
   */
  private static void saveToScope(PageContext pageContext,
      MessageFinder msgFinder) {
    HttpSession session = pageContext.getSession();
    if (session != null) {
      session.setAttribute(MessageFinder.REUSED_INSTANCE, msgFinder);
    }
    else {
      ServletRequest request = pageContext.getRequest();
      request.setAttribute(MessageFinder.REUSED_INSTANCE, msgFinder);
    }
  }
}
