<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<a href="<tags:actionURL actionId="lava.crms.people.caregiver.caregiver" startMode="add" />">
<spring:message code="action.lava.crms.people.caregiver.caregiver.add"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.people.caregiver.patientCaregivers" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.caregiver.patientCaregivers.list"/></a><br>
</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.people.caregiver.projectCaregivers" />">
<spring:message code="action.lava.crms.people.caregiver.projectCaregivers.list"/></a><br>
	
</body>
</html>