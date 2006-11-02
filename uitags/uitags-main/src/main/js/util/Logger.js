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
 * Creates a logger instance for a certain class.
 *
 * @class A logger that prints messages using different streams depending
 *     on their availability. The logger will attempt to use the streams
 *     in the following order: debug panel, document body, or alert box.
 * @extends uiUtil_Object
 * @param {Class} loggedClass the class that the logger belongs to
 * @param {int} logLevel the initial log level
 */
function uiUtil_Logger(loggedClass, logLevel) {
  this._super();

  // use getTypeName instead of uiUtil_Object.getClassName to allow undefined class
  var className = uiUtil_Type.getTypeName(loggedClass);
  // a singleton, only allow the first invocation
  if (uiUtil_Logger.__instances[className] != null) {
    throw new uiUtil_CreateException("There shouldn't be more than one " +
        "instances of logger for the same class: " + className);
  }
  // to make sure that if this constructor is called the first time
  // the next time will not be allowed anymore
  uiUtil_Logger.__instances[className] = this;

  /**
   * @type int[]
   * @private
   */
  this.__allLevels = new Array();
  /**
   * @type Class
   * @private
   */
  this.__loggedClass = loggedClass;
  /**
   * @type boolean
   * @private
   */
  this.__needDebugPanel = (logLevel != uiUtil_Logger.LEVEL_NONE);
  /**
   * @type int
   * @private
   */
  this.__currentLevel = null;

  this.setLevel(logLevel);
}

// NOTE: don't use uiUtil_Type to declare the logger class, because
//       the generated code tries to instantiate a logger whenever a
//       class is instantiated, which could cause infinite recursive
//       calls for creating a logger object
uiUtil_Object._inherits(uiUtil_Logger, uiUtil_Object);

/**
 * @type uiUtil_Logger.PrintStream
 * @private
 */
uiUtil_Logger.__defaultStream = null;
/**
 * @type hash&lt;uiUtil_Logger&gt;
 * @private
 */
uiUtil_Logger.__instances = new Object();

/**
 * @type int
 */
uiUtil_Logger.LEVEL_NONE = 0;
/**
 * @type int
 */
uiUtil_Logger.LEVEL_FATAL = 1;
/**
 * @type int
 */
uiUtil_Logger.LEVEL_ERROR = 2;
/**
 * @type int
 */
uiUtil_Logger.LEVEL_WARN = 3;
/**
 * @type int
 */
uiUtil_Logger.LEVEL_INFO = 4;
/**
 * @type int
 */
uiUtil_Logger.LEVEL_DEBUG = 5;
/**
 * @type int
 */
uiUtil_Logger.LEVEL_ALL = 6;

if ((typeof uiGlobal_defaultLevel) == "undefined") {
  /**
   * By default, logging support is disabled. Avoid changing the logging
   * level here, instead, do it by setting the global variable
   * <code>uiGlobal_defaultLevel</code> in the designated <i>init</i> files,
   * such as <i>initDebug.js</i>.
   *
   * @type int
   * @private
   */
  uiUtil_Logger.__defaultLevel = uiUtil_Logger.LEVEL_NONE;
}
else {
  uiUtil_Logger.__defaultLevel = uiGlobal_defaultLevel;
}


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Returns the most suitable available stream to print debug statements.
 * This method is used for lazy loading, thus allowing debug panels to
 * be obtained at runtime (since its instantiation at startup is not
 * possible, because the window has not finished loading yet).
 *
 * @param {Class} loggedClass the owner class of the logger
 * @return the available stream
 * @type uiUtil_Logger.PrintStream
 * @private
 */
uiUtil_Logger.prototype.__obtainStream = function(loggedClass) {
  var className = uiUtil_Type.getTypeName(loggedClass);
  var stream = null;

  this.__needDebugPanel = true;
  try {
    stream = uiHtml_DebugPanel.getInstance("Creator: " + className);
    this.__needDebugPanel = false;
  }
  catch (e1) {
    try {
      // NOTE: Theoritically the document will never be used, because
      //       whenever the document is ready, the debug panel should
      //       already be ready as well. However, it might be useful
      //       for debugging under unusual circumstances
      stream = uiHtml_Document.getInstance().getPrintStream();
    }
    catch (e2) {
      // Revert to default logger.
      stream = uiUtil_Logger.getDefaultPrintStream();
    }
  }
  return stream;
};

/**
 * Enables logging for a specified level along with all other levels above it.
 *
 * @param {int} level the level to enable
 */
uiUtil_Logger.prototype.setLevel = function(level) {
  this.__currentLevel = level;

  var max = level + 1;  // plus 1 to include the specified level
  for (var i = 0; i < max; ++i) {
    this.__allLevels[i] = true;
  }
};

/**
 * Sets the logger's print stream.
 *
 * @param {uiUtil_Logger.PrintStream} stream the new print stream
 */
uiUtil_Logger.prototype.setStream = function(stream) {
  this.__stream = stream;
};

/**
 * Returns the logger's print stream.
 *
 * @return the print stream
 * @type uiUtil_Logger.PrintStream
 */
uiUtil_Logger.prototype.getStream = function() {
  if (this.__needDebugPanel) {
    try {
      this.__stream = this.__obtainStream(this.__loggedClass);
    }
    catch (e) {
      // Still can't get debug panel, never mind then.
      throw e;
    }
  }
  return this.__stream;
};

/**
 * Logs the specified message to a print stream.
 *
 * @param {uiUtil_Logger.PrintStream} stream the stream
 * @param {String} message the message
 * @param {int} level the requested log level
 *
 * @private
 */
uiUtil_Logger.prototype.__logTo = function(stream, message, level) {
  var levelName;
  switch (level) {
    case uiUtil_Logger.LEVEL_FATAL : levelName = "FATAL";
                                        break;
    case uiUtil_Logger.LEVEL_ERROR : levelName = "ERROR";
                                        break;
    case uiUtil_Logger.LEVEL_WARN  : levelName = "WARN";
                                        break;
    case uiUtil_Logger.LEVEL_INFO  : levelName = "INFO";
                                        break;
    case uiUtil_Logger.LEVEL_DEBUG : levelName = "DEBUG";
                                        break;
  }

  stream.println("[" + levelName + "] " +
      uiUtil_Type.getTypeName(this.__loggedClass) + ": " + message);
};

/**
 * Logs a message to the logger's print stream. This method does not
 * perform checking whether a message should be logged according to
 * the specified log level, which is expected to have been performed
 * in the caller due to efficiency reason.
 *
 * @param {String} message the message
 * @param {int} level the requested log level
 *
 * @private
 */
uiUtil_Logger.prototype.__log = function(message, level) {
  this.__logTo(this.getStream(), message, level);
};

/**
 * Logs a message with LEVEL_FATAL level.
 *
 * @param {String} message the message
 */
uiUtil_Logger.prototype.fatal = function(message) {
  if (this.__allLevels[uiUtil_Logger.LEVEL_FATAL]) {
    this.__log(message, uiUtil_Logger.LEVEL_FATAL);
  }
};

/**
 * Logs a message with LEVEL_ERROR level.
 *
 * @param {String} message the message
 */
uiUtil_Logger.prototype.error = function(message) {
  if (this.__allLevels[uiUtil_Logger.LEVEL_ERROR]) {
    this.__log(message, uiUtil_Logger.LEVEL_ERROR);
  }
};

/**
 * Logs a message with LEVEL_WARN level.
 *
 * @param {String} message the message
 */
uiUtil_Logger.prototype.warn = function(message) {
  if (this.__allLevels[uiUtil_Logger.LEVEL_WARN]) {
    this.__log(message, uiUtil_Logger.LEVEL_WARN);
  }
};

/**
 * Logs a message with LEVEL_INFO level.
 *
 * @param {String} message the message
 */
uiUtil_Logger.prototype.info = function(message) {
  if (this.__allLevels[uiUtil_Logger.LEVEL_INFO]) {
    this.__log(message, uiUtil_Logger.LEVEL_INFO);
  }
};

/**
 * Logs a message with LEVEL_DEBUG level.
 *
 * @param {String} message the message
 */
uiUtil_Logger.prototype.debug = function(message) {
  if (this.__allLevels[uiUtil_Logger.LEVEL_DEBUG]) {
    this.__log(message, uiUtil_Logger.LEVEL_DEBUG);
  }
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * Returns a logger instance for a class.
 *
 * @param {Class} optLoggedClass an optional class that owns the logger
 * @param {int} optLevel optional log level
 * @return the logger instance
 * @type uiUtil_Logger
 */
uiUtil_Logger.getInstance = function(optLoggedClass, optLevel) {
  // Use getTypeName instead of uiUtil_Object.getClassName to allow
  // undefined parameter as the owner class.
  var className = uiUtil_Type.getTypeName(optLoggedClass);
  if (uiUtil_Logger.__instances[className] == null) {
    var level = uiUtil_Type.isDefined(optLevel) ?
        optLevel : uiUtil_Logger.__defaultLevel;
    return new uiUtil_Logger(optLoggedClass, level);
  }
  // NOTE: this variable is already set in the constructor.
  return uiUtil_Logger.__instances[className];
};

/**
 * Returns a logger's default print stream.
 *
 * @return the print stream
 * @type uiUtil_Logger.PrintStream
 */
uiUtil_Logger.getDefaultPrintStream = function() {
  if (uiUtil_Logger.__defaultStream == null) {
    uiUtil_Logger.__defaultStream = new uiUtil_Logger.PrintStream();
  }
  return uiUtil_Logger.__defaultStream;
};


///////////////////////////////////
////////// Inner Classes //////////
///////////////////////////////////

/**
 * @class A default stream for writing messages.
 */
function uiUtil_Logger$PrintStream() {
  this._super();
}

/** @ignore */
uiUtil_Logger.PrintStream =
    uiUtil_Object.declareClass(uiUtil_Logger$PrintStream, uiUtil_Object);

/**
 * Prints the specified text to the stream.
 *
 * @param {String} text the text to print
 */
uiUtil_Logger.PrintStream.prototype.print = function(text) {
  alert(text);
};

/**
 * Prints the specified text to the stream and a newline at the end.
 *
 * @param {String} text the text to print
 */
uiUtil_Logger.PrintStream.prototype.println = function(text) {
  alert(text);
};
