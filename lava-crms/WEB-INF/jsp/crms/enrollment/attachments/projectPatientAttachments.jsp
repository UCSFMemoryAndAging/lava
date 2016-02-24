<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="projectPatientAttachments"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects /' : currentProject.name}</page:param>
	
<c:import url="/WEB-INF/jsp/crms/people/attachments/attachmentsContent.jsp">
	<c:param name="component">${component}</c:param>
</c:import>
	
</page:applyDecorator>	    

