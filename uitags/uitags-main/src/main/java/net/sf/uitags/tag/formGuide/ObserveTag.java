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

/**
 * Notifies {@link net.sf.uitags.tag.formGuide.FormGuideTag} of widgets to observe.
 * 
 * (ctoohey) all FormGuide child tags now extends BaseChildTag instead of AbstractUiTag
 *
 * @author jonni
 * @version $Id$
 */
public class ObserveTag extends ObserveForNullTag {
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
   * The "forValue" tag attribute which the element's value must match for the observe rule
   * to hold. Can be a regular expression.
   */
  private String forValue;

  /**
   * (ctoohey)
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
  public ObserveTag() {
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
  public void setForValue(String val) {
    this.forValue = val;
  }

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
   * (ctoohey) Modified to allow specifying multiple element ids or names in
   * the observe tag (using comma separated format) so that if many observe
   * tags share the same attributes, only one observe tag need be used.
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  protected final void doEndTagWithoutRuntimeValidation() {
    FormGuideTag formGuideTag = (FormGuideTag) findParent(FormGuideTag.class);
    if (this.elementIds != null) {
    	String[] elementIdArray = elementIds.split(",");
    	for (String elementId : elementIdArray) {
    		if (this.component != null) {
    			elementId = component + "_" + elementId.trim();
    		}
    		formGuideTag.addObservedElementId(elementId.trim(), this.forValue, this.negate);
    		// special case for instrument data collection mode, where comboRadioSelect controls
    		//  are used. a comboRadioSelect control uses radio buttons for valid values and
    		//  has a select box which just contains missing data code options and the blank
    		//  option (the empty string).
    		// the element id in the observe tag refers to the radio button group portion of
    		//  the control
    		// currently, the assumption is that the observe tag for a comboRadioSelect will
    		//  be a valid value, not a missing data code, i.e. the forValue attribute for the
    		//  observe tag applies to the radio button group portion of the control. when the
    		//  control has a valid value, the value of the selectbox is blank. because of this,
    		//  the observe rule is configured such that a blank value for the select box means
    		//  the observe rule for that select box holds, which a missing data code for the
    		//  select box means the observe rule for the select box does not hold. an observe 
    		//  element is added for the selectbox portion of the control accordingly.
    		// note: select box element id is the element id of the radio button group with
    		//  _CODE appended
    		if (this.comboRadioSelect == Boolean.TRUE) {
    			formGuideTag.addObservedElementId(elementId.trim() + "_CODE", "", this.negate);
    		}
    	}
    }
    else if (this.elementNames != null) {
    	String[] elementNameArray = elementNames.split(",");
    	for (String elementName : elementNameArray) {
    		formGuideTag.addObservedElementName(elementName.trim(), this.forValue, this.negate);
    		// see comments on special case for comboRadioSelect controls above
    		if (this.comboRadioSelect == Boolean.TRUE) {
    			formGuideTag.addObservedElementName(elementName.trim() + "_CODE", "", this.negate);
    		}
    	}
    }
  }
}
