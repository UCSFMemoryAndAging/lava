<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="allDoctors"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>

	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="listFilters">
	<tags:listFilterField property="firstName" component="${component}" entityType="doctor"/>
	<tags:listFilterField property="lastName" component="${component}" entityType="doctor"/>
</content>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.doctor.doctor" eventId="doctor__add" component="${component}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Doctor Name" width="20%" sort="fullNameRev" />
<tags:componentListColumnHeader component="${component}" label="Location" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Phone/Email" width="20%"/>

</tags:listRow>
</content>




<tags:list component="${component}">
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.people.doctor.doctor" component="doctor" idParam="${item.id}" locked="${item.locked}"/>	    
		</tags:listCell>

		<tags:listCell>
			<tags:listField property="fullNameRev" component="${component}" listIndex="${iterator.index}" entityType="doctor"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="addressBlock" component="${component}" listIndex="${iterator.index}" entityType="doctor"/>	
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="phoneEmailBlock" component="${component}" listIndex="${iterator.index}" entityType="doctor"/>	
		</tags:listCell>
		
	
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>    

