/* common.js

   Common javascript functions shared across all pages. This file is included by
   jsp/includes/javascript.jsp and that file is included by each page level 
   decorator (i.e. "main", "modal") so it is available to every page. 

*/

// the submitted flag prevents prompting user to confirm exit when leaving page via an 
// action button. this only applies to modal pages, which set the window.onbeforeunload 
// event handler which checks this flag (this event handler is set in editable.js which
// is only included by the modal decorator)
var submitted = false;

/* submitForm
  
   This function is called by all form submissions that handle actions to submit
   the form. 
   
   The eventId representing the current action (aka event) is passed as an argument
   and is assigned to the _eventId property for processing by the controller.
   
   A callback function can be passed as an argument if a given form needs specific
   functionality to be executed before the form is submitted.
   
   The target argument, if supplied, is used as the value of the HTML target attribute
   on the <form> tag to open the response in a new browser window.
   NOTE: 9/25/07: this is not supported in the current version of Spring Web Flow so it
         is not used yet.

   note: if no callback function, it is valid to call this with two arguments and
        javascript will assign null to the submitCallback argument
*/
function submitForm(form, eventId, target, submitCallback) {
    if (submitCallback != null) {
		submitCallback(form);
	}

	submitted = true; // see comments for submitted variable	

	// enable all form controls	
	// pages which have skip logic or other logic which disables certain
	// controls need to enable those controls on submitting the form, because a 
	// disabled control does not submit its value. 
	// note: this depends on functions in uitags.js
	for (var i = 0; i < form.length; i++) {
		if(uiHtml_Element.isArray(form.elements[i]) && uiHtml_Element.getWidgetType(form.elements[i]) != 'select') {
		    for(var j = 0; j < form.elements[i].length; j++) {
				form.elements[i][j].disabled = false;
			}
		}
		else {
			form.elements[i].disabled = false;
		}
	}

	form._eventId.value = eventId;
	if (target != null && target != '') {
		form.target = target;
	}
	form.submit();
}


function setFocus(focusFieldId) {
    // first check if it is an autocomplete field, because otherwise it will find the hidden select box
    // associated with autocomplete fields and set focus to that
    var obj = document.getElementById('acs_textbox_' + focusFieldId);
    if (obj == null) { 
        // check if field is standard text, textarea or select
        obj = document.getElementById(focusFieldId);
        if (obj == null) { 
         	// field does not have an id so must be a group of radiobuttons or checkboxes
            // where the individual option ids are composed of the fieldId concatenated
            // with '0', '1', '2', etc.  so, just find the first of these indivdual
            // options
        	obj = document.getElementById(focusFieldId + '0');
        }
    }
    
    if (obj != null) {
	    obj.focus();
	}
}


function onlyNumsAllowed(e) {
	var iKeyCode;
	if(window.event) // IE
	{
		iKeyCode = event.keyCode;
	}
	else if(e.which) // Firefox
	{
		iKeyCode = e.which;
	}
	/* accept numeric '0' thru '9'(ASCII keycodes 48 thru 57) or backspace(8), or '.' for decimals (46) or negative (45) */
	if( iKeyCode > 47 && iKeyCode < 58 || iKeyCode == 8 || iKeyCode == 46 || iKeyCode == 45 || iKeyCode == undefined ) return;
	else
		if (window.event)
			window.event.returnValue = null;
		else e.preventDefault();
}

// TODO: this function needs to be refactored later into scope-specific javascript
function UDS_onlyNaccCharactersAllowed(e) {
	var iKeyCode;
	if(window.event) // IE
	{
		iKeyCode = event.keyCode;
	}
	else if(e.which) // Firefox
	{
		iKeyCode = e.which;
	}
	/* accept backspace(8) and all printable characters(ASCII keycodes 32 thru 126)
	 *   except single quotes (39), double quotes (34), ampersands (38), and percentage signs (37)
	 *   Use overlapping conditions for readability (since will be a quick evaluation anyway).
	 */
	if( (iKeyCode > 31 && iKeyCode < 127 || iKeyCode == 8|| iKeyCode == undefined)
		&&
		!(iKeyCode == 34 || iKeyCode == 37 || iKeyCode == 38 || iKeyCode == 39) ) return;
	else
		if (window.event)
			window.event.returnValue = null;
		else e.preventDefault();
}

function textareaMaxLength(textareaElement, maxLength) {
	var length = textareaElement.value.length;
	var remaining = length - maxLength;
	//alert("curr length=" + length + " maxlen=" + maxLength + " remaining=" + remaining);
	var msg = "Sorry, you have input "+length+" characters into the "+
       "text box you just completed. It can return no more than "+
       maxLength+" characters to be processed. Please abbreviate "+
       "your text by at least "+remaining+" characters";
     if (length > maxLength) {
		alert(msg);
		// use of setTimeout is required because tabbing out of the textarea will set focus to
		// the next control, so have to wait for that to happen before resetting focus to the
		// textarea to control
		myVar = setTimeout("setTextareaFocus('" + textareaElement.id + "')", 100);
     }
}

function setTextareaFocus(textareaElementId) {
	document.getElementById(textareaElementId).focus();
}


function validateDateEntry(element){
	if (element.value!=null && element.value !=''){ //don't try to parse nulls
		var d = parseDate(element.value);
		if(d == null){
			alert('Date doesn\'t match any recognized date formats (MM/DD/YYYY).\nPlease correct.');
			element.focus(); //this isn't working....
			return false;
		}else{
			element.value=formatDate(d,'MM/dd/yyyy');
			return true;
		}
	}
}
	
function validateDateTimeEntry(element){
	if (element.value!=null && element.value !=''){ //don't try to parse nulls
		var d = parseDate(element.value);
		if(d == null){
			alert('This date field requires a time  ( Accepted format: MM/DD/YYYY HH:MM[AM/PM] ). Please correct.\nEnter 12:00AM when no time is appropriate for this field.');
			element.focus(); //this isn't working....
			return false;
		}else{
			element.value=formatDate(d,'MM/dd/yyyy h:mm a');
			return true;
		}
	}
}
	

