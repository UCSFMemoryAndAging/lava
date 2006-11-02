/**
 * Jun 24, 2005
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
package net.sf.uitags.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.sf.uitags.tagutil.AttributeSupport;
import net.sf.uitags.tagutil.validation.TlvLeakageException;

/**
 * A tag that allows you to add an additional HTML attribute for the
 * enclosing JSP tag (must be a tag that has a corresponding HTML tag).
 *
 * @author hgani
 * @version $Id$
 */
public class AttributeTag extends AbstractUiTag {

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
   * The 'name' tag attribute.
   */
  private String name;
  /**
   * The 'value' tag attribute.
   */
  private String value;


  ///////////////////////////////////////////
  ////////// Tag attribute setters //////////
  ///////////////////////////////////////////

  /**
   * Tag attribute setter.
   *
   * @param name name of the tag attribute
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Tag attribute setter.
   *
   * @param value value of the tag attribute
   */
  public void setValue(String value) {
    this.value = value;
  }


  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////

  /**
   * Invokes the {@link AttributeSupport#addAttribute(String, String)} of
   * the enclosing tag.
   *
   * @see javax.servlet.jsp.tagext.Tag#doStartTag()
   * @return <code>SKIP_BODY</code>
   * @throws JspException to communicate error
   */
  public int doStartTag() throws JspException {
    Tag parentTag = getParent();
    if (!(parentTag instanceof AttributeSupport)) {
      throw new TlvLeakageException("Invalid use of tag outside " +
          "legitimate parent tag: " + parentTag.getClass().getName());
    }
    AttributeSupport attributeSupport = (AttributeSupport) parentTag;
    attributeSupport.addAttribute(this.name, this.value);

    return SKIP_BODY;
  }
}
