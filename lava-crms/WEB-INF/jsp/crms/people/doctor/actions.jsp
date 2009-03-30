<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<a href="<tags:actionURL actionId="lava.crms.people.doctor.doctor" startMode="add"/>">
<spring:message code="action.lava.crms.people.doctor.doctor.add"/></a><br>
<a href="<tags:actionURL actionId="lava.crms.people.doctor.patientDoctors" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.doctor.patientDoctors.list"/></a><br>
</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.people.doctor.allDoctors" />">
<spring:message code="action.lava.crms.people.doctor.allDoctors.list"/></a><br>
</body>
</html>