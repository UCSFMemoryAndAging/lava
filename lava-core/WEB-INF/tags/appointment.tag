<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ attribute name="component" required="true"
              description="the component name" %>
<%@ attribute name="appointment" type="edu.ucsf.lava.core.calendar.model.Appointment" required="true" 
              description="the appointment to render" %>
<%@ attribute name="content" required="true" 
              description="the string content to display for the appointment" %>
<%@ attribute name="calendarDateRange" required="false"  
              description="updates render param for the calendar view range (e.g. week vs. day)" %>
<%@ attribute name="dayLength" required="false" 
              description="updates render param for day length (work vs. full)  " %>
<%@ attribute name="calendarDateField" required="false"
			  description="used to update render param for beginning date of the calendar view" %>
<%@ attribute name="actionId" required="false" 
			  description="the action ID for the click event of the appointment box" %>
<%@ attribute name="eventId" required="false" 
			  description="the event ID for the click event of the appointment box" %>
   
<c:if test="${!empty calendarDateRange}">
	<c:set var="parameters" value="calendarDateRange,${calendarDateRange}"/>
	<tags:setRenderParams parameters="${parameters}"/>
</c:if>
<c:if test="${!empty dayLength}">
	<c:set var="parameters" value="dayLength,${dayLength}"/>
	<tags:setRenderParams parameters="${parameters}"/>
</c:if>
<c:if test="${!empty calendarDateField}">
	<c:set var="calendarDateStartField">
	  ${calendarDateField}Start
	</c:set>	
	<tags:setRenderParam parameter="calendarDateStart" paramValue="${command.components[component].filter.params[calendarDateStartField]}"/>
</c:if>	

<%-- add appointment id and content to render list parameters. Actual render is done by calendar tag --%>
<tags:addRenderParamElement parameter="apptIdList" paramValue="${appointment.id}"/>
<c:set var="apptUrl">
	<tags:actionURL actionId="${actionId}" eventId="${eventId}"  flowExecutionKey="${flowExecutionKey}" idParam="${appointment.id}"/>
</c:set>
<tags:addRenderParamElement parameter="apptContentList" paramValue="${content}"/>
<tags:addRenderParamElement parameter="apptUrlList" paramValue="${apptUrl}"/>


</a>