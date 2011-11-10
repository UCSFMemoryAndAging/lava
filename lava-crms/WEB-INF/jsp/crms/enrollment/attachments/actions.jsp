<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">

<a href="<tags:actionURL actionId="lava.crms.enrollment.attachments.enrollmentAttachments" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.enrollment.attachments.enrollmentAttachments.list"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.enrollment.attachments.enrollmentAttachment" startMode="add"/>">
<spring:message code="action.lava.crms.enrollment.attachments.enrollmentAttachment.add"/></a><br>



</c:if>

<!--  project actions -->
 <a href="<tags:actionURL actionId="lava.crms.enrollment.attachments.projectEnrollmentAttachments" />">
<spring:message code="action.lava.crms.enrollment.attachments.projectEnrollmentAttachments.list"/></a><br>


</body>
</html>