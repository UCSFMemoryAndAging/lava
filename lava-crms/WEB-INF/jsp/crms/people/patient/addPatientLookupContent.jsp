<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="foundPatients"/>
<c:set var="pageName">${param.pageName}</c:set>

<tags:componentListHasResults component="foundPatients">

<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
     	<tags:createField property="ignoreMatches" component="addPatient" labelStyle="right"/>
</content>


<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}" label="Action" width="20%"/>
	<tags:componentListColumnHeader component="${component}" label="PIDN" width="15%"sort="id"/>
	<tags:componentListColumnHeader component="${component}" label="Name" width="40%" sort="fullNameRev"/>
	<tags:componentListColumnHeader component="${component}" label="Date of Birth" width="20%" sort="birthDate"/>
</tags:listRow>
</content>


<tags:list component="${component}">
	
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<a href="<tags:actionURL actionId="lava.crms.enrollment.status.addEnrollmentStatus" idParam="${item.id}" startMode="add"/>">Use This Patient</a>
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
</tags:listRow>
</tags:list>

</page:applyDecorator> 

</tags:componentListHasResults>
