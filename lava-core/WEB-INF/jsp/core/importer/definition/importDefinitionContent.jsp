<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">definition</page:param>
  <page:param name="sectionNameKey">importDefinition.basicConfig.section</page:param>
	<tags:createField property="name" component="${component}"/>
	<tags:fileUpload paramName="uploadFile"  component="${component}"/>
<%-- TODO: button to download the mapping file 
   and prob some info text about uploading / downloading mapping file --%>	
	<tags:createField property="notes" component="${component}"/>
</page:applyDecorator>
