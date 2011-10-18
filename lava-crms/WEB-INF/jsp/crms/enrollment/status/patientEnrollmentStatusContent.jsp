<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.enrollment.status.addEnrollmentStatus" idParam="${currentPatient.id}" eventId="addEnrollmentStatus__add" component="${component}"/>	    
</content>



<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Project" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Enrolled" width="10%"/>
<tags:componentListColumnHeader component="${component}" label="Latest Status (Date)" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Latest Status Note" width="42%"/>
</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.enrollment.status.enrollmentStatus" component="enrollmentStatus" idParam="${item.id}" locked="${item.locked}"/>	    
		</tags:listCell>
	    <tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="enrollmentStatus"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="enrolledDate" component="${component}" listIndex="${iterator.index}" entityType="enrollmentStatus"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="latestDesc" component="${component}" listIndex="${iterator.index}" entityType="enrollmentStatus"/>
			(<tags:listField property="latestDate" component="${component}" listIndex="${iterator.index}" entityType="enrollmentStatus"/>)
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="latestNote" component="${component}" listIndex="${iterator.index}" entityType="enrollmentStatus"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>
