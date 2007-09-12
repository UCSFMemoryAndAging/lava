/**
 * Jan 18, 2005
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
 * Notifies {@link net.sf.uitags.tag.formGuide.FormGuideTag} of widgets to enable.
 * 
 * (ctoohey) all FormGuide child tags now extends BaseChildTag instead of AbstractUiTag
 *
 * @author jonni
 * @author hgani
 * @version $Id$
 */
public class EnableTag extends BaseChildTag {
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
  static final String CALLBACK_METHOD = "enableElements";


  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The "comboRadioSelect" tag attribute. The flag indicating whether the widget is a
   * comboRadioSelect (true), or not (false). Special processing is done for comboRadioSelect
   * widgets.
   */
  private Boolean _comboRadioSelect; // setter only
  private Boolean comboRadioSelect;


  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public EnableTag() {
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
  
  public void setComboRadioSelect(Boolean val) {
    this._comboRadioSelect = val;
  }


  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////

  public int doStartTag() throws JspException {
      // set defaults
	  // technique for setting attribute default values. see BaseSkipUnskipTag for details.
	  this.comboRadioSelect = this._comboRadioSelect;
      if (this.comboRadioSelect == null) {
    	  this.comboRadioSelect = new Boolean(false);
      }

      return SKIP_BODY;
  }
  

  
  /**
   * Communicates with the parent tag ({@link FormGuideTag}).
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  public int doEndTag() throws JspException {
	  validateAttributes();

	  FormGuideTag formGuideTag = (FormGuideTag) findParent(FormGuideTag.class);
	  formGuideTag.addJavascriptCallback(CALLBACK_METHOD, DisableTag.CALLBACK_METHOD, 
			  this.elementIds, this.elementNames, this.component);

	  if (this.comboRadioSelect) {
		  // generate the select box elements that correspond to the radio button elements
		  // by appending _CODE to the element id or name, create a comma separated list 
		  // then add do/undo (enable/disable) actions as javascript callbacks for these 
		  // elements 
		  
		  String[] elementArray = null;
		  StringBuffer selectElementIds = null;
		  StringBuffer selectElementNames = null;
		  if (this.elementIds != null) {
		   	elementArray = this.elementIds.split(",");
			selectElementIds = new StringBuffer();
		  }
		  else if (this.elementNames != null) {
		   	elementArray = this.elementNames.split(",");
			selectElementNames = new StringBuffer();
		  }
		  boolean firstElement = true;
		  for (String elementIdOrName : elementArray) {
		  		if (this.elementIds != null) {
		  			if (!firstElement) {
		  				selectElementIds.append(",");
		  			}
		  			selectElementIds.append(elementIdOrName.trim() + "_CODE");
		  		}
		  		else {
		  			if (!firstElement) {
		  				selectElementNames.append(",");
		  			}
		  			selectElementNames.append(elementIdOrName.trim() + "_CODE");
		  		}
		  		firstElement = false;
		  }
		  
          formGuideTag.addJavascriptCallback(CALLBACK_METHOD, DisableTag.CALLBACK_METHOD, 
        		  (selectElementIds != null ? selectElementIds.toString() : null), 
        		  (selectElementNames != null ? selectElementNames.toString() : null), this.component);

	  }	  
    return EVAL_PAGE;
  }
}
