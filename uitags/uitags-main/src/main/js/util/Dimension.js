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
 * Creates a wrapper for dimension properties of a rectangle. This class
 * does not require any particular dimensional unit, however all the
 * provided properties have to be of the same unit.
 *
 * @class Wraps all dimension properties of a rectangle.
 * @extends uiUtil_Object
 * @param {int} left the left most point
 * @param {int} top the top most point
 * @param {int} width the width of the rectangle
 * @param {int} height the height of the rectangle
 */
function uiUtil_Dimension(left, top, width, height) {
  this._super();
  /**
   * @type int
   * @private
   */
  this.__left = left;
  /**
   * @type int
   * @private
   */
  this.__top = top;
  /**
   * @type int
   * @private
   */
  this.__width = width;
  /**
   * @type int
   * @private
   */
  this.__height = height;
  /**
   * @type int
   * @private
   */
  this.__right = null;
  /**
   * @type int
   * @private
   */
  this.__bottom = null;
}

uiUtil_Dimension = uiUtil_Object.declareClass(uiUtil_Dimension, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Returns the width of the rectangle.
 *
 * @return the width value
 * @type int
 */
uiUtil_Dimension.prototype.getWidth = function() {
  return this.__width;
};

/**
 * Returns the height of the rectangle.
 *
 * @return the height value
 * @type int
 */
uiUtil_Dimension.prototype.getHeight = function() {
  return this.__height;
};

/**
 * Returns the left most point.
 *
 * @return the unit value
 * @type int
 */
uiUtil_Dimension.prototype.getLeft = function() {
  return this.__left;
};

/**
 * Returns the right most point.
 *
 * @return the unit value
 * @type int
 */
uiUtil_Dimension.prototype.getRight = function() {
  if (this.__right == null) {
    this.__right = this.__left + this.__width;
  }
  return this.__right;
};

/**
 * Returns the top most point.
 *
 * @return the unit value
 * @type int
 */
uiUtil_Dimension.prototype.getTop = function() {
  return this.__top;
};

/**
 * Returns the bottom most point.
 *
 * @return the unit value
 * @type int
 */
uiUtil_Dimension.prototype.getBottom = function() {
  if (this.__bottom == null) {
    this.__bottom = this.__top + this.__height;
  }
  return this.__bottom;
};

/**
 * Returns the string representation of the dimension.
 *
 * @return the string
 * @type String
 */
uiUtil_Dimension.prototype.toString = function() {
  return this.getClassName() + " [" +
      " x=" + this.__left +
      " y=" + this.__top +
      " width=" + this.__width +
      " height=" + this.__height +
      " ]";
};

/**
 * Indicates whether the other object is logically equal to this one.
 *
 * @return <code>true</code> if this object is the same as the
 *     <code>obj</code> argument, <code>false</code> otherwise
 * @type boolean
 */
uiUtil_Dimension.prototype.equals = function(obj) {
  if (this == obj) {
    return true;
  }
  if (!(obj instanceof uiUtil_Dimension)) {
    return false;
  }

  return (this.__left == obj.__left) &&
      (this.__top == obj.__top) &&
      (this.__width == obj.__width) &&
      (this.__height == obj.__height);
};
