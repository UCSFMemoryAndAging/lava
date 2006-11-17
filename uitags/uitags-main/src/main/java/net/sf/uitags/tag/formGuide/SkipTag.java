/**
 * May 16, 2006
 * Charlie Toohey
 * SkipTag
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
 * Notifies {@link net.sf.uitags.FormGuideTag} of widgets to logically skip.
 *
 * @author jonni
 * @version $Id$
 */
public class SkipTag extends BaseSkipUnskipTag {
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
  static final String CALLBACK_METHOD = "skipElement";


  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////


  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public SkipTag() {
    super();
  }



  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////



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
	  return doEndTag(CALLBACK_METHOD, UnskipTag.CALLBACK_METHOD, 
			  this.skipValue, this.skipOptionText, this.unskipValue, this.unskipOptionText,
			  FormGuideTag.LOGICAL_SKIP_CODE, FormGuideTag.LOGICAL_SKIP_TEXT, "", "");
  }
}
