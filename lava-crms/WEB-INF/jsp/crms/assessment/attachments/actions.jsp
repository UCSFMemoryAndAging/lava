<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">

<a href="<tags:actionURL actionId="lava.crms.assessment.attachments.assessmentAttachments" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.assessment.attachments.assessmentAttachments.list"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.assessment.attachments.assessmentAttachment" startMode="add"/>">
<spring:message code="action.lava.crms.assessment.attachments.assessmentAttachment.add"/></a><br>



</c:if>

<!--  project actions -->
 <a href="<tags:actionURL actionId="lava.crms.assessment.attachments.projectAssessmentAttachments" />">
<spring:message code="action.lava.crms.assessment.attachments.projectAssessmentAttachments.list"/></a><br>


</body>
</html>