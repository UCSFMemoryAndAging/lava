<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>

<!-- REMOVE the following tag if there are any report links, so 
that the "Reports" heading shows in left nav -->


<!--  patient reports -->
<c:if test="${not empty currentPatient}">


</c:if>

<!--  project reports -->
<a href="<tags:actionURL actionId="lava.crms.reporting.reports.reportLauncher" parameters="param,researchVisitListByDate" flowExecutionKey="${flowExecutionKey}" eventId="reportLauncher__view"/>">
<spring:message code="action.lava.crms.reporting.reports.researchVisitListByDate.report"/></a><br/>
<a href="<tags:actionURL actionId="lava.crms.reporting.reports.reportLauncher" parameters="param,researchVisitListByPatient" flowExecutionKey="${flowExecutionKey}" eventId="reportLauncher__view"/>">
<spring:message code="action.lava.crms.reporting.reports.researchVisitListByPatient.report"/></a><br/>

</body>
</html>
