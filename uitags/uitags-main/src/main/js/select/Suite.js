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

function uiSelect_Suite(selectableList) {
  this._super();

  this.__list = selectableList;
  this.__itemHandler = selectableList.getItemHandler();

  if (this.__list instanceof uiHtml_Select) {
//    this.__list.enableHistoryTracking();
  }
}

uiSelect_Suite = uiUtil_Object.declareClass(uiSelect_Suite, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiSelect_Suite.prototype._initAllNoneToggle = function(toggle) {
  this.__allNoneToggle = toggle;

  var suite = this;
  this.__allNoneToggle.appendOnStateOn(function(e) {
    suite.__selectAll(e);
  });
  this.__allNoneToggle.appendOnStateOff(function(e) {
    suite.__selectNone(e);
  });
};

uiSelect_Suite.prototype._initAllTrigger = function(trigger) {
  this.__allTrigger = trigger;

  var suite = this;
  this.__allTrigger.appendEventHandler("click", function(e) {
    suite.__selectAll(e);
  });
};

uiSelect_Suite.prototype._initNoneTrigger = function(trigger) {
  this.__noneTrigger = trigger;

  var suite = this;
  this.__noneTrigger.appendEventHandler("click", function(e) {
    suite.__selectNone(e);
  });
};

uiSelect_Suite.prototype._initInverseTrigger = function(trigger) {
  this.__inverseTrigger = trigger;

  var suite = this;
  this.__inverseTrigger.appendEventHandler("click", function(e) {
    suite.__selectInverse(e);
  });
};

uiSelect_Suite.prototype._initRangeTrigger = function(trigger) {
  this.__rangeTrigger = trigger;

  var suite = this;
  this.__rangeTrigger.appendEventHandler("click", function(e) {
    suite.__selectRange(e);
  });
};

// Toggles selection of supplied selectable elements on and off alternately.
uiSelect_Suite.prototype.__selectAll = function(domEvent) {
  var listSize = this.__list.size();
  for(var i = 0; i < listSize; i++) {
    var item = this.__list.getItemAt(i);
    this.__itemHandler.setSelected(item, true, domEvent);
  }
};

uiSelect_Suite.prototype.__selectNone = function(domEvent) {
  var listSize = this.__list.size();
  for(var i = 0; i < listSize; ++i) {
    var item = this.__list.getItemAt(i);
    this.__itemHandler.setSelected(item, false, domEvent);
  }
};

// Inverts the value of the selectable elements' "selected" status.
uiSelect_Suite.prototype.__selectInverse = function(domEvent) {
  var listSize = this.__list.size();
  for(var i = 0; i < listSize; ++i) {
    var item = this.__list.getItemAt(i);
    if (this.__itemHandler.isSelected(item)) {
      this.__itemHandler.setSelected(item, false, domEvent);
    }
    else {
      this.__itemHandler.setSelected(item, true, domEvent);
    }
  }
};

// Checks elements that are between a pair of selected ones.
uiSelect_Suite.prototype.__selectRange = function(domEvent) {
  var nowSelecting = false;
  var listSize = this.__list.size();

  for (var i = 0; i < listSize; ++i) {
    var currItem = this.__list.getItemAt(i);
    var nextItem = this.__list.getItemAt(i + 1);

    // Do stuff at the end of a series of selected elements
    if (this.__itemHandler.isSelected(currItem) &&
        (i + 1 < listSize) && !this.__itemHandler.isSelected(nextItem)) {
      nowSelecting = !nowSelecting;

      // Find the match for current pair
      var endPairIndex = -1;
      for (var j = i + 2; j < listSize; ++j) {
        var item = this.__list.getItemAt(j);
        if (this.__itemHandler.isSelected(item)) {
          endPairIndex = j;
          break;
        }
      }

      // If current pair is unmatched, return
      if (endPairIndex == -1) {
        return;
      }

      if (nowSelecting) {
        for (var j=i+1; j < endPairIndex; j++) {
          var item = this.__list.getItemAt(j);
          this.__itemHandler.setSelected(item, true, domEvent);
        }
      }

      i = endPairIndex - 1;
    }
  }
};
