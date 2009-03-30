<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="patientCaregivers"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.caregiver.caregiver" eventId="caregiver__add" component="${component}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>

<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Name" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Relation" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Contact Status" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Roles" width="20%"/>
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

