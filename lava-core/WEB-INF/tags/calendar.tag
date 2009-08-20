<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- calendar

	 renders a calendar and encasulates the logic for displaying a day, week, and month 
	 view as well as different length days
 
--%>

<%@ attribute name="component" required="true"
              description="the component name" %>
<%@ attribute name="pageName" required="true"
              description="the page name" %>
<%@ attribute name="description" required="true"
              description="the description for the calendar title bar" %>
<%@ attribute name="range" required="true"
              description="the range to display, 'Day','Week','Month'" %>
<%@ attribute name="dayLength" required="true"
              description="the length of the day to display 'Work Day','Full Day'" %>
              
<c:choose>
	<c:when test="${range == 'Week'}"> 
		<div class="calendarWeekBox">
	</c:when>
	<c:when test="${range == 'Day'}"> 
		<div class="calendarDayBox">
	</c:when>
</c:choose>	


	<tags:calendarHeader component="${component}" pageName="${pageName}" description="${description}" range="${range}" dayLength="${dayLength}"/>



	<c:if test="${dayLength=='Full Day'}">
	
	<tags:calendarHour hour="12" suffix="AM" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="1" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="2" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="3" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="4" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="5" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	</c:if>
	
	<tags:calendarHour hour="6" suffix="AM" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="7" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="8" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarWorkHour hour="9" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarWorkHour hour="10" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarWorkHour hour="11" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarWorkHour hour="12" suffix="PM" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarWorkHour hour="1" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarWorkHour hour="2" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarWorkHour hour="3" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarWorkHour hour="4" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarWorkHour hour="5" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="6" suffix="PM" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="7" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="8" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="9" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	
	
	<c:if test="${dayLength=='Full Day'}">
	
	<tags:calendarHour hour="10" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	<tags:calendarHour hour="11" suffix="00" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	</c:if>



		<jsp:doBody/>

</div> <%--End of bounding calendar box div --%>

