<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>

<a href="<tags:actionURL actionId="lava.core.admin.calendar.calendar" startMode="add"/>">
<spring:message code="action.lava.core.admin.calendar.calendar.add"/></a><br/>
<a href="<tags:actionURL actionId="lava.core.admin.calendar.calendars" />">
<spring:message code="action.lava.core.admin.calendar.calendars.list"/></a><br/>
<br/>
<br/>

<a href="<tags:actionURL actionId="lava.core.admin.calendar.resourceCalendar" startMode="add"/>">
<spring:message code="action.lava.core.admin.calendar.resourceCalendar.add"/></a><br/>
<a href="<tags:actionURL actionId="lava.core.admin.calendar.resourceCalendars"/>">
<spring:message code="action.lava.core.admin.calendar.resourceCalendars.list"/></a><br/>



</body>
</html>