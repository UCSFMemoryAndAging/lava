<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">

<a href="<tags:actionURL actionId="lava.crms.people.attachments.patientAttachments" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.attachments.patientAttachments.list"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.people.attachments.patientAttachment" startMode="add"/>">
<spring:message code="action.lava.crms.people.attachments.patientAttachment.add"/></a><br>

</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.people.attachments.projectPatientAttachments" />">
<spring:message code="action.lava.crms.people.attachments.projectPatientAttachments.list"/></a><br>


</body>
</html>