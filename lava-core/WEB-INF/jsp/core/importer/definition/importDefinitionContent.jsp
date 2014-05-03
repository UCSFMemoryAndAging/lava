<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">template</page:param>
  <page:param name="sectionNameKey">importTemplate.basicConfig.section</page:param>
	<tags:createField property="name" component="${component}"/>
	<tags:createField property="mappingFileInput" component="${component}"/>   <%-- MultipartFile upload control --%>
<%-- TODO: button to download the mapping file 
   and prob some info text about uploading / downloading mapping file --%>	
	<tags:createField property="notes" component="${component}"/>
</page:applyDecorator>
