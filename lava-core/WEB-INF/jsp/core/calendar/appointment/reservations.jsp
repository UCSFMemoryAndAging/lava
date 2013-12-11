<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="reservations"/>
<c:set var="calendarDateRange"><tags:componentListFilterProperty component="${component}" property="displayRange"/></c:set>
<c:set var="dayLength"><tags:componentListFilterProperty component="${component}" property="showDayLength"/></c:set>
<c:set var="calendarDateStart"><tags:componentListFilterProperty component="${component}" property="overlapDateRangeStart"/></c:set>
<c:set var="calendarDateEnd"><tags:componentListFilterProperty component="${component}" property="overlapDateRangeEnd"/></c:set>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="quicklinks"></page:param>
     <page:param name="pageHeadingArgs"><tags:componentProperty component="calendar" property="name"/></page:param>
 

<page:applyDecorator name="component.calendar.content">
	<page:param name="component">${component}</page:param>
	<page:param name="calendarDateRange">${calendarDateRange}</page:param>
	<page:param name="dayLength">${dayLength}</page:param>
	<page:param name="calendarDateStart">${calendarDateStart}</page:param>
	<page:param name="calendarDateEnd">${calendarDateEnd}</page:param>
	<c:import url="/WEB-INF/jsp/core/calendar/appointment/reservationsContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>

</page:applyDecorator>    

</page:applyDecorator>    
	    
