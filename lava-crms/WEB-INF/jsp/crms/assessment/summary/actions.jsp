<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<a href="<tags:actionURL actionId="lava.crms.assessment.summary.patientInstruments"/>">
<spring:message code="action.lava.crms.assessment.summary.patientInstruments.list"/></a><br>
<!-- broken
<a href="<tags:actionURL actionId="lava.crms.assessment.instrument.instrument" startMode="add"/>">
<spring:message code="action.lava.crms.assessment.instrument.instrument.add"/></a><br>
-->
</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.assessment.summary.projectInstruments"/>">
<spring:message code="action.lava.crms.assessment.summary.projectInstruments.list"/></a><br>

</body>
</html>