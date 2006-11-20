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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

  /**
   * Value to indicate that a property has been logically skipped.
   */
  static final String LOGICAL_SKIP_CODE = "-6";
  /**
   * Text to indicate that a property has been logically skipped, for
   * those widgets where text accompanies a value, such as select boxes.
   */
  static final String LOGICAL_SKIP_TEXT = "Logical Skip";
  /**
   * Special value for radio buttons in a comboRadioSelect widget that
   * indicates the missing data code from the select box should be used
   * as the value of the widget.
  */
  static final String COMBO_RADIO_SELECT_USE_SELECT = "-9999";


  ////////////////////////////
  ////////// Fields //////////
  ////////////////////////////

  /**
   * Widgets for ignore rules
   */
  private List ignoreWidgets;
  
  /**
   * Widgets for observe rules
   */
  private List observedWidgets;
  
  /**
   * Widgets for depends
   */
  private List dependsWidgets;
  
  /**
   * (ctoohey)
   * Widget groups.
   * 
   * Used internally for removeOption child tag tasks.
   */
  private Map<String,ObservedWidget> observedWidgetGroups;
  
  /**
   * (ctoohey)
   * Flag used internally for removeOption child tag tasks.
   */
  private Boolean firstObserveCloned = false;
  
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
  
  /**
   * (ctoohey)
   * The "ignoreDoOnLoad" tag attribute
   *
   * If "true", will not execute the doAction when the page loads. If "false", will
   * execute the doAction (assuming all observe rules hold such that the doAction 
   * should execute). 
   * defaults to false
   */
  private Boolean _ignoreDoOnLoad; // setter only
  private Boolean ignoreDoOnLoad;

  /**
   * (ctoohey)
   * The "ignoreUndoOnLoad" tag attribute
   *
   * If "true", will not execute the undoAction when the page loads. If "false", will
   * execute the undoAction (assuming all observe rules do not hold such that the undoAction 
   * should execute). 
   * defaults to false
   */
  private Boolean _ignoreUndoOnLoad; // setter only
  private Boolean ignoreUndoOnLoad;

  /**
   * (ctoohey)
   * The "ignoreDo" tag attribute
   *
   * If "true" will not execute the doAction during a user event. If "false", will 
   * execute the doAction (assuming all observe rules hold such that the doAction 
   * should execute).
   * defaults to false 
   */
  private Boolean _ignoreDo; // setter only
  private Boolean ignoreDo;

  /**
   * (ctoohey)
   * The "ignoreUndo" tag attribute
   *
   * If "true", will not execute the undoAction during a user event. If "false", will
   * execute the undoAction (assuming all observe rules do not hold such that the undoAction 
   * should execute).
   * defaults to false 
   */
  private Boolean _ignoreUndo; // setter only
  private Boolean ignoreUndo;

  /**
   * (ctoohey)
   * The "prompt" tag attribute
   *
   * If not null, the user will be prompted with this string before the do action
   * executes, giving the user a chance to cancel the do action. The prompt will not be issued
   * on page load. 
   */
  private String _prompt; // setter only
  private String prompt;
  
  /**
   * (ctoohey)
   * The "ignoreAndOr" tag attribute
   * 
   * Used in determining whether the ignore rules are met, in which case the formGuide tag is
   * ignored in its entirety.
   * 
   * If "and" then all ignore rules must be met.
   * If "or then only one ignore rule must be met. 
   * Default is "and".
   */
  private String _ignoreAndOr; // setter only
  private String ignoreAndOr;

  /**
   * (ctoohey)
   * The "observeAndOr" tag attribute
   * 
   * Used in determining whether the observe rules are met, in which case the do actions in the
   * formGuide tag are executed (unless the ignore rules are met, in which case the actions are ignored). 
   * If the observe rules are not met, then the undo actions are executed.
   * 
   * If "and" then all observe rules must be met.
   * If "or then only one observe rule must be met. 
   * Default is "and".
   */
  private String _observeAndOr; // setter only
  private String observeAndOr;
  
  /**
   * (ctoohey)
   * The "mode" tag attribute. 
   *
   * Corresponds to the view mode of the current view. If this represents
   * a readonly mode, e.g. "vw" or "lv", then this tag and its children have
   * no effect, since there should be no user events in readonly mode.
   */
  private String _mode; // setter only 
  private String mode;
  
  /**
   * (ctoohey)
   * The "simulateEvents" tag attribute.
   * 
   * This boolean attribute determines whether events which trigger all formGuide tags on the
   * page should be simulated on page load to properly set up the page. Since this only needs
   * to be done once, this attribute should be set "true" only on one of the formGuide tags if
   * multiple tags exist on the page. Also, it should be set on the last formGuide tag of the
   * page, so that all formGuide tags have initialized on page load before event simulation
   * takes place.
   * 
   * defaults to "false"
   */
  private Boolean _simulateEvents;
  private Boolean simulateEvents;


  //////////////////////////////////
  ////////// Constructors //////////
  //////////////////////////////////

  /**
   * Default constructor.
   */
  public FormGuideTag() {
    super();
  }

  // author:ctoohey
  // used by the removeOptions child tag
  public Map<String, ObservedWidget> getObservedWidgetGroups() {
	  return this.observedWidgetGroups;
  }
  // author:ctoohey
  public Boolean isFirstObserveCloned() {
	  return this.firstObserveCloned;
  }
  // author:ctoohey
  public void setFirstObserveCloned(Boolean firstObserveCloned) {
	  this.firstObserveCloned = firstObserveCloned;
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

  /**
   * author:ctoohey
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setIgnoreDoOnLoad(Boolean ignoreDoOnLoad) {
    this._ignoreDoOnLoad = ignoreDoOnLoad;
  }

  /**
   * author:ctoohey
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setIgnoreUndoOnLoad(Boolean ignoreUndoOnLoad) {
    this._ignoreUndoOnLoad = ignoreUndoOnLoad;
  }
  
  /**
   * author:ctoohey
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setIgnoreDo(Boolean ignoreDo) {
    this._ignoreDo = ignoreDo;
  }

  /**
   * author:ctoohey
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setIgnoreUndo(Boolean ignoreUndo) {
    this._ignoreUndo = ignoreUndo;
  }
  
  /**
   * author:ctoohey
   * Tag attribute setter.
   *
   * @param val value of the tag attribute
   */
  public void setPrompt(String prompt) {
    this._prompt = prompt;
  }

  /**
   * author:ctoohey
   * Tag attribute setter.
   *
   * The ignoreAndOr attribute has value of "and" or "or" and determines whether
   * all ignore tag conditions must be met ("and") or just one of the 
   * ignore tag conditions ("or") must be met.
   *
   * See setObserveAndOr for special syntax for select boxes, which also applies
   * to the ignore tags. 
   *
   * @param val value of the tag attribute
   */
  public void setIgnoreAndOr(String val) {
    this._ignoreAndOr = val;
  }

  /**
   * author:ctoohey
   * Tag attribute setter.
   *
   * The observeAndOr attribute has value of "and" or "or" and determines whether
   * all observe tag conditions must be met ("and") or just one of the 
   * observe tag conditions ("or") must be met.
   *
   * note: when the observe tag special syntax for select boxes is used, where
   *       the forValue in the observe tag can be ".*" indicating that each
   *       value of the select box other than blank is involved in the observer,
   *       typically andOr would be "or" because a select box can not have
   *       multiple values selected at one time, unless, it is a multiple
   *       select box, in which case andOr could be "and" or "or".
   *
   * @param val value of the tag attribute
   */
  public void setObserveAndOr(String val) {
    this._observeAndOr = val;
  }

  /**
   * author:ctoohey
   * Tag attribute setter.
   *
   * The view mode in effect.
   *
   * @param val value of the tag attribute
   */
  public void setMode(String val) {
    this._mode = val;
  }
  /**
   * authod:ctoohey
   * Tag attribute getter.
   */
  public String getMode() {
    return this.mode;
  }
    
  /**
   * author:ctoohey
   * Tag attribute setter.
   * 
   * @param val value of the tag attribute
   */
  public void setSimulateEvents(Boolean val) {
    this._simulateEvents = val;
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
    this.ignoreWidgets   = new ArrayList();
    this.observedWidgets   = new ArrayList();
    this.dependsWidgets   = new ArrayList();
    this.observedWidgetGroups      = new HashMap();
    this.doTasks           = new ArrayList();
    this.undoTasks         = new ArrayList();
    
    // set defaults for optional formGuide tag attributes
    this.ignoreDoOnLoad = this._ignoreDoOnLoad;
    if (this.ignoreDoOnLoad == null) {
    	this.ignoreDoOnLoad = new Boolean(false);
    }
    this.ignoreUndoOnLoad = this._ignoreUndoOnLoad;
    if (this.ignoreUndoOnLoad == null) {
    	this.ignoreUndoOnLoad = new Boolean(false);
    }
    this.ignoreDo = this._ignoreDo;
    if (this.ignoreDo == null) {
    	this.ignoreDo = new Boolean(false);
    }
    this.ignoreUndo = this._ignoreUndo;
    if (this.ignoreUndo == null) {
    	this.ignoreUndo = new Boolean(false);
    }
    this.ignoreAndOr = this._ignoreAndOr;
    if (this.ignoreAndOr == null) {
    	this.ignoreAndOr = "and";
    }
    this.observeAndOr = this._observeAndOr;
    if (this.observeAndOr == null) {
    	this.observeAndOr = "and";
    }
    this.mode = this._mode;
    if (this.mode == null) {
    	this.mode = "dc";
    }
    this.simulateEvents = this._simulateEvents;
    if (this.simulateEvents == null) {
    	this.simulateEvents = false;
    }
    this.prompt = this._prompt;

    // initialize internal variable
    firstObserveCloned = false;

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
    template.map("ignoreWidgets",   this.ignoreWidgets);
    template.map("observedWidgets", this.observedWidgets);
    template.map("dependsWidgets",  this.dependsWidgets);
    template.map("doTasks",         this.doTasks);
    template.map("undoTasks",       this.undoTasks);
    template.map("listener",        this.listener);
    template.map("ignoreDoOnLoad",  this.ignoreDoOnLoad);    
    template.map("ignoreUndoOnLoad",this.ignoreUndoOnLoad);    
    template.map("ignoreDo",        this.ignoreDo);    
    template.map("ignoreUndo",      this.ignoreUndo);    
    template.map("prompt",          this.prompt);    
    template.map("observeAndOr",    this.observeAndOr);    
    template.map("ignoreAndOr",     this.ignoreAndOr);    

    if (!(this.mode.equals("vw") || this.mode.equals("lv"))) {
    	println(template.evalToString());

    	if (this.simulateEvents) {
    		Template templateSimulateEvent = Template.forName(Template.FORM_GUIDE_SIMULATE_EVENT);
    		println(templateSimulateEvent.evalToString());
    	}
    }

    makeInvisibleFromChildren();
    return EVAL_PAGE;
  }

  
  /**
   * author: ctoohey
   * Lets child tags add widget name for an ignore rule.
   *
   * @param elementName name of the element for the ignore rule
   * @param value value of the element for the ignore rule to hold
   * @param negate negate whether the ignore rule holds or not
   */
  void addIgnoreElementName(String elementName, String value, Boolean negate) {
	  // can use the same data structure that observe elements use
	  ObservedWidget ignoreWidget = new ObservedWidget(null, elementName, value, negate);
	  this.ignoreWidgets.add(ignoreWidget);
  }

  /**
   * author: ctoohey
   * Lets child tags add widget ID for an ignore rule.
   *
   * @param elementId ID of the element for the ignore rule
   * @param value value of the element for the ignore rule to hold
   * @param negate (ctoohey) negate whether the ignore rule holds or not
   */
  void addIgnoreElementId(String elementId, String value, Boolean negate) {
	  // can use the same data structure that observe elements use
	  ObservedWidget ignoreWidget = new ObservedWidget(elementId, null, value, negate);
	  this.ignoreWidgets.add(ignoreWidget);
  }

  
  /**
   * Lets child tags add widget name for observe rule.
   *
   * @param elementName name of the widget to observe
   * @param value value of the widget to observe
   * @param negate (ctoohey) negate whether the observe rule holds or not
   */
  void addObservedElementName(String elementName, String value, Boolean negate) {
	  ObservedWidget observedWidget = new ObservedWidget(null, elementName, value, negate);
	  this.observedWidgets.add(observedWidget);
	  // keep a map of unique observed widgets for the removeOption callback
	  if (!this.observedWidgetGroups.containsKey(elementName)) {
		  this.observedWidgetGroups.put(elementName, observedWidget);
	  }
  }

  /**
   * Lets child tags add widget ID for observe rule.
   *
   * @param elementId ID of the widget to observe
   * @param value value of the widget to observe
   * @param negate (ctoohey) negate whether the observe rule holds or not
   */
  void addObservedElementId(String elementId, String value, Boolean negate) {
	  ObservedWidget observedWidget = new ObservedWidget(elementId, null, value, negate);
	  this.observedWidgets.add(observedWidget);
	  // keep a map of unique observed widgets for the removeOption callback
	  if (!this.observedWidgetGroups.containsKey(elementId)) {
		  this.observedWidgetGroups.put(elementId, observedWidget);
	  }
  }


  /**
   * author: ctoohey
   * Lets child tags add widget name upon which actions are is dependent.
   *
   * @param elementName name of the element depended upon
   */
  void addDependsElementName(String elementName) {
	  // can use the same data structure that observe elements use
	  ObservedWidget dependsWidget = new ObservedWidget(null, elementName, null, null);
	  this.dependsWidgets.add(dependsWidget);
  }

  /**
   * author: ctoohey
   * Lets child tags add widget ID upon which actions are dependent.
   *
   * @param elementId ID of the element depended upon
   */
  void addDependsElementId(String elementId) {
	  // can use the same data structure that observe elements use
	  ObservedWidget dependsWidget = new ObservedWidget(elementId, null, null, null);
	  this.dependsWidgets.add(dependsWidget);
  }


  /**
   * author:ctoohey 
   * Lets child tags specify javascript callback statements 
   * 
   * @param doCallback Javascript callback for do action generated by child tag
   * @param undoCallback Javascript callback for undo action generated by child tag
   */
  void addJavascriptCallback(String doCallback, String undoCallback) {
   	    this.doTasks.add(doCallback);

   	    if (undoCallback != null) {
   	    	this.undoTasks.add(undoCallback);
   	    }
  }
  
  /**
   * Lets child tags specify javascript callback statement which
   * takes 2 parameters (elementId and elementName)
   * e.g. child tags: enable,disable,insert,remove
   * 
   * (ctoohey) modified to work with multiple elements, i.e. comma-separated string
   * of element ids or element names
   * 
   * The reason why addJavascriptCallback is overloaded here is because a number
   * of child tags use the exact same code to iterate thru element ids/names and
   * construct the Javascript callback, so this allows those tags to share
   * that code. 
   * currently used by child tags: enable, disable, insert, remove
   *
   * @param doCallback the doCallback statement
   * @param undoCallback the undoCallback statement
   * @param string of comma-separated element ID/name of the involved element(s)
   */
  void addJavascriptCallback(
	String doCallback, String undoCallback,
    String elementIds, String elementNames, String component) {

    String[] elementArray = null;
    if (elementIds != null) {
    	elementArray = elementIds.split(",");
    }
    else if (elementNames != null) {
    	elementArray = elementNames.split(",");
    }
    for (String elementIdOrName : elementArray) {
   		if (elementIds != null && component != null) {
   	   		elementIdOrName = component + "_" + elementIdOrName.trim(); 
   		}
   		String idOrNameParam = getJsElementParam(elementIdOrName.trim());
        this.doTasks.add(UiString.simpleConstruct(
                "{0}(domEvent, {1}, {2});",
                new String[] { doCallback, (elementIds != null ? idOrNameParam : null), (elementNames != null ? idOrNameParam : null)}));

        this.undoTasks.add(UiString.simpleConstruct(
                "{0}(domEvent, {1}, {2});",
                new String[] { undoCallback, (elementIds != null ? idOrNameParam : null), (elementNames != null ? idOrNameParam : null)}));
    }
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
     * (ctoohey)
     * negate flag negates the result of the widget's observe
     */
    private Boolean negate;

    /**
     * Creates a data container instance.
     *
     * @param name  widget name
     * @param value widget value
     */
    private ObservedWidget(String id, String name, String value, Boolean negate) {
      this.id = id;
      this.name = name;
      this.value = value;
      this.negate = negate;
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

    /**
     * author:ctoohey
     * Returns negate value. Made public to allow access by Java bean tool.
     *
     * @return widget observe negated
     */
    public Boolean getNegate() {
      return this.negate;
    }
  }
}
