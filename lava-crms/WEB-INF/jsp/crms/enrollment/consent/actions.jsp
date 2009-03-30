<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<a href="<tags:actionURL actionId="lava.crms.enrollment.consent.consent" startMode="add"/>">
<spring:message code="action.lava.crms.enrollment.consent.consent.add"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.enrollment.consent.patientConsents" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.enrollment.consent.patientConsents.list"/></a><br>
</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.enrollment.consent.projectConsents" />">
<spring:message code="action.lava.crms.enrollment.consent.projectConsents.list"/></a><br>

</body>
</html>