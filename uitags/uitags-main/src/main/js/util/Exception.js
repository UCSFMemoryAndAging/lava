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

/////
// Exception
//
//////////////////////////////////////
////////// Class Definition //////////
//////////////////////////////////////

/**
 * Creates a generic exception object.
 *
 * @class The top most class in the exception class hierarchy. This is
 *     intended to be used for generic purposes.
 * @extends uiUtil_Object
 * @param {String} message the error message
 */
function uiUtil_Exception(message) {
  this._super();
  /**
   * @type String
   * @private
   */
  this.__message = message;
}

uiUtil_Exception = uiUtil_Object.declareClass(
    uiUtil_Exception, uiUtil_Object, false);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Returns the exception message.
 *
 * @return the message
 * @type String
 */
uiUtil_Exception.prototype.getMessage = function() {
  return ((this.__message == null) ? "" : this.__message);
};

/**
 * Returns a string representation of the exception. The return value
 * also contains the exception class name, thus this method is useful
 * for debugging purposes.
 *
 * @return the string
 * @type String
 */
uiUtil_Exception.prototype.toString = function() {
  return this.getClassName() + ": " + this.getMessage();
};



/////
// Create Exception
//
//////////////////////////////////////
////////// Class Definition //////////
//////////////////////////////////////

/**
 * Creates an exception object for creation errors.
 *
 * @class This exception is intended to be used for errors during the
 *     creation of objects.
 * @extends uiUtil_Exception
 * @param {String} message the error message
 */
function uiUtil_CreateException(message) {
  this._super(message);
}

uiUtil_CreateException = uiUtil_Object.declareClass(
    uiUtil_CreateException, uiUtil_Exception, false);



/////
// Illegal State Exception
//
//////////////////////////////////////
////////// Class Definition //////////
//////////////////////////////////////

/**
 * Creates an exception object for internal errors.
 *
 * @class This exception is intended to be used for internal errors
 *     that should not occur unless there is a logic error.
 * @extends uiUtil_Exception
 * @param {String} message the error message
 */
function uiUtil_IllegalStateException(message) {
  this._super(message);
}

uiUtil_IllegalStateException = uiUtil_Object.declareClass(
    uiUtil_IllegalStateException, uiUtil_Exception, false);



/////
// Illegal Argument Exception
//
//////////////////////////////////////
////////// Class Definition //////////
//////////////////////////////////////

/**
 * Creates an exception object for argument errors.
 *
 * @class This exception is intended to be used for errors when
 *     unexpected argument types or values are encountered.
 * @extends uiUtil_Exception
 * @param {String} message the error message
 */
function uiUtil_IllegalArgumentException(message) {
  this._super(message);
}

uiUtil_IllegalArgumentException = uiUtil_Object.declareClass(
    uiUtil_IllegalArgumentException, uiUtil_Exception, false);
