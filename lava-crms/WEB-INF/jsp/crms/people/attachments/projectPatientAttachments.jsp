<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="projectPatientAttachments"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects /' : currentProject.name}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="listFilters">
<tags:contentColumn columnClass="colLeft2Col5050">
	<tags:listFilterField property="patient.firstName" component="${component}" entityType="crmsFile"/>
	<tags:listFilterField property="patient.lastName" component="${component}" entityType="crmsFile"/>
	
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">
		<tags:listFilterField property="fileType" component="${component}" entityType="lavaFile"/>
		<tags:listFilterField property="contentType" component="${component}" entityType="lavaFile"/>
	</tags:contentColumn>
</content>

<c:if test="${not empty currentPatient}">
<content tag="customActions">
	<tags:actionURLButton buttonText="Add" actionId="lava.crms.people.attachments.patientAttachment" eventId="patientAttachment__add" component="${component}"/>	    
	
</content>
</c:if>

<content tag="listQuickFilter">
	<a class="listControlBarActionCurrent" href="<tags:actionURL actionId="lava.crms.people.attachments.projectPatientAttachments"/>">All PDFs</a> | 
	<a class="listControlBarAction" href="<tags:actionURL actionId="lava.crms.enrollment.attachments.projectEnrollmentAttachments"/>">Enrollment PDFs</a> | 
	<a class="listControlBarAction"  href="<tags:actionURL actionId="lava.crms.assessment.attachments.projectAssessmentAttachments"/>">Assessment PDFs</a>
</content>

<content tag="listColumns">
<tags:listRow>

<tags:componentListColumnHeader component="${component}" label="Action" width="12%"/>
<tags:componentListColumnHeader component="${component}" label="Patient" width="20%" sort="patient.fullNameRevNoSuffix"/>
<tags:componentListColumnHeader component="${component}" label="Type" width="18%" sort="contentType"/>
<tags:componentListColumnHeader component="${component}" label="Association" width="30%"/>
<tags:componentListColumnHeader component="${component}" label="File Status" width="20%" sort="fileStatusDate"/>

</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLButton buttonImage="view" actionId="lava.crms.people.attachments.patientAttachment" eventId="patientAttachment__view" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="edit" actionId="lava.crms.people.attachments.patientAttachment" eventId="patientAttachment__edit" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="delete" actionId="lava.crms.people.attachments.patientAttachment" eventId="patientAttachment__delete" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="download" actionId="lava.crms.people.attachments.patientAttachment" eventId="patientAttachment__download" idParam="${item.id}"/>	    
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="patient.fullNameRevNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="crmsFile"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="contentType" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/><br/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="associationBlock" component="${component}" listIndex="${iterator.index}" entityType="crmsFile"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="statusBlock" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/>
		</tags:listCell>
		
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

