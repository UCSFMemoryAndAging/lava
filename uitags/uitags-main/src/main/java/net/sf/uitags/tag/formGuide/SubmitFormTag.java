/**
 * September 19, 2006
 * Charlie Toohey
 * SubmitFormTag
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

import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.util.UiString;

/**
 * Submits specified form with specified action event as a request parameter when the onChange 
 * event occurs for the observed widget(s) and observe rules are met.
 *
 * @author jonni
 * @version $Id$
 */
public class SubmitFormTag extends AbstractUiTag {
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
  static final String CALLBACK_METHOD = "submitForm";



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * The "form" tag attribute
   */
  private String form;

  /**
   * The "actionEvent" tag attribute
   */
  private String event;



  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public SubmitFormTag() {
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
  public void setForm(String val) {
    this.form = val;
  }

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setEvent(String val) {
    this.event = val;
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
	  FormGuideTag formGuideTag = (FormGuideTag) findParent(FormGuideTag.class);
	    
	  // there is no undo task for a submitForm child tag, because it does not make 
	  // sense; once the form is submitted it is gone.
	  formGuideTag.addJavascriptCallback(
			UiString.simpleConstruct(
					"{0}(domEvent, document.{1}, {2});",
					new String[] { CALLBACK_METHOD,	getJsElementParam(this.form), getJsElementParam(this.event)}),
			null);
    return EVAL_PAGE;
  }
}
