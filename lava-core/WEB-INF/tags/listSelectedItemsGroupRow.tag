<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ tag body-content="empty" %>

<%-- listComponent is the current list component and groupComponent is the group handler 
component, where the group handler is a secondary handler to the primary list handler and
is used to create the group. 

The select checkboxes are properties of the listComponent.

When an eventActionButton is clicked the event prefix should be the groupComponent
so that the secondary group handler can create the group --%> 

<%@ attribute name="listComponent" required="true" 
       description="The name of the list component" %>
<%@ attribute name="groupComponent" required="true" 
       description="The name of the list component" %>
<%@ attribute name="pageName" required="true" 
       description="The name of the page, i.e. HTML form to post" %>
<%@ attribute name="selectedCountMsgKey" required="true" 
       description="The message key to display the number of items currently selected" %>
<%@ attribute name="locked" 
       description="[optional] whether modifying actions would be disabled" %>

<spring:message var="selectedCountMsg" code="${selectedCountMsgKey}" text=""/>

<!-- selected item actions -->
<tags:listRow>
	<tags:listCell width="2%">
		<div class="field">
			<input type="checkbox" name="selectAllCheckbox" class="inputData" value="1" onclick="selectAllCheckboxChecked('${pageName}','${listComponent}', ${command.components[listComponent].numOnCurrentPage})">
		</div>
	</tags:listCell>

	<tags:listCell styleClass="actionButton" width="12%">
		<tags:eventActionButton buttonImage="view" component="${groupComponent}" action="view" pageName="${pageName}"/>
		<%-- edit event represents "enter", "enterReview", "upload", etc. depending upon the instrument --%>
		<tags:eventActionButton buttonImage="edit" component="${groupComponent}" action="edit" pageName="${pageName}" locked="${locked}"/>
		<tags:eventActionButton buttonImage="status" component="${groupComponent}" action="status" pageName="${pageName}"/>
		<tags:eventActionButton buttonImage="delete" component="${groupComponent}" action="bulkDelete" pageName="${pageName}" locked="${locked}"/>
	</tags:listCell> 
	<tags:listCell colspan="5">
		<span class="bold" id="numSelectedParent">&nbsp;</span> ${selectedCountMsg}&nbsp;&nbsp;
	</tags:listCell>
</tags:listRow>
