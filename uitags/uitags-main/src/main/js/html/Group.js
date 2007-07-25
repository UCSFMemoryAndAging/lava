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

// @param domObjects either an array or a browser's native collection.
function uiHtml_Group(domObjects, optHandler) {
  this._super();
  this.__items = domObjects;
  this.__handler = optHandler;

  // (ctoohey) used to distinguish uiHtml_Select from uiHtml_Group when returned by uiHtml_Group.createByEither
  this.__type = "group";

  if (this.__handler == null) {
    // NOTE: in order for this to work, the collection should contain
    //       at least one item
    var firstItem = domObjects[0];
    if (firstItem == null) {
      // this violation could happen if a select does not contain any option
      throw new uiUtil_IllegalStateException(
          "A group should contain at least one item.");
    }

    // NOTE: list sometimes contains a lot of items, in which case it is
    //       highly inefficient to create objects for wrapping each,
    //       therefore it is better to get a handler for all of them
    //
    // assume that the rest of the elements are of the same type
    this.__handler = uiHtml_Group.__getWrapper(firstItem);
  }
}

uiHtml_Group = uiUtil_Object.declareClass(uiHtml_Group, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiHtml_Group.prototype.appendEventHandler = function(event, handler) {
  for (var i = 0; i < this.__items.length; ++i) {
    this.__handler.appendEventHandler(this.__items[i], event, handler);
  }
};

uiHtml_Group.prototype.executeAggregateEventHandler = function(event, optDomEvent) {
  for (var i = 0; i < this.__items.length; ++i) {
    this.__handler.executeAggregateEventHandler(this.__items[i], event, optDomEvent);
  }
};

uiHtml_Group.prototype.__constructItemArgs = function(item, args) {
  var argArray = new Array();
  var outputIndex = 0;
  argArray[outputIndex++] = item;

  var start = args.callee.length - 1;
  for (var i = start; i < args.length; ++i) {
    argArray[outputIndex++] = args[i];
  }
  return argArray;
};

uiHtml_Group.prototype.traverse = function(
    handlerOwner, handlerFunction, optArgN) {
  var argArray = this.__constructItemArgs(null, arguments);

  for (var i = 0; i < this.__items.length; ++i) {
    argArray[0] = this.__items[i];
    handlerFunction.apply(handlerOwner, argArray);
  }
};

uiHtml_Group.prototype.getItemHandler = function() {
  return this.__handler;
};

uiHtml_Group.prototype.size = function() {
  return this.__items.length;
};

uiHtml_Group.prototype.getItemAt = function(index) {
  return this.__items[index];
};

uiHtml_Group.prototype.setItemAt = function(index, item) {
  this.__items[index] = item;
};

uiHtml_Group.prototype.addItem = function(item) {
  throw new Exception("Operation not supported by super class. " +
      "This is an optional operation of the subclass.");
};

// Returns an array of items in the group.
uiHtml_Group.prototype._getItems = function() {
  return this.__items;
};

uiHtml_Group.prototype.hasValue = function(value) {
  for (var i = 0; i < this.__items.length; ++i) {
    if (this.__handler.getLogicalValue(this.__items[i]) == value) {
      return true;
    }
  }

  return false;
};

// author:ctoohey
// observe and ignore child tags matching their forValue is done via
// a regular expression match
uiHtml_Group.prototype.hasRegExpValue = function(forValue) {
  var forValueRegExp = new RegExp(forValue);
  // note: the __items array contains multiple items for selectboxes and
  //       radiobutton groups, but for all other types of element this array
  //       just contains a single widget
  // note: the forValue=null situation has already been handled in Rule._holds,
  //       so know that forValue is not null at this point, meaning that if
  //       this group represents selectible widgets (i.e. select options, 
  //       radio buttons, checkboxes) that at least one of them is selected.
  //       thus, must iterate thru all of the selectible items to make sure none
  //       of them matches before returning false, but as soon as there is a match,
  //       return true.
  for (var i = 0; i < this.__items.length; ++i) {
    var logicalValue = this.__handler.getLogicalValue(this.__items[i]);
    if (logicalValue != null && logicalValue.match(forValueRegExp)) {
      return true;
    }
  }

  return false;
};

uiHtml_Group.prototype.getValues = function() {
  var values = new Array();
  for (var i = 0; i < this.__items.length; ++i) {
    var value = this.__handler.getLogicalValue(this.__items[i]);
    if (value != null) {
      values.push(value);
    }
  }

  return values;
};

uiHtml_Group.prototype.handleItemAtIndex = function(
    index, owner, handler, optArgN) {
  var argArray = this.__constructItemArgs(this.__items[index], arguments);
  return handler.apply(owner, argArray);
};

uiHtml_Group.prototype.handleItemWithValue = function(
    value, owner, handler, optArgN) {
  for (var i = 0; i < this.__items.length; ++i) {
    if (this.__items[i].value == value) {
      var argArray = this.__constructItemArgs(this.__items[i], arguments);
      return handler.apply(owner, argArray);
    }
  }
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

// Returns either a browser's native collection of multiple dom objects
// or an array of objects, thus allowing either to be accessed in a
// similar manner.
// The browser specific collection doesn't get converted to array, since
// it could impose big performance impact.
uiHtml_Group.__getDomObjectsByEither = function(id, name) {
  var domObjects;
  var domObject = uiHtml_Document.getInstance().getDomObjectById(id, false);
  if (domObject == null) {
    // either id was not specified, or must be name of a radio button group, need to use
    // getDomObjectsByName (all radio buttons have the same name)
    if (name == null || name.length == 0) {
      // special handling for LavaWeb component handler elementId which represents radio button group
      // need to construct the element name for the radio button group, which is of the format:
      // "component['COMPONENT_NAME'].PROPERTY_NAME

      // parse out component name from id
      var endComponent = id.indexOf("_");
      var component = id.substring(0, endComponent);
      // parse out property name from id
      var property = id.substring(endComponent + 1);
      
      name = "components['" + component + "']." + property;
    }
    domObjects = uiHtml_Document.getInstance().getDomObjectsByName(name, false);
  }
  else {
    domObjects = new Array(domObject);
  }

  // note that getElementsByName() always return a collection, so
  // it's best to check for the first item in the collection
  if (domObjects[0] == null) {
    throw new uiUtil_IllegalArgumentException(
        "Invalid element ID: " + id + " and name: " + name);
  }
  return domObjects;
};

uiHtml_Group.createByEither = function(id, name) {
  // if both id and name are specified, first the id is used to attempt to locate the 
  //  HTML element, and if not found, then the name is used
  // if no component is specified in the tag and elementId is specifed, id is equal to
  //  elementId 
  // if a component and elementId are specified in the tag, is component_elementId, e.g. 
  // e.g. if elementId="noChgImp" component="instrument", then id="instrument_noChgImp"
  // if elementName is specifed in the tag, name is equal to elementName, and component
  //  is ignored
  var domObjects = uiHtml_Group.__getDomObjectsByEither(id, name);

  try {
    // it's not possible to have multiple select boxes with the same name
    // so we assume that 'obj' would never be an array of 'select'
    if (uiHtml_Element.getWidgetType(domObjects[0]) == "select") {
      return new uiHtml_Select(domObjects[0]);
    }
    else {
      return new uiHtml_Group(domObjects);
    }
  }
  catch (e) {
    // provide more detailed info
    if (e instanceof uiUtil_Exception) {
      throw new uiUtil_CreateException("[" + name + "] " + e.getMessage());
    }
  }
};

uiHtml_Group.__getWrapper = function(domWidget) {
  switch(uiHtml_Element.getWidgetType(domWidget)) {
    case "checkbox" :
        return uiHtml_CheckBoxWrapper.getInstance();
    case "radio"    :
        return uiHtml_RadioWrapper.getInstance();
    default         :
        return uiHtml_ElementWrapper.getInstance();
  }
};

