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
 * Creates a stateless wrapper for DOM elements.
 *
 * @class A stateless generic wrapper for DOM elements. Note that if
 *     there exists a class that wraps a specific type of DOM element,
 *     it is strongly recommended that that wrapper class is used
 *     instead of this class, because some functionalities provided
 *     by that class might have been be tuned specifically to handle
 *     the DOM element with a more appropriate/desirable behaviour.
 * @extends uiUtil_Object
 */
function uiHtml_ElementWrapper() {
  this._super();
}

// NOTE: Cannot make this class singleton since some other classes
//       are extending it.
uiHtml_ElementWrapper = uiUtil_Object.declareClass(
    uiHtml_ElementWrapper, uiUtil_Object);

/**
 * A single instance can be reused, since the object contains no state.
 *
 * @type uiHtml_ElementWrapper
 * @private
 */
uiHtml_ElementWrapper.__instance = null;

/**
 * An array that keeps track of all elements with extended properties.
 * This allows clean-ups to be performed on these elements when they
 * are not used anymore.
 *
 * @type Array
 * @private
 */
uiHtml_ElementWrapper.__extendedElements = new Array();


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/////
// Style-related method section starts.
/**
 * Returns a name that follows the javascript style of referring to
 * a style attribute name, e.g. z-index -> zIndex.
 *
 * @param {String} name the attribute name
 * @return the normalized name
 * @type String
 * @private
 */
uiHtml_ElementWrapper.prototype.__normalizeStyleAttributeName = function(name) {
  var index;
  while ((index = name.indexOf("-")) >= 0) {
    var nextChar = name.charAt(index + 1);
    name = name.replace(new RegExp("-" + nextChar), nextChar.toUpperCase());
  }
  return name;
};

/**
 * Returns a cascaded style attribute value of an element as specified by
 * global style sheets, inline styles, and HTML attributes.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} name the attribute name
 * @return the attribute value
 * @type String
 */
uiHtml_ElementWrapper.prototype.getCascadedStyleAttribute = function(domElement, name) {
  if (domElement.currentStyle) {  // IE 6.x
    return domElement.currentStyle[this.__normalizeStyleAttributeName(name)];
  }
  else if (document.defaultView) {  // firefox 1.x or opera 8.x
    var cascadedStyle = document.defaultView.getComputedStyle(domElement, "");
    return cascadedStyle.getPropertyValue(name);
  }
  else {
    // In IE, selectOption.currentStyle is somehow null, in which case
    // we'll just assume that we can't find the intended attribute name.
    return null;
  }
};

/**
 * Sets the value of a certain inline style attribute of an element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} name the attribute name
 * @param {String} value the attribute value
 */
uiHtml_ElementWrapper.prototype.setStyleAttribute = function(
      domElement, name, value) {
  domElement.style[this.__normalizeStyleAttributeName(name)] = value;
};

/**
 * Returns the <i>uitags</i> extended properties object of a DOM element.
 * The properties object is useful for maintaining additional states of
 * the element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @return the properties object
 * @type uiHtml_ElementWrapper.DomExtProps
 * @private
 */
uiHtml_ElementWrapper.prototype.__getExtendedProperties = function(
    domElement) {
  if (domElement.uitagsProps == null) {
    domElement.uitagsProps = new uiHtml_ElementWrapper.DomExtProps();
    uiHtml_ElementWrapper.__registerElementToFinalize(domElement);
  }
  else if (!(domElement.uitagsProps instanceof
      uiHtml_ElementWrapper.DomExtProps)) {
    // This might happen if there is interference from other libraries.
    throw new uiUtil_IllegalStateException(
        "domElement.uitagsProps is not a valid uitags properties object.");
  }
  return domElement.uitagsProps;
};

/**
 * Sets a certain extended property to a DOM element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} key the name of the property
 * @param {String} value the value of the property
 * @private
 */
uiHtml_ElementWrapper.prototype.__setExtendedProperty = function(
    domElement, key, value) {
  var uitagsProps = this.__getExtendedProperties(domElement);
  uitagsProps[key] = value;
};

/**
 * Returns the value of a certain extended property of a DOM element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} key the name of the property
 * @return the value
 * @type String
 * @private
 */
uiHtml_ElementWrapper.prototype.__getExtendedProperty = function(
    domElement, key) {
  var uitagsProps = this.__getExtendedProperties(domElement);
  return uitagsProps[key];
};

/**
 * Sets the property allocated for a certain class.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {Class} ownerClass the class
 * @return the child properties object
 * @type uiUtil_Object
 */
uiHtml_ElementWrapper.prototype._setClassProperty = function(
    domElement, ownerClass, value) {
  var uitagsProps = this.__getExtendedProperties(domElement);
  var className = uiUtil_Object.getClassName(ownerClass);
  uitagsProps.__classProperty[className] = value;
};

/**
 * Returns the value that is stored in an extended property reserved for
 * a certain class.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {Class} ownerClass the class
 * @return any previously stored value or <code>null</code> if the value
 *     has never been set using #_setClassProperty
 * @type Object
 */
uiHtml_ElementWrapper.prototype._getClassProperty = function(
    domElement, ownerClass) {
  var uitagsProps = this.__getExtendedProperties(domElement);
  var className = uiUtil_Object.getClassName(ownerClass);
  return uitagsProps.__classProperty[className];
};

/**
 * Returns a certain style attribute's value that a DOM element inherits
 * from its parent element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} attrName the name of the style attribute
 * @param {String} inheritValue the value that indicates that the
 *     attribute is inheritted from the parent
 * @param {String} defaultValue the value that is going to be used if
 *     none of the parents has the explicit attribute value
 * @return the inherited value
 * @type String
 * @private
 */
uiHtml_ElementWrapper.prototype.__getInherittedStyleAttribute = function(
    domElement, attrName, inheritValue, defaultValue) {
  var currentElement = domElement;
  while (currentElement != document.body.parentNode) {
    var styleValue = this.getCascadedStyleAttribute(currentElement, attrName);
    if (styleValue != inheritValue) {
      return styleValue;
    }
    currentElement = currentElement.parentNode;
  }
  return defaultValue;
};

/**
 * Sets an element's visibility status.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {boolean} showing a value that indicates whether the element
 *     should be visible or not
 * @private
 */
uiHtml_ElementWrapper.prototype.__setShowing = function(domElement, showing) {
  domElement.style.visibility = (showing) ? "visible" : "hidden";
};

/**
 * Returns an element's visibility status.
 *
 * @param {HTMLElement} domElement the DOM element
 * @return <code>true</code> if the element is visible, <code>false</code>
 *     otherwise
 * @type boolean
 */
uiHtml_ElementWrapper.prototype.isShowing = function(domElement) {
  var styleValue = this.__getInherittedStyleAttribute(
      domElement, "visibility", "inherit", "visible");
  return styleValue == "visible";
};

/**
 * Makes an element visible.
 *
 * @param {HTMLElement} domElement the DOM element
 */
uiHtml_ElementWrapper.prototype.show = function(domElement) {
  this.__setShowing(domElement, true);
};

/**
 * Makes an element invisible. Note that although the element is not
 * visible, it still occupies its original space in the browser window.
 *
 * @param {HTMLElement} domElement the DOM element
 */
uiHtml_ElementWrapper.prototype.hide = function(domElement) {
  this.__setShowing(domElement, false);
};

/**
 * Checks if an element is appearing.
 *
 * @param {HTMLElement} domElement the DOM element
 * @return <code>true</code> if the element is appearing, <code>false</code>
 *     otherwise
 * @type boolean
 */
uiHtml_ElementWrapper.prototype.isAppearing = function(domElement) {
  return this.getCascadedStyleAttribute(domElement, "display") != "none";
};

/**
 * Makes an element appear.
 *
 * @param {HTMLElement} domElement the DOM element
 */
uiHtml_ElementWrapper.prototype.appear = function(domElement) {
  if (this.isAppearing(domElement)) {
    return;
  }
  var expectedValue = this.__getExtendedProperty(domElement, "__appearDisplay");
  domElement.style.display = expectedValue;
};

/**
 * Makes an element disappear. Note that the element does not occupy any
 * space in the browser window.
 *
 * @param {HTMLElement} domElement the DOM element
 */
uiHtml_ElementWrapper.prototype.disappear = function(domElement) {
  if (!this.isAppearing(domElement)) {
    return;
  }
  var appearValue = this.getCascadedStyleAttribute(domElement, "display");
  this.__setExtendedProperty(domElement, "__appearDisplay", appearValue);
  if (appearValue == null) {
    // NOTE: In IE 6, when an element is not attached to any particular
    //       parent, its "display" property is null.
    this.__logger.warn("Display style property is null");
    this.__setExtendedProperty(domElement, "__appearDisplay", "block");
  }
  domElement.style.display = "none";
};

/**
 * Sets the z-index of an element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {int} depth the z-index value
 */
uiHtml_ElementWrapper.prototype.setDepth = function(domElement, depth) {
  if (!uiUtil_Type.isNumber(depth)) {
    throw new uiUtil_IllegalArgumentException("Not a number: " + depth);
  }
  domElement.style.zIndex = depth;
};

/**
 * Returns the z-index of an element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @return the z-index value
 */
uiHtml_ElementWrapper.prototype.getDepth = function(domElement) {
  var origValue = this.__getInherittedStyleAttribute(
      domElement, "z-index", "auto", "0");
  var value = parseInt(origValue);
  if (uiUtil_Type.isNumber(value)) {
    return value;
  }
  throw new uiUtil_IllegalStateException("Depth value is not a valid number");
};

/**
 * Sets the dimension of the element. Any <code>null</code> argument value
 * indicates the old value to be retained. <b>Note</b> that this operation
 * takes effect only when the position style attribute is <i>absolute</i>.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {int} x the x coordinate
 * @param {int} y the y coordinate
 * @param {int} width the element width
 * @param {int} height the element height
 */
uiHtml_ElementWrapper.prototype.setDimension = function(
    domElement, x, y, width, height) {
  if (x) {
    domElement.style.left = x + "px";
  }
  if (y) {
    domElement.style.top = y + "px";
  }
  if (width) {
    domElement.style.width = width + "px";
  }
  if (height) {
    domElement.style.height = height + "px";
  }
};

/**
 * Sets the dimension of the element. This is a convenience method for
 * performing exactly the same operation as {@link #setDimension}.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {uiUtil_Dimension} dimension the dimension object
 */
uiHtml_ElementWrapper.prototype.setDimensionObject = function(domElement, dimension) {
  this.setDimension(domElement,
      dimension.getLeft(), dimension.getTop(),
      dimension.getWidth(), dimension.getHeight());
};
// Style-related method section ends.
/////

/////
// Event-related method section starts.
/**
 * Adds a new handler for a certain event to the list of handlers, which
 * is maintained using an extended property.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName the name of the event
 * @param {String} extPropName name of the extended property
 * @param {function} eventHandler the event handler
 * @return the ID of the appended event handler
 * @type int
 * @private
 */
uiHtml_ElementWrapper.prototype.__addAggregateEventHandler = function(
    domElement, eventName, extPropName, eventHandler) {
  var aggregateHandler = this.__getAggregateEventHandler(
      domElement, eventName, extPropName);
  aggregateHandler.push(eventHandler);

  if (!this.__isEventPropertyInitialized(domElement, eventName)) {
    this.__initializeEventProperty(domElement, eventName);
  }

  // NOTE: This will return indices starting from 1, 2, ..., which is
  //       useful for removal of individual handler.
  return aggregateHandler.length;
};

/**
 * Checks if an extended property for a certain event has been used
 * (initialized).
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName the name of the event
 * @return <code>true</code> if it has been initialized, <code>false</code>
 *     otherwise
 * @type boolean
 * @private
 */
uiHtml_ElementWrapper.prototype.__isEventPropertyInitialized = function(
    domElement, eventName) {
  var initializedHandlers = this.__getExtendedProperty(
      domElement, "__initializedEventHandlers");
  return initializedHandlers[eventName] == true;
};

/**
 * Initializes the event handler property so that when the event is
 * triggered, the element's original event handler (if exists) and the
 * prepended/appended handlers are all executed in the correct order.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName the name of the event
 * @private
 */
uiHtml_ElementWrapper.prototype.__initializeEventProperty = function(
    domElement, eventName) {
  var wrapper = this;
  var origHandler = domElement["on" + eventName];
  domElement["on" + eventName] = function(e) {
    var backwardAggregateHandler = wrapper.__getAggregateEventHandler(
        domElement, eventName, "__backwardEventHandlers");
    for (var i = backwardAggregateHandler.length - 1; i >= 0; --i) {
	  //debug: if (uiHtml_Element.getWidgetType(domElement) == "checkbox") {alert("bwd calling handler=" + backwardAggregateHandler[i]);}
      backwardAggregateHandler[i].call(this, e);
    }

    //debug: if (uiHtml_Element.getWidgetType(domElement) == "checkbox") {alert("orig calling handler=" + origHandler);}
    var retValue = (origHandler == null) ? null : origHandler.call(this, e);

    var forwardAggregateHandler = wrapper.__getAggregateEventHandler(
        domElement, eventName, "__forwardEventHandlers");
    for (var i = 0; i < forwardAggregateHandler.length; ++i) {
	  //debug: if (uiHtml_Element.getWidgetType(domElement) == "checkbox") {alert("fwd calling handler=" + backwardAggregateHandler[i]);}
      // (ctoohey) use return value from the last handler called, because for onclick handlers (radiobuttons,checkbox)
      //  can utilize the fact that when they return false the checked state of the element is reversed back to what
      //  it was, in the case where the user cancels an action at the prompt
      retValue = forwardAggregateHandler[i].call(this, e);
    }

    //debug:if (uiHtml_Element.getWidgetType(domElement) == "checkbox") {alert("return=" + retValue);}
    return retValue;
  };

  var initializedHandlers = this.__getExtendedProperty(
      domElement, "__initializedEventHandlers");
  initializedHandlers[eventName] = true;
};

/**
 * Returns the handler list that is maintained by a certain extended property.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName the name of the event
 * @param {String} extPropName the property name
 * @return the handler array
 * @type function[]
 * @private
 */
uiHtml_ElementWrapper.prototype.__getAggregateEventHandler = function(
    domElement, eventName, extPropName) {
  var allHandlers = this.__getExtendedProperty(domElement, extPropName);
  if (allHandlers[eventName] == null) {
    allHandlers[eventName] = new Array();
  }
  return allHandlers[eventName];
};

/**
 * Prepends a function to the event handler list of a DOM element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName event name of the prepended handlers
 * @param {function} eventHandler the event handler
 * @return an ID of the event handler
 * @type int
 */
uiHtml_ElementWrapper.prototype.prependEventHandler = function(
    domElement, eventName, eventHandler) {
  // NOTE: return negative indices, i.g. -1, -2, ...
  return -1 * this.__addAggregateEventHandler(
      domElement, eventName, "__backwardEventHandlers", eventHandler);
};

/**
 * Appends a function to the event handler list of a DOM element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName event name of the appended handlers
 * @param {function} eventHandler the event handler
 * @return an ID of the event handler
 * @type int
 */
uiHtml_ElementWrapper.prototype.appendEventHandler = function(
    domElement, eventName, eventHandler) {
  return this.__addAggregateEventHandler(
      domElement, eventName, "__forwardEventHandlers", eventHandler);
};

/**
 * Removes a certain function from the event handler list of a DOM element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName event name of the removed handlers
 * @param {int} handlerId ID of the prepended/appended event handler
 */
uiHtml_ElementWrapper.prototype.removeEventHandler = function(
    domElement, eventName, handlerId) {
  // NOTE: Since indices are in the range of ..., -2, -1, 1, 2, ...,
  //       we need to adjust the index.
  var actualIndex;
  var extPropName;
  if (handlerId > 0) {
    actualIndex = handlerId - 1;
    extPropName = "__forwardEventHandlers";
  }
  else if (handlerId < 0) {
    actualIndex = handlerId + 1;
    extPropName = "__backwardEventHandlers";
  }
  else {
    throw new uiUtil_IllegalArgumentException(
        "Handler start index should not be 0");
  }

  var aggregateHandler = this.__getAggregateEventHandler(
      domElement, eventName, extPropName);
  uiUtil_ArrayUtils.removeAt(aggregateHandler, actualIndex);
};

/**
 * Removes all functions that were added to a DOM element's event handler
 * list using {@link #prependEventHandler} or {@link #appendEventHandler}.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName event name of the cleared handlers
 */
uiHtml_ElementWrapper.prototype.clearEventHandlerExtension = function(
    domElement, eventName) {
  var backwardAggregateHandler = this.__getAggregateEventHandler(
      domElement, eventName, "__backwardEventHandlers");
  uiUtil_ArrayUtils.clear(backwardAggregateHandler);

  var forwardAggregateHandler = this.__getAggregateEventHandler(
      domElement, eventName, "__forwardEventHandlers");
  uiUtil_ArrayUtils.clear(forwardAggregateHandler);
};

/**
 * Executes the aggregated handler of an event. It is recommended to
 * execute the event handler through this method instead of invoking it
 * via the event handler property directly, because it is important to
 * ensure that the domElement is the owner of the method execution
 * (thus the "this" keyword refers to the correct object -- the owner).
 * <p>
 * Note that although this the <code>optDomEvent</code> argument is
 * optional, it is recommended that a DOM event object is provided,
 * since some event handlers need the event object to perform operation.
 * It is safe to ignore the <code>optDomEvent</code> only if it is
 * known that all of the handlers do not need it.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName event name of the cleared handlers
 * @param {DOMEvent} optDomEvent the optional DOM event object
 */
uiHtml_ElementWrapper.prototype.executeAggregateEventHandler = function(
    domElement, eventName, optDomEvent) {
  if (domElement["on" + eventName] != null) {
    domElement["on" + eventName].call(domElement, optDomEvent);
  }
};

/**
 * Appends a handler for continuous mouse press on a certain element.
 * On mouse press, the handler will be executed once and then after a certain
 * amount of time will be continuously executed until the mouse is released.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {function} eventHandler the event handler
 */
uiHtml_ElementWrapper.prototype.appendMousePressHandler = function(
    domElement, eventHandler) {

  var supporters = this.__getExtendedProperty(
      domElement, "__mousePressHandlingSupporters");
  supporters.push(new uiHtml_ElementWrapper.MousePressHandlingSupporter(
      domElement, eventHandler));
};

/**
 * Makes this element draggable and performs initialization for dragging.
 *
 * @param {uiHtml_Element} domElement the element to be made draggable
 * @param {uiHtml_Element} optDomTrigger an optional drag handle,
 *     by default the draggable element itself is the handle.
 * @param {function} optSetCoordinate a strategy for updating the draggable
 *     element's position
 */
uiHtml_ElementWrapper.prototype.enableDragSupport = function(
    domElement, optTrigger, optSetCoordinate) {
  var supporter = this.__getExtendedProperty(domElement, "__dragSupporter");
  if (supporter == null) {
    var wrapper = this;
    setCoordinate = uiUtil_Type.getValue(optSetCoordinate, function(x, y) {
      wrapper.setDimension(domElement, x, y, null, null);
    });
    supporter = new uiHtml_ElementWrapper.DragSupporter(domElement, setCoordinate);
    supporter.__setDragTrigger(uiUtil_Type.getValue(optTrigger, domElement));
    this.__setExtendedProperty(domElement, "__dragSupporter", supporter)
  }
};

/**
 * Specifies a rectangular boundary that limits the positioning of a
 * dragged element.
 *
 * @param {uiHtml_Element} domElement the DOM element
 * @param {int} optLeft left coordinate of the rectangle
 * @param {int} optTop top coordinate of the rectangle
 * @param {int} optWidth width of the rectangle
 * @param {int} optHeight height of the rectangle
 */
uiHtml_ElementWrapper.prototype.restrictDragging = function(
    domElement, optLeft, optTop, optWidth, optHeight) {
  var supporter = this.__getExtendedProperty(domElement, "__dragSupporter");
  if (supporter == null) {
    throw new uiUtil_IllegalStateException(
        "Drag support has not been enabled");
  }
  supporter.__restrictDragging(optLeft, optTop, optWidth, optHeight);
};
// Event-related method section ends.
/////

/////
// Generic method section starts.
/**
 * Returns the width of an element. <b>Note</b> that overriding this
 * method will also affect the width value returned by the dimension
 * related methods (i.e. #_obtainRelativeDimension, #_obtainAbsoluteDimension,
 * #_getDimension, #getRelativeDimension, and #getAbsoluteDimension).
 *
 * @param {HTMLElement} domElement the DOM element
 */
uiHtml_ElementWrapper.prototype.getWidth = function(domElement) {
  return domElement.offsetWidth;
};

/**
 * Returns the height of an element. <b>Note</b> that overriding this
 * method will also affect the height value returned by the dimension
 * related methods (i.e. #_obtainRelativeDimension, #_obtainAbsoluteDimension,
 * #_getDimension, #getRelativeDimension, and #getAbsoluteDimension).
 *
 * @param {HTMLElement} domElement the DOM element
 */
uiHtml_ElementWrapper.prototype.getHeight = function(domElement) {
  return domElement.offsetHeight;
};

/**
 * Calculates the position of an element relative to its parent/container.
 * This method gives freedom to the caller to specify the width and height,
 * because the returned {@link uiUtil_Dimension} object will include the
 * width and height values provided as arguments of this method.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {int} width the width value to be included as part of the
 *     returned dimension object
 * @param {int} height the height value to be included as part of the
 *     returned dimension object
 * @return the dimension
 * @type uiUtil_Dimension
 */
uiHtml_ElementWrapper.prototype._obtainRelativeDimension = function(
    domElement, width, height) {
  // Return the offset dimension instead of the style's dimension
  // because the style's dimension does not necessarily reflect
  // the element's real dimension.
  // The offset dimension takes into account the space occupied by
  // paddings, margins, and scroll bars.
  // The offset dimension also varies depending on the type of the
  // element positioning, whether it is: relative, absolute, etc.
  return new uiUtil_Dimension(
      domElement.offsetLeft, domElement.offsetTop, width, height);
};

/**
 * Calculates the absolute position of an element (the offset from the
 * top-left corner of the root element (document body) in the browser).
 * This method gives freedom to the caller to specify the width and height,
 * because the returned {@link uiUtil_Dimension} object will include the
 * width and height values provided as arguments of this method.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {int} width the width value to be included as part of the
 *     returned dimension object
 * @param {int} height the height value to be included as part of the
 *     returned dimension object
 * @return the dimension
 * @type uiUtil_Dimension
 */
uiHtml_ElementWrapper.prototype._obtainAbsoluteDimension = function(
    domElement, width, height) {
  var x = 0;
  var y = 0;
  var currentElement = domElement;
  while (currentElement != null &&
      uiHtml_Element.getTagName(currentElement) != "body") {
    x += currentElement.offsetLeft;
    y += currentElement.offsetTop;
    currentElement = currentElement.offsetParent;
  }
  return new uiUtil_Dimension(x, y, width, height);
};

/**
 * Returns the dimension of an element, based on the position (left and top)
 * calculated by the <code>obtainDimension</code>, and the provided width
 * and height.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {function} obtainDimension the function that calculates the left
 *     and top pixels and returns the dimension object
 * @param {function} getWidth a function that returns the width value
 *     to be included as part of the returned dimension object
 * @param {function} getHeight a function that returns the height value
 *     to be included as part of the returned dimension object
 * @return the dimension
 * @type uiUtil_Dimension
 */
uiHtml_ElementWrapper.prototype._getDimension = function(
    domElement, obtainDimension, getWidth, getHeight) {
  if (!this.isAppearing(domElement)) {
    var showing = this.isShowing(domElement);  // keep the original state

    this.hide(domElement);
    this.appear(domElement);  // so that we can get the dimension

    var dimension = obtainDimension.call(
        this, domElement, getWidth(), getHeight());

    // set back to the original state
    this.__setShowing(domElement, showing);
    this.disappear(domElement);

    return dimension;
  }
  return obtainDimension.call(
      this, domElement, getWidth(), getHeight());
};

/**
 * Returns the position of an element relative to its parent/container.
 *
 * @param {HTMLElement} domElement the DOM element
 * @return the dimension
 * @type uiUtil_Dimension
 */
uiHtml_ElementWrapper.prototype.getRelativeDimension = function(domElement) {
  // Use the getWidth() and getHeight() of this element to allow the
  // child classes to override their implementations.
  var wrapper = this;
  return this._getDimension(domElement, this._obtainRelativeDimension,
      function() { return wrapper.getWidth(domElement); },
      function() { return wrapper.getHeight(domElement); });
};

/**
 * Returns the absolute position of an element.
 *
 * @param {HTMLElement} domElement the DOM element
 * @return the dimension
 * @type uiUtil_Dimension
 */
uiHtml_ElementWrapper.prototype.getAbsoluteDimension = function(domElement) {
  // Use the getWidth() and getHeight() of this element to allow the
  // child classes to override their implementations.
  var wrapper = this;
  return this._getDimension(domElement, this._obtainAbsoluteDimension,
      function() { return wrapper.getWidth(domElement); },
      function() { return wrapper.getHeight(domElement); });
};

/**
 * Clears all the children of an element.
 *
 * @param {HTMLElement} domElement the DOM element
 */
uiHtml_ElementWrapper.prototype.clearChildDomObjects = function(domElement) {
  while (domElement.hasChildNodes()) {
    domElement.removeChild(domElement.firstChild);
  }
};

// NOTE: Although this class represents generic HTML elements, it also
//       contain some widget specific functions for convenience (code
//       reuse) only, to avoid having too many levels in the object hierarchy.
/**
 * Returns the supplied DOM element's logical value. This method is a
 * default implementation which simply returns domElement.value.
 * <p>
 * Some elements have a concept of logical value. For example, a radio button's
 * logical value is <code>null</code> if it is not selected. Others,
 * for example a text field, don't have a logical value,
 * for which case this method's default implementation is sufficient.
 *
 * @param {HTMLElement} domElement the DOM element to operate on
 * @return the supplied DOM element's logical value
 * @type String
 */
uiHtml_ElementWrapper.prototype.getLogicalValue = function(domElement) {
  return domElement.value;
};

/**
 * Checks if a widget is disabled.
 *
 * @param {HTMLElement} domElement the DOM element
 * @return <code>true</code> if disabled, <code>false</code> otherwise
 */
uiHtml_ElementWrapper.prototype.isDisabled = function(domElement) {
  return domElement.disabled;
};

/**
 * Enables/Disables a widget.
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {boolean} value the disabled status -- <code>true</code> indicates
 *     disabled, while <code>false</code> indicates enabled
 */
uiHtml_ElementWrapper.prototype.setDisabled = function(domElement, value) {
  // ctoohey
  var obj = ACS['acs_textbox_' + domElement.id];
  if (obj != null) {
  	if (value) {
		obj.disable();  
	}
	else {
		obj.enable();
	}
  }
  else {
	domElement.disabled = value;
  }
};

// ctoohey: additional functions added

// author: ctoohey
// Skips specified widget (can be a multi- or single-sized widget), which means
// disabling the widget, and setting its value to the specified value argument.
// This method is the exact opposite of unskip().
// 
// For selectboxes, options have text and value. The value is matched against 
// the element value and/or the optionText is matched against the element
// text to determine which option to mark as selected to set the selectbox value.
// If the option does not exist, then a new option is created, where the value is 
// the value, and optionText is the text. 
// note: an exception is autocomplete selectboxes which have limitToList set, in which case
//       new option is not added if there is no match. value is just not set if there is 
//       no match. 
uiHtml_ElementWrapper.prototype.skip = function(domElement, value, optionText) {
  // determine whether the control is an autocomplete widget, in which case special handling
  // is required, because the autocomplete selectbox widget is actually hidden and has an associated
  // visible input box, so need to call a function on the autocomplete widget to disable it (and
  // set the skip value)
  var autocompleteObj = ACS['acs_textbox_' + domElement.id];
  if (autocompleteObj != null) {
    // autocomplete control has its own function to skip which disables it. it also sets the value to 
    // the argument passed to it, matching the option value/text to set the value. 
    autocompleteObj.skip(value, optionText);
  }
  else {
      //alert("uiHtml_ElementWrapper.skip name=" + widget.name + " value=" + widget.value + " type=" + uiCommon_getWidgetType(widget) + " text=" + text + " value=" + value);
	  var elementType = uiHtml_Element.getWidgetType(domElement);
	  if (elementType == 'select') {
	    var selectObj = new uiHtml_Select(domElement);
	    //alert("doing skip for domElement id=" + domElement.id + " textValue=" + textValue + " optionValue=" + optionValue);
	    selectObj.setSelectedOptionAddIfNoMatch(optionText, value);
	  }
	  else if (elementType == 'radio' || elementType == 'checkbox') {
	    // check the box/button whose value is equal to the value argument
	    if (domElement.value == value) {
	    	domElement.checked = true;
    	}
	  }
	  else {
	  	domElement.value = value;
      }
	
	  // NOTE: all elements are enabled in the form's onsubmit event handler because a
	  //       disabled element's data is not submitted to the server
	  domElement.disabled = true;

  }
}  

// author: ctoohey
// Unskips supplied obj (can be a multi- or single-sized widget), which means enabling
// the widget and setting its value to the value argument.
// This method is the exact opposite of uiCommon_skipWidgets().

// For selectboxes, options have text and value. The value argument is matched against the 
// the value and/or the optionText arg is matched against the text to determine which option 
// to mark as selected to set the selectbox value.
// If the option does not exist, then a new option is created, where the value arg is 
// the value, and optionText arg is the text. 
// note: an exception is autocomplete selectboxes which have limitToList set, in which case
//       new option is not added if there is no match. value is just not set if there is 
//       no match. 
uiHtml_ElementWrapper.prototype.unskip = function(domElement, value, optionText) {
  // determine whether the control is an autocomplete widget, in which case special handling
  // is required, because the autocomplete selectbox widget is actually hidden and has an associated
  // visible input box, so need to call a function on the autocomplete widget to enable it (and
  // set the unskip value)
  var autocompleteObj = ACS['acs_textbox_' + domElement.id];
  if (autocompleteObj != null) {
    // autocomplete control has its own function to unskip which enables it and sets its value.
    autocompleteObj.unskip(value, optionText);  
  }
  else {
      // only set an unskip value if the current value is skip
      if (domElement.value == "-6" || domElement.value == "-6.0" || domElement.value == "Logical Skip") {
        //alert("unskipSingleWidget name=" + widget.name + " value=" + widget.value + " type=" + uiCommon_getWidgetType(widget) + " text=" + text + " value=" + value);
        //note: originally, only set unskip value if current widget value was Logical Skip (-6), but per
        //      the definition of the unskip tag, always set the unskip value. if want to enable a tag
        //      but leave its value as it, should be using the enable tag
	    var elementType = uiHtml_Element.getWidgetType(domElement);
	    if (elementType == 'select') {
	      var selectObj = new uiHtml_Select(domElement);
	      selectObj.setSelectedOptionAddIfNoMatch(optionText, value);
	    }
	    else if (elementType == 'radio' || elementType == 'checkbox') {
	      // check the box/button whose value is equal to the value argument
	      if (domElement.value == value) {
	   		domElement.checked = true;
	   	  }
	    }
	    else {
	  	  domElement.value = value;
	    }
	  }
	
	  domElement.disabled = false;
	  
  }	 
}

/**
 * author:ctoohey
 *
 * Clone the options in srcElement to targetElement (after clearing
 * the targetWidget), but retains selected option of targetWidget
 * note: currently only implemented for select box widgets
 * note: assumption is that the targetWidget options are a subset
 *       of the srcWidget options
 *
 */
uiHtml_ElementWrapper.prototype.cloneOptions = function(srcElementId, srcElementName, targetElementId, targetElementName) {
  // determine whether the selectboxes are autocomplete widgets, in which case we need
  // to use the autocomplete select boxes
  var srcSelectObj;
  var srcAutocompleteObj = ACS['acs_textbox_' + srcElementId];
  if (srcAutocompleteObj != null ) {
      srcSelectObj = new uiHtml_Select(srcAutocompleteObj.select);
  }
  else {
      srcSelectObj = uiHtml_Group.createByEither(srcElementId, srcElementName);
  }
/*
alert("src contents:");
for (var i = 0; i < srcSelectObj._getItems().length; ++i) {
  alert("src item=" + i + " text=" + srcSelectObj.getItemAt(i).text + " value=" + srcSelectObj.getItemAt(i).value);
}
*/

  var targetSelectObj;
  var targetAutocompleteObj = ACS['acs_textbox_' + targetElementId];
  if (targetAutocompleteObj != null ) {
    targetSelectObj = new uiHtml_Select(targetAutocompleteObj.select);
  }
  else {
    targetSelectObj = uiHtml_Group.createByEither(targetElementId, targetElementName);
  }
/*
alert("target contents:");
for (var i = 0; i < targetSelectObj._getItems().length; ++i) {
  alert("target item=" + i + " text=" + targetSelectObj.getItemAt(i).text + " value=" + targetSelectObj.getItemAt(i).value);
}
*/

  // save target's current selection (can not just save the selectedIndex
  //  because after copy, target's selection may not have same index because the 
  //  target may have had fewer options than the src if some of its options were 
  //  removed by a prior removeOption action)
  var targetSelectedOptionValue = targetSelectObj.getValues()[0];
    
  targetSelectObj.clearItems();
  targetSelectObj.addItems(srcSelectObj._getItems());
/*
alert("after cloning:");
for (var i = 0; i < targetSelectObj._getItems().length; ++i) {
  alert("target item=" + i + " text=" + targetSelectObj.getItemAt(i).text + " value=" + targetSelectObj.getItemAt(i).value);
}
*/

  // restore target's currently selected value
  // note: it may be possible that the currently selected value does not exist if the src select
  //       which was cloned did not contain it, in which case nothing is selected after the cloning
  targetSelectObj.setSelectedOptionByValue(targetSelectedOptionValue);
} 

/**
 * author:ctoohey
 *
 * Removes the option selected by the srcWidget from targetWidget
 * note: current not implemented for multiple select box widgets
 * note: currently only implemented for select box widgets
 * assumption: the target element selectbox has a blank item
 */
uiHtml_ElementWrapper.prototype.removeOption = function(srcElementId, srcElementName, targetElementId, targetElementName) {
  // determine whether the selectboxes are autocomplete widgets, in which case we need
  // to use the autocomplete select boxes
  var srcSelectObj;
  var srcAutocompleteObj = ACS['acs_textbox_' + srcElementId];
  if (srcAutocompleteObj != null ) {
      srcSelectObj = new uiHtml_Select(srcAutocompleteObj.select);
  }
  else {
      srcSelectObj = uiHtml_Group.createByEither(srcElementId, srcElementName);
  }

  var targetSelectObj;
  var targetAutocompleteObj = ACS['acs_textbox_' + targetElementId];
  if (targetAutocompleteObj != null ) {
    targetSelectObj = new uiHtml_Select(targetAutocompleteObj.select);
  }
  else {
    targetSelectObj = uiHtml_Group.createByEither(targetElementId, targetElementName);
  }

  // get the selected option in the src whose matching option (matching text and value) in target 
  // should be removed 
  var srcSelectedOption = srcSelectObj.getSelectedOption(); // returns null if nothing selected
  // special case: do not remove the blank option
  if (srcSelectedOption != null) {
    if (srcSelectedOption.value == '') {
      return;
    }

    // record the currently selected target value. if it is the option that gets removed,
    // set the target selected option to the blank option 
    var targetSelectedOption = targetSelectObj.getSelectedOption();
    var targetSelectedValue = targetSelectedOption.value;

    if (targetSelectObj.removeItem(srcSelectedOption)) {
      // if the selected option was removed, set the target's selected option to the blank option
      if (targetSelectedValue == srcSelectedOption.value) {
        if (targetAutocompleteObj != null) {
          // if autocomplete, change the textbox to blank
          targetAutocompleteObj.textbox.value = '';
          targetAutocompleteObj.selectItemByTextEntry(false);
        }
        else {
          targetSelectObj.setSelectedOptionByValue('');
        }
      }
    }
  }
}


// author: ctoohey
// Sets the specified value for the specified widget. 
// 
// For selectboxes, options have text and value. The value is matched against 
// the value and/or optionText is matched against the text to determine which option to 
// mark as selected to set the selectbox value.
// If the option does not exist, then a new option is created, where the value is 
// the value, and optionText is the text. 
// note: an exception is autocomplete selectboxes which have limitToList set, in which case
//       new option is not added if there is no match. value is just not set if there is 
//       no match. 
uiHtml_ElementWrapper.prototype.setValue = function(domElement, value, optionText) {
  // determine whether the control is an autocomplete widget, in which case special handling
  // is required, because the autocomplete selectbox widget is actually hidden and has an associated
  // visible input box, so need to call a function on the autocomplete widget to set a value.
  var autoCompleteObj = ACS['acs_textbox_' + domElement.id];
  if (autoCompleteObj != null) {
      // first try to set by matching the value to the options values. if their is no match, the function
      // does not add the option to the list or issue an alert to the user. matching on value is purely for
      // use by javascript functions that are setting a known value in the control, e.g. a prior diagnosis
      // value or a neuroexam normal value. therefore, the functionality of adding the option and alerting the
      // user is not necessary. that functionality is in setValue, which is tailored towards the user trying 
      // to select a value via keyboard input.
      if (!autoCompleteObj.setValueByValue(value)) {
		// try to match optionText to the options text
	      autoCompleteObj.setValue(optionText);
	}
  }
  else {
	  var elementType = uiHtml_Element.getWidgetType(domElement);
	  if (elementType == 'select') {
	    var selectObj = new uiHtml_Select(domElement);
	    selectObj.setSelectedOptionAddIfNoMatch(optionText, value);
	  }
	  else if (elementType == 'radio' || elementType == 'checkbox') {
	    // check the box/button whose value is equal to the value argument
	    if (domElement.value == value) {
	    	domElement.checked = true;
    	}
	  }
	  else if (elementType == 'textarea' || elementType == 'text' || elementType == 'submit' || elementType == 'button') {
	  	domElement.value = value;
      }
  }
}  
  

  
// Generic method section ends.
/////


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * Returns a singleton instance of the class. Although this class is not
 * a pure singleton, the use of this method is for efficiency reason.
 *
 * @return the instance
 * @type uiHtml_ElementWrapper
 */
uiHtml_ElementWrapper.getInstance = function() {
  if (uiHtml_ElementWrapper.__instance == null) {
    uiHtml_ElementWrapper.__instance = new uiHtml_ElementWrapper();
  }
  return uiHtml_ElementWrapper.__instance;
};

/**
 * Appends an element to the list of elements that will be cleaned up when
 * {@link uiHtml_ElementWrapper._finalizeElements} is invoked.
 *
 * @param {HTMLElement} domElement the DOM element
 * @private
 */
uiHtml_ElementWrapper.__registerElementToFinalize = function(domElement) {
  if (uiHtml_Window.getInstance().isIe()) {
    uiHtml_ElementWrapper.__extendedElements.push(domElement);
  }
};

/**
 * Performs necessary clean-ups on previously modified elements (by the
 * means of extended properties). The main purpose of this operation is
 * to avoid memory leaks in IE6 by removing any possible circular
 * references.
 */
uiHtml_ElementWrapper._finalizeElements = function() {
  if (uiHtml_Window.getInstance().isIe()) {
    var wrapper = uiHtml_ElementWrapper.getInstance();
    for (var i = 0; i < uiHtml_ElementWrapper.__extendedElements.length; ++i) {
      var domElement = uiHtml_ElementWrapper.__extendedElements[i];
      var initializedHandlers = wrapper.__getExtendedProperty(
        domElement, "__initializedEventHandlers");
      for (var eventName in initializedHandlers) {
        domElement["on" + eventName] = null;
      }
      domElement.uitagsProps = null;
    }
  }
};


///////////////////////////////////
////////// Inner Classes //////////
///////////////////////////////////

/**
 * Default constructor.
 *
 * @class Strictly speaking, this class is not really needed as it does
 * not provide any more implementation than the Javascript Object class.
 * This class is needed for checking whether the extended properties
 * object of an element is a type of this class. If it is not, then there
 * is an interference by external libraries, and thus the developer can
 * be notified of this rather than just letting the naming conflict goes
 * unnoticed.
 * @extends uiUtil_Object
 */
function uiHtml_ElementWrapper$DomExtProps() {
  this._super();

  /**
   * The display value to be used when an element appears. Use "inline" as
   * the default value, since apparently there are  more inline than block
   * elements.
   *
   * @see #appear
   * @type String;
   * @private
   */
  this.__appearDisplay = "inline";
  /**
   * A hash keyed on event names to keep track of event handlers that
   * have been initialized (via the prepend/append event handler methods).
   *
   * @see #prependEventHandler
   * @see #appendEventHandler
   * @type hash&lt;boolean&gt;
   * @private
   */
  this.__initializedEventHandlers = new Object();
  /**
   * A hash keyed on event names to maintain a list of prepended
   * event handlers.
   *
   * @see #prependEventHandler
   * @type hash&lt;Array&gt;
   * @private
   */
  this.__backwardEventHandlers = new Object();
  /**
   * A hash keyed on event names to maintain a list of appended
   * event handlers.
   *
   * @see #appendEventHandler
   * @type hash&lt;Array&gt;
   * @private
   */
  this.__forwardEventHandlers = new Object();
  /**
   * A wrapper for attributes necessary for dragging. This reference will
   * only be initialized (at runtime) when the drag support for the element
   * is enabled.
   *
   * @type uiHtml_ElementWrapper$DragSupporter
   * @private
   */
  this.__dragSupporter = null;
  /**
   * A property that is reserved for storing the extended properties of
   * {@link uiHtml_ElementWrapper}'s child classes.
   *
   * @type hash&lt;uiUtil_Object&gt;
   * @private
   */
  this.__classProperty = new Object();

  /**
   * An array of mouse press event handlers.
   *
   * @type Array
   * @private
   */
  this.__mousePressHandlingSupporters = new Array();
}

/** @ignore */
uiHtml_ElementWrapper.DomExtProps = uiUtil_Object.declareClass(
    uiHtml_ElementWrapper$DomExtProps, uiUtil_Object);



/**
 * Default constructor.
 *
 * @class A class that adds a handler for continuous mouse press on a certain
 * element. On mouse press, the handler will be executed once and then after
 * a certain amount of time will be continuously executed until the mouse is
 * released.
 * @extends uiUtil_Object
 *
 * @param {HTMLElement} domElement the DOM element
 * @param {function} eventHandler the event handler
 */
function uiHtml_ElementWrapper$MousePressHandlingSupporter(domElement, eventHandler) {
  this.__intervalTimerId = null;
  this.__initialTimerId = null;

  var supporter = this;
  var wrapper = uiHtml_ElementWrapper.getInstance();
  wrapper.appendEventHandler(domElement, "mouseout", function(e) {
    supporter.__cancelTimer();
  });
  wrapper.appendEventHandler(domElement, "mouseup", function(e) {
    supporter.__cancelTimer();
  });

  wrapper.appendEventHandler(domElement, "mousedown", function(e) {
    eventHandler.call(this);

    supporter.__initialTimerId = window.setTimeout(function(e) {
      supporter.__intervalTimerId = window.setInterval(function(e) {
        eventHandler.call(this);
      }, 50);
    }, 200);
  });
}

/** @ignore */
uiHtml_ElementWrapper.MousePressHandlingSupporter = uiUtil_Object.declareClass(
    uiHtml_ElementWrapper$MousePressHandlingSupporter, uiUtil_Object);

/**
 * Cancels all the timers that were setup for handling continuous mouse press.
 *
 * @ignore
 */
uiHtml_ElementWrapper.MousePressHandlingSupporter.prototype.__cancelTimer = function() {
  window.clearInterval(this.__intervalTimerId);
  window.clearTimeout(this.__initialTimerId);
};



/**
 * @class A supporter that provides drag functionality for an element.
 * @extends uiUtil_Object
 * @param {HTMLElement} domElement the element
 */
function uiHtml_ElementWrapper$DragSupporter(domElement, setCoordinate) {
  /**
   * A reference to all elements that are temporarily disabled (thus
   * unselectable). This is especially important when dragging an element
   * in IE since normally this would make any text in the element selected.
   *
   * @type HTMLElement[]
   * @private
   */
  this.__ieUnselectableDomElements = null;
  /**
   * The ID of the appended mouse up handler when dragging is initiated.
   *
   * @type int
   * @private
   */
  this.__mouseUpHandlerIndex = 0;
  /**
   * The ID of the appended mouse move handler when dragging is initiated.
   *
   * @type int
   * @private
   */
  this.__mouseMoveHandlerIndex = 0;
  /**
   * The draggable element.
   *
   * @type HTMLElement
   * @private
   */
  this.__domElement = domElement;
  /**
   * The element that triggers dragging. If not specified, dragging is
   * triggered by the draggable element itself.
   *
   * @type HTMLElement
   * @private
   */
  this.__trigger = null;
  /**
   * The distance of the new mouse X position compared to its old position.
   *
   * @type int
   * @private
   */
  this.__leftDistance = -1;
  /**
   * The distance of the new mouse Y position compared to its old position.
   *
   * @type int
   * @private
   */
  this.__topDistance = -1;
  /**
   * A strategy function for changing the element's position during dragging.
   * This way the {@link uiHtml_ElementWrapper} can be used as a delegate
   * by another class and allowing its <i>setCoordinate</i> method to still be
   * used polymorphically in the child classes, since all the super class
   * needs to do is to pass the polymorphic method as a strategy.
   *
   * @type function
   * @private
   */
  this.__setCoordinate = setCoordinate;

  // The following are used only when the drag boundary restriction is
  // enabled.
  /**
   * Indication whether the dragging area is restricted by a specified
   * rectangle.
   *
   * @type boolean
   * @private
   */
  this.__restrict = false;
  /**
   * Left boundary of restricted dragging area.
   *
   * @type int
   * @private
   */
  this.__borderLeft = -1;
  /**
   * Top boundary of restricted dragging area.
   *
   * @type int
   * @private
   */
  this.__borderTop = -1;
  /**
   * Right boundary of restricted dragging area.
   *
   * @type int
   * @private
   */
  this.__borderRight = -1;
  /**
   * Bottom boundary of restricted dragging area.
   *
   * @type int
   * @private
   */
  this.__borderBottom = -1;
}

/** @ignore */
uiHtml_ElementWrapper.DragSupporter = uiUtil_Object.declareClass(
    uiHtml_ElementWrapper$DragSupporter, uiUtil_Object);

/**
 * Sets the trigger for the dragging.
 *
 * @ignore
 */
uiHtml_ElementWrapper.DragSupporter.prototype.__setDragTrigger = function(trigger) {
  this.__trigger = trigger;

  var dragger = this;
  this.__trigger.appendEventHandler("mousedown", function(e) {
    dragger.__startDrag(e);
  });

  this.__trigger.setStyleAttribute("cursor", "move");
};

/**
 * Initializes the drag boundary. An unspecified or a <code>null</code>
 * value indicates that the corresponding window property will be used.
 *
 * @ignore
 */
uiHtml_ElementWrapper.DragSupporter.prototype.__initDragBoundary = function(
    maxWidth, maxHeight) {
  var uiWindow = uiHtml_Window.getInstance();
  this.__dragMaxWidth = (maxWidth) ? maxWidth : uiWindow.getWidth();
  this.__dragMaxHeight = (maxHeight) ? maxHeight : uiWindow.getHeight();
};

/**
 * Enables the drag boundary restriction. An unspecified or a <code>null</code>
 * value indicates that the corresponding window property will be used.
 *
 * @ignore
 */
uiHtml_ElementWrapper.DragSupporter.prototype.__restrictDragging = function(
    optLeft, optTop, optMaxWidth, optMaxHeight) {
  this.__restrict = true;

  this.__borderLeft = (optLeft) ? optLeft : 0;
  this.__borderTop = (optTop) ? optTop : 0;

  this.__initDragBoundary(optMaxWidth, optMaxHeight);

  var element = this;
  uiHtml_Window.getInstance().appendEventHandler("resize", function(e) {
    element.__initDragBoundary(optMaxWidth, optMaxHeight);
  });
}

/**
 * Performs necessary initializations to start dragging. This method is
 * invoked when a drag request is triggered.
 *
 * @ignore
 */
uiHtml_ElementWrapper.DragSupporter.prototype.__startDrag = function(e) {
  this.__logger.debug("Start drag");

  var uiDoc = uiHtml_Document.getInstance();
  // In case for some reason, the old event handlers haven't been cleared,
  // which only happens when there is a bug, therefore the following are
  // only for safeguards.
  if (this.__mouseMoveHandlerIndex != 0) {
    uiDoc.removeEventHandler("mousemove", this.__mouseMoveHandlerIndex);
  }
  if (this.__mouseUpHandlerIndex != 0) {
    uiDoc.removeEventHandler("mouseup", this.__mouseUpHandlerIndex);
  }

  var dragger = this;
  this.__mouseMoveHandlerIndex = uiDoc.appendEventHandler("mousemove", function(e) {
    dragger.__drag(e);
  });
  this.__mouseUpHandlerIndex = uiDoc.appendEventHandler("mouseup", function(e) {
    dragger.__endDrag(e);
  });

  var event = new uiHtml_Event(e);
  var position = event.getViewPortPosition();
  // Will only work if the element does not get resized during the dragging.
  var dimension = uiHtml_ElementWrapper.getInstance().
      getRelativeDimension(this.__domElement);
  this.__leftDistance = position.getLeft() - dimension.getLeft();
  this.__topDistance = position.getTop() - dimension.getTop();

  if (this.__restrict) {
    this.__borderRight = this.__dragMaxWidth - dimension.getWidth();
    this.__borderBottom = this.__dragMaxHeight - dimension.getHeight();
  }

  // NOTE: In IE 6, preventing the default action does not avoid the
  //       element's content from being highlighted.
  if (uiHtml_Window.getInstance().isIe()) {
    this.__ieMakeUnselectableRecursively(this.__domElement);
  }
  else {
    event.preventDefault();
  }
};

/**
 * Makes the dragged element and all its descendants unselectable. This
 * operation is essential in IE 6, because dragging an element in IE6
 * will automatically causes any text in the element to be selected or
 * highlighted, which is not the desired behaviour
 *
 * @ignore
 */
uiHtml_ElementWrapper.DragSupporter.prototype
    .__ieMakeUnselectable = function(domElement) {
  if (domElement.unselectable != "on") {
    domElement.unselectable = "on";
    // Use this to help cancelling the "unselectable" status at later time,
    // because some elements might have been already unselectable, in which
    // case we will not need to make them selectable.
    this.__ieUnselectableDomElements.push(domElement);
  }
};

/**
 * Recursively traverses all of the descendants of an element to disable
 * their selectability.
 *
 * @ignore
 */
uiHtml_ElementWrapper.DragSupporter.prototype
    .__ieMakeUnselectableRecursively = function(domElement) {
  this.__logger.debug("Make unselectable");

  // Sometimes the array is already initialized. This could be caused
  // by multiple mousedown events that are detected consecutively, which
  // could happen if the mouse is released outside the browser document.
  if (this.__ieUnselectableDomElements == null) {
    this.__ieUnselectableDomElements = new Array();
  }
  this.__ieMakeUnselectable(domElement);

  var childNodes = domElement.childNodes;
  for (var i = 0; i < childNodes.length; ++i) {
    var node = childNodes.item(i);
    // The text itself cannot be made unselectable.
    if (!uiUtil_Type.isTextNode(node)) {
      this.__ieMakeUnselectableRecursively(node);
    }
  }
};

/**
 * Switch back the dragged element and its descendants to selectable.
 *
 * @ignore
 */
uiHtml_ElementWrapper.DragSupporter.prototype.__ieCancelUnselectable = function() {
  if (this.__ieUnselectableDomElements != null) {
    this.__logger.debug("cancel unselectable");

    for (var i = 0; i < this.__ieUnselectableDomElements.length; ++i) {
      this.__ieUnselectableDomElements[i].unselectable = "off";
    }
    this.__ieUnselectableDomElements = null;
  }
};

/**
 * Calculates and moves to the element's new position as a response
 * to a drag event.
 *
 * @ignore
 */
uiHtml_ElementWrapper.DragSupporter.prototype.__drag = function(e) {
  // NOTE: Use the event's static method to avoid creating an event wrapper,
  //       since this method gets called many times when the element is
  //       dragged.
  var position = uiHtml_Event.getViewPortPosition(e);
  var x = position.getLeft() - this.__leftDistance;
  var y = position.getTop() - this.__topDistance;

  this.__logger.debug(position.getLeft() + ", " +
      position.getTop() + " => " + x + ", " + y);

  if (this.__restrict) {
    if (x < this.__borderLeft) {
      x = this.__borderLeft;
    }
    else if (x > this.__borderRight) {
      x = this.__borderRight;
    }

    if (y < this.__borderTop) {
      y = this.__borderTop;
    }
    else if (y > this.__borderBottom) {
      y = this.__borderBottom;
    }
  }

  this.__setCoordinate(x, y);
};

/**
 * Signifies the end of the dragging.
 *
 * @ignore
 */
uiHtml_ElementWrapper.DragSupporter.prototype.__endDrag = function(e) {
  this.__logger.debug("End drag");

  uiHtml_Document.getInstance().removeEventHandler(
      "mousemove", this.__mouseMoveHandlerIndex);
  uiHtml_Document.getInstance().removeEventHandler(
      "mouseup", this.__mouseUpHandlerIndex);
  this.__mouseMoveHandlerIndex = 0;
  this.__mouseUpHandlerIndex = 0;

  if (uiHtml_Window.getInstance().isIe()) {
    this.__ieCancelUnselectable();  // change back to normal
  }
};
