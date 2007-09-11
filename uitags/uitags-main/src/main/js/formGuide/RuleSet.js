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
 * @param ignoreDoOnLoad (ctoohey) do not execute doAction on page load
 * @param ignoreUndoOnLoad (ctoohey) do not execute undoAction on page load
 * @param ignoreAndOr (ctoohey) designates whether one or all ignore tags must match
 * @param observeAndOr (ctoohey) designates where one or all observe tags must match
 * @param prompt (ctoohey) prompts the user before performing the doAction, allowing them to cancel
 * @param alert (ctoohey) alerts the user with pertinent information
 */
function uiFormGuide_RuleSet(doAction, undoAction, ignoreDoOnLoad, ignoreUndoOnLoad, ignoreDo, ignoreUndo, ignoreAndOr, observeAndOr, prompt, alert) {
  this._super();
  /**
   * @type uiFormGuide_Rule[]
   * @private
   */
  this.__rules = new Array();
  /** (ctoohey)
   * @type uiFormGuide_Rule[]
   * @private
   */
  this.__ignoreRules = new Array();
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

  this.__ignoreDoOnLoad = ignoreDoOnLoad;
  this.__ignoreUndoOnLoad = ignoreUndoOnLoad;
  this.__ignoreDo = ignoreDo;
  this.__ignoreUndo = ignoreUndo;
  this.__ignoreAndOr = ignoreAndOr;
  this.__observeAndOr = observeAndOr;
  this.__prompt = prompt;
  this.__alert = alert;
  
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

// begin:ctoohey

uiFormGuide_RuleSet.prototype.getIgnoreDoOnLoad = function() {
  return this.__ignoreDoOnLoad;
}  

uiFormGuide_RuleSet.prototype.getIgnoreUndoOnLoad = function() {
  return this.__ignoreUndoOnLoad;
}  

uiFormGuide_RuleSet.prototype.getIgnoreDo = function() {
  return this.__ignoreDo;
}  

uiFormGuide_RuleSet.prototype.getIgnoreUndo = function() {
  return this.__ignoreUndo;
}  

uiFormGuide_RuleSet.prototype.getPrompt = function() {
  return this.__prompt;
}  

uiFormGuide_RuleSet.prototype.getAlert = function() {
  return this.__alert;
}  

/**
 * Adds an ignore rule to this rule set. This method is typically called right after
 * a rule set is instantiated in order to populate it.
 *
 * @param {uiFormGuide_IgnoreRule} rule the rule to add
 */
uiFormGuide_RuleSet.prototype.addIgnoreRule = function(rule) {
  this.__ignoreRules.push(rule);
};

/**
 * Returns <code>true</code> if all ignore rules within this rule set hold,
 * <code>false</code> otherwise.
 *
 * @return <code>true</code> if all ignore rules within this rule set hold,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiFormGuide_RuleSet.prototype._allIgnoreRulesHold = function() {
  // if no ignore tags, then return false so observe rules not ignored
  if (this.__ignoreRules.length == 0) {
    return false;
  }
  for (var i = 0; i < this.__ignoreRules.length; ++i) {
    var ruleHolds = this.__ignoreRules[i]._holds();
    if (this.__observeAndOr == "and") {
      // at least one rule does not hold, so rules do not hold
      if (!ruleHolds) {
        return false;
      }  
    }
    else if (this.__observeAndOr == "or") {
      // at least one rule holds, so rules hold
      if (ruleHolds) {
        return true;
      }
    }    
  }

  if (this.__observeAndOr == "and") {
	// all of the rules held
    return true;
  }
  else if (this.__observeAndOr == "or") {
    // none of the rules held
    return false;
  }  
};

/**
 * Returns an array of all ignore rules in the rule set.
 *
 * @return the array
 * @type uiFormGuide_IgnoreRule[]
 */
uiFormGuide_RuleSet.prototype.getIgnoreRules = function() {
  return this.__ignoreRules;
};
// end:ctoohey

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
 * (ctoohey) modified to use observeAndOr attribute to determine outcome
 *
 * @return <code>true</code> if all rules within this rule set hold,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiFormGuide_RuleSet.prototype._allRulesHold = function() {
  // if no observe tags, then return true. this allows tag to just 
  // specify ignore rules such that if ignore rules do not hold, the
  // doAction should be executed (undoAction will never be executed, 
  // because either ignore rule(s) will hold and actions ignored, or,
  // observe rules will always hold since there are on observe rules)
  if (this.__rules.length == 0) {
    return true;
  }
  for (var i = 0; i < this.__rules.length; ++i) {
    var ruleHolds = this.__rules[i]._holds();
    if (this.__observeAndOr == "and") {
      // at least one rule does not hold, so rules do not hold
      if (!ruleHolds) {
        return false;
      }  
    }
    else if (this.__observeAndOr == "or") {
      // at least one rule holds, so rules hold
      if (ruleHolds) {
        return true;
      }
    }    
  }

  if (this.__observeAndOr == "and") {
	// all of the rules held
    return true;
  }
  else if (this.__observeAndOr == "or") {
    // none of the rules held
    return false;
  }  
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


////// ctoohey: beginning of new actions added to extend uitags 
//////          note: some of the existing actions above were modified as well


/**
 * author: ctoohey
 * Skip an element, i.e. disable it and set it to the specified value.
 *
 * @param {HTMLElement} domElement the element
 * @param {DOMEvent} domEvent the event
 * @param value the value to set the element to (select the matching option value for selectboxes)
 * @param optionText for selectbox elements, select the matching option text
 * @private
 */
uiFormGuide_RuleSet.prototype.__skipEachElement = function(
    domElement, domEvent, value, optionText) {
  //alert("calling skipEachElement value=" + value + " optionText=" + optionText);
  if (!this.__notifyLifeCycleEvent("onBeforeSkip", domEvent, domElement)) {
    return;
  }
  this.__elementHandler.skip(domElement, value, optionText);
  this.__notifyLifeCycleEvent("onAfterSkip", domEvent, domElement);
};

/**
 * author: ctoohey
 * Skips single or multiple elements (e.g. if the element is a group of radiobuttons),
 * i.e. disables them and sets their value to the specified value.
 *
 * @param {DOMEvent} domEvent the event
 * @param {String} elementId ID of the element
 * @param {String} elementName name of the element
 * @param value the value to set the element to (select the matching option value for selectboxes)
 * @param optionText for selectbox elements, select the matching option text
 * @private
 */
uiFormGuide_RuleSet.prototype.skipElement = function(
    domEvent, elementId, elementName, value, optionText) {
  //alert("calling skipElement value=" + value + " optionText=" + optionText);
  var group = uiHtml_Group.createByEither(elementId, elementName);
  // for select boxes, group is a uiHtml_Select object (where the select options are
  //  stored in an array)
  // for radio button group (and any other elements that stored as a collection by the 
  //  browser), group is a uiHtml_Group object with an items field that is a browser's 
  //  native collection containing each individual radio button element
  // for other elements, group is a uiHtml_Group object with an items field that is an
  //  array containing just the element. 
  // this way, all elements are stored in an array (or collection) so they can be processed
  //  similarly
  // group.traverse utilizes the javascript "arguments" variable to facilitate a passing
  //  a variable number of arguments so that ..EachElement function which is called for
  //  each element in group is passed all of the arguments starting with domEvent in this call
  // for select boxes, the EachElement function is called for the select box as a whole
  //  and then called for each select box option (see uiHtml_Select.traverse)
  
  // currently, the EachElement functions use this.__elementHandler to execute specific functions
  //  to achieve the goal, and this.__elementHandler is always an instance of ElementWrapper, regardless
  //  of whether the element has a more specialized wrapper (RadioWrapper, CheckboxWrapper,
  //  SelectOptionWrapper). this is probably because to date there has not been any specialized 
  //  behavior required to carry of the standard formGuide actions. if the need arises, then the
  //  group.traverse function should include the group handler (which is a wrapper) as an argument to 
  //  the EachElement function and it should use that to execute the specific functions rather than 
  //  this.__elementHandler (and because those wrappers all "inherit" from ElementWrapper, if those
  //  wrappers do not implement a given function, they would need to implement and delegate to 
  //  the ElementWrapper version, or ??? will it automatically call the ElementWrapper version ???)
  // note: these specialized wrappers are used when it comes to appending event handlers for each 
  //  uiHtml_Group (where a uiFormGuide_Observed object is created for each distinct uiHtml_Group object,
  //  and calls appendEventHandler on the group object, which in turn calls its handler (wrapper)
  //  version of appendEventHandler)
  group.traverse(this, this.__skipEachElement, domEvent, value, optionText);
};


/**
 * author: ctoohey
 * Unskip an element, i.e. enable it and set it to the specified value.
 *
 * @param {HTMLElement} domElement the element
 * @param {DOMEvent} domEvent the event
 * @param value the value to set the element to (select the matching option value for selectboxes)
 * @param optionText for selectbox elements, select the matching option text
 * @private
 */
uiFormGuide_RuleSet.prototype.__unskipEachElement = function(
    domElement, domEvent, value, optionText) {
  if (!this.__notifyLifeCycleEvent("onBeforeSkip", domEvent, domElement)) {
    return;
  }
  this.__elementHandler.unskip(domElement, value, optionText);
  this.__notifyLifeCycleEvent("onAfterSkip", domEvent, domElement);
};

/**
 * author: ctoohey
 * Unskips single or multiple elements (e.g. if the element is a group of radiobuttons),
 * i.e. enables them and sets their value to the specified value.
 *
 * @param {DOMEvent} domEvent the event
 * @param {String} elementId ID of the element
 * @param {String} elementName name of the element 
 * @param value the value to set the element to (select the matching option value for selectboxes)
 * @param optionText for selectbox elements, select the matching option text
 * @private
 */
uiFormGuide_RuleSet.prototype.unskipElement = function(
    domEvent, elementId, elementName, value, optionText) {
  var group = uiHtml_Group.createByEither(elementId, elementName);
  group.traverse(this, this.__unskipEachElement, domEvent, value, optionText);
};


/**
 * author: ctoohey
 * Clones the options from a src selectbox to a target. This function is used as part of
 * the removeOption action.
 * The purpose of this function is to repopulate the target selectbox before removing 
 * options from it, because a target selectbox may have already had other options removed 
 * from prior actions, and it should start with a "full deck" before having options 
 * removed.
 *
 * Since the group in this case is a select box, no need to traverse 
 * elements of a group. 
 *
 * @param {DOMEvent} domEvent the event
 * @param {String} srcElementId ID of the element whose options are cloned
 * @param {String} srcElementName name of the element whose options are cloned
 * @param {String} targetElementId ID of the element that gets a copy of src options
 * @param {String} targetElementName name of the element that gets a copy of src options
 * 
 * @private
 */
uiFormGuide_RuleSet.prototype.cloneOptions = function(
    domEvent, srcElementId, srcElementName, targetElementId, targetElementName) {
  // in this function, group is only used by the lifecycle function
  var group = uiHtml_Group.createByEither(targetElementId, targetElementName);
  if (!this.__notifyLifeCycleEvent("onBeforeCloneOptions", domEvent, group.getItemAt(0))) {
    return;
  }
  this.__elementHandler.cloneOptions(srcElementId, srcElementName, targetElementId, targetElementName);

  var srcSelectObj;
  var srcAutocompleteObj = ACS['acs_textbox_' + srcElementId];
  if (srcAutocompleteObj != null ) {
      srcSelectObj = new uiHtml_Select(srcAutocompleteObj.select);
  }
  else {
      srcSelectObj = uiHtml_Group.createByEither(srcElementId, srcElementName);
  }
  this.__notifyLifeCycleEvent("onAfterCloneOptions", domEvent, srcSelectObj.getDomObject());
};


/**
 * author: ctoohey
 * Removes option from a selectbox that is selected in another select box.
 * Assumes that both selectboxes have the same options, 
 * e.g. LavaWeb qualityIssues and qualityIssues2 selectboxes.
 *
 * note: currently this action is only implemented for selectboxes, and only
 *       works for selectboxes that only allow a single selection, not for 
 *       selectboxes that allow multiple selections
 * assumption: the src and target selectboxes have the same option values
 * assumption: the target selectbox has a blank option, so that it its currently
 *             selected option is the one that is removed, it then sets its 
 *             currently selected option to its blank option
 *
 * Since the group in this case is a select box, no need to traverse 
 * elements of a group. 
 *
 * @param {DOMEvent} domEvent the event
 * @param {String} srcElementId ID of the element whose currently select option value is removed from targetElement
 * @param {String} srcElementName name of the element whose currently select option value is removed from targetElement
 * @param {String} targetElementId ID of the element whose option is removed
 * @param {String} targetElementName name of the element whose option is removed
 * 
 * @private
 */
uiFormGuide_RuleSet.prototype.removeOption = function(
    domEvent, srcElementId, srcElementName, targetElementId, targetElementName) {
  var group = uiHtml_Group.createByEither(targetElementId, targetElementName);
  if (!this.__notifyLifeCycleEvent("onBeforeRemoveOption", domEvent, group.getItemAt(0))) {
    return;
  }
  this.__elementHandler.removeOption(srcElementId, srcElementName, targetElementId, targetElementName);

  var srcSelectObj;
  var srcAutocompleteObj = ACS['acs_textbox_' + srcElementId];
  if (srcAutocompleteObj != null ) {
      srcSelectObj = new uiHtml_Select(srcAutocompleteObj.select);
  }
  else {
      srcSelectObj = uiHtml_Group.createByEither(srcElementId, srcElementName);
  }
  this.__notifyLifeCycleEvent("onAfterRemoveOption", domEvent, srcSelectObj.getDomObject());
};


/**
 * author: ctoohey
 * Set the value of an element or elements to the specified value. 
 *
 * If the element is a selectbox, select the option whose text field matches textValue 
 * and/or whose value field matches optionValue (depending upon whether one or both are 
 * supplied). If there is no match, add a new option to the selectbox using textValue and
 * optionValue and select it. 
 *
 * @param {HTMLElement} domElement the element
 * @param {DOMEvent} domEvent the event
 * @param value the value to set the element to (select the matching option value for selectboxes)
 * @param optionText for selectbox elements, select the matching option text
 * @private
 */
uiFormGuide_RuleSet.prototype.__setValueEachElement = function(
    domElement, domEvent, value, optionText) {
  if (!this.__notifyLifeCycleEvent("onBeforeSkip", domEvent, domElement)) {
    return;
  }
  this.__elementHandler.setValue(domElement, value, optionText);
  this.__notifyLifeCycleEvent("onAfterSkip", domEvent, domElement);
};

/**
 * author: ctoohey
 * Sets the value of an element or elements (e.g. if elements is a group of multiple
 * elements, e.g. radiobuttons) to the specified value. 
 *
 * If the element is a selectbox, select the option whose text field matches textValue 
 * and/or whose value field matches optionValue (depending upon whether one or both are 
 * supplied). If there is no match, add a new option to the selectbox using textValue and
 * optionValue and select it. 
 *
 * @param {DOMEvent} domEvent the event
 * @param {String} elementId ID of the element
 * @param {String} elementName name of the element
 * @param value the value to set the element to (select the matching option value for selectboxes)
 * @param optionText for selectbox elements, select the matching option text
 * @private
 */
uiFormGuide_RuleSet.prototype.setValue = function(
    domEvent, elementId, elementName, value, optionText) {
  var group = uiHtml_Group.createByEither(elementId, elementName);
  group.traverse(this, this.__setValueEachElement, domEvent, value, optionText);
};


/**
 * author: ctoohey
 * Calls the common non-uitags submitForm function, passing the specified form
 * and event request param value.
 *
 * @param {DOMEvent} domEvent the event
 * @param {String} form form to submit
 * @param {String} event value of event request parameter to submit with form
 * @private
 */
uiFormGuide_RuleSet.prototype.submitForm = function(domEvent, form, event) {
	submitForm(form, event);
};
