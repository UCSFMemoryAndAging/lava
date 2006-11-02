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

  var observed = this;
  widgetGroup.appendEventHandler("change", function(e) {
    // Wrap this in an anonymous function to make sure that the function
    // is executed under the proper owner.
    observed._respond(e);
  });
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
 */
uiFormGuide_Observed.prototype._respond = function(domEvent) {
  var ruleSetsToExecute = new Array();
  for (var i = 0; i < this.__ruleSets.length; ++i) {
    if (this.__ruleSets[i]._allRulesHold()) {
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
