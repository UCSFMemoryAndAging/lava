<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="importDefinition"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="hasFileUpload">true</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="name"/></page:param>
  
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
 
		<c:import url="/WEB-INF/jsp/core/importer/definition/importDefinitionContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>

		<page:applyDecorator name="component.entity.section">
  			<page:param name="sectionId">crmsConfig</page:param>
  			<page:param name="sectionNameKey">importDefinition.crmsConfig.section</page:param>
  			<tags:createField property="patientMustExist" component="${component}"/>
  			<tags:createField property="projNameForContext" component="${component}"/>
  			<tags:createField property="visitMustExist" component="${component}"/>
  			<tags:createField property="visitTypeSupplied" component="${component}"/>
  			<tags:createField property="visitType" component="${component}"/>
  			<tags:createField property="visitWithSupplied" component="${component}"/>
  			<tags:createField property="visitWith" component="${component}"/>
  			<tags:createField property="visitLocSupplied" component="${component}"/>
  			<tags:createField property="visitLoc" component="${component}"/>
  			<tags:createField property="visitStatusSupplied" component="${component}"/>
  			<tags:createField property="visitStatus" component="${component}"/>
		</page:applyDecorator>
	
	</page:applyDecorator>    
</page:applyDecorator>	    

