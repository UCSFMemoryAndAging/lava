<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<div id="projectContext">
<form  name="projectContext" method="post" action="${requestUrl}">
		
	<div id="modalProjectContextLabel">
	Project Filter
	</div>
	
	<tags:createField property="projectName" component="projectContext"  mode="vw"
	  fieldStyle="projectContextField" labelAlignment="none" labelStyle="modalProjectContext" dataStyle="projectContext"/>
</form>  
</div>

