/**
 * May 1, 2006
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
package net.sf.uitags.tag.panel;

import javax.servlet.jsp.JspException;

import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.tagutil.validation.DeferredValidationException;
import net.sf.uitags.tagutil.validation.RuntimeValidator;
import net.sf.uitags.util.Template;

/**
 * Injects panel hide capability in response to a certain event.
 *
 * @author hgani
 * @version $Id$
 */
public class ShowTag extends AbstractUiTag {

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
   * The 'injectTo' tag attribute.
   */
  private String injectTo;

  /**
   * The 'injectToName' tag attribute.
   */
  private String injectToName;

  /**
   * The "on" tag attribute
   */
  private String on;

  /**
   * The "delay" tag attribute
   */
  private int delayInMillis;

  /**
   * The "position" tag attribute
   */
  private boolean positionAttachedToMouse = false;


  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

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

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setOn(String val) {
    this.on = val;
  }

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setDelay(int val) {
    this.delayInMillis = val;
  }

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setPosition(String position) {
    if ("auto".equals(position)) {
      this.positionAttachedToMouse = false;
    }
    else if ("mouse".equals(position)) {
      this.positionAttachedToMouse = true;
    }
    else {
      throw new DeferredValidationException(
          "Position attribute must be either 'auto' or 'mouse'. Provided: '" +
          position + "'.");
    }
  }


  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////

  /**
   * Instructs web container to skip the tag's body.
   *
   * @see javax.servlet.jsp.tagext.Tag#doStartTag()
   * @return <code>SKIP_BODY</code>
   * @throws JspException to communicate error
   */
  public int doStartTag() throws JspException {
    return SKIP_BODY;
  }

  /**
   * Renders the HTML code for showing a panel.
   *
   * @see javax.servlet.jsp.tagext.Tag#doEndTag()
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  public int doEndTag() throws JspException {
    RuntimeValidator.assertAttributeExclusive(
        "injectTo", this.injectTo, "injectToName", this.injectToName);
    RuntimeValidator.assertEitherSpecified(
        "injectTo", this.injectTo, "injectToName", this.injectToName);

    PanelTag parent = (PanelTag) findParent(PanelTag.class);

    Template tpl = Template.forName(Template.PANEL_SHOW);
    tpl.map("triggerId", this.injectTo);
    tpl.map("triggerName", this.injectToName);
    tpl.map("triggerEvent", this.on);
    tpl.map("delayInMillis", new Integer(this.delayInMillis));
    tpl.map("attachedToMouse", String.valueOf(this.positionAttachedToMouse));

    parent.addChildJsCode(tpl.evalToString());

    return EVAL_PAGE;
  }
}
