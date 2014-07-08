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
  			<tags:createField property="patientExistRule" component="${component}"/>
  			<%-- currently not allow existing Patient properties to be overwritten as that is not the
  				mission of the import functionality
  			<tags:createField property="allowPatientUpdate" component="${component}"/>
  			  --%>
  			<tags:createField property="projNameForContext" component="${component}"/>
  			<tags:createField property="esExistRule" component="${component}"/>
  			<%-- currently not allow existing EnrollmentStatus properties to be overwritten as that 
  				is not the 	mission of the import functionality
  			<tags:createField property="allowEsUpdate" component="${component}"/>
  			  --%>
  			<tags:createField property="esStatus" component="${component}"/>
  			<tags:createField property="visitExistRule" component="${component}"/>
  			<%-- currently not allow existing Visit properties to be overwritten as that is not the
  				mission of the import functionality
  			<tags:createField property="allowVisitUpdate" component="${component}"/>
  			  --%>
  			<tags:createField property="visitType" component="${component}"/>
  			<tags:createField property="visitWith" component="${component}"/>
  			<tags:createField property="visitLoc" component="${component}"/>
  			<tags:createField property="visitStatus" component="${component}"/>
  			<tags:createField property="instrExistRule" component="${component}"/>
  			<tags:createField property="allowInstrUpdate" component="${component}"/>
  			<tags:createField property="instrType" component="${component}"/>
  			<tags:createField property="instrVer" component="${component}"/>
  			<tags:createField property="instrDcStatus" component="${component}"/>
 		</page:applyDecorator>
	
		<ui:formGuide observeAndOr="or" ignoreDoOnLoad="true" simulateEvents="true">
	    	<ui:observe elementIds="importDefinition_projNameForContext" forValue=".+"/>
	    	<ui:setValue elementIds="importDefinition_esStatus" value=""/>
	    	<ui:setValue elementIds="importDefinition_visitType" value=""/>
	    	<ui:setValue elementIds="importDefinition_visitWith" value=""/>
	    	<ui:setValue elementIds="importDefinition_visitLoc" value=""/>
	    	<ui:submitForm form="importDefinition" event="importDefinition__reRender"/>
		</ui:formGuide>
	
	</page:applyDecorator>    
</page:applyDecorator>	    

