<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient reports -->
<c:if test="${not empty currentPatient}">


</c:if>

<!--  project reports -->
<a href="<tags:actionURL actionId="lava.crms.reporting.reports.reportLauncher" parameters="param,projectPatientStatus" flowExecutionKey="${flowExecutionKey}" eventId="reportLauncher__view"/>">
<spring:message code="action.lava.crms.reporting.reports.projectPatientStatus.report"/></a><br>
</body>
</html>
