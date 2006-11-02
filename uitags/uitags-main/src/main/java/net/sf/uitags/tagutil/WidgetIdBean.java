/**
 * May 9, 2006
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

import javax.servlet.jsp.PageContext;

public class WidgetIdBean {
  private static final String INSTANCE_KEY = WidgetIdBean.class.getName();

  private String id;
  private String name;
  private PageContext pageContext;

  public void setPageContext(PageContext pageContext) {
    this.pageContext = pageContext;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns widget's ID. Auto-generates unique ID if necessary.
   */
  public String getId() {
    if (this.id == null && this.name == null) {
      long instanceId = ScopedIdGenerator.nextId(
          PageContext.REQUEST_SCOPE, INSTANCE_KEY, this.pageContext) - 1;
      this.id = INSTANCE_KEY + instanceId;
      this.pageContext.getRequest().setAttribute("id",  this.id);
    }

    // If one of ID or name is not null, return the ID without auto-generation.
    return this.id;
  }

  /**
   * Returns widget's ID. Auto-generates unique ID if necessary.
   */
  public String getName() {
    return this.name;
  }
}
