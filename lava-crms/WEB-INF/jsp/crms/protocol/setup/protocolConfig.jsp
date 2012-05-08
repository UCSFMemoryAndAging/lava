<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolConfig"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
   
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
<tags:createField property="projName" component="${component}"/>
<tags:createField property="descrip" component="${component}"/>
<c:if test="${componentView != 'view'}">
<%-- only show text if not First Timepoint because if First it would be redundant with text shown below --%>
<tags:ifComponentPropertyNotEmpty component="protocolConfig" property="firstProtocolTimepointConfigId">  
	<tags:outputText textKey="protocol.firstTimepointConfigInfo" inline="false" styleClass="italic"/>
	<tags:createField property="firstProtocolTimepointConfigId" component="${component}"/>
</tags:ifComponentPropertyNotEmpty>	
<tags:ifComponentPropertyEmpty component="protocolConfig" property="firstProtocolTimepointConfigId">
	<%-- if no First Timepoint Config yet, no sense in having the property editable --%>  
	<tags:createField property="firstProtocolTimepointConfigId" component="${component}" mode="vw"/>
</tags:ifComponentPropertyEmpty>	
</c:if>
<tags:createField property="effDate" component="${component}"/>
<tags:createField property="expDate" component="${component}"/>
<tags:createField property="notes" component="${component}"/>
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
  <page:param name="sectionId">timepoints</page:param>
  <page:param name="sectionNameKey">protocolConfig.timepoints.section</page:param>

<tags:ifComponentPropertyEmpty component="protocolConfig" property="firstProtocolTimepointConfigId">  
	<tags:outputText textKey="protocol.addFirstTimepointConfig" inline="false" styleClass="italic"/>
</tags:ifComponentPropertyEmpty>	
<div class="verticalSpace10">&nbsp;</div>
<tags:actionURLButton buttonText="Add Timepoint Conf" actionId="lava.crms.protocol.setup.protocolTimepointConfig" eventId="protocolTimepointConfig__add" component="${component}" parameters="param,${protocolId}" locked="${currentPatient.locked}"/>
<div class="verticalSpace10">&nbsp;</div>

<tags:tableForm>  

<tags:listRow>
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Protocol Component" width="25%"/>
	<tags:listColumnHeader label="Type" width="15%" />
	<tags:listColumnHeader label="Summary" width="30%" />
	<tags:listColumnHeader label="Eff. Date" width="10%" />
	<tags:listColumnHeader label="Exp. Date" width="10%" />
</tags:listRow>

<c:forEach items="${command.components[component].children}" var="protocolTimepointConfig" varStatus="timepointIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolTimepointConfig" component="protocolTimepointConfig" idParam="${protocolTimepointConfig.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell>Timepoint</tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].summary" component="${component}"  metadataName="protocolConfig.summary" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].effDate" component="${component}" metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${timepointIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
</tags:listRow>	

	<c:forEach items="${protocolTimepointConfig.children}" var="protocolVisitConfig" varStatus="visitIterator">
	<tags:listRow>
		<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolVisitConfig" component="protocolVisitConfig" idParam="${protocolVisitConfig.id}" locked="${item.locked}"/></tags:listCell>
		<tags:listCell>
			<c:forEach begin="1" end="8">&nbsp;</c:forEach>
			<tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
		</tags:listCell>
		<tags:listCell>Visit</tags:listCell>
		<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].summary" component="${component}"  metadataName="protocolConfig.summary" mode="${fieldMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
	</tags:listRow>
	
		<c:forEach items="${protocolVisitConfig.options}" var="protocolVisitConfigOption" varStatus="visitOptionIterator">
		<tags:listRow>
			<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolVisitConfigOption" component="protocolVisitConfigOption" idParam="${protocolVisitConfigOption.id}" locked="${item.locked}"/></tags:listCell>
			<tags:listCell>&nbsp;</tags:listCell>
			<tags:listCell>Visit Option</tags:listCell>
			<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].options[${visitOptionIterator.index}].summary" component="${component}"  metadataName="protocolConfig.summary" mode="${fieldMode}"/></tags:listCell>
			<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].options[${visitOptionIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
			<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].options[${visitOptionIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
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
				<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].summary" component="${component}"  metadataName="protocolConfig.summary" mode="${fieldMode}"/></tags:listCell>
				<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
				<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
			</tags:listRow>
			
					<c:forEach items="${protocolInstrumentConfig.options}" var="protocolInstrumentConfigOption" varStatus="instrumentOptionIterator">
					<tags:listRow>
						<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolInstrumentConfigOption" component="protocolInstrumentConfigOption" idParam="${protocolInstrumentConfigOption.id}" locked="${item.locked}"/></tags:listCell>
						<tags:listCell>&nbsp;</tags:listCell>
						<tags:listCell>Instrument Option</tags:listCell>
						<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].summary" component="${component}"  metadataName="protocolConfig.summary" mode="${fieldMode}"/></tags:listCell>
						<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
						<tags:listCell><tags:createField property="children[${timepointIterator.index}].children[${visitIterator.index}].children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
					</tags:listRow>
					</c:forEach>
			
			</c:forEach>
		
	</c:forEach>

</c:forEach>
</tags:tableForm>  
</page:applyDecorator>

</c:if>

</page:applyDecorator>    


