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

 		<c:import url="/WEB-INF/jsp/core/importer/import/importSetupContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>

<%--		
		<c:import url="/WEB-INF/jsp/core/importer/import/importResultContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
 --%>		
	
	</page:applyDecorator>    
</page:applyDecorator>	    

<%-- TODO: list of allImportLogs as secondary list, ala visits on Add Visit --%>
