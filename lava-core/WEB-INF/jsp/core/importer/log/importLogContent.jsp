<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">log</page:param>
  <page:param name="sectionNameKey">importLog.log.section</page:param>
	<tags:createField property="importTimestamp" component="${component}"/>
	<tags:createField property="importedBy" component="${component}"/>
	<tags:createField property="filename" component="${component}"/>
	<tags:createField property="templateName" component="${component}"/>
</page:applyDecorator>
