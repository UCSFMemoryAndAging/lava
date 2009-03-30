<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<a href="<tags:actionURL actionId="lava.crms.people.contactInfo.contactInfo" startMode="add" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.contactInfo.contactInfo.add"/></a><br>
<a href="<tags:actionURL actionId="lava.crms.people.contactInfo.patientContactInfo" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.contactInfo.patientContactInfo.list"/></a><br>
</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.people.contactInfo.projectContactInfo"/>">
<spring:message code="action.lava.crms.people.contactInfo.projectContactInfo.list"/></a><br>
</body>
</html>