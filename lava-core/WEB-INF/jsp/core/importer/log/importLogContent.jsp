<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<%-- this is included by both the import action (for results of an import execution) and the
importLog action for just reviewing import logs.

when used by the import action note the primary component is 'import' but the log data is in
'importLog' component

This content is only displayed in view mode, i.e. there is no concept of editing log content  --%>

<c:set var="pageName">${param.component}</c:set>
<c:set var="component" value="importLog"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">logInfo</page:param>
  <page:param name="sectionNameKey">importLog.info.section</page:param>
	<tags:createField property="importTimestamp" component="${component}"/>
	<tags:createField property="importedBy" component="${component}"/>
  	<tags:createField property="dataFile.name" component="${component}"/>
<%-- TODO: download data file button --%>
	<tags:createField property="definition.name" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">logResults</page:param>
  <page:param name="sectionNameKey">importLog.results.section</page:param>
	<tags:createField property="totalRecords" component="${component}"/>
	<tags:createField property="imported" component="${component}"/>
	<tags:createField property="updated" component="${component}"/>
	<tags:createField property="alreadyExist" component="${component}"/>
	<tags:createField property="errors" component="${component}"/>
	<tags:createField property="warnings" component="${component}"/>
<%-- decide if notes is needed. must be part of the importSetup that gets transferred over?? --%>	
	<tags:createField property="notes" component="${component}"/>
</page:applyDecorator>


<c:import url="/WEB-INF/jsp/core/importer/log/importLogDetailContent.jsp">
	<c:param name="component">${component}</c:param>
	<c:param name="pageName">${component}</c:param>
</c:import>


