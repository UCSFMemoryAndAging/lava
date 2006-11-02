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

function uiHtml_PrintSupporter(domElement) {
  this._super();

  this.__domElement = domElement;
  this.__textNode = uiHtml_Document.createTextNode("", domElement);
}

uiHtml_PrintSupporter =
    uiUtil_Object.declareClass(uiHtml_PrintSupporter, uiUtil_Object);


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiHtml_PrintSupporter.prototype.print = function(text) {
  this.__textNode.appendData(text);
};

uiHtml_PrintSupporter.prototype.println = function(text) {
  this.print(text);

  // print the new line
  uiHtml_Document.createDomObject("br", true, this.__domElement);
  this.__textNode = uiHtml_Document.createTextNode("", this.__domElement);
};
