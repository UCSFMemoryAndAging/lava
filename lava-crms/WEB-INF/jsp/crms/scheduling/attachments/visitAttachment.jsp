<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="visitAttachment"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="hasFileUpload">true</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/>,<tags:componentProperty component="${component}" property="visit" property2="visitDescrip"/></page:param>
 
 <page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
		<c:import url="/WEB-INF/jsp/crms/attachments/attachmentContent.jsp">
				<c:param name="component">${component}</c:param>
		</c:import>
</page:applyDecorator>
    
</page:applyDecorator>	    

