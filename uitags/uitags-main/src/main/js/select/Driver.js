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

function uiSelect_Driver() {
  this._super();
}

uiSelect_Driver = uiUtil_Object.declareSingleton(uiSelect_Driver, uiUtil_Object);

uiHtml_Window.getInstance().prependEventHandler("load", function(e) {
  uiSelect_driver = new uiSelect_Driver();
});


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiSelect_Driver.prototype.createAllNoneSuite = function(
    toggleId, toggleName, listId, listName,
    optIsOn, optOnPropArray, optOffPropArray) {
  var toggle = uiHtml_Toggle.createByEither(
      toggleId, toggleName, null, null, uiUtil_Type.getBoolean(optIsOn, false));
  if (optOnPropArray) {
    toggle.setOnProperties(optOnPropArray);
  }
  if (optOffPropArray) {
    toggle.setOffProperties(optOffPropArray);
  }

  var suite = new uiSelect_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initAllNoneToggle(toggle);
};

uiSelect_Driver.prototype.createAllSuite = function(triggerId, triggerName, listId, listName) {
  var suite = new uiSelect_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initAllTrigger(uiHtml_Element.createByEither(triggerId, triggerName));
};

uiSelect_Driver.prototype.createNoneSuite = function(triggerId, triggerName, listId, listName) {
  var suite = new uiSelect_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initNoneTrigger(uiHtml_Element.createByEither(triggerId, triggerName));
};

uiSelect_Driver.prototype.createInverseSuite = function(triggerId, triggerName, listId, listName) {
  var suite = new uiSelect_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initInverseTrigger(uiHtml_Element.createByEither(triggerId, triggerName));
};

uiSelect_Driver.prototype.createRangeSuite = function(triggerId, triggerName, listId, listName) {
  var suite = new uiSelect_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initRangeTrigger(uiHtml_Element.createByEither(triggerId, triggerName));
};

