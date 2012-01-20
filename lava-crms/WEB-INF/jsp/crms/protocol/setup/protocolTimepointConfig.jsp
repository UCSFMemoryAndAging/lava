<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolTimepointConfig"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
   
<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField">label</page:param>  
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="label"/></page:param>
  <page:param name="quicklinks">sched,primary,collect,repeat</page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<c:if test="${componentView != 'add'}">
<tags:createField property="id" component="${component}" entityType="protocolTimepointConfig"/>
</c:if>
<tags:createField property="label" component="${component}" entityType="protocolTimepointConfig"/>
<tags:createField property="optional" component="${component}" entityType="protocolTimepointConfig"/>
<tags:createField property="effDate" component="${component}" entityType="protocolTimepointConfig"/>
<tags:createField property="expDate" component="${component}" entityType="protocolTimepointConfig"/>
<tags:createField property="notes" component="${component}" entityType="protocolTimepointConfig"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">sched</page:param>
  <page:param name="sectionNameKey">${component}.sched.section</page:param>
<c:choose>
	<c:when test="${firstTimepointFlag}">
		<tags:outputText textKey="protocol.firstTimepointConfig" inline="false" styleClass="bold"/>
		<tags:outputText textKey="protocol.firstTimepointConfigPart2" inline="false" styleClass="italic"/>
	</c:when>
	<c:otherwise>
		<tags:createField property="schedWinRelativeTimepointId" component="${component}"/>
		<tags:createField property="schedWinRelativeAmount,schedWinRelativeUnits" component="${component}"/>
	</c:otherwise>
</c:choose>
<c:if test="${firstTimepointFlag}">
	<tags:outputText textKey="protocol.firstTimepointSchedWin" inline="false"  styleClass="italic"/>
</c:if>
<tags:outputText textKey="protocol.timepointSchedWinOffset" inline="false" styleClass="italic"/>
<tags:createField property="schedWinOffset" component="${component}"/>
<tags:outputText textKey="protocol.timepointSchedWinSize" inline="false" styleClass="italic"/>
<tags:createField property="schedWinSize" component="${component}"/>
<tags:outputText textKey="protocol.timepointDuration" inline="false" styleClass="italic"/>
<tags:createField property="duration" component="${component}"/>
<%--comment out until implemented (add to skip logic too)
<tags:outputText textKey="protocol.timepointSchedAuto" inline="false" styleClass="italic"/>
<tags:createField property="schedAutomatic" component="${component}"/>
 --%>
<%-- for debugging only --%>
<spring:bind path="command.components['${component}'].schedWinDaysFromStart">
days from protocol start=${status.value}</br>  
</spring:bind>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">primary</page:param>
  <page:param name="sectionNameKey">${component}.primary.section</page:param>
<c:if test="${componentView != 'view'}">
<tags:outputText textKey="protocol.timepointPrimaryProtocolVisitConfigInfo" inline="false" styleClass="italic"/>
</c:if>
<tags:createField property="primaryProtocolVisitConfigId" component="${component}" mode="${componentView == 'add' ? 'vw' : componentMode}"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">collect</page:param>
  <page:param name="sectionNameKey">${component}.collect.section</page:param>
<tags:createField property="collectWindowDefined" component="${component}" labelAlignment="right" labelStyle="checkboxRight"/>
<tags:outputText textKey="protocol.timepointCollectWin" inline="false" styleClass="italic"/>
<tags:outputText textKey="protocol.timepointCollectWinOffset" inline="false" styleClass="italic"/>
<tags:createField property="collectWinOffset" component="${component}"/>
<tags:outputText textKey="protocol.timepointCollectWinSize" inline="false" styleClass="italic"/>
<tags:createField property="collectWinSize" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">repeat</page:param>
  <page:param name="sectionNameKey">${component}.repeat.section</page:param>
<tags:createField property="repeating" component="${component}" labelAlignment="right" labelStyle="checkboxRight"/>
<tags:createField property="repeatInterval" component="${component}"/>
<tags:outputText textKey="protocol.timepointRepeatNum" inline="false" styleClass="italic"/>
<tags:createField property="repeatInitialNum" component="${component}"/>
<tags:createField property="repeatCreateAutomatic" component="${component}"/>
</page:applyDecorator>

</page:applyDecorator>    


<c:if test="${componentView == 'view'}">

<%-- displaying the protocol tree as a list so set mode so properties are not editable --%>
<c:set var="fieldMode" value="lv"/>

<c:set var="timepointId">
	<tags:componentProperty component="${component}" property="id"/>
</c:set>
<c:set var="component" value="timepointConfigTree"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">addVisit</page:param>
  <page:param name="sectionNameKey">protocolTimepointConfig.addVisit.section</page:param>
  
<div class="verticalSpace10">&nbsp;</div>
<tags:actionURLButton buttonText="Add Visit" actionId="lava.crms.protocol.setup.protocolVisitConfig" eventId="protocolVisitConfig__add" component="${component}" parameters="param,${timepointId}" locked="${currentPatient.locked}"/>
<div class="verticalSpace10">&nbsp;</div>

<tags:tableForm>  

<tags:listRow>
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Protocol Component" width="26%"/>
	<tags:listColumnHeader label="Type" width="16%" />
	<tags:listColumnHeader label="Eff. Date" width="9%" />
	<tags:listColumnHeader label="Exp. Date" width="9%" />
	<tags:listColumnHeader label="Notes" width="30%" />
</tags:listRow>

<c:forEach items="${command.components[component].children}" var="protocolVisitConfig" varStatus="visitIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolVisitConfig" component="protocolVisitConfig" idParam="${protocolVisitConfig.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${visitIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell>Visit</tags:listCell>
	<tags:listCell><tags:createField property="children[${visitIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${visitIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${visitIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
</tags:listRow>	

	<c:forEach items="${protocolVisit.options}" var="protocolVisitConfigOption" varStatus="visitOptionIterator">
	<tags:listRow>
		<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolVisitConfigOption" component="protocolVisitConfigOption" idParam="${protocolVisitConfigOption.id}" locked="${item.locked}"/></tags:listCell>
		<tags:listCell>
			<c:forEach begin="1" end="3">&nbsp;</c:forEach>
			<tags:createField property="children[${visitIterator.index}].options[${visitOptionIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
		</tags:listCell>
		<tags:listCell>Visit Option</tags:listCell>
		<tags:listCell><tags:createField property="children[${visitIterator.index}].options[${visitOptionIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="children[${visitIterator.index}].options[${visitOptionIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="children[${visitIterator.index}].options[${visitOptionIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
	</tags:listRow>
	</c:forEach>
	
		<c:forEach items="${protocolVisitConfig.children}" var="protocolInstrumentConfig" varStatus="instrumentIterator">
		<tags:listRow>
			<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolInstrumentConfig" component="protocolInstrumentConfig" idParam="${protocolInstrumentConfig.id}" locked="${item.locked}"/></tags:listCell>
			<tags:listCell>
				<c:forEach begin="1" end="8">&nbsp;</c:forEach>
				<tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
			</tags:listCell>
			<tags:listCell>Instrument</tags:listCell>
			<tags:listCell><tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
			<tags:listCell><tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
			<tags:listCell><tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
		</tags:listRow>
		
				<c:forEach items="${protocolInstrumentConfig.options}" var="protocolInstrumentConfigOption" varStatus="instrumentOptionIterator">
				<tags:listRow>
					<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolInstrumentConfigOption" component="protocolInstrumentConfigOption" idParam="${protocolInstrumentConfigOption.id}" locked="${item.locked}"/></tags:listCell>
					<tags:listCell>
						<c:forEach begin="1" end="11">&nbsp;</c:forEach>
						<tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
					</tags:listCell>
					<tags:listCell>Instrument Option</tags:listCell>
					<tags:listCell><tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
					<tags:listCell><tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
					<tags:listCell><tags:createField property="children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
				</tags:listRow>
				</c:forEach>
		
		</c:forEach>
	
</c:forEach>
</tags:tableForm>  
</page:applyDecorator>

</c:if>

<c:if test="${componentView != 'view'}">

<ui:formGuide>
	<ui:observeForNull elementIds="collectWindowDefined" component="${component}"/>
	<ui:disable elementIds="collectWinSize,collectWinOffset" component="${component}"/>
	<ui:setValue elementIds="collectWinSize,collectWinOffset" component="${component}" value=""/>
</ui:formGuide>

<ui:formGuide simulateEvents="true">
	<ui:observeForNull elementIds="repeating" component="${component}"/>
	<ui:disable elementIds="repeatInterval,repeatInitialNum,repeatCreateAutomatic" component="${component}"/>
	<ui:setValue elementIds="repeatInterval,repeatInitialNum,repeatCreateAutomatic" component="${component}" value=""/>
</ui:formGuide>

</c:if>


</page:applyDecorator>    
