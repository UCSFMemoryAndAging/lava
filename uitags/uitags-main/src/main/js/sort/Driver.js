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

function uiSort_Driver() {
  this._super();
}

uiSort_Driver = uiUtil_Object.declareSingleton(uiSort_Driver, uiUtil_Object);

uiHtml_Window.getInstance().prependEventHandler("load", function(e) {
  uiSort_driver = new uiSort_Driver();
});


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiSort_Driver.prototype.createAscendingSuite = function(triggerId, triggerName, listId, listName) {
  var suite = new uiSort_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initAscendingTrigger(uiHtml_Element.createByEither(triggerId, triggerName));
};

uiSort_Driver.prototype.createDescendingSuite = function(triggerId, triggerName, listId, listName) {
  var suite = new uiSort_Suite(uiHtml_Group.createByEither(listId, listName));
  suite._initDescendingTrigger(uiHtml_Element.createByEither(triggerId, triggerName));
};
