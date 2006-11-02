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
 * @class The listener of {@link uiPanel_Suite}'s life cycle events.
 * @extends uiUtil_Object
 */
function uiPanel_LifeCycleListener() {
  this._super();
}

uiPanel_LifeCycleListener = uiUtil_Object.declareClass(
    uiPanel_LifeCycleListener, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Responds to the event triggered before a panel showing is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the showing
 * @param {HTMLDivElement} domDiv DOM object of the panel
 * @return <code>true</code> to proceed with the showing,
 *     <code>false</code> to cancel it
 * @type boolean
 */
uiPanel_LifeCycleListener.prototype.onBeforeShow = function(domEvent, domDiv) {
//  alert("before showing " + domDiv.id);
  return true;  // proceed with the showing
};

/**
 * Responds to the event triggered after a panel showing is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the showing
 * @param {HTMLDivElement} domDiv DOM object of the panel
 */
uiPanel_LifeCycleListener.prototype.onAfterShow = function(domEvent, domDiv) {
//  alert("after showing: " + domDiv.id);
};

/**
 * Responds to the event triggered before a panel hiding is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the hiding
 * @param {HTMLDivElement} domDiv DOM object of the panel
 * @return <code>true</code> to proceed with the hiding,
 *     <code>false</code> to cancel it
 * @type boolean
 */
uiPanel_LifeCycleListener.prototype.onBeforeHide = function(domEvent, domDiv) {
//  alert("before hiding: " + domDiv.id);
  return true;  // proceed with the hiding
};

/**
 * Responds to the event triggered after a panel showing is performed.
 *
 * @param {DOMEvent} domEvent the event that triggers the hiding
 * @param {HTMLDivElement} domDiv DOM object of the panel
 */
uiPanel_LifeCycleListener.prototype.onAfterHide = function(domEvent, domDiv) {
//  alert("after hiding: " + domDiv.id);
};
