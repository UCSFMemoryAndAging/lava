<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolAssessmentTimepointConfig"/>
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
<tags:createField property="id" component="${component}" entityType="protocolTimepointConfig"/>
</c:if>
<tags:createField property="label" component="${component}" entityType="protocolTimepointConfig"/>
<tags:createField property="notes" component="${component}" entityType="protocolTimepointConfig"/>
<tags:createField property="effDate" component="${component}" entityType="protocolTimepointConfig"/>
<tags:createField property="expDate" component="${component}" entityType="protocolTimepointConfig"/>
<tags:createField property="optional" component="${component}" labelStyle="longLeft" entityType="protocolTimepointConfig"/>

<tags:createField property="firstTimepoint" component="${component}" labelStyle="longLeft" entityType="protocolTimepointConfig"/>

<tags:createField property="schedWinAnchorTimepointId" component="${component}" labelStyle="longLeft" entityType="protocolTimepointConfig"/>
<tags:createField property="schedWinDaysFromAnchor" component="${component}" labelStyle="longLeft" entityType="protocolTimepointConfig"/>
<tags:createField property="schedWinSize" component="${component}" labelStyle="longLeft" entityType="protocolTimepointConfig"/>
<tags:createField property="schedWinOffset" component="${component}" labelStyle="longLeft" entityType="protocolTimepointConfig"/>

<tags:createField property="collectWinAnchorVisitId" component="${component}" labelStyle="longLeft"/>
<tags:createField property="collectWinSize" component="${component}" labelStyle="longLeft"/>
<tags:createField property="collectWinOffset" component="${component}" labelStyle="longLeft"/>
<tags:createField property="collectWinStatus" component="${component}" labelStyle="longLeft"/>

</page:applyDecorator>

<ui:formGuide simulateEvents="true">
	<ui:observeForNull elementIds="firstTimepoint" component="${component}"/>
	<ui:enable elementIds="schedWinAnchorTimepointId,schedWinDaysFromAnchor,schedWinSize,schedWinOffset" component="${component}"/>
</ui:formGuide>

</page:applyDecorator>    


<c:if test="${componentView == 'view'}">

<%-- displaying the protocol tree as a list so set mode so properties are not editable --%>
<c:set var="fieldMode" value="lv"/>

<c:set var="timepointId">
	<tags:componentProperty component="${component}" property="id"/>
</c:set>
<c:set var="component" value="timepointConfigTree"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">timepointConfigTree</page:param>
  <page:param name="sectionNameKey">protocol.timepointConfigTree.section</page:param>
  
<div class="verticalSpace10">&nbsp;</div>
<tags:actionURLButton buttonText="Add Visit" actionId="lava.crms.protocol.setup.protocolVisitConfig" eventId="protocolVisitConfig__add" component="${component}" parameters="param,${timepointId}" locked="${currentPatient.locked}"/>
<div class="verticalSpace10">&nbsp;</div>

<tags:tableForm>  

<tags:listRow>
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Protocol Component" width="24%"/>
	<tags:listColumnHeader label="Type" width="18%" />
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

	<c:forEach items="${protocolVisit.options}" var="protocolVisitOptionConfig" varStatus="visitOptionIterator">
	<tags:listRow>
		<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolVisitOptionConfig" component="protocolVisitOptionConfig" idParam="${protocolVisitOptionConfig.id}" locked="${item.locked}"/></tags:listCell>
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
		
				<c:forEach items="${protocolInstrumentConfig.options}" var="protocolInstrumentOptionConfig" varStatus="instrumentOptionIterator">
				<tags:listRow>
					<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolInstrumentOptionConfig" component="protocolInstrumentOptionConfig" idParam="${protocolInstrumentOptionConfig.id}" locked="${item.locked}"/></tags:listCell>
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

</page:applyDecorator>    
