<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<tags:ifComponentExists component="calendar">
	<c:set var="calendarId"><tags:componentProperty component="calendar" property="id"/></c:set>
</tags:ifComponentExists>


<content tag="customActions">
	<tags:actionURLButton buttonText="Add Reservation"  actionId="lava.core.calendar.appointment.reservation" eventId="reservation__add" component="${component}" parameters="calendarId,${calendarId}" />	    
</content>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="11%"/>
<tags:componentListColumnHeader component="${component}"  label="Time" width="20%" sort="startDate,startTime"/>
<tags:componentListColumnHeader component="${component}" label="Organizer" width="20%" sort="organizer.userName"/>
<tags:componentListColumnHeader component="${component}" label="Description" width="30%" sort="description"/>

</tags:listRow>
</content>

<tags:list component="${component}">
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.core.calendar.appointment.reservation" component="reservation" idParam="${item.id}" locked="${item.locked}"/>
		</tags:listCell>
	   <tags:listCell>
			<tags:listField property="startDate" component="${component}" listIndex="${iterator.index}" entityType="appointment"/>
			<tags:listField property="startTime" component="${component}" listIndex="${iterator.index}" entityType="appointment"/><BR/>
			<tags:listField property="endDate" component="${component}" listIndex="${iterator.index}" entityType="appointment"/>
			<tags:listField property="endTime" component="${component}" listIndex="${iterator.index}" entityType="appointment"/>
			
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="organizer.userName" component="${component}" listIndex="${iterator.index}" entityType="reservation"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="description" component="${component}" listIndex="${iterator.index}" entityType="appointment"/>
		</tags:listCell>
	
		
	</tags:listRow>
</tags:list>
