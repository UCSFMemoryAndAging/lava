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
 * Creates a rule, passing a widget group and an expected value for future
 * evaluation.
 *
 * @class A rule which holds if a widget group contains an expected value.
 * @extends uiUtil_Object
 * @param {uiHtml_Group} widgetGroup
 * @param {String} expectedValue a <code>null</code> expected value is only
 *     meaningful if the widget group contains selectible widgets such as
 *     checkboxes, radio buttons, or a select box. T
 *     (ctoohey) This can be a regular expression.
 * @param {Boolean} negate (ctoohey) negate whether the rule holds or not
 */
function uiFormGuide_Rule(widgetGroup, expectedValue, negate) {
  this._super();
  /**
   * @type uiHtml_Group
   * @private
   */
  this.__widgetGroup = widgetGroup;
  /**
   * @type String
   * @private
   */
  this.__expectedValue = expectedValue;
  
  this.__negate = negate;
}

uiFormGuide_Rule = uiUtil_Object.declareClass(uiFormGuide_Rule, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Returns <code>true</code> if this rule holds, <code>false</code> otherwise.
 * This rule holds if its widget group contains the expected value, or if the
 * expected value is <code>null</code> and none of the selectible widgets in
 * the widget group is selected.
 *
 * @return <code>true</code> if this rule holds, <code>false</code> otherwise
 * @type boolean
 */
uiFormGuide_Rule.prototype._holds = function() {
  if (this.__expectedValue == null) {
    return (this.__widgetGroup.getValues().length == 0);
  }

  // (ctoohey) modified to use regular expression matching in determing whether
  // an element's value matches the rule value
  if (this.__negate) {
  	return ! this.__widgetGroup.hasRegExpValue(this.__expectedValue);
  }
  else { 
    return this.__widgetGroup.hasRegExpValue(this.__expectedValue);
  }
};

/**
 * Returns the widget group to check for the expected value.
 *
 * @return the group.
 * @type uiHtml_Group
 */
uiFormGuide_Rule.prototype.getWidgetGroup = function() {
  return this.__widgetGroup;
};

/**
 * Returns the expected value for the rule to hold.
 *
 * @return the value.
 * @type String
 */
uiFormGuide_Rule.prototype.getExpectedValue = function() {
  return this.__expectedValue;
};

/**
 * Returns the negate value for negating whether the rule holds or not.
 *
 * @return the value.
 * @type Boolean
 */
uiFormGuide_Rule.prototype.getNegate = function() {
  return this.__negate;
};
