/**
 * November 17, 2006
 * author: ctoohey
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

/**
 * Notifies {@link net.sf.uitags.tag.formGuide.FormGuideTag} of widgets to observe.
 * 
 * (ctoohey) all FormGuide child tags now extends BaseChildTag instead of AbstractUiTag
 *
 * @author jonni
 * @version $Id$
 */
public class DependsTag extends BaseChildTag {
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
  public DependsTag() {
    super();
  }



  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

  /**
   * author:ctoohey
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
	  super.doStartTag();
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
   * Designate all elements upon which the formGuide tag is dependent, beyond
   * those elements in the ignore and observe tags. This will result in
   * adding this formGuide tag to the event handlers for those elements, so
   * when they change, the skip logic in this formGuide tag executes. 
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  public int doEndTag() throws JspException {
    validateAttributes();
    FormGuideTag formGuideTag = (FormGuideTag) findParent(FormGuideTag.class);
    if (this.elementIds != null) {
    	String[] elementIdArray = elementIds.split(",");
    	for (String elementId : elementIdArray) {
    		if (this.component != null) {
    			elementId = component + "_" + elementId.trim();
    		}
    		formGuideTag.addDependsElementId(elementId.trim());
    		if (this.comboRadioSelect == Boolean.TRUE) {
    			formGuideTag.addDependsElementId(elementId.trim() + "_CODE");
    		}
    	}
    }
    else if (this.elementNames != null) {
    	String[] elementNameArray = elementNames.split(",");
    	for (String elementName : elementNameArray) {
    		formGuideTag.addDependsElementName(elementName.trim());
    		// see comments on special case for comboRadioSelect controls above
    		if (this.comboRadioSelect == Boolean.TRUE) {
    			formGuideTag.addDependsElementName(elementName.trim() + "_CODE");
    		}
    	}
    }
    return EVAL_PAGE;
  }
}
