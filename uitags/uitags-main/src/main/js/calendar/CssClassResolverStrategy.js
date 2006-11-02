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
 * @class The default strategy for determining the CSS class for calendar
 *     grid cells. One can provide a custom strategy by extending this class
 *     and redefining public and protected methods defined here.
 * @extends uiUtil_Object
 */
function uiCalendar_CssClassResolverStrategy() {
  this._super();
}

uiCalendar_CssClassResolverStrategy =
    uiUtil_Object.declareClass(uiCalendar_CssClassResolverStrategy, uiUtil_Object);

// Globally specified to allow access from code other than uitags'.
// Day indices use standard Javascript's Date.
CALENDAR_INDEX_SUNDAY = 0;
CALENDAR_INDEX_MONDAY = 1;
CALENDAR_INDEX_TUEDAY = 2;
CALENDAR_INDEX_WEDNESDAY = 3;
CALENDAR_INDEX_THURSDAY = 4;
CALENDAR_INDEX_FRIDAY = 5;
CALENDAR_INDEX_SATURDAY = 6;


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Checks whether two dates are semantically equal.
 *
 * @param {Date} the first date to compare
 * @param {Date} the second date to compare
 * @return <code>true</code> if both <code>Date</code>s have the same
 *     date, month, and year
 * @type boolean
 * @private
 */
uiCalendar_CssClassResolverStrategy.prototype.__dateEquals = function(date1, date2) {
  return (date1.getDate() == date2.getDate() &&
      date1.getMonth() == date2.getMonth() &&
      date1.getFullYear() == date2.getFullYear());
};

/**
 * Returns the CSS class name for the cell occupied by the
 * <code>processedDate</code>.
 * This default implementation returns <code>uiCalendar_cellWeekend</code>
 * if <code>processedDate</code> falls on Saturday or Sunday,
 * or <code>uiCalendar_cellWeekday</code> otherwise.
 *
 * @param {Date} processedDate the <code>Date</code> occupying
 *     the cell for whom a CSS class name is being resolved
 * @param {Date} selectedDate the currently selected date
 * @return the CSS class name
 * @type String
 */
uiCalendar_CssClassResolverStrategy.prototype.getForDate =
    function(processedDate, selectedDate) {

  var currentlyProcessedMonth = processedDate.getMonth();
  var currentlySelectedMonth = selectedDate.getMonth();

  var classNames = "";

  // Either the previous or the next month.
  if (currentlyProcessedMonth != currentlySelectedMonth) {
    classNames += "uiCalendar_otherMonth ";
  }

  if (this.__dateEquals(processedDate, selectedDate)) {
    classNames += "uiCalendar_selected ";
  }

  switch (processedDate.getDay()) {
    case CALENDAR_INDEX_SUNDAY:
    case CALENDAR_INDEX_SATURDAY:
        classNames += "uiCalendar_cellWeekend ";
        break;
    default:
        classNames += "uiCalendar_cellWeekday ";
  }

  return classNames;
};

/**
 * Returns the CSS class name for the header for the day at the specified
 * <code>dayIndex</code>.
 * This default implementation returns <code>uiCalendar_headWeekend</code>
 * if <code>processedDate</code> falls on Saturday or Sunday,
 * or <code>uiCalendar_headWeekday</code> otherwise.
 *
 * @param {int} dayIndex the index specifying the day.
 *     0 signifies Sunday, 1 signifies Monday, and 6 signifies Saturday.
 * @return the CSS class name
 * @type String
 */
uiCalendar_CssClassResolverStrategy.prototype.getForHeader =
    function(dayIndex) {
  switch (dayIndex) {
    case CALENDAR_INDEX_SUNDAY   : return "uiCalendar_headWeekend";
    case CALENDAR_INDEX_SATURDAY : return "uiCalendar_headWeekend";
    default                      : return "uiCalendar_headWeekday";
  }
};
