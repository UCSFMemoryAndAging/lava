<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="importLog"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="importTimestamp"/>,<tags:componentProperty component="${component}" property="dataFile" property2="name"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
 
		<c:import url="/WEB-INF/jsp/core/importer/log/importLogContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
	
	</page:applyDecorator>    
</page:applyDecorator>	    

