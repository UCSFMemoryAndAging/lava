<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="importDefinition"/>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="hasFileUpload">true</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="name"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>

		<page:applyDecorator name="component.entity.section">
		  <page:param name="sectionId">definition</page:param>
		  <page:param name="sectionNameKey">importDefinition.basicConfig.section</page:param>
			<tags:createField property="name" component="${component}"/>
			<tags:outputText textKey="importDefinition.mappingFileFormat" inline="false" styleClass="italic"/>
			<tags:createField property="mappingFile.name" component="${component}" inline="true" metadataName="importDefinition.mappingFilename"/>
			<c:choose>
				<c:when test="${componentView == 'view'}">
					<tags:listActionURLButton buttonImage="download" actionId="lava.core.importer.definition.importDefinition" eventId="importDefinition__download" />
				</c:when>
				<c:when test="${componentView == 'edit'}">
					<tags:outputText textKey="importDefinition.downloadMappingFile" inline="false"/>
				</c:when>
			</c:choose>					
			<%-- div required following inline createField --%>
			<div class="verticalSpace10">&nbsp;</div>
			<c:if test="${componentMode != 'vw'}">
				<tags:outputText textKey="importDefinition.reuploadMappingFile" inline="false" styleClass="italic"/>
				<tags:fileUpload paramName="uploadFile"  component="${component}"/>
			</c:if>	
			<div class="verticalSpace10">&nbsp;</div>
			<tags:createField property="dataFileFormat" component="${component}"/>
			<tags:createField property="startDataRow" component="${component}"/>
			<tags:createField property="dateFormat" component="${component}"/>
			<tags:outputText textKey="importDefinition.defaultDateFormat" inline="false" indent="true"/>
			<tags:createField property="timeFormat" component="${component}"/>
			<tags:outputText textKey="importDefinition.defaultTimeFormat" inline="false" indent="true"/>
			<tags:createField property="notes" component="${component}"/>
		</page:applyDecorator>
		 
	</page:applyDecorator>    
</page:applyDecorator>	    

