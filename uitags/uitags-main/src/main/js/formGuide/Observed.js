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

  // autocomplete widgets require special handling, so handle their own
  //  onchange event handling, allowing a callback function to be set
  var autoCompleteObj = ACS['acs_textbox_' + this.__widgetGroup._getItems()[0].id];
  if (autoCompleteObj != null) {
    //alert("registering event handler with acs");  
    // autocomplete passes the autocomplete element (i.e. the hidden selectbox
    // of the autocomplete) to the handler
  	autoCompleteObj.onChangeCallback = observed._respond;
  }
  else {
    // standard element event handler regsitration. assign event handler function 
    // to this element. depending upon the type of element and the browser, the
    // event handler function could be assigned to onchange, onclick, or maybe
    // something else (see the ..Wrapper scripts appendEventHandler methods)
    widgetGroup.appendEventHandler("change", function(e) {
      // Wrap this in an anonymous function to make sure that the function
      // is executed under the proper owner.
      observed._respond(e);
    });
    
    
    // for calendar control, prepend "cal_" to element id and if this object exists, it is the javascript
	//  calendar popup object (i.e. the calendar control is not a form field, it is a non-form object), 
	//  so register a callback for the onChange event for the text input field
    //  associated with the calendar popup, i.e. this.__widget
    if (uiHtml_Element.getWidgetType(this.__widgetGroup._getItems()[0]) == 'text') {
        // have to enclose in try/catch because eval will throw an error if object does not exist
	    try {
		    obj = eval("cal_" + his.__widgetGroup._getItems()[0].id);
			if (obj != null) {
				obj.setOnChangeCallback(observed._respond);
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
 * undoActions from executing
 */
uiFormGuide_Observed.prototype._respond = function(domEvent) {
  // (ctoohey)
  for (var i = 0; i < this.__ruleSets.length; ++i) {
    if (this.__ruleSets[i]._allIgnoreRulesHold()) {
      return;
    }
  }

  var ruleSetsToExecute = new Array();
  for (var i = 0; i < this.__ruleSets.length; ++i) {
    if (this.__ruleSets[i]._allRulesHold()) {
      // if prompt attribute was supplied, prompt the user to give them a chance to 
      // cancel the doAction(s). do not issue this prompt when the event was simulated
      // on page load to set up the page, i.e. when domEvent is null
      var doIt = true;
      if (i == 0 && domEvent && prompt) {
        doIt = confirm(prompt);
      }
      if (!doIt) {
        return false;
      }
      ruleSetsToExecute.push(this.__ruleSets[i]);
    }
    else {
      this.__ruleSets[i]._undoAction(domEvent);
    }
  }

  // Perform the do actions last, because we do not want the
  // actions to get undone later.
  for (var i = 0; i < ruleSetsToExecute.length; i++) {
    ruleSetsToExecute[i]._doAction(domEvent);
  }
};
