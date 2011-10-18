<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>


<c:set var="component" value="projectPatients"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects /' : currentProject.name}</page:param>

	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="listFilters">
	<tags:listFilterField property="firstName" component="${component}" entityType="patient"/>
	<tags:listFilterField property="lastName" component="${component}" entityType="patient"/>
</content>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.patient.addPatient" eventId="addPatient__add" component="${component}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}" label="Action" width="10%"/>
	<tags:componentListColumnHeader component="${component}" label="PIDN" width="10%"sort="id"/>
	<tags:componentListColumnHeader component="${component}" label="Name" width="40%" sort="fullNameRev"/>
	<tags:componentListColumnHeader component="${component}" label="Date of Birth" width="15%" sort="birthDate"/>
	<tags:componentListColumnHeader component="${component}" label="Age" width="10%" sort="birthDate"/>	
	<tags:componentListColumnHeader component="${component}" label="Deceased" width="15%" sort="deathDate"/>	
</tags:listRow>
</content>


<tags:list component="${component}">
	
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.people.patient.patient" component="patient" idParam="${item.id}" locked="${item.locked}"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="id" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="fullNameRevNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="birthDate" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="age" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<c:if test="${item.deceased == true}">
			<tags:listField property="deceased" component="${component}" listIndex="${iterator.index}" entityType="patient"/>
				<c:if test="${not empty item.deathDate}">		
				:<tags:listField property="deathDate" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
				</c:if>
			</c:if>
		</tags:listCell>
</tags:listRow>
</tags:list>

</page:applyDecorator> 
</page:applyDecorator>   
	    

