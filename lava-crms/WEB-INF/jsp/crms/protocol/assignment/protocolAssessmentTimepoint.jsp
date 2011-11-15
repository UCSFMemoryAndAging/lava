<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolAssessmentTimepoint"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<c:set var="timepointConfigLabel">
	<tags:componentProperty component="${component}" property="timepointConfig" property2="label"/>
</c:set>   
   
<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField"></page:param>  
  <page:param name="pageHeadingArgs">${protocolConfigLabel},${timepointConfigLabel},<tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/></page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<c:if test="${componentView != 'add'}">
<tags:createField property="id" component="${component}" entityType="protocolTimepoint"/>
</c:if>
<tags:createField property="timepointConfig.label" component="${component}" metadataName="protocolConfig.label" mode="vw"/>
<tags:createField property="currStatus" component="${component}" entityType="protocol" labelStyle="longLeft"/>
<tags:createField property="currReason" component="${component}" entityType="protocol" labelStyle="longLeft"/>
<tags:createField property="currNote" component="${component}" entityType="protocol" labelStyle="longLeft"/>
<tags:createField property="schedWinStatus" component="${component}" entityType="protocolTimepoint" labelStyle="longLeft"/>
<tags:createField property="schedWinReason" component="${component}" entityType="protocolTimepoint" labelStyle="longLeft"/>
<tags:createField property="schedWinNote" component="${component}" entityType="protocolTimepoint" labelStyle="longLeft"/>
<tags:createField property="schedWinStart" component="${component}" entityType="protocolTimepoint" labelStyle="longLeft"/>
<tags:createField property="schedWinEnd" component="${component}" entityType="protocolTimepoint" labelStyle="longLeft"/>
<tags:createField property="collectWinStatus" component="${component}" labelStyle="longLeft"/>
<tags:createField property="collectWinReason" component="${component}" labelStyle="longLeft"/>
<tags:createField property="collectWinNote" component="${component}" labelStyle="longLeft"/>
<tags:createField property="collectWinStart" component="${component}" labelStyle="longLeft"/>
<tags:createField property="collectWinEnd" component="${component}" labelStyle="longLeft"/>
<tags:createField property="notes" component="${component}"/>
  
</page:applyDecorator>

</page:applyDecorator>    


<c:if test="${componentView == 'view'}">

<%-- displaying the protocol tree as a list so set mode so properties are not editable --%>
<c:set var="fieldMode" value="lv"/>

<c:set var="timepointId">
	<tags:componentProperty component="${component}" property="id"/>
</c:set>
<c:set var="component" value="protocolTimepointTree"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">timepointTree</page:param>
  <page:param name="sectionNameKey">protocol.timepointTree.section</page:param>
  
<tags:tableForm>  

<tags:listRow>
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Protocol Component" width="25%"/>
	<tags:listColumnHeader label="Type" width="25%" />
	<tags:listColumnHeader label="Notes" width="40%" />
</tags:listRow>

<c:forEach items="${command.components[component].children}" var="visit" varStatus="visitIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolVisit" component="protocolVisit" idParam="${visit.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell>
		<c:forEach begin="1" end="8">&nbsp;</c:forEach>
		<tags:createField property="children[${visitIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
	</tags:listCell>
	<tags:listCell>Visit</tags:listCell>
	<tags:listCell><tags:createField property="children[${visitIterator.index}].notes" component="${component}" metadataName="protocol.notes" mode="${fieldMode}"/></tags:listCell>
</tags:listRow>

		<c:forEach items="${visit.children}" var="instrument" varStatus="instrumentIterator">
		<tags:listRow>
			<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolInstrument" component="protocolInstrument" idParam="${instrument.id}" locked="${item.locked}"/></tags:listCell>
			<tags:listCell>
				<c:forEach begin="1" end="16">&nbsp;</c:forEach>
				<tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
			</tags:listCell>
			<tags:listCell>Instrument</tags:listCell>
			<tags:listCell><tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].notes" component="${component}" metadataName="protocol.notes" mode="${fieldMode}"/></tags:listCell>
		</tags:listRow>
		</c:forEach>
	
</c:forEach>

</tags:tableForm>  
</page:applyDecorator>

</c:if>

</page:applyDecorator>    
