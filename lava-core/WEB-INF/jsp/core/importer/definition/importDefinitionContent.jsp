<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">definition</page:param>
  <page:param name="sectionNameKey">importDefinition.basicConfig.section</page:param>
	<tags:createField property="name" component="${component}"/>
	<tags:createField property="mappingFile.name" component="${component}" metadataName="importDefinition.mappingFilename"/>
	<c:if test="${componentMode != 'vw'}">
		<tags:fileUpload paramName="uploadFile"  component="${component}"/>
	</c:if>	
	<div class="verticalSpace10">&nbsp;</div>
<%-- TODO: button to download the mapping file 
   and prob some info text about uploading / downloading mapping file --%>
   
   <%-- TODO: createField for dataFileFormat dropdown --%>
   	
	<tags:createField property="notes" component="${component}"/>
</page:applyDecorator>
