<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- although the target of the action is 'crmsImport' must use 'import' as the component because this
action customizes the core action whose target is 'import' (see BaseFlowBuilder getBaseActionId) --%>
<c:set var="component" value="import"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<%-- set up vars for page title --%>
<c:if test="${flowState == 'result'}"> 
	<c:set var="importDataFile">
		<tags:componentProperty component="importLog" property="dataFile" property2="name"/>
	</c:set>
</c:if>
<c:if test="${flowState == 'result'}">
	<%-- have to convert the java.sql.Timestamp directly from command component because PropertyEditor has
		been registered by LavaComponentFormAction to convert to String --%> 
	<fmt:formatDate value="${command.components['importLog'].importTimestamp}" type="both" 
		pattern="MM/dd/yyyy hh:mm a" var="importTimestampString"/>
</c:if>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="forceMain">true</page:param> 
  <page:param name="hasFileUpload">true</page:param>
  <page:param name="pageHeadingArgs">${importDataFile},${importTimestamp}</page:param>

<page:applyDecorator name="component.import.content">
  <page:param name="component">${component}</page:param>
 
	<c:if test="${flowState == 'edit'}"> 
 		<c:import url="/WEB-INF/jsp/core/importer/import/importSetupContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
		
		<page:applyDecorator name="component.entity.section">
			<page:param name="sectionId">crmsSetup</page:param>
			<page:param name="sectionNameKey">import.crmsSetup.section</page:param>
			<tags:createField property="projName" component="${component}"/>
		</page:applyDecorator>
	</c:if>
	
	<c:if test="${flowState == 'result'}"> 
		<%-- import log displays overall import results and individual import record errors and warnings 
			note that components 'importLog' and 'importLogMessages' are put into the ComponentCommand map as well
			as the 'import' component. this is done when an import is executed and used to display 
			results from the log. 'import' is the "primary" component because this is what the handler 
			is using as the default component object name so it used to set up the component mode and 
			component view --%>
		<c:import url="/WEB-INF/jsp/crms/importer/log/crmsImportLogContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
	</c:if>	
	
	</page:applyDecorator>    
</page:applyDecorator>	    

<%-- TODO: list of allCrmsImportLogs as secondary list, ala visits on Add Visit --%>
