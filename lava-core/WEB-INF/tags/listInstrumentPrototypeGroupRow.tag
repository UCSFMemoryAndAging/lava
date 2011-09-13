<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ tag body-content="empty" %>


<%-- component and pageName are typically different, as pageName is the current list
component and component is the group handler component, where the group handler is a
secondary handler to the primary list handler and is used to create the group. So when
an eventActionButton is clicked the event prefix should be this group handler component
so that the secondary handler can create the group --%> 

<%@ attribute name="groupPrototype" required="true" 
       description="The name of the instrument group prototype which defines a list of instruments
       				to be selected" %>
<%@ attribute name="groupComponent" required="true" 
       description="The name of the group component whose handler handles group events" %>
<%@ attribute name="pageName" required="true" 
       description="The name of the page, i.e. HTML form to post" %>
<%@ attribute name="locked" 
       description="[optional] whether modifying actions would be disabled" %>

<%-- instrument group prototype actions --%>
<c:if test="${not empty groupPrototype}"> 
<tags:listRow>

	<tags:listCell width="2%">
		&nbsp;
	</tags:listCell>

	<tags:listCell styleClass="actionButton" width="12%">
		<%-- append actions with "_prototype" so event handling will create the group from a project-visit
			instrument group prototype instead of user selected items. this is not necessary for bulkDelete
			which only applies to groups created from prototypes --%>
		<tags:eventActionButton buttonImage="view" component="${groupComponent}" action="view_prototype" pageName="${pageName}"/>
		<%-- edit event represents "enter", "enterReview", "upload", etc. depending upon the instrument --%>
		<tags:eventActionButton buttonImage="edit" component="${groupComponent}" action="edit_prototype" pageName="${pageName}" locked="${locked}"/>
		<tags:eventActionButton buttonImage="status" component="${groupComponent}" action="status_prototype" pageName="${pageName}"/>
		<tags:eventActionButton buttonImage="delete" component="${groupComponent}" action="bulkDelete" pageName="${pageName}" locked="${locked}"/>
	</tags:listCell> 

	<tags:listCell colspan="5">
		<tags:outputText text="${groupPrototype}"/>
	</tags:listCell>

</tags:listRow>

</c:if>

