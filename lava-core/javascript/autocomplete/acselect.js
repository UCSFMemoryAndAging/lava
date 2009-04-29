/** acselect: an autocompete select solution
	author: joe hesse (jhesse@memory.ucsf.edu)
	version: 1.4 (April 21, 2009)
	note: started from existing code from actb.js (http://www.codeproject.com/jscript/jsactb.asp)
	functionality: operates similarly to combo box in Microsoft Access with autocomplete enabled. 
	
	1.1 Features:  added multiple column support. 
	1.2 Features:  added support for enabling/disabling, using a skip code for logical skips, and observing the "change" event.<b>
	1.2. Outstanding Bug..user can use the mouse to bypass the limit to list check.  Problem with event handling I think.  
	1.3 Added functionality to match against values entered with leading zeros. This is an option that can be disabled if desired. 
    example html:
    	    <input type="text" autocomplete="off" name="testac_ac" id="testac_ac"/>
     		<a onclick="obj.toggleList()"><img src="images/1downarrow.png" border="0"/></a>
  			<div id="testac_hidden_block" style="display:none">
    		<select name="testac" id="testac">
    			<option value=""></option>
	    		<option value="1">apple</option>
	    		<option value="3">mango</option>
    			<option value="4">pineapple</option>
    			<option value="5">orange</option>
    			<option value="11">1apple</option>
    			<option value="13">1mango</option>
    			<option value="14">1pineapple</option>
			</select>
		   </div>	
  
 			<script language="javascript" type="text/javascript">
				var obj = new acselect(document.getElementById("testac_ac"),document.getElementById("testac"));
			</script>
*/
var  ACS = new Array();


//acselect constuctor
function acselect(textboxIn,selectIn){
	/* ---- Public Variables ---- */
	
	
    this.defaultIndex = 0; //the index in the select list that should be selected if no match for the text entered (typically if you
    							//put a blank (null entry first in the list) this will suffice for handling values with no entry
	this.listRows = 10;    // Number of elements autocomplete can show (-1: no limit)
	this.columnCount = 1; //does not include the implicit "hidden" key column 
	this.listWidth = 'auto'; //auto or width specification (do not inlcude px)
	this.columnWidths = ['auto']; //'px' is assumed; 'auto' will assign the remaining space to the column
	this.columnHeaders = null; //Set as an array with column headings 
	this.columnDelimiter = '|';  //used to split the value text into columns
	this.mouse = true; // Enable Mouse Support
	this.startcheck = 1; // Show widget only after this number of characters is typed in.
	this.limitToList = true; //limit entry to items in the list, setting to 'false' will add items to the underlying select list.  option value and text will be set to the new value.
	this.skipCodeText = 'Logical Skip'; //The textual representation of the logical skip code.  Set to '' if no code used for logical skips. 
	this.skipCodeValue = -6; //The value of the logical skip code.
	this.onChangeCallback = acs_onChangeCallbackPlaceholder; //set this to a javascript function to have it called when the value of the select is changed. 
	this.onChangeCallbackOwner = null; // the object owner of the callback function
	this.appendOnChangeCallback = acs_appendOnChangeCallback;
	this.matchLeadingZeros = true; // when true this will match values regardless of leading zeros entered (e.g. '4','04', and '004' all match '4')
	
	/* ---- Public Variables ---- */
	
	
	
	/* --- Styles --- */
	
	this.scrollBgColor = '#CCCCCC';
	this.bgColor = '#FFFFFF';
	this.textColor = '#000000';
	this.borderColor = '#000000';
	this.highlightedBgColor = '#000000';
	this.highlightedTextColor = '#FFFFFF';
	this.fFamily = 'Verdana';
	this.fSize = '11px';
	this.hStyle = 'text-decoration:underline;font-weight="bold"';

	/* --- Styles --- */

	/* ---- Private Variables ---- */
	this.displayOn = false; //whether the list is displayed or not
	this.firstDisplayedIndex = 0; //first row in list display
	this.lastDisplayedIndex = 0; //last row in list display
	this.mouseOnList = 0;  //flag indicating the mouse in on the list 
	this.cancelKey = false; //flag to indicate that the keypress event should be cancelled. 
	this.select = selectIn;  //the select control
	this.textbox = textboxIn;//the textbox control
	this.uniqueid = this.textbox.id;
	this.HEADER_ROW_INDEX=-1;
	this.changed = false; //flag to track if the value is the select has changed.
	 
	/* ---- function mapping---- */

	
	//event handlers
	this.handleKeyDown = acs_checkkey;
	this.handleChange = acs_onExit;
	this.handleBlur= acs_onExit;
	this.handleKeyPress = acs_keypress;
	
	//list display functions
	this.toggleList = acs_toggleList;
	
	//control management functions
	this.disable = acs_disable;
	this.enable = acs_enable;
	this.skip = acs_skip;
	this.unskip = acs_unskip;
	this.setValue = acs_setValue; // sets value by matching to option text
	this.setValueByValue = acs_setValueByValue; // sets value by matching to option value
	this.getValue = acs_getValue;
	this.doOnChange = acs_doOnChange;
	this.doLimitToList = acs_doLimitToList;
	this.hideList = acs_hideList;
	this.doDisplay = acs_display;
	this.remake = acs_remake;
	
	//scrolling and moving functions
	this.selectPrevious = acs_selectPrevious;
	this.selectNext = acs_selectNext;
	this.scrollDownOne = acs_scrollDownOne;
	this.scrollUpOne = acs_scrollUpOne;
	this.scrollListToSelectedIndex = acs_scrollListToSelectedIndex;
	this.scrollListToIndex = acs_scrollListToIndex;
	
	//selection functions
	this.selectItemByTextEntry = acs_selectItemByTextEntry;
	this.selectItemByValueEntry = acs_selectItemByValueEntry;
	this.selectdefaultIndex = acs_selectdefaultIndex;
	this.selectItemByIndex = acs_selectItemByIndex;
	this.findMatch = acs_findMatch;
	this.getIndexTextValue = acs_getIndexTextValue;
	this.getIndexValue = acs_getIndexValue;
	//table creation and destruction
	this.destroyTable = acs_destroyTable;
	this.createList = acs_createList;
	this.createRow = acs_createRow;
	this.isScrollBoxRow = acs_isScrollBoxRow;
	this.highlightRow = acs_highlightRow;
	this.doHighlight = acs_doHighlight;
	this.doUnhighlight = acs_doUnhighlight;
	this.createScrollColumn = acs_createScrollColumn;
	this.createColumn = acs_createColumn;
	
	//hack to initialize the awt toolkit for the beep
	//decided to abandon this beep now as Firefox 2 gave "java is not defined" javascript error for some users
	//this.toolkit = java.awt.Toolkit.getDefaultToolkit();
	
	//make sure a select and textbox were passed in
	if (selectIn.type!="select-one" || textboxIn.type!="text"){ 
		alert("ACS.constructor: Invalid Textbox or Select Object passed to acs constructor"); 
		return;
		}
	
	ACS[this.textbox.id] = this;
	
	//set the initial value of the textbox
	if(this.select.selectedIndex == null){
		this.select.selectedIndex = this.defaultIndex;
		}
	this.textbox.value = this.getIndexTextValue(this.select.selectedIndex);
	return this;	
	}
	
	
	
	/*****************************************
		high level event handling routines
	********************************/

	function acs_doOnChange(force){
		if((force == true) || (this.changed == true)){
			this.onChangeCallback.call(this.onChangeCallbackOwner);
			this.changed = false;
			}
		}
	
	function acs_onChangeCallbackPlaceholder(){
		//this is a placeholder so the code doesn't error out when no callback supplied. 
		
		}
		
	function acs_appendOnChangeCallback(callback, owner) {
	    var existingCallback = this.onChangeCallback;
	    this.onChangeCallback = function() {
			existingCallback.call(owner);
			callback.call(owner);
		}
	}
	
	


		
			
	
	
	
	function acs_doLimitToList(){
		//decided to abandon beep for now as Firefox 2 gave "java is not defined" javascript error for some users
		//this.toolkit.beep(); 
		if (this.displayOn == false){
			this.doDisplay(true);
			}
		this.textbox.focus();
		setSelection(this.textbox,0,text.length);
		
	}
	
	function acs_onExit(evt){

		//get object from array by id
		acs = ACS[getElement(evt).id];
		
	//don't do exit if mouse is on list;
	if(acs.mouseOnList == 1){return};
		
	if(!acs.selectItemByTextEntry(true))
		{
			stopEvent(evt);
			return false;
		}else{
			
			acs.doOnChange();
			return true;
		}
	}
		
			
	
	
	/************************************************************
		List display and recreation function
	***********************************/
	
	//convenience function...called by page using syntax ACS.toggleList();
	function acs_toggleList(){
	
		if(this.textbox.disabled == true){
	
			return true;
			}
				
		if (this.displayOn == true){
			this.hideList();
		}else{
			this.doDisplay(true);
			this.textbox.focus();
		}
		return true;
	}
	
	//enable --used by unSkip method
	function acs_enable(){
		this.textbox.disabled = false;
		}
		
	//disable --used by skip method
	function acs_disable(){
		this.textbox.disabled = true;
		}
	
	
	function acs_skip(value, optionText){
		this.disable();
		if (!this.selectItemByValueEntry(value)) {
	                this.textbox.value = optionText;
			this.selectItemByTextEntry(false, value);
		}
	}	
	function acs_unskip(value, optionText){
		this.enable();
		if(this.textbox.value == this.skipCodeText || this.getIndexValue(this.select.selectedIndex) == this.skipCodeValue){
			if (!this.selectItemByValueEntry(value)) {
				this.textbox.value = optionText;
				this.selectItemByTextEntry(false, value);
			}
		}

	}			
	
	// set value by matching against option text
	// note: valueForNewOption is an optional parameter which is only used if the value does not exist
	// and limitToList is false such that a new option is created, and this parameter is used for the value
	// of the new list option
	function acs_setValue(optionText, valueForNewOption){
		// save value in case set fails
		var oldOptionText = this.textbox.value;
		this.textbox.value = optionText;
		// if optionText not found in list, and limitToList true, reset to old value
		if (!this.selectItemByTextEntry(false, valueForNewOption)) {
			this.textbox.value = oldOptionText;
		}
	}
	
	// set value by matching against option value
	// this function was added for usage by javascript skip logic, such as setting diagnosis fields
	// to their prior value, or setting neuroexam fields to their normal values. whereas setValue
        // is called when a user inputs via the keyboard and must contain functionality to add options
        // to the list and alert the user, this function need not do that. just return false if no match.
	function acs_setValueByValue(value){
		// if value not found in list, and limitToList true, reset to old value
		if (!this.selectItemByValueEntry(value)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	function acs_getValue(){
		return this.textbox.value;
	}
	
	//hide the list
	function acs_hideList(){
		this.destroyTable();
		this.displayOn = false;
		this.mouseOnList = 0;
	}
	
	//called to display list...assumes that it should scroll to the currently selected index item.
	function acs_display(force){
 		//if force passed in then always display, otherwise use "ACS.displayOn" to determine what to do
 		if (force == true){
 			this.displayOn = true;
 			}
 		//always destroy prior list
 		this.destroyTable();
 		
		//set list scroll "location" based on currently selected item.
		this.scrollListToSelectedIndex();
		
		if (!this.displayOn) {return;}
		
		table = this.createList();
		var index = 0;	
		for (index = this.firstDisplayedIndex;index <= this.lastDisplayedIndex;index++){		
			this.createRow(table,index);
			}
	}
	

	
	
	function acs_remake(){
		
		if(!this.displayOn){return;}
		
		table = this.createList();
		for (index=this.firstDisplayedIndex; index <= this.lastDisplayedIndex; index++){
				row = this.createRow(table, index);		
		}
	}
	
	/*********************************************************
	     List scrolling and moving functions
	*****************************************/
	function acs_selectPrevious(){
		if (!this.displayOn) return;
		if (this.select.selectedIndex == 0) return;
		this.selectItemByIndex(this.select.selectedIndex-1,false);
		this.highlightRow(this.select.selectedIndex);
		
		
		if (this.select.selectedIndex < this.firstDisplayedIndex){
			 this.scrollUpOne(); 
		}
		
	}
	
	function acs_selectNext(){
		if (!this.displayOn) return;
		if (this.select.selectedIndex == this.select.options.length -1) return;
	
		this.selectItemByIndex(this.select.selectedIndex+1,false);
		this.highlightRow(this.select.selectedIndex);
		
		
		if (this.select.selectedIndex > this.lastDisplayedIndex){
			 this.scrollDownOne(); //highlighting taken care of by movedown()
		}
		
	}
	
	function acs_scrollDownOne(){
		if (this.lastDisplayedIndex < this.select.options.length -1){
			this.firstDisplayedIndex++;
			this.lastDisplayedIndex++;
			this.remake();
			}
	}
	function acs_scrollUpOne(){
		if (this.firstDisplayedIndex > 0){
			this.firstDisplayedIndex--;
			this.lastDisplayedIndex--;
			this.remake();
			}
	}

	//convenience function
	function acs_scrollListToSelectedIndex(){
		this.scrollListToIndex(this.select.selectedIndex);
		}
	
	//sets index variables used to determine what items to display...ensures we don't scroll past the end of the list. 	
	function acs_scrollListToIndex(index){
		var rowsDisplayed = (this.listRows > this.select.options.length) ? this.select.options.length : this.listRows;
		
		this.firstDisplayedIndex = index;
		this.lastDisplayedIndex = this.firstDisplayedIndex + rowsDisplayed -1;
		if(this.lastDisplayedIndex > this.select.options.length -1){
			this.lastDisplayedIndex = this.select.options.length -1;
			this.firstDisplayedIndex = this.lastDisplayedIndex - rowsDisplayed -1;
		}
		
		if(this.firstDisplayedIndex < 0){
			this.firstDisplayedIndex = 0;
			this.lastDisplayedIndex = rowsDisplayed -1;
		}
	}
	/**************************************************
		 Mouse handler functions for scolling and click support
	 ************************/
	//handles any click on the scroll bar area.
	function acs_mouseScroll(evt){
		if (!evt) evt = event;		
	
		acs = ACS[getTargetElement(evt).getAttribute('acs_id')];
		
		var itemClickedIndex = this.getAttribute('pos');
		var scrollableLength = acs.select.options.length -1 - acs.listRows;
		var scrollBarLength = acs.listRows - 2;
		if (scrollBarLength <= 0) {return false};
		
		var scrollAreaClicked = itemClickedIndex - acs.firstDisplayedIndex;
		var scrollTo = Math.round((scrollAreaClicked*scrollableLength)/scrollBarLength);
		
		acs.scrollListToIndex(scrollTo);
		acs.remake();
		
		//always put the focus back on the textbox after a mouse click (to reenable the onExit functionality)
		acs.textbox.focus();
	}
	
	//handles click in the down button in the scroll bar
	function acs_mouseScrollDown(evt){
		if (!evt) evt = event;
		acs = ACS[getElement(evt).getAttribute('acs_id')];
		if (evt.stopPropagation){
			evt.stopPropagation();
		}else{
			evt.cancelBubble = true;
		}
		acs.scrollDownOne();
		//always put the focus back on the textbox after a mouse click (to reenable the onExit functionality)
		acs.textbox.focus();	
		}
	//handles click on the up button in the scroll bar
	function acs_mouseScrollUp(evt){
		if (!evt) evt = event;
		
		acs = ACS[getElement(evt).getAttribute('acs_id')];

		if (evt.stopPropagation){
			evt.stopPropagation();
		}else{
			evt.cancelBubble = true;
		}

		acs.scrollUpOne();
		//always put the focus back on the textbox after a mouse click (to reenable the onExit functionality)
		acs.textbox.focus();
	}
	//handles mouse click on the list to select an item/
	function acs_mouseSelectItem(evt){
		if (!evt) evt = event;
		acs = ACS[getElement(evt).getAttribute('acs_id')];
		if (!acs.displayOn) return;
		acs.selectItemByIndex(this.getAttribute('pos'));
		acs.doOnChange(true);
		acs.hideList();
		
    	//always put the focus back on the textbox after a mouse click (to reenable the onExit functionality)
		acs.textbox.focus();
	}
	function acs_mouseOnList(evt){
		if (!evt) evt = event;
		acs = ACS[getElement(evt).getAttribute('acs_id')];
		acs.mouseOnList = 1;
	}
	function acs_mouseOffList(evt){
		
		if (!evt) evt = event;
		acs = ACS[getElement(evt).getAttribute('acs_id')];
		acs.mouseOnList = 0;
	}
	function acs_mouseHighlightItem(evt){
		if (!evt) evt = event;
		acs = ACS[getElement(evt).getAttribute('acs_id')];
		acs.highlightRow(this.getAttribute('pos'));
			
	}
	
	
	/**************************************************
		Functions to select the item based either on the 
		text in the textbox or the index of the item selected in the list
    *************************************/
	function acs_selectItemByTextEntry(onExit, valueForNewOption){
		
		//if textbox matches underlying select list then just return
		if(this.getIndexTextValue(this.select.selectedIndex) == this.textbox.value){return true;}
		
		//check if the text in the textbox matches the an item in the select list
		for(i=0; i < this.select.options.length; i++){
			if (this.textbox.value == this.getIndexTextValue(i)){
				this.select.selectedIndex=i;
				this.changed = true;
				return true;
				}
			}
		
			
		//if we are here then the textbox text does not match a value in the list
		if(this.limitToList){
			if (onExit){
				this.doLimitToList();
			}
			return false;
			
		}else{	
			//if we are here then we need to add the item to underlying list and select
			var option = document.createElement("option");
			if (valueForNewOption != null) {	
				option.value = valueForNewOption;
			}
			else {
				option.value = this.textbox.value;
			}
			option.text = this.textbox.value;
			this.select.options.add(option);
			this.select.selectedIndex=this.select.options.length -1;
			this.changed = true;
			return true;
		}
	
	}

	// this function was added for usage by javascript skip logic, such as setting diagnosis fields
	// to their prior value, or setting neuroexam fields to their normal values
	function acs_selectItemByValueEntry(value){
		//if textbox matches underlying select list then just return
		if(this.getIndexValue(this.select.selectedIndex) == value){return true;}
		
		//check if the value passed in matches the value of an item in the select list
		for(i=0; i < this.select.options.length; i++){
			if (value == this.getIndexValue(i)){
				this.select.selectedIndex=i;
				this.textbox.value = this.getIndexTextValue(i);
				this.changed = true;
				return true;
				}
			}

		// this function was added to select item by value, programatically, i.e. not when the user
		// selects a value (that is what selectItemByTextEntry is for). therefore, it does not support
		// the limitToList flag, and does not support an onExit parameter to issue an alert to user if
		// the value is not in the list. if value is not in the select list, just return false
		return false;
	}
	
	function acs_selectdefaultIndex(updateText){
		if(updateText){
			this.selectItemByIndex(this.defaultIndex);
		}else{
			this.select.selectedIndex = this.defaultIndex;
		}
	}
			
	function acs_selectItemByIndex(index,setCaretInTextBox){
		
		if(this.select.selectedIndex != index){
			this.changed = true;
			}
			
		this.select.selectedIndex=index;
		text = this.getIndexTextValue(this.select.selectedIndex);
		
		//get caret position
		if(setCaretInTextBox){
			caretPos = getCaretStart(this.textbox);
		}else{
			caretPos = 0;
		}
		this.textbox.value = text;
		setSelection(this.textbox,caretPos,text.length);
	
	}	
	
	function acs_getIndexTextValue(index){
		if(this.select.options.length <= index) {return "";}
		text = this.select.options[index].text;
		indexTextValues = text.split(this.columnDelimiter);
		return indexTextValues[0];
	}

	function acs_getIndexValue(index){
		if(this.select.options.length <= index) {return "";}
		value = this.select.options[index].value;
		return value;
	}


    /**************************************************
    	 keyboard entry handling functions 
     *************************/
    
	function acs_checkkey(evt){
		if (!evt) evt = event;
		a = evt.keyCode;
	
		
		acs = ACS[getTargetElement(evt).id];		
		acs.cancelKey = 0;
		switch (a){
		//esc key
			case 27:
				if (acs.displayOn){
					acs.hideList();
					acs.cancelKey = 1;
					return false;
					}else{
					return true;
					}
			break;
		//up arrow
			case 38:
				acs.selectPrevious();
				acs.cancelKey = 1;
				return false;
			break;
		//down arrow	
			case 40:
				//display list if not currently displayed
				if(!acs.displayOn){
					acs.doDisplay(true);
					acs.cancelKey = 1;
				}else{
				acs.selectNext();
				acs.cancelKey = 1;
				}
				return false;
			break;
		//backspace or delete
			case 8: case 46:  
				return true;
			break;
		//return or tab
			case 13: case 9:
					//try to do an "onExit" entry match
					if(!acs.selectItemByTextEntry(true)){
						//cancel key if no match found (and the list is set to LimitToList)
						stopEvent(evt);
						acs.cancelKey = 1;
						return false;
					}else{
						//allow the exit from the textbox				
						acs.doOnChange();
						acs.hideList();
						return true;
					}
			
			break;
		//any other key press
			default:
				//find the first matched item in the list based on the text entered
				setTimeout(function(){acs.findMatch(a);},50);
				break;
		}
	}
	
	
	//This handler cancels key presses that we don't want to pass through to the underlying textbox
	//based on our inspection in the acs_checkKey handler.
	function acs_keypress(e){
		if (!e) e = event;
		
		acs = ACS[getTargetElement(e).id];	
	
		if (acs.cancelKey != 0) {stopEvent(e)};
		return acs.cancelKey;
	}
	

	function acs_findMatch(kc){
		if (kc == 38 || kc == 40 || kc == 13) return;
		
			
		//if no match can be made (or should be made, set to unmatched index)
		if (this.textbox.value.length < this.startcheck || this.textbox.value.length == 0){
			this.selectdefaultIndex(true);
			return;
		}
		
		caretLoc = getCaretStart(this.textbox);
		var text = this.textbox.value;
		var textNoZeros = text.replace(/^0*/,'');
		//if textNoZeros has no length then set to text otherwise we end up matching on blank entry
		if(textNoZeros.length == 0){
			textNoZeros = text;
		}
		text = text.addslashes();
		textNoZeros = textNoZeros.addslashes();
		
		var re = RegExp("^" + text, "i");
		var reNoZeros = RegExp("^" + textNoZeros, "i");
	
		//start looking from 100 rows back from the currently selected index and loop around to the beggining if needed
		//..this is fastest in most situations where a really long list is sorted aphabetically
		var startingIndex = (this.select.selectedIndex < 50)? 0 : this.select.selectedIndex - 50;
		
		var items = this.select.options.length;
		for (var counter=startingIndex;counter<items+startingIndex;counter++){
			var indexToTest = (counter < items) ? counter : counter - startingIndex;
			//first test exact match
			if (re.test(this.select.options[indexToTest].text)){
				this.selectItemByIndex(indexToTest,caretLoc);
				this.changed = true;
				this.doDisplay(false);
				return;
			}else if(this.matchLeadingZeros == true){
				if (reNoZeros.test(this.select.options[indexToTest].text)){
					this.selectItemByIndex(indexToTest,caretLoc);
					this.changed = true;
					this.doDisplay(false);
					return;
				}
			}
		}
		
		//no match, so set underlying select to default index without affecting textbox
		this.selectdefaultIndex(false);
		this.doDisplay(false);
		return;
		
	}
	
	/**************************************************
	 functions for creating, destroying, and formatting the table and rows for the list
	 *****************************************/
	//destroy the list if it exists
	function acs_destroyTable(){
		if (document.getElementById('tat_table_'+this.uniqueid)){
			document.body.removeChild(document.getElementById('tat_table_'+this.uniqueid)); 
			} 
		}



	//Create the table 
	function acs_createList(){
		this.destroyTable();
		a = document.createElement('table');
		a.setAttribute('acs_id',this.uniqueid);
		
		a.cellSpacing='0px';
		a.cellPadding='0px';
		a.style.position='absolute';
	
		a.style.top = eval(curTop(this.textbox) + this.textbox.offsetHeight) + "px";
		a.style.border = 'solid 1px' 
		a.style.borderColor = this.borderColor;
		a.style.left = curLeft(this.textbox) + "px";
		a.style.backgroundColor=this.bgColor;
		a.id = 'tat_table_'+this.uniqueid;
		
		if(this.listWidth=='auto') {
				this.listWidth = this.textbox.offsetWidth
		}
		a.style.width = this.listWidth + 'px'
		
		document.body.appendChild(a);
		
		//if mouse support enabled, setup event handlers
		if (this.mouse){
			a.onmouseout = acs_mouseOffList;
			a.onmouseover = acs_mouseOnList;
			}
		//if column Headers then draw them
		if(this.columnHeaders != null){
			this.createRow(a,this.HEADER_ROW_INDEX);
			}
	return a;
	}
	
	
	
	function acs_createRow(table,index){
		r = table.insertRow(-1);
		r.id = 'tat_tr_'+this.uniqueid+(index);
		r.setAttribute('acs_id',this.uniqueid);
		
		if(index == this.HEADER_ROW_INDEX){
			columnText = this.columnHeaders;
		}else{
			columnText = this.select.options[index].text.split(this.columnDelimiter);
		}
		allocatedWidth = 0;
		columnWidth = 0;
		//for each column
		for (var i=0; i < this.columnCount;i++){
			//determine width of column
			if(i >= this.columnWidths.length){
				columnWidth = 0;  //not specified, set to 0
			}else{
				columnWidth = this.columnWidths[i];
			}
			if(columnWidth == 'auto'){
				columnWidth = this.listWidth - allocatedWidth;  //if 'auto' set to remainder of width
				}
			if((allocatedWidth + columnWidth) > this.ListWidth){
				columnWidth = this.listWidth - allocatedWidth;  //if specified width would be greater than listwidth then set to remaining width
				}
			allocatedWidth = allocatedWidth + columnWidth;
			if(columnWidth != 0){ //skip 0 width columns
				this.createColumn(r,i,index,columnText[i],columnWidth);
			}
				
		}
		
		//add scroll bar row only if there are too many items than can be displayed at once
		if(this.select.options.length > this.listRows){
			this.createScrollColumn(r,index)
			}
	}
	
	function acs_createScrollColumn(row, index){
		scrollCell = row.insertCell(-1);
		scrollCell.id = 'tat_scroll'+this.uniqueid+(index);
		scrollCell.setAttribute('pos',index); 
		scrollCell.setAttribute('acs_id',this.uniqueid);
		scrollCell.style.width='15px';
		scrollCell.style.borderLeft = 'solid 1px';
		
		if(index == this.HEADER_ROW_INDEX){
			scrollCell.innerHTML ='';
			
		//up arrow in first row 
		}else if (index == this.firstDisplayedIndex){
			scrollCell.innerHTML = '<img src="images/1uparrow.png" border="0" acs_id="' + this.uniqueid +'">';
			if(this.columnHeaders != null){
				scrollCell.style.borderTop = 'solid 1px';
				}
			scrollCell.style.borderBottom = 'solid 1px';
			//enable mouse support
			if (this.mouse){
				scrollCell.style.cursor = 'pointer';
				scrollCell.onclick=acs_mouseScrollUp;
				}
		//down arrow in last row
		}else if(index == this.lastDisplayedIndex) {
			scrollCell.innerHTML = '<img src="images/1downarrow.png" border="0" acs_id="' + this.uniqueid +'">';
			//enable mouse support
			if (this.mouse){
				scrollCell.style.cursor = 'pointer';
				scrollCell.onclick=acs_mouseScrollDown;
				scrollCell.style.borderTop = 'solid 1px';
				scrollCell.style.borderBottom = 'solid 1px' 

				}
		//put 'scroll indicator' in appropriate row
		}else if (this.isScrollBoxRow(index)){
			scrollCell.innerHTML = '<img src="images/middle.png" border="0" acs_id="' + this.uniqueid +'">';
		}else{
			scrollCell.innerHTML = '';
			//enable mouse support
			if (this.mouse){
				scrollCell.style.cursor = 'pointer';
				scrollCell.onclick=acs_mouseScroll;
				}
		}
	
		scrollCell.style.backgroundColor = this.scrollBgColor;
	
	}
						
		
	function acs_createColumn(row,columnIndex,index,text,width){
		c = row.insertCell(-1);
		c.id = 'tat_td_'+columnIndex+this.uniqueid+(index);
		c.setAttribute('pos',index);
		c.setAttribute('acs_id',this.uniqueid);
		
		if ((this.mouse) && (index != this.HEADER_ROW_INDEX)){
			c.style.cursor = 'pointer';
			c.onclick=acs_mouseSelectItem;
			c.onmousemove = acs_mouseHighlightItem;
			}
		c.style.fontFamily = this.fFamily;
		c.style.fontSize = this.fSize;
		c.style.textAlign = "left";
	
		if(index == this.HEADER_ROW_INDEX){
			c.style.borderBottom = 'solid 1px';
			}
		if(index > this.firstDisplayedIndex){
			c.style.borderTop = 'solid 1px';
			}
		if(columnIndex > 0) {
			c.style.borderLeft = 'solid 1px';
		
			}	 
		
		c.style.width = width+'px';
		if (text == null){
			text = '';
			}
		c.innerHTML = (text == '')? '&nbsp;':text;
				
		//set colors 
		if(index == this.HEADER_ROW_INDEX){
			c.style.backgroundColor = this.scrollBgColor;
		}else if (index == this.select.selectedIndex){  
			this.doHighlight(index);
		}else{
			this.doUnhighlight(index);
			}
	}
		
	
		
	
	function acs_isScrollBoxRow(index){
		var scrollableLength = this.select.options.length -1 - this.listRows;
		var scrollBarLength = this.listRows - 2;
		if (scrollBarLength <= 0) {return false};
		var rowUnderConsideration = index - this.firstDisplayedIndex;
		//if scrolled to end of list, the box is in next to last row
		if(this.lastDisplayedIndex == this.select.options.length -1){
			return(rowUnderConsideration ==  this.listRows -1);  
			}
		//if at beginning of list, the box is in next to first row
		if(this.firstDisplayedIndex == 0){
			return(rowUnderConsideration == 1);
			}
		//proper box for the scroll bar is the number of scroll bar units * the ratio of the first displayed item index / total scrollable items.
		return(rowUnderConsideration == Math.round(scrollBarLength*(this.firstDisplayedIndex/scrollableLength)));
		}
			
	function acs_highlightRow(index){
		//do nothing if the index supplied is not displayed
		if(index >= this.firstDisplayedIndex && index <= this.lastDisplayedIndex){
			this.doHighlight(index);
			
			//loop through rows unhighlighting any others that were highlighted
			for (i = this.firstDisplayedIndex; i <= this.lastDisplayedIndex;i++){
				if(i != index){
					this.doUnhighlight(i);
					}
				}
			}
		}			
		
		
		
		function acs_doHighlight(index){
				if(index >= this.firstDisplayedIndex && index <= this.lastDisplayedIndex){
					document.getElementById('tat_tr_'+this.uniqueid+index).style.backgroundColor = this.highlightedBgColor;
					document.getElementById('tat_tr_'+this.uniqueid+index).style.color = this.highlightedTextColor;
				}
			}
		
		
		function acs_doUnhighlight(index){
				if(index >= this.firstDisplayedIndex && index <= this.lastDisplayedIndex){
					document.getElementById('tat_tr_'+this.uniqueid+index).style.backgroundColor = this.bgColor;
					document.getElementById('tat_tr_'+this.uniqueid+index).style.color = this.textColor;		
				}
			}
	
	
	
	

