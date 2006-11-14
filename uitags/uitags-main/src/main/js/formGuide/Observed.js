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
 * Creates an instance which wraps a widget group. The widget group's
 * event handler list gets manipulated to make observation possible.
 *
 * (ctoohey) modified to register event handlers for autocomplete
 * and CalendarPopup widgets.
 *
 * @class An observation target which may be a single widget or
 *     a group of multiple widgets. Corresponds to an instance of
 *     &lt;observed&gt; within a &lt;formGuide&gt;.
 * @extends uiUtil_Object
 * @param {uiHtml_Group} widgetGroup
 */
function uiFormGuide_Observed(widgetGroup) {
  this._super();
  /**
   * @type uiFormGuide_RuleSet[]
   * @private
   */
  this.__ruleSets = new Array();
  
  this.__widgetGroup = widgetGroup;

  var observed = this;

  // autocomplete widgets require special handling, so handle their own onchange event 
  // handling, allowing a callback function to be set.
  // know that if element is an autocomplete, dealing with a selectbox, in which case
  //  widgetGroup is a uiHtml_Select object and the selectbox is the domObject
  var autoCompleteObj = null;
  if (this.__widgetGroup.__type == "select") {
    autoCompleteObj = ACS['acs_textbox_' + this.__widgetGroup.getDomObject().id];
  }  
  if (autoCompleteObj != null) {
    //alert("registering event handler with acs");  
    // autocomplete does not pass any arguments to the handler, i.e. the domEvent arg is null
  	autoCompleteObj.onChangeCallback = observed._respond;
  	autoCompleteObj.onChangeCallbackOwner = this;
  }
  else {
    // standard element event handler regsitration. assign event handler function 
    // to this element. depending upon the type of element and the browser, the
    // event handler function could be assigned to onchange, onclick, or maybe
    // something else (see the ..Wrapper scripts appendEventHandler methods)
    /*
    if (this.__widgetGroup._getItems()[0].id) {
      alert("calling appendEventHandler for widget=" + this.__widgetGroup._getItems()[0].id);
    }
    else {
      alert("calling appendEventHandler for widget=" + this.__widgetGroup.getDomObject().id);
    } 
    */     
    widgetGroup.appendEventHandler("change", function(e) {
      // Wrap this in an anonymous function to make sure that the function
      // is executed under the proper owner.
      return observed._respond(e);
    });
    
    
    // for calendar control, prepend "cal_" to element id and if this object exists, it is the javascript
	//  calendar popup object (i.e. the calendar control is not a form field, it is a non-form object), 
	//  so register a callback for the onChange event for the text input field
    //  associated with the calendar popup, i.e. this.__widget
    if (uiHtml_Element.getWidgetType(this.__widgetGroup._getItems()[0]) == 'text') {
        // have to enclose in try/catch because eval will throw an error if object does not exist
	    try {
		    obj = eval("cal_" + this.__widgetGroup._getItems()[0].id);
			if (obj != null) {
				obj.setOnChangeCallback(observed._respond);
				obj.setOnChangeCallbackOwner(this);
				// since a single CalendarPopup object services all date fields on the page, need to 
				// register which widgets should generate a callback with the CalendarPopup object
				obj.addCallbackWidget(this.__widgetGroup._getItems()[0]);
			}
		}
		catch (e) {
			// ignore exception	
		}
	}
  }
}

uiFormGuide_Observed = uiUtil_Object.declareClass(uiFormGuide_Observed, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Adds a rule set of which this observation target is a member. This method is
 * typically called right after an observation target is instantiated in order
 * to populate it.
 *
 * @param {uiFormGuide_RuleSet} ruleSet the rule set to add
 */
uiFormGuide_Observed.prototype.addRuleSet = function(ruleSet) {
  this.__ruleSets.push(ruleSet);
};

/**
 * The method to execute in response to the wrapped widget group's
 * onchange event.
 *
 * (ctoohey) modified to allow ignore rules to prevent any doActions or
 * undoActions from executing, to process the ignoreDoOnLoad/ignoreUndoOnLoad
 * formGuide tag attributes, and the prompt formGuide tag attribute
 * @param domEvent the event (null when simulated event on page load)
 * @param simulateRuleSets list of all RuleSets for the page if simulating an event. otherwise null.
 */
uiFormGuide_Observed.prototype._respond = function(domEvent, simulateRuleSets) {
  // when this is called on page load to simulate events to setup the page properly, a
  // list of all the RuleSets on the page is passed in as the simulateRuleSets argument,
  // and this is the list of RuleSets which should be processed, so that each RuleSet is
  // only processed once
  
  // otherwise, when this function is called as an event handler in response to user event,
  // simulateRuleSets is null, and the RuleSets that were associated with the element that
  // is the target of the event are processed.
  // key point to remember about this function is that for the element which is the target
  // of the event (i.e. the "observed" element), this function processes ALL RuleSets
  // that were associated with this element on page load (see the page HTML source for
  // uiHtml_Window.getInstance().appendEventHandler for these associations), i.e. the
  // uiFormGuide_do1/undo1 RuleSet, the uiFormGuide_do2/undo2 RuleSet,etc.
  
  var ruleSetsToProcess;
  if (simulateRuleSets) {
    ruleSetsToProcess = simulateRuleSets;
  }
  else {
    ruleSetsToProcess = this.__ruleSets;
  }

  var ruleSetsToExecute = new Array();
  for (var i = 0; i < ruleSetsToProcess.length; ++i) {
    //alert("PROCESSING RuleSet=" + i + " length=" + ruleSetsToProcess.length + " doAction=" + ruleSetsToProcess[i].__doAction);
    if (ruleSetsToProcess[i]._allIgnoreRulesHold()) {
      //alert("Observed respond event handler, all ignore rules hold so ignoring for RuleSet=" + ruleSetsToProcess[i].__doAction);
      continue; // ignore this RuleSet doAction/undoAction
    }
    // if ignore rules do not hold, continue to determine whether observe rules hold and
    // act according
    if (ruleSetsToProcess[i]._allRulesHold()) {
      //alert("Observed respond event handler, allRulesHold=true for RuleSet=" + ruleSetsToProcess[i].__doAction);  
      // simulateRuleSets will always be non-null on page load, and false (null) on user-generated events
      if (simulateRuleSets) {
        if (ruleSetsToProcess[i].getIgnoreDoOnLoad()) {
          //alert("ignoreDoOnLoad");
          continue;
        }
      }
      // if prompt attribute was supplied, prompt the user to give them a chance to 
      // cancel the doAction(s). do not prompt on page load, and only prompt once,i.e.
      // on first iteration
	  var doIt = true;
      if (!simulateRuleSets && i == 0 && ruleSetsToProcess[i].getPrompt()) {
      	doIt = confirm(ruleSetsToProcess[i].getPrompt());
	  }
      if (!doIt) {
    	// if onclick handler (checkbox) returning false cancels the action so 
  		// nothing is set on the click
        return false; 
      }
      ruleSetsToExecute.push(ruleSetsToProcess[i]);
    }
    else {
      //alert("Observed respond event handler, allRulesHold=false for RuleSet=" + ruleSetsToProcess[i].__undoAction); 
      // simulateRuleSets will always be non-null on page load, and false (null) on user-generated events
      if (simulateRuleSets) {
        if (ruleSetsToProcess[i].getIgnoreUndoOnLoad()) {
          //alert("ignoreUndoOnLoad");
          continue;
        }
      } 
      ruleSetsToProcess[i]._undoAction(domEvent);
    }
  }

  // Perform the do actions last, because we do not want the
  // actions to get undone later.
  for (var i = 0; i < ruleSetsToExecute.length; i++) {
    ruleSetsToExecute[i]._doAction(domEvent);
  }
  
  return true;
};
