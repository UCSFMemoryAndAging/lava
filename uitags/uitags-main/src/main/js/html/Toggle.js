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

function uiHtml_Toggle(triggerOn, triggerOff, initialIsOn, toggleEvent) {
  this._super();

  this.__triggerOn = triggerOn;
  this.__debugId = this.__triggerOn.getDomObject().id;
  this.__onPropertyArray = new Array();
  this.__offPropertyArray = new Array();
  this.__toggleEvent = uiUtil_Type.getString(
      toggleEvent, uiHtml_Toggle.DEFAULT_EVENT);

  if (triggerOff != null) {  // with two triggers
    this.__triggerOff = triggerOff;

    this.__strategyDisplayOn = this.__doubleDisplayOn;
    this.__strategyDisplayOff = this.__doubleDisplayOff;
    this.__strategyAppendOnStateOn = this.__doubleAppendOnStateOn;
    this.__strategyAppendOnStateOff = this.__doubleAppendOnStateOff;
    this.__strategyPrependOnStateOn = this.__doublePrependOnStateOn;
    this.__strategyPrependOnStateOff = this.__doublePrependOnStateOff;

    var toggle = this;
    this.__triggerOn.prependEventHandler(this.__toggleEvent, function(e) {
      toggle.switchOff();
    });
    this.__triggerOff.prependEventHandler(this.__toggleEvent, function(e) {
      toggle.switchOn();
    });
  }
  else {  // with only one trigger
    this.__triggerOff = this.__triggerOn;  // the same trigger for on and off

    this.__strategyDisplayOn = this.__singleDisplayOn;
    this.__strategyDisplayOff = this.__singleDisplayOff;
    this.__strategyAppendOnStateOn = this.__singleAppendOnStateOn;
    this.__strategyAppendOnStateOff = this.__singleAppendOnStateOff;
    this.__strategyPrependOnStateOn = this.__singlePrependOnStateOn;
    this.__strategyPrependOnStateOff = this.__singlePrependOnStateOff;

    // NOTE: it is important to prepend the event handler rather than
    //       to append to make sure that the state is changed before
    //       the execution of any other handlers
    // NOTE: it is possible that both triggerOn and triggerOff refer to
    //       the same trigger, in which case we can't directly determine
    //       what state to switch to, which is why we rely on switchState
    //       function to do it
    var toggle = this;
    this.__triggerOn.prependEventHandler(this.__toggleEvent, function(e) {
      toggle._switchState();
    });
  }

  if (uiUtil_Type.getBoolean(initialIsOn, true)) {
    this.switchOn();
  }
  else {
    this.switchOff();
  }
}

// NOTE: at the moment toggle extends from object, NOT element
uiHtml_Toggle = uiUtil_Object.declareClass(uiHtml_Toggle, uiUtil_Object);

uiHtml_Toggle.DEFAULT_EVENT = "click";
uiHtml_Toggle.__toggles = new Array();


////////////////////////////////////////
////////// Non-static Methods //////////
////////////////////////////////////////

uiHtml_Toggle.prototype._switchState = function() {
  if (this.__stateOn) {
    this.switchOff();
  }
  else {
    this.switchOn();
  }
};

uiHtml_Toggle.prototype.switchOn = function() {
  this.__logger.info("switching on " + this.__debugId);
  this.__stateOn = true;
  this.__strategyDisplayOn();

  var domTrigger = this.__triggerOn.getDomObject();
  for (prop in this.__onPropertyArray) {
    domTrigger[prop] = this.__onPropertyArray[prop];
  }
};

uiHtml_Toggle.prototype.switchOff = function() {
  this.__logger.info("switching off " + this.__debugId);
  this.__stateOn = false;
  this.__strategyDisplayOff();

  var domTrigger = this.__triggerOff.getDomObject();
  for (prop in this.__offPropertyArray) {
    domTrigger[prop] = this.__offPropertyArray[prop];
  }
};

uiHtml_Toggle.prototype.appendOnStateOn = function(eventHandler) {
  this.__strategyAppendOnStateOn(eventHandler);
};

uiHtml_Toggle.prototype.appendOnStateOff = function(eventHandler) {
  this.__strategyAppendOnStateOff(eventHandler);
};

uiHtml_Toggle.prototype.setOnProperties = function(propArray) {
  this.__onPropertyArray = propArray;
};

uiHtml_Toggle.prototype.setOffProperties = function(propArray) {
  this.__offPropertyArray = propArray;
};


//////////////////////////////////////
////////// Strategy Methods //////////
//////////////////////////////////////

// strategies for toggle with two triggers
uiHtml_Toggle.prototype.__doubleDisplayOn = function() {
  this.__triggerOff.disappear();
  this.__triggerOn.appear();
};

uiHtml_Toggle.prototype.__doubleDisplayOff = function() {
  this.__triggerOn.disappear();
  this.__triggerOff.appear();
};

uiHtml_Toggle.prototype.__doublePrependOnStateOn = function(eventHandler) {
  this.__triggerOff.prependEventHandler(this.__toggleEvent, eventHandler);
};

uiHtml_Toggle.prototype.__doublePrependOnStateOff = function(eventHandler) {
  this.__triggerOn.prependEventHandler(this.__toggleEvent, eventHandler);
};

uiHtml_Toggle.prototype.__doubleAppendOnStateOn = function(eventHandler) {
  this.__triggerOff.appendEventHandler(this.__toggleEvent, eventHandler);
};

uiHtml_Toggle.prototype.__doubleAppendOnStateOff = function(eventHandler) {
  this.__triggerOn.appendEventHandler(this.__toggleEvent, eventHandler);
};

// strategies for toggle with one triggers
uiHtml_Toggle.prototype.__singleDisplayOn = function() {
  // nothing to do
};

uiHtml_Toggle.prototype.__singleDisplayOff = function() {
  // nothing to do
};

uiHtml_Toggle.prototype.__singlePrependOnStateOn = function(eventHandler) {
  var obj = this;
  this.__triggerOn.prependEventHandler(this.__toggleEvent, function(e) {
    // NOTE: as this is called before the state is changed, the
    //       state would still be off
    if (!obj.__stateOn) {
      eventHandler.call(this, e);
    }
  });
};

uiHtml_Toggle.prototype.__singlePrependOnStateOff = function(eventHandler) {
  var obj = this;
  this.__triggerOn.prependEventHandler(this.__toggleEvent, function(e) {
    // NOTE: as this is called before the state is changed, the
    //       state would still be on
    if (obj.__stateOn) {
      eventHandler.call(this, e);
    }
  });
};

uiHtml_Toggle.prototype.__singleAppendOnStateOn = function(eventHandler) {
  var obj = this;
  this.__triggerOn.appendEventHandler(this.__toggleEvent, function(e) {
    // remember that this method _appends_ the event handler
    // therefore, when this handler gets executed, the state is
    // supposed to be on
    if (obj.__stateOn) {
      eventHandler.call(this, e);
    }
  });
};

uiHtml_Toggle.prototype.__singleAppendOnStateOff = function(eventHandler) {
  var obj = this;
  // note that this also uses the 'on trigger', because there is only
  // one trigger (therefore we'll need to check the state of the toggle)
  this.__triggerOn.appendEventHandler(this.__toggleEvent, function(e) {
    // remember that this method _appends_ the event handler
    // therefore, when this handler gets executed, the state is
    // supposed to be off
    if (!obj.__stateOn) {
      eventHandler.call(this, e);
    }
  });
};


////////////////////////////////////
////////// Static Methods //////////
////////////////////////////////////

uiHtml_Toggle.createByEither = function(
    onId, onName, offId, offName, initialIsOn, eventType) {
  var triggerOn = uiHtml_Element.createByEither(onId, onName);
  var triggerOff = null;
  try {
    triggerOff = uiHtml_Element.createByEither(offId, offName);
  }
  catch(e) {
    // we can still proceed with triggerOff == null
    uiUtil_Logger.getInstance(uiHtml_Toggle).warn(
        onId + ": triggerOff does not exist.");
  }
  return new uiHtml_Toggle(triggerOn, triggerOff, initialIsOn, eventType);
};
