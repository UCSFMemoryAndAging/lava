/**
 * September 19, 2006
 * Charlie Toohey
 * SetValueTag
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
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.uitags.util.UiString;

/**
 * Sets specified widget to specified value if the observe rules are met. 
 *
 */

public class SetValueTag extends BaseChildTag {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 52L;

  /**
   * Javascript callback for elements.
   */
  static final String CALLBACK_METHOD = "setValue";



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The "value" tag attribute
   *
   * Value to set for the widget. For selectboxes, match option's value field to this to set selectbox.
   */
  private String value;

  /**
   * The "optionText" tag attribute
   *
   * For selectbox only, match option's text field against this to set value.
   */
  private String optionText;


  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public SetValueTag() {
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
  public void setValue(String val) {
    this.value = val;
  }

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setOptionText(String val) {
    this.optionText = val;
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
	  validateAttributes();
	  FormGuideTag formGuideTag = (FormGuideTag) findParent(FormGuideTag.class);
	    
	  String[] elementArray = null;
	  if (this.elementIds != null) {
		  elementArray = this.elementIds.split(",");
	  }
	  else if (this.elementNames != null) {
		  elementArray = this.elementNames.split(",");
	  }
	  for (String elementIdOrName : elementArray) {
   		  if (this.elementIds != null && this.component != null) {
	   	  	elementIdOrName = component + "_" + elementIdOrName.trim(); 
	   	  }
		  String idOrNameParam = getJsElementParam(elementIdOrName.trim());

		  // note: the setValue child tag has no undo action, since setValue simply
		  //       sets a widget to a value

		  formGuideTag.addJavascriptCallback(
				UiString.simpleConstruct(
						"{0}(domEvent, {1}, {2}, {3}, {4});",
						new String[] { CALLBACK_METHOD, (this.elementIds != null ? idOrNameParam : null), 
								(this.elementNames != null ? idOrNameParam : null), 
								getJsElementParam(this.value), getJsElementParam(this.optionText)}),
				null);
	  }
    return EVAL_PAGE;
  }
}
