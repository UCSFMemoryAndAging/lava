<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="importLog"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<%-- have to convert the java.sql.Timestamp directly from command component because PropertyEditor has
	been registered by LavaComponentFormAction to convert to String --%> 
<fmt:formatDate value="${command.components['importLog'].importTimestamp}" type="both" 
		pattern="MM/dd/yyyy hh:mm a" var="importTimestampString"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="pageHeadingArgs">${importTimestampString},<tags:componentProperty component="${component}" property="dataFile" property2="name"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
 
		<c:import url="/WEB-INF/jsp/crms/importer/log/crmsImportLogContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>

	</page:applyDecorator>    
</page:applyDecorator>	    

