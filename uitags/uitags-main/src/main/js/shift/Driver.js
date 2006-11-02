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

function uiShift_Driver() {
  this._super();
}

uiShift_Driver = uiUtil_Object.declareSingleton(uiShift_Driver, uiUtil_Object);

uiHtml_Window.getInstance().prependEventHandler("load", function(e) {
  uiShift_driver = new uiShift_Driver();
});


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiShift_Driver.prototype.createUpSuite = function(triggerId, triggerName, listId, listName) {
  var suite = new uiShift_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initUpTrigger(uiHtml_Element.createByEither(triggerId, triggerName));
};

uiShift_Driver.prototype.createDownSuite = function(triggerId, triggerName, listId, listName) {
  var suite = new uiShift_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initDownTrigger(uiHtml_Element.createByEither(triggerId, triggerName));
};

uiShift_Driver.prototype.createFirstSuite = function(triggerId, triggerName, listId, listName) {
  var suite = new uiShift_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initFirstTrigger(uiHtml_Element.createByEither(triggerId, triggerName));
};

uiShift_Driver.prototype.createLastSuite = function(triggerId, triggerName, listId, listName) {
  var suite = new uiShift_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initLastTrigger(uiHtml_Element.createByEither(triggerId, triggerName));
};
