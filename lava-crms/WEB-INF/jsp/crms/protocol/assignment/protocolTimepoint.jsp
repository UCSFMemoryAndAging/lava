t<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolTimepoint"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<c:set var="timepointConfigLabel">
	<tags:componentProperty component="${component}" property="protocolTimepointConfig" property2="label"/>
</c:set>   
   
<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField"></page:param>  
  <page:param name="pageHeadingArgs">${timepointConfigLabel},${protocolConfigLabel},<tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/></page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
<c:if test="${componentView != 'add'}">
<tags:createField property="id" component="${component}" entityType="protocolTimepoint"/>
</c:if>
<tags:createField property="protocolTimepointConfig.label" component="${component}" metadataName="protocolTimepointConfig.label" mode="vw"/>
</page:applyDecorator>

<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">compStatusSection</page:param>
  <page:param name="sectionNameKey">${component}.compStatusSection.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
<tags:createField property="compStatusOverride" component="${component}"/>
<tags:createField property="compStatus" component="${component}"/>
<tags:createField property="compStatusComputed" component="${component}"/>
<%-- compStatus rolled up from visits. no way to roll up reason or note, so do not include those here?? --%>
</page:applyDecorator>
</tags:contentColumn>

<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">repeat</page:param>
  <page:param name="sectionNameKey">${component}.repeat.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
<c:choose>
	<c:when test="${repeatingTimepoint}">
		<tags:createField property="repeatNum" component="${component}"/>
		<c:if test="${componentView == 'edit' && not empty lastRepeatNum}">
			<tags:createField property="newRepeatNum" component="${component}"/>
			<tags:eventButton buttonText="Add Repeating" action="custom" component="${component}" pageName="${component}" className="pageLevelLeftmostButton" locked="${command.components[component].locked}"/>
		</c:if>
	</c:when>
	<c:otherwise>
		<tags:outputText textKey="protocol.notRepeatingTimepoint" inline="false" styleClass="italic"/>
	</c:otherwise>
</c:choose>	
</page:applyDecorator>
</tags:contentColumn>

<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">schedStatus</page:param>
  <page:param name="sectionNameKey">${component}.schedStatus.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
<tags:createField property="schedWinAnchorDate" component="${component}"/>
<tags:createField property="schedWinStart" component="${component}"/>
<tags:createField property="schedWinEnd" component="${component}"/>
<tags:createField property="schedWinStatus" component="${component}"/>
<%-- schedWinStatus rolled up from visits. no way to roll up reason or note, so do not include those here?? --%>
</page:applyDecorator>
</tags:contentColumn>

<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">idealSchedWin</page:param>
  <page:param name="sectionNameKey">${component}.idealSchedWin.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
<tags:createField property="idealSchedWinAnchorDate" component="${component}"/>
<tags:createField property="idealSchedWinStart" component="${component}"/>
<tags:createField property="idealSchedWinEnd" component="${component}"/>
</page:applyDecorator>
</tags:contentColumn>


<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">collectStatus</page:param>
  <page:param name="sectionNameKey">${component}.collectStatus.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
<tags:createField property="collectWinAnchorDate" component="${component}"/>
<tags:createField property="collectWinStart" component="${component}"/>
<tags:createField property="collectWinEnd" component="${component}"/>
<tags:createField property="collectWinStatus" component="${component}"/>
<%-- collectWinStatus rolled up from visits. no way to roll up reason or note, so do not include those here?? --%>
</page:applyDecorator>
</tags:contentColumn>

<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">idealCollectWin</page:param>
  <page:param name="sectionNameKey">${component}.idealCollectWin.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
<tags:createField property="idealCollectWinAnchorDate" component="${component}"/>
<tags:createField property="idealCollectWinStart" component="${component}"/>
<tags:createField property="idealCollectWinEnd" component="${component}"/>
</page:applyDecorator>
</tags:contentColumn>



<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
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
  <page:param name="sectionNameKey">protocolTimepoint.timepointTree.section</page:param>
  
<tags:tableForm>  

<tags:listRow>
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Protocol Component" width="26%"/>
	<tags:listColumnHeader label="Configuration" width="12%" />
	<tags:listColumnHeader label="Assignment" width="52%" />
</tags:listRow>

<c:forEach items="${command.components[component].children}" var="visit" varStatus="visitIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolVisit" component="protocolVisit" idParam="${visit.id}" noDelete="true" locked="${item.locked}"/></tags:listCell>
	<tags:listCell>
		<tags:createField property="children[${visitIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
	</tags:listCell>
	<tags:listCell>
		Visit
		<tags:listActionURLButton buttonImage="view" actionId="lava.crms.protocol.setup.protocolVisitConfig" eventId="protocolVisitConfig__view" idParam="${visit.config.id}"/>
	</tags:listCell>
	<tags:listCell>
		<tags:createField property="children[${visitIterator.index}].summary" component="${component}" metadataName="protocol.summary" mode="${fieldMode}"/>
		<tags:listActionURLButton buttonImage="view" actionId="lava.crms.scheduling.visit.visit" eventId="visit__view" idParam="${visit.visit.id}"/>
	</tags:listCell>	    
</tags:listRow>

		<c:forEach items="${visit.children}" var="instrument" varStatus="instrumentIterator">
		<tags:listRow>
			<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolInstrument" component="protocolInstrument" idParam="${instrument.id}" noDelete="true" locked="${item.locked}"/></tags:listCell>
			<tags:listCell>
				<c:forEach begin="1" end="8">&nbsp;</c:forEach>
				<tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
			</tags:listCell>
			<tags:listCell>
				Instrument
				<tags:listActionURLButton buttonImage="view" actionId="lava.crms.protocol.setup.protocolInstrumentConfig" eventId="protocolInstrumentConfig__view" idParam="${instrument.config.id}"/>
			</tags:listCell>
			<tags:listCell>
				<tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].summary" component="${component}" metadataName="protocol.summary" mode="${fieldMode}"/>
				<tags:listActionURLButton buttonImage="view" actionId="lava.crms.assessment.instrument.${instrument.instrument.instrTypeEncoded}" eventId="${instrument.instrument.instrTypeEncoded}__view" idParam="${instrument.instrument.id}"/>
			</tags:listCell>
		</tags:listRow>
		</c:forEach>
	
</c:forEach>

</tags:tableForm>  
</page:applyDecorator>

</c:if>


<c:if test="${componentView != 'view'}">

<ui:formGuide simulateEvents="true">
	<ui:observeForNull elementIds="compStatusOverride" component="${component}"/>
	<ui:disable elementIds="compStatus" component="${component}"/>
</ui:formGuide>

</c:if>

</page:applyDecorator>    
