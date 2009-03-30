<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- formatDateRange

--%>

<%@ attribute name="range" required="true" 
              description="the range, i.e. Day, Week, Month" %>
<%@ attribute name="beginDate" type="java.util.Date" required="false" 
              description="the begin date of the range" %>
<%@ attribute name="endDate" type="java.util.Date" required="false" 
              description="the end date of the range" %>
<%@ attribute name="shortFormat" required="false" %>


<c:choose>
	<c:when test="${range == 'All'}">
		All Dates
 	</c:when>
	<c:when test="${range == 'Day' && empty shortFormat}">
		<fmt:formatDate value="${beginDate}" pattern="EEEE  MMMM d yyyy"/>
 	</c:when>
	<c:when test="${range == 'Day' && not empty shortFormat}">
		<fmt:formatDate value="${beginDate}" pattern="MMM dd, yyyy"/>
 	</c:when>
 	<c:when test="${(range == 'Week' || range == 'Custom') && empty shortFormat}">
 	    <fmt:formatDate value="${beginDate}" pattern="EEEE  MMMM d yyyy"/> to <fmt:formatDate value="${endDate}" pattern="EEEE  MMMM d yyyy"/>
    </c:when>
 	<c:when test="${(range == 'Week' || range == 'Custom') && not empty shortFormat}">
 	    <fmt:formatDate value="${beginDate}" pattern="MMM dd, yyyy"/> - <fmt:formatDate value="${endDate}" pattern="MMM dd, yyyy"/>
    </c:when>
    <c:when test="${range == 'Month'}">
    	<fmt:formatDate value="${beginDate}" pattern="MMMM yyyy"/>
    </c:when>
</c:choose>        		
	
