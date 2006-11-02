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
 * Creates a wrapper for the current window.
 *
 * @class A wrapper for HTML window objects.
 * @extends uiHtml_Element
 */
function uiHtml_Window() {
  this._super(window);
  /**
   * Signifies whether the client browser is opera
   *
   * @type boolean
   * @private
   */
  this.__isOpera = window.opera ? true : false;
  /**
   * Signifies whether the client browser is IE.
   *
   * @type boolean
   * @private
   */
  this.__isIe = (document.all != null && !this.__isOpera);
  /**
   * ID of the finalizer event handler. This value allows us to remove
   * the finalizer from the event handler list and reappend it to the
   * end of the handler list to make sure that the finalizer is the last
   * handler that gets executed upon window unload.
   *
   * @type int
   * @private
   */
  this.__finalizerId = this.__appendElementFinalizer();

  var uiWindow = this;
  this.appendEventHandler("error", function(message, url, line) {
    uiWindow.__printError(message, url, line);
  });

  this.prependEventHandler("load", function(e) {
    uiHtml_Window.__isLoaded = true;
    uiHtml_Window.getInstance().__logger.debug("finish loading");
  });
}

uiHtml_Window = uiUtil_Object.declareSingleton(uiHtml_Window, uiHtml_Element);

/**
 * A special variable to determine whether the windows has been loaded
 * without creating an instance of this class. The main purpose is to
 * avoid problems when the instantiation itself needs the window to be
 * loaded first.
 *
 * @type boolean
 * @private
 */
uiHtml_Window.__isLoaded = false;


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Checks if the client browser is Opera.
 *
 * @return <code>true</code> if it is Opera, <code>false</code> otherwise
 * @type boolean
 */
uiHtml_Window.prototype.isOpera = function() {
  return this.__isOpera;
};

/**
 * Checks if the client browser is IE.
 *
 * @return <code>true</code> if it is IE, <code>false</code> otherwise
 * @type boolean
 */
uiHtml_Window.prototype.isIe = function() {
  return this.__isIe;
};

/**
 * Returns the browser's version number.
 *
 * @return the version number
 * @type int
 */
uiHtml_Window.prototype.getBrowserVersion = function() {
  if (this.__isOpera) {
    return this.__extractVersion(navigator.userAgent, "Opera");
  }
  if (this.__isIe) {
    return this.__extractVersion(navigator.userAgent, "MSIE");
  }
  // NOTE: Currently support for other browsers is not needed yet.
  return -1;
};

uiHtml_Window.prototype.__extractVersion = function(data, idSubstring) {
  var index = data.indexOf(idSubstring);
  if (index >= 0) {
    return parseFloat(data.substring(index + idSubstring.length + 1));
  }
  return -1;
}

/**
 * Displays a message for an unhandled error.
 *
 * @param {String} message the error message
 * @param {String} url the Javascript file where the error occured
 * @param {int} line the line number of the code that causes the error
 * @private
 */
uiHtml_Window.prototype.__printError = function(message, url, line) {
  this.__logger.info(message + " (Loc: " + url + ":" + line + ")");
};

/**
 * Returns the width of the window in pixels.
 *
 * @return the width
 * @type int
 */
uiHtml_Window.prototype.getWidth = function() {
  if (this.__isIe) {
    return document.documentElement.clientWidth;
  }
  else {
    return window.innerWidth;
  }
};

/**
 * Returns the height of the window in pixels.
 *
 * @return the height
 * @type int
 */
uiHtml_Window.prototype.getHeight = function() {
  if (this.__isIe) {
    return document.documentElement.clientHeight;
  }
  else {
    return window.innerHeight;
  }
};

/**
 * Appends element finalizer to the end of the <code>unload</code> event
 * handler list.
 *
 * @return the finalizer ID
 * @type int
 * @private
 */
uiHtml_Window.prototype.__appendElementFinalizer = function() {
  return this._callSuper("appendEventHandler", "unload", function(e) {
    uiHtml_ElementWrapper._finalizeElements();
    // This is important for testing/debugging as it signifies that the
    // finalizer has been called.
    uiHtml_Window.__isLoaded = false;
  });
}

/*
 * Overrides parent's method.
 */
uiHtml_Window.prototype.appendEventHandler = function(eventName, handler) {
  if (eventName == "unload" && this.__finalizerId != null) {
    // Ensures that the finalizer event handler is always the last one.
    this.removeEventHandler(eventName, this.__finalizerId);
    var index = this._callSuper("appendEventHandler", eventName, handler);
    this.__finalizerId = this.__appendElementFinalizer();
    return index;
  }
  return this._callSuper("appendEventHandler", eventName, handler);
};

/*
 * Overrides parent's method.
 */
uiHtml_Window.prototype.clearEventHandlerExtension = function(eventName) {
  this._callSuper("clearEventHandlerExtension", eventName);

  if (eventName == "unload") {
    this.__finalizerId = this.__appendElementFinalizer();
  }
};

/**
 * Returns the request parameters (query strings).
 *
 * @return hash of all the parameters
 * @type hash&lt;String&gt;
 */
uiHtml_Window.prototype.getRequestParameters = function() {
  var parameters = new Object();
  var url = window.top.location.href;
  var queries = url.split("&");
  for (var i = 0; i < queries.length; ++i) {
    var pair = queries[i].split("=");
    var key = pair[0];
    var value = pair[1];
    parameters[key] = value;
  }
  return parameters;
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * Checks if the window has finished loading.
 *
 * @return <code>true</code> if it has, <code>false</code> otherwise
 * @type boolean
 */
uiHtml_Window.isLoaded = function() {
  return uiHtml_Window.__isLoaded;
};
