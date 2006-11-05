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
uiFormGuide_Driver.prototype.createRuleSet = function(doAction, undoAction, ignoreAndOr, observeAndOr, prompt) {
  return new uiFormGuide_RuleSet(doAction, undoAction, ignoreAndOr, observeAndOr, prompt);
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
  var key = this.__getObservedMapKey(widgetId, widgetName);
  var observed = this.__observedMap[key];
  if (observed == null) {
    // May represent a single widget or a group of widgets with the same name.
    var widgetGroup = uiHtml_Group.createByEither(widgetId, widgetName);
    observed = new uiFormGuide_Observed(widgetGroup);
    this.__observedMap[key] = observed;
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
 * @param {uiFormGuide_Observed} observed the observed widget(s) wrapper
 */
uiFormGuide_Driver.prototype.simulateOnChangeEvent = function(observed) {
  observed._respond();
};
