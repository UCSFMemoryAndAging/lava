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

function uiOptionTransfer_Driver() {
  this._super();

  this.__suites = new Array();
}

uiOptionTransfer_Driver =
    uiUtil_Object.declareSingleton(uiOptionTransfer_Driver, uiUtil_Object);

uiHtml_Window.getInstance().prependEventHandler("load", function(e) {
  uiOptionTransfer_driver = new uiOptionTransfer_Driver();
});


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiOptionTransfer_Driver.prototype.createSuite = function(
    sourceId, sourceName,
    targetId, targetName) {
  var sourceList = uiHtml_Select.createByEither(sourceId, sourceName);
  var targetList = uiHtml_Select.createByEither(targetId, targetName);

  var suiteId = this.__suites.length;
  this.__suites[suiteId] = new uiOptionTransfer_Suite(sourceList, targetList);
  return suiteId;
};

uiOptionTransfer_Driver.prototype.registerTransferTrigger = function(
    suiteId, triggerId, triggerName) {
  var trigger = uiHtml_Element.createByEither(triggerId, triggerName);
  this.__suites[suiteId]._setTransferTrigger(trigger);
};

uiOptionTransfer_Driver.prototype.registerReturnTrigger = function(
    suiteId, triggerId, triggerName) {
  var trigger = uiHtml_Element.createByEither(triggerId, triggerName);
  this.__suites[suiteId]._setReturnTrigger(trigger);
};

uiOptionTransfer_Driver.prototype.registerTransferAllTrigger = function(
    suiteId, triggerId, triggerName) {
  var trigger = uiHtml_Element.createByEither(triggerId, triggerName);
  this.__suites[suiteId]._setTransferAllTrigger(trigger);
};

uiOptionTransfer_Driver.prototype.registerReturnAllTrigger = function(
    suiteId, triggerId, triggerName) {
  var trigger = uiHtml_Element.createByEither(triggerId, triggerName);
  this.__suites[suiteId]._setReturnAllTrigger(trigger);
};
