<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>




<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Select" width="11%"/>
<tags:componentListColumnHeader component="${component}" label="Name" width="20%" sort="name"/>
<tags:componentListColumnHeader component="${component}" label="Description" width="40%" sort="description"/>
<tags:componentListColumnHeader component="${component}" label="Type" width="20%" sort="type"/>

</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		 <tags:listCell styleClass="actionButton">
			
		<c:choose>
			<c:when test="${item.type == 'Resource'}">
				<tags:listActionURLButton buttonImage="list" actionId="lava.core.calendar.appointment.reservations" idParam="${item.id}" title="reservations"/>
			</c:when>
			<c:otherwise>
				<tags:listActionURLButton buttonImage="list" actionId="lava.core.calendar.appointment.appointments" idParam="${item.id}" title="appointments"/>
			</c:otherwise>
		</c:choose>
			
	   </tags:listCell>
	   <tags:listCell>
			<tags:listField property="name" component="${component}" listIndex="${iterator.index}" entityType="calendar"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="description" component="${component}" listIndex="${iterator.index}" entityType="calendar"/>
		</tags:listCell>
		<tags:listCell>
			<c:choose>
			<c:when test="${item.type == 'Resource'}">
					<tags:listField property="resourceType" component="${component}" listIndex="${iterator.index}" entityType="resourceCalendar"/>
			</c:when>
			<c:otherwise>
					<tags:listField property="type" component="${component}" listIndex="${iterator.index}" entityType="calendar"/>
			</c:otherwise>
		</c:choose></tags:listCell>
		
		
	</tags:listRow>
</tags:list>
