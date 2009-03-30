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
<%@ attribute name="hasOwnFlows" type="java.lang.Boolean" required="false"
       description="if an instrument has its own flows, the events must be prefixed by the instrType
       				instead of by 'instrument' which is used by all instruments which share the 
       				core instrument flows, or by 'macdiagnosis' which is used for all versions of 
       				diagnosis."%>
<c:choose>
	<c:when test="${fn:startsWith(instrTypeEncoded, 'macdiagnosis')}">
		<c:set var="eventPrefix" value="macdiagnosis"/>
	</c:when>
	<c:when test="${hasOwnFlows}">
		<c:set var="eventPrefix" value="${instrTypeEncoded}"/>
	</c:when>
	<c:otherwise>
		<c:set var="eventPrefix" value="instrument"/>		
	</c:otherwise>
</c:choose>	



<tags:listActionURLButton buttonImage="view" actionId="${actionId}" eventId="${eventPrefix}__view" idParam="${idParam}"/>	    

<%-- enter and enterReview flow are mutually exclusive for a given instrument, so will only have one or the other --%>
<c:if test="${instrumentConfig[instrTypeEncoded].enterFlow}">
		<tags:listActionURLButton buttonImage="edit" actionId="${actionId}" eventId="${eventPrefix}__enter" idParam="${idParam}"/>	    
</c:if>
<c:if test="${instrumentConfig[instrTypeEncoded].enterReviewFlow}">
		<tags:listActionURLButton buttonImage="edit" actionId="${actionId}" eventId="${eventPrefix}__enterReview" idParam="${idParam}"/>	    
</c:if>
<c:if test="${instrumentConfig[instrTypeEncoded].collectFlow}">
		<tags:listActionURLButton buttonImage="edit" actionId="${actionId}" eventId="${eventPrefix}__collect" idParam="${idParam}"/>	    
</c:if>
<c:if test="${instrumentConfig[instrTypeEncoded].uploadFlow}">
<tags:listActionURLButton buttonImage="edit" actionId="${actionId}" eventId="${eventPrefix}__upload" idParam="${idParam}"/>	    
</c:if>

<tags:listActionURLButton buttonImage="status" actionId="${actionId}" eventId="${eventPrefix}__status" idParam="${idParam}"/>	    

<tags:listActionURLButton buttonImage="delete" actionId="${actionId}" eventId="${eventPrefix}__delete" idParam="${idParam}"/>	    
		   
