<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolConfig"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
   
<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField">label</page:param>  
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="label"/></page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<c:if test="${componentView != 'add'}">
<tags:createField property="id" component="${component}"/>
</c:if>
<tags:createField property="label" component="${component}"/>
<tags:createField property="notes" component="${component}"/>
<tags:createField property="projName" component="${component}"/>
<%-- NOT IMPLEMENTED YET (JUST NEED CATEGORY LIST OF DIFFERENT PROTOCOL CATEGORIES)
<tags:createField property="category" component="${component}"/>
 --%>
<c:if test="${componentView != 'view'}">
<tags:outputText textKey="protocol.firstTimepointConfigInfo" inline="false" styleClass="italic"/>
</c:if>
<tags:createField property="firstProtocolTimepointConfigId" component="${component}"/>
<tags:createField property="effDate" component="${component}"/>
<tags:createField property="expDate" component="${component}"/>
</page:applyDecorator>

</page:applyDecorator>


<c:if test="${componentView == 'view'}">

<%-- displaying the protocol tree as a list so set mode so properties are not editable --%>
<c:set var="fieldMode" value="lv"/>

<c:set var="protocolId">
	<tags:componentProperty component="${component}" property="id"/>
</c:set>
<c:set var="component" value="protocolConfigTree"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">protocolConfigTree</page:param>
  <page:param name="sectionNameKey">protocol.protocolConfigTree.section</page:param>
  
<div class="verticalSpace10">&nbsp;</div>
<tags:actionURLButton buttonText="Add Timepoint" actionId="lava.crms.protocol.setup.protocolAssessmentTimepointConfig" eventId="protocolAssessmentTimepointConfig__add" component="${component}" parameters="param,${protocolId}" locked="${currentPatient.locked}"/>
<div class="verticalSpace10">&nbsp;</div>

<tags:tableForm>  

<tags:listRow>
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Protocol Component" width="40%"/>
	<tags:listColumnHeader label="Type" width="30%" />
	<tags:listColumnHeader label="Eff. Date" width="10%" />
	<tags:listColumnHeader label="Exp. Date" width="10%" />
<%--	
	<tags:listColumnHeader label="Notes" width="30%" />
 --%>	
</tags:listRow>

<c:forEach items="${command.components[component].children}" var="protocolAssessmentTimepointConfig" varStatus="timepointIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolAssessmentTimepointConfig" component="protocolAssessmentTimepointConfig" idParam="${protocolAssessmentTimepointConfig.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell>Timepoint</tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].effDate" component="${component}" metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
<%--	
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
--%>	
</tags:listRow>	

	<c:forEach items="${protocolAssessmentTimepointConfig.children}" var="protocolVisitConfig" varStatus="visitIterator">
	<tags:listRow>
		<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolVisitConfig" component="protocolVisitConfig" idParam="${protocolVisitConfig.id}" locked="${item.locked}"/></tags:listCell>
		<tags:listCell>
			<c:forEach begin="1" end="8">&nbsp;</c:forEach>
			<tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
		</tags:listCell>
		<tags:listCell>Visit</tags:listCell>
		<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
<%--	
		<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
--%>		
	</tags:listRow>
	
		<c:forEach items="${protocolVisitConfig.options}" var="protocolVisitOptionConfig" varStatus="visitOptionIterator">
		<tags:listRow>
			<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolVisitOptionConfig" component="protocolVisitOptionConfig" idParam="${protocolVisitOptionConfig.id}" locked="${item.locked}"/></tags:listCell>
			<tags:listCell>
				<c:forEach begin="1" end="11">&nbsp;</c:forEach>
				<tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].options[${visitOptionIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
			</tags:listCell>
			<tags:listCell>Visit Option</tags:listCell>
			<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].options[${visitOptionIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
			<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].options[${visitOptionIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
<%--	
			<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].options[${visitOptionIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
--%>			
		</tags:listRow>
		</c:forEach>

			<c:forEach items="${protocolVisitConfig.children}" var="protocolInstrumentConfig" varStatus="instrumentIterator">
			<tags:listRow>
				<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolInstrumentConfig" component="protocolInstrumentConfig" idParam="${protocolInstrumentConfig.id}" locked="${item.locked}"/></tags:listCell>
				<tags:listCell>
					<c:forEach begin="1" end="16">&nbsp;</c:forEach>
					<tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
				</tags:listCell>
				<tags:listCell>Instrument</tags:listCell>
				<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
				<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
<%--	
				<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
--%>				
			</tags:listRow>
			
					<c:forEach items="${protocolInstrumentConfig.options}" var="protocolInstrumentOptionConfig" varStatus="instrumentOptionIterator">
					<tags:listRow>
						<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolInstrumentOptionConfig" component="protocolInstrumentOptionConfig" idParam="${protocolInstrumentOptionConfig.id}" locked="${item.locked}"/></tags:listCell>
						<tags:listCell>
							<c:forEach begin="1" end="19">&nbsp;</c:forEach>
							<tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
						</tags:listCell>
						<tags:listCell>Instrument Option</tags:listCell>
						<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
						<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
<%--	
						<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
--%>						
					</tags:listRow>
					</c:forEach>
			
			</c:forEach>
		
	</c:forEach>

</c:forEach>
</tags:tableForm>  
</page:applyDecorator>

</c:if>

</page:applyDecorator>    


