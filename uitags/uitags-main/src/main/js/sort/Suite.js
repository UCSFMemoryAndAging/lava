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

function uiSort_Suite(list) {
  this._super();

  this.__list = list;
  this.__comparatorFunction = uiSort_Suite.
      OptionTextCaseInsensitiveComparator.prototype.compare;

//  this.__list.enableHistoryTracking();
}

uiSort_Suite = uiUtil_Object.declareClass(uiSort_Suite, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiSort_Suite.prototype._initAscendingTrigger = function(trigger) {
  this.__ascendingTrigger = trigger;

  var suite = this;
  this.__ascendingTrigger.appendEventHandler("click", function(e) {
    suite.__sortAsc();
  });
};

uiSort_Suite.prototype._initDescendingTrigger = function(trigger) {
  this.__descendingTrigger = trigger;

  var suite = this;
  this.__descendingTrigger.appendEventHandler("click", function(e) {
    suite.__sortDesc();
  });
};

uiSort_Suite.prototype.__sortAsc = function() {
  this.__list.sortItems(this.__comparatorFunction, false);
};

uiSort_Suite.prototype.__sortDesc = function() {
  this.__list.sortItems(this.__comparatorFunction, true);
};


///////////////////////////////////
////////// Inner Classes //////////
///////////////////////////////////

function uiSort_Suite$OptionTextCaseInsensitiveComparator() {
}

uiSort_Suite.OptionTextCaseInsensitiveComparator =
    uiUtil_Object.declareClass(uiSort_Suite$OptionTextCaseInsensitiveComparator, uiUtil_Object);

uiSort_Suite.OptionTextCaseInsensitiveComparator.prototype.compare = function(first, second) {
  var handler = uiHtml_SelectOptionWrapper.getInstance();
  var str1 = first.text.toLowerCase();
  var str2 = second.text.toLowerCase();
  if(str1 == str2) {
    return 0;
  }
  return (str1 > str2) ? 1 : -1;
};
