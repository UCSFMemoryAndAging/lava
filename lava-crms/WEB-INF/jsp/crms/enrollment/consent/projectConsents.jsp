<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="projectConsents"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects /' : currentProject.name}</page:param>

	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

	<c:import url="/WEB-INF/jsp/crms/enrollment/consent/projectConsentsContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>

</page:applyDecorator>    

</page:applyDecorator>    
	    
