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
package net.sf.uitags.tagutil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.uitags.util.UiString;

/**
 * A helper for tags that support arbitrary HTML tag attributes.
 *
 * @author hgani
 * @version $Id$
 */
public class AttributeSupportHelper implements AttributeSupport {
  /**
   * HTML tag attribute values keyed on attribute names.
   */
  private Map attributeMap;

  /**
   * Constructs a new helper.
   */
  public AttributeSupportHelper() {
    this.attributeMap = new HashMap();
  }

  /** {@inheritDoc} */
  public void addAttribute(String name, String value) {
    this.attributeMap.put(name, value);
  }

  /**
   * Returns HTML code representing the specified attributes preceeded by
   * a space, therefore the generated code can be appended to an existing
   * HTML tag without any need to modify the code. 
   *
   * @return the HTML code
   */
  public String eval() {
    StringBuffer buffer = new StringBuffer();
    for (
        Iterator iter = this.attributeMap.entrySet().iterator(); 
        iter.hasNext(); ) {
      Map.Entry entry = (Map.Entry) iter.next();
      buffer.append(UiString.simpleConstruct(" {0}=\"{1}\"", new String[] { 
          (String) entry.getKey(), (String) entry.getValue() }));
    }
    return buffer.toString();
  }
}
