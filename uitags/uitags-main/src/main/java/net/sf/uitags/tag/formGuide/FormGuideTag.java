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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import net.sf.uitags.tag.AbstractUiTag;
import net.sf.uitags.tagutil.ScopedIdGenerator;
import net.sf.uitags.util.Template;
import net.sf.uitags.util.UiString;

/**
 * Renders JavaScript for enabling/disabling widgets.
 *
 * @author jonni
 * @version $Id$
 */
public class FormGuideTag extends AbstractUiTag {
  ///////////////////////////////
  ////////// Constants //////////
  ///////////////////////////////

  /**
   * Serial Version UID.
   */
  private static final long serialVersionUID = 100L;

  /**
   * Key of the scoped attribute that stores auto-incremented ID for
   * instances of this tag handler.
   */
  private static final String TAG_INSTANCE_ID_KEY =
      "net.sf.uitags.FormGuideTag.instanceId";



  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * Widgets to observe
   */
  private List observedWidgets;
  /**
   * List of JS tasks to perform on "do"
   */
  private List doTasks;
  /**
   * List of JS tasks to perform on "undo"
   */
  private List undoTasks;
  /**
   * The "listener" tag attribute
   */
  private String listener;


  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public FormGuideTag() {
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
  public void setListener(String val) {
    this.listener = val;
  }


  ///////////////////////////////
  ////////// Tag logic //////////
  ///////////////////////////////

  /**
   * Performs initialization.
   *
   * @return <code>EVAL_BODY_INCLUDE</code>
   * @throws JspException to communicate error
   */
  public int doStartTag() throws JspException {
    // Initialize here to avoid tag reuse problem
    this.observedWidgets   = new ArrayList();
    this.doTasks           = new ArrayList();
    this.undoTasks         = new ArrayList();

    makeVisibleToChildren();
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Prints out HTML code to create the form guide.
   *
   * @return <code>EVAL_PAGE</code>
   * @throws JspException to communicate error
   */
  public int doEndTag() throws JspException {
    // Get unique tag instance ID
    long instanceId = ScopedIdGenerator.nextId(
        PageContext.REQUEST_SCOPE, TAG_INSTANCE_ID_KEY, this.pageContext);

    Template template = Template.forName(Template.FORM_GUIDE);
    template.map("instanceId",      new Long(instanceId));
    template.map("observedWidgets", this.observedWidgets);
    template.map("doTasks",         this.doTasks);
    template.map("undoTasks",       this.undoTasks);
    template.map("listener",        this.listener);


    println(template.evalToString());

    makeInvisibleFromChildren();
    return EVAL_PAGE;
  }

  /**
   * Lets child tags add widget name to observe.
   *
   * @param elementName name of the widget to observe
   * @param value value of the widget to observe
   */
  void addObservedElementName(String elementName, String value) {
    this.observedWidgets.add(new ObservedWidget(null, elementName, value));
  }

  /**
   * Lets child tags add widget ID to observe.
   *
   * @param elementId ID of the widget to observe
   * @param value value of the widget to observe
   */
  void addObservedElementId(String elementId, String value) {
    this.observedWidgets.add(new ObservedWidget(elementId, null, value));
  }

  /**
   * Lets child tags specify javascript callback statement.
   *
   * @param doCallback the doCallback statement
   * @param undoCallback the undoCallback statement
   * @param element the ID/name of the involved element
   */
  void addJavascriptCallback(
      String doCallback, String undoCallback,
      String elementId, String elementName) {

    String idParam = getJsElementParameter(elementId);
    String nameParam = getJsElementParameter(elementName);

    this.doTasks.add(UiString.simpleConstruct(
        "{0}(domEvent, {1}, {2});",
        new String[] { doCallback, idParam, nameParam }));

    this.undoTasks.add(UiString.simpleConstruct(
        "{0}(domEvent, {1}, {2});",
        new String[] { undoCallback, idParam, nameParam }));
  }

  private String getJsElementParameter(String value) {
    if (value == null) {
      return "null";
    }
    return '"' + value + '"';
  }


  ///////////////////////////////////
  ////////// Inner classes //////////
  ///////////////////////////////////

  /**
   * Simple data container class, holding name and value of observed widgets.
   * Made public to allow access by Java bean tools.
   */
  public static final class ObservedWidget {
    /**
     * Widget ID
     */
    private String id;
    /**
     * Widget name
     */
    private String name;
    /**
     * Widget value
     */
    private String value;

    /**
     * Creates a data container instance.
     *
     * @param name  widget name
     * @param value widget value
     */
    private ObservedWidget(String id, String name, String value) {
      this.id = id;
      this.name = name;
      this.value = value;
    }

    /**
     * Returns widget ID. Made public to allow access by Java bean tool.
     *
     * @return widget name
     */
    public String getId() {
      return this.id;
    }

    /**
     * Returns widget name. Made public to allow access by Java bean tool.
     *
     * @return widget name
     */
    public String getName() {
      return this.name;
    }

    /**
     * Returns widget value. Made public to allow access by Java bean tool.
     *
     * @return widget value
     */
    public String getValueInDoubleQuotesOrNull() {
      return (this.value == null)? "null" : "\"" + this.value + "\"";
    }
  }
}
