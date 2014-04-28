<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

 <%-- TODO: use the flow state to determine whether to display import setup (with an Import button)
or import results (with a Close button) 
<c:set var="component" value="importSetup"/>
<%--
<c:set var="component" value="importSetup"/>
--%>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="pageHeadingArgs"></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
 
 <%-- TODO: use the flow state to determine whether to display import setup (with an Import button)
or import results (with a Close button) 
--%>

 		<c:import url="/WEB-INF/jsp/core/importer/log/importSetupContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
		
		<page:applyDecorator name="component.entity.section">
			<page:param name="sectionId">crmsSetup</page:param>
			<page:param name="sectionNameKey">import.crmsSetup.section</page:param>
			<tags:createField property="projName" component="${component}"/>
		</page:applyDecorator>

<%--		
		<c:import url="/WEB-INF/jsp/core/importer/log/importResultContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
		
		here will be the CRMS results will be more or less the contents of CrmsImportLog, so maybe the
		importResult command object will actually be importLog
		
 --%>		
	
	</page:applyDecorator>    
</page:applyDecorator>	    

<%-- TODO: list of allCrmsImportLogs as secondary list, ala visits on Add Visit --%>
