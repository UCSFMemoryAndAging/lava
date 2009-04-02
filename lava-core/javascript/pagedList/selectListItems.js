// this code assumes the presence of a HTML element with the id "numSelectedParent"
// which is a <span> tag within which the count of the current number of selected
// items in the list should be displayed (in the entire list, not just the number selected
// on the current page)
// this is currently created by the tag which creates the selected item actions
// row at the top of the list (listSelectedItemsGroupRow.tag)

function updateNumSelectedDisplay() {
	numSelectedText = document.createTextNode(numSelected);
	numSelectedParent = document.getElementById("numSelectedParent");
	if (numSelectedParent != null) {
		numSelectedParent.replaceChild(numSelectedText, numSelectedParent.firstChild);
	}
}

// called by onclick event handler (set in metadata for 'selected' checkbox toggle
function selectItemClicked(checkbox) {
	if (checkbox.checked) {
		numSelected += 1;
	}
	else {
		numSelected -= 1;
	}
	updateNumSelectedDisplay();
}			

function selectAllCheckboxChecked(pageName, component, numOnCurrentPage) {
//alert("numOnCurrentPage=" + numOnCurrentPage + " comp=" + component + " pageName=" + pageName);	
	if (eval('document.' + pageName + '.selectAllCheckbox.checked')) {
		selectAll(component, 0, numOnCurrentPage);
	}		
	else {
		selectNone(component, 0, numOnCurrentPage)
	}		
}			

function selectAll(component, firstElement, lastElement) {
//alert("component=" + component + " first=" + firstElement + " last=" + lastElement);
	for (var i = firstElement; i <= lastElement; i++) {
		var checkbox = document.getElementById(component + '_pageList_' + i + '_selected');
		if (checkbox != null) {
			if (!checkbox.checked) {
				checkbox.checked = true;
				numSelected = numSelected + 1;
			}
		}
	}
	updateNumSelectedDisplay();
}

function selectNone(component, firstElement, lastElement) {
	for (var i = firstElement; i <= lastElement; i++) {
		var checkbox = document.getElementById(component + '_pageList_' + i + '_selected');
		if (checkbox != null) {
			if (checkbox.checked) {
				checkbox.checked = false;
				numSelected = numSelected - 1;
			}
		}
	}
	updateNumSelectedDisplay();
}

