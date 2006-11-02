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

function uiHtml_DebugPanel(initialText) {
  this._super(uiHtml_Document.getInstance().createDomObject("div"));

  this.__initialize(initialText);
}

uiHtml_DebugPanel = uiUtil_Object.declareClass(uiHtml_DebugPanel, uiHtml_Panel);

uiHtml_DebugPanel.__fallbackInstance = null;
uiHtml_DebugPanel.__instance = null;


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiHtml_DebugPanel.prototype.__initialize = function(initialText) {
  this.__contentPane = uiHtml_Panel.create(false);
  this.setContent(this.__contentPane.getDomObject());

  this.__printStream = new uiHtml_PrintSupporter(
      this.__contentPane.getDomObject());
  this.println(initialText);

  // manually initilize the logger, since we didn't use uiUtil_Type to
  // declare this class
  this.__logger = uiUtil_Logger.getInstance(uiHtml_DebugPanel);
};

// attach a drag handle a the top of the panel
uiHtml_DebugPanel.prototype.enableDragHandle = function() {
  var domDiv = uiHtml_Document.getInstance().createDomObject("div");
  this.getDomObject().appendChild(domDiv);

  var dragHandle = new uiHtml_Panel(domDiv, false);
  this.enableDragSupport(dragHandle);

  dragHandle.setStyleAttribute("border-bottom", "1px solid blue");
  dragHandle.setStyleAttribute("background-color", "#cdd8e3");
  dragHandle.setStyleAttribute("position", "absolute");
  dragHandle.setDimension(0, 0, this.getWidth(), 18);

  if (this.getContent() != null) {
    var content = uiHtml_Element.createElementByType(this.getContent());
    content.setStyleAttribute("position", "absolute");
    content.setDimension(0, 20);
  }
  this.setDimension(null, null, null, this.getHeight() + 18);
};

uiHtml_DebugPanel.prototype.print = function(text) {
  if (this.__printStream) {
    this.__printStream.print(text);
    this.__contentPane.scrollToBottom();
  }
  else {  // only happens when error occurs before the stream is initialized
    alert(text);
  }
};

uiHtml_DebugPanel.prototype.println = function(text) {
  if (this.__printStream) {
    this.__printStream.println(text);
    this.__contentPane.scrollToBottom();
  }
  else {  // only happens when error occurs before the stream is initialized
    alert(text);
  }
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

uiHtml_DebugPanel.__initializeDefaultStyle = function() {
  var panel = uiHtml_DebugPanel.__instance;

  panel.__contentPane.showScrollBars();
  panel.__contentPane.setDimension(0, 0, 400, 300);
  panel.__contentPane.setStyleAttribute("background-color", "#ffffe1");

  panel.setDepth(5);
  panel.setStyleAttribute("position", "absolute");
  panel.setDimension(400, 300, 400, 300);
  panel.setStyleAttribute("border", "1px solid blue");
  panel.enableDragHandle();

  panel.show();
};

uiHtml_DebugPanel.getInstance = function(initialText) {
  if (uiHtml_DebugPanel.__instance == null) {
    uiHtml_DebugPanel.__fallbackInstance = uiUtil_Logger.getDefaultPrintStream();
    // initialize a fallback logger first to avoid recursion
    uiHtml_DebugPanel.__instance = uiHtml_DebugPanel.__fallbackInstance;
  }
  if (uiHtml_DebugPanel.__instance == uiHtml_DebugPanel.__fallbackInstance) {
    var tempHolder = new uiHtml_DebugPanel(initialText);
    uiHtml_DebugPanel.__instance = tempHolder;
    uiHtml_DebugPanel.__initializeDefaultStyle();
  }

  return uiHtml_DebugPanel.__instance;
};

