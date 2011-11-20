<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolVisitConfig"/>
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
<c:if test="${primaryVisitConfigFlag}">
	<tags:outputText textKey="protocol.primaryVisitConfig" inline="false" styleClass="bold"/>
</c:if>
<tags:createField property="optional" component="${component}"/>
<tags:createField property="defaultOptionId" component="${component}"/>
<tags:createField property="notes" component="${component}"/>
<tags:createField property="effDate" component="${component}"/>
<tags:createField property="expDate" component="${component}"/>
</page:applyDecorator>

</page:applyDecorator>    


<c:if test="${componentView == 'view'}">

<%-- displaying the protocol tree as a list so set mode so properties are not editable --%>
<c:set var="fieldMode" value="lv"/>

<c:set var="visitId">
	<tags:componentProperty component="${component}" property="id"/>
</c:set>
<c:set var="component" value="visitConfigTree"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">visitConfigTree</page:param>
  <page:param name="sectionNameKey">protocol.visitConfigTree.section</page:param>
  
<div class="verticalSpace10">&nbsp;</div>
<tags:actionURLButton buttonText="Add Instrument"  actionId="lava.crms.protocol.setup.protocolInstrumentConfig" eventId="protocolInstrumentConfig__add" component="${component}" parameters="param,${visitId}" locked="${currentPatient.locked}"/>   
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

<c:forEach items="${command.components[component].children}" var="protocolInstrumentConfig" varStatus="instrumentIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolInstrumentConfig" component="protocolInstrumentConfig" idParam="${protocolInstrumentConfig.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell>
		<tags:createField property="children[${instrumentIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
	</tags:listCell>
	<tags:listCell>Instrument</tags:listCell>
	<tags:listCell><tags:createField property="children[${instrumentIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${instrumentIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="children[${instrumentIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
</tags:listRow>

		<c:forEach items="${protocolInstrumentConfig.options}" var="protocolInstrumentOptionConfig" varStatus="instrumentOptionIterator">
		<tags:listRow>
			<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolInstrumentOptionConfig" component="protocolInstrumentOptionConfig" idParam="${protocolInstrumentOptionConfig.id}" locked="${item.locked}"/></tags:listCell>
			<tags:listCell>
				<c:forEach begin="1" end="3">&nbsp;</c:forEach>
				<tags:createField property="children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
			</tags:listCell>
			<tags:listCell>Instrument Option</tags:listCell>
			<tags:listCell><tags:createField property="children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
			<tags:listCell><tags:createField property="children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
			<tags:listCell><tags:createField property="children[${instrumentIterator.index}].options[${instrumentOptionIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
		</tags:listRow>
		</c:forEach>

</c:forEach>
	
</tags:tableForm>  
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">visitOptionConfig</page:param>
  <page:param name="sectionNameKey">protocol.visitOptionConfig.section</page:param>
<div class="verticalSpace10">&nbsp;</div>
<tags:actionURLButton buttonText="Add Option"  actionId="lava.crms.protocol.setup.protocolVisitOptionConfig" eventId="protocolVisitOptionConfig__add" component="${component}" parameters="param,${visitId}" locked="${currentPatient.locked}"/>   
<div class="verticalSpace10">&nbsp;</div>
<tags:tableForm>  
<tags:listRow>
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Option" width="21%"/>
	<tags:listColumnHeader label="Effective Date" width="23%" />
	<tags:listColumnHeader label="Expiration Date" width="23%" />
	<tags:listColumnHeader label="Notes" width="23%" />
</tags:listRow>

<c:forEach items="${command.components[component].options}" var="protocolVisitOptionConfig" varStatus="visitOptionIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolVisitOptionConfig" component="protocolVisitOptionConfig" idParam="${protocolVisitOptionConfig.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell><tags:createField property="options[${visitOptionIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="options[${visitOptionIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="options[${visitOptionIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="options[${visitOptionIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
</tags:listRow>	
</c:forEach>
</tags:tableForm> 
</page:applyDecorator>

</c:if>

</page:applyDecorator>    


