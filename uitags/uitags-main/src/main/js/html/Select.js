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

function uiHtml_Select(domSelect, optOptions) {
  this._super(domSelect);
  this.__domSelect = domSelect;
  this.__options = domSelect.options;
  this.__optionMap = new Array();

  // Turned off for efficiency reason, especially when the select box
  // contains many options.
  this.__valueMapping = false;

  if (optOptions != null) {
    this.addItems(optOptions);
  }
  this.__handler = uiHtml_SelectOptionWrapper.getInstance();
  this.__group = new uiHtml_Group(this.__options, this.__handler);
  this.__scrollSupporter = new uiHtml_ScrollSupporter(this.__domSelect);
}

uiHtml_Select = uiUtil_Object.declareClass(uiHtml_Select, uiHtml_Element);

uiHtml_Select.INVALID_INDEX = -1;
// upper limit of number of entries in the history before starts cropping
// this mean that at all time the max entries in the history equals:
// UPPER_LIMIT - REMOVE_RATE
uiHtml_Select.HISTORY_UPPER_LIMIT = 20;
// number of entries to delete in each removal
uiHtml_Select.HISTORY_REMOVE_RATE  = 5;

uiHtml_Select.__historyEnabled = new Array();


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/////
// uiHtml_Group delegation starts
uiHtml_Select.prototype.getItemHandler = function() {
  return this.__group.getItemHandler();
};

uiHtml_Select.prototype.size = function() {
  return this.__group.size();
};

uiHtml_Select.prototype.getItemAt = function(index) {
  return this.__group.getItemAt(index);
};

uiHtml_Select.prototype.setItemAt = function(index, item) {
  return this.__group.setItemAt(index, item);
};

uiHtml_Select.prototype._getItems = function() {
  return this.__group._getItems();
};

uiHtml_Select.prototype.hasValue = function(value) {
  return this.__group.hasValue(value);
};

// author:ctoohey
// observe and ignore child tags matching their forValue is done via
// a regular expression match
uiHtml_Select.prototype.hasRegExpValue = function(value) {
  return this.__group.hasRegExpValue(value);
}  

uiHtml_Select.prototype.getValues = function() {
  return this.__group.getValues();
};

uiHtml_Select.prototype.traverse = function(handlerOwner, handlerFunction, optArgN) {
  var argArray = new Array(this.__domSelect);
  for (var i = 1; i < arguments.length; ++i) {
    argArray[i] = arguments[i];
  }
  handlerFunction.apply(handlerOwner, argArray);
  this.__group.traverse(handlerOwner, handlerFunction, arguments);
};

uiHtml_Select.prototype.handleItemAtIndex = function(index, owner, handler) {
  return this.__group.handleItemAtIndex(index, owner, handler);
};

uiHtml_Select.prototype.handleItemWithValue = function(value, owner, handler) {
  return this.__group.handleItemWithValue(value, owner, handler);
};

uiHtml_Select.prototype.addItem = function(option) {
  this.__options.add(option);
  if (this.__valueMapping) {
    this.__optionMap[option.value] = option;
  }
};

uiHtml_Select.prototype.enableOptionValueMapping = function() {
  this.__valueMapping = true

  var listLength = this.size();
  for (var i = 0; i < listLength; ++i) {
    var option = this.getItemAt(i);
    this.__optionMap[option.value] = option;
  }
}
// uiHtml_Group delegation ends
/////

/////
// uiHtml_ScrollSupporter delegation starts
uiHtml_Select.prototype.showScrollBars = function(always) {
  this.__scrollSupporter.showScrollBars(always);
};

uiHtml_Select.prototype.hideScrollBars = function() {
  this.__scrollSupporter.hideScrollBars();
};

uiHtml_Select.prototype.scrollToTop = function() {
  this.__scrollSupporter.scrollToTop();
};

uiHtml_Select.prototype.scrollToBottom = function() {
  if (uiHtml_Window.getInstance().isIe()) {
    // somehow the normal way does not work in this particular case
    // apparently this.__domElement.scrollTop is read-only in IE

    // currently selected items
    var selectedItems = new Array();
    var listLength = this.size();
    for (var i = 0; i < listLength; ++i) {
      var item = this.getItemAt(i);
      if (this.__handler.isSelected(item)) {
        selectedItems.push(item);
      }
    }

    var domSelect = this.__domSelect;

    // Perform the actual scrolling (a very dodgy way, but better than
    // nothing).
    domSelect.multiple = false;

    // allow time for the browser to refresh
    // reference: http://webdeveloper.com/forum/showthread.php?t=43503&highlight=move+select+list
    window.setTimeout(function(e) {
      domSelect.selectedIndex = listLength - 1;
    }, 50);  // the delay should be bigger than the reverting back multiple
    window.setTimeout(function(e) {
      domSelect.multiple = true;
    }, 10);

    var item = this.getItemAt(listLength - 1);
    item.selected = false;

    window.setTimeout(function(e) {
      // Just in case the selected items got reset when switching
      // between single and multiple list.
      for (var i = 0; i < selectedItems.length; ++i) {
        selectedItems[i].selected = true;
      }
    }, 100);
  }
  else {
    this.__scrollSupporter.scrollToBottom();
  }
};
// uiHtml_ScrollSupporter delegation ends
/////

uiHtml_Select.prototype.getDomObject = function() {
  return this.__domSelect;
};

uiHtml_Select.prototype.getItemByValue = function(value) {
  if (!this.__valueMapping) {
    throw new uiUtil_IllegalStateException(
        "Option value mapping is not enabled");
  }
  return this.__optionMap[value];
};

// Adds the supplied opt to the list of Option objects (it's up to the
// function invoker to make sure that it won't end up with duplicated
// option.
uiHtml_Select.prototype.addItems = function(options) {
  for (var i = 0; i < options.length; ++i) {
    this.addItem(options[i]);
  }
};

// Removes the supplied opt from the list of Option objects
uiHtml_Select.prototype.removeItem = function(option) {
  var options = this.__domSelect.options;
  var i = this.indexOf(option);
  if (i != uiHtml_Select.INVALID_INDEX) {
    options[i] = null;
  }
};

uiHtml_Select.prototype.sortItems = function(comparator, reverse) {
  var options = this.__domSelect.options;
  var tempArray = new Array(options.length);
  for(var i = 0; i < options.length; ++i) {
    tempArray[i] = this.__handler.clone(options[i]);
  }

  tempArray.sort(comparator);
  if (reverse) {
    tempArray.reverse(comparator);
  }

  for (var i = 0; i < options.length; ++i) {
    options[i] = tempArray[i];
  }
};

uiHtml_Select.prototype.setSelectedIndex = function(index, optDomEvent) {
  this.setSelectedItem(this.__options[index], optDomEvent);
};

uiHtml_Select.prototype.setSelectedItem = function(item, optDomEvent) {
  this.__handler.setSelected(item, true, optDomEvent);
};

uiHtml_Select.prototype.setSelectedValue = function(value, optDomEvent) {
  var option = this.getItemByValue(value);
  this.setSelectedItem(option, optDomEvent);
};

/**
 * author: ctoohey
 * Selects the selectbox option whose text matches textValue and optionValue
 * or just one of them if only one is supplied.
 * If there is no match, a new option is created using textValue as the text
 * and optionValue as the value, and this option is selected. If textValue is
 * not supplied, the new option has both text and value set to optionValue, and
 * if optionValue is not supplied, the new option has both text and value set
 * to textValue.
 */
uiHtml_Select.prototype.setSelectedOptionAddIfNoMatch(textValue, optionValue) {
  var list = widget.options;
  for(var i = 0; i < this.__options.length; ++i) {
    if(this.__options[i].text == textValue || this.__options[i].value == optionValue) {
      this.__domSelect.selectedIndex = i;
      this.__options[i].selected = true;
      return;
    }
  }
  
  // if the option did not exist, add a new option
  // This trick is needed to make IE work
  var i = this.__options.length++;
  if (textValue != null) {
    this.__options[i].text  = textValue;
  else {
    this.__options[i].text  = optionValue;
  }  
  
  if (newOptionValue != null) {
    this.__options[i].value = newOptionValue;
  }
  else {
    this.__options[i].value = textValue;
  }

  this.__options[i].selected = true;
  this.__domSelect.selectedIndex = i;
}


// Removes all Option objects in opts
uiHtml_Select.prototype.clearItems = function() {
  var options = this.__domSelect.options;
  while (options.length > 0) {
    options[0] = null;
  }
};

uiHtml_Select.prototype.removeItemAt = function(index) {
  this.__options[index] = null;
};

uiHtml_Select.prototype.enableHistoryTracking = function() {
  var id = this.__domSelect.id;
  if (!uiHtml_Select.__historyEnabled[id]) {
    uiHtml_Select.__historyEnabled[id] = this;

    this.__changeHistory = new uiUtil_Vector(new Array());
    this.__historyIndex = -1;
    this.__lastSelected = new Array();
    this.__pushHistory();  // record current state as a start point

    var select = this;
    this.appendEventHandler("change", function(e) {
      select.__pushHistory();
    });

    this.appendEventHandler("keydown", function(e) {
      var event = new uiHtml_Event(e);
      if (!event.isAltPressed() &&
          event.isCtrlPressed() &&
          !event.isShiftPressed()) {
        var ch = event.getPressedChar(true);

        if (ch == "z") {  // undo
          select.__undoHistory(e);
        }
        else if (ch == "y") {  // redo
          select.__redoHistory(e);
        }
      }
    });
  }
};

uiHtml_Select.prototype.__pushHistory = function() {
  var currSelected = new Array();
  var options = this.__domSelect.options;
  for(var i = 0; i < options.length; ++i) {
    if (this.__handler.isSelected(options[i])) {
      currSelected[options[i].value] = options[i];
    }
  }

  // clear history entries that get lost after undoing
  if (this.__historyIndex < this.__changeHistory.length - 1) {
    // this might happen because the history has been undoed
    this.__changeHistory.removeAt(
        -1, this.__changeHistory.length - this.__historyIndex - 1);
  }

  var currChanges = this.__getChanges(currSelected, this.__lastSelected);
  this.__changeHistory.add(currChanges);
  if (this.__changeHistory.length >= uiHtml_Select.HISTORY_UPPER_LIMIT) {
    // remove the first few history entries
    this.__changeHistory.removeAt(0, uiHtml_Select.HISTORY_REMOVE_RATE);
  }
  this.__historyIndex = this.__changeHistory.length - 1;
  this.__lastSelected = currSelected;

  this.__logger.debug("history size: " + this.__changeHistory.length);
};

uiHtml_Select.prototype.__getChanges = function(currSelected, lastSelected) {
  var changes = new Array();
  for (var key in currSelected) {
    // NOTE: the lastSelected can sometimes be null (see playHistory())
    if (!lastSelected[key] && currSelected[key]) {  // not found thus a change
      changes.push(currSelected[key]);
    }
  }
  for (var key in lastSelected) {
    // NOTE: the lastSelected can sometimes be null (see playHistory())
    if (!currSelected[key] && lastSelected[key]) {  // not found thus a change
      changes.push(lastSelected[key]);
    }
  }
  return changes;
};

uiHtml_Select.prototype.__undoHistory = function(domEvent) {
  // NOTE: the first entry in the history is just the starting point of
  //       the recording and thus does not keep track of any changes
  //       therefore we should stop once we hit it
  if (this.__historyIndex < 1) {
    return;
  }

  this.__playHistory(domEvent, this.__historyIndex--);
};

uiHtml_Select.prototype.__redoHistory = function(domEvent) {
  // NOTE: the first entry in the history is just the starting point of
  //       the recording and thus does not keep track of any changes
  //       therefore we should stop once we hit it
  if (this.__historyIndex >= this.__changeHistory.length - 1) {
    return;
  }

  this.__playHistory(domEvent, ++this.__historyIndex);
};

uiHtml_Select.prototype.__playHistory = function(domEvent, historyIndex) {
  var changes = this.__changeHistory.get(historyIndex);
  for (var i = 0; i < changes.length; ++i) {
    var selected = this.__handler.isSelected(changes[i]);
    var key = changes[i].value;
    if (selected) {
      this.__handler.setSelected(changes[i], false, domEvent);
      this.__lastSelected[key] = null;
    }
    else {
      this.__handler.setSelected(changes[i], true, domEvent);
      this.__lastSelected[key] = changes[i];
    }
  }
};





////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

uiHtml_Select.createByEither = function(id, name) {
  return uiHtml_Element.createByEither(id, name, uiHtml_Select);
};

uiHtml_Select.create = function(options, optAppear) {
  return new uiHtml_Select(
      uiHtml_Document.getInstance().createDomObject("select", optAppear), options);
};

