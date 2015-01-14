<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->

<c:if test="${not empty currentPatient}">

<a href="<tags:actionURL actionId="lava.crms.people.patient.patient" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.patient.patient.view"/></a><br>

</c:if>

<!--  project actions -->

<a href="<tags:actionURL actionId="lava.crms.people.patient.projectPatients"/>">
<spring:message code="action.lava.crms.people.patient.projectPatients.list"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.people.family.projectFamilies"/>">
<spring:message code="action.lava.crms.people.patient.projectFamilies.list"/></a>

</body>
</html>