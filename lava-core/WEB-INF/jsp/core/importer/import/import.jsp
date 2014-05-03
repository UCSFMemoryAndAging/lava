<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- note that component 'imporResult' is put into the ComponentCommand map as well
as the 'import' component. this is done when an import is executed and used to display 
results, when indicated by the flowState value. but still want 'import' as 
the "primary" component because this is what the handler is using as the default 
component object name so it used to set up the component mode and component view --%>
<c:set var="component" value="import"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="hasFileUpload">true</page:param>
  <page:param name="pageHeadingArgs"></page:param>
 
<page:applyDecorator name="component.import.content">
  <page:param name="component">${component}</page:param>
 
	<c:if test="${flowState == 'edit'}"> 
 		<c:import url="/WEB-INF/jsp/core/importer/import/importSetupContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
	</c:if>	
	<c:if test="${flowState == 'review'}"> 
		<c:import url="/WEB-INF/jsp/core/importer/import/importResultContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
	</c:if>	
	
	</page:applyDecorator>    
</page:applyDecorator>	    

<%-- TODO: list of allImportLogs as secondary list, ala visits on Add Visit --%>
