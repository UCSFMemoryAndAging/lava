<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- listInstrumentActionURLStandardButtons
	create the standard instrument view, collect, enter, delete action URL buttons for use in a list column
--%>

<%@ attribute name="actionId" required="true" 
       description="the action id to use" %>
<%@ attribute name="idParam" 
       description="an idParam for the action"%>
<%@ attribute name="instrTypeEncoded" required="true"
       description="the type of the instrument used to access configuration data"%>


<tags:listActionURLButton buttonImage="view" actionId="${actionId}" eventId="${instrTypeEncoded}__view" idParam="${idParam}"/>	    

<%-- enter and enterReview flow are mutually exclusive for a given instrument, so will only have one or the other --%>
<c:if test="${instrumentConfig[instrTypeEncoded].enterFlow}">
		<tags:listActionURLButton buttonImage="edit" actionId="${actionId}" eventId="${instrTypeEncoded}__enter" idParam="${idParam}"/>	    
</c:if>
<c:if test="${instrumentConfig[instrTypeEncoded].enterReviewFlow}">
		<tags:listActionURLButton buttonImage="edit" actionId="${actionId}" eventId="${instrTypeEncoded}__enterReview" idParam="${idParam}"/>	    
</c:if>
<c:if test="${instrumentConfig[instrTypeEncoded].collectFlow}">
		<tags:listActionURLButton buttonImage="edit" actionId="${actionId}" eventId="${instrTypeEncoded}__collect" idParam="${idParam}"/>	    
</c:if>
<c:if test="${instrumentConfig[instrTypeEncoded].uploadFlow}">
<tags:listActionURLButton buttonImage="edit" actionId="${actionId}" eventId="${instrTypeEncoded}__upload" idParam="${idParam}"/>	    
</c:if>

<tags:listActionURLButton buttonImage="status" actionId="${actionId}" eventId="${instrTypeEncoded}__status" idParam="${idParam}"/>	    

<tags:listActionURLButton buttonImage="delete" actionId="${actionId}" eventId="${instrTypeEncoded}__delete" idParam="${idParam}"/>	    
		   
