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
 * Creates a wrapper for a DOM element.
 *
 * @class A stateful generic wrapper for DOM elements. Different DOM
 *     elements need different instances of this class, since it
 *     internally maintains the state relevant only to the wrapped element.
 * @extends uiUtil_Object
 */
function uiHtml_Element(domElement, inline) {
  this._super();

  /**
   * The wrapped DOM element.
   *
   * @type HTMLElement
   * @private
   */
  this.__domElement = domElement;
  /**
   * A delegate object for most of the methods.
   *
   * @type uiHtml_ElementWrapper
   * @private
   */
  this.__supporter = uiHtml_ElementWrapper.getInstance();
}

uiHtml_Element = uiUtil_Object.declareClass(uiHtml_Element, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/////
// Style-related method section starts.
/**
 * Sets the value of a certain inline style attribute of an element.
 *
 * @see uiHtml_ElementWrapper#setStyleAttribute
 * @param {String} name the attribute name
 * @param {String} value the attribute value
 */
uiHtml_Element.prototype.setStyleAttribute = function(name, value) {
  this.__supporter.setStyleAttribute(this.__domElement, name, value);
};

/**
 * Returns a cascaded style attribute value of an element as specified by
 * global style sheets, inline styles, and HTML attributes.
 *
 * @see uiHtml_ElementWrapper#getCascadedStyleAttribute
 * @param {String} name the attribute name
 * @return the attribute value
 * @type String
 */
uiHtml_Element.prototype.getCascadedStyleAttribute = function(name) {
  return this.__supporter.getCascadedStyleAttribute(this.__domElement, name);
};

/**
 * Returns an element's visibility status.
 *
 * @see uiHtml_ElementWrapper#isShowing
 * @return <code>true</code> if the element is visible, <code>false</code>
 *     otherwise
 * @type boolean
 */
uiHtml_Element.prototype.isShowing = function() {
  return this.__supporter.isShowing(this.__domElement);
};

/**
 * Makes an element visible.
 *
 * @see uiHtml_ElementWrapper#show
 */
uiHtml_Element.prototype.show = function() {
  this.__supporter.show(this.__domElement);
};

/**
 * Makes an element invisible. Note that although the element is not
 * visible, it still occupies its original space in the browser window.
 *
 * @see uiHtml_ElementWrapper#hide
 */
uiHtml_Element.prototype.hide = function() {
  this.__supporter.hide(this.__domElement);
};

/**
 * Checks if an element is appearing.
 *
 * @see uiHtml_ElementWrapper#isAppearing
 * @return <code>true</code> if the element is appearing, <code>false</code>
 *     otherwise
 * @type boolean
 */
uiHtml_Element.prototype.isAppearing = function() {
  return this.__supporter.isAppearing(this.__domElement);
};

/**
 * Makes an element appear.
 *
 * @see uiHtml_ElementWrapper#appear
 */
uiHtml_Element.prototype.appear = function() {
  this.__supporter.appear(this.__domElement);
};

/**
 * Makes an element disappear. Note that the element does not occupy any
 * space in the browser window.
 *
 * @see uiHtml_ElementWrapper#disappear
 */
uiHtml_Element.prototype.disappear = function() {
  this.__supporter.disappear(this.__domElement);
};

/**
 * Sets the z-index of an element.
 *
 * @see uiHtml_ElementWrapper#setDepth
 * @param {int} depth the z-index value
 */
uiHtml_Element.prototype.setDepth = function(depth) {
  this.__supporter.setDepth(this.__domElement, depth);
};

/**
 * Returns the z-index of an element.
 *
 * @see uiHtml_ElementWrapper#getDepth
 * @return the z-index value
 */
uiHtml_Element.prototype.getDepth = function() {
  return this.__supporter.getDepth(this.__domElement);
};

/**
 * Sets the dimension of the element. Any <code>null</code> argument value
 * indicates the old value to be retained. <b>Note</b> that this operation
 * takes effect only when the position style attribute is <i>absolute</i>.
 *
 * @see uiHtml_ElementWrapper#setDimension
 * @param {int} x the x coordinate
 * @param {int} y the y coordinate
 * @param {int} width the element width
 * @param {int} height the element height
 */
uiHtml_Element.prototype.setDimension = function(x, y, width, height) {
  this.__supporter.setDimension(this.__domElement, x, y, width, height);
};

/**
 * Sets the dimension of the element. This is a convenience method for
 * performing exactly the same operation as {@link #setDimension}.
 *
 * @see uiHtml_ElementWrapper#setDimensionObject
 * @param {uiUtil_Dimension} dimension the dimension object
 */
uiHtml_Element.prototype.setDimensionObject = function(dimension) {
  this.__supporter.setDimensionObject(this.__domElement, dimension);
};
// Style-related method section ends.
/////

/////
// Event-related method section starts.
/**
 * Prepends a function to the event handler list of the element.
 *
 * @see uiHtml_ElementWrapper#prependEventHandler
 * @param {String} eventName name of the event
 * @param {function} eventHandler the event handler
 * @return an ID of the event handler
 * @type int
 */
uiHtml_Element.prototype.prependEventHandler = function(eventName, eventHandler) {
  return this.__supporter.prependEventHandler(
      this.__domElement, eventName, eventHandler);
};

/**
 * Appends a function to the event handler list of the element.
 *
 * @see uiHtml_ElementWrapper#appendEventHandler
 * @param {String} eventName name of the event
 * @param {function} eventHandler the event handler
 * @return an ID of the event handler
 * @type int
 */
uiHtml_Element.prototype.appendEventHandler = function(eventName, eventHandler) {
  return this.__supporter.appendEventHandler(
      this.__domElement, eventName, eventHandler);
};

/**
 * Removes a certain function from the event handler list of a DOM element.
 *
 * @see uiHtml_ElementWrapper#removeEventHandler
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName event name of the removed handlers
 * @param {int} handlerId ID of the prepended/appended event handler
 */
uiHtml_Element.prototype.removeEventHandler = function(
    eventName, startIndex, optCount) {
  this.__supporter.removeEventHandler(
      this.__domElement, eventName, startIndex, optCount);
};

/**
 * Removes all functions that were added to a DOM element's event handler
 * list using {@link #prependEventHandler} or {@link #appendEventHandler}.
 *
 * @see uiHtml_ElementWrapper#clearEventHandlerExtension
 * @param {HTMLElement} domElement the DOM element
 * @param {String} eventName event name of the cleared handlers
 */
uiHtml_Element.prototype.clearEventHandlerExtension = function(eventName) {
  this.__supporter.clearEventHandlerExtension(this.__domElement, eventName);
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
 * @see uiHtml_ElementWrapper#executeAggregateEventHandler
 * @param {String} eventName event name of the cleared handlers
 * @param {DOMEvent} optDomEvent the optional DOM event object
 */
uiHtml_Element.prototype.executeAggregateEventHandler = function(
    eventName, optDomEvent) {
  this.__supporter.executeAggregateEventHandler(
      this.__domElement, eventName, optDomEvent);
};

/**
 * Appends a handler for continuous mouse press on a certain element.
 * On mouse press, the handler will be executed once and then after a certain
 * amount of time will be continuously executed until the mouse is released.
 *
 * @see uiHtml_ElementWrapper#appendMousePressHandler
 * @param {function} eventHandler the event handler
 */
uiHtml_Element.prototype.appendMousePressHandler = function(eventHandler) {
  this.__supporter.appendMousePressHandler(this.__domElement, eventHandler);
};

/**
 * Makes this element draggable and performs initialization for dragging.
 *
 * @see uiHtml_ElementWrapper#enableDragSupport
 * @param {uiHtml_Element} optTrigger an optional drag handle,
 *     by default this element is the handle.
 */
uiHtml_Element.prototype.enableDragSupport = function(optTrigger) {
  var element = this;
  this.__supporter.enableDragSupport(this.__domElement, optTrigger, function(x, y) {
    element.setDimension(x, y, null, null);
  });
};

/**
 * Specifies a rectangular boundary that limits the positioning of a
 * dragged element.
 *
 * @see uiHtml_ElementWrapper#restrictDragging
 * @param {int} optLeft left coordinate of the rectangle
 * @param {int} optTop top coordinate of the rectangle
 * @param {int} optWidth width of the rectangle
 * @param {int} optHeight height of the rectangle
 */
uiHtml_Element.prototype.restrictDragging = function(
    optLeft, optTop, optMaxWidth, optMaxHeight) {
  this.__supporter.restrictDragging(
      this.__domElement, optLeft, optTop, optMaxWidth, optMaxHeight);
};
// Event-related method section ends.
/////

/////
// Generic method section starts.
/**
 * Returns the width of the element. <b>Note</b> that overriding this
 * method will also affect the width value returned by the dimension
 * related methods (i.e. #_obtainRelativeDimension, #_obtainAbsoluteDimension,
 * #_getDimension, #getRelativeDimension, and #getAbsoluteDimension).
 *
 * @see uiHtml_ElementWrapper#getWidth
 */
uiHtml_Element.prototype.getWidth = function() {
  return this.__supporter.getWidth(this.__domElement);
};

/**
 * Returns the height of the element. <b>Note</b> that overriding this
 * method will also affect the height value returned by the dimension
 * related methods (i.e. #_obtainRelativeDimension, #_obtainAbsoluteDimension,
 * #_getDimension, #getRelativeDimension, and #getAbsoluteDimension).
 *
 * @see uiHtml_ElementWrapper#getHeight
 */
uiHtml_Element.prototype.getHeight = function() {
  return this.__supporter.getHeight(this.__domElement);
};

/**
 * Returns the position of an element relative to its parent/container.
 *
 * @see uiHtml_ElementWrapper#getRelativeDimension
 * @return the dimension
 * @type uiUtil_Dimension
 */
uiHtml_Element.prototype.getRelativeDimension = function() {
  // Use the getWidth() and getHeight() of this element to allow the
  // child classes to override their implementations.
  var element = this;
  return this.__supporter._getDimension(this.__domElement,
      this.__supporter._obtainRelativeDimension,
      function() { return element.getWidth(); },
      function() { return element.getHeight(); });
};

/**
 * Returns the absolute position of an element.
 *
 * @see uiHtml_ElementWrapper#getAbsoluteDimension
 * @return the dimension
 * @type uiUtil_Dimension
 */
uiHtml_Element.prototype.getAbsoluteDimension = function() {
  // Use the getWidth() and getHeight() of this element to allow the
  // child classes to override their implementations.
  var element = this;
  return this.__supporter._getDimension(this.__domElement,
      this.__supporter._obtainAbsoluteDimension,
      function() { return element.getWidth(); },
      function() { return element.getHeight(); });
};

/**
 * Enables/Disables a widget.
 *
 * @see uiHtml_ElementWrapper#setDisabled
 * @param {boolean} value the disabled status -- <code>true</code> indicates
 *     disabled, while <code>false</code> indicates enabled
 */
uiHtml_Element.prototype.setDisabled = function(value) {
  this.__supporter.setDisabled(this.__domElement, value);
};

/**
 * Returns the wrapped DOM element.
 *
 * @return the DOM object
 * @type HTMLElement
 */
uiHtml_Element.prototype.getDomObject = function() {
  return this.__domElement;
};
// Generic method section ends.
/////


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * Creates a wrapper for an element if it has not already been previously
 * created and thus cached, in which case the cached version will be returned.
 *
 * @param {String} id ID of the element
 * @param {String} name name of the element
 * @param {Class} optTypeClass an optional indication of the wrapper type
 *     of the element, if not specified, the most suitable wrapper for the
 *     element will be used
 * @return the wrapper
 * @type uiHtml_Element
 */
uiHtml_Element.createByEither = function(id, name, optTypeClass) {
  var uiDocument = uiHtml_Document.getInstance();
  var domObject = null;
  try {
    domObject = uiDocument.getDomObjectById(id, true);
  }
  catch (e) {
    if (name == null) {
      throw new uiUtil_IllegalArgumentException("Invalid element ID: " + id);
    }
    domObject = uiDocument.getDomObjectsByName(name, true)[0];
  }

  var extProps = uiHtml_Element.__getExtProps(domObject);
  if (extProps.__elementWrapper == null) {
    extProps.__elementWrapper = uiHtml_Element.__createElement(
          domObject, optTypeClass);
    uiHtml_Element.__logger.debug("Created new element " +
        extProps.__elementWrapper);
    uiHtml_Element.__logger.debug("ID/name: " + id + "/" + name);
  }
  return extProps.__elementWrapper;
};

/**
 * Creates a new class that wraps the provided DOM object, whose type is
 * determined by the <code>optTypeClass</code> argument. If the type is
 * not specified, a most suitable class for the object will be created
 * and returned.
 *
 * @param {HTMLElement} domObject the element DOM object
 * @param {Class} optTypeClass an optional indication of the wrapper type
 *     of the element, if not specified, the most suitable wrapper for the
 *     element will be used
 * @return the wrapper
 * @type uiHtml_Element
 */
uiHtml_Element.__createElement = function(domObject, optTypeClass) {
  if (optTypeClass != null &&
      uiUtil_Object.isAssignableFrom(uiHtml_Element, optTypeClass)) {
    return new optTypeClass(domObject);
  }
  return uiHtml_Element.createElementByType(domObject);
};

/**
 * Returns {@link uiHtml_Element}-related extended properties for an
 * element.
 *
 * @param {HTMLElement} domElement the element
 * @return the properties object
 * @type uiHtml_Element.DomExtProps
 */
uiHtml_Element.__getExtProps = function(domElement) {
  var supporter = uiHtml_ElementWrapper.getInstance();
  var extProps = supporter._getClassProperty(domElement, uiHtml_Element);
  if (extProps == null) {
    extProps = new uiHtml_Element.DomExtProps();
    supporter._setClassProperty(domElement, uiHtml_Element, extProps);
  }
  return extProps;
}

/**
 * Creates and returns the most appropriate wrapper (either an instance
 * of {@link uiHtml_Element} or its subclasses) for a HTML element.
 *
 * @param {HTMLElement} domElement the element
 * @return the created element
 * @type uiHtml_Element
 */
uiHtml_Element.createElementByType = function(domElement) {
  if (domElement == null) {
    return null;
  }

  switch(uiHtml_Element.getWidgetType(domElement)) {
    case "select"   : return new uiHtml_Select(domElement);
    default         : return new uiHtml_Element(domElement);
  }
};

/**
 * Returns a string representing the type of a widget. If a non-widget
 * object is provided, the string <code>undefined</code> will be returned.
 *
 * @param {HTMLElement} domElement the widget's DOM object
 * @return the type
 * @type String
 */
uiHtml_Element.getWidgetType = function(domElement) {
  var tagName = uiHtml_Element.getTagName(domElement);
  switch (tagName) {
    case "label"   :
    case "select"  :
    case "option"  :
    case "textarea": return tagName;
    case "input"   : // submit, button, text, radio, checkbox
                     return domElement.type.toLowerCase();
    default        : return "undefined";
  }
};

/**
 * author: ctoohey
 *
 * Checks whether the supplied object is an array.
 * A select widget is considered an array (of options).
 * select and radio group widgets are arrays.
 */
uiHtml_Element.isArray = function(domElement) {
  return domElement != null && domElement[0] != null;
}

/**
 * Returns the HTML tag name of an element.
 *
 * @param {HTMLElement} domElement the element
 * @return the tag name
 * @type String
 */
uiHtml_Element.getTagName = function(domElement) {
  var tagName = domElement.tagName;
  if (tagName == null) {
    return "undefined";
  }
  return tagName.toLowerCase();
};

/**
 * Initializes a newly created dom element. Some elements are to appear
 * when they are created, while some others might not.
 *
 * @param {HTMLElement} domElement the element
 * @param {boolean} optAppear an optional parameter indicating whether
 *     the element should initially appear or not (disappear).
 */
uiHtml_Element._initializeDomObject = function(domElement, optAppear) {
  var supporter = uiHtml_ElementWrapper.getInstance();
  if (uiUtil_Type.getBoolean(optAppear, true)) {
    supporter.appear(domElement);
  }
  else {
    supporter.disappear(domElement);
  }
};


///////////////////////////////////
////////// Inner Classes //////////
///////////////////////////////////

/**
 * Default constructor.
 *
 * @class An extended properties wrapper for elements
 * @extends uiUtil_Object
 */
function uiHtml_Element$DomExtProps() {
  /**
   * The HTML wrapper (i.e. Element, Select, Toggle, Panel, etc.) object
   * cache, intended to prevent instantiation of multiple wrappers for the
   * same DOM object, in which case, the state of the two wrappers might
   * not be consistent. Thus, to avoid the inconsistency problem, a DOM
   * HTML element is meant to have only one wrapper.
   *
   * @type uiHtml_Element
   * @private
   */
  this.__elementWrapper = null;
}

/** @ignore */
uiHtml_Element.DomExtProps = uiUtil_Object.declareClass(
    uiHtml_Element$DomExtProps, uiUtil_Object);
