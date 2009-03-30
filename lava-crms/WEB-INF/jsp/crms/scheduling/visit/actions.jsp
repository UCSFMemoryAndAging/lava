<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>


<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<a href="<tags:actionURL actionId="lava.crms.scheduling.visit.visit" startMode="add"/>">
<spring:message code="action.lava.crms.scheduling.visit.visit.add"/></a><br>
</c:if>

<!--  visit actions -->
<c:if test="${not empty currentVisit}">
<a href="<tags:actionURL actionId="lava.crms.scheduling.visit.visit" idParam="${currentVisit.id}"/>">
<spring:message code="action.lava.crms.scheduling.visit.visit.view"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.scheduling.visit.visit" startMode="delete" idParam="${currentVisit.id}"/>">
<spring:message code="action.lava.crms.scheduling.visit.visit.delete"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.scheduling.visit.visitInstruments" idParam="${currentVisit.id}"/>">
<spring:message code="action.lava.crms.scheduling.visit.visitInstruments.list"/></a><br>

<%-- 
<a href="<tags:actionURL actionId="lava.crms.assessment.instrument.instrument" startMode="add"/>">
<spring:message code="action.lava.crms.assessment.instrument.instrument.add"/></a><br>
--%>
</c:if>

<!--  project actions -->
<c:if test="${not empty currentProject}">
</c:if>
</body>
</html>