/**
 * November 11, 2006
 * Charlie Toohey
 * BaseSkipUnskipTag
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

import net.sf.uitags.util.UiString;

/**
 * Parent of formGuide skip and unskip child tags.
 *
 * @author jonni
 * @version $Id$
 */
public class BaseSkipUnskipTag extends BaseChildTag {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The "skipValue" tag attribute. The value to give the widget that is skipped.
   * For selectboxes, matched against the option's value field to set the selectbox.
   * note: presently not implemented for acs select boxes. they have predefined
   *       skipValue and skipOptionText
   */
  protected String _skipValue; // setter 
  protected String skipValue; // used
  
  /**
   * The "skipOptionText" tag attribute. For selectboxes only, matched against the option's
   * text field to set the selectbox selected option. 
   * note: presently not implemented for acs select boxes. they have predefined
   *       skipValue and skipOptionText
   */
  protected String _skipOptionText; // setter
  protected String skipOptionText; // used
  
  /**
   * The "unskipValue" tag attribute. The value to give the widget value when it 
   * is unskipped, i.e. when undo action causes a skipped widget to be unskipped.
   * For selectboxes, matched against the option's value field to set the selectbox.
   * note: presently not implemented for acs select boxes. they have predefined
   *       unskipValue and unskipOptionText
   *
   * note: while the skip tag is used to skip a field, based on some observation being 
   * true, when that observation is false, the field is unskipped, which is why this
   * attribute is needed, and vice versa
   */
  protected String _unskipValue; // setter
  protected String unskipValue;
  /**
   * The "unskipOptionText" tag attribute. For selectboxes only, matched against the option's
   * text field to set the selectbox selected option. 
   * note: presently not implemented for acs select boxes. they have predefined
   *       unskipValue and unskipOptionText
   *
   * note: while the skip tag is used to skip a field, based on some observation being 
   * true, when that observation is false, the field is unskipped, which is why this
   * attribute is needed, and vice versa
   */
  protected String _unskipOptionText; // setter
  protected String unskipOptionText;
  
  /**
   * The "comboRadioSelect" tag attribute. The flag indicating whether the widget is a
   * comboRadioSelect (true), or not (false). Special processing is done for comboRadioSelect
   * widgets.
   */
  protected Boolean _comboRadioSelect; // setter
  protected Boolean comboRadioSelect;


  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////


  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setSkipValue(String val) {
    this._skipValue = val;
  }
  
  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setSkipOptionText(String val) {
    this._skipOptionText = val;
  }
  
  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setUnskipValue(String val) {
    this._unskipValue = val;
  }
  
  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setUnskipOptionText(String val) {
    this._unskipOptionText = val;
  }
  
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
	  
	  // the JSP container reuses classic tag handler instances, for any tags that are called with the
	  // same attributes present (not the same attribute values, just the same attributes). this means 
	  // that instance variables are shared across different calls to the tag, even across different 
	  // pages. because of this, the fields corresponding to tag attributes should be treated as 
	  // read only, i.e. they should only be settable via their setter methods, and only the JSP
	  // container should call the setter methods. the attribute variables should not be modified
	  // by the tag code. this means that member fields can not be initialized in the constructor,
	  // because the constructor may only called the first time the tag is used. non-attribute
	  // member field initialization should take place in doStartTag. 
	  
	  // the trick to having default values for attributes that are not supplied is to have a
	  // second member field corresponding to each attribute. in doStartTag (which is called after
	  // the attribute setter methods) if no attribute was supplied (i.e. attribute is null), 
	  // assign the default to the corresponding field, and use that corresponding field throughout the code.
	  // since the instance is only shared by callers that supply the same attributes, the unsupplied
	  // attributes will always remain null and will always be initialized to their default values.
	  // callers that do supply these attributes will use a different instance of the tag, so there
	  // is no conflict.

	  // attribute fields in this class are defined with a leading '_' to distinguish them from
	  // their corresponding field which is the same but without the leading '_'
	  // note: this field naming convention is presently used for attributes which are set to 
	  //       defaults in doStartTag if they are not supplied. attributes that do not have default
	  //       values, e.g. required attributes, do not follow this naming convention; the
	  //       attribute is named without the leading '_' and used throughout the code
	  // TODO: go thru all tags and change attribute field names to have leading '_' to be consistent

      // set defaults based on use in instrument data enty, data collection, as defaults can
      //  be logically determined for that functionality. when used for other functionality,
      //  the skipTextValue/unskipTextValue and skipOptionValue/unskipOptionValue attributes 
	  //  should be supplied to the tag

	  this.comboRadioSelect = this._comboRadioSelect;
      if (this.comboRadioSelect == null) {
    	  this.comboRadioSelect = new Boolean(false);
      }

      this.skipValue = this._skipValue;
      if (this.skipValue == null) {
    	  if (this.comboRadioSelect) {
    		  // if comboRadioSelect, skipOptionText is set for the radiobutton portion, and doEndTag
    		  // method supplies the appropriate defaults for the selectbox portion.
              // set radio button to special value to indicate value should come from the select box
    		  this.skipValue = FormGuideTag.COMBO_RADIO_SELECT_USE_SELECT;
    	  }
          else {
              this.skipValue = FormGuideTag.LOGICAL_SKIP_CODE;
          }
      }
      this.skipOptionText = this._skipOptionText;
      if (this.skipOptionText == null) {
    	  if (this.comboRadioSelect) {
    		  // since this is for a radio button, option text does not apply; setting skipValue 
    		  // above is enough to select the correct radio button
          }
          else {
        	  this.skipOptionText = FormGuideTag.LOGICAL_SKIP_TEXT;
          }
      }

      this.unskipValue = this._unskipValue;
      if (this.unskipValue == null) {
        if (this.comboRadioSelect) {
        	// if comboRadioSelect, unskipValue is set for the radiobutton portion, and
        	// doEndTag method supplies the appropriate defaults for the selectbox portion.
        	// set radio button to special value to indicate value should come from the select 
        	// so that the unskip effect is that not radio button is visually selected, and
        	// the select box is blank (as set by the defaults in doEndTag)
        	this.unskipValue = FormGuideTag.COMBO_RADIO_SELECT_USE_SELECT;
        }
        else {
            this.unskipValue = "";
        }
      }
      this.unskipOptionText = this._unskipOptionText; 
      if (this.unskipOptionText == null) {
    	  if (this.comboRadioSelect) {
          	// since this is for a radio button, option text does not apply; setting unskipTextValue 
          	// above is enough to select the correct radio button
    	  }
          else {
          	this.unskipOptionText = "";
          }
      }

      return SKIP_BODY;
  }

  /**
   * Communicates with the parent tag ({@link FormGuideTag}).
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  public int doEndTag(String doCallback, String undoCallback,
		  				String doValue, String doOptionText,
		  				String undoValue, String undoOptionText,
		  				String doValueComboRadioSelect, String doOptionTextComboRadioSelect,
		  				String undoValueComboRadioSelect, String undoOptionTextComboRadioSelect) throws JspException {
	  
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
   		formGuideTag.addJavascriptCallback(
   				UiString.simpleConstruct(
   						"{0}(domEvent, {1}, {2}, {3}, {4});",
   						new String[] { doCallback, (this.elementIds != null ? idOrNameParam : null), (this.elementNames != null ? idOrNameParam : null), getJsElementParam(doValue), getJsElementParam(doOptionText) }),
           		UiString.simpleConstruct(
           				"{0}(domEvent, {1}, {2}, {3}, {4});",
           				new String[] { undoCallback, (this.elementIds != null ? idOrNameParam : null), (this.elementNames != null ? idOrNameParam : null), getJsElementParam(undoValue), getJsElementParam(undoOptionText) }));
    		
   		// also skip the select box if this is a comboRadioSelect control
   		if (this.comboRadioSelect) {
   			// since skipTextValue and unskipTextValue are set up for the radio button portion of this control,
   			// do not use these and pass in what should be used for skipping and unskipping the select box portion of 
   			// the the comboRadioSelect
   	   		String idOrNameComboRadioSelectParam = getJsElementParam(elementIdOrName.trim() + "_CODE");
       		formGuideTag.addJavascriptCallback(
       				UiString.simpleConstruct(
       						"{0}(domEvent, {1}, {2}, {3}, {4});",
       						new String[] { doCallback, (this.elementIds != null ? idOrNameComboRadioSelectParam : null), (this.elementNames != null ? idOrNameComboRadioSelectParam : null), getJsElementParam(doValueComboRadioSelect), getJsElementParam(doOptionTextComboRadioSelect) }),
               		UiString.simpleConstruct(
               				"{0}(domEvent, {1}, {2}, {3}, {4});",
               				new String[] { undoCallback, (this.elementIds != null ? idOrNameComboRadioSelectParam : null), (this.elementNames != null ? idOrNameComboRadioSelectParam : null), getJsElementParam(undoValueComboRadioSelect), getJsElementParam(undoOptionTextComboRadioSelect) }));
   		}
    }
    return EVAL_PAGE;
  }
  
}
