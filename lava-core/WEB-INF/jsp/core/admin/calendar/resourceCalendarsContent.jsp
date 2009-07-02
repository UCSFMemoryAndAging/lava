<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>



<content tag="customActions">
	<tags:actionURLButton buttonText="Add Calendar"  actionId="lava.core.admin.calendar.resourceCalendar" eventId="resourceCalendar__add" component="${component}" />	    
</content>




<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="11%"/>
<tags:componentListColumnHeader component="${component}" label="Name" width="20%" sort="name"/>
<tags:componentListColumnHeader component="${component}" label="Description" width="40%" sort="description"/>
<tags:componentListColumnHeader component="${component}" label="Type" width="20%" sort="resourceType"/>
<tags:componentListColumnHeader component="${component}" label="Location/<br/>Contact" width="29%"/>

</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		 <tags:listCell styleClass="actionButton">
				<tags:listActionURLStandardButtons actionId="lava.core.admin.calendar.resourceCalendar" component="resourceCalendar" idParam="${item.id}"/>
		</tags:listCell>
	   <tags:listCell>
			<tags:listField property="name" component="${component}" listIndex="${iterator.index}" entityType="calendar"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="description" component="${component}" listIndex="${iterator.index}" entityType="calendar"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="resourceType" component="${component}" listIndex="${iterator.index}" entityType="resourceCalendar"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="location" component="${component}" listIndex="${iterator.index}" entityType="resourceCalendar"/><br/>
			<tags:listField property="contact.userName" component="${component}" listIndex="${iterator.index}" entityType="resourceCalendar"/>
		</tags:listCell>
		
	</tags:listRow>
</tags:list>
