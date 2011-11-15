<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocol"/>
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
<c:if test="${componentView != 'add'}">
<tags:createField property="id" component="${component}" entityType="protocol"/>
</c:if>
<tags:createField property="protocolConfigId" component="${component}" mode="${componentView == 'add' ? 'de' : 'vw'}"/>
<c:if test="${componentView != 'add'}">
<tags:createField property="projName" component="${component}"/>
</c:if>
<tags:createField property="patient.fullNameNoSuffix" component="${component}" metadataName="patient.fullNameNoSuffix"/>
<tags:createField property="enrolledDate" component="${component}"/>
<%--
<tags:createField property="staff" component="${component}"/>
 --%>
<c:if test="${componentView != 'add'}">
<tags:createField property="currStatus" component="${component}"/>
<tags:createField property="currReason" component="${component}"/>
<tags:createField property="currNote" component="${component}"/>
<tags:createField property="notes" component="${component}"/>
</c:if>
</page:applyDecorator>

</page:applyDecorator>


<c:if test="${componentView == 'view'}">

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
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Protocol Component" width="25%"/>
	<tags:listColumnHeader label="Type" width="25%" />
	<tags:listColumnHeader label="Notes" width="40%" />
</tags:listRow>

<c:forEach items="${command.components[component].children}" var="timepoint" varStatus="timepointIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolAssessmentTimepoint" component="protocolAssessmentTimepoint" idParam="${timepoint.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell>Timepoint</tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].notes" component="${component}" metadataName="protocol.notes" mode="${fieldMode}"/></tags:listCell>
</tags:listRow>	

	<c:forEach items="${timepoint.children}" var="visit" varStatus="visitIterator">
	<tags:listRow>
		<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolVisit" component="protocolVisit" idParam="${visit.id}" locked="${item.locked}"/></tags:listCell>
		<tags:listCell>
			<c:forEach begin="1" end="8">&nbsp;</c:forEach>
			<tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
		</tags:listCell>
		<tags:listCell>Visit</tags:listCell>
		<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].notes" component="${component}" metadataName="protocol.notes" mode="${fieldMode}"/></tags:listCell>
	</tags:listRow>
	
			<c:forEach items="${visit.children}" var="instrument" varStatus="instrumentIterator">
			<tags:listRow>
				<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolInstrument" component="protocolInstrument" idParam="${instrument.id}" locked="${item.locked}"/></tags:listCell>
				<tags:listCell>
					<c:forEach begin="1" end="16">&nbsp;</c:forEach>
					<tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
				</tags:listCell>
				<tags:listCell>Instrument</tags:listCell>
				<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].notes" component="${component}" metadataName="protocol.notes" mode="${fieldMode}"/></tags:listCell>
			</tags:listRow>
			</c:forEach>
		
	</c:forEach>

</c:forEach>

</tags:tableForm>  
</page:applyDecorator>

</c:if>

</page:applyDecorator>    


