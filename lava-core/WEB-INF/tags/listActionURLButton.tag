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
<%@ attribute name="locked" 
       description="[optional] whether the action will be disabled.
                    Ideally want to use this attribute when assigning a modifying action"%>
       				
<%-- to avoid image whisker problem, do not leave any white space between end of <a> tag and beginning of <img> tag --%>		

<%-- default to not locked --%>
<c:if test="${empty locked}">
<c:set var="locked" value="false"/>
</c:if>

<c:set var="title" value="${not empty eventId? eventId :(not empty startMode? startMode: '')}"/>

<c:choose>
<c:when test="${locked}">
<%-- this is same locked viewing as found in eventActionButton.tag; refer to notes there --%>
<img src="images/ACTION_${buttonImage}_locked.png" width="16" height="16" border="0" title="${title} (locked)"/>
</c:when>
<c:otherwise>
<%-- if eventId was specified, participating in an existing flow, so propagate eventId and flowExecutionKey --%>
<c:if test="${not empty eventId}">
<a href="<tags:actionURL actionId="${actionId}" flowExecutionKey="${flowExecutionKey}" eventId="${eventId}" idParam="${empty idParam? '':idParam}" parameters="${parameters}"/>"><img 
	src="images/ACTION_${buttonImage}.png" width="16" height="16" border="0" title="${title}"/></a>
</c:if>

<%-- if eventId is empty, URL starts a new flow, so no flowExecutionKey or eventId parameters (and the
     flowId is derived from the URL itself, where startMode defaults to 'view' if not specified). this
     is useful when the list action is for another list, and since lists usually start new root flows,
     specify listActionURLButton with no eventId --%>
<c:if test="${empty eventId}">	
<a href="<tags:actionURL actionId="${actionId}" startMode="${empty startMode? 'view':startMode}" idParam="${empty idParam? '':idParam}" parameters="${parameters}"/>"><img 
	src="images/ACTION_${buttonImage}.png" width="16" height="16" border="0" title="${title}"/></a>
</c:if>
</c:otherwise>
</c:choose>

