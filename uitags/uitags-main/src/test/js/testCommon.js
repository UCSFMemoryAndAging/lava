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

var uiGlobal_sourceDir = '../../main/js/';
uiGlobal_loadClasses();

// relative path depth is different from the source base path,
// which is pretty wierd -- we can't do anything since this is
// what jsUnit requires
function getTestBasePath() {
  return '../';
}

function uiGlobal_includeJsFile(filePath){
  var scriptTag = document.createElement('script');
  scriptTag.setAttribute('type', 'text/javascript');
  scriptTag.setAttribute('src', filePath);

  var headTag = document.getElementsByTagName('head').item(0);
  headTag.appendChild(scriptTag);
}

function uiGlobal_includeJsTestFile(filename){
  uiGlobal_includeJsFile(uiGlobal_sourceDir + filename);
}

function uiGlobal_loadClasses() {
  uiGlobal_includeJsFile('jsunit/app/jsUnitCore.js');

  uiGlobal_includeJsTestFile("util/Object.js");
  uiGlobal_includeJsTestFile("util/Exception.js");
  uiGlobal_includeJsTestFile("util/Type.js");
  uiGlobal_includeJsTestFile("util/ArrayUtils.js");
  uiGlobal_includeJsTestFile("util/Logger.js");
  uiGlobal_includeJsTestFile("util/Dimension.js");
  uiGlobal_includeJsTestFile("util/Calendar.js");

  uiGlobal_includeJsTestFile("html/Element.js");
  uiGlobal_includeJsTestFile("html/ScrollSupporter.js");
  uiGlobal_includeJsTestFile("html/PrintSupporter.js");
  uiGlobal_includeJsTestFile("html/Event.js");

  uiGlobal_includeJsTestFile("html/Window.js");
  uiGlobal_includeJsTestFile("html/Document.js");
  uiGlobal_includeJsTestFile("html/ElementWrapper.js");
  uiGlobal_includeJsTestFile("html/RadioWrapper.js");
  uiGlobal_includeJsTestFile("html/CheckBoxWrapper.js");
  uiGlobal_includeJsTestFile("html/SelectOptionWrapper.js");
  uiGlobal_includeJsTestFile("html/Group.js");
  uiGlobal_includeJsTestFile("html/Select.js");
  uiGlobal_includeJsTestFile("html/Toggle.js");
  uiGlobal_includeJsTestFile("html/Panel.js");
  uiGlobal_includeJsTestFile("html/DebugPanel.js");

  uiGlobal_includeJsTestFile("optionTransfer/Suite.js");
  uiGlobal_includeJsTestFile("optionTransfer/Driver.js");

  uiGlobal_includeJsTestFile("panel/Suite.js");
  uiGlobal_includeJsTestFile("panel/Driver.js");

  uiGlobal_includeJsTestFile("formGuide/Observed.js");
  uiGlobal_includeJsTestFile("formGuide/Rule.js");
  uiGlobal_includeJsTestFile("formGuide/RuleSet.js");
  uiGlobal_includeJsTestFile("formGuide/Driver.js")

  uiGlobal_includeJsTestFile("select/Suite.js");
  uiGlobal_includeJsTestFile("select/Driver.js");

  uiGlobal_includeJsTestFile("sort/Suite.js");
  uiGlobal_includeJsTestFile("sort/Driver.js");

  uiGlobal_includeJsTestFile("shift/Suite.js");
  uiGlobal_includeJsTestFile("shift/Driver.js");

  uiGlobal_includeJsTestFile("calendar/Suite.js");
  uiGlobal_includeJsTestFile("calendar/Driver.js");
  uiGlobal_includeJsTestFile("calendar/ActionResolverStrategy.js");
  uiGlobal_includeJsTestFile("calendar/CssClassResolverStrategy.js");
  uiGlobal_includeJsTestFile("calendar/SelectedDateObtainerStrategy.js");
  uiGlobal_includeJsTestFile("calendar/WidgetDateObtainerStrategy.js");
  uiGlobal_includeJsTestFile("calendar/YearListObtainerStrategy.js");
}


///// Utility class to assist the tests /////

function uiTest_TestUtils() {
}

uiTest_TestUtils.__lockReleased = false;

// NOTE: Use this function to trigger a click event to avoid
//       problems in Opera where event handlings are done
//       asynchronously.
uiTest_TestUtils.synchronousTrigger = function(domElement, event) {
  uiTest_TestUtils.__lockReleased = false;
  domElement[event]();
  if (window.opera) {
    while (!uiTest_TestUtils.__lockReleased);
  }
}

uiTest_TestUtils.synchronousClick = function(domElement) {
  uiTest_TestUtils.synchronousTrigger(domElement, "click");
}

uiTest_TestUtils.releaseEventLock = function() {
  uiTest_TestUtils.__lockReleased = true;
}
