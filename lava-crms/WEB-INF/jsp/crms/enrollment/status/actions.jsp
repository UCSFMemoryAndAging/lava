<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">

<a href="<tags:actionURL actionId="lava.crms.enrollment.status.addEnrollmentStatus" startMode="add" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.enrollment.status.addEnrollmentStatus.add"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.enrollment.status.patientEnrollmentStatus" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.enrollment.status.patientEnrollmentStatus.list"/></a><br>
</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.people.patient.addPatient" startMode="add"/>">
<spring:message code="action.lava.crms.people.patient.addPatient"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.enrollment.status.projectEnrollmentStatus" />">
<spring:message code="action.lava.crms.enrollment.status.projectEnrollmentStatus.list"/></a><br>

</body>
</html>