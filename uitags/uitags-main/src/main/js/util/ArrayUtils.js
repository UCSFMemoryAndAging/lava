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
 * @class A utility class that provides extra functionality to
 *     Javascript arrays.
 * @extends uiUtil_Object
 */
function uiUtil_ArrayUtils() {
  this._super();
}

uiUtil_ArrayUtils = uiUtil_Object.declareUtil(uiUtil_ArrayUtils, uiUtil_Object);

/**
 * Represents an invalid array index.
 * @type int
 */
uiUtil_ArrayUtils.INVALID_INDEX = -1;


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * Returns the index of a certain item in an array.
 *
 * @param {Array} array the array
 * @param {Object} item the item to search
 * @param {uiUtil_Type.EqualityTester} optEqualityTester the optional
 *     item equality tester
 * @return the index if the item was found, uiUtil_ArrayUtils.INVALID_INDEX
 *     otherwise
 * @type int
 */
uiUtil_ArrayUtils.indexOf = function(array, item, optEqualityTester) {
  var equalityTester = uiUtil_Type.getEqualityTester(optEqualityTester);
  for (var i = 0; i < array.length; ++i) {
    if (equalityTester.equals(array[i], item)) {
      return i;
    }
  }
  return uiUtil_ArrayUtils.INVALID_INDEX;
};

/**
 * Checks whether an array contains a certain item.
 *
 * @param {Array} array the array
 * @param {Object} item the item to search
 * @param {uiUtil_Type.EqualityTester} optEqualityTester the optional
 *     item equality tester
 * @return <code>true</code> if the item was found, <code>false</code>
 *      otherwise
 * @type boolean
 */
uiUtil_ArrayUtils.contains = function(array, item, optTester) {
  var index = uiUtil_ArrayUtils.indexOf(array, item, optTester);
  return index != uiUtil_ArrayUtils.INVALID_INDEX;
};

/**
 * Returns an item located as the specified index in the array. If the
 * provided <code>index</code> is negative, the item is search backward
 * (starting from index <code>array.length</code>).
 *
 * @param {Array} array the array
 * @param {int} index index of the item
 * @return the found item
 * @throws uiUtil_IllegalArgumentException if index is larger than
 *     <code>array.length</code>
 * @type Object
 */
uiUtil_ArrayUtils.get = function(array, index) {
  if (index >= array.length) {
    throw new uiUtil_IllegalArgumentException("Index out of bound: " + index);
  }
  if (index >= 0) {
    return array[index];
  }
  return array[array.length + index];  // search backward if index is negative
};

/**
 * Clears all items in an array.
 *
 * @param {Array} array the array
 */
uiUtil_ArrayUtils.clear = function(array) {
  while (array.length > 0) {
    array.pop();
  }
};

/**
 * Appends an item to an array only if the item does not exist in the array.
 *
 * @param {Array} array the array
 * @param {Object} item the item to append
 * @param {uiUtil_Type.EqualityTester} optEqualityTester item equality tester
 */
uiUtil_ArrayUtils.addUnique = function(array, item, optEqualityTester) {
  if (!uiUtil_ArrayUtils.contains(array, item, optEqualityTester)) {
    array.push(item);
  }
};

/**
 * Removes the first item in an array.
 *
 * @param {Array} array the array
 * @param {Object} item the item to remove
 * @param {uiUtil_Type.EqualityTester} optEqualityTester the optional
 *     item equality tester
 */
uiUtil_ArrayUtils.removeFirst = function(array, item, optEqualityTester) {
  var index = uiUtil_ArrayUtils.indexOf(array, item, optEqualityTester);
  if (index != uiUtil_ArrayUtils.INVALID_INDEX) {
    uiUtil_ArrayUtils.removeAt(array, index, 1);
  }
};

/**
 * Removes the one or more items located at a specified index in an array.
 *
 * @param {Array} array the array
 * @param {int} index the specified index
 * @param {int} optCount the number of items to remove starting from the
 *     specified index, which default to 1
 */
uiUtil_ArrayUtils.removeAt = function(array, index, optCount) {
  var count = ((uiUtil_Type.isInt(optCount)) ? optCount : 1);
  if (index >= 0) {
    array.splice(index, count);
  }
  else {  // in reverse
    array.splice(array.length + index - count + 1, count);
  }
};

/**
 * Sorts all items in an array in the order specified by either the
 * <code>optComparator</code> or a default comparator.
 *
 * @param {Array} array the array
 * @param {uiUtil_Type.Comparator} optComparator the optional comparator
 */
uiUtil_ArrayUtils.sort = function(array, optComparator) {
  var comparator = uiUtil_Type.getComparator(optComparator);
  array.sort(comparator.compare);
};

/**
 * Reverses the order of all items in an array as specified by either the
 * <code>optComparator</code> or a default comparator.
 *
 * @param {Array} array the array
 * @param {uiUtil_Type.Comparator} optComparator the optional comparator
 */
uiUtil_ArrayUtils.reverse = function(array, optComparator) {
  var comparator = uiUtil_Type.getComparator(optComparator);
  array.reverse(comparator.compare);
};

/**
 * Converts a non-array collection to an array. If the provided
 * <code>collection</code> is already an <code>Array</code>, it
 * will just be returned without modification.
 *
 * @param {Collection} collection the collection
 * @return either the original array or the converted array
 * @type Array
 */
uiUtil_ArrayUtils.toArrayIfNotAlready = function(collection) {
  if (collection instanceof Array) {
    return collection;
  }
  if (uiUtil_Type.isDefined(collection) &&
      uiUtil_Type.isDefined(collection[0]) &&
      uiUtil_Type.isInt(collection.length)) {
    var array = new Array(collection.length);
    for (var i = 0; i < collection.length; ++i) {
      array[i] = collection[i];
    }

    return array;
  }
  throw new uiUtil_IllegalArgumentException(
      "The provided argument is not a valid collection");
};

/**
 * Returns the string representation of an array, which includes all
 * items in the array separated by comma.
 *
 * @param {Array} array the array
 * @return the string
 * @type String
 */
uiUtil_ArrayUtils.toString = function(array) {
  var string = array.getClassName() + "{";
  if (array.length > 0) {
    string += array[0];
    for (var i = 1; i < array.length; ++i) {
      string += ", " + array[i];
    }
  }
  return string + "}";
};
