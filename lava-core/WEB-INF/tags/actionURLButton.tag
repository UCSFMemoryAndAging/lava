<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- actionURLButton
	create a standard action URL button
--%>
<%@ attribute name="buttonText" required="true"
       description="the text to use for the button"%>
<%@ attribute name="actionId" required="true" 
       description="the action id to use" %>
<%@ attribute name="component" required="true"
       description="the component for the action"%>
<%@ attribute name="eventId"
       description="[optional] flow event id" %>      
<%@ attribute name="startMode"  
       description="[optional] mode for new flow (eventId is empty). if no mode (and no eventId), mode defaults to 'view'" %>      
<%@ attribute name="idParam" 
       description="[optional] an idParam for the action"%>
<%@ attribute name="parameters" required="false"
       description="extra parameters, must be supplied as comma delimited pairs of param names and values
       				e.g. paramname1,paramvalue1,paramname2,paramvalue2"%>
       

<%-- to avoid image whisker problem, do not leave any white space between end of <a> tag and beginning of <img> tag --%>		
       
<%-- if eventId was specified, it means that this link is participating in an existing flow, so propagate eventId 
     and flowExecutionKey --%>
<c:if test="${not empty eventId}">
<a href="<tags:actionURL actionId="${actionId}"  flowExecutionKey="${flowExecutionKey}" eventId="${eventId}" idParam="${empty idParam? '':idParam}" parameters="${empty parameters? '':parameters}"/>" class="${(fn:length(buttonText) < 7) ? 'eventButtonSmall ' : (fn:length(buttonText) < 10) ? 'eventButtonMedium ' : (fn:length(buttonText) < 13) ? 'eventButtonLarge ' : 'eventButtonXLarge '} ${empty className ? 'eventButton':className}">${buttonText}</a>
</c:if>

<%-- if eventId is empty, URL starts a new flow, so no flowExecutionKey or eventId parameters (and the
     flowId is derived from the URL itself, where startMode defaults to 'view' if not specified) --%>
<c:if test="${empty eventId}">	
	<a href="<tags:actionURL actionId="${actionId}" startMode="${empty startMode? '':startMode}" idParam="${empty idParam? '':idParam}" parameters="${empty parameters? '':parameters}"/>" class="${(fn:length(buttonText) < 7) ? 'eventButtonSmall ' : (fn:length(buttonText) < 10) ? 'eventButtonMedium ' : (fn:length(buttonText) < 13) ? 'eventButtonLarge ' : 'eventButtonXLarge '} ${empty className ? 'eventButton':className}">${buttonText}</a>
</c:if>



