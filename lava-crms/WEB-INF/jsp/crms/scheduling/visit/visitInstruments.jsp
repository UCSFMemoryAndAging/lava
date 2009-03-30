<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>


<c:set var="component" value="visitInstruments"/>


<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix},${currentVisit.visitDescrip}</page:param>

	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="listFilters">
</content>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add" actionId="lava.crms.assessment.instrument.instrument" eventId="instrument__add" component="${component}"/>	    
</content>


<%-- TODO: put this and javascript in decorator and then just use a flag to turn it on.
maybe put javascript in a selectListItems.js include file --%>

<%-- REPLACING
<span class="bold" id="numSelectedParent">&nbsp;</span> Selected&nbsp;&nbsp;
--%>
<%-- use return false so href is ignored --%>
<%-- REPLACING
<a href="javascript:void" onClick="selectAll('${component}', 0, ${command.components[component].numOnCurrentPage});return false">Select All</a>&nbsp;&nbsp;|&nbsp;
<a href="javascript:void" onClick="selectNone('${component}', 0, ${command.components[component].numOnCurrentPage});return false">Select None</a>
--%>

<script language="javascript" type="text/javascript">
<!-- 
var firstElement = ${command.components[component].firstElementOnPage};
var lastElement = ${command.components[component].lastElementOnPage};
var numSelected = ${command.components[component].numSelected};

updateNumSelectedDisplay();

// this code assumes the presence of a HTML element with the id "numSelectedParent"
// which is a <span> tag within which the count of the current number of selected
// items in the list should be displayed (in the entire list, not just the number selected
// on the current page)

function updateNumSelectedDisplay() {
	numSelectedText = document.createTextNode(numSelected);
	numSelectedParent = document.getElementById("numSelectedParent");
	numSelectedParent.replaceChild(numSelectedText, numSelectedParent.firstChild);
}

// called by onclick event handler (set in metadata for 'selected' checkbox toggle)
function selectItemClicked(checkbox) {
	if (checkbox.checked) {
		numSelected += 1;
	}
	else {
		numSelected -= 1;
	}
	updateNumSelectedDisplay();
}

//TODO: can this be a uitags ? would have to check/uncheck all displayed checkboxes
// so would need to know the fieldId's of those ? 
// even if it could check the individual select checkboxes, it would need to execute
// javascript to update the numSelected (both update the variable, and call the function
// to update)
// therefore, will probably put all of this in a js include file instead
function selectAllChecked(pageName) {
	if (eval('document.' + pageName + '.selectAllCheckbox.checked')) {
		selectAll('${component}', 0, ${command.components[component].numOnCurrentPage});
	}		
	else {
		selectNone('${component}', 0, ${command.components[component].numOnCurrentPage})
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
//alert("numSelected=" + numSelected);
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

//-->
</script>

<%-- replace below lines with updateNumSelectedDisplay --%>
<script language="javascript" type="text/javascript">
<!--
numSelectedText = document.createTextNode(numSelected);
numSelectedParent = document.getElementById("numSelectedParent");
numSelectedParent.replaceChild(numSelectedText, numSelectedParent.firstChild);
//-->
</script>

<content tag="groupActions">

<%-- instrument group prototype actions --%>
<c:if test="${not empty groupPrototype}"> 
<tags:listRow>

	<tags:listCell width="2%">
		&nbsp;
	</tags:listCell>

	<tags:listCell styleClass="actionButton" width="10%">
		<%-- append actions with "_group" so event handling will create the group from a project-visit
			instrument group prototype instead of user selected items. this is not necessary for deleteAll
			which only applies to groups created from prototypes --%>
		<tags:eventActionButton buttonImage="view" component="instrumentGroup" action="view_group" pageName="${component}"/>
		<tags:eventActionButton buttonImage="edit" component="instrumentGroup" action="enter_group" pageName="${component}"/>
		<tags:eventActionButton buttonImage="status" component="instrumentGroup" action="status_group" pageName="${component}"/>
		<tags:eventActionButton buttonImage="delete" component="instrumentGroup" action="deleteAll" pageName="${component}"/>
	</tags:listCell> 

	<tags:listCell colspan="5">
		<tags:outputText text="${groupPrototype}"/>
	</tags:listCell>

</tags:listRow>
</c:if>

<!-- selected item actions -->
<tags:listRow>
	<tags:listCell width="2%">
<%-- TODO: create a tag for this for reusability. better yet, a tag for this whole block for instrument selection
  which calls the tag for just the checkbox for general selection --%>
		<div class="field">
			<input type="checkbox" name="selectAllCheckbox" class="inputData" value="1" onclick="selectAllChecked('${component}')">
		</div>
	</tags:listCell>

	<tags:listCell styleClass="actionButton" width="10%">
		<tags:eventActionButton buttonImage="view" component="instrumentGroup" action="view" pageName="${component}"/>
		<tags:eventActionButton buttonImage="edit" component="instrumentGroup" action="enter" pageName="${component}"/>
		<tags:eventActionButton buttonImage="status" component="instrumentGroup" action="status" pageName="${component}"/>
		<tags:eventActionButton buttonImage="delete" component="instrumentGroup" action="delete" pageName="${component}"/>
	</tags:listCell> 
	<tags:listCell colspan="5">
		<span class="bold" id="numSelectedParent">&nbsp;</span> Instruments Selected&nbsp;&nbsp;
	</tags:listCell>
	
</tags:listRow>
</content>

<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}"  label="" width="2%"/>
	<tags:componentListColumnHeader component="${component}"  label="Action" width="10%"/>
	<tags:componentListColumnHeader component="${component}"  label="Measure"width="11%"/>
	<tags:componentListColumnHeader component="${component}"  label="Collection" width="20%"/>
	<tags:componentListColumnHeader component="${component}"  label="Data Entry" width="19%"/>
	<tags:componentListColumnHeader component="${component}"  label="Validation" width="19%"/>
	<tags:componentListColumnHeader component="${component}"  label="Summary" width="19%"/>
</tags:listRow>
</content>


<tags:list component="${component}">
	
	<tags:listRow>
		<tags:listCell styleClass="actionButton"> 
			<tags:listItemSelect component="${component}" listIndex="${iterator.index}" entityType="instrument" mode="le"/>
		</tags:listCell>
		<tags:listCell styleClass="actionButton">
		   <c:choose>
		        <c:when test="${fn:startsWith(item.instrTypeEncoded, 'macdiagnosis')}">
					<tags:listInstrumentActionURLStandardButtons actionId="lava.crms.assessment.diagnosis.${item.instrTypeEncoded}" idParam="${item.id}" instrTypeEncoded="${item.instrTypeEncoded}"/>
				</c:when>
			    <c:when test="${not empty instrumentConfig[item.instrTypeEncoded]}"> <%-- implemented instrument --%>
					<tags:listInstrumentActionURLStandardButtons actionId="lava.crms.assessment.instrument.${item.instrTypeEncoded}" idParam="${item.id}" instrTypeEncoded="${item.instrTypeEncoded}" hasOwnFlows="${instrumentConfig[item.instrTypeEncoded].hasOwnFlows ? true : false}"/>
				</c:when>
				<c:otherwise>
					<tags:listActionURLButton buttonImage="delete" actionId="lava.crms.assessment.instrument.instrument" eventId="instrument__delete" idParam="${item.id}"/>	    
				</c:otherwise>
			</c:choose>					  
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="instrType" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>
			
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="collectionStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="entryStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>			
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="verifyStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>			
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="summary" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>			
		</tags:listCell>
</tags:listRow>
</tags:list>

</page:applyDecorator> 
</page:applyDecorator>   
	    

