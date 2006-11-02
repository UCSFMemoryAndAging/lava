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
 * @class The listener of form guide's life cycle events.
 * @extends uiUtil_Object
 */
function uiFormGuide_LifeCycleListener() {
  this._super();
}

uiFormGuide_LifeCycleListener = uiUtil_Object.declareClass(
    uiFormGuide_LifeCycleListener, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Responds to the event triggered before an element insertion is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the insertion
 * @param {HTMLElement} domElement the element that will be inserted
 * @param {uiFormGuide_RuleSet} ruleSet the rule set containing all
 *     the rules that need to be passed to perform the insertion
 * @return <code>true</code> to proceed with the insertion,
 *     <code>false</code> to cancel it
 * @type boolean
 */
uiFormGuide_LifeCycleListener.prototype.onBeforeInsert = function(domEvent, domElement, ruleSet) {
//  alert("before inserting: [" + domElement.id + ":" + domElement.name + "]");
  return true;  // proceed with the insertion
};

/**
 * Responds to the event triggered after an element insertion is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the insertion
 * @param {HTMLElement} domElement the inserted element
 * @param {uiFormGuide_RuleSet} ruleSet the rule set containing all
 *     the rules that need to be passed to perform the insertion
 */
uiFormGuide_LifeCycleListener.prototype.onAfterInsert = function(domEvent, domElement, ruleSet) {
//  alert("after inserting: [" + domElement.id + ":" + domElement.name + "]");
};

/**
 * Responds to the event triggered before an element removal is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the removal
 * @param {HTMLElement} domElement the element that will be removed
 * @param {uiFormGuide_RuleSet} ruleSet the rule set containing all
 *     the rules that need to be passed to perform the insertion
 * @return <code>true</code> to proceed with the removal,
 *     <code>false</code> to cancel it
 * @type boolean
 */
uiFormGuide_LifeCycleListener.prototype.onBeforeRemove = function(domEvent, domElement, ruleSet) {
//  alert("before removing: [" + domElement.id + ":" + domElement.name + "]");
  return true;  // proceed with the removal
};

/**
 * Responds to the event triggered after an element removal is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the removal
 * @param {HTMLElement} domElement the removed element
 * @param {uiFormGuide_RuleSet} ruleSet the rule set containing all
 *     the rules that need to be passed to perform the insertion
 */
uiFormGuide_LifeCycleListener.prototype.onAfterRemove = function(domEvent, domElement, ruleSet) {
//  alert("after removing: [" + domElement.id + ":" + domElement.name + "]");
};

/**
 * Responds to the event triggered before an element enabling is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the enabling
 * @param {HTMLElement} domElement the element that will be enabled
 * @param {uiFormGuide_RuleSet} ruleSet the rule set containing all
 *     the rules that need to be passed to perform the insertion
 * @return <code>true</code> to proceed with the enabling,
 *     <code>false</code> to cancel it
 * @type boolean
 */
uiFormGuide_LifeCycleListener.prototype.onBeforeEnable = function(domEvent, domElement, ruleSet) {
//  alert("before enabling: [" + domElement.id + ":" + domElement.name + "]");
  return true;  // proceed with the enabling
};

/**
 * Responds to the event triggered after an element enabling is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the enabling
 * @param {HTMLElement} domElement the removed element
 * @param {uiFormGuide_RuleSet} ruleSet the rule set containing all
 *     the rules that need to be passed to perform the insertion
 */
uiFormGuide_LifeCycleListener.prototype.onAfterEnable = function(domEvent, domElement, ruleSet) {
//  alert("after enabling: [" + domElement.id + ":" + domElement.name + "]");
};

/**
 * Responds to the event triggered before an element disabling is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the disabling
 * @param {HTMLElement} domElement the element that will be disabled
 * @param {uiFormGuide_RuleSet} ruleSet the rule set containing all
 *     the rules that need to be passed to perform the insertion
 * @return <code>true</code> to proceed with the disabling,
 *     <code>false</code> to cancel it
 * @type boolean
 */
uiFormGuide_LifeCycleListener.prototype.onBeforeDisable = function(domEvent, domElement, ruleSet) {
//  alert("before disabling: [" + domElement.id + ":" + domElement.name + "]");
  return true;  // proceed with the disabling
};

/**
 * Responds to the event triggered after an element disabling is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the disabling
 * @param {HTMLElement} domElement the disabled element
 * @param {uiFormGuide_RuleSet} ruleSet the rule set containing all
 *     the rules that need to be passed to perform the insertion
 */
uiFormGuide_LifeCycleListener.prototype.onAfterDisable = function(domEvent, domElement, ruleSet) {
//  alert("after disabling: [" + domElement.id + ":" + domElement.name + "]");
};
