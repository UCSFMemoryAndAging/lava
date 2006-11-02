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

function uiOptionTransfer_Suite(sourceList, targetList) {
  this._super();

  this.__srcList = sourceList;
  this.__tgtList = targetList;

  this.__srcHandler = this.__srcList.getItemHandler();
  this.__tgtHandler = this.__tgtList.getItemHandler();

  // assign other reference instead of 'this', because 'this' in
  // 'src.ondblclick' refers to 'src' instead of the current object
  var suite = this;
  // attach the 'dblclick' event to the select widget instead
  // of select options, because the latter does not work in IE 6.x
  // NOTE: in mozilla 1.x the source of the event comes from the 'option'
  //       event though the handler is attached to 'select'
  this.__srcList.appendEventHandler("dblclick", function(e) {
    suite.__transfer(e, false);
  });
  this.__tgtList.appendEventHandler("dblclick", function(e) {
    suite.__return(e, false);
  });
  var domForm = this.__tgtList.getDomObject().form;
  if (domForm != null) {
    uiHtml_ElementWrapper.getInstance().prependEventHandler(
        domForm, "submit",  function(e) {
      suite.__selectAllTargetItems(e);
    });
  }

  this.__syncItems();

//  this.__srcList.enableHistoryTracking();
//  this.__tgtList.enableHistoryTracking();
}

uiOptionTransfer_Suite =
    uiUtil_Object.declareClass(uiOptionTransfer_Suite, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiOptionTransfer_Suite.prototype.__selectAllTargetItems = function(domEvent) {
  var listSize = this.__tgtList.size();
  for(var i = 0; i < listSize; i++) {
    this.__tgtHandler.setSelected(this.__tgtList.getItemAt(i), true, domEvent);
  }
};

uiOptionTransfer_Suite.prototype.__syncItems = function() {
  this.__srcList.enableOptionValueMapping();
  var listSize = this.__tgtList.size();
  for(var i = 0; i < listSize; ++i) {
    var tgtItem = this.__tgtList.getItemAt(i);
    var srcItem = this.__srcList.getItemByValue(tgtItem.value);
    if (srcItem != null) {
      this.__srcHandler.setDisabled(srcItem, true);
    }
  }
}

// Produces the javascript code for transferring items from source to
// target select box
uiOptionTransfer_Suite.prototype.__transfer = function(domEvent, all) {
  var listSize = this.__srcList.size();
  for(var i = 0; i < listSize; ++i) {
    var item = this.__srcList.getItemAt(i);
    // with IE, it is still possible to transfer disabled option,
    // so we have to make sure to only transfer a non-disabled ones
    if((all || this.__srcHandler.isSelected(item)) &&
        !this.__srcHandler.isDisabled(item) &&
        this.__find(this.__tgtList, item) < 0) {
      this.__transferItem(domEvent, item);
      if (!this.__srcList.getDomObject().multiple && !all) {
        var nextIndex = i + 1;
        if (nextIndex < listSize) {
          this.__srcList.setSelectedIndex(nextIndex);
        }
        // don't continue, with single item list, if the browser chooses
        // to select the subsequent item, it will also be transferred,
        // which is not what it is supposed to do since it wasn't selected
        return;
      }
    }
  }
};

// Puts an item into the target select box
uiOptionTransfer_Suite.prototype.__transferItem = function(domEvent, item) {
  var clone = this.__srcHandler.clone(item);

  // Disable the original item.
  this.__srcHandler.setDisabled(item, true);
  this.__srcHandler.setSelected(item, false, domEvent);

  this.__tgtList.addItem(clone);
  // Normally, transferred items should have already been selected (by
  // the user). However in "transfer all" mode, where the source items
  // are not necessarily selected, the following explicity item selection
  // is essential.
  this.__srcHandler.setSelected(clone, true, domEvent);
};

// Returns the location (index) of opt in list. If not found, -1 is returned.
uiOptionTransfer_Suite.prototype.__find = function(group, needle) {
  var wrapper = uiHtml_SelectOptionWrapper.getInstance();
  var listSize = group.size();
  for(var i = 0; i < listSize; ++i) {
    var item = group.getItemAt(i);
    if(item.value == needle.value && item.text == needle.text) {
      return i;
    }
  }
  return -1;
};

// Produces the javascript code for removing items from target select box
uiOptionTransfer_Suite.prototype.__return = function(domEvent, all) {
  var listSize = this.__tgtList.size();
  for (var i = listSize - 1; i >= 0; --i) {
    var item = this.__tgtList.getItemAt(i);
    if (all || this.__tgtHandler.isSelected(item)) {
      var index = this.__find(this.__srcList, item);
      // some target items don't exist in the source item collection
      if(index >= 0) {
        this.__returnItem(domEvent, this.__srcList.getItemAt(index), i);
      }
    }
  }
};

// Puts an item into the target select box
uiOptionTransfer_Suite.prototype.__returnItem = function(domEvent, sourceItem, targetIndex) {
  // reset source item's state
  this.__srcHandler.setDisabled(sourceItem, false);
  this.__srcHandler.setSelected(sourceItem, true, domEvent);
  this.__tgtList.removeItemAt(targetIndex);
};

uiOptionTransfer_Suite.prototype._setTransferTrigger = function(trigger) {
  var suite = this;
  trigger.appendEventHandler("click", function(e) {
    suite.__transfer(e, false);
  });
};

uiOptionTransfer_Suite.prototype._setReturnTrigger = function(trigger) {
  var suite = this;
  trigger.appendEventHandler("click", function(e) {
    suite.__return(e, false);
  });
};

uiOptionTransfer_Suite.prototype._setTransferAllTrigger = function(trigger) {
  var suite = this;
  trigger.appendEventHandler("click", function(e) {
    suite.__transfer(e, true);
  });
};

uiOptionTransfer_Suite.prototype._setReturnAllTrigger = function(trigger) {
  var suite = this;
  trigger.appendEventHandler("click", function(e) {
    suite.__return(e, true);
  });
};
