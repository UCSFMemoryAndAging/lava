<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- listActionURLStandardButtons
	create the standard view, edit, delete action URL buttons for use in a list column
--%>

<%@ attribute name="actionId" required="true" 
       description="the action id to use" %>
<%@ attribute name="component" required="true" 
       description="the name of the component used to construct the eventId" %>
<%@ attribute name="idParam" 
       description="an idParam for the action"%>
<%@ attribute name="parameters" required="false"
       description="extra parameters, must be supplied as comma delimited pairs of param names and values
       				e.g. paramname1,paramvalue1,paramname2,paramvalue2"%>
<%@ attribute name="locked" 
       description="[optional] whether modifying actions would be disabled"%>
<%@ attribute name="noDelete" type="java.lang.Boolean"
       description="[optional] hide the delete action"%>

<%-- the list action buttons for entity CRUD are part of the flow conversation from the listing to the 
 entity page. this is indicated by supplying an eventId, which will result in the flowExecutionKey and
 eventId being propagated, and the request will participate in the existing flow conversation --%>       
<tags:listActionURLButton buttonImage="view" actionId="${actionId}" eventId="${component}__view" idParam="${idParam}" parameters="${parameters}"/>	    
<tags:listActionURLButton buttonImage="edit" actionId="${actionId}" eventId="${component}__edit" idParam="${idParam}" parameters="${parameters}" locked="${locked}"/>
<c:if test="${!noDelete}">
<tags:listActionURLButton buttonImage="delete" actionId="${actionId}" eventId="${component}__delete" idParam="${idParam}" parameters="${parameters}" locked="${locked}"/>	    
</c:if>		