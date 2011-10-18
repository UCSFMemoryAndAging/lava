<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>


<content tag="listFilters">
	
	<tags:contentColumn columnClass="colLeft2Col5050">
	
	<tags:listFilterField property="patient.firstName" component="${component}" entityType="enrollmentStatus"/>
	<tags:listFilterField property="patient.lastName" component="${component}" entityType="enrollmentStatus"/>
	<tags:listFilterField property="latestDesc" component="${component}" entityType="enrollmentStatus"/>
	
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
	<c:if test="${empty currentProject}">
		<tags:listFilterField property="projName" component="${component}" entityType="enrollmentStatus"/>
	</c:if>
	<tags:listFilterField property="enrolledDateStart" component="${component}" entityType="enrollmentStatus"/>
	<tags:listFilterField property="enrolledDateEnd" component="${component}" entityType="enrollmentStatus"/>
	</tags:contentColumn>
</content>

<c:if test="${not empty currentPatient}">
<content tag="customActions">
		<tags:actionURLButton buttonText="Add"  actionId="lava.crms.enrollment.status.addEnrollmentStatus" idParam="${currentPatient.id}" eventId="addEnrollmentStatus__add" component="${component}"/>
</content>
</c:if>

<content tag="listColumns">
<c:choose>
	<c:when test="${empty currentProject}">
		<tags:listRow>
		<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
		<tags:componentListColumnHeader component="${component}" label="Patient" width="20%" sort="patient.fullNameRevNoSuffix"/>
		<tags:componentListColumnHeader component="${component}" label="Project" width="15%" sort="projName"/>
		<tags:componentListColumnHeader component="${component}" label="Enrolled" width="10%" sort="enrolledDate"/>
		<tags:componentListColumnHeader component="${component}" label="Latest Status (Date)" width="20%" sort="latestDesc"/>
		<tags:componentListColumnHeader component="${component}" label="Latest Status Note" width="22%"/>
		</tags:listRow>
	</c:when>
	<c:otherwise>
		<tags:listRow>
		<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
		<tags:componentListColumnHeader component="${component}" label="Patient" width="20%" sort="patient.fullNameRevNoSuffix"/>
		<tags:componentListColumnHeader component="${component}" label="Enrolled" width="10%" sort="enrolledDate"/>
		<tags:componentListColumnHeader component="${component}" label="Latest Status (Date)" width="20%" sort="latestDesc"/>
		<tags:componentListColumnHeader component="${component}" label="Latest Status Note" width="42%"/>
		</tags:listRow>
	</c:otherwise>
</c:choose>
</content>



<tags:list component="${component}">
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.enrollment.status.enrollmentStatus" component="enrollmentStatus" idParam="${item.id}" locked="${item.locked}"/>	    
		</tags:listCell>
	    <tags:listCell>
			<tags:listField property="patient.fullNameRevNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="enrollmentStatus" metadataName="patient.fullNameRevNoSuffix"/>
			<tags:listActionURLButton buttonImage="view" actionId="lava.crms.people.patient.patient" eventId="patient__view" idParam="${item.patient.id}"/>	    
		</tags:listCell>
	<c:if test="${empty currentProject}">
		<tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="enrollmentStatus"/>	
		</tags:listCell>
	</c:if>
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
