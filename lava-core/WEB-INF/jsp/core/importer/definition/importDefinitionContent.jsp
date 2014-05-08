<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">definition</page:param>
  <page:param name="sectionNameKey">importDefinition.basicConfig.section</page:param>
	<tags:createField property="name" component="${component}"/>
	<tags:createField property="mappingFile.name" component="${component}" metadataName="lavaFile.name" mode="vw"/>
	<tags:fileUpload paramName="uploadFile"  component="${component}"/>
	<div class="verticalSpace10">&nbsp;</div>
<%-- TODO: button to download the mapping file 
   and prob some info text about uploading / downloading mapping file --%>	
	<tags:createField property="notes" component="${component}"/>
</page:applyDecorator>
