/**
 * Nov 20, 2004
 *
 * Copyright 2004 uitags
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
package net.sf.uitags.tag.optionTransfer;

import javax.servlet.jsp.JspException;

import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.tagutil.validation.RuntimeValidator;
import net.sf.uitags.util.Template;

/**
 * Injects the functionality to transfer selected items in the source
 * box to the target box of the option transfer suite.
 *
 * @author hgani
 * @version $Id$
 */
public class TransferTag extends AbstractUiTag {

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
   * The 'injectTo' tag attribute.
   */
  private String injectTo;

  /**
   * The 'injectToName' tag attribute.
   */
  private String injectToName;


  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setInjectTo(String val) {
    this.injectTo = val;
  }

  /**
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setInjectToName(String val) {
    this.injectToName = val;
  }


  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////

  /**
   * Prints the necessary Javascript code.
   *
   * @see javax.servlet.jsp.tagext.Tag#doStartTag()
   * @return <code>SKIP_BODY</code>
   * @throws JspException to communicate error
   */
  public int doStartTag() throws JspException {
    RuntimeValidator.assertAttributeExclusive(
        "injectTo", this.injectTo, "injectToName", this.injectToName);
    RuntimeValidator.assertEitherSpecified(
        "injectTo", this.injectTo, "injectToName", this.injectToName);

    Template tpl = Template.forName(Template.OPTION_TRANSFER_TRANSFER);
    tpl.map("triggerId", this.injectTo);
    tpl.map("triggerName", this.injectToName);

    OptionTransferTag parent = (OptionTransferTag) findParent(
        OptionTransferTag.class);
    parent.addChildJsCode(tpl.evalToString());

    return SKIP_BODY;
  }
}
