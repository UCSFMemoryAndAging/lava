<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- calendarheader

	 renders an hour on the calendar and encasulates the logic for displaying a day, week, and month view 
 
--%>

<%@ attribute name="component" required="true"
              description="the component name" %>
<%@ attribute name="pageName" required="true"
              description="the page name" %>
<%@ attribute name="range" required="true"
              description="the range to display, 'Day','Week','Month'" %>
<%@ attribute name="dayLength" required="true"
              description="the length of the day to display 'Work Day','Full Day'" %>
<%@ attribute name="hour" required="true"
              description="the hour to render, e.g. 12,1,2,3,4,5..." %>
<%@ attribute name="suffix" required="true"
              description="the suffix, e.g. 00 or AM or PM" %>
<%@ attribute name="intervals" 
              description="the number of intervals, defaults to 2" %>
<%@ attribute name="intervalClass" 
              description="optional class to apply to the interval divs" %>

              
<c:set var="intervals" value="${empty intervals ? 2 : intervals}"/>

              
<c:choose>
	<c:when test="${range == 'Week'}"> 
		<div class="calendarWeekHourBox">
			<div class="calendarWeekHourLabelBox">
				<span class="calendarWeekHourLabel">${hour}</span>
				<span class="${suffix == 'AM' || suffix == 'PM' ? 'calendarWeekHourLabelAMPMSuffix':'calendarWeekHourLabelSuffix'}">${suffix}</span>
			</div>
		
			<c:forEach begin="1" end="${intervals}" var="interval">
				<c:forEach begin="1" end="7" var="day">
					<div class="calendarWeekHour${intervals}IntervalBox 
					<c:if test="${day==1 || day==7}"> calendarIntervalBoxWeekend </c:if> 
					<c:if test="${not empty intervalClass}">${intervalClass}</c:if>" 
					>&nbsp;</div>
				</c:forEach>
			</c:forEach>
		</div>
	</c:when>
	<c:when test="${range == 'Day'}"> 
		<div class="calendarDayHourBox">
			<div class="calendarDayHourLabelBox">
				<span class="calendarDayHourLabel">${hour}</span>
				<span class="${suffix == 'AM' || suffix == 'PM' ? 'calendarDayHourLabelAMPMSuffix':'calendarDayHourLabelSuffix'}">${suffix}</span>
			</div>
			<c:forEach begin="1" end="${intervals}" var="interval">
				<div class="calendarDayHour${intervals}IntervalBox
					<c:if test="${day==1 || day==7}"> calendarIntervalBoxWeekend </c:if> 
					<c:if test="${not empty intervalClass}">${intervalClass}</c:if>" 
					>&nbsp;</div>
			</c:forEach>
		</div>
	</c:when>
</c:choose>			
		
	
  	
	              