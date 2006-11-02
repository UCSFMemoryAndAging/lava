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
 * Creates a default strategy object.
 *
 * @class The default strategy for resolving the action to perform when a
 *     cell in the calendar grid is clicked. One can provide a
 *     custom strategy by extending this class and redefining public
 *     and protected methods defined here.
 * @extends uiUtil_Object
 * @param {String} inputId the ID of the widget to update when a calendar grid
 *     cell is clicked (exclusive with <code>inputName</code>)
 * @param {String} inputName the name of the widget to update when a calendar
 *     grid cell is clicked (exclusive with <code>inputId</code>)
 * @param {String} optPanelId an optional argument specifying the ID of the
 *     calendar's containing panel to hide when a calendar grid cell is clicked.
 *     If no such panel, provide <code>null</code>.
 * @param {String} optFormat an optional argument specifying the format of the
 *     date to use. Default is "MM/dd/yyyy".
 */
function uiCalendar_ActionResolverStrategy(inputId, inputName, optPanelId, optFormat) {
  this._super();
  /**
   * @type HTMLElement
   * @private
   */
  var input = uiHtml_Element.createByEither(inputId, inputName);
  this.__domInput = input.getDomObject();
  // Don't resolve the panel using panelId now because the panel may not have
  // been created at this point.
  /**
   * @type String
   * @private
   */
  this.__panelId = optPanelId;
  /**
   * @type String
   * @private
   */
  this.__format = uiUtil_Type.getString(optFormat, "MM/dd/yyyy");
  /**
   * @type String
   * @private
   */
  this.__origColor = null;
}

uiCalendar_ActionResolverStrategy =
    uiUtil_Object.declareClass(uiCalendar_ActionResolverStrategy, uiUtil_Object);

/**
 * Returns a function to be executed when the cell containing the
 * supplied <code>processedDate</code> is clicked.
 * This default implementation updates the widget whose ID was passed into
 * this class' constructor with the <code>processedDate</code>
 * and hides the calendar's containing panel (if there is such panel).
 *
 * @param {String} eventName the event to respond to
 * @param {uiCalendar_Suite} calendarSuite the relevant calendar suite
 * @param {Date} processedDate the date that is currently being processed
 * @param {Date} selectedDate the date that is currently selected (by
 *     the calendar suite)
 * @return a function to be executed when the cell containing the
 *     supplied <code>processedDate</code> is clicked
 * @type function
 */
uiCalendar_ActionResolverStrategy.prototype.getAction = function(
    eventName, calendarSuite, processedDate, selectedDate) {
  switch (eventName) {
    case "click" :
        return this.__getClickAction(
            calendarSuite, processedDate, selectedDate);
    case "mouseover" :
        return this.__getMouseOverAction(processedDate, selectedDate);
    case "mouseout" :
        return this.__getMouseOutAction(processedDate, selectedDate);
  }
};

/**
 * Checks if two dates are in the same month.
 *
 * @param {Date} date1 the first date
 * @param {Date} date2 the second date
 * @return <code>true</code> if both dates are in the same month,
 *     <code>false</code> otherwise
 * @type boolean
 * @private
 */
uiCalendar_ActionResolverStrategy.prototype.__inTheSameMonth = function(
    date1, date2) {
  if (date1.getMonth() == date2.getMonth()) {
    return true;
  }
  return false;
};

/**
 * Returns the click event handler for a grid cell that contains the
 * <code>processedDate</code> value.
 *
 * @param {uiCalendar_Suite} calendarSuite the relevant calendar suite
 * @param {Date} processedDate the date that is currently being processed
 * @param {Date} selectedDate the date that is currently selected (by
 *     the calendar suite)
 * @return the click handler
 * @type function
 * @private
 */
uiCalendar_ActionResolverStrategy.prototype.__getClickAction = function(
    calendarSuite, processedDate, selectedDate) {
  if (!this.__inTheSameMonth(processedDate, selectedDate)) {
    return null;
  }

  // NOTE: When the event handler gets executed, "this" refers to the owner
  // of the handler (i.e. grid cell) instead of this object, thus we need to
  // save this object reference to a different variable name.
  var strategy = this;
  return function(e) {
    calendarSuite.update(processedDate);
    strategy.__domInput.value = (new uiUtil_Calendar(
        processedDate)).format(strategy.__format);
    if (strategy.__panelId != null) {
      var panel = uiPanel_driver.getSuite(strategy.__panelId);
      if (panel != null) {
        panel.requestHide(e, true);
      }
    }
  };
};

/**
 * Returns the mouseover event handler for a grid cell that contains the
 * <code>processedDate</code> value.
 *
 * @param {Date} processedDate the date that is currently being processed
 * @param {Date} selectedDate the date that is currently selected (by
 *     the calendar suite)
 * @return the click handler
 * @type function
 * @private
 */
uiCalendar_ActionResolverStrategy.prototype.__getMouseOverAction = function(
    processedDate, selectedDate) {
  if (!this.__inTheSameMonth(processedDate, selectedDate)) {
    return null;
  }
  return function(e) {
    this.__origColor = this.style.backgroundColor;
    this.style.backgroundColor = "#ffd0d7";
  };
};

/**
 * Returns the mouseout event handler for a grid cell that contains the
 * <code>processedDate</code> value.
 *
 * @param {Date} processedDate the date that is currently being processed
 * @param {Date} selectedDate the date that is currently selected (by
 *     the calendar suite)
 * @return the click handler
 * @type function
 * @private
 */
uiCalendar_ActionResolverStrategy.prototype.__getMouseOutAction = function(
    processedDate, selectedDate) {
  if (!this.__inTheSameMonth(processedDate, selectedDate)) {
    return null;
  }
  return function(e) {
    this.style.backgroundColor = this.__origColor;
  };
};

/**
 * Returns an array of the name of all the events that this strategy wants
 * to specify handlers for.
 *
 * @return the array
 * @type String[]
 */
uiCalendar_ActionResolverStrategy.prototype.getEventsOfInterest = function() {
  return new Array("click", "mouseover", "mouseout");
};
