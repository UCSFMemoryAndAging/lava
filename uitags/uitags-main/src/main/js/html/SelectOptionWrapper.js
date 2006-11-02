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

function uiHtml_SelectOptionWrapper() {
  this._super();
}

uiHtml_SelectOptionWrapper =
    uiUtil_Object.declareSingleton(uiHtml_SelectOptionWrapper, uiHtml_ElementWrapper);

/**
 * Indicates whether a global (applies for all options) initialization
 * for option disabling has been performed.
 *
 * @type boolean
 * @private
 */
uiHtml_SelectOptionWrapper.__ieDisablingInitialized = false;


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiHtml_SelectOptionWrapper.prototype.isSelected = function(domOption) {
  return domOption.selected;
};

uiHtml_SelectOptionWrapper.prototype.setSelected = function(
    domOption, value, optDomEvent) {
  if (domOption.selected != value) {
    domOption.selected = value;

    // Trigger the select box's "change" event.
    var domSelect = domOption.parentNode;
    if (domSelect != null &&
        uiHtml_Element.getWidgetType(domSelect) == "select") {
      this.executeAggregateEventHandler(domSelect, "change", optDomEvent);
    }
  }
};

uiHtml_SelectOptionWrapper.prototype.setDisabled = function(
    domOption, value) {
  domOption.disabled = value;

  // IE 6 does not handle select option disabling at all.
  if (uiHtml_Window.getInstance().isIe()) {
    var extProps = this.__getOptionExtProps(domOption);
    if (domOption.disabled) {
      if (!uiHtml_SelectOptionWrapper.__ieDisablingInitialized) {
        this.__initializeIeDisabling(domOption);
      }

      // Store the previous/original color first.
      extProps.__originalColor = this.getCascadedStyleAttribute(
          domOption, "color");
      this.setStyleAttribute(domOption, "color", "gray");
    }
    else {
      // Because the option's color was changed earlier, it needs to be reset.
      this.setStyleAttribute(domOption, "color", extProps.__originalColor);
    }
  }
}

uiHtml_SelectOptionWrapper.prototype.__initializeIeDisabling = function(domOption) {
  var domSelect = domOption.parentNode;
  if (domSelect != null &&
      uiHtml_Element.getWidgetType(domSelect) == "select") {
    // NOTE: Logically, the effect of mousedown/focus should be more
    //       realistic, however it seems that manually changing the
    //       "selected" value when the browser is changing it (while
    //       mousedown/focus) does not work. The value can only be
    //       manually changed after the browser changes the value --
    //       even using "click" does not work.
    this.appendEventHandler(domSelect, "change", function(e) {
      for (var i = 0; i < domSelect.options.length; ++i) {
        var domOption = domSelect.options[i];
        if (domOption.disabled && domOption.selected) {
          domOption.selected = false;
        }
      }
    });
  }

  uiHtml_SelectOptionWrapper.__ieDisablingInitialized = true;
}

uiHtml_SelectOptionWrapper.prototype.__getOptionExtProps = function(domOption) {
  var extProps = this._getClassProperty(domOption, uiHtml_SelectOptionWrapper);
  if (extProps == null) {
    extProps = new uiHtml_SelectOptionWrapper.DomExtProps();
    this._setClassProperty(domOption, uiHtml_SelectOptionWrapper, extProps);
  }
  return extProps;
}

/**
 * Returns the value of the supplied DOM select option's <code>value</code>
 * property if it is selected, or <code>null</code> otherwise.
 *
 * @param {HTMLInputElement} domElement the DOM checkbox to operate on
 * @return the supplied DOM checkbox's logical value
 * @type String
 */
uiHtml_SelectOptionWrapper.prototype.getLogicalValue = function(domElement) {
  return (this.isSelected(domElement))? domElement.value : null;
};

// in Mozilla 1.7, cloneNode() does not copy 'defaultSelected'
// and 'selected' attributes
uiHtml_SelectOptionWrapper.prototype.clone = function(domOption) {
  return uiHtml_SelectOptionWrapper.create(
      domOption.text, domOption.value,
      domOption.defaultSelected, domOption.selected);
};

uiHtml_SelectOptionWrapper.prototype.equals = function(domOption1, domOption2) {
  return domOption1.value == domOption2.value;
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

uiHtml_SelectOptionWrapper.create = function(text, value, optDefaultSelected, optSelected) {
  var domOption = new Option(text, value,
      uiUtil_Type.getBoolean(optDefaultSelected, false),
      uiUtil_Type.getBoolean(optSelected, false));
  return domOption;
};


///////////////////////////////////
////////// Inner Classes //////////
///////////////////////////////////

/**
 * Default constructor.
 *
 * @class An extended properties wrapper for select options
 * @extends uiUtil_Object
 */
function uiHtml_SelectOptionWrapper$DomExtProps() {
  /**
   * The option's original color. Useful for simulating option disabling
   * in IE.
   *
   * @type String
   * @private
   */
  this.__originalColor = "black";
}

/** @ignore */
uiHtml_SelectOptionWrapper.DomExtProps = uiUtil_Object.declareClass(
    uiHtml_SelectOptionWrapper$DomExtProps, uiUtil_Object);
