<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="projectCaregivers"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects /' : currentProject.name}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="listFilters">
<tags:contentColumn columnClass="colLeft2Col5050">
	
	<tags:listFilterField property="patient.firstName" component="${component}" entityType="caregiver"/>
	<tags:listFilterField property="patient.lastName" component="${component}" entityType="caregiver"/>
	<tags:listFilterField property="firstName" component="${component}" entityType="caregiver"/>
	<tags:listFilterField property="lastName" component="${component}" entityType="caregiver"/>
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
		<tags:listFilterField property="relation" component="${component}" entityType="caregiver"/>
		<tags:listFilterField property="contactDesc" component="${component}" entityType="caregiver"/>
		<tags:listFilterField property="rolesDesc" component="${component}" entityType="caregiver"/>
	</tags:contentColumn>
</content>

<c:if test="${not empty currentPatient}">
<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.caregiver.caregiver" eventId="caregiver__add" component="${component}"/>	    
</content>
</c:if>

<content tag="listColumns">
<tags:listRow>

<tags:componentListColumnHeader component="${component}" label="Action" width="9%"/>
<tags:componentListColumnHeader component="${component}" label="Patient" width="20%" sort="patient.fullNameRevNoSuffix"/>
<tags:componentListColumnHeader component="${component}" label="Caregiver" width="20%" sort="fullNameRev"/>
<tags:componentListColumnHeader component="${component}" label="Relation" width="10%" sort="relation"/>
<tags:componentListColumnHeader component="${component}" label="Contact" width="20%" sort="contactDesc"/>
<tags:componentListColumnHeader component="${component}" label="Roles" width="12%" sort="rolesDesc"/>
<tags:componentListColumnHeader component="${component}" label="Lives W/PT" width="6%"/>
<tags:componentListColumnHeader component="${component}" label="Active" width="6%"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLStandardButtons actionId="lava.crms.people.caregiver.caregiver" component="caregiver" idParam="${item.id}"/>	    
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="patient.fullNameNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="caregiver"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="fullName" component="${component}" listIndex="${iterator.index}" entityType="caregiver"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="relation" component="${component}" listIndex="${iterator.index}" entityType="caregiver"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="contactDesc" component="${component}" listIndex="${iterator.index}" entityType="caregiver"/>
			</tags:listCell>
		<tags:listCell>
			<tags:listField property="rolesDesc" component="${component}" listIndex="${iterator.index}" entityType="caregiver"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="livesWithPatient" component="${component}" listIndex="${iterator.index}" entityType="caregiver"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="active" component="${component}" listIndex="${iterator.index}" entityType="caregiver"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

