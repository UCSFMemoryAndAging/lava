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
 * Creates a strategy for extracting a date from the value of a form widget.
 * The value to be parsed is expected to be in the format specified by
 * <code>optFormat</code> argument. If <code>optFormat</code> is not
 * specified, the assumed format is "MM/dd/yyyy".
 *
 * @class A strategy for obtaining a date from a form widget.
 * @extends uiCalendar_SelectedDateObtainerStrategy
 * @param {String} inputId ID of the form widget (exclusive with
 *     <code>inputName</code>)
 * @param {String} inputName name of the form widget (exclusive with
 *     <code>inputId</code>)
 * @param {String} optFormat the optional date format
 */
function uiCalendar_WidgetDateObtainerStrategy(inputId, inputName, optFormat) {
  this._super();

  /**
   * The form widget containing the value to be parsed.
   *
   * @type HTMLElement
   * @private
   */
  var input = uiHtml_Element.createByEither(inputId, inputName);
  this.__domInput = input.getDomObject();
  /**
   * The expected date format.
   *
   * @type String
   * @private
   */
  this.__format = uiUtil_Type.getString(optFormat, "MM/dd/yyyy");
}

uiCalendar_WidgetDateObtainerStrategy = uiUtil_Object.declareClass(
    uiCalendar_WidgetDateObtainerStrategy, uiUtil_Object);

/**
 * Returns the date extracted from the widget value.
 *
 * @see #uiCalendar_WidgetDateObtainerStrategy
 * @param {Date} selectedDate currently selected date.
 * @return the date to be set as the selected date
 * @type Date
 */
uiCalendar_WidgetDateObtainerStrategy.prototype.getNewDate =
    function(selectedDate) {
  try {
    var newDate = uiUtil_Calendar.createFromString(
        this.__domInput.value, this.__format);
    return newDate.toDate();
  }
  catch (e) {
    if (e instanceof uiUtil_CreateException) {
      alert("Invalid date: " + this.__domInput.value +
          ". Make sure that the date format is correct.");
      return new Date();
    }
    else {
      throw e;
    }
  }
};
