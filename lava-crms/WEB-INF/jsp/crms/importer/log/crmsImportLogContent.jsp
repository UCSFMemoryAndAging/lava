<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- this is included by the importLog action for just reviewing import logs.

This content is only displayed in view mode, i.e. there is no concept of editing log content  --%>

<c:set var="pageName">${param.component}</c:set>
<c:set var="component" value="importLog"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
<c:if test="${componentView == 'edit'}">
	<tags:outputText textKey="importLog.notesEditable" styleClass="bold" inline="false"/>
</c:if>	
</page:applyDecorator>


<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">logInfo</page:param>
  <page:param name="sectionNameKey">importLog.info.section</page:param>
	<tags:createField property="importTimestamp" component="${component}"/>
	<tags:createField property="importedBy" component="${component}"/>
	<%-- TODO: because of the horizontal alignment issue where value is slightly lower than label, and
		because of the following div required (unless it is the last field), redo the inline concept to 
		pass parameters to createField for the action and create action button within createField --%>
  	<tags:createField property="dataFile.name" component="${component}" inline="true"/>
	<%-- the handler is written to handle download event as meaning to download the data file, so do not need to identify what property to download --%>
	<tags:listActionURLButton buttonImage="download" actionId="lava.core.importer.log.importLog" eventId="importLog__download" parameters="param,importLog"/>
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

	<div class="verticalSpace30">&nbsp;</div>
	<tags:outputText textKey="importLog.successfulRecordsMetrics" styleClass="bold" inline="false"/>
	
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

	<%-- as of multiple instrument data records, no longer makes sense to report these as one import
		record could have multiple new or exisitng counts so the numbers do not mean much to the 
		user across all import records. they would make more sense per import record, but already
		have importLogMessage for each instrument within a muliple instrument data record
	<tags:outputText textKey="importLog.multipleInstrumentsInfo" styleClass="italic" inline="false"/>
	<tags:createField property="newInstruments" component="${component}" labelStyle="longLeft"/>
	<tags:createField property="existingInstruments" component="${component}" labelStyle="longLeft"/>
	--%>

	<%-- this will only be tracked in "update" mode so until that is supported, do not bother displaying.
		and even then, seems like this would be redundant with the global updated count
	<tags:createField property="existingInstrumentsWithData" component="${component}" labelStyle="longLeft"/>
	 --%>

	<tags:createField property="notes" component="${component}" labelStyle="longLeft"/>
</page:applyDecorator>


<c:import url="/WEB-INF/jsp/core/importer/log/importLogDetailContent.jsp">
	<c:param name="pageName">${pageName}</c:param>
</c:import>


