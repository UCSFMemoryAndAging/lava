/**
 * November 11, 2006
 * Charlie Toohey
 * BaseChildTag
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
 * Parent of all formGuide child tags. 
 *
 * @author jonni
 * @version $Id$
 */
public class BaseChildTag extends AbstractUiTag {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The "elementId" tag attribute. A String of comma-separated elementId's.
   */
  protected String elementIds;
  /**
   * The "elementName" tag attribute. A String of comma-separated elementName's.
   * An alternative to specifying elementIds.
   */
  protected String elementNames;
  /**
   * The "skipTextValue" tag attribute. The value to give the widget that is skipped.
   */
  
  /**
   * The optional "component" tag attribute. If present and elementIds are used to specify
   * the elements, the value and an '_' are prepended to each elementId, to construct the
   * element ids used in the LavaWeb component handler design. This allows the user 
   * to just specify the property portion of elements in elementIds, and just specify the
   * component once. It can especially be helpful where a JSTL EL expression is used to
   * derive the component value in the page so that this expression does not have to be present
   * for every elementId, when many elementIds are used.
   * 
   * It is also a required attribute when the element represents a radio button group when
   * using elementIds to specify elements. In this case there is no matching element id, and 
   * the element name must be used to find the array of individual radio button elements.
   * The component is used to construct the element name based on the names used in the 
   * LavaWeb component handler design.
   */
  protected String component;

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
  public void setElementIds(String val) {
    this.elementIds = val;
  }
  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setElementNames(String val) {
    this.elementNames = val;
  }
  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setComponent(String val) {
    this.component = val;
  }

  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////
  protected void validateAttributes() {
	  RuntimeValidator.assertAttributeExclusive(
	        "elementIds", this.elementIds, "elementNames", this.elementNames);
	  RuntimeValidator.assertEitherSpecified(
	        "elementIds", this.elementIds, "elementNames", this.elementNames);
  }
  

}
