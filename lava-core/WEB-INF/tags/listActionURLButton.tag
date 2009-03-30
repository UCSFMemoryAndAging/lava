<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- listActionURLButton
	create a standard action URL button for use in a list column
--%>
<%@ attribute name="buttonImage" required="true"
       description="the part of the button image name that varies...e.g. cancel, delete, etc. "%>
<%@ attribute name="actionId" required="true" 
       description="the action id to use" %>
<%@ attribute name="eventId"
       description="[optional] flow event id" %>      
<%@ attribute name="startMode"
       description="[optional] mode for new flow (eventId is empty) defaults to 'view'" %>      
<%@ attribute name="idParam" 
       description="[optional] an idParam for the action"%>
<%@ attribute name="parameters" required="false"
       description="extra parameters, must be supplied as comma delimited pairs of param names and values
       				e.g. paramname1,paramvalue1,paramname2,paramvalue2"%>
       				
<%-- to avoid image whisker problem, do not leave any white space between end of <a> tag and beginning of <img> tag --%>		

<%-- if eventId was specified, participating in an existing flow, so propagate eventId and flowExecutionKey --%>
<c:if test="${not empty eventId}">
<a href="<tags:actionURL actionId="${actionId}" flowExecutionKey="${flowExecutionKey}" eventId="${eventId}" idParam="${empty idParam? '':idParam}" parameters="${parameters}"/>"><img 
	src="images/ACTION_${buttonImage}.png" width="16" height="16" border="0" title="${eventId}"/></a>
</c:if>

<%-- if eventId is empty, URL starts a new flow, so no flowExecutionKey or eventId parameters (and the
     flowId is derived from the URL itself, where startMode defaults to 'view' if not specified). this
     is useful when the list action is for another list, and since lists usually start new root flows,
     specify listActionURLButton with no eventId --%>
<c:if test="${empty eventId}">	
<a href="<tags:actionURL actionId="${actionId}" startMode="${empty startMode? 'view':startMode}" idParam="${empty idParam? '':idParam}" parameters="${parameters}"/>"><img 
	src="images/ACTION_${buttonImage}.png" width="16" height="16" border="0" title="${empty startMode? '':startMode}"/></a>
</c:if>


