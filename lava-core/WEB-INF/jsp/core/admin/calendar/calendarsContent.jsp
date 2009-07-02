<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>



<content tag="customActions">
	<tags:actionURLButton buttonText="Add Calendar"  actionId="lava.core.admin.calendar.calendar" eventId="calendar__add" component="${component}" />	    
	<tags:actionURLButton buttonText="Add Resource Cal."  actionId="lava.core.admin.calendar.resourceCalendar" eventId="resourceCalendar__add" component="${component}" />	    

</content>




<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="11%"/>
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
				<tags:listActionURLStandardButtons actionId="lava.core.admin.calendar.resourceCalendar" component="resourceCalendar" idParam="${item.id}"/>
			</c:when>
			<c:otherwise>
				<tags:listActionURLStandardButtons actionId="lava.core.admin.calendar.calendar" component="calendar" idParam="${item.id}"/>
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
			<tags:listField property="type" component="${component}" listIndex="${iterator.index}" entityType="calendar"/>
		</tags:listCell>
		
		
	</tags:listRow>
</tags:list>
