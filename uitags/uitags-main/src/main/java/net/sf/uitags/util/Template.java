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
package net.sf.uitags.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ResourceNotFoundException;

/**
 * A template is a string (sometimes of great length) that contains
 * placeholders that can be substituted with values. A template is
 * retrieved by name.
 * <p>
 * This class uses Velocity for its templating capability, but its
 * interface is designed in such a way that the rest of the system need
 * not be aware of this. This allows us to replace Velocity with another
 * templating system if the need arises.
 *
 * @author jonni
 * @author hgani
 * @version $Id$
 */
public final class Template {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////
  /**
   * Velocity config file
   */
  private static final String CONFIG = "velocity.properties";

  /// Velocity templates for generating HTML/JS code. ///
  public static final String CALENDAR = "uitags/calendar/calendar.vm";

  public static final String CALENDAR_UPDATE_DATE =
      "uitags/calendar/updateDate.vm";

  public static final String CALENDAR_LIST_MONTHS =
      "uitags/calendar/listMonths.vm";

  public static final String CALENDAR_LIST_YEARS =
      "uitags/calendar/listYears.vm";

  public static final String FORM_GUIDE = "uitags/formGuide/formGuide.vm";

  public static final String OPTION_TRANSFER =
      "uitags/optionTransfer/optionTransfer.vm";

  public static final String OPTION_TRANSFER_RETURN =
      "uitags/optionTransfer/return.vm";

  public static final String OPTION_TRANSFER_RETURN_ALL =
      "uitags/optionTransfer/returnAll.vm";

  public static final String OPTION_TRANSFER_TRANSFER =
      "uitags/optionTransfer/transfer.vm";

  public static final String OPTION_TRANSFER_TRANSFER_ALL =
      "uitags/optionTransfer/transferAll.vm";

  public static final String PANEL = "uitags/panel/panel.vm";

  public static final String PANEL_ANCHOR = "uitags/panel/anchor.vm";

  public static final String PANEL_DRAG = "uitags/panel/drag.vm";

  public static final String PANEL_HIDE = "uitags/panel/hide.vm";

  public static final String PANEL_SHOW = "uitags/panel/show.vm";

  public static final String PANEL_STICK = "uitags/panel/stick.vm";

  public static final String SELECT = "uitags/select/select.vm";

  public static final String SHIFT = "uitags/shift/shift.vm";

  public static final String SORT = "uitags/sort/sort.vm";



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * Holds values that will go to the placeholders
   */
  private Map params = new HashMap();
  /**
   * The file containing the template requested by the client
   */
  private String file;



  ///////////////////////////////////////////
  ////////// Static initialization //////////
  ///////////////////////////////////////////

  static {
    init();
  }

  /**
   * Loads Velocity config file and uses it to initialize Velocity.
   *
   * @throws PropertiesLoadingException if failed to load the config file
   * @throws RuntimeException if Velocity throws exception during initialization
   */
  private static void init() {
    Properties props = loadProperties(CONFIG);
    try {
      Velocity.init(props);
    }
    catch (Exception e) {
      // Velocity.init() throws an Exception object, all we can do is wrap it
      // in a RuntimeException.
      throw new RuntimeException(e);
    }
  }

  /**
   * Loads Velocity config file.
   *
   * @param file name of the config file
   * @return loaded configuration entries
   * @throws PropertiesLoadingException if failed to load the config file
   */
  private static Properties loadProperties(String file) {
    Properties props = new Properties();
    try {
      props.load(Template.class.getResourceAsStream(file));
    }
    catch (IOException e) {
      throw new PropertiesLoadingException(e, file);
    }
    return props;
  }



  //////////////////////////////////
  ////////// Construction //////////
  //////////////////////////////////

  /**
   * Factory method for creating a template for the given name.
   *
   * @param name which template to create?
   * @return the template for the given name
   */
  public static Template forName(String name) {
    // The "name" is mapped to a file name.
    return new Template(name);
  }

  /**
   * Non-instantiable by client, called by the factory method.
   *
   * @param file the name of the file containing the requested template
   */
  private Template(String file) {
    this.file = file;
  }



  ////////////////////////////////////////
  ////////// For use by clients //////////
  ////////////////////////////////////////

  /**
   * Binds the specified value to a placeholder with the specified name.
   * The actual value substitution is not performed until {@link #eval()}
   * or {@link #evalToString()} is invoked.
   *
   * @param key the name of the placeholder to which the value is to be inserted
   * @param value the value that goes to the placeholder
   */
  public void map(String key, Object value) {
    if (key == null) {
      throw new NullPointerException(
          "Key of template parameter can't be null.");
    }
    this.params.put(key, value);
  }

  /**
   * Performs the actual substitution of placeholders with values that
   * have been previously supplied to {@link #map(String, Object)}.
   *
   * @return substitution result
   * @throws RuntimeException if failed to perform value substitution
   */
  public StringWriter eval() {
    map("velocityAdapter", new VelocityAdapter());

    StringWriter result = new StringWriter();
    try {
      Velocity.mergeTemplate(
          this.file, "ISO8859-1", new VelocityContext(this.params), result);
    }
    catch (ResourceNotFoundException e) {
      throw new RuntimeException(e);
    }
    catch (MethodInvocationException e) {
      throw new RuntimeException(e);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    return result;
  }

  /**
   * Same as {@link #eval()}, only return value is different.
   *
   * @return substitution result
   * @throws RuntimeException if failed to perform value substitution
   */
  public String evalToString() {
    return eval().toString();
  }
}
