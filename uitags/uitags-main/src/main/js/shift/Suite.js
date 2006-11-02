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

function uiShift_Suite(list) {
  this._super();

  this.__list = list;
  this.__handler = this.__list.getItemHandler();

//  this.__list.enableHistoryTracking();
}

uiShift_Suite = uiUtil_Object.declareClass(uiShift_Suite, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiShift_Suite.prototype._initUpTrigger = function(trigger) {
  this.__upTrigger = trigger;

  var suite = this;
  this.__upTrigger.appendMousePressHandler(function(e) {
    suite.__shiftUp();
  });
};

uiShift_Suite.prototype._initDownTrigger = function(trigger) {
  this.__downTrigger = trigger;

  var suite = this;
  this.__downTrigger.appendMousePressHandler(function(e) {
    suite.__shiftDown();
  });
};

uiShift_Suite.prototype._initFirstTrigger = function(trigger) {
  this.__firstTrigger = trigger;

  var suite = this;
  this.__firstTrigger.appendEventHandler("mousedown", function(e) {
    suite.__shiftFirst();
  });
};

uiShift_Suite.prototype._initLastTrigger = function(trigger) {
  this.__lastTrigger = trigger;

  var suite = this;
  this.__lastTrigger.appendEventHandler("mousedown", function(e) {
    suite.__shiftLast();
  });
};

uiShift_Suite.prototype.__shiftItemUp = function(index) {
  var shiftedUp = this.__handler.clone(this.__list.getItemAt(index));
  var shiftedDown = this.__list.getItemAt(index - 1);
  this.__list.setItemAt(index - 1, shiftedUp);
  this.__list.setItemAt(index, shiftedDown);
};

uiShift_Suite.prototype.__shiftUp = function() {
  var num = 1;
  if (this.__distanceText != null) {
    num = parseInt(this.__distanceText.getDomObject().value);
    if(isNaN(num)) {
      return;
    }
  }

  var listSize = this.__list.size();
  // NOTE: The first iteration is just to get all selected items. The
  //       shifting will be done in another iteration since in Opera 8 & 9,
  //       shifting an item causes other selected items to be unselected.
  //       Another advantage of doing this is that in IE 6, shifting a
  //       selected item takes longer because the scrollbar keeps going
  //       back and forth (between the top of the box and the position of
  //       the shifted item).
  var selectedIndices = new Array();
  // NOTE: The first item should also be processed (i = 0) in order to help
  //       determining whether the other selected items should be shifted
  //       or not (when the first item is selected, the whole group already
  //       hits the boundary, and thus shouldn't be shifted).
  for (var i = 0; i < listSize; ++i) {
    var item = this.__list.getItemAt(i);
    if (this.__handler.isSelected(item)) {
      selectedIndices.push(i);
    }
  }

  for (var i = 0; i < selectedIndices.length; ++i) {
    var index = selectedIndices[i];

    if(index - num < 0) {
      num = index;
      break;
    }
    else {
      this.__unselect(this.__list.getItemAt(index));

      for(var j = 0; j < num; ++j) {
        this.__shiftItemUp(index - j);
      }
      selectedIndices[i] = index - num;
    }
  }

  // NOTE: "num" could be zero when the top most selected item is already
  //       at the top of the box, in which case we don't want to try
  //       selecting the item, which could mess up the shifting.
  if (num > 0) {
    this.__selectItems(selectedIndices);
  }
};

// NOTE: the algorithm/behavour differs slightly from shiftUp() on
//   multiple items
uiShift_Suite.prototype.__shiftFirst = function() {
  var listSize = this.__list.size();
  // NOTE: The first iteration is just to get all selected items. The
  //       shifting will be done in another iteration since in Opera 8 & 9,
  //       shifting an item causes other selected items to be unselected.
  //       Another advantage of doing this is that in IE 6, shifting a
  //       selected item takes longer because the scrollbar keeps going
  //       back and forth (between the top of the box and the position of
  //       the shifted item).
  var selectedIndices = new Array();
  for(var i = 0; i < listSize; ++i) {
    var item = this.__list.getItemAt(i);
    if (this.__handler.isSelected(item)) {
      selectedIndices.push(i);
    }
  }

  var numPushed = 0;
  for (var i = 0; i < selectedIndices.length; ++i) {
    var index = selectedIndices[i];
    this.__unselect(this.__list.getItemAt(index));

    var num = index - numPushed;
    for(var j = 0; j < num; ++j) {
      this.__shiftItemUp(index - j);
      selectedIndices[i] = numPushed;
    }
    ++numPushed;
  }

  this.__selectItems(selectedIndices);
  this.__list.scrollToTop();
};

// Selects an item, but does not trigger the select's onchange event.
uiShift_Suite.prototype.__select = function(item) {
  item.selected = true;
};

// Unselects an item, but does not trigger the select's onchange event.
uiShift_Suite.prototype.__unselect = function(item) {
  item.selected = false;
};

uiShift_Suite.prototype.__shiftItemDown = function(index) {
  var shiftedDown = this.__handler.clone(this.__list.getItemAt(index));
  var shiftedUp = this.__list.getItemAt(index + 1);
  this.__list.setItemAt(index + 1, shiftedDown);
  this.__list.setItemAt(index, shiftedUp);
};

uiShift_Suite.prototype.__shiftDown = function() {
  var num = 1;
  if (this.__distanceText != null) {
    num = parseInt(this.__distanceText.getDomObject().value);
    if(isNaN(num)) {
      return;
    }
  }

  // NOTE: The first iteration is just to get all selected items. The
  //       shifting will be done in another iteration since in Opera 8 & 9,
  //       shifting an item causes other selected items to be unselected.
  //       Another advantage of doing this is that in IE 6, shifting a
  //       selected item takes longer because the scrollbar keeps going
  //       back and forth (between the top of the box and the position of
  //       the shifted item).
  var selectedIndices = new Array();
  var listSize = this.__list.size();
  // NOTE: The last item (i = lastIndex) should also be processed in order to
  //       help determining whether the other selected items should be shifted
  //       or not (when the last item is selected, the whole group already
  //       hits the boundary, and thus shouldn't be shifted).
  var lastIndex = listSize - 1;
  for(var i = lastIndex; i >= 0; --i) {
    var item = this.__list.getItemAt(i);
    if (this.__handler.isSelected(item)) {
      selectedIndices.push(i);
    }
  }

  for (var i = 0; i < selectedIndices.length; ++i) {
    var index = selectedIndices[i];

    if(index + num > lastIndex) {
      num = lastIndex - index;
      break;
    }
    else {
      this.__unselect(this.__list.getItemAt(index));

      for(var j = 0; j < num; ++j) {
        this.__shiftItemDown(index + j);
      }
      selectedIndices[i] = index + num;
    }
  }

  // NOTE: "num" could be zero when the top most selected item is already
  //       at the top of the box, in which case we don't want to try
  //       selecting the item, which could mess up the shifting.
  if (num > 0) {
    this.__selectItems(selectedIndices);
  }
};

// NOTE: the algorithm/behaviour differs slightly from shiftDown() on
//   multiple items
uiShift_Suite.prototype.__shiftLast = function() {
  var listSize = this.__list.size();
  // NOTE: The first iteration is just to get all selected items. The
  //       shifting will be done in another iteration since in Opera 8 & 9,
  //       shifting an item causes other selected items to be unselected.
  //       Another advantage of doing this is that in IE 6, shifting a
  //       selected item takes longer because the scrollbar keeps going
  //       back and forth (between the top of the box and the position of
  //       the shifted item).
  var selectedIndices = new Array();
  for(var i = listSize - 1; i >= 0; --i) {
    var item = this.__list.getItemAt(i);
    if (this.__handler.isSelected(item)) {
      selectedIndices.push(i);
    }
  }

  var numPushed = 0;
  for (var i = 0; i < selectedIndices.length; ++i) {
    var index = selectedIndices[i];
    this.__unselect(this.__list.getItemAt(index));

    var last = listSize - 1;
    var num = last - index - numPushed;
    for(var j = 0; j < num; ++j) {
      this.__shiftItemDown(index + j);
      selectedIndices[i] = last - numPushed;
    }
    ++numPushed;
  }

  this.__selectItems(selectedIndices);
  this.__list.scrollToBottom();
};

uiShift_Suite.prototype.__selectItems = function(selectedIndices) {
  if (uiHtml_Window.getInstance().isOpera()) {
    // NOTE: In Opera 9, since there seems to be rendering problem where
    //       other items also get selected when an item is selected just
    //       after it is shifted, we need to use the timer to create a very
    //       short delay.
    var suite = this;
    window.setTimeout(function(e) {
      suite.__implSelectItems(selectedIndices);
    }, 0);
  }
  else {
    this.__implSelectItems(selectedIndices);
  }
}

uiShift_Suite.prototype.__implSelectItems = function(selectedIndices) {
  for(var i = 0; i < selectedIndices.length; ++i) {
    this.__select(this.__list.getItemAt(selectedIndices[i]));
  }
}
