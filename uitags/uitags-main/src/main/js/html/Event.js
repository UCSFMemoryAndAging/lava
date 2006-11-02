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

function uiHtml_Event(event) {
  this._super();

  if (event == null) {  // IE
    this.__domEvent = window.event;
    this.__w3cModel = false;
  }
  else {  // netscape, mozilla, opera (W3C)
    this.__domEvent = event;
    this.__w3cModel = true;
  }
}

uiHtml_Event = uiUtil_Object.declareClass(uiHtml_Event, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

// Returns the object that triggers the event.
uiHtml_Event.prototype.getDomSource = function() {
  if (this.__w3cModel) {
    return this.__domEvent.currentTarget;
  }
  else {
    return this.__domEvent.srcElement;
  }
};

// Avoids the event from bubbling up to the parent.
uiHtml_Event.prototype.preventBubble = function() {
  if (this.__w3cModel) {
    // NOTE: Although the newer firefox and opera support cancelBubble
    //       property, it's still better to use the W3C standard way.
    this.__domEvent.stopPropagation();
  }
  else {
    this.__domEvent.cancelBubble = true;
  }
};

// Avoids the execution of the default event handling.
// In IE, the call should look like this:
// return event.preventDefault();
uiHtml_Event.prototype.preventDefault = function() {
  if (this.__w3cModel) {
    this.__domEvent.preventDefault();
  }
  else {
    this.__domEvent.returnValue = false;
  }
};

uiHtml_Event.prototype.getViewPortPosition = function() {
  return new uiUtil_Dimension(this.__domEvent.clientX, this.__domEvent.clientY);
};

uiHtml_Event.prototype.getAbsolutePosition = function() {
  return new uiUtil_Dimension(
      uiHtml_Document.getInstance().getScrollLeft() + this.__domEvent.clientX,
      uiHtml_Document.getInstance().getScrollTop() + this.__domEvent.clientY);
};

uiHtml_Event.prototype.isAltPressed = function() {
  return this.__domEvent.altKey;
};

uiHtml_Event.prototype.isCtrlPressed = function() {
  return this.__domEvent.ctrlKey;
};

uiHtml_Event.prototype.isShiftPressed = function() {
  return this.__domEvent.shiftKey;
};

uiHtml_Event.prototype.getPressedChar = function(optToLower) {
  var code = this.__domEvent.keyCode;
  if (this.__w3cModel) {
    code = this.__domEvent.which;
  }
  var character = String.fromCharCode(code);
  if (uiUtil_Type.getBoolean(optToLower, false)) {
    character = character.toLowerCase();
  }
  return character;
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

uiHtml_Event.__getDomObject = function(event) {
  if (event == null) {  // IE
    return window.event;
  }
  else {  // netscape & mozilla (W3C)
    return event;
  }
};

uiHtml_Event.getViewPortPosition = function(event) {
  var domEvent = uiHtml_Event.__getDomObject(event);
  return new uiUtil_Dimension(domEvent.clientX, domEvent.clientY);
};
