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

/**
 * This class is uninstantiable.
 *
 * @class A utility class for dealing with the different types in Javascript.
 * @extends uiUtil_Object
 */
function uiUtil_Type() {
  this._super();
}

uiUtil_Type = uiUtil_Object.declareUtil(uiUtil_Type, uiUtil_Object);


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * Checks if a variable is defined. This only checks whether a variable
 * has been initialized. Whereas, a variable with <code>null</code> value
 * is still considered initialized/defined.
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the variable is initialized/defined,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isDefined = function(value) {
  return ("undefined" != (typeof value));
};

/**
 * Checks if a variable is referencing to a function.
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the value is a function,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isFunction = function(value) {
  return ("function" == (typeof value));
};

/**
 * Checks if a variable is referencing to an object. This returns
 * <code>true</code> if value is a browser specific object (anonymous object).
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the value is an object,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isObject = function(value) {
  return ("object" == (typeof value));
};

/**
 * Checks if a variable is referencing to a string.
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the value is a string,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isString = function(value) {
  return ("string" == (typeof value));
};

/**
 * Checks if a variable contains a boolean value.
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the value is a boolean,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isBoolean = function(value) {
  return ("boolean" == (typeof value));
};

/**
 * Checks if a variable contains a valid number, which include both whole
 * and decimal number.
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the value is a valid number,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isNumber = function(value) {
  return ("number" == (typeof value)) && !isNaN(value);
};

/**
 * Checks if a variable contains a valid whole number.
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the value is a valid whole number,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isInt = function(value) {
  if (!uiUtil_Type.isNumber(value)) {
    return false;
  }
  // if it is a whole number, it does not have a dot sign
  return (new String(value).indexOf('.') < 0);
};

/**
 * Checks if a variable contains a valid decimal number.
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the value is a valid decimal number,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isDouble = function(value) {
  if (!uiUtil_Type.isNumber(value)) {
    return false;
  }
  // if it is a decimal, it has a dot sign
  return (new String(value).indexOf('.') >= 0);
};

/**
 * Returns a string representation of the variable's type.
 *
 * @param {Object} value the variable to evaluate
 * @return the string
 * @type String
 */
uiUtil_Type.getTypeName = function(value) {
  if (!uiUtil_Type.isDefined(value)) {
    return "undefined";
  }
  // NOTE: Real undefined variable also passes against null, thus we have
  //       to check for undefined variable before checking against null.
  else if (value == null) {
    return "null";
  }
  // NOTE: In Firefox 1, a node is also an object, so better to have this
  //       condition before isObject.
  //       A node is not a function, thus should test this condition
  //       before the isFunction
  else if (uiUtil_Type.isHtmlNode(value)) {
    return value.nodeName.toLowerCase();
  }
  if (!uiUtil_Type.isFunction(value)) {
    return (typeof value);
  }
  // an object, returns its class name
  return uiUtil_Object.getClassName(value);
};

/**
 * Checks if a variable is referencing to an HTML node, which include
 * both element and text node.
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the value is an HTML node,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isHtmlNode = function(value) {
  return value.nodeName != null;
};

/**
 * Checks if a variable is referencing to an HTML element node.
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the value is an HTML element node,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isElementNode = function(value) {
  return value.nodeType == 1;
};

/**
 * Checks if a variable is referencing to an HTML text node.
 *
 * @param {Object} value the variable to evaluate
 * @return <code>true</code> if the value is an HTML text node,
 *     <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Type.isTextNode = function(value) {
  return value.nodeType == 3;
};

/**
 * Returns the provided <code>value</code> if it is not void, otherwise the
 * <code>defaultValue</code> will be returned.
 *
 * @param {Object} value the provided value
 * @param {Object} defaultValue the default value
 * @return either <code>value</code> or <code>defaultValue</code>
 * @type Object
 */
uiUtil_Type.getValue = function(value, defaultValue) {
  if (value == null) {
    return defaultValue;
  }
  return value;
};

/**
 * Returns the provided string <code>value</code> if it is not void,
 * otherwise the <code>defaultValue</code> will be returned.
 *
 * @param {String} value the provided string
 * @param {String} defaultValue the default value
 * @throws uiUtil_IllegalArgumentException if value is not a
 *     <code>String</code>
 * @return either <code>value</code> or <code>defaultValue</code>
 * @type String
 */
uiUtil_Type.getString = function(value, defaultValue) {
  if (uiUtil_Type.isString(value)) {
    return value;
  }
  if (uiUtil_Type.isDefined(value)) {  // that means wrong type!
    throw new uiUtil_IllegalArgumentException(
        "Invalid string value: " + value);
  }
  // It is the responsibility of the caller to pass the correct default value.
  return defaultValue;
};

/**
 * Returns the provided boolean <code>value</code> if it is not void,
 * otherwise the <code>defaultValue</code> will be returned.
 *
 * @param {boolean} value the provided boolean
 * @param {boolean} defaultValue the default value
 * @throws uiUtil_IllegalArgumentException if value is not a
 *     <code>boolean</code>
 * @return either <code>value</code> or <code>defaultValue</code>
 * @type boolean
 */
uiUtil_Type.getBoolean = function(value, defaultValue) {
  if (uiUtil_Type.isBoolean(value)) {
    return value;
  }
  if (uiUtil_Type.isDefined(value)) {  // that means wrong type!
    throw new uiUtil_IllegalArgumentException(
        "Invalid boolean value: " + value);
  }
  // It is the responsibility of the caller to pass the correct default value.
  return defaultValue;
};

/**
 * Returns the provided integer <code>value</code> if it is not void,
 * otherwise the <code>defaultValue</code> will be returned.
 *
 * @param {int} value the provided integer
 * @param {int} defaultValue the default value
 * @throws uiUtil_IllegalArgumentException if value is not an
 *     <code>int</code>
 * @return either <code>value</code> or <code>defaultValue</code>
 * @type int
 */
uiUtil_Type.getInt = function(value, defaultValue) {
  if (uiUtil_Type.isInt(value)) {
    return value;
  }
  if (uiUtil_Type.isDefined(value)) {  // that means wrong type!
    throw new uiUtil_IllegalArgumentException(
        "Invalid integer value: " + value);
  }
  // It is the responsibility of the caller to pass the correct default value.
  return defaultValue;
};

/**
 * Returns the default implementation of type comparator if the provided
 * <code>optComparator</code> is void.
 *
 * @param {uiUtil_Type.Comparator} optComparator the provided comparator
 * @return either <code>optComparator</code> or a default comparator
 * @type uiUtil_Type.Comparator
 */
uiUtil_Type.getComparator = function(optComparator) {
  if (optComparator == null) {
    return new uiUtil_Type.Comparator();
  }
  return optComparator;
};

/**
 * Returns the default implementation of type equality tester if the provided
 * <code>optTester</code> is void.
 *
 * @param {uiUtil_Type.EqualityTester} optTester the provided tester
 * @return either <code>optTester</code> or a default equality tester
 * @type uiUtil_Type.EqualityTester
 */
uiUtil_Type.getEqualityTester = function(optTester) {
  if (optTester == null) {
    return new uiUtil_Type.EqualityTester();
  }
  return optTester;
};


///////////////////////////////////
////////// Inner Classes //////////
///////////////////////////////////

/**
 * @class A default equality tester strategy. To create a custom
 *     strategy, subclass this default one.
 * @extends uiUtil_Object
 */
function uiUtil_Type$EqualityTester() {
  this._super();
}

/** @ignore */
uiUtil_Type.EqualityTester = uiUtil_Object.declareClass(
    uiUtil_Type$EqualityTester, uiUtil_Object, false);

/**
 * Checks whether two objects are equal.
 *
 * @param {Object} first the first object
 * @param {Object} second the second object
 * @return <code>true</code> if the two objects are equal, <code>false</code>
 *     otherwise
 * @type boolean
 */
uiUtil_Type.EqualityTester.prototype.equals = function(first, second) {
  return first == second;
};



/**
 * @class A default comparator strategy. To create a custom strategy,
 *     subclass this default one.
 * @extends uiUtil_Object
 */
function uiUtil_Type$Comparator() {
  this._super();
}

/** @ignore */
uiUtil_Type.Comparator = uiUtil_Object.declareClass(
    uiUtil_Type$Comparator, uiUtil_Object, false);

/**
 * Compares two objects and returns their relative distance value.
 *
 * @param {Object} first the first object
 * @param {Object} second the second object
 * @return negative integer if the first is less that the second object,
 *     positive if the first object is greater than the second one, or
 *     zero if they are equal
 * @type int
 */
uiUtil_Type.Comparator.prototype.compare = function(first, second) {
  if (first < second) {
    return -1;
  }
  else if (first > second) {
    return 1;
  }
  return 0;
};
