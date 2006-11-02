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
 * @class The default strategy for providing the <i>year lister</i>'s
 *     (a select box) options when the selected year changes. One can
 *     provide a custom strategy by extending this class and redefining
 *     public and protected methods defined here.
 * @extends uiUtil_Object
 */
function uiCalendar_YearListObtainerStrategy() {
  this._super();
}

uiCalendar_YearListObtainerStrategy = uiUtil_Object.declareClass(
    uiCalendar_YearListObtainerStrategy, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Returns a new list of select options for the year lister.
 * This default implementation returns an array of year values with the
 * selected year located in the middle of the array, thus allowing the
 * user to navigate to the previous and next few years.
 *
 * @param {int} selectedYear the year lister's currently selected value
 * @return an array of year values to populate the year lister with
 * @type int[]
 */
uiCalendar_YearListObtainerStrategy.prototype.getYearArray =
    function(selectedYear) {
  var yearArray = new Array();
  for (var i = 5; i > 0; i--) {
    yearArray.push(selectedYear - i);
  }
  yearArray.push(selectedYear);
  for (var i = 1; i <= 5; i++) {
    yearArray.push(selectedYear + i);
  }

  return yearArray;
};
