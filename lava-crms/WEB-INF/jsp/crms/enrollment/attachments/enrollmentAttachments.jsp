<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="enrollmentAttachments"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
	<tags:actionURLButton buttonText="Add" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__add"  component="${component}" parameters="patientId,${currentPatient.id}"/>	    
</content> 

<content tag="listQuickFilter">
	<a class="listControlBarAction" href="<tags:actionURL actionId="lava.crms.people.attachments.patientAttachments"/>">All Attachments</a> | 
	<a class="listControlBarActionCurrent" href="<tags:actionURL actionId="lava.crms.enrollment.attachments.enrollmentAttachments"/>">Enrollment Attachments</a> | 
	<a class="listControlBarAction"  href="<tags:actionURL actionId="lava.crms.assessment.attachments.assessmentAttachments"/>">Assessment Attachments</a>
</content>

<content tag="listColumns">
<tags:listRow>

<tags:componentListColumnHeader component="${component}" label="Action" width="12%"/>
<tags:componentListColumnHeader component="${component}" label="Type (Source)" width="28%" sort="contentType"/>
<tags:componentListColumnHeader component="${component}" label="Association" width="35%"/>
<tags:componentListColumnHeader component="${component}" label="File Status" width="25%" sort="fileStatusDate"/>

</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLButton buttonImage="view" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__view" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="edit" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__edit" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="delete" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__delete" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="download" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__download" idParam="${item.id}"/>	    
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

