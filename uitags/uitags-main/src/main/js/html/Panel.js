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

//////////////////////////////////////
////////// Class Definition //////////
//////////////////////////////////////

/**
 * Creates a basic panel.
 *
 * @class A basic panel.
 * @extends uiHtml_Element
 * @param {HTMLDivElement} domDiv the &lt;div&gt; being turned into a panel
 * @param {boolean} optUseIFrame an optional argument to specify whether to
 *     lay an iframe on top of the &lt;div&gt;. Default is <code>true</code>
 *     if in IE.
 */
function uiHtml_Panel(domDiv, optUseIFrame) {
  this._super(domDiv);

  /**
   * @type HTMLDivElement
   * @private
   */
  this.__domDiv = domDiv;
  /**
   * @type HTMLElement
   * @private
   */
  this.__contentDomElement = null;
  /**
   * Indicates whether focus support has been enabled.
   * @type boolean
   * @private
   */
  this.__focusSupportEnabled = false;
  /**
   * Indicates whether the panel will focus when the mouse is down.
   * @type boolean
   * @private
   */
  this.__willFocus = false;
  /**
   * Indicates whether the panel is currently focused.
   * @type boolean
   * @private
   */
  this.__isFocused = false;
  /**
   * Delegate for the panel's scrolling feature.
   * @type uiHtml_ScrollSupporter
   * @private
   */
  this.__scrollSupporter = new uiHtml_ScrollSupporter(this.__domDiv);
  /**
   * Iframe to lay on top of the panel's &lt;div&gt;. Only used if in IE.
   * This variable stays null if in other browser.
   * @type HTMLIFrameElement
   * @private
   */
  this.__iframe = null;

  // An iframe is used in IE to solve select widget's z-index problem.
  // Reference: http://www.macridesweb.com/oltest/IframeShim.html .
  if (uiUtil_Type.getBoolean(optUseIFrame, true) &&
      uiHtml_Window.getInstance().isIe()) {
    this.__createIFrame();
  }
}

uiHtml_Panel = uiUtil_Object.declareClass(uiHtml_Panel, uiHtml_Element);

/**
 * Keeps track the panels with focus support enabled.
 * @type uiHtml_Panel[]
 * @private
 */
uiHtml_Panel.__focusEnabledPanels = new Array();


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/////
// uiHtml_ScrollSupporter delegation starts

/**
 * Shows both horizontal and vertical scroll bars.
 *
 * @param {boolean} always whether the scroll bars always appear regardless
 *     of whether the content overflows the panel
 */
uiHtml_Panel.prototype.showScrollBars = function(always) {
  this.__scrollSupporter.showScrollBars(always);
  this.__updateIFrameDimension();
};

/**
 * Hides both horizontal and vertical scroll bars.
 */
uiHtml_Panel.prototype.hideScrollBars = function() {
  this.__scrollSupporter.hideScrollBars();
  this.__updateIFrameDimension();
};

/**
 * Moves the panel's "viewport" to the top of the panel.
 */
uiHtml_Panel.prototype.scrollToTop = function() {
  this.__scrollSupporter.scrollToTop();
};

/**
 * Moves the panel's "viewport" to the bottom of the panel.
 */
uiHtml_Panel.prototype.scrollToBottom = function() {
  this.__scrollSupporter.scrollToBottom();
};
// uiHtml_ScrollSupporter delegation ends
/////

/**
 * Creates and prepares an iframe if in IE. The newly created iframe is set
 * to the instance variable <code>__iframe</code>.
 * @private
 */
uiHtml_Panel.prototype.__createIFrame = function() {
  this.__iframe = new uiHtml_Element(
      uiHtml_Document.getInstance().createDomObject("iframe"));

  var domIFrame = this.__iframe.getDomObject();
  domIFrame.frameBorder = "0";
  domIFrame.scrolling = "no";
  domIFrame.src = "javascript: false;";
  // In Mozilla, background is by default transparent, which is what we want.
  // In IE, background is by default white/grey.
  // Making the background transparent in IE will make the iframe appears
  // behind select boxes, which defeats the purpose of using the iframe,
  // thus the following should be avoided:
  // domIFrame.style.backgroundColor = "transparent";
  // domIFrame.allowTransparency = true;

  // Allow transparent panel background in IE.
  // This doesn't work for windowed element, such as select widget --
  // the widget will just disappear, which is not too bad, because
  // it still emulates transparent window.
  // NOTE: transparency does not seem to work in WINE.
  domIFrame.style.filter =
      "progid:DXImageTransform.Microsoft.Alpha(style = 0, opacity = 0)";

  this.__iframe.setDepth(this.getDepth() - 1);
  this.__iframe.setStyleAttribute("position", "absolute");
  this.__updateIFrameDimension();

  if (this.isAppearing()) {
    this.__iframe.appear();
  }
  else {
    this.__iframe.disappear();
  }

  if (this.isShowing()) {
    this.__iframe.show();
  }
  else {
    this.__iframe.hide();
  }
};

/**
 * Keeps the panel's and iframe's position in sync.
 * @private
 */
uiHtml_Panel.prototype.__updateIFrameDimension = function() {
  if (this.__iframe == null) {
    return;
  }
  this.__iframe.setDimensionObject(this.getAbsoluteDimension());
};

/*
 * Overrides parent's method.
 */
uiHtml_Panel.prototype.show = function() {
  this._callSuper("show");
  if (this.__iframe != null) {
    this.__iframe.show();
  }
  // any child elements will automatically show as well
};

/*
 * Overrides parent's method.
 */
uiHtml_Panel.prototype.hide = function() {
  this._callSuper("hide");
  if (this.__iframe != null) {
    this.__iframe.hide();
  }
  // any child elements will automatically hide as well
};

/**
 * Similar to {@link #show}, except appearing takes up space.
 */
uiHtml_Panel.prototype.appear = function() {
  this._callSuper("appear");
  if (this.__iframe != null) {
    this.__iframe.appear();
  }
  // any child elements will automatically appear as well
};

/*
 * Overrides parent's method.
 */
uiHtml_Panel.prototype.disappear = function() {
  this._callSuper("disappear");
  if (this.__iframe != null) {
    this.__iframe.disappear();
  }
  // any child elements will automatically disappear as well
};

/*
 * Overrides parent's method.
 */
uiHtml_Panel.prototype.setStyleAttribute = function(name, value) {
  this._callSuper("setStyleAttribute", name, value);

  switch (name) {
    case "position" :
        // setting this property might change the panel's position
        this.__updateIFrameDimension();
        break;
  }
};

/*
 * Overrides parent's method.
 */
uiHtml_Panel.prototype.setDimensionObject = function(dimension) {
  this._callSuper("setDimensionObject", dimension);
  this.__updateIFrameDimension();
};

/*
 * Overrides parent's method.
 */
uiHtml_Panel.prototype.setDimension = function(x, y, width, height) {
  this._callSuper("setDimension", x, y, width, height);
  this.__updateIFrameDimension();
};

/*
 * Overrides parent's method.
 */
uiHtml_Panel.prototype.setDepth = function(depth) {
  this._callSuper("setDepth", depth);

  // This makes sure the iframe is just below the panel (by 1 z-index).
  if (this.__iframe != null) {
    this.__iframe.setDepth(depth - 1);
  }
};

/**
 * Performs necessary initialization in order to be able to simulate
 * focus and blur events.
 * <p>
 * Firefox, IE, and Opera do not support onfocus & onblur for div. In IE,
 * the iframe's event cannot be triggered because it is covered by the div,
 * thus we need this workaround
 *
 * @private
 */
uiHtml_Panel.prototype.__enableFocusSupport = function() {
  if (this.__focusSupportEnabled == true) {
    return;
  }
  this.__focusSupportEnabled = true;

  var panel = this;
  this.appendEventHandler("mouseover", function(e) {
    panel.__willFocus = true;
  });
  this.appendEventHandler("mouseout", function(e) {
    panel.__willFocus = false;
  });

  uiHtml_Document.getInstance().appendEventHandler("mousedown", function(e) {
    uiHtml_Panel.__executeFocusHandlers(e);
  });

  uiHtml_Panel.__focusEnabledPanels.push(this);
};

/**
 * Same as the parent's method, except that this method specifically ensures
 * that the "focus" and "blur" events are supported.
 *
 * @param {String} event
 * @param {function} handler
 */
uiHtml_Panel.prototype.prependEventHandler = function(event, handler) {
  if (event == "focus" || event == "blur") {
    this.__enableFocusSupport();
  }
  return this._callSuper("prependEventHandler", event, handler);
};

/**
 * Same as the parent's method, except that this method specifically ensures
 * that the "focus" and "blur" events are supported.
 *
 * @param {String} event
 * @param {function} handler
 */
uiHtml_Panel.prototype.appendEventHandler = function(event, handler) {
  if (event == "focus" || event == "blur") {
    this.__enableFocusSupport();
  }
  return this._callSuper("appendEventHandler", event, handler);
};

/**
 * Focuses this panel.
 */
uiHtml_Panel.prototype.focus = function() {
  this.__isFocused = true;
};

/**
 * Executes onfocus handler.
 *
 * @param {String} eventName valid values are "focus" and "blur"
 * @param {DOMEvent} domEvent the actual triggered event
 * @param {boolean} focusRequired will be compared with <code>this.__isFocused</code>
 * @private
 */
uiHtml_Panel.prototype.__executeFocusFunction = function(
    eventName, domEvent, focusRequired) {
  this.__logger.debug("isFocused: " + this.__isFocused);

  if (this.__isFocused == focusRequired) {
    this.executeAggregateEventHandler(eventName, domEvent);

    // Although the function may not be executed, we still need to change
    // the focus state.
    // NOTE: this does not always change the state -- 'focusRequired'
    // might hold the same value as 'isFocused'
    this.__isFocused = !this.__isFocused;
  }
};

/**
 * Sets the supplied DOM element as this panel's content.
 *
 * @param {HTMLElement} domElement the content
 */
uiHtml_Panel.prototype.setContent = function(domElement) {
  this.__contentDomElement = domElement;

  var domChild = this.__contentDomElement;
  var domParent = domChild.parentNode;
  var domThis = this.getDomObject();

  // only if the dom element hasn't been attached to the appropriate parent (which is this panel's div)
  if (domParent != domThis) {
    domParent.removeChild(domChild);
    domParent.appendChild(domThis);
    domThis.appendChild(domChild);
  }
};

/**
 * Returns the DOM element which is this panel's content.
 *
 * @return the DOM element which is this panel's content
 * @type HTMLElement
 */
uiHtml_Panel.prototype.getContent = function() {
  return this.__contentDomElement;
};

/*
 * Overrides parent's method.
 */
uiHtml_Panel.prototype.enableDragSupport = function(optTriggerElement) {
  this._callSuper("enableDragSupport", optTriggerElement);

  // In firefox 1.5, dragging panels with scroll bar causes shaky drag movement.
  // Remember that if one of the handle's ancestors has a scroll bar, it
  // will still be shaky, but this is out of this panel's control.
  try {
    optTriggerElement.hideScrollBars();
  }
  catch (e) {
    // Reasons why this might fail:
    // - trigger is not specified
    // - the trigger is not a scrollable element
  }
  this.hideScrollBars();
  this.setStyleAttribute("position", "absolute");
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * Goes through all panels with focus support enabled and executes
 * their event handler according to whether they focus or blur.
 *
 * @param {DOMEvent} domEvent the actual triggered event
 * @private
 */
uiHtml_Panel.__executeFocusHandlers = function(domEvent) {
  for (var i = 0; i < uiHtml_Panel.__focusEnabledPanels.length; ++i) {
    var panel = uiHtml_Panel.__focusEnabledPanels[i];
    if (panel.__willFocus == true) {
      // To trigger onFocus event handler the panel must NOT be
      // currently focused.
      panel.__executeFocusFunction("focus", domEvent, false);
    }
    else {
      panel.__executeFocusFunction("blur", domEvent, true);
    }
  }
};

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
 * @type uiHtml_Panel
 */
uiHtml_Panel.createByEither = function(id, name) {
  return uiHtml_Element.createByEither(id, name, uiHtml_Panel);
};

/**
 * Creates a DOM &lt;div&gt;, creates a panel object, and wraps the &lt;div&gt;
 * within the panel object.
 *
 * @param {boolean} optUseIFrame an optional argument to specify whether to
 *     lay an iframe on top of the &lt;div&gt;. By default the value is
 *     <code>true</code> if in IE.
 * @param {boolean} optAppear an optional argument whether the panel should
 *     appear initially. Default is <code>true</code>.
 * @return the newly created panel
 * @type uiHtml_Panel
 */
uiHtml_Panel.create = function(optUseIFrame, optAppear) {
  var domDiv = uiHtml_Document.getInstance().createDomObject("div", optAppear);
  return new uiHtml_Panel(domDiv, optUseIFrame);
};
