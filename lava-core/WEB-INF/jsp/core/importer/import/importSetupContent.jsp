<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<%-- TODO: use the flow state to determine whether to display import setup (with an Import button)
or import results (with a Close button) --%>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">setup</page:param>
  <page:param name="sectionNameKey">importLog.log.section</page:param>
	<tags:createField property="templateName" component="${component}"/>
	<tags:createField property="dataFile" component="${component}"/>  <%-- MultipartFile upload control --%>
	<tags:createField property="notes" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">result</page:param>
  <page:param name="sectionNameKey">importLog.result.section</page:param>
</page:applyDecorator>

<%-- TODO: list of allImportLogs as secondary list, ala visits on Add Visit --%>