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

function uiHtml_CheckBoxWrapper() {
  this._super();
}

uiHtml_CheckBoxWrapper =
    uiUtil_Object.declareSingleton(uiHtml_CheckBoxWrapper, uiHtml_ElementWrapper);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiHtml_CheckBoxWrapper.prototype.appendEventHandler = function(domCheckBox, event, handler) {
  // In IE 6, checkbox's onchange only invoked when the it is blurred.
  if (event == "change" && uiHtml_Window.getInstance().isIe()) {
    return this._callSuper(
        "appendEventHandler", domCheckBox, "click", handler);
  }
  else {
    return this._callSuper("appendEventHandler", domCheckBox, event, handler);
  }
};

uiHtml_CheckBoxWrapper.prototype.isSelected = function(domCheckBox) {
  return domCheckBox.checked;
};

uiHtml_CheckBoxWrapper.prototype.setSelected = function(
    domCheckBox, value, optDomEvent) {
  if (domCheckBox.checked != value) {
    domCheckBox.checked = value;
    this.executeAggregateEventHandler(domCheckBox, "change", optDomEvent);
  }
};

/**
 * Returns the value of the supplied DOM checkbox's <code>value</code>
 * property if it is selected, or <code>null</code> otherwise.
 *
 * @param {HTMLInputElement} domElement the DOM checkbox to operate on
 * @return the supplied DOM checkbox's logical value
 * @type String
 */
uiHtml_CheckBoxWrapper.prototype.getLogicalValue = function(domElement) {
  return (this.isSelected(domElement))? domElement.value : null;
};
