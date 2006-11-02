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
 * Creates a trigger blocker by providing the trigger's ID or name.
 *
 * @class An implementation of {@link uiPanel_LifeCycleListener} to
 *     to allow a trigger (element) to act as a toggle to alternately
 *     show/hide a panel. This listener creates a layer (referred as
 *     <i>blocker</i>) on top of the trigger and alternately inserts
 *     and removes the layer. When the layer is inserted, any mouse
 *     click attempt on the trigger will be blocked by the layer, and
 *     thus preventing the panel to be shown.
 *     <p>
 *     Additionally, the panel can be set up to hide when the layer is
 *     clicked, so that we can achieve the effect of having a trigger
 *     that toggles the panel's visibility. Note the importance of the
 *     layer, since without it, just after the panel is hidden, it will
 *     be reshown again, because the trigger receives the click event
 *     and thus respond to it accordingly.
 * @extends uiPanel_LifeCycleListener
 * @param {String} divId ID of the panel's div
 * @param {String} triggerId ID of the trigger
 * @param {String} triggerName name of the trigger
 */
function uiPanel_TriggerBlocker(divId, triggerId, triggerName) {
  this._super();

  /**
   * The trigger to cover.
   * @type uiHtml_Element
   * @private
   */
  this.__trigger = uiHtml_Element.createByEither(triggerId, triggerName);

  var domDiv = uiHtml_Document.getInstance().getDomObjectById(divId);
  var showing = uiHtml_ElementWrapper.getInstance().isShowing(domDiv);
  /**
   * The layer that covers the trigger.
   * @type uiHtml_Panel
   * @private
   */
  this.__blocker = uiHtml_Panel.create(true, showing);
  this.__blocker.setStyleAttribute("position", "absolute");
  this.__blocker.setDimensionObject(this.__trigger.getAbsoluteDimension());
  // Increase depth by 2 because in IE a panel is made up of
  // a div and an iframe.
  this.__blocker.setDepth(this.__trigger.getDepth() + 2);
}

uiPanel_TriggerBlocker = uiUtil_Object.declareClass(
    uiPanel_TriggerBlocker, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Inserts the layer, thus any future attempt to click the trigger will
 * be blocked, which will prevent the showing of the panel.
 *
 * @param {DOMEvent} domEvent the event that triggers the showing
 * @param {HTMLDivElement} domDiv DOM object of the panel
 */
uiPanel_TriggerBlocker.prototype.onAfterShow = function(domEvent, domDiv) {
  this.__blocker.appear();
};

/**
 * Removes the layer to unblock future event, which will re-enable the
 * showing of the panel.
 *
 * @param {DOMEvent} domEvent the event that triggers the showing
 * @param {HTMLDivElement} domDiv DOM object of the panel
 */
uiPanel_TriggerBlocker.prototype.onAfterHide = function(domEvent, domDiv) {
  this.__blocker.disappear();
};
