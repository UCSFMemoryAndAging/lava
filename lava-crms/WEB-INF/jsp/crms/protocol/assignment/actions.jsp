<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>

<!--  patient actions -->
<c:if test="${not empty currentPatient}">

<c:if test="${currentPatient.locked}">
<img src="images/lock.png"  width="10" height="10" border="0" title="LOCKED"/>&nbsp;
</c:if>

<a href="<tags:actionURL actionId="lava.crms.protocol.assignment.protocol" startMode="add" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.protocol.assignment.protocol.add"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.protocol.assignment.patientProtocols" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.protocol.assignment.patientProtocols.list"/></a><br>
</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.protocol.assignment.projectProtocols" />">
<spring:message code="action.lava.crms.protocol.assignment.projectProtocols.list"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.protocol.setup.projectProtocolConfigs" />">
<spring:message code="action.lava.crms.protocol.setup.projectProtocolConfigs.list"/></a><br>

</body>
</html>