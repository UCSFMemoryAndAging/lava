<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<%-- set mode variable for fields within listCell tags which do not need labels because the table row
and column headers make a label unnecessary. --%>
<c:set var="controlMode" value="lv"/>
<c:choose>
	<c:when test="${componentView == 'edit' || componentView == 'add'}">
		<c:set var="controlMode" value="le"/>
	</c:when>
</c:choose>

<%-- note that because this jsp content could be included by different actions, e.g. an application instance
action for customization of Import Definitions, and want to share a single set of metadata, so passing
entity="importDefinition" to every createField so it will share the same metadata regardless of the value
of component --%>

<c:set var="mappingFile">
	<tags:componentProperty component="${component}" property="mappingFile" property2="fileId"/>
</c:set>		
<c:if test="${componentView != 'add'}">
<c:if test="${empty mappingFile}">
  	<img class="errorIcon" src="images/error.png" alt="error"/>
	<tags:outputText textKey="importDefinition.noMappingFile" inline="true" styleClass="errorListItem"/>
</c:if>
</c:if>

<page:applyDecorator name="component.entity.section">
	<page:param name="sectionId">definition</page:param>
	<page:param name="sectionNameKey">importDefinition.basicConfig.section</page:param>
		<tags:createField property="name" component="${component}" entity="importDefinition"/>
		<tags:createField property="created" component="${component}" entity="importDefinition"/>
		<tags:createField property="createdBy" component="${component}" entity="importDefinition"/>
		<tags:createField property="notes" component="${component}" entity="importDefinition"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
	<page:param name="sectionId">crmsConfig</page:param>
	<page:param name="sectionNameKey">importDefinition.crmsConfig.section</page:param>
	<page:param name="instructions"><spring:message code="importDefinition.crmsInstructions"/></page:param>

	<div class="verticalSpace10">&nbsp;</div>
	<tags:outputText textKey="importDefinition.project.usage" styleClass="italic" inline="false"/>
	<tags:createField property="projName" component="${component}" entity="importDefinition"/>
	<div class="verticalSpace30">
		&nbsp;
	</div>	
	<tags:tableForm>
		<tags:listRow>
			<tags:listColumnHeader labelKey="" width="10%"/>
			<tags:listColumnHeader label="Settings" width="30%"/>
			<tags:listColumnHeader label="Exists Rule" width="25%"/>
			<tags:listColumnHeader label="Defaults if Import Creates Entity" width="35%"/>
		</tags:listRow>

		<tags:listRow>
			<tags:listCell/>
			<tags:listCell/>
			<tags:listCell>
				<tags:outputText textKey="importDefinition.existsRules" styleClass="italic" inline="false"/>
				<tags:outputText textKey="importDefinition.existsRulesImpact" styleClass="italic" inline="false"/>
			</tags:listCell>
			<tags:listCell><tags:outputText textKey="importDefinition.defaults" styleClass="italic" inline="false"/></tags:listCell>
		</tags:listRow>
		
		<tags:listRowGroup>
 				<tags:listRow>
				<tags:listCell>Patient</tags:listCell>
				<tags:listCell>
					<tags:outputText textKey="importDefinition.patient.patientOnly" styleClass="italic" inline="false"/>
					<tags:createField property="patientOnlyImport" component="${component}" entity="importDefinition"/>
				</tags:listCell>
	  			<%-- currently not allow existing Patient properties to be overwritten as that is not the
 						mission of the import functionality
 					<tags:createField property="allowPatientUpdate" component="${component}"/>
 			  		--%>
				<tags:listCell><tags:createField property="patientExistRule" component="${component}" entity="importDefinition" mode="${controlMode}"/></tags:listCell>
				<tags:listCell/>
			</tags:listRow>
		</tags:listRowGroup>
		
		<tags:listRowGroup>
 				<tags:listRow>
				<tags:listCell>Enrollment Status</tags:listCell>
				<tags:listCell></tags:listCell>
	  			<%-- currently not allow existing EnrollmentStatus properties to be overwritten as that 
 						is not the 	mission of the import functionality
	  			<tags:createField property="allowEsUpdate" component="${component}"/>
                   --%>
				<tags:listCell><tags:createField property="esExistRule" component="${component}" entity="importDefinition" mode="${controlMode}"/></tags:listCell>
				<tags:listCell labelSize="medium"><tags:createField property="esStatus" component="${component}" entity="importDefinition"/></tags:listCell>
			</tags:listRow>
		</tags:listRowGroup>
		
		<tags:listRowGroup>
 				<tags:listRow height="100">
				<tags:listCell>Visit</tags:listCell>
				<tags:listCell labelSize="medium">
					<tags:outputText textKey="importDefinition.visit.visitWindow" styleClass="italic" inline="false"/>
					<tags:createField property="visitWindow" component="${component}" entity="importDefinition"/><br/>
					<tags:ifHasRole roles="SYSTEM ADMIN">  <%-- only allow the SYSTEM ADMIN to turn on the visit project match (for now) --%> 
						<div class="verticalSpace30">&nbsp;</div>
						<tags:outputText textKey="importDefinition.visit.matchVisitProjFlag" styleClass="italic" inline="false"/>
						<tags:createField property="matchVisitProjFlag" component="${component}" entity="importDefinition"/>
						<div class="verticalSpace30">&nbsp;</div>
					</tags:ifHasRole>
					<tags:outputText textKey="importDefinition.visit.matchVisitTimeFlag" styleClass="italic" inline="false"/>
					<tags:createField property="matchVisitTimeFlag" component="${component}" entity="importDefinition"/>
					<tags:ifHasRole roles="SYSTEM ADMIN">   <%-- only allow the SYSTEM ADMIN to turn off the visit type match --%> 
						<div class="verticalSpace30">&nbsp;</div>
						<tags:outputText textKey="importDefinition.visit.matchVisitTypeFlag" styleClass="italic" inline="false"/>
						<tags:createField property="matchVisitTypeFlag" component="${component}" entity="importDefinition"/>
						<div class="verticalSpace30">&nbsp;</div>
					</tags:ifHasRole>
					<tags:outputText textKey="importDefinition.visit.matchVisitTypeInfo" styleClass="italic" inline="false"/>
					<tags:outputText textKey="importDefinition.visit.matchVisitTypes" inline="true"/><br/>
					<tags:createField property="matchVisitType" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="matchVisitType2" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="matchVisitType3" component="${component}" entity="importDefinition" mode="${controlMode}"/>
				</tags:listCell>
	  			<%-- currently not allow existing Visit properties to be overwritten as that is not the
	  			 	mission of the import functionality
	  			<tags:createField property="allowVisitUpdate" component="${component}"/>
                   --%>
				<tags:listCell><tags:createField property="visitExistRule" component="${component}" entity="importDefinition" mode="${controlMode}"/></tags:listCell>
				<tags:listCell labelSize="medium">
					<tags:outputText textKey="importDefinition.visit.visitTypeMatched" styleClass="italic" inline="false"/>
					<tags:outputText textKey="importDefinition.visit.newVisit" styleClass="italic" inline="false"/>
					<tags:createField property="visitType" component="${component}" entity="importDefinition"/>
					<div class="verticalSpace10">&nbsp;</div>
		  			<tags:createField property="visitWith" component="${component}" entity="importDefinition"/>
					<div class="verticalSpace10">&nbsp;</div>
		  			<tags:createField property="visitLoc" component="${component}" entity="importDefinition"/>
					<div class="verticalSpace10">&nbsp;</div>
		  			<tags:createField property="visitStatus" component="${component}" entity="importDefinition"/>
				</tags:listCell>
			</tags:listRow>
		</tags:listRowGroup>
		
		<tags:listRowGroup>
 				<tags:listRow>
				<tags:listCell>Instrument</tags:listCell>
				<tags:listCell>
					<tags:outputText textKey="importDefinition.instr.multipleInstruments" styleClass="italic" inline="false"/>
					<tags:outputText text="Instrument #1" inline="true"/><br/>
					<tags:createField property="instrType" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate" component="${component}" entity="importDefinition"/><br/>   --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #2" inline="true"/><br/>
					<tags:createField property="instrType2" component="${component}" entity="importDefinition" mode="${controlMode}"/>
 					<tags:createField property="instrVer2" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias2" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
 					<tags:createField property="instrCaregiver2" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate2" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #3" inline="true"/><br/>
					<tags:createField property="instrType3" component="${component}" entity="importDefinition" mode="${controlMode}"/>
 					<tags:createField property="instrVer3" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias3" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
 					<tags:createField property="instrCaregiver3" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate3" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #4" inline="true"/><br/>
					<tags:createField property="instrType4" component="${component}" entity="importDefinition" mode="${controlMode}"/>
 					<tags:createField property="instrVer4" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias4" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
 					<tags:createField property="instrCaregiver4" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate4" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #5" inline="true"/><br/>
					<tags:createField property="instrType5" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer5" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias5" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver5" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate5" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #6" inline="true"/><br/>
					<tags:createField property="instrType6" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer6" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias6" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver6" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate6" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #7" inline="true"/><br/>
					<tags:createField property="instrType7" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer7" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias7" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver7" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate7" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #8" inline="true"/><br/>
					<tags:createField property="instrType8" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer8" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias8" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver8" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate8" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #9" inline="true"/><br/>
					<tags:createField property="instrType9" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer9" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias9" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver9" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate9" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #10" inline="true"/><br/>
					<tags:createField property="instrType10" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer10" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias10" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver10" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate10" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #11" inline="true"/><br/>
					<tags:createField property="instrType11" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer11" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias11" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver11" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate11" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #12" inline="true"/><br/>
					<tags:createField property="instrType12" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer12" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias12" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver12" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate12" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #13" inline="true"/><br/>
					<tags:createField property="instrType13" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer13" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias13" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver13" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate13" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #14" inline="true"/><br/>
					<tags:createField property="instrType14" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer14" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias14" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver14" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate14" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText text="Instrument #15" inline="true"/><br/>
					<tags:createField property="instrType15" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<tags:createField property="instrVer15" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:outputText textKey="importDefinition.instr.mappingAlias" inline="true"/>
					<tags:createField property="instrMappingAlias15" component="${component}" entity="importDefinition" mode="${controlMode}"/><br/>
					<tags:createField property="instrCaregiver15" component="${component}" entity="importDefinition"/><br/>
					<%-- <tags:createField property="instrCalculate15" component="${component}" entity="importDefinition"/><br/> --%>
					<div class="verticalSpace30">&nbsp;</div>
					<tags:outputText text="Global Default Code" inline="true"/><br/>
					<tags:outputText textKey="importDefinition.instr.defaultCode" inline="true"/><br/>
					<tags:createField property="instrDefaultCode" component="${component}" entity="importDefinition" mode="${controlMode}"/>
				</tags:listCell>
				<tags:listCell>
					<tags:createField property="instrExistRule" component="${component}" entity="importDefinition" mode="${controlMode}"/>
					<div class="verticalSpace10">&nbsp;</div>
					<tags:outputText textKey="importDefinition.instr.existsRule" styleClass="italic" inline="false"/>
				<%-- currently not allowing update of instrument data to users because of testing needed to ensure 
				    no issues with database contention and examining data quality issues that may arise giving users 
				    the ability to update data in bulk this way (note that REDCap has this functionality). Need to 
				    implement a confirmation page in the flow that will show user existing and new values.
 			  		--%>
					<div class="verticalSpace30">&nbsp;</div>
					<tags:ifHasRole roles="SYSTEM ADMIN"> 
	  					<tags:createField property="allowInstrUpdate" component="${component}" entity="importDefinition"/>
						<div class="verticalSpace30">&nbsp;</div>
	  				</tags:ifHasRole>	
					<tags:outputText textKey="importDefinition.instr.caregiverExistsRuleLabel" inline="false"/>
					<tags:outputText textKey="importDefinition.instr.caregiverExistsRule" styleClass="italic" inline="false"/>
		  			<tags:createField property="instrCaregiverExistRule" component="${component}" entity="importDefinition" mode="${controlMode}"/>
				</tags:listCell>				  			
				<tags:listCell labelSize="medium">
		  			<tags:createField property="instrDcStatus" component="${component}" entity="importDefinition"/>
				</tags:listCell>
			</tags:listRow>
		</tags:listRowGroup>
		
	</tags:tableForm>
	
	</page:applyDecorator>

<%-- TODO: currently putting this near the end because when projName is selected the view is rerendered
    to populate project dependent dropdowns and because the current implementation of uploading files is
    such that file is not using Spring bind to bind the file to the command object, the selected file gets
    lost on this rerender and user has to Browse and select it again. The plan is to refactor so that the
	file to upload uses spring bind to bind to a file property on the command object--%>
<page:applyDecorator name="component.entity.section">
	<page:param name="sectionId">mappingFile</page:param>
	<page:param name="sectionNameKey">importDefinition.mappingFile.section</page:param>
		<tags:outputText textKey="importDefinition.mappingFileFormat" inline="false" styleClass="italic"/>
		<tags:outputText textKey="importDefinition.mappingFileFormatCrms" inline="false" styleClass="italic"/>
		<tags:createField property="mappingFile.name" component="${component}" entity="importDefinition" inline="true" metadataName="importDefinition.mappingFilename"/>
		<c:choose>
			<c:when test="${componentView == 'view'}">
			   	<%-- since this content jsp may be included by different actions with different components,
   					need to use whatever the component is for events (actionId is fine as is because that just gets
   					turned into actionUrl which on a user request is handled by whatever dynamic and/or instance 
   					customization of the action exist) --%>
				<c:if test="${not empty mappingFile}">
					<tags:listActionURLButton buttonImage="download" actionId="lava.core.importer.definition.importDefinition" eventId="${component}__download" />
				</c:if>
			</c:when>
			<c:when test="${componentView == 'edit'}">
				<tags:outputText textKey="importDefinition.downloadMappingFile" inline="false" styleClass="italic"/>
			</c:when>
		</c:choose>					
		<%-- div required following inline outputText --%>
		<div class="verticalSpace10">&nbsp;</div>
		<c:if test="${componentMode != 'vw'}">
			<tags:outputText textKey="importDefinition.reuploadMappingFile" inline="false" styleClass="italic"/>
			<tags:fileUpload paramName="uploadFile" component="${component}"/>
		</c:if>	
		<div class="verticalSpace10">&nbsp;</div>
	</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
			<page:param name="sectionId">dataFile</page:param>
	<page:param name="sectionNameKey">importDefinition.dataFile.section</page:param>
		<tags:createField property="dataFileFormat" component="${component}" entity="importDefinition"/>
		<tags:createField property="startDataRow" component="${component}" entity="importDefinition"/>
		<tags:ifHasRole roles="SYSTEM ADMIN">   <%-- only allow the SYSTEM ADMIN to turn on Allow Extreme Dates --%> 
			<tags:createField property="allowExtremeDates" component="${component}" entity="importDefinition"/>
		</tags:ifHasRole>
		<tags:outputText textKey="importDefinition.dateTimeFormatting" inline="false" styleClass="italic"/>
		<tags:createField property="dateFormat" component="${component}" entity="importDefinition"/>
		<tags:outputText textKey="importDefinition.defaultDateFormat" inline="false" indent="true" styleClass="italic"/>
		<tags:createField property="timeFormat" component="${component}" entity="importDefinition"/>
		<tags:outputText textKey="importDefinition.defaultTimeFormat" inline="false" indent="true" styleClass="italic"/>
</page:applyDecorator>


<ui:formGuide>
   	<ui:observeForNull elementIds="patientOnlyImport" component="${component}" negate="true"/>
   	<ui:setValue elementIds="visitWindow,matchVisitTypeFlag,matchVisitType,matchVisitType2,matchVisitType3,visitExistRule,visitType,visitWith,visitLoc,visitStatus" component="${component}" value=""/>
   	<ui:disable elementIds="visitWindow,matchVisitTypeFlag,matchVisitType,matchVisitType2,matchVisitType3,visitExistRule,visitType,visitWith,visitLoc,visitStatus" component="${component}"/>
   	<ui:setValue elementIds="instrType,instrMappingAlias,instrVer,instrCaregiver" component="${component}" value=""/>
   	<ui:disable elementIds="instrType,instrMappingAlias,instrVer,instrCaregiver" component="${component}"/>
   	<ui:setValue elementIds="instrType2,instrMappingAlias2,instrVer2,instrCaregiver2" component="${component}" value=""/>
   	<ui:disable elementIds="instrType2,instrMappingAlias2,instrVer2,instrCaregiver2" component="${component}"/>
   	<ui:setValue elementIds="instrType3,instrMappingAlias3,instrVer3,instrCaregiver3" component="${component}" value=""/>
   	<ui:disable elementIds="instrType3,instrMappingAlias3,instrVer3,instrCaregiver3" component="${component}"/>
   	<ui:setValue elementIds="instrType4,instrMappingAlias4,instrVer4,instrCaregiver4" component="${component}" value=""/>
   	<ui:disable elementIds="instrType4,instrMappingAlias4,instrVer4,instrCaregiver4" component="${component}"/>
   	<ui:setValue elementIds="instrType5,instrMappingAlias5,instrVer5,instrCaregiver5" component="${component}" value=""/>
   	<ui:disable elementIds="instrType5,instrMappingAlias5,instrVer5,instrCaregiver5" component="${component}"/>
   	<ui:setValue elementIds="instrType6,instrMappingAlias6,instrVer6,instrCaregiver6" component="${component}" value=""/>
   	<ui:disable elementIds="instrType6,instrMappingAlias6,instrVer6,instrCaregiver6" component="${component}"/>
   	<ui:setValue elementIds="instrType7,instrMappingAlias7,instrVer7,instrCaregiver7" component="${component}" value=""/>
   	<ui:disable elementIds="instrType7,instrMappingAlias7,instrVer7,instrCaregiver7" component="${component}"/>
   	<ui:setValue elementIds="instrType8,instrMappingAlias8,instrVer8,instrCaregiver8" component="${component}" value=""/>
   	<ui:disable elementIds="instrType8,instrMappingAlias8,instrVer8,instrCaregiver8" component="${component}"/>
   	<ui:setValue elementIds="instrType9,instrMappingAlias9,instrVer9,instrCaregiver9" component="${component}" value=""/>
   	<ui:disable elementIds="instrType9,instrMappingAlias9,instrVer9,instrCaregiver9" component="${component}"/>
   	<ui:setValue elementIds="instrType10,instrMappingAlias10,instrVer10,instrCaregiver10" component="${component}" value=""/>
   	<ui:disable elementIds="instrType10,instrMappingAlias10,instrVer10,instrCaregiver10" component="${component}"/>
   	<ui:setValue elementIds="instrType11,instrMappingAlias11,instrVer11,instrCaregiver11" component="${component}" value=""/>
   	<ui:disable elementIds="instrType11,instrMappingAlias11,instrVer11,instrCaregiver11" component="${component}"/>
   	<ui:setValue elementIds="instrType12,instrMappingAlias12,instrVer12,instrCaregiver12" component="${component}" value=""/>
   	<ui:disable elementIds="instrType12,instrMappingAlias12,instrVer12,instrCaregiver12" component="${component}"/>
   	<ui:setValue elementIds="instrType13,instrMappingAlias13,instrVer13,instrCaregiver13" component="${component}" value=""/>
   	<ui:disable elementIds="instrType13,instrMappingAlias13,instrVer13,instrCaregiver13" component="${component}"/>
   	<ui:setValue elementIds="instrType14,instrMappingAlias14,instrVer14,instrCaregiver14" component="${component}" value=""/>
   	<ui:disable elementIds="instrType14,instrMappingAlias14,instrVer14,instrCaregiver14" component="${component}"/>
   	<ui:setValue elementIds="instrType15,instrMappingAlias15,instrVer15,instrCaregiver15" component="${component}" value=""/>
   	<ui:disable elementIds="instrType15,instrMappingAlias15,instrVer15,instrCaregiver15" component="${component}"/>
   	<ui:setValue elementIds="instrDefaultCode" component="${component}" value=""/>
   	<ui:disable elementIds="instrDefaultCode" component="${component}"/>
   	<ui:setValue elementIds="instrExistRule,instrCaregiverExistRule,instrDcStatus" component="${component}" value=""/>
   	<ui:disable elementIds="instrExistRule,instrCaregiverExistRule,instrDcStatus" component="${component}"/>
</ui:formGuide>

<tags:ifHasRole roles="SYSTEM ADMIN"> 
<ui:formGuide>
   	<ui:observeForNull elementIds="patientOnlyImport" component="${component}" negate="true"/>
   	<ui:setValue elementIds="allowInstrUpdate" component="${component}" value=""/>
   	<ui:disable elementIds="allowInstrUpdate" component="${component}"/>
</ui:formGuide>
</tags:ifHasRole>	

<ui:formGuide>
	<%-- if Patient Exist Rule is Must Not Exist, then other entities cannot possibly exist so set
		their rule to Must Not Exist and disable --%>
   	<ui:observe elementIds="patientExistRule" component="${component}" forValue="3"/>
   	<%-- clear the properties that have project-dependent lists --%>
   	<ui:setValue elementIds="esExistRule" component="${component}" value="3"/>
   	<ui:disable elementIds="esExistRule" component="${component}"/>
</ui:formGuide>


<ui:formGuide>
	<%-- if Patient Exist Rule is Must Not Exist, then other entities cannot possibly exist so set
		their rule to Must Not Exist and disable --%>
   	<ui:observe elementIds="patientExistRule" component="${component}" forValue="3"/>
   	<ui:ignoreForNull elementIds="patientOnlyImport" component="${component}" negate="true"/>
   	<%-- clear the properties that have project-dependent lists --%>
   	<ui:setValue elementIds="visitExistRule,instrExistRule" component="${component}" value="3"/>
   	<ui:disable elementIds="visitExistRule,instrExistRule" component="${component}"/>
   	<%-- TODO: add allowInstrUpdate to these --%>
   	<ui:setValue elementIds="instrCaregiverExistRule" component="${component}" value="1"/>
   	<ui:disable elementIds="instrCaregiverExistRule" component="${component}"/>
</ui:formGuide>

<ui:formGuide observeAndOr="or">
   	<ui:observeForNull elementIds="instrCaregiver,instrCaregiver2,instrCaregiver3,instrCaregiver4,instrCaregiver5,
   		instrCaregiver6,instrCaregiver7,instrCaregiver8,instrCaregiver9" component="${component}" negate="true"/>
   	<ui:enable elementIds="instrCaregiverExistRule" component="${component}"/>
</ui:formGuide>
<ui:formGuide observeAndOr="and">
   	<ui:observeForNull elementIds="instrCaregiver,instrCaregiver2,instrCaregiver3,instrCaregiver4,instrCaregiver5,
   		instrCaregiver6,instrCaregiver7,instrCaregiver8,instrCaregiver9" component="${component}"/>
   	<ui:setValue elementIds="instrCaregiverExistRule" component="${component}" value=""/>
</ui:formGuide>

<ui:formGuide observeAndOr="or" ignoreDoOnLoad="true" simulateEvents="true">
   	<ui:observe elementIds="projName" component="${component}" forValue=".+"/>
   	<%-- clear the properties that have project-dependent lists --%>
   	<ui:setValue elementIds="esStatus" component="${component}" value=""/>
   	<ui:setValue elementIds="visitType" component="${component}" value=""/>
   	<ui:setValue elementIds="visitWith" component="${component}" value=""/>
   	<ui:setValue elementIds="visitLoc" component="${component}" value=""/>
   	<%-- since this content jsp may be included by different actions with different components,
   		need to use whatever the component is for events --%>
   	<ui:submitForm form="${component}" event="${component}__reRender"/>
</ui:formGuide>

