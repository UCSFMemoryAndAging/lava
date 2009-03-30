<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- actionURL
	 Generate a lava action URL
--%>

<%@ attribute name="actionId" required="true"
       description="the action id used to lookup the action from the actionservice"%>
<%@ attribute name="startMode" required="false"
       description="this is used if a new flow should be started (when flowExecutionKey is empty). 
       				if it is not supplied, when the request starts a new flow, the flow id will default
       				to 'view' startMode. it is typically used with the left nav actions because it 
       				facilitates starting a flow which is usually a subflow as a root flow, 
       				e.g. startMode=add could start the Add Visit flow as a root flow. normally Add Visit 
       				is a subflow of the visitInstruments flow."%>
<%@ attribute name="flowExecutionKey" required="false"
       description="webflow flow execution key. if not empty, it means that the request is to participate
       				in an existing flow, propagatating both flowExecutionKey and eventId"%>
<%@ attribute name="eventId" required="false"
       description="the event for an existing flow"%>
<%@ attribute name="idParam" required="false"
       description="the id query parameter, will be added to the url as id=[idparam]"%>
<%@ attribute name="parameters" required="false"
       description="extra parameters, must be supplied as comma delimited pairs of param names and values
       				e.g. paramname1,paramvalue1,paramname2,paramvalue2"%>
       				
<%-- c:url prepends the context path to relative URL's, whether context-relative (starting with '/') or 
page-relative (not starting with '/') which is what actions[actionId].actionUrl returns --%>       				
<c:url var="actionUrl" value="${actions[actionId].actionUrl}">   
	<c:choose>
		<%-- MUST explicitly test pageScoped flowExecutionKey which refers to the attribute value because if there is
			an existing flow, there will be a requestScoped flowExecutionKey that is definitely not empty --%>
		<c:when test="${empty pageScope.flowExecutionKey}">
			<%-- construct URL that will start a new flow (simply due to absence of _flowExecutionKey request parameter --%>
			<c:if test="${not empty startMode}"><c:param name="_do" value="${startMode}"/></c:if>
		</c:when>
		<c:otherwise>		
			<%-- construt URL that will participate in existing flow by propagating _flowExecutionKey and _eventId --%>
			<c:param name="_flowExecutionKey" value="${flowExecutionKey}"/>
			<%-- eventId only pertains to existing flows (if not supplied, web flow will just refresh the page on this link) --%>
			<c:if test="${not empty eventId}"><c:param name="_eventId" value="${eventId}"/></c:if>
		</c:otherwise>
	</c:choose>					
	
	<c:if test="${not empty idParam}"><c:param name="id" value="${idParam}"/></c:if>

	<c:forTokens items="${parameters}" delims="," var="current" varStatus="status">
      	<c:if test="${status.index % 2 == 0}">
        	<c:set var="paramName" value="${current}"/>
      	</c:if>
      	<c:if test="${status.index % 2 == 1}">
        	<c:param name="${paramName}" value="${current}"/>
	    </c:if>
    </c:forTokens>
</c:url>
${actionUrl}

