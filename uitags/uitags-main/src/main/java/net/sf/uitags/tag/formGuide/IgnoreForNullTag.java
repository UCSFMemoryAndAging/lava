/**
 * November 1, 2006
 * Charlie Toohey
 * IgnoreForNullTag
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
 * Notifies {@link net.sf.uitags.tag.formGuide.FormGuideTag} of ignore rules that can
 * cause formGuide tag as a whole to be ignored.
 */
public class IgnoreForNullTag extends BaseChildTag {
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
   * The "negate" tag attribute 
     "true" means widget value NOT equal forValue. 
     "false" means widget value equals forValue.
     defaults to "false"
   */
  protected Boolean _negate; // setter only
  protected Boolean negate;




  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public IgnoreForNullTag() {
    super();
  }



  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

  /**
   * author: ctoohey
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
     "true" means widget value NOT equal forValue. 
     "false" means widget value equals forValue.
     defaults to "false"
   */
  public void setNegate(Boolean val) {
    this.negate = val;
  }



  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////
  
  public int doStartTag() throws JspException {
      // set defaults
	  // technique for setting attribute default values. see BaseSkipUnskipTag for details.
	  this.negate = this._negate;
      if (this.negate == null) {
	    this.negate = new Boolean(false);
      }
      return SKIP_BODY;
  }

  /**
   * Performs runtime validation and delegates to the overridable method
   * {@link #doEndTagWithoutRuntimeValidation()}.
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  public final int doEndTag() throws JspException {
	  validateAttributes();

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
    
    if (this.elementIds != null) {
    	String[] elementIdArray = elementIds.split(",");
    	for (String elementId : elementIdArray) {
       		if (this.component != null) {
    			elementId = component + "_" + elementId.trim();
    		}
    		formGuideTag.addIgnoreElementId(elementId.trim(), null, this.negate);
    	}
    }
    else if (this.elementNames != null) {
    	String[] elementNameArray = elementNames.split(",");
    	for (String elementName : elementNameArray) {
    		formGuideTag.addIgnoreElementName(elementName.trim(), null, this.negate);
    	}    		
    }
  }
}
