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

function uiHtml_RadioWrapper() {
  this._super();
}

uiHtml_RadioWrapper =
    uiUtil_Object.declareSingleton(uiHtml_RadioWrapper, uiHtml_ElementWrapper);

// Keyed on the radio group name.
uiHtml_RadioWrapper.__selectedRadio = new Array();


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiHtml_RadioWrapper.prototype.appendEventHandler = function(domRadio, event, handler) {
  // In Opera 8, radio's onchange does not work at all.
  // In IE 6, radio's onchange only invoked when the it is blurred.
  var isOpera = uiHtml_Window.getInstance().isOpera();
  var isIe = uiHtml_Window.getInstance().isIe();
  if (event == "change" && (isOpera || isIe)) {
    if (domRadio.checked) {
      uiHtml_RadioWrapper.__selectedRadio[domRadio.name] = domRadio;
    }

    var wrapper = this;
    return this._callSuper(
        "appendEventHandler", domRadio, "click", function(e) {
      // NOTE: The following is to solve a radio specific problem, where
      //       a click does not necessarily change the radio's state.
      var previouslySelected =
          uiHtml_RadioWrapper.__selectedRadio[domRadio.name];
      if (previouslySelected != domRadio) {
        handler();
        uiHtml_RadioWrapper.__selectedRadio[domRadio.name] = domRadio;
      }
    });
  }
  else {
    return this._callSuper("appendEventHandler", domRadio, event, handler);
  }
};

uiHtml_RadioWrapper.prototype.isSelected = function(domRadio) {
  return domRadio.checked;
};

uiHtml_RadioWrapper.prototype.setSelected = function(domRadio, value, optDomEvent) {
  if (domRadio.checked != value) {
    domRadio.checked = value;
    this.executeAggregateEventHandler(domRadio, "change", optDomEvent);
  }
};

/**
 * Returns the value of the supplied DOM radio button's <code>value</code>
 * property if it is selected, or <code>null</code> otherwise.
 *
 * @param {HTMLInputElement} domElement the DOM radio button to operate on
 * @return the supplied DOM radio button's logical value
 * @type String
 */
uiHtml_RadioWrapper.prototype.getLogicalValue = function(domElement) {
  return (this.isSelected(domElement))? domElement.value : null;
};
