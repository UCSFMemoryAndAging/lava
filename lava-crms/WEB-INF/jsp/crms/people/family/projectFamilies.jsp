<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>


<c:set var="component" value="projectFamilies"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects /' : currentProject.name}</page:param>

	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="listFilters">
	<tags:listFilterField property="firstName" component="${component}" entityType="patient"/>
	<tags:listFilterField property="lastName" component="${component}" entityType="patient"/>
</content>



<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}" label="Action" width="10%"/>
	<tags:componentListColumnHeader component="${component}" label="PIDN" width="10%" sort="id"/>
	<tags:componentListColumnHeader component="${component}" label="Proband" width="40%" sort="fullNameRev"/>
	<tags:componentListColumnHeader component="${component}" label="Date of Birth" width="15%" sort="birthDate"/>
	<tags:componentListColumnHeader component="${component}" label="Age" width="10%" sort="birthDate"/>	
	<tags:componentListColumnHeader component="${component}" label="Twin" width="15%" sort="twinZygosity"/>	
</tags:listRow>
</content>


<tags:list component="${component}">
	
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLButton buttonImage="view" actionId="lava.crms.people.patient.patient" idParam="${item.id}"/>
				<tags:listActionURLButton buttonImage="list" actionId="lava.crms.people.family.familyMembers" idParam="${item.id}"/>
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
			<tags:listField property="twinZygosity" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
</tags:listRow>
</tags:list>

</page:applyDecorator> 
</page:applyDecorator>   
	    

