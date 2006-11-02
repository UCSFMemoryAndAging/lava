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
 * Creates a wrapper for a standard JavaScript <code>Date</code> object.
 *
 * @class Wraps a standard JavaScript <code>Date</code> object to provide
 *     richer functionality, such as listener notification on setter
 *     invocation.
 * @extends uiUtil_Object
 * @param {Date} optDate the JavaScript <code>Date</code> object to wrap. If
 *     this optional argument is not supplied, a new <code>Date</code>
 *     is created internally as the underlying date object.
 */
function uiUtil_Calendar(optJsDate) {
  this._super();

  /**
   * @type uiUtil_Calendar.UpdateListener[]
   * @private
   */
  this.__updateListeners = new Array();
  /**
   * @type Date
   * @private
   */
  this.__date = (optJsDate == null) ? new Date() : new Date(optJsDate);
}

uiUtil_Calendar = uiUtil_Object.declareClass(uiUtil_Calendar, uiUtil_Object);

/**
 * Number of days in a week.
 * @type int
 */
uiUtil_Calendar.NUM_DAYS_IN_A_WEEK = 7;
/**
 * Number of months in a year.
 * @type int
 */
uiUtil_Calendar.NUM_MONTHS_IN_A_YEAR = 12;

/**
 * The day of week index representing Sunday.
 * @type int
 */
uiUtil_Calendar.INDEX_SUNDAY = 0;
/**
 * The day of week index representing Monday.
 * @type int
 */
uiUtil_Calendar.INDEX_MONDAY = 1;
/**
 * The day of week index representing Tuesday.
 * @type int
 */
uiUtil_Calendar.INDEX_TUESDAY = 2;
/**
 * The day of week index representing Wednesday.
 * @type int
 */
uiUtil_Calendar.INDEX_WEDNESDAY = 3;
/**
 * The day of week index representing Thursday.
 * @type int
 */
uiUtil_Calendar.INDEX_THURSDAY = 4;
/**
 * The day of week index representing Friday.
 * @type int
 */
uiUtil_Calendar.INDEX_FRIDAY = 5;
/**
 * The day of week index representing Saturday.
 * @type int
 */
uiUtil_Calendar.INDEX_SATURDAY = 6;

/**
 * The index representing January.
 * @type int
 */
uiUtil_Calendar.INDEX_JANUARY = 0;
/**
 * The index representing February.
 * @type int
 */
uiUtil_Calendar.INDEX_FEBRUARY = 1;
/**
 * The index representing March.
 * @type int
 */
uiUtil_Calendar.INDEX_MARCH = 2;
/**
 * The index representing April.
 * @type int
 */
uiUtil_Calendar.INDEX_APRIL = 3;
/**
 * The index representing May.
 * @type int
 */
uiUtil_Calendar.INDEX_MAY = 4;
/**
 * The index representing June.
 * @type int
 */
uiUtil_Calendar.INDEX_JUNE = 5;
/**
 * The index representing July.
 * @type int
 */
uiUtil_Calendar.INDEX_JULY = 6;
/**
 * The index representing August.
 * @type int
 */
uiUtil_Calendar.INDEX_AUGUST = 7;
/**
 * The index representing September.
 * @type int
 */
uiUtil_Calendar.INDEX_SEPTEMBER = 8;
/**
 * The index representing October.
 * @type int
 */
uiUtil_Calendar.INDEX_OCTOBER = 9;
/**
 * The index representing November.
 * @type int
 */
uiUtil_Calendar.INDEX_NOVEMBER = 10;
/**
 * The index representing December.
 * @type int
 */
uiUtil_Calendar.INDEX_DECEMBER = 11;

/**
 * The character representing the year in date formatting.
 * @type int
 */
uiUtil_Calendar.CODE_YEAR = "y";
/**
 * The character representing the month in date formatting.
 * @type int
 */
uiUtil_Calendar.CODE_MONTH = "M";
/**
 * The character representing the day in date formatting.
 * @type int
 */
uiUtil_Calendar.CODE_DAY = "d";
/** @private */
uiUtil_Calendar.__SPECIAL_CHARS =
    uiUtil_Calendar.CODE_YEAR +
    uiUtil_Calendar.CODE_MONTH +
    uiUtil_Calendar.CODE_DAY;


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Adds a listener for date updates.
 *
 * @param {uiUtil_Calendar.UpdateListener} listener the listener
 */
uiUtil_Calendar.prototype.addUpdateListener = function(listener) {
  this.__updateListeners.push(listener);
};

/**
 * Exports the current date to a <code>Date</code> object. Every call to
 * this method returns a new copy <code>Date</code> object.
 *
 * @return the date
 * @type Date
 */
uiUtil_Calendar.prototype.toDate = function() {
  // Create a copy of the date object, thus the internal date object
  // is guaranteed to be unchanged.
  return new Date(this.__date);
};

/**
 * Imports the provided <code>date</code> argument. Every future updates
 * made to this object will not affect the object (<code>Date</code>)
 * referenced by the <code>date</code> argument.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @param {Date} jsDate the date
 */
uiUtil_Calendar.prototype.fromDate = function(jsDate) {
  // Create a copy of the date object, thus the original date object
  // is guaranteed to be unchanged.
  this.__date = new Date(jsDate);
  this.__notifyListeners();
};

/**
 * Updates the year, month, and day attributes.
 * <p>
 * Note: Simply setting day, month, and year individually is not the same
 * as updating them altogether, because the day value might overflow
 * depending on the month value.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @param {int} newYear the new year value
 * @param {int} newMonth the new month value
 * @param {int} newDay the new day value
 * @return <code>this</code>
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.prototype.update = function(newYear, newMonth, newDay) {
  // NOTE: The update goes in the order of year, month, day to avoid
  //       problems with overflowing (e.g. month > 12 will increment
  //       the year).
  //       However, it is also important to reset the date to 1 first,
  //       to avoid automatic overflowing (e.g. when the current day
  //       is 30 and month is set to Feb, unlike other browsers, IE6
  //       will immediately adjust the date, thus becomes 1/2 of Mar).
  this.__date.setDate(1);

  this.__date.setYear(newYear);
  this.__date.setMonth(newMonth);
  this.__date.setDate(newDay);
  this.__notifyListeners();
  return this;
};

/**
 * Sets the day attribute.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @param {int} newDay the new day value
 * @return <code>this</code>
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.prototype.setDay = function(newDay) {
  this.__date.setDate(newDay);
  this.__notifyListeners();
  return this;
};

/**
 * Sets the month attribute.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @param {int} newMonth the new month value
 * @return <code>this</code>
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.prototype.setMonth = function(newMonth) {
  this.__date.setMonth(newMonth);
  this.__notifyListeners();
  return this;
};

/**
 * Sets the year attribute.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @param {int} newYear the new year value
 * @return <code>this</code>
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.prototype.setYear = function(newYear) {
  this.__date.setYear(newYear);
  this.__notifyListeners();
  return this;
};

/**
 * Increments the current day value.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @return <code>this</code>
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.prototype.incrementDay = function() {
  return this.setDay(this.getDay() + 1);
};

/**
 * Decrements the current day value.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @return <code>this</code>
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.prototype.decrementDay = function() {
  return this.setDay(this.getDay() - 1);
};

/**
 * Increments the current month value.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @return <code>this</code>
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.prototype.incrementMonth = function() {
  return this.setMonth(this.getMonth() + 1);
};

/**
 * Decrements the current month value.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @return <code>this</code>
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.prototype.decrementMonth = function() {
  return this.setMonth(this.getMonth() - 1);
};

/**
 * Increments the current year value.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @return <code>this</code>
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.prototype.incrementYear = function() {
  return this.setYear(this.getYear() + 1);
};

/**
 * Decrements the current year value.
 * <p>
 * Note: This operation will notify all registered update listeners.
 *
 * @return <code>this</code>
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.prototype.decrementYear = function() {
  return this.setYear(this.getYear() - 1);
};

/**
 * Notifies all the listeners of an update.
 *
 * @private
 */
uiUtil_Calendar.prototype.__notifyListeners = function() {
  for (var i = 0; i < this.__updateListeners.length; ++i) {
    this.__updateListeners[i].dateUpdated(this);
  }
};

/**
 * Returns the day value.
 *
 * @return the value
 * @type int
 */
uiUtil_Calendar.prototype.getDay = function() {
  return this.__date.getDate();
};

/**
 * Returns the month value.
 *
 * @return the value
 * @type int
 */
uiUtil_Calendar.prototype.getMonth = function() {
  return this.__date.getMonth();
};

/**
 * Returns the year value.
 *
 * @return the value
 * @type int
 */
uiUtil_Calendar.prototype.getYear = function() {
  return this.__date.getFullYear();
};

/**
 * Returns the first day of the week in the current month.
 *
 * @return the day index, ranged from 0 to 6
 * @type int
 */
uiUtil_Calendar.prototype.getFirstDayInMonth = function() {
  var firstInMonth = new Date(this.__date);
  firstInMonth.setDate(1);
  return firstInMonth.getDay();
};

/**
 * Returns number of days in the current month.
 *
 * @return the number of days
 * @type int
 */
uiUtil_Calendar.prototype.getNumDaysInMonth = function() {
  var numDays = new Array(12);
  numDays[0]  = 31;
  numDays[1]  = this.__isLeapYear()? 29 : 28;
  numDays[2]  = 31;
  numDays[3]  = 30;
  numDays[4]  = 31;
  numDays[5]  = 30;
  numDays[6]  = 31;
  numDays[7]  = 31;
  numDays[8]  = 30;
  numDays[9]  = 31;
  numDays[10] = 30;
  numDays[11] = 31;

  return numDays[this.__date.getMonth()];
};

/**
 * Checks if the current year is a leap year.
 *
 * @return <code>true</code> if it is, <code>false</code> otherwise
 * @type boolean
 * @private
 */
uiUtil_Calendar.prototype.__isLeapYear = function() {
  var year = this.__date.getFullYear();
  if ((year % 4 == 0 && year % 100 != 0) ||
      year % 400 == 0) {
    return true;
  }
  return false;
};

/**
 * Formats the string representation of the current date.
 *
 * @param {String} format the specified format
 * @return the formatted string
 * @type String
 */
uiUtil_Calendar.prototype.format = function(format) {
  var specialChars = uiUtil_Calendar.__SPECIAL_CHARS;
  var newString = format;
  for (var i = 0; i < specialChars.length; ++i) {
    var regex = new RegExp("(" + specialChars.charAt(i) + "+)");
    if (regex.test(format)) {
      var specialCode = RegExp.$1;

      var codeHandler = uiUtil_Calendar.__formats[specialCode];
      if (codeHandler != null) {
        newString = newString.replace(
            specialCode, codeHandler.call(this));
      }
    }
  }
  return newString;
};

/** @private */
uiUtil_Calendar.__formats = new Array();
/** @private */
uiUtil_Calendar.__formats["yyyy"] = function() {
  return this.getYear();
};
/** @private */
uiUtil_Calendar.__formats["yy"] = function() {
  var yearString = new String(this.getYear());
  return yearString.substring(2);
};
/** @private */
uiUtil_Calendar.__formats["MM"] = function() {
  var month = this.getMonth() + 1;
  return (month < 10) ? "0" + month : month;
};
/** @private */
uiUtil_Calendar.__formats["dd"] = function() {
  var day = this.getDay();
  return (day < 10) ? "0" + day : day;
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

/**
 * Creates a {@link uiUtil_Calendar} Object from the provided string
 * represented date.
 *
 * @param {String} string the string representing a date
 * @param {String} format a hint for the parser for converting the string
 * @return the created object
 * @type uiUtil_Calendar
 */
uiUtil_Calendar.createFromString = function(string, format) {
  var regex = new uiUtil_Calendar.DateRegExp(format);
  if (regex.__test(string)) {
    var year = regex.__getYear();
    var month = regex.__getMonth();
    var day = regex.__getDay();

    var calendar = new uiUtil_Calendar(new Date(year, month, day));
    if (calendar.getDay() != day ||
        calendar.getMonth() != month ||
        calendar.getYear() != year) {
      throw new uiUtil_CreateException("Invalid date value " + string);
    }
    return calendar;
  }
  throw new uiUtil_CreateException("Invalid date '" + string +
      "' according to the format: " + format);
};


///////////////////////////////////
////////// Inner Classes //////////
///////////////////////////////////

/**
 * @class A reference for defining the interface of {@link uiUtil_Calendar}'s
 *     update listeners.
 * @extends uiUtil_Object
 */
function uiUtil_Calendar$UpdateListener() {
  this._super();
}

/** @ignore */
uiUtil_Calendar.UpdateListener =
    uiUtil_Object.declareClass(uiUtil_Calendar$UpdateListener, uiUtil_Object);

/**
 * A callback method that gets invoked whenever any of
 * {@link uiUtil_Calendar}'s date attributes gets updated.
 *
 * @param {uiUtil_Calendar} date the new date
 */
uiUtil_Calendar.UpdateListener.prototype.dateUpdated = function(date) {
  alert("Date updated " + date.format("dd/MM/yyyy"));
};



/**
 * @class A regular expression parser for dates.
 * @extends uiUtil_Object
 */
function uiUtil_Calendar$DateRegExp(format) {
  this._super();

  this.__yearIndex = 0;
  this.__monthIndex = 0;
  this.__dayIndex = 0;
  this.__currentIndex = 1;

  this.__regex = new RegExp(this.__getRegexPatternFrom(format));
}

/** @ignore */
uiUtil_Calendar.DateRegExp =
    uiUtil_Object.declareClass(uiUtil_Calendar$DateRegExp, uiUtil_Object);

/** @ignore */
uiUtil_Calendar.DateRegExp.prototype.__getRegexPatternFrom = function(format) {
  var pattern = "";
  var currentChar = "";
  var currentCount;
  for (var i = 0; i < format.length; ++i) {
    if (format.charAt(i) == currentChar) {
      ++currentCount;
    }
    else {
      if (currentChar != "") {
        pattern += this.__generateDigitPattern(currentCount);
      }

      if (this.__isSpecialChar(format.charAt(i))) {
        currentChar = format.charAt(i);
        currentCount = 1;
      }
      else {
        pattern += format.charAt(i);
        currentChar = "";
      }
    }
  }
  if (currentChar != "") {  // last unprocessed char
    pattern += this.__generateDigitPattern(currentCount);
  }
  return pattern;
};

/** @ignore */
uiUtil_Calendar.DateRegExp.prototype.__generateDigitPattern = function(count) {
  return "(\\d{" + count + "})";
};

/** @ignore */
uiUtil_Calendar.DateRegExp.prototype.__isSpecialChar = function(currentChar) {
  switch (currentChar) {
    case uiUtil_Calendar.CODE_YEAR :
        this.__yearIndex = this.__currentIndex;
        ++this.__currentIndex;
        return true;
    case uiUtil_Calendar.CODE_MONTH :
        this.__monthIndex = this.__currentIndex;
        ++this.__currentIndex;
        return true;
    case uiUtil_Calendar.CODE_DAY :
        this.__dayIndex = this.__currentIndex;
        ++this.__currentIndex;
        return true;
  }
  return false;
};

/** @ignore */
uiUtil_Calendar.DateRegExp.prototype.__test = function(string) {
  return this.__regex.test(string);
};

/** @ignore */
uiUtil_Calendar.DateRegExp.prototype.__getDay = function() {
  return eval("RegExp.$" + this.__dayIndex);
};

/** @ignore */
uiUtil_Calendar.DateRegExp.prototype.__getMonth = function() {
  return eval("RegExp.$" + this.__monthIndex) - 1;
};

/** @ignore */
uiUtil_Calendar.DateRegExp.prototype.__getYear = function() {
  return eval("RegExp.$" + this.__yearIndex);
};
