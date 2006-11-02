/**
 * Jan 31, 2006
 *
 * Copyright 2004 - 2005 uitags
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
package net.sf.uitags.tag.formGuide;

import javax.servlet.jsp.JspException;

import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.tagutil.validation.RuntimeValidator;

/**
 * Notifies {@link net.sf.uitags.tag.formGuide.FormGuideTag} of elements to remove.
 *
 * @author hgani
 * @version $Id$
 */
public class RemoveTag extends AbstractUiTag {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 100L;

  /**
   * Javascript callback for elements.
   */
  public static final String CALLBACK_METHOD = "removeElements";


  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The "elementId" tag attribute
   */
  private String elementId;
  /**
   * The "widgetName" tag attribute
   */
  private String elementName;



  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public RemoveTag() {
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
  public void setElementId(String val) {
    this.elementId = val;
  }

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setElementName(String val) {
    this.elementName = val;
  }


  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////

  /**
   * Communicates with the parent tag ({@link FormGuideTag}).
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  public int doEndTag() throws JspException {
    RuntimeValidator.assertAttributeExclusive(
        "elementId", this.elementId, "elementName", this.elementName);
    RuntimeValidator.assertEitherSpecified(
        "elementId", this.elementId, "elementName", this.elementName);

    FormGuideTag formGuideTag = (FormGuideTag) findParent(FormGuideTag.class);
    formGuideTag.addJavascriptCallback(CALLBACK_METHOD,
        InsertTag.CALLBACK_METHOD, this.elementId, this.elementName);

    return EVAL_PAGE;
  }
}
