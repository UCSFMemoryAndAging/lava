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
 * Creates a rich panel.
 *
 * @class A rich panel (compared to its superclass).
 * @extends uiHtml_Panel
 * @param {HTMLDivElement} domDiv the &lt;div&gt; being turned into a panel
 */
function uiPanel_Suite(domDiv) {
  this._super(domDiv);

  /**
   * The DOM representation of the panel.
   * @type HTMLDivElement
   * @private
   */
  this.__domDiv = domDiv;
  /**
   * A panel is never hideable when the mouse cursor is hovering on the panel.
   * If this variable is true, it means the panel is stuck due to this
   * feature.
   * @type boolean
   * @private
   */
  this.__automaticallyStuck = false;
  /**
   * Whether the panel has been explicitly set to be unhideable.
   * @type boolean
   * @private
   */
  this.__explicitlyStuck = false;
  /**
   * The element to which this panel is anchored.
   * @type uiHtml_Element
   * @private
   */
  this.__positionAnchor = null;
  /**
   * ID of the timer for showing the panel (useful for cancelling the timer).
   * @type int
   * @private
   */
  this.__showTimerId = null;
  /**
   * ID of the timer for hiding the panel (useful for cancelling the timer).
   * @type int
   * @private
   */
  this.__hideTimerId = null;
  /**
   * A listener that responds to life cycle events.
   * @type uiPanel_DefaultLifeCycleListener
   * @private
   */
  this.__lifeCycleListener = null;

  /**
   * This panel's original z-index.
   * @type int
   * @private
   */
  this.__originalZIndex = this.getDepth();

  uiPanel_Suite.__addToZIndexGroup(this, this.__originalZIndex);

  // NOTE: have to click twice to activate a panel using onMouseDown
  // (seems to be a bug in Opera -- tested in 8.x), thus we'll use
  // onClick only for Opera
  var event = (uiHtml_Window.getInstance().isOpera())? "click" : "mousedown" ;
  var panel = this;
  this.appendEventHandler(event, function(e) {
    panel.__moveToTop();
  });

  var suite = this;
  this.appendEventHandler("mouseover", function(e) {
    suite.__disableHide(true);
  });
  this.appendEventHandler("mouseout", function(e) {
    suite.__enableHide(true);
  });
}

uiPanel_Suite = uiUtil_Object.declareClass(uiPanel_Suite, uiHtml_Panel);

/**
 * A map of uiPanel_Suite arrays keyed by zIndex. Each map entry is a group
 * of uiPanel_Suite objects sharing the same original zIndex.
 * During construction, however, the panel's zIndex gets modified so that
 * no two panels share the same zIndex anymore.
 * @private
 */
uiPanel_Suite.__zIndexGroups = new Array();


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Sets the listener to respond to the panel's life cycle events.
 *
 * @param {uiPanel_DefaultLifeCycleListener} listener the listener
 */
uiPanel_Suite.prototype._setLifeCycleListener = function(listener) {
  this.__lifeCycleListener = listener;
};

/**
 * Registers a toggle to be this panel's stick trigger (an object which
 * tells this panel to stick and unstick).
 *
 * @param {uiHtml_Toggle} toggle the stick trigger to register
 */
uiPanel_Suite.prototype._setSticker = function(toggle) {
  this.__sticker = toggle;

  var suite = this;
  this.__sticker.appendOnStateOn(function(e) {
    suite.__disableHide();
  });
  this.__sticker.appendOnStateOff(function(e) {
    suite.__enableHide();
  });
};

/**
 * Activates a timer for showing this panel.
 *
 * @param {integer} msDelay delay in milliseconds
 * @private
 */
uiPanel_Suite.prototype.__activateShowTimer = function(domEvent, msDelay) {
  this.__logger.debug("Activating panel show timer");

  var suite = this;
  this.__showTimerId = window.setTimeout(function() {
    suite.requestShow(domEvent);
  }, msDelay);
};

/**
 * Deactivates a timer for showing this panel.
 * @private
 */
uiPanel_Suite.prototype.__deactivateShowTimer = function() {
  if (this.__showTimerId != null) {
    this.__logger.debug("Deactivating panel show timer");
    window.clearTimeout(this.__showTimerId);
    this.__showTimerId = null;
  }
};

/**
 * Activates a timer to hide the panel.
 *
 * @param {integer} msDelay delay in milliseconds
 * @param {boolean} force see {@link #_setHideTrigger}
 * @private
 */
uiPanel_Suite.prototype.__activateHideTimer = function(domEvent, msDelay, force) {
  this.__logger.debug("Activating panel hide timer");

  var suite = this;
  this.__hideTimerId = window.setTimeout(function() {
    suite.requestHide(domEvent, force);
  }, msDelay);
};

/**
 * Deactivates a timer to hide the panel.
 * @private
 */
uiPanel_Suite.prototype.__deactivateHideTimer = function() {
  if (this.__hideTimerId != null) {
    this.__logger.debug("Deactivating panel hide timer");
    window.clearTimeout(this.__hideTimerId);
    this.__hideTimerId = null;
  }
};

/**
 * Registers a show trigger that will activate a show timer when an event
 * is triggered. The show timer however will be deactivated if the
 * opposite event is fired.
 *
 * @param {uiHtml_Element} trigger the element to be registered as the trigger
 * @param {String} eventType the type of event which triggers the showing of
 *     this panel
 * @param {integer} actionDelay the delay in milliseconds between
 *     the event getting fired and the panel getting shown
 * @param {boolean} attachedToMouse whether this panel should be shown at
 *     the mouse pointer
 */
uiPanel_Suite.prototype._setShowTrigger =
    function(trigger, eventType, actionDelay, attachedToMouse) {

  var suite = this;
  trigger.appendEventHandler(eventType, function(e) {
    if (!suite.isShowing()) {
      suite.__adjustPosition(attachedToMouse, new uiHtml_Event(e));
    }
    suite.__activateShowTimer(e, actionDelay);
  });

  // NOTE: event frequently triggered such as these should be canceled when
  // the opposite event is triggered
  if (eventType == "mouseover") {
    trigger.appendEventHandler("mouseout", function(e) {
      suite.__deactivateShowTimer();
    });
  }
  else if (eventType == "focus") {
    trigger.appendEventHandler("blur", function(e) {
      suite.__deactivateShowTimer();
    });
  }
};

/**
 * Registers a hide trigger that will activate a hide timer when an event
 * is triggered. The show timer however will be deactivated if the
 * opposite event is fired.
 *
 * @param {uiHtml_Element} trigger the element to be registered as the trigger
 * @param {String} eventType the type of event which triggers the hiding of
 *     this panel
 * @param {integer} actionDelay the delay in milliseconds between
 *     the event getting fired and the panel getting hidden
 * @param {boolean} force whether to forcefully hide this panel even if
 *     the mouse pointer is currently over the panel
 */
uiPanel_Suite.prototype._setHideTrigger =
    function(trigger, eventType, actionDelay, force) {
  var suite = this;
  trigger.appendEventHandler(eventType, function(e) {
    suite.__activateHideTimer(e, actionDelay, force);
  });

  // NOTE: event frequently triggered such as this should be canceled when
  // the opposite event is triggered
  if (eventType == "mouseout") {
    trigger.appendEventHandler("mouseover", function(e) {
      suite.__deactivateHideTimer();
    });
  }
  else if (eventType == "blur") {
    trigger.appendEventHandler("focus", function(e) {
      suite.__deactivateHideTimer();
    });
  }
};

/**
 * Registers an element to which this panel is anchored. This method should
 * be called only at load time.
 *
 * @param {uiHtml_Element} anchor an element to which this panel is anchored
 */
uiPanel_Suite.prototype._setPositionAnchor = function(anchor) {
  this.__positionAnchor = anchor;
  // We're now executing in response to body loading, adjust the panel's
  // position.
  this.__adjustPosition(false, null);
};

/**
 * Disables panel hiding (either automatically or manually -- see comments
 * in constructor for detail).
 *
 * @param {boolean} auto
 * @private
 */
uiPanel_Suite.prototype.__disableHide = function(auto) {
  if (auto) {
    this.__automaticallyStuck = true;
  }
  else {
    this.__explicitlyStuck = true;
  }
};

/**
 * Enables panel hiding (either automatically or manually -- see comments
 * in constructor for detail).
 *
 * @param {boolean} auto
 * @private
 */
uiPanel_Suite.prototype.__enableHide = function(auto) {
  if (auto) {
    this.__automaticallyStuck = false;
  }
  else {
    this.__explicitlyStuck = false;
  }
};

/**
 * Checks whether the panel should hide given its sticking status.
 *
 * @return <code>true</code> if the panel should hide,
 *     <code>false</code> otherwise
 * @type boolean
 * @private
 */
uiPanel_Suite.prototype.__shouldHide = function() {
  if (!this.__automaticallyStuck && !this.__explicitlyStuck) {
    return true;
  }
  return false;
};

/**
 * Adjusts the position of the panel using the position of either
 * the mouse cursor or a registered anchor.
 *
 * @param {boolean} attachedToMouse see {@link #_setShowTrigger}
 * @param {uiHtml_Event} event the event which causes this method to get
 *     invoked
 * @private
 */
uiPanel_Suite.prototype.__adjustPosition = function(attachedToMouse, event) {
  // Prioritize mouse based positioning over anchor based - makes more sense.
  if (attachedToMouse) {
    this.setStyleAttribute("position", "absolute");
    var position = event.getAbsolutePosition();
    this.setDimension(position.getLeft(), position.getTop());
  }
  else if (this.__positionAnchor) {
    this.setStyleAttribute("position", "absolute");
    this.__adjustPositionRelativeTo(this.__positionAnchor);
  }
  // else, no explicit position mentioned, no need to move the panel.
};

/**
 * Notifies all the life cycle listeners of any of the panel's life
 * cycle events.
 *
 * @param {String} eventName the name of the event
 * @param {DOMEvent} domEvent the triggered event
 * @return <code>true</code> to proceed with the event handling,
 *     <code>false</code> to cancel the handling
 * @type boolean
 * @private
 */
uiPanel_Suite.prototype.__notifyLifeCycleEvent = function(eventName, domEvent) {
  if (this.__lifeCycleListener) {
    var eventHandler = this.__lifeCycleListener[eventName];
    if (eventHandler != null) {
      return eventHandler.call(this.__lifeCycleListener,
          uiHtml_Event.__getDomObject(domEvent), this.__domDiv);
    }
  }
  return true;
};

/**
 * Shows this panel and calls the showing-related life cycle methods.
 *
 * @see #show()
 * @param {DOMEvent} domEvent the event that triggers the showing
 */
uiPanel_Suite.prototype.requestShow = function(domEvent) {
  if (!this.__notifyLifeCycleEvent("onBeforeShow", domEvent)) {
    return;
  }
  this.show();
  this.__notifyLifeCycleEvent("onAfterShow", domEvent);
};

/**
 * Shows this panel and moves it above other panels with the same z-index.
 * Note that calling this method directly will not invoke the life cycle
 * methods, thus it is recommended to use {@link #requestShow} instead.
 */
uiPanel_Suite.prototype.show = function() {
  this.focus();
  this.__moveToTop();
  this._callSuper("show");
};

/**
 * Hides this panel and calls the hiding-related life cycle methods.
 * This panel will not hide if it is sticking unless the supplied
 * <code>force</code> is <code>true</code>.
 *
 * @see #hide()
 * @param {DOMEvent} domEvent the event that triggers the hiding
 * @param {boolean} force see {@link #_setHideTrigger}
 */
uiPanel_Suite.prototype.requestHide = function(domEvent, force) {
  if (this.__shouldHide() || force) {
    if (!this.__notifyLifeCycleEvent("onBeforeHide", domEvent)) {
      return;
    }
    this.hide();
    this.__notifyLifeCycleEvent("onAfterHide", domEvent);
  }
};

/**
 * Hides this panel. Note that calling this method directly will not invoke
 * the life cycle methods, thus it is recommended to use {@link #requestHide}
 * instead.
 */
uiPanel_Suite.prototype.hide = function() {
  this._callSuper("hide");
};

/**
 * Adjusts this panel's position relative to the position of the
 * supplied anchor.
 *
 * @param {uiHtml_Element} the anchor
 * @private
 */
uiPanel_Suite.prototype.__adjustPositionRelativeTo = function(anchor) {
  var maxWidth = uiHtml_Document.getInstance().getWidth();

  var anchorDimension = anchor.getAbsoluteDimension();
  var anchorLeft = anchorDimension.getLeft();
  var anchorRight = anchorDimension.getRight();
  var anchorTop = anchorDimension.getTop();

  var panelWidth = this.getWidth();
  var lhsLeft = anchorLeft - panelWidth;
  var crossesLeftBoundary = (lhsLeft < 0);
  var crossesRightBoundary = ((anchorRight + panelWidth) > maxWidth);

  if (crossesRightBoundary && !crossesLeftBoundary) {
    this.setDimension(lhsLeft, anchorTop);
  }
  else {
    this.setDimension(anchorRight, anchorTop);
  }
};

/**
 * Moves this panel to the top of the panel stack.
 * Also causes the current top panel to drop to second top-most.
 *
 * @private
 */
uiPanel_Suite.prototype.__moveToTop = function() {
  var zIndexGroup = uiPanel_Suite.__zIndexGroups[this.__originalZIndex];
  var topZIndex = this.__originalZIndex + zIndexGroup.length - 1;
  var oldZIndex = this.getDepth();

  for (var i = 0; i < zIndexGroup.length; i++) {
    if (zIndexGroup[i].getDepth() > oldZIndex) {
      zIndexGroup[i].setDepth(zIndexGroup[i].getDepth() - 1);
    }
  }

  this.setDepth(topZIndex);
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * If <code>id</code> is specified, creates a panel object wrapping a DOM
 * element with the supplied <code>id</code>. Otherwise creates a panel
 * object wrapping a DOM element with the supplied <code>name</code>.
 *
 * @param {String} id may be <code>null</code> if the <code>name</code>
 *     argument is not
 * @param {String} name may be <code>null</code> if the <code>id</code>
 *     argument is not
 * @return the newly created panel
 * @type uiPanel_Suite
 */
uiPanel_Suite.createByEither = function(id, name) {
  return uiHtml_Element.createByEither(id, name, uiPanel_Suite);
};

/**
 * Registers the panel to a group of other panels with the same z-index.
 * This allows each panel to change its relative ordering as a reaction to
 * the user switching between panels.
 *
 * @param {uiPanel_Suite} panel the panel to register
 * @param {integer} zIndexValue to idenfity the group to which the panel
 *     is to be added
 * @private
 */
uiPanel_Suite.__addToZIndexGroup = function(panel, zIndexValue) {
  if (uiPanel_Suite.__zIndexGroups[zIndexValue] == null) {
    uiPanel_Suite.__zIndexGroups[zIndexValue] = new Array();
  }

  var zIndexGroup = uiPanel_Suite.__zIndexGroups[zIndexValue];
  panel.setDepth(zIndexValue + zIndexGroup.length);
  zIndexGroup.push(panel);
};
