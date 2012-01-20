<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolCompletionStatus"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<c:if test="${componentView != 'add'}">
	<c:set var="protocolConfigLabel">
		<tags:componentProperty component="${component}" property="protocolConfig" property2="label"/>
	</c:set>   
</c:if>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField">protocolId</page:param>  
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/>,${protocolConfigLabel}</page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<tags:createField property="id" component="${component}" entityType="protocol"/>
<tags:createField property="projName" component="${component}" entityType="protocol" mode="vw"/>
<tags:createField property="patient.fullNameNoSuffix" component="${component}" metadataName="patient.fullNameNoSuffix"/>
<tags:createField property="assignedDate" component="${component}" entityType="protocol" mode="vw"/>
<%--
<tags:createField property="staff" component="${component}"/>
 --%>
<tags:createField property="currStatus" component="${component}" entityType="protocol" mode="vw"/>
<tags:createField property="currReason" component="${component}" entityType="protocol" mode="vw"/>
<tags:createField property="currNote" component="${component}" entityType="protocol" mode="vw"/>
<tags:createField property="notes" component="${component}" entityType="protocol" mode="vw"/>
</page:applyDecorator>

</page:applyDecorator>


<%-- displaying the protocol tree as a list so set mode so properties are not editable --%>
<c:set var="fieldMode" value="lv"/>

<c:set var="protocolId">
	<tags:componentProperty component="${component}" property="id"/>
</c:set>
<c:set var="component" value="protocolTree"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">protocolTree</page:param>
  <page:param name="sectionNameKey">protocol.protocolTree.section</page:param>
  
<tags:tableForm>  

<tags:listRow>
	<tags:listColumnHeader label="Action" width="6%"/>
	<tags:listColumnHeader label="Protocol Component" width="24%"/>
	<tags:listColumnHeader label="Configuration" width="12%"/>
	<tags:listColumnHeader label="Assignment" width="29%"/>
	<tags:listColumnHeader label="Completion Status" width="29%"/>
</tags:listRow>

<c:forEach items="${command.components[component].children}" var="timepoint" varStatus="timepointIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLButton buttonImage="view" actionId="lava.crms.protocol.assignment.protocolTimepoint" eventId="protocolTimepoint__view" idParam="${timepoint.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell>
		Timepoint
		<tags:listActionURLButton buttonImage="view" actionId="lava.crms.protocol.setup.protocolTimepointConfig" eventId="protocolTimepointConfig__view" idParam="${timepoint.config.id}"/>	    
	</tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].assignDescrip" component="${component}" metadataName="protocol.assignDescrip" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].currStatusBlock" component="${component}" metadataName="protocol.currStatusBlock" mode="${fieldMode}"/></tags:listCell>
</tags:listRow>	

	<c:forEach items="${timepoint.children}" var="visit" varStatus="visitIterator">
	<tags:listRow>
		<tags:listCell><tags:listActionURLButton buttonImage="view" actionId="lava.crms.protocol.assignment.protocolVisit" eventId="protocolVisit__view" idParam="${timepoint.id}" locked="${item.locked}"/></tags:listCell>
		<tags:listCell>
			<c:forEach begin="1" end="8">&nbsp;</c:forEach>
			<tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
		</tags:listCell>
		<tags:listCell>
			Visit
			<tags:listActionURLButton buttonImage="view" actionId="lava.crms.protocol.setup.protocolVisitConfig" eventId="protocolVisitConfig__view" idParam="${visit.config.id}"/>
		</tags:listCell>	    
		<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].assignDescrip" component="${component}" metadataName="protocol.assignDescrip" mode="${fieldMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].currStatusBlock" component="${component}" metadataName="protocol.currStatusBlock" mode="${fieldMode}"/></tags:listCell>
	</tags:listRow>
	
			<c:forEach items="${visit.children}" var="instrument" varStatus="instrumentIterator">
			<tags:listRow>
				<tags:listCell><tags:listActionURLButton buttonImage="view" actionId="lava.crms.protocol.assignment.protocolInstrument" eventId="protocolInstrument__view" idParam="${timepoint.id}" locked="${item.locked}"/></tags:listCell>
				<tags:listCell>
					<c:forEach begin="1" end="16">&nbsp;</c:forEach>
					<tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
				</tags:listCell>
				<tags:listCell>
					Instrument
					<tags:listActionURLButton buttonImage="view" actionId="lava.crms.protocol.setup.protocolInstrumentConfig" eventId="protocolInstrumentConfig__view" idParam="${instrument.config.id}"/>
				</tags:listCell>
				<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].assignDescrip" component="${component}" metadataName="protocol.assignDescrip" mode="${fieldMode}"/></tags:listCell>
				<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].currStatusBlock" component="${component}" metadataName="protocol.currStatusBlock" mode="${fieldMode}"/></tags:listCell>
			</tags:listRow>
			</c:forEach>
		
	</c:forEach>

</c:forEach>

</tags:tableForm>  
</page:applyDecorator>

</page:applyDecorator>    


