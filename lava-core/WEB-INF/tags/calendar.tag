<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- calendar

	 renders a calendar and encapsulates the logic for displaying a day, week, and month 
	 view as well as different length days
 
--%>
<%@ attribute name="component" required="true"
              description="the component name" %>
<%@ attribute name="calendar" type="edu.ucsf.lava.core.calendar.model.Calendar" required="true" 
              description="the calendar to render" %>
<%@ attribute name="pageName" required="true"
              description="the page name" %>
<%@ attribute name="description" required="true"
              description="the description for the calendar title bar" %>
<%@ attribute name="calendarDateRange" required="true"
              description="the range to display, 'Day','Week','Month'" %>
<%@ attribute name="dayLength" required="true"
              description="the length of the day to display 'Work Day','Full Day'" %>
<%@ attribute name="calendarDateStart" required="true" type="java.util.Date"
              description="the first day of the viewable calendar date range" %>
<%@ attribute name="dayActionId" required="false"
			  description="actionID for click event of calendar day" %>
<%@ attribute name="dayEventId" required="false"
			  description="eventId for click event of calendar day ; defaults to __view" %>
<%@ attribute name="timeActionId" required="false"
			  description="actionID for click event of calendar time interval" %>
<%@ attribute name="timeEventId" required="false"
			  description="eventId for click event of calendar time interval; defaults to __view" %>
			                       

	<%-- set render parameters for use by calendar renderer --%>
	<c:if test="${!empty calendarDateRange}">
		<tags:setRenderParam parameter="calendarDateRange" paramValue="${calendarDateRange}"/>
	</c:if>	
	<c:if test="${!empty dayLength}">
		<tags:setRenderParam parameter="dayLength" paramValue="${dayLength}"/>
	</c:if>	
	<c:if test="${!empty calendarDateStart}">
		<tags:setRenderParam parameter="calendarDateStart" paramValue="${calendarDateStart}"/>
	</c:if>	
	<c:if test="${!empty dayActionId}">
		<c:if test="${empty dayEventId}">
			<c:set var="dayEventId" value="__view"/>
		</c:if>
		<c:set var="dayClickEventUrl"><tags:actionURL actionId="${dayActionId}" eventId="${dayEventId}" parameters="calendarId,${calendar.id}" flowExecutionKey="${flowExecutionKey}"/></c:set>
		<tags:setRenderParam parameter="dayClickEventUrl" paramValue="${dayClickEventUrl}"/>
	</c:if>	
	<c:if test="${!empty timeActionId}">
		<c:if test="${empty timeEventId}">
			<c:set var="timeEventId" value="__view"/>
		</c:if>
		<c:set var="timeClickEventUrl"><tags:actionURL actionId="${timeActionId}" eventId="${timeEventId}" parameters="calendarId,${calendar.id}" flowExecutionKey="${flowExecutionKey}"/></c:set>
		<tags:setRenderParam parameter="timeClickEventUrl" paramValue="${timeClickEventUrl}"/>
	</c:if>	
	
	<tags:calendarHeader component="${component}" pageName="${pageName}" description="${description}" calendarDateRange="${calendarDateRange}" dayLength="${dayLength}"/>
	
	<tags:render entity="${calendar}"></tags:render>
	
	<jsp:doBody/>

<%--End of bounding calendar box div --%>

