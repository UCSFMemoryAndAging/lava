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
 
		<page:applyDecorator name="component.entity.section">
  			<page:param name="sectionId">crmsConfig</page:param>
  			<page:param name="sectionNameKey">importDefinition.crmsConfig.section</page:param>

  			<tags:createField property="projName" component="${component}"/>

  			<tags:createField property="patientExistRule" component="${component}"/>
  			<%-- currently not allow existing Patient properties to be overwritten as that is not the
  				mission of the import functionality
  			<tags:createField property="allowPatientUpdate" component="${component}"/>
  			  --%>
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
			<%-- currently not allowing update of instrument data because of testing needed to ensure no issues
			  with database contention and examining data quality issues that may arise giving users the ability to 
			  update data in bulk this way (note that REDCap has this functionality)  			
  			<tags:createField property="allowInstrUpdate" component="${component}"/>
  			  --%>
  			<tags:createField property="instrType" component="${component}"/>
  			<tags:createField property="instrVer" component="${component}"/>
  			<tags:createField property="instrDcStatus" component="${component}"/>
 		</page:applyDecorator>
	
		<ui:formGuide observeAndOr="or" ignoreDoOnLoad="true" simulateEvents="true">
	    	<ui:observe elementIds="importDefinition_projName" forValue=".+"/>
	    	<ui:setValue elementIds="importDefinition_esStatus" value=""/>
	    	<ui:setValue elementIds="importDefinition_visitType" value=""/>
	    	<ui:setValue elementIds="importDefinition_visitWith" value=""/>
	    	<ui:setValue elementIds="importDefinition_visitLoc" value=""/>
	    	<ui:submitForm form="importDefinition" event="importDefinition__reRender"/>
		</ui:formGuide>

		<%-- currently putting this last because when projName is selected the view is rerendered to populate
			project dependent dropdowns and because the current implementation of uploading files is such that 
			file is not using Spring bind to bind the file to the command object, the selected file gets lost
			on this rerender and user has to Browse and select it again. The plan is to refactor so that the
			file to upload uses spring bind to bind to a file property on the command object--%>
		<c:import url="/WEB-INF/jsp/core/importer/definition/importDefinitionContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>

	</page:applyDecorator>    
</page:applyDecorator>	    

