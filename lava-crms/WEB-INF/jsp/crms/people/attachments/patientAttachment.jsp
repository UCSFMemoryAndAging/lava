<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="patientAttachment"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="hasFileUpload">true</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/></page:param>
  
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
		<c:import url="/WEB-INF/jsp/crms/attachments/attachmentContent.jsp">
				<c:param name="component">${component}</c:param>
		</c:import>
</page:applyDecorator>
	
</page:applyDecorator>    

