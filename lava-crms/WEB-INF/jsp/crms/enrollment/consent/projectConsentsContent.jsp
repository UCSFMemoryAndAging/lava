<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<c:if test="${not empty currentPatient}">
<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.enrollment.consent.consent"  eventId="consent__add" component="${component}"/>	    
</content>
</c:if>

<content tag="listFilters">
	
	<tags:contentColumn columnClass="colLeft2Col5050">
	
	<tags:listFilterField property="patient.firstName" component="${component}" entityType="consent"/>
	<tags:listFilterField property="patient.lastName" component="${component}" entityType="consent"/>
	<tags:listFilterField property="consentType" component="${component}" entityType="consent"/>
	<c:if test="${empty currentProject}">
		<tags:listFilterField property="projName" component="${component}" entityType="enrollmentStatus"/>
	</c:if>
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
	<tags:listFilterField property="consentDeclined" component="${component}" entityType="consent"/>
	<tags:listFilterField property="consentDateStart" component="${component}" entityType="consent"/>
	<tags:listFilterField property="consentDateEnd" component="${component}" entityType="consent"/>
	</tags:contentColumn>
</content>

<content tag="listColumns">
<c:choose>
	<c:when test="${empty currentProject}">
		<tags:listRow>
		<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
		<tags:componentListColumnHeader component="${component}" label="Patient" width="20%" sort="patient.fullNameRevNoSuffix"/>
		<tags:componentListColumnHeader component="${component}" label="Project" width="15%" sort="projName"/>
		<tags:componentListColumnHeader component="${component}" label="Consent Type" width="27%" sort="consentType"/>
		<tags:componentListColumnHeader component="${component}" label="HIPAA" width="8%"/>
		<tags:componentListColumnHeader component="${component}" label="Declined" width="10%" sort="consentDeclined"/>
		<tags:componentListColumnHeader component="${component}" label="Consent Date" width="13%" sort="consentDate"/>
		</tags:listRow>
	</c:when>
	<c:otherwise>
		<tags:listRow>
		<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
		<tags:componentListColumnHeader component="${component}" label="Patient" width="20%" sort="patient.fullNameRevNoSuffix"/>
		<tags:componentListColumnHeader component="${component}" label="Consent Type" width="32%" sort="consentType"/>
		<tags:componentListColumnHeader component="${component}" label="HIPAA" width="8%"/>
		<tags:componentListColumnHeader component="${component}" label="Declined" width="10%" sort="consentDeclined"/>
		<tags:componentListColumnHeader component="${component}" label="Consent Date" width="13%" sort="consentDate"/>
		</tags:listRow>
	</c:otherwise>
</c:choose>
</content>



<tags:list component="${component}">
	<tags:listRow>
		 <tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.enrollment.consent.consent" component="consent" idParam="${item.id}" locked="${item.locked}"/>	    
		</tags:listCell>
	   <tags:listCell>
			<tags:listField property="patient.fullNameRevNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="consent" metadataName="patient.fullNameRevNoSuffix"/>
		</tags:listCell>
	 <c:if test="${empty currentProject}">
		<tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="enrollmentStatus"/>	
		</tags:listCell>
	</c:if>
		  <tags:listCell>
		  		<tags:listField property="consentTypeBlock" component="${component}" listIndex="${iterator.index}" entityType="consent"/>
		  </tags:listCell>
		  <tags:listCell>
		  		<tags:listField property="hipaa" component="${component}" listIndex="${iterator.index}" entityType="consent"/>
		  </tags:listCell>
		<tags:listCell>
			<tags:listField property="consentDeclined" component="${component}" listIndex="${iterator.index}" entityType="consent"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="consentDate" component="${component}" listIndex="${iterator.index}" entityType="consent"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>
