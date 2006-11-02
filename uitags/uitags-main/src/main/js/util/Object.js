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

/**
 * Default constructor.
 *
 * @class The root of the class hierarchy in uitags.
 *     Note to developer: since this is the root, avoid dependency on
 *     any other classes
 */
function uiUtil_Object() {
  var inherit = arguments[this.__init.length];
  if (!inherit) {
    this.__init.apply(this, arguments);
  }
  ++uiUtil_Object.__numOfInstantiatedObjects;
}

/**
 * Internal tracker of the number objects (child classes of this class)
 * that get instantiated.
 *
 * @type int
 * @private
 */
uiUtil_Object.__numOfInstantiatedObjects = 0;

/**
 * The actual implementation of the class' constructor.
 *
 * @private
 */
uiUtil_Object.prototype.__init = function() {
  // Currently there is nothing to do in the constructor.
};

/**
 * Since object shouldn't depend on any other clases, we need this method
 * to transform to array instead of using uiUtil_ArrayUtils.
 *
 * @private
 */
uiUtil_Object.prototype.__toArray = function(collection, start) {
  var arr = new Array();
  for (var i = start; i < collection.length; ++i) {
    arr[i - start] = collection[i];
  }
  return arr;
};

/**
 * Calls the super class' constructor.
 *
 * @param {Object} optArgN any number of arguments can be passed
 */
uiUtil_Object.prototype._super = function(optArgN) {
  // note that 'arguments' is not necessarily an array
  // note that the arguments start from index 0
  this.__apply("__init", this.__toArray(arguments, 0));
};

/**
 * Calls a certain method of the super class
 *
 * @param {String} method name of the method to invoke
 * @param {Object} optArgN any number of arguments can be passed
 * @return the return value of the invoked method
 * @type Object
 */
uiUtil_Object.prototype._callSuper = function(method, optArgN) {
  // note that 'arguments' is not necessarily an array
  // note that the arguments start from index 1
  return this.__apply(method, this.__toArray(arguments, 1));
};

/**
 * Calls a certain method of the super class, passing an array of values
 * as the arguments of the method.
 *
 * @param {String} method name of the method to invoke
 * @param {Collection} optArguments a collection of arguments for the method
 * @return the return value of the invoked method
 * @type Object
 * @private
 */
uiUtil_Object.prototype.__apply = function(method, optArguments) {
  // Note that although child class can be derived from child object,
  // it does not work in a child class of a child class, because the
  // child object will always refer to the bottom most child class.
  // Therefore, this method should explicitly receive the class of
  // the instance that is calling the parent's method
  if (this.__executingClass[method] == null) {
    this.__executingClass[method] = this.constructor;
  }

  var currentClass = this.__executingClass[method];
  this.__executingClass[method] = currentClass.__parentClass;

  try {
    var returnValue = currentClass.__parentPrototype[method].apply(this, optArguments);
  }
  finally {
    this.__executingClass[method] = currentClass;
  }

  return returnValue;
};

/**
 * Returns the class name of the object.
 *
 * @return the class name
 * @type String
 */
uiUtil_Object.prototype.getClassName = function() {
  return uiUtil_Object.getClassName(this.constructor);
};

/**
 * Returns the string representation of the object. The returned value
 * also contains a unique identifier for each instance of the classes
 * extending from this class.
 *
 * @return the string
 * @type String
 */
uiUtil_Object.prototype.toString = function() {
  return this.getClassName() + uiUtil_Object.__numOfInstantiatedObjects;
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * Makes a class inherit from another class.
 *
 * @param {Class} childClass the class that inherits
 * @param {Class} parentClass the class that is inherited
 * @return the new prototype object of the child class
 * @type Object
 */
uiUtil_Object._inherits = function(childClass, parentClass) {
  var UNDEFINED_VALUE;

  // The purpose of the following block of code it to construct an dummy
  // object of the parent class, and assign the instance to the child's
  // prototype property so that the child "inherits" (has reference)
  // to the parent class' methods.
  // We need to set a flag for the instantiation, so that the parent
  // class knows that we are trying to create a dummy object and thus,
  // does not try to process anything.

  // Get expected num of arguments for the constructor.
  var count = parentClass.prototype.__init.length;

  var command = "new parentClass(";
  if (count > 0) {
    for (var i = 0; i < count; ++i) {
      command += "UNDEFINED_VALUE, ";
    }
  }
  command += "true);";

  // Inherit base class' function.
  childClass.prototype = eval(command);
  // Fix the (well-known) reference so that we can refer to the actual class
  // of the child instance.
  childClass.prototype.constructor = childClass;
  /**
   * A hash of owner classes keyed on the method name that is executing.
   * This is used to keep track of the current class when calling super
   * classes' methods.
   *
   * @type hash&lt;Class&gt;
   * @private
   */
  childClass.prototype.__executingClass = new Object();
  // Allow access to base class' overriden methods.
  childClass.__parentPrototype = parentClass.prototype;
  // Allows the child to know its parent class.
  childClass.__parentClass = parentClass;

  return childClass.prototype;
};

/**
 * Checks if <code>class1</code> is either the same as, or is a superclass
 * of <code>class2</code>.
 *
 * @param {Object} class1 class1
 * @param {Object} class2 class2
 * @return <code>true</code> if <code>class1<code> is the same as, or is
 *     a superclass of <code>class2</code>
 * @type boolean
 */
uiUtil_Object.isAssignableFrom = function(class1, class2) {
  var currClass = class2;
  while (currClass != null) {
    if (class1 == currClass) {
      return true;
    }
    currClass = currClass.__parentClass;
  }
  return false;
};

/**
 * Declares a normal class, which is achieved by modifying some internal
 * properties of an already existing class.
 *
 * @param {Class} origClass the existing class that is being declared
 * @param {Class} parentClass the class to extend from
 * @param {boolean} optUseLogger an optional parameter to indicate whether
 *     a shared logger for the class needs to be created
 * @return a new reference to the declared class
 * @type Class
 */
uiUtil_Object.declareClass = function(origClass, parentClass, optUseLogger) {
  return uiUtil_Object.__declareGenericClass(origClass, parentClass,
      ((optUseLogger == null) ? true : optUseLogger),
      uiUtil_Object.__evaluateClassCode);
};

/**
 * Declares a singleton class, which is achieved by modifying some internal
 * properties of an already existing class.
 *
 * @param {Class} origClass the existing class that is being declared
 * @param {Class} parentClass the class to extend from
 * @param {boolean} optUseLogger an optional parameter to indicate whether
 *     a shared logger for the class needs to be created
 * @return a new reference to the declared class
 * @type Class
 */
uiUtil_Object.declareSingleton = function(origClass, parentClass, optUseLogger) {
  return uiUtil_Object.__declareGenericClass(origClass, parentClass,
      ((optUseLogger == null) ? true : optUseLogger),
      uiUtil_Object.__evaluateSingletonCode);
};

/**
 * Declares a utility class, which is achieved by modifying some internal
 * properties of an already existing class.
 *
 * @param {Class} origClass the existing class that is being declared
 * @param {Class} parentClass the class to extend from
 * @return a new reference to the declared class
 * @type Class
 */
uiUtil_Object.declareUtil = function(origClass, parentClass) {
  return uiUtil_Object.__declareGenericClass(
      origClass, parentClass, false, uiUtil_Object.__evaluateUtilCode);
};

/**
 * Provides a generic way to declare the different types of class.
 *
 * @param {Class} origClass the existing class that is being declared
 * @param {Class} parentClass the class to extend from
 * @param {boolean} useLogger a parameter to indicate whether
 *     a shared logger for the class needs to be created
 * @param {function} codeGenerator the function that is responsible
 *     for generating the appropriate code for declaring the class
 * @return a new reference to the declared class
 * @type Class
 * @private
 */
uiUtil_Object.__declareGenericClass = function(
    origClass, parent, useLogger, codeGenerator) {
  try {
    // generate code for the constructor's declaration
    var classRef = codeGenerator(origClass);

    var newPrototype = uiUtil_Object._inherits(classRef, parent);
    newPrototype.__init = origClass;
    if (useLogger) {
      newPrototype.__logger = uiUtil_Logger.getInstance(classRef);
      classRef.__logger = newPrototype.__logger;
    }
  }
  catch (e) {  // don't let any error slip
    alert("Error declaring class: " + uiUtil_Object.getClassName(classRef) +
        ".\nCaused by: " + e);
  }

  // NOTE: Return the function reference, so that:
  // - The reference can be assigned to a variable for easy referencing.
  // - Global accesses to the function (in IE 6) are enabled by assigning
  //   the reference to a variable with the same name as the original
  //   class name.
  return classRef;
};

/**
 * Generates the code for declaring a constructor.
 *
 * @param {Class} origClass the class of the constructor
 * @return the code
 * type String
 * @private
 */
uiUtil_Object.__generateConstructorPrototype = function(origClass) {
  var className = uiUtil_Object.getClassName(origClass);
  return "function " + className + "()";
};

/**
 * Generates and evaluates the code for declaring a utility class.
 *
 * @param {Class} origClass the class
 * @return the reference to the new class
 * type Class
 * @private
 */
uiUtil_Object.__evaluateUtilCode = function(origClass) {
  var className = uiUtil_Object.getClassName(origClass);
  return eval(
      className + " = " + uiUtil_Object.__generateConstructorPrototype(origClass) + " {" +
      "  throw new uiUtil_CreateException(" +
      "      'This class is not instantiable: ' + this.getClassName());" +
      "};"
  );
};

/**
 * Generates and evaluates the code for declaring a normal class.
 *
 * @param {Class} origClass the class
 * @return the reference to the new class
 * type Class
 * @private
 */
uiUtil_Object.__evaluateClassCode = function(origClass) {
  var className = uiUtil_Object.getClassName(origClass);
  return eval(
      // give a name to the function so that:
      // - it is consistent with other functions
      // - we can get class name from function reference
      // - In Mozilla, this is sufficient to allow the function to
      //   be accessed globally, however this is not the case in IE
      //
      // assign the function reference to a variable so that:
      // - eval() returns reference to the function (don't know the reason)
      // - we can use the name as an argument for _inherits()
      className + " = " + uiUtil_Object.__generateConstructorPrototype(origClass) + " {" +
      "  var inherit = arguments[this.__init.length];" +
      "  if (!inherit) {" +
      "    this.__init.apply(this, arguments);" +
      "  }" +
      "};"
  );
};

/**
 * Generates and evaluates the code for declaring a singleton class.
 *
 * @param {Class} origClass the class
 * @return the reference to the new class
 * type Class
 * @private
 */
uiUtil_Object.__evaluateSingletonCode = function(origClass) {
  var className = uiUtil_Object.getClassName(origClass);
  return eval(
      // this should be after the class declaration
      "var temp = " + uiUtil_Object.__generateConstructorPrototype(origClass) + " {" +
      "  if (" + className + ".__instance != null) {" +
      "    throw new uiUtil_CreateException(" +
      "        'A singleton cannot have multiple instances: ' + className);" +
      "  }" +
      // to make sure that if this constructor is called the first time
      // the next time will not be allowed anymore
      "  " + className + ".__instance = this;" +
      "  var inherit = arguments[this.__init.length];" +
      "  if (!inherit) {" +
      "    try {" +
      "      this.__init.apply(this, arguments);" +
      "    }" +
      "    catch (e) {" +
      "      " + className + ".__instance = null;" +  // reset it
      "      throw e" +  // rethrow the exception
      "    }" +
      "  }" +
      "};" + // semi-colon is needed because this is an assignment
      "temp.__instance = null;" +
      "temp.getInstance = function() {" +  // a factory method
      "  if (" + className + ".__instance == null) {" +
      "    new " + className + "();" +
      "  }" +
      // NOTE: this variable is already set in the constructor
      "  return " + className + ".__instance;" +
      "};" +
      className + " = temp;"
  );
};

/**
 * Returns the name of a certain class.
 *
 * @param {Class} objectClass the class
 * @return the class name
 * @type String
 */
uiUtil_Object.getClassName = function(objectClass) {
  if (objectClass.name == null) {
    return objectClass.toString().replace(
        /(.*\n)*function ([^\(]*)\((.*\n)*.*/, "$2");
  }
  return objectClass.name;  // Firefox 1 only (not sure about Opera though)
};

/**
 * Returns a string containing all properties of the object paired
 * with their values.
 *
 * @param {Object} object any object
 * @return the string
 * @type String
 */
uiUtil_Object.getPropertiesString = function(object) {
  var str = "";
  for (property in object) {
    str += ' ' + property + "=" + this[property];
  }
  return str;
};
