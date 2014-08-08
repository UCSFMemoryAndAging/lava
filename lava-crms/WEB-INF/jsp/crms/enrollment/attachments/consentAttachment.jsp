<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="consentAttachment"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="hasFileUpload">true</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="contentType"/>,<tags:componentProperty component="${component}" property="name"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="parameters">param,${parentView}</page:param>

<c:if test="${componentView == 'view'}">

<tags:ifComponentPropertyNotEmpty property="location" component="${component}">
	<content tag="customActions">
		<tags:eventButton buttonText="Download" action="download" component="${component}" pageName="${component}" className="pageLevelLeftmostButton" target="download"/>
	</content>
</tags:ifComponentPropertyNotEmpty>

</c:if>

<page:applyDecorator name="component.entity.section">
	<page:param name="sectionNameKey">attachment.linking.section</page:param>
		<tags:createField property="patient.fullNameRevNoSuffix" entityType="crmsFile" component="${component}"/>
</page:applyDecorator>  

<c:if test="${componentView != 'view' && componentView != 'delete'}">
	<tags:ifComponentPropertyEmpty property="location" component="${component}">
		<page:applyDecorator name="component.entity.section">
			<page:param name="sectionNameKey">lavaFile.upload.section</page:param>
				<tags:fileUpload paramName="uploadFile"  component="${component}"/>
		</page:applyDecorator>
	</tags:ifComponentPropertyEmpty>
</c:if>

<c:import url="/WEB-INF/jsp/crms/attachments/attachmentCommonContent.jsp">
	<c:param name="component">${component}</c:param>
</c:import>

</page:applyDecorator>    
</page:applyDecorator>	    

