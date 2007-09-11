/**
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

/**
 * Default constructor.
 *
 * @class Facade to the form guide suite.
 * @extends uiUtil_Object
 */
function uiFormGuide_Driver() {
  this._super();
  /**
   * @type hash&lt;uiFormGuide_Observed&gt;
   * @private
   */
  this.__observedMap = new Object();
  /** 
   *  (ctoohey)
   * data structure for all the RuleSets on a page, so that on page load, after all RuleSets
   * have been created, can simulate events to set up the page by processing each RuleSet as
   * if an event had initiated processing. this is an alternative to simulating an event for
   * each observed element, because since multiple observed elements can be associated with the
   * same RuleSet, that technique can result in a RuleSet being processed multiple times on
   * page load (which may or may not cause problems, but is inefficient and also is confusing
   * when trying to debug).
   */
  this.__allRuleSets = new Array();     
  this.__anObserved = null; // need an observed for implementation of simulateOnChangeEvent
  /** (ctoohey) 
   * data structure to track observed elements for a given RuleSet so that an observed is not
   * associated with the same RuleSet more than once.
   */  
  this.__observedPerRuleSetMap = new Object();
}

uiFormGuide_Driver = uiUtil_Object.declareSingleton(uiFormGuide_Driver, uiUtil_Object);

uiHtml_Window.getInstance().prependEventHandler("load", function(e) {
  uiFormGuide_driver = new uiFormGuide_Driver();
});


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Creates a rule set.
 *
 * @param {function} doAction the action to take when the all the rules in
 *     the set hold
 * @param {function} undoAction the action to take when at least one of
 *     the rules in the set does not hold
 * @return the rule set
 * @type uiFormGuide_RuleSet
 */
uiFormGuide_Driver.prototype.createRuleSet = function(doAction, undoAction, ignoreDoOnLoad, ignoreUndoOnLoad, ignoreDo, ignoreUndo, ignoreAndOr, observeAndOr, prompt, alert) {
  var ruleSet = new uiFormGuide_RuleSet(doAction, undoAction, ignoreDoOnLoad, ignoreUndoOnLoad, ignoreDo, ignoreUndo, ignoreAndOr, observeAndOr, prompt, alert);
  this.__allRuleSets.push(ruleSet);
  return ruleSet;
};

/**
 * Creates a rule. Only one of <code>widgetId</code> or <code>widgetName</code>
 * has to be specified.
 *
 * @param {String} widgetId ID of the widget
 * @param {String} widgetName name of the widget
 * @param {String} forValue the value that the widget should have in order
 *     for the rule to hold (can be a regular expression)
 * @param {String} negate 
 * @return the rule
 * @type uiFormGuide_Rule
 */
uiFormGuide_Driver.prototype.createRule =
    function(widgetId, widgetName, forValue, negate) {
  var widgetGroup = uiHtml_Group.createByEither(widgetId, widgetName);
  return new uiFormGuide_Rule(widgetGroup, forValue, negate);
};

/**
 * author: ctoohey
 * Add an observed widget to a map of observed element for a given RuleSet. This
 * is used by the formGuide.vm template to make sure that a given observed widget
 * (which could be from an ignore tag or an observe tag) is not associated with a
 * given RuleSet more than once (because this would result in actions being performed
 * more than once; in some cases, this redundancy would not hurt except in terms
 * of efficiency, e.g. skip action, but in other cases executing an action more than
 * once could be problematic).
 *
 * @param {String} widgetId ID of the widget
 * @param {String} widgetName name of the widget
 * @return the wrapper
 * @type uiFormGuide_Observed
 */
uiFormGuide_Driver.prototype.elementAlreadyObservedForThisRuleSet = function(widgetId, widgetName) {
  var key = this.__getObservedMapKey(widgetId, widgetName);
  var observed = this.__observedPerRuleSetMap[key];
  if (observed == null) {
    //alert("key=" + key+ " has not been observed yet, so associate with RuleSet");
    this.__observedPerRuleSetMap[key] = key; // does not matter what is put into the map
    return false;
  }
  //alert("key=" + key + " was already observed, so do not associate with RuleSet again");
  return true;
};

/**
 * author: ctoohey
 * Allow the formGuide.vm template to clear out the map for observed elements for a
 * given RuleSet, after using the above function to add a list of distinct ignore and 
 * observe elements to the map.
 */
uiFormGuide_Driver.prototype.clearObservedForThisRuleSet = function() {
  this.__observedPerRuleSetMap = new Object();  
}

/**
 * Creates a wrapper for observed widget(s). Only one of <code>widgetId</code>
 * or <code>widgetName</code> has to be specified.
 *
 * @param {String} widgetId ID of the widget
 * @param {String} widgetName name of the widget
 * @return the wrapper
 * @type uiFormGuide_Observed
 */
uiFormGuide_Driver.prototype.createObservedIfNotInCache =
    function(widgetId, widgetName) {
  // note that observed widgets include ignore, observe and depends elements
  var key = this.__getObservedMapKey(widgetId, widgetName);
  var observed = this.__observedMap[key];
  if (observed == null) {
    // May represent a single widget or a group of widgets with the same name.
    var widgetGroup = uiHtml_Group.createByEither(widgetId, widgetName);
    observed = new uiFormGuide_Observed(widgetGroup);
    this.__observedMap[key] = observed;

    // store an observed element for use in the simulateOnChangeEvent function
    if (!this.__anObserved) {
      this.__anObserved = observed;
    }
  }
  return observed;
};

/**
 * Returns a unique key out of the combination of <code>widgetId</code>
 * and <code>widgetName</code>.
 *
 * @param {String} widgetId ID of the widget
 * @param {String} widgetName name of the widget
 * @return the key
 * @type String
 * @private
 */
uiFormGuide_Driver.prototype.__getObservedMapKey =
    function(widgetId, widgetName) {
  return "id:" + widgetId + "name:" + widgetName;
};

/**
 * Simulates an <i>onchange</i> event. This method is useful for initializing
 * the state of all the observed widgets when the window is loading.
 *
 * (ctoohey) modified so that instead of being called once per formGuide tag set (taking an
 * observed element as an argument) this is just called once per page (without arguments)
 *
 * before, it was called once per formGuide tag set (onload) with one of the observed elements
 * from that formGuide tag's observe child elements (since each observe element is
 * associated with the same RuleSet, only need to call simulate for one of the elements),
 * and it would iterate thru all the RuleSets for that observed element, which includes
 * the RuleSet for the formGuide tag being processed, and any other RuleSets with which
 * the observe element had bee associated with in earlier formGuide tags (but not any
 * RuleSets which the observe element may be associated with in later formGuide tags). the
 * end result is that that same RuleSet could be processed multiple times if an observe
 * element appeared in multiple formGuide tag sets and was the observed element passed
 * as an argument to this simulate function, resulting in redundant and possibly problematic
 * execution of doAction/undoAction calls on page load. 
 *
 * now, the driver maintains its own list of RuleSets by adding a RuleSet to it each time one
 * is created for a formGuide tag set. thus, after all formGuide tags have been initialized
 * on page load, the driver has a list of all RuleSets. Then, there is an onload handler 
 * which calls this simulate function, which processes all the RuleSets as if events had
 * triggered each RuleSet. 
 * note: an observed element is still needed, only because the event handling function for 
 *       observed elements is non-static, so randomly using the first observed element that
 *       was created. once inside the handler, the list of RuleSets associated with this 
 *       observed element is ignored, and instead it processes the list of all RuleSets that 
 *       is passed here to the handler
 */
uiFormGuide_Driver.prototype.simulateOnChangeEvent = function() {
  this.__anObserved._respond(null, this.__allRuleSets);
};
