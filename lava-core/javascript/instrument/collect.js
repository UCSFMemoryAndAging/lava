// these functions support the comboRadioSelect widget that is specific to data
// collection mode

// when the onchange event is received by the select box, "deselect" the radio
// button by selecting the last radio button in the group which is hidden and which
// indicates the value of the control should come from the select box (and is a
// missing data code)
// note: using the field id rather than name because field name's may have punctuation
//       that javascript can not handle
function deselectRadio(fieldId) {
  var lastIndex = 0;
  for (var i=0; i < 10; i++) {
    radiobuttonOption = document.getElementById(fieldId + i);
    if (radiobuttonOption != null) {
      radiobuttonOption.checked = false;
    }
    else {
      lastIndex = i - 1;
      break;
    }
  }
  // go back and check the last radio button, which indicates value should come from
  //  missing data code select box
  radiobuttonOption = document.getElementById(fieldId + lastIndex);
  radiobuttonOption.checked = true;
}

// when the onclick event is received by one of the radiobuttons in the group,
// "deselect" the select box by setting it to the blank entry, which is the first
// entry in every list
// note: using the field id rather than name because field name's may have punctuation
//       that javascript can not handle
function deselectSingleSelectBox(fieldId) {
  selectBox = document.getElementById(fieldId);
  selectBox.selectedIndex = 0;
}


