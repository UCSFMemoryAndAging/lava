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
	<%-- TODO: because of the horizontal alignemnt issue where value is slightly lower than label, and
		because of the following div required (unless it is the last field), redo the inline concept to 
		pass parameters to createField for the action and create action button within createField --%>
  	<tags:createField property="dataFile.name" component="${component}" inline="true"/>
	<%-- the handler is written to handle download event as meaning to download the data file, so do not need to identify what property to download --%>
	<%-- do not show the download button in the import action as entering the import download subflow would call getBackingObject which
		initializes a new importLog because it is just designed to prep for the import. this new importLog instance does not contain dataFile --%>
	<c:if test="${fn:endsWith(pageName, 'Log')}">
		<tags:listActionURLButton buttonImage="download" actionId="lava.core.importer.log.importLog" eventId="importLog__download" parameters="param,importLog"/>
	</c:if>	
	<%-- div required following inline createField --%>
	<div class="verticalSpace10">&nbsp;</div>
	<tags:createField property="projName" component="${component}"/>
 	<tags:createField property="definition.name" component="${component}" inline="true"/>
	<c:set var="definitionId">
		<tags:componentProperty component="${component}" property="definition" property2="id"/>
	</c:set>
	<%-- using listActionURLButton for the list icon --%>	
	<tags:listActionURLButton buttonImage="view" actionId="lava.core.importer.definition.importDefinition" eventId="importDefinition__view" idParam="${definitionId}"/>
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
	<c:param name="pageName">${pageName}</c:param>
</c:import>


