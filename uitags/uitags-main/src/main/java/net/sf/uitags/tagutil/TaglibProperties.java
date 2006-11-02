/**
 * Nov 12, 2004
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
package net.sf.uitags.tagutil;

import java.io.IOException;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;


import net.sf.uitags.util.ArrayUtils;
import net.sf.uitags.util.PropertiesLoadingException;
import net.sf.uitags.util.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Configuration properties for the taglib. There are 3 types of properties,
 * listed here in order of loading sequence (last one loaded wins):
 * <ol>
 *   <li>The <i>factory-default</i> properties are part of the taglib.
 *       These configuration entries are <i>overridden</i> if they are
 *       also defined in the property file described below.</li>
 *   <li>The <i>site-wide properties</i> file (uitags.properties)
 *       contains site-wide configuration, overriding the factory-default
 *       properties. Configuration entries in this file affect all tags which
 *       do not define their own run-time properties. This file is optional.
 *       </li>
 *   <li>The <i>run-time properties</i> are not set in any file. A run-time
 *       property is specified in the JSP as a tag attribute. The
 *       executing tag handler <i>has</i> to pass run-time properties'
 *       values to
 *       {@link TaglibProperties#setRuntimeProperty(String, String)}.
 *       This allows handlers which subsequently retrieve properties
 *       from the <code>TaglibProperties</code> instance see the
 *       overriding values.</li>
 * </ol>
 *
 * @author jonni
 * @version $Id$
 */
public class TaglibProperties {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /**
   * Name of the factory-default properties file
   */
  private static final String FACTORY_DEFAULT = "factory-default.properties";
  /**
   * Path to the site-wide properties file
   */
  private static final String SITE_WIDE       = "uitags";



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * Logger
   */
  private static Log log = LogFactory.getLog(TaglibProperties.class);

  /**
   * Contains entries from the factory-default properties, overriden by the
   * ones from the site-wide properties.
   */
  private static Properties mergedProps = new Properties();

  /**
   * An immutable version of the merged properties. The purpose of this variable
   * is to allow utility classes access properties whose value always remain
   * constant (never changes due to overriding).
   */
  public static final TaglibProperties MERGED;

  /**
   * The runtime properties, those that are defined in JSP. Entries in this
   * object shadow those that are in "mergedProps".
   */
  private Properties runtimeProps;



  /////////////////////////////////////////////////////
  ////////// Construction and initialization //////////
  /////////////////////////////////////////////////////

  /**
   * Loads the factory-default and site-wide properties.
   *
   * @throws PropertiesLoadingException if failed to load the
   *     factory-default properties
   */
  static {
    // Load the factory-default properties
    try {
      mergedProps.load(
          TaglibProperties.class.getResourceAsStream(FACTORY_DEFAULT));
    }
    catch (IOException e) {
      throw new PropertiesLoadingException(e, FACTORY_DEFAULT);
    }

    // Load the site-wide properties. It's OK if it's not provided.
    ResourceBundle rb = null;
    try {
      rb = ResourceBundle.getBundle(SITE_WIDE);
      // Exception would have been thrown here if not found
      Enumeration keys = rb.getKeys();
      while (keys.hasMoreElements()) {
        String key = (String) keys.nextElement();
        mergedProps.setProperty(key, rb.getString(key));
      }
    }
    catch (MissingResourceException e) {
      if (log.isDebugEnabled()) {
        log.debug("Properties file '" + SITE_WIDE + "' not found.");
      }
    }

    if (log.isDebugEnabled()) {
      log.debug("Taglib properties loaded: " + mergedProps);
    }

    // An immutable version of the merged properties
    MERGED = new TaglibProperties() {
      public void setRuntimeProperty(String key, String value) {
        throw new IllegalStateException("Object is immutable.");
      }
    };
  }

  /**
   * Made private to allow for limited subclassing.
   */
  private TaglibProperties() {
    this.runtimeProps = new Properties();
  }

  /**
   * Creates an instance whose entries can be safely overriden without
   * affecting other instances.
   *
   * @return an new instance
   */
  public static TaglibProperties getInstance() {
    return new TaglibProperties();
  }



  /////////////////////////////////////////////////
  ////////// Used by this class' clients //////////
  /////////////////////////////////////////////////

  /**
   * Sets a runtime property, overriding the corresponding property
   * configured in the factory-default and site-wide properties files.
   * If the supplied <code>value</code> is <code>null</code> nothing is
   * done.
   *
   * @param key   the key of the property to set
   * @param value the value of the property to set
   */
  public void setRuntimeProperty(String key, String value) {
    if (value == null) {
      return;
    }
    this.runtimeProps.setProperty(key, value);
  }

  /**
   * Returns the value of the property whose key is supplied.
   *
   * @param key the key of the property to return
   * @return the property as <code>String</code>. If value is
   *     <code>null</code>, an empty string is returned instead.
   */
  public String get(String key) {
    // A runtime property is given higher priority.
    String ret = this.runtimeProps.getProperty(key);

    // If it's not a runtime property, try loading it from the factory-default
    // or site-wide configuration.
    if (ret == null) {
      ret = mergedProps.getProperty(key);
    }

    return (ret == null) ? "" : ret;
  }

  public void setRuntimeProperty(String key, String[] values) {
    if (values == null) {
      return;
    }
    this.runtimeProps.setProperty(key, StringUtils.join(values));
  }

  public String[] getAsArray(String key) {
    String stringValue = get(key);
    return ArrayUtils.toArray(stringValue);
  }
}
