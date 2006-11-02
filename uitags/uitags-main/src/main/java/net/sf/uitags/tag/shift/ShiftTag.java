/**
 * Jan 31, 2006
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
package net.sf.uitags.tag.shift;

import javax.servlet.jsp.JspException;

import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.tagutil.TaglibProperties;
import net.sf.uitags.tagutil.validation.RuntimeValidationException;
import net.sf.uitags.tagutil.validation.RuntimeValidator;
import net.sf.uitags.util.Template;


/**
 * Renders a Javascript button which shifts (upwards or downwards)
 * a group options in a select box.
 *
 * @see TaglibProperties
 * @author hgani
 * @version $Id$
 */
public class ShiftTag extends AbstractUiTag {

  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 100L;



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The "type" tag attribute
   */
  private ShortcutType shortcutType;
  /**
   * The "applyTo" tag attribute
   */
  private String applyTo;
  /**
   * The "applyToName" tag attribute
   */
  private String applyToName;
  /**
   * The "injectTo" tag attribute
   */
  private String injectTo;
  /**
   * The "injectToName" tag attribute
   */
  private String injectToName;



  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public ShiftTag() {
    super();
  }



  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setType(String val) {
    this.shortcutType = ShortcutType.findByName(val);
  }

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setApplyTo(String val) {
    this.applyTo = val;
  }

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setApplyToName(String val) {
    this.applyToName = val;
  }

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setInjectTo(String val) {
    this.injectTo = val;
  }

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setInjectToName(String val) {
    this.injectToName = val;
  }



  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////

  /**
   * Instructs web container to evaluate the tag's body.
   *
   * @see javax.servlet.jsp.tagext.Tag#doStartTag()
   * @return <code>EVAL_BODY_INCLUDE</code>
   * @throws JspException to communicate error
   */
  public int doStartTag() throws JspException {
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Prints out HTML code to construct the button.
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  public int doEndTag() throws JspException {
    RuntimeValidator.assertAttributeExclusive(
        "applyTo", this.applyTo, "applyToName", this.applyToName);
    RuntimeValidator.assertAttributeExclusive(
        "injectTo", this.injectTo, "injectToName", this.injectToName);

    RuntimeValidator.assertEitherSpecified(
        "applyTo", this.applyTo, "applyToName", this.applyToName);
    RuntimeValidator.assertEitherSpecified(
        "injectTo", this.injectTo, "injectToName", this.injectToName);

    Template tpl = Template.forName(Template.SHIFT);
    tpl.map("callback", this.shortcutType.getCallbackName());
    tpl.map("applyTo", this.applyTo);
    tpl.map("applyToName", this.applyToName);
    tpl.map("injectTo", this.injectTo);
    tpl.map("injectToName", this.injectToName);
    println(tpl.evalToString());

    return EVAL_PAGE;
  }



  ///////////////////////////////////
  ////////// Inner classes //////////
  ///////////////////////////////////

  /**
   * The type of the shortcut, which is either "up", "down",
   * "first", or "last".
   *
   * @author hgani
   */
  private abstract static class ShortcutType {
    /// Constants ///

    /**
     * The "up" type
     */
    private static final ShortcutType UP = new ShortcutType("up") {
      String getCallbackName() {
        return "createUpSuite";
      }
    };

    /**
     * The "down" type
     */
    private static final ShortcutType DOWN = new ShortcutType("down") {
      String getCallbackName() {
        return "createDownSuite";
      }
    };

    /**
     * The "first" type
     */
    private static final ShortcutType FIRST = new ShortcutType("first") {
      String getCallbackName() {
        return "createFirstSuite";
      }
    };

    /**
     * The "last" type
     */
    private static final ShortcutType LAST = new ShortcutType("last") {
      String getCallbackName() {
        return "createLastSuite";
      }
    };

    /// Fields ///

    /**
     * Name of the shortcut type
     */
    private final String name;

    /// Constructors ///

    /**
     * Non-instantiable by client.
     *
     * @param name the name of the shortcut type
     */
    private ShortcutType(String name) {
      this.name = name;
    }

    /// Methods ///

    /**
     * Returns the Javascript function name for callback.
     *
     * @return the name
     */
    abstract String getCallbackName();

    /**
     * Returns the shortcut type given the name.
     *
     * @param name the name of the shortcut type to return
     * @return the shortcut type to return
     * @throws RuntimeValidationException if supplied shortcut name is invalid
     */
    static ShortcutType findByName(String name) {
      if (name.equals(UP.name)) {
        return UP;
      }
      else if (name.equals(DOWN.name)) {
        return DOWN;
      }
      else if (name.equals(FIRST.name)) {
        return FIRST;
      }
      else if (name.equals(LAST.name)) {
        return LAST;
      }

      // assert is not used as the "name" value comes from JSP, which can be
      // anything
      throw new RuntimeValidationException("Invalid type: '" + name + "'.");
    }
  }
}