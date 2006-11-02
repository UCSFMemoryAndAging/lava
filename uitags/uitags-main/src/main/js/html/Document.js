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
 * Creates a wrapper for the current HTML document.
 *
 * @class A wrapper for HTML document objects.
 * @extends uiHtml_Element
 */
function uiHtml_Document() {
  this._super(document.body);
  /**
   * Simplifies the insertion of texts to the document.
   *
   * @type uiUtil_Logger.PrintStream
   * @private
   */
  this.__printStream = null;
  /**
   * In Firefox 1, document.documentElement is the only thing that can be
   * scrolled.
   *
   * @type uiHtml_ScrollSupporter
   * @private
   */
  this.__scrollSupporter =
      new uiHtml_ScrollSupporter(document.documentElement);

  /**
   * In Firefox 1.5, IE 6, Opera 8, document refers to part of the viewport
   * that contains visible DOM elements. Therefore any event occuring
   * outside that area (can happen when the document is short) will not
   * trigger the handler execution. Whereas document.body refers to the
   * whole viewport, which is more desirable.
   *
   * @type uiHtml_Element
   * @private
   */
  this.__eventSupporter = uiHtml_Element.createElementByType(document);
}

uiHtml_Document = uiUtil_Object.declareSingleton(uiHtml_Document, uiHtml_Element);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Returns a DOM element object with the specified ID.
 *
 * @param {String} id ID of the DOM element
 * @param {boolean} optAssert an optional argument to indicate whether
 *     an exception should be thrown if the DOM element does not exist
 * @throws uiUtil_IllegalArgumentException if <code>optAssert</code> is
 *     <code>true</code> and the DOM element does not exist
 * @return the DOM object
 * @type HTMLElement
 */
uiHtml_Document.prototype.getDomObjectById = function(id, optAssert) {
  var domObject = document.getElementById(id);
  if (uiUtil_Type.getBoolean(optAssert, true) && domObject == null) {
    throw new uiUtil_IllegalArgumentException("Invalid element ID: " + id);
  }
  return domObject;
};

/**
 * Returns the browser's native collection of DOM objects with the
 * specified name.
 *
 * @param {String} name name of the DOM element
 * @param {boolean} optAssert an optional argument to indicate whether
 *     an exception should be thrown if the DOM element does not exist
 * @throws uiUtil_IllegalArgumentException if <code>optAssert</code> is
 *     <code>true</code> and the DOM element does not exist
 * @return the DOM object collection
 * @type HTMLElement
 */
uiHtml_Document.prototype.getDomObjectsByName = function(name, optAssert) {
  var domObjects = document.getElementsByName(name);
  if (uiUtil_Type.getBoolean(optAssert, true) && domObjects[0] == null) {
    throw new uiUtil_IllegalArgumentException("Invalid element name: " + name);
  }
  return domObjects;
};

/**
 * Asserts the window to have finished loading, which is necessary
 * for certain operations in order to avoid unpredictable behaviours
 *
 * @param {String} message the error message
 * @throws uiUtil_IllegalStateException if the window is not completely
 *     loaded yet
 * @private
 */
uiHtml_Document.prototype.__assertLoaded = function(message) {
  // Example of the problem: IE might just show "page cannot be
  // displayed" instead of throwing exceptions, which is hard to debug.
  if (!uiHtml_Window.isLoaded()) {
    throw new uiUtil_IllegalStateException(message);
  }
};

/**
 * Asserts the window to have finished loading when attaching DOM object
 * to the document.
 *
 * @param {String} tagName the tag name of the DOM object
 * @throws uiUtil_IllegalStateException if the window is not completely
 * @private
 */
uiHtml_Document.prototype.__assertLoadedWhenAttachingDomObjectOf = function(tagName) {
  this.__assertLoaded("DOM object " + tagName + " should not be " +
        "attached to document before the window finishes loading");
};

/**
 * Creates a DOM element that is a direct child of the <i>document</i>.
 *
 * @param {String} tagName the tag name of the DOM object
 * @param {boolean} optAppear an option argument to indicate whether the
 *     created DOM element should appear or not (disappear)
 * @throws uiUtil_IllegalStateException if the window hasn't finished loading
 *     because it could cause unpredictable behaviour in some browsers
 * @return the DOM object
 * @type HTMLElement
 */
uiHtml_Document.prototype.createDomObject = function(tagName, optAppear) {
  this.__assertLoadedWhenAttachingDomObjectOf(tagName);
  return uiHtml_Document.createDomObject(tagName, optAppear, document.body);
};

/*
 * Overrides parent's method.
 */
uiHtml_Document.prototype.prependEventHandler = function(eventName, eventHandler) {
  return this.__eventSupporter.prependEventHandler(eventName, eventHandler);
};

/*
 * Overrides parent's method.
 */
uiHtml_Document.prototype.appendEventHandler = function(eventName, eventHandler) {
  return this.__eventSupporter.appendEventHandler(eventName, eventHandler);
};

/*
 * Overrides parent's method.
 */
uiHtml_Document.prototype.removeEventHandler = function(eventName, startIndex, optCount) {
  this.__eventSupporter.removeEventHandler(eventName, startIndex, optCount);
};

/*
 * Overrides parent's method.
 */
uiHtml_Document.prototype.clearEventHandlerExtension = function(eventName) {
  this.__eventSupporter.clearEventHandlerExtension(eventName);
};

/**
 * Returns the print stream of the document, which allows texts to be
 * appended to the document.
 *
 * @throws uiUtil_IllegalStateException if the window hasn't finished loading
 *     because it could cause unpredictable behaviour in some browsers
 * @return the print stream
 * @type uiUtil_Logger.PrintStream
 */
uiHtml_Document.prototype.getPrintStream = function() {
  if (this.__printStream == null) {
    this.__assertLoaded(
        "Cannot get print stream before the window finishes loading");
    this.__printStream = new uiHtml_PrintSupporter(this.getDomObject());
  }
  return this.__printStream;
};

/**
 * Scrolls to the top of the document.
 *
 * @see uiHtml_ScrollSupporter#scrollToTop()
 */
uiHtml_Document.prototype.scrollToTop = function() {
  this.__scrollSupporter.scrollToTop();
};

/**
 * Scrolls to the bottom of the document.
 *
 * @see uiHtml_ScrollSupporter#scrollToBottom()
 */
uiHtml_Document.prototype.scrollToBottom = function() {
  this.__scrollSupporter.scrollToBottom();
};

/**
 * Returns the left scroll offset of the document.
 *
 * @see uiHtml_ScrollSupporter#getScrollLeft()
 */
uiHtml_Document.prototype.getScrollLeft = function() {
  return this.__scrollSupporter.getScrollLeft();
};

/**
 * Returns the top scroll offset of the document.
 *
 * @see uiHtml_ScrollSupporter#getScrollTop()
 */
uiHtml_Document.prototype.getScrollTop = function() {
  return this.__scrollSupporter.getScrollTop();
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * Creates a dom object that can have any element as its parent.
 *
 * @param {String} tagName the tag name of the DOM object
 * @param {boolean} optAppear an option argument to indicate whether the
 *     created DOM element should appear or not (disappear)
 * @param {HTMLElement} optParent the nominated parent
 * @throws uiUtil_IllegalStateException if the window hasn't finished loading
 *     because it could cause unpredictable behaviour in some browsers
 * @return the DOM object
 * @type HTMLElement
 */
uiHtml_Document.createDomObject = function(tagName, optAppear, optParent) {
  var domObject = document.createElement(tagName);
  uiHtml_Element._initializeDomObject(domObject, optAppear);

  if (uiUtil_Type.isDefined(optParent)) {
    optParent.appendChild(domObject);
  }  // else, standalone object, no owner yet
  return domObject;
};

/**
 * Creates a text node that can have any element as its parent.
 *
 * @param {String} initialText the content of the text node
 * @param {HTMLElement} optParent the nominated parent
 * @throws uiUtil_IllegalStateException if the window hasn't finished loading
 *     because it could cause unpredictable behaviour in some browsers
 * @return the DOM object
 * @type TextNode
 */

uiHtml_Document.createTextNode = function(initialText, optParent) {
  var domTextNode = document.createTextNode(initialText);
  if (uiUtil_Type.isDefined(optParent)) {
    optParent.appendChild(domTextNode);
  }  // else, standalone object, no owner yet
  return domTextNode;
};
