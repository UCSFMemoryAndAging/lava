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
 * @class Facade to the calendar suite.
 * @extends uiUtil_Object
 */
function uiCalendar_Driver() {
  this._super();
  /**
   * @type uiCalendar_Suite[]
   * @private
   */
  this.__suites = new Array();
}

uiCalendar_Driver = uiUtil_Object.declareSingleton(uiCalendar_Driver, uiUtil_Object);

uiHtml_Window.getInstance().prependEventHandler("load", function(e) {
  uiCalendar_driver = new uiCalendar_Driver();
});


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Creates a calendar suite.
 *
 * @param {String} calendarGridId ID of the grid in the calendar suite
 * @param {String[]} weekLabels week labels to be displayed in the grid
 *     header
 * @param {uiCalendar_CssClassResolverStrategy} classResolver a strategy
 *     for resolving CSS classes for each cell in the grid
 * @param {uiCalendar_ActionResolverStrategy} actionResolver a strategy
 *     for resolving the action to take when a certain event is triggered
 *     on each cell in the grid
 * @return ID of the created suite
 * @type int
 */
uiCalendar_Driver.prototype.createSuite = function(
    calendarGridId, weekLabels, classResolver, actionResolver) {

  var domTable = uiHtml_Document.getInstance().getDomObjectById(calendarGridId);
  var suite = new uiCalendar_Suite(
      domTable, weekLabels, classResolver, actionResolver);

  var suiteId = this.__suites.length;
  this.__suites[suiteId] = suite;
  return suiteId;
};

/**
 * Registers a trigger for updating the calendar's selected date. Either of
 * <code>triggerId</code> or <code>triggerName</code> has to be specified.
 *
 * @param {int} suiteId ID of the calendar suite
 * @param {String} triggerId ID of the trigger
 * @param {String} triggerName name of the trigger
 * @param {String} eventName name of the triggered event
 * @param {uiCalendar_SelectedDateObtainerStrategy} strategy a strategy
 *     for dynamically obtaining the calendar's selected date
 */
uiCalendar_Driver.prototype.registerUpdateTrigger = function(
    suiteId, triggerId, triggerName, eventName, strategy) {
  var trigger = uiHtml_Element.createByEither(triggerId, triggerName);
  this.__suites[suiteId]._addUpdater(trigger, eventName, strategy);
};

/**
 * Registers a year lister -- a specific type of update trigger that only
 * updates the year of the calendar. Either of <code>listerId</code> or
 * <code>listerName</code> has to be specified.
 *
 * @param {int} suiteId ID of the calendar suite
 * @param {String} listerId ID of the lister
 * @param {String} listerName name of the lister
 * @param {uiCalendar_YearListObtainerStrategy} strategy a strategy
 *     for dynamically obtaining a list of years to be displayed
 */
uiCalendar_Driver.prototype.registerYearLister = function(
    suiteId, listerId, listerName, strategy) {
  var select = uiHtml_Select.createByEither(listerId, listerName);
  this.__suites[suiteId]._addYearLister(select, strategy);
};

/**
 * Registers a month lister -- a specific type of update trigger that only
 * updates the month of the calendar. Either of <code>listerId</code> or
 * <code>listerName</code> has to be specified.
 *
 * @param {int} suiteId ID of the calendar suite
 * @param {String} listerId ID of the lister
 * @param {String} listerName name of the lister
 * @param {String[]} monthLabels the labels to be displayed by the lister
 */
uiCalendar_Driver.prototype.registerMonthLister = function(
    suiteId, listerId, listerName, monthLabels) {
  var select = uiHtml_Select.createByEither(listerId, listerName);
  this.__suites[suiteId]._addMonthLister(select, monthLabels);
};
