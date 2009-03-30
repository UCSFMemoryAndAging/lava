<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<div id="patientContext">
<div id="modalPatientContextLabel">
	Current Patient 
</div>
<form name="patientContext" method="post" action="${requestUrl}">
		<tags:createField property="patientSearch" component="patientContext" mode="vw"
			fieldStyle="patientContextField" labelAlignment="none" labelStyle="modalPatientContext" dataStyle="patientContext"/>
</form>
</div>
