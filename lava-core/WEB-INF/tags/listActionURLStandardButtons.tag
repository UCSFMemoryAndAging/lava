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
      
<%-- the list action buttons for entity CRUD are part of the flow conversation from the listing to the 
 entity page. this is indicated by supplying an eventId, which will result in the flowExecutionKey and
 eventId being propagated, and the request will participate in the existing flow conversation --%>       
<tags:listActionURLButton buttonImage="view" actionId="${actionId}" eventId="${component}__view" idParam="${idParam}"/>	    
<tags:listActionURLButton buttonImage="edit" actionId="${actionId}" eventId="${component}__edit" idParam="${idParam}"/>	    
<tags:listActionURLButton buttonImage="delete" actionId="${actionId}" eventId="${component}__delete" idParam="${idParam}"/>	    
		   