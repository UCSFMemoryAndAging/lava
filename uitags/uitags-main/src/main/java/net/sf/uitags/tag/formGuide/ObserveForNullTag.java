/**
 * Jan 16, 2005
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
 * Notifies {@link net.sf.uitags.tag.formGuide.FormGuideTag} of widgets to observe.
 *
 * @author jonni
 * @version $Id$
 */
public class ObserveForNullTag extends AbstractUiTag {
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
   * The "elementId" tag attribute
   */
  protected String elementId;
  /**
   * The "elementName" tag attribute
   */
  protected String elementName;



  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public ObserveForNullTag() {
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
   * Performs runtime validation and delegates to the overridable method
   * {@link #doEndTagWithoutRuntimeValidation()}.
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  public final int doEndTag() throws JspException {
    RuntimeValidator.assertAttributeExclusive(
        "elementId", this.elementId, "elementName", this.elementName);
    RuntimeValidator.assertEitherSpecified(
        "elementId", this.elementId, "elementName", this.elementName);

    doEndTagWithoutRuntimeValidation();

    return EVAL_PAGE;
  }

  /**
   * Performs the actual logic of {@link #doEndTag}. This implementation
   * communicates with the parent tag ({@link FormGuideTag}).
   *
   * @throws JspException to communicate error
   */
  protected void doEndTagWithoutRuntimeValidation() {
    FormGuideTag formGuideTag = (FormGuideTag) findParent(FormGuideTag.class);
    if (this.elementId != null) {
      formGuideTag.addObservedElementId(this.elementId, null);
    }
    else if (this.elementName != null) {
      formGuideTag.addObservedElementName(this.elementName, null);
    }
  }
}
