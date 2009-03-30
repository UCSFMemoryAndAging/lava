<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<a href="<tags:actionURL actionId="lava.crms.assessment.uds.patientUdsInstruments"/>">
<spring:message code="action.lava.crms.assessment.uds.patientUdsInstruments.list"/></a><br>
<a href="<tags:actionURL actionId="lava.crms.assessment.uds.patientUdsSubmissions"/>">
<spring:message code="action.lava.crms.assessment.uds.patientUdsSubmissions.list"/></a><br>

</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.assessment.uds.projectUdsInstruments"/>">
<spring:message code="action.lava.crms.assessment.uds.projectUdsInstruments.list"/></a><br>
<a href="<tags:actionURL actionId="lava.crms.assessment.uds.projectUdsSubmissions"/>">
<spring:message code="action.lava.crms.assessment.uds.projectUdsSubmissions.list"/></a><br>
</body>
</html>