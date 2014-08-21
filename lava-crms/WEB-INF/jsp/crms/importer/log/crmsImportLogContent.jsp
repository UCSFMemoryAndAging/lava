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
  	<tags:createField property="dataFile.name" component="${component}" metadataName="importLog.dataFilename"/>
<%-- TODO: download data file button --%>
	<tags:createField property="projName" component="${component}"/>
	<tags:createField property="definitionName" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">logResults</page:param>
  <page:param name="sectionNameKey">importLog.results.section</page:param>
	<tags:createField property="totalRecords" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="imported" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="updated" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="alreadyExist" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="errors" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="warnings" component="${component}" labelStyle="longLeft"/>
	
	<tags:createField property="newPatients" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="existingPatients" component="${component}" labelStyle="longLeft"/>

	<tags:createField property="newContactInfo" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="existingContactInfo" component="${component}" labelStyle="longLeft"/>
	
	<tags:createField property="newCaregivers" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="existingCaregivers" component="${component}" labelStyle="longLeft"/>

	<tags:createField property="newCaregiversContactInfo" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="existingCaregiversContactInfo" component="${component}" labelStyle="longLeft"/>

	<tags:createField property="newEnrollmentStatuses" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="existingEnrollmentStatuses" component="${component}" labelStyle="longLeft"/>
	
	<tags:createField property="newVisits" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="existingVisits" component="${component}" labelStyle="longLeft"/>
	
	<tags:createField property="newInstruments" component="${component}" labelStyle="longLeft"/>
<%-- TODO: list instrType(s) created. can reference importLog.importDefinition --%>	
<%--   info text that instruments are handled as a group in terms of new/exisitng,etc. --%>	
	<tags:createField property="existingInstruments" component="${component}" labelStyle="longLeft"/>
<%-- TODO: info text to clarify this number vs. prior (the following is essentially an update) --%>	
	<tags:createField property="existingInstrumentsWithData" component="${component}" labelStyle="longLeft"/>
<%-- decide if notes is needed. must be part of the importSetup that gets transferred over?? --%>	
	<tags:createField property="notes" component="${component}" labelStyle="longLeft"/>
</page:applyDecorator>


<c:import url="/WEB-INF/jsp/core/importer/log/importLogDetailContent.jsp">
	<c:param name="component">${component}</c:param>
	<c:param name="pageName">${component}</c:param>
</c:import>


