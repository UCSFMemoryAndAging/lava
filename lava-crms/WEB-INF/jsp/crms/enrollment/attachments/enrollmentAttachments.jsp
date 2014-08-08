<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="enrollmentAttachments"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	
	<%-- enrollmentAttachmentsContent.jsp shared with entity specific attachments list, but that list
		differs in the Add (adding to a specific EnrollmentStatus by id) and does not have quick filters.
		so the Add actionURLButton is not part of that include --%>
	
	<content tag="customActions">
		<tags:actionURLButton buttonText="Add" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__add"  component="${component}" parameters="patientId,${currentPatient.id},param,entityList"/>	    
	</content> 

	<content tag="listQuickFilter">
		<a class="listControlBarAction" href="<tags:actionURL actionId="lava.crms.people.attachments.patientAttachments"/>">All Attachments</a> | 
		<a class="listControlBarActionCurrent" href="<tags:actionURL actionId="lava.crms.enrollment.attachments.enrollmentAttachments"/>">Enrollment Attachments</a> | 
		<a class="listControlBarAction"  href="<tags:actionURL actionId="lava.crms.assessment.attachments.assessmentAttachments"/>">Assessment Attachments</a>
	</content>

	<c:import url="/WEB-INF/jsp/crms/enrollment/attachments/enrollmentAttachmentsContent.jsp">
		<c:param name="component">${component}</c:param>
		<c:param name="parentView">entityList</c:param>
	</c:import>

</page:applyDecorator>    

</page:applyDecorator>    


