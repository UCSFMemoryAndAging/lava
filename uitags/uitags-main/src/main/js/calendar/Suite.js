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
 * Creates a calendar widget.
 *
 * @class A calendar widget whose main role is to manage the calendar grid
 *     with the help of the various strategy objects supplied to this object.
 *     This class is a listener to the <i>date updated</i> event fired by
 *     {@link uiUtil_Calendar}.
 * @extends uiUtil_Object
 * @param {HTMLTableElement} domTable the calendar grid table
 * @param {String[]} weekLabels labels for the calendar grid header
 * @param {uiCalendar_CssClassResolverStrategy} classResolver for deciding
 *     which CSS class to apply to a particular grid cell
 * @param {uiCalendar_ActionResolverStrategy} actionResolver for deciding
 *     what action to take when a particular grid cell is clicked
 */
function uiCalendar_Suite(domTable, weekLabels, classResolver, actionResolver) {
  this._super();

  /**
   * A wrapper to help perform DOM operations on the calendar grid cells.
   *
   * @type uiHtml_ElementWrapper
   * @private
   */
  this.__elementHandler = uiHtml_ElementWrapper.getInstance();

  /**
   * Currently selected date.
   *
   * @type uiUtil_Calendar
   * @private
   */
  this.__selectedDate = new uiUtil_Calendar();

  /**
   * The calendar grid table.
   *
   * @type HTMLTableElement
   * @private
   */
  this.__domTable = domTable;

  /**
   * An array of this calendar's year listers, allowing this calendar to
   * update all of the year listers when the selected date changes.
   *
   * @type uiCalendar_Suite.YearLister[]
   * @private
   */
  this.__yearListers = new Array();

  /**
   * An array of this calendar's year listers, allowing this calendar to
   * update all of the year listers when the selected date changes.
   *
   * @type uiCalendar_Suite.MonthExtractor[]
   * @private
   */
  this.__monthListers = new Array();

  /**
   * A strategy that decides what CSS class to apply for a grid
   * cell with a particular date.
   *
   * @type uiCalendar_CssClassResolverStrategy
   * @private
   */
  this.__classResolver = classResolver;

  /**
   * A strategy that decides what action to execute when an event
   * is triggered against a grid cell with a particular date.
   *
   * @type uiCalendar_ActionResolverStrategy
   * @private
   */
  this.__actionResolver = actionResolver;

  this.__selectedDate.addUpdateListener(this);
  this.__initGrid(this.__domTable, weekLabels);
}

uiCalendar_Suite = uiUtil_Object.declareClass(uiCalendar_Suite, uiUtil_Object);

/**
 * The total number of rows in the grid table.
 * @type int
 */
uiCalendar_Suite.GRID_TOTAL_ROW_COUNT = 7;  // Including header row

/**
 * The starting row index from which the grid cells are to be populated
 * with day labels (usually the value of this attribute is 1, leaving
 * the row 0 for grid header).
 * @type int
 */
uiCalendar_Suite.GRID_DATA_ROW_START = 1;

/**
 * The total number of colums in the grid table.
 * @type int
 */
uiCalendar_Suite.GRID_TOTAL_COL_COUNT = uiUtil_Calendar.NUM_DAYS_IN_A_WEEK;


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Builds the DOM objects that make up the calendar grid table and its contents.
 *
 * @param {HTMLTableElement} domTable
 * @param {String[]} weekLabels
 * @private
 */
uiCalendar_Suite.prototype.__initGrid = function(domTable, weekLabels) {
  this.__addHeaderTo(domTable, weekLabels);
  this.__addBodyTo(domTable);
  this.__updateGrid(domTable);
};

/**
 * Allows day indices in a week to be changed.
 * E.g. 0 becomes MON instead of SUN
 * NOTE: This is just a future possibility. Currently this method still
 *      does nothing.
 *
 * @param {int} origDayOfWeekId the original day index
 * @return the day index after translation
 * @type int
 * @private
 */
uiCalendar_Suite.prototype.__translateToCustomDayIndex = function(origDayOfWeekId) {
  return origDayOfWeekId;  // Currently custom day index not allowed.
};

/**
 * Adds DOM objects to the supplied <code>gridTable</code> to create the
 * table's header.
 *
 * @param {HTMLTableElement} gridTable
 * @param {String[]} weekLabels
 * @private
 */
uiCalendar_Suite.prototype.__addHeaderTo = function(gridTable, weekLabels) {
  var dayNameRow = this.__addRowTo(gridTable);

  var maxDayId = uiUtil_Calendar.NUM_DAYS_IN_A_WEEK;
  for (var dayOfWeekId = 0; dayOfWeekId < maxDayId; ++dayOfWeekId) {
    var dayOfThisCell = this.__translateToCustomDayIndex(dayOfWeekId);
    var cell = this.__addCellTo(dayNameRow, weekLabels[dayOfThisCell]);
    var classOfThisCell = this.__classResolver.getForHeader(dayOfWeekId);
    cell.className = classOfThisCell;
  }
};

/**
 * Appends a row to the supplied <code>table</code>.
 *
 * @param {HTMLTableElement} table
 * @return the newly added DOM row
 * @type HTMLTableRowElement
 * @private
 */
uiCalendar_Suite.prototype.__addRowTo = function(table) {
  return table.insertRow(-1);
};

/**
 * Appends a cell containing the supplied <code>text</code> to the supplied
 * <code>row</code>.
 *
 * @param {HTMLTableRowElement} row
 * @param {String} text
 * @return the newly added DOM cell
 * @type HTMLTableCellElement
 * @private
 */
uiCalendar_Suite.prototype.__addCellTo = function(row, text) {
  var cell = row.insertCell(-1);
  uiHtml_Document.createTextNode(text, cell);
  return cell;
};

/**
 * Adds DOM objects to the supplied <code>gridTable</code> to create a table
 * body containing rows with empty cells -- each contains a whitespace.
 *
 * @param {HTMLTableElement} gridTable
 * @private
 */
uiCalendar_Suite.prototype.__addBodyTo = function(gridTable) {
  var rowStart = uiCalendar_Suite.GRID_DATA_ROW_START;
  var rowCount = uiCalendar_Suite.GRID_TOTAL_ROW_COUNT;
  for (var row = rowStart; row < rowCount; row++) {
    var currentRow = this.__addRowTo(gridTable);
    for (var col=0; col < uiCalendar_Suite.GRID_TOTAL_COL_COUNT; col++) {
      this.__addCellTo(currentRow, " ");
    }
  }
};

/**
 * Updates all the calendar's year and month listers. This method is a
 * callback that gets called when the selected date changes.
 *
 * @param {uiUtil_Calendar} date the new date
 */
uiCalendar_Suite.prototype.dateUpdated = function(date) {
  for (var i = 0; i < this.__yearListers.length; i++) {
    this.__updateYearLister(this.__yearListers[i], date);
  }
  for (var i = 0; i < this.__monthListers.length; i++) {
    this.__updateMonthLister(this.__monthListers[i]);
  }
};

/**
 * To be called when the selected date has changed, this method updates
 * the year lister's options. This method doesn't fire the year lister's
 * onchange event.
 *
 * @param {uiCalendar_Suite.YearLister} lister to update
 * @private
 */
uiCalendar_Suite.prototype.__updateYearLister = function(lister, date) {
  var select = lister.__select;

  select.clearItems();
  var years = lister.__strategy.getYearArray(this.__selectedDate.getYear());
  for (var i = 0; i < years.length; ++i) {
    select.addItem(uiHtml_SelectOptionWrapper.create(years[i], years[i]));
  }
  select.setSelectedValue(this.__selectedDate.getYear());
};

/**
 * Registers a year lister to this calendar. A <i>year lister</i> is a
 * specific type of <i>date updater</i>.
 *
 * @param {uiHtml_Select} select the year lister
 * @param {uiCalendar_SelectedDateObtainerStrategy} strategy
 * @see #_addUpdater
 */
uiCalendar_Suite.prototype._addYearLister = function(select, strategy) {
  var lister = new uiCalendar_Suite.YearLister(select, strategy);
  this.__yearListers.push(lister);

  this._addUpdater(select, "change",
      new uiCalendar_Suite.YearExtractor(select.getDomObject()));

  this.__updateYearLister(lister);
};

/**
 * To be called when the selected date has changed, this method updates
 * the selected month in the supplied month lister. This doesn't fire
 * the month lister's onchange event.
 *
 * @param {uiCalendar_Suite.MonthLister} lister
 * @private
 */
uiCalendar_Suite.prototype.__updateMonthLister = function(lister) {
  lister.__select.setSelectedValue(this.__selectedDate.getMonth());
};

/**
 * Registers a month lister to this calendar. A <i>month lister</i> is a
 * specific type of <i>date updater</i>.
 *
 * @param {uiHtml_Select} select the month lister
 * @param {String[]} monthLabels the lister's option labels
 * @see #_addUpdater
 */
uiCalendar_Suite.prototype._addMonthLister = function(select, monthLabels) {
  var lister = new uiCalendar_Suite.MonthLister(select);
  this.__monthListers.push(lister);

  select.clearItems();
  for (var i = 0; i < monthLabels.length; ++i) {
    select.addItem(uiHtml_SelectOptionWrapper.create(monthLabels[i], i));
  }

  this._addUpdater(select, "change",
      new uiCalendar_Suite.MonthExtractor(select.getDomObject()));

  this.__updateMonthLister(lister);
};

/**
 * Registers a <i>date updater</i> to this calendar.
 * A <i>date updater</i> is a logical collection of 3 things: a widget which
 * triggers an event, the type of event triggered, and the strategy to invoke
 * to get the new value of selected date.
 * <p>
 * A date updater allows this calendar to know when update its selected date
 * and how to obtain it.
 *
 * @param {uiHtml_Element} trigger the element whose event firing causes
 *     this calendar's selected date to be updated
 * @param {String} event the event that triggers the update
 * @param {uiCalendar_SelectedDateObtainerStrategy} strategy
 */
uiCalendar_Suite.prototype._addUpdater = function(trigger, event, strategy) {
  var suite = this;
  trigger.appendEventHandler(event, function(e) {
    var jsSelectedDate = new Date(suite.__selectedDate.toDate());
    var jsNewDate = strategy.getNewDate(jsSelectedDate);
    suite.update(jsNewDate);
  });
};

/**
 * Updates the selected date (to have the same value as <code>jsNewDate</code>)
 * as well as the calendar grid.
 *
 * @param {Date} jsNewDate the <code>Date</code> to copy fields from
 */
uiCalendar_Suite.prototype.update = function(jsNewDate) {
  // NOTE: Don't bother updating if they are the same, since this will
  //       cause infinite recursion, because changing the date will
  //       trigger notification on the listers (year/month), which in
  //       turn will execute this again.
  var newDate = new uiUtil_Calendar(jsNewDate);
  if (this.__selectedDate.getDay() != newDate.getDay() ||
      this.__selectedDate.getMonth() != newDate.getMonth() ||
      this.__selectedDate.getYear() != newDate.getYear()) {
    this.__selectedDate.fromDate(jsNewDate);
    this.__updateGrid(this.__domTable);
  }
};

/**
 * Updates the grid cells to reflect the currently selected date.
 *
 * @param {HTMLTableElement} gridTable the calendar grid to update
 * @private
 */
uiCalendar_Suite.prototype.__updateGrid = function(gridTable) {
  var firstDayInMonth = this.__selectedDate.getFirstDayInMonth();
  var numDaysInMonth = this.__selectedDate.getNumDaysInMonth();

  var currentDate = new uiUtil_Calendar(this.__selectedDate.toDate());
  currentDate.setDay(1);
  for (var i = 0; i <= firstDayInMonth; ++i) {
    // Some of the rendered days are from the previous month
    currentDate.decrementDay();
  }

  var rowIndex = uiCalendar_Suite.GRID_DATA_ROW_START;
  var rowCount = uiCalendar_Suite.GRID_TOTAL_ROW_COUNT;
  for (var row = rowIndex; row < rowCount; ++row) {  // Remaining rows
    currentRow = gridTable.rows[row];
    for (var col=0; col < uiUtil_Calendar.NUM_DAYS_IN_A_WEEK; ++col) {
      currentDate.incrementDay();
      this.__updateCellData(currentRow.cells[col], currentDate);
    }
  }
};

/**
 * Updates the date of the supplied <code>cell</code>.
 *
 * @param {HTMLTableCellElement} cell the cell to update
 * @param {uiUtil_Calendar} date the value for the cell
 * @private
 */
uiCalendar_Suite.prototype.__updateCellData = function(cell, date) {
  this.__detachCellClickHandler(cell);
  this.__elementHandler.clearChildDomObjects(cell);

  if (date == null) {
    uiHtml_Document.createTextNode(" ", cell);
  }
  else {
    uiHtml_Document.createTextNode(date.getDay(), cell);
    this.__attachCellClickHandler(cell, date);
    this.__updateCellClass(cell, date);
  }
};

/**
 * Updates CSS the class of the supplied <code>cell</code>.
 *
 * @param {HTMLTableCellElement} cell the cell to update
 * @param {uiUtil_Calendar} date used to determine which CSS class the
 *     cell gets
 * @private
 */
uiCalendar_Suite.prototype.__updateCellClass = function(cell, date) {
  var cssClass = this.__classResolver.getForDate(
      date.toDate(), this.__selectedDate.toDate());
  cell.className = cssClass;
};

/**
 * Attaches event handlers of a certain cell.
 *
 * @param {HTMLTableCellElement} cell the cell to which the handler to
 *     be attached
 * @param {uiUtil_Calendar} date used to determine the handler
 * @private
 */
uiCalendar_Suite.prototype.__attachCellClickHandler = function(domCell, date) {
  if (this.__actionResolver == null) {
    return null;
  }

  var eventNames = this.__actionResolver.getEventsOfInterest();
  for (var i = 0; i < eventNames.length; ++i) {
    var action = this.__actionResolver.getAction(
        eventNames[i], this, date.toDate(), this.__selectedDate.toDate());
    if (action != null) {
      this.__elementHandler.appendEventHandler(domCell, eventNames[i], action);
    }
  }
};

/**
 * Removes event handlers of a certain cell.
 *
 * @param {HTMLTableCellElement} cell the cell from which the previously
 *     attached handler to be removed
 * @private
 */
uiCalendar_Suite.prototype.__detachCellClickHandler = function(domCell) {
  if (this.__actionResolver == null) {
    return;
  }

  var eventNames = this.__actionResolver.getEventsOfInterest();
  for (var i = 0; i < eventNames.length; ++i) {
    this.__elementHandler.clearEventHandlerExtension(domCell, eventNames[i]);
  }
};


///////////////////////////////////
////////// Inner Classes //////////
///////////////////////////////////

/**
 * @class Groups together a year lister and its date obtainer strategy.
 *     This is a "data container" to be used internally.
 * @param {uiHtml_Select} select
 * @param {uiCalendar_YearListObtainerStrategy} strategy
 */
function uiCalendar_Suite$YearLister(select, strategy) {
  this.__select = select;
  this.__strategy = strategy;

  this.__select.enableOptionValueMapping();
}

/** @ignore */
uiCalendar_Suite.YearLister =
    uiUtil_Object.declareClass(uiCalendar_Suite$YearLister, uiUtil_Object);



/**
 * @class This is a "data container" to be used internally. This class
 * is not really required but provided for consistency with the above
 * <code>uiCalendar_Suite.YearLister</code>.
 * @param {uiHtml_Select} select
 */
function uiCalendar_Suite$MonthLister(select) {
  this.__select = select;

  this.__select.enableOptionValueMapping();
}

/** @ignore */
uiCalendar_Suite.MonthLister =
    uiUtil_Object.declareClass(uiCalendar_Suite$MonthLister, uiUtil_Object);



/**
 * @class A strategy that uses the value from the <code>domMonthInput</code>
 * widget as the selected date's new month.
 * @extends uiCalendar_SelectedDateObtainerStrategy
 * @param {HTMLElement} domMonthInput the widget whose value is to be taken as
 *     the selected date's new month
 */
function uiCalendar_Suite$MonthExtractor(domMonthInput) {
  this.__domMonthInput = domMonthInput;
}

/** @private */
uiCalendar_Suite.MonthExtractor = uiUtil_Object.declareClass(
    uiCalendar_Suite$MonthExtractor, uiUtil_Object);

/** @ignore */
uiCalendar_Suite.MonthExtractor.prototype.getNewDate = function(selectedDate) {
  selectedDate.setMonth(this.__domMonthInput.value);
  return selectedDate;
};



/**
 * @class A strategy that uses the value from the <code>domYearInput</code>
 * widget as the selected date's new year.
 * @extends uiCalendar_SelectedDateObtainerStrategy
 * @param {HTMLElement} domYearInput the widget whose value is to be taken as
 *     the selected date's new year
 */
function uiCalendar_Suite$YearExtractor(domYearInput) {
  this.__domYearInput = domYearInput;
}

/** @private */
uiCalendar_Suite.YearExtractor = uiUtil_Object.declareClass(
    uiCalendar_Suite$YearExtractor, uiUtil_Object);

/** @ignore */
uiCalendar_Suite.YearExtractor.prototype.getNewDate = function(selectedDate) {
  selectedDate.setYear(this.__domYearInput.value);
  return selectedDate;
};
