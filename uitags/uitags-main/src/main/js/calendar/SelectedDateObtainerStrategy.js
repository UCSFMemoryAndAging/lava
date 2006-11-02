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
 * @class The default strategy for obtaining a date to be set as
 *     the selected date. One can provide a custom strategy by extending
 *     this class and redefining public and protected methods defined here.
 * @extends uiUtil_Object
 */
function uiCalendar_SelectedDateObtainerStrategy() {
  this._super();
}

uiCalendar_SelectedDateObtainerStrategy =
    uiUtil_Object.declareClass(uiCalendar_SelectedDateObtainerStrategy, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Returns the date to be set as the selected date. This default implementation
 * always returns <code>new Date()</code>.
 *
 * @param {Date} selectedDate currently selected date. This argument may be used
 *     to calculate the return value.
 * @return the date to be set as the selected date
 * @type Date
 */
uiCalendar_SelectedDateObtainerStrategy.prototype.getNewDate = function(selectedDate) {
  return new Date();
};
