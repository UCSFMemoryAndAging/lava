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
 * Creates a rule set with the <i>do</i> and <i>undo</i> actions initialized.
 * Rules should be added subsequently to this instance through {@link #addRule}.
 *
 * @class A rule set which corresponds to an instance of &lt;formGuide&gt; tag,
 *     containing {@link uiFormGuide_Rule rule}s, <i>do</i> and
 *     <i>undo</i> actions.
 * @extends uiUtil_Object
 * @param {function} doAction the <i>do</i> action: the function to execute
 *     when all rules hold
 * @param {function} undoAction the <i>undo</i> action: the function to execute
 *     when at least one rule doesn't hold
 */
function uiFormGuide_RuleSet(doAction, undoAction) {
  this._super();
  /**
   * @type uiFormGuide_Rule[]
   * @private
   */
  this.__rules = new Array();
  /**
   * @type function
   * @private
   */
  this.__doAction = doAction;
  /**
   * @type function
   * @private
   */
  this.__undoAction = undoAction;
  /**
   * @type uiHtml_ElementWrapper
   * @private
   */
  this.__elementHandler = new uiHtml_ElementWrapper();
  /**
   * A listener that responds to life cycle events.
   *
   * @type uiFormGuide_LifeCycleListener
   * @private
   */
  this.__lifeCycleListener = null;
}

uiFormGuide_RuleSet = uiUtil_Object.declareClass(uiFormGuide_RuleSet, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Adds a rule to this rule set. This method is typically called right after
 * a rule set is instantiated in order to populate it.
 *
 * @param {uiFormGuide_Rule} rule the rule to add
 */
uiFormGuide_RuleSet.prototype.addRule = function(rule) {
  this.__rules.push(rule);
};

/**
 * Returns <code>true</code> if all rules within this rule set hold,
 * <code>false</code> otherwise.
 *
 * @return <code>true</code> if all rules within this rule set hold,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiFormGuide_RuleSet.prototype._allRulesHold = function() {
  // check for AND condition
  for (var i = 0; i < this.__rules.length; ++i) {
    if (!this.__rules[i]._holds()) {
      return false;
    }
  }
  return true;
};

/**
 * Returns an array of all rules in the rule set.
 *
 * @return the array
 * @type uiFormGuide_Rule[]
 */
uiFormGuide_RuleSet.prototype.getRules = function() {
  return this.__rules;
};

/**
 * Executes the <i>do</i> action.
 *
 * @param {DOMEvent} domEvent the event that triggers the do action
 */
uiFormGuide_RuleSet.prototype._doAction = function(domEvent) {
  this.__doAction(domEvent, this);
};

/**
 * Executes the <i>undo</i> action.
 *
 * @param {DOMEvent} domEvent the event that triggers the undo action
 */
uiFormGuide_RuleSet.prototype._undoAction = function(domEvent) {
  this.__undoAction(domEvent, this);
};

/**
 * Sets the form guide's life cycle listener.
 *
 * @param {uiFormGuide_LifeCycleListener} listener the listener
 */
uiFormGuide_RuleSet.prototype.setLifeCycleListener = function(listener) {
  this.__lifeCycleListener = listener;
};

/**
 * Notifies all the life cycle listeners of any of the form guide's life
 * cycle events.
 *
 * @param {String} eventName the name of the event
 * @param {DOMEvent} domEvent the triggered event
 * @param {HTMLElement} domElement the element on which the action is performed
 * @return <code>true</code> to proceed with the event handling,
 *     <code>false</code> to cancel the handling
 * @type boolean
 * @private
 */
uiFormGuide_RuleSet.prototype.__notifyLifeCycleEvent = function(
    eventName, domEvent, domElement) {
  if (this.__lifeCycleListener) {
    var eventHandler = this.__lifeCycleListener[eventName];
    if (eventHandler != null) {
      return eventHandler.call(
          this.__lifeCycleListener, domEvent, domElement, this);
    }
  }
  return true;
};

/**
 * Enables an element as a response to an event.
 *
 * @param {HTMLElement} domElement the element
 * @param {DOMEvent} domEvent the event
 * @private
 */
uiFormGuide_RuleSet.prototype.__enableEachElement = function(
    domElement, domEvent) {
  if (!this.__notifyLifeCycleEvent("onBeforeEnable", domEvent, domElement)) {
    return;
  }
  this.__elementHandler.setDisabled(domElement, false);
  this.__notifyLifeCycleEvent("onAfterEnable", domEvent, domElement);
};

/**
 * Disables an element as a response to an event.
 *
 * @param {HTMLElement} domElement the element
 * @param {DOMEvent} domEvent the event
 * @private
 */
uiFormGuide_RuleSet.prototype.__disableEachElement = function(
    domElement, domEvent) {
  if (!this.__notifyLifeCycleEvent("onBeforeDisable", domEvent, domElement)) {
    return;
  }
  this.__elementHandler.setDisabled(domElement, true);
  this.__notifyLifeCycleEvent("onAfterDisable", domEvent, domElement);
};

/**
 * Inserts an element as a response to an event.
 *
 * @param {HTMLElement} domElement the element
 * @param {DOMEvent} domEvent the event
 * @private
 */
uiFormGuide_RuleSet.prototype.__insertEachElement = function(
    domElement, domEvent) {
  if (!this.__notifyLifeCycleEvent("onBeforeInsert", domEvent, domElement)) {
    return;
  }
  this.__elementHandler.appear(domElement);
  this.__notifyLifeCycleEvent("onAfterInsert", domEvent, domElement);
};

/**
 * Removes an element as a response to an event.
 *
 * @param {HTMLElement} domElement the element
 * @param {DOMEvent} domEvent the event
 * @private
 */
uiFormGuide_RuleSet.prototype.__removeEachElement = function(
    domElement, domEvent) {
  if (!this.__notifyLifeCycleEvent("onBeforeRemove", domEvent, domElement)) {
    return;
  }
  this.__elementHandler.disappear(domElement);
  this.__notifyLifeCycleEvent("onAfterRemove", domEvent, domElement);
};

/**
 * Enables single or multiple elements with the specified ID or name.
 *
 * @param {DOMEvent} domEvent the event
 * @param {String} elementId ID of the element
 * @param {String} elementName name of the element
 * @private
 */
uiFormGuide_RuleSet.prototype.enableElements = function(
    domEvent, elementId, elementName) {
  var group = uiHtml_Group.createByEither(elementId, elementName);
  group.traverse(this, this.__enableEachElement, domEvent);
};

/**
 * Disables single or multiple elements with the specified ID or name.
 *
 * @param {DOMEvent} domEvent the event
 * @param {String} elementId ID of the element
 * @param {String} elementName name of the element
 * @private
 */
uiFormGuide_RuleSet.prototype.disableElements = function(
    domEvent, elementId, elementName) {
  var group = uiHtml_Group.createByEither(elementId, elementName);
  group.traverse(this, this.__disableEachElement, domEvent);
};

/**
 * Inserts single or multiple elements with the specified ID or name.
 *
 * @param {DOMEvent} domEvent the event
 * @param {String} elementId ID of the element
 * @param {String} elementName name of the element
 * @private
 */
uiFormGuide_RuleSet.prototype.insertElements = function(
    domEvent, elementId, elementName) {
  var group = uiHtml_Group.createByEither(elementId, elementName);
  group.traverse(this, this.__insertEachElement, domEvent);
};

/**
 * Removes single or multiple elements with the specified ID or name.
 *
 * @param {DOMEvent} domEvent the event
 * @param {String} elementId ID of the element
 * @param {String} elementName name of the element
 * @private
 */
uiFormGuide_RuleSet.prototype.removeElements = function(
    domEvent, elementId, elementName) {
  var group = uiHtml_Group.createByEither(elementId, elementName);
  group.traverse(this, this.__removeEachElement, domEvent);
};
