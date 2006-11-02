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
 * Creates an object to support an element's scrolling functionality.
 *
 * @class A supporter for the scrolling functionality of an element.
 * @extends uiUtil_Object
 * @param {HTMLElement} domElement the element
 */
function uiHtml_ScrollSupporter(domElement) {
  this._super();

  this.__domElement = domElement;
}

uiHtml_ScrollSupporter =
    uiUtil_Object.declareClass(uiHtml_ScrollSupporter, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

/**
 * Shows vertical and horizontal scroll bars. Unless <code>optAlways</code>
 * is specified and is equal to <code>true</code>, the scrollbars will only
 * be shown when the content overflows the element's area.
 *
 * @param {boolean} optAlways an optional parameter indicating whether to
 *     always show the scroll bars or to show them only when the element's
 *     content overflows
 */
uiHtml_ScrollSupporter.prototype.showScrollBars = function(optAlways) {
  // Use 'auto' as the default mode.
  this.__domElement.style.overflow = ((optAlways == true) ? "scroll" : "auto");
};

/**
 * Hides both vertical and horizontal scrollbars, no matter whether the
 * element's content overflows its area or not.
 */
uiHtml_ScrollSupporter.prototype.hideScrollBars = function() {
  this.__domElement.style.overflow = "hidden";
};

/**
 * Scrolls the vertical scroll bar to the top of the element.
 */
uiHtml_ScrollSupporter.prototype.scrollToTop = function() {
  this.__domElement.scrollTop = 0;
};

/**
 * Scrolls the vertical scroll bar to the bottom of the element.
 */
uiHtml_ScrollSupporter.prototype.scrollToBottom = function() {
  this.__domElement.scrollTop = this.__domElement.scrollHeight;
};

/**
 * Returns the offset of the horizontal scroll bar.
 *
 * @return the horizontal offset
 * @type int
 */
uiHtml_ScrollSupporter.prototype.getScrollLeft = function() {
  return this.__domElement.scrollLeft;
};

/**
 * Returns the offset of the vertical scroll bar.
 *
 * @return the vertical offset
 * @type int
 */
uiHtml_ScrollSupporter.prototype.getScrollTop = function() {
  return this.__domElement.scrollTop;
};
