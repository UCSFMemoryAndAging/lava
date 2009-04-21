<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- ifComponentPropertyNotEmpty
	 if the component property specified is not empty then do the body
--%>
<%@ attribute name="component" required="true" 
       description="the component" %>
<%@ attribute name="property" required="true" 
       description="the property" %>
<%@ attribute name="property2" 
       description="an optional second property" %>  
<%@ attribute name="listIndex" 
       description="if specified use the list lookup syntax for the component property" %>  


<c:choose>
	<c:when test="${empty listIndex}">
		<c:choose>
			<c:when test="${empty property2}">
				<c:set var="theProperty" value="${command.components[component][property]}"/>
			</c:when>
			<c:otherwise>
				<c:set var="theProperty" value="${command.components[component][property][property2]}"/>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${empty property2}">
				<c:set var="theProperty" value="${command.components[component].pageList[listIndex].entity[property]}"/>
			</c:when>
			<c:otherwise>
					<c:set var="theProperty" value="${command.components[component].pageList[listIndex].entity[property][property2]}"/>
			</c:otherwise>
		</c:choose>
	</c:otherwise>	
</c:choose>
<c:if test="${not empty theProperty}">
	<jsp:doBody/>
</c:if>
