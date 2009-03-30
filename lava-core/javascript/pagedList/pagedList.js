// callback function to submitForm function, which changes the page property which
// is associated with the top record navigation control, when the user changes the
// bottom navigation control
function bottomRecordNavSetPage(form){
	form.page.value = form.bottomRecordNav.value;
}

function bottomRecordNavSetPageSize(form){
	form.pageSize.value = form.bottomPageSize.value;
}

  




