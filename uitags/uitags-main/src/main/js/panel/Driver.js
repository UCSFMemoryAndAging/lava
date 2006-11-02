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
 * @class Facade to the panel suite.
 * @extends uiUtil_Object
 */
function uiPanel_Driver() {
  this._super();
  /**
   * @type hash&lt;uiPanel_Suite&gt;
   * @private
   */
  this.__suites = new Array();
}

uiPanel_Driver = uiUtil_Object.declareSingleton(uiPanel_Driver, uiUtil_Object);

uiHtml_Window.getInstance().prependEventHandler("load", function(e) {
  uiPanel_driver = new uiPanel_Driver();
});


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Creates a panel suite.
 *
 * @param {String} divId ID of the panel DOM object (an HTML
 *     <i>div</i> element)
 * @return ID of the created suite
 * @type String
 */
uiPanel_Driver.prototype.createSuite = function(divId) {
  var suite = uiPanel_Suite.createByEither(divId, null);
  this.__suites[divId] = suite;

  return divId;  // the suite ID just happens to be the same as the div ID
};

/**
 * Returns the panel with the given ID or <code>null</code> if none.
 *
 * @param {String} panelId the ID of the panel to obtain
 * @return the panel with the given ID or <code>null</code> if none
 * @type uiPanel_Suite
 */
uiPanel_Driver.prototype.getSuite = function(panelId) {
  return this.__suites[panelId];
};

/**
 * Sets the listener to respond to the panel's life cycle events.
 *
 * @param {String} panelId the ID of the panel suite
 * @param {uiPanel_DefaultLifeCycleListener} listener the listener
 */
uiPanel_Driver.prototype.setLifeCycleListener = function(panelId, listener) {
  this.__suites[panelId]._setLifeCycleListener(listener);
};

/**
 * Set an anchor element to assist determining a panel's position. Either
 * one of <code>anchorId</code> or <code>anchorName</code> has to be
 * specified.
 *
 * @param {String} panelId the ID of the panel suite
 * @param {String} anchorId the ID of the anchor DOM object
 * @param {String} anchorName the name of the anchor DOM object
 */
uiPanel_Driver.prototype.setPositionAnchor = function(panelId, anchorId, anchorName) {
  var anchor = uiHtml_Element.createByEither(anchorId, anchorName);
  this.__suites[panelId]._setPositionAnchor(anchor);
};

/**
 * Registers a handle for dragging a certain panel. Either one of
 * <code>draggerId</code> or <code>draggerName</code> has to be specified.
 *
 * @param {String} panelId the ID of the panel suite
 * @param {String} draggerId the ID of the dragger DOM object
 * @param {String} draggerName the name of the dragger DOM object
 */
uiPanel_Driver.prototype.registerDragElement = function(
    panelId, draggerId, draggerName) {
  var handle = uiHtml_Element.createByEither(draggerId, draggerName);
  this.__suites[panelId].enableDragSupport(handle);
};

/**
 * Registers an element to disable hiding functionality of a panel. Either
 * one of <code>stickerId</code> or <code>stickerName</code> has to be
 * specified.
 *
 * @param {String} panelId the ID of the panel suite
 * @param {String} stickerId the ID of the sticker DOM object
 * @param {String} stickerName the name of the sticker DOM object
 */
uiPanel_Driver.prototype.registerStickElement = function(
    panelId, stickerId, stickerName) {
  var toggle = uiHtml_Toggle.createByEither(
      stickerId, stickerName, null, null, false);
  this.__suites[panelId]._setSticker(toggle);
};

/**
 * Registers a trigger element for showing a panel. Either one of
 * <code>triggerId</code> or <code>triggerName</code> has to be specified.
 *
 * @param {String} panelId the ID of the panel suite
 * @param {String} triggerId the ID of the trigger DOM object
 * @param {String} triggerName the name of the trigger DOM object
 * @param {String} eventName name of the event that triggers the showing
 * @param {int} actionDelay a delay before showing the panel (in milliseconds)
 * @param {boolean} attachedToMouse <code>true</code> to indicate that
 *     when the panel shows, its top-left corner will stick to the mouse
 *     cursor, while <code>false</code> to indicate that the original
 *     position will be used
 */
uiPanel_Driver.prototype.registerShowTrigger = function(
    panelId, triggerId, triggerName, eventName, actionDelay, attachedToMouse) {
  var trigger = uiHtml_Element.createByEither(triggerId, triggerName);
  this.__suites[panelId]._setShowTrigger(trigger, eventName, actionDelay, attachedToMouse);
};

/**
 * Registers a trigger element for hiding a panel. Either one of
 * <code>triggerId</code> or <code>triggerName</code> has to be specified.
 *
 * @see #registerStickElement
 * @param {String} panelId the ID of the panel suite
 * @param {String} triggerId the ID of the trigger DOM object
 * @param {String} triggerName the name of the trigger DOM object
 * @param {String} eventName name of the event that triggers the hiding
 * @param {int} actionDelay a delay before hiding the panel (in milliseconds)
 * @param {boolean} force <code>true</code> to indicate that the panel will
 *     be hidden no matter whether it is <i>stuck</i> or not, while
 *     <code>false</code> means that when the panel is <i>stuck</i> it
 *     will not be hidden
 */
uiPanel_Driver.prototype.registerHideTrigger = function(
    panelId, triggerId, triggerName, eventName, actionDelay, force) {
  var trigger = uiHtml_Element.createByEither(triggerId, triggerName);
  this.__suites[panelId]._setHideTrigger(trigger, eventName, actionDelay, force);
};
