<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->

<a href="<tags:actionURL actionId="lava.crms.people.patient.addPatient" startMode="add"/>">
<spring:message code="action.lava.crms.people.patient.addPatient"/></a><br>


<!--  project actions -->

<a href="<tags:actionURL actionId="lava.crms.people.patient.projectPatients"/>">
<spring:message code="action.lava.crms.people.patient.projectPatients.list"/></a>

</body>
</html>