<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolInstrumentConfig"/>
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
<tags:createField property="optional" component="${component}"/>
<tags:createField property="notes" component="${component}"/>
<tags:createField property="effDate" component="${component}"/>
<tags:createField property="expDate" component="${component}"/>
</page:applyDecorator>

</page:applyDecorator>    


<c:if test="${componentView == 'view'}">

<%-- displaying the protocol tree as a list so set mode so properties are not editable --%>
<c:set var="fieldMode" value="lv"/>

<c:set var="instrumentId">
	<tags:componentProperty component="${component}" property="id"/>
</c:set>
<c:set var="component" value="instrumentOptionsConfig"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">instrumentOptionConfig</page:param>
  <page:param name="sectionNameKey">protocol.instrumentOptionConfig.section</page:param>
<div class="verticalSpace10">&nbsp;</div>
<tags:actionURLButton buttonText="Add Option"  actionId="lava.crms.protocol.setup.protocolInstrumentOptionConfig" eventId="protocolInstrumentOptionConfig__add" component="${component}" parameters="param,${instrumentId}" locked="${currentPatient.locked}"/>   
<div class="verticalSpace10">&nbsp;</div>
<tags:tableForm>  
<tags:listRow>
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Option" width="21%"/>
	<tags:listColumnHeader label="Effective Date" width="23%" />
	<tags:listColumnHeader label="Expiration Date" width="23%" />
	<tags:listColumnHeader label="Notes" width="23%" />
</tags:listRow>

<c:forEach items="${command.components[component].options}" var="protocolInstrumentOptionConfig" varStatus="instrumentOptionIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolInstrumentOptionConfig" component="protocolInstrumentOptionConfig" idParam="${protocolInstrumentOptionConfig.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell><tags:createField property="options[${instrumentOptionIterator.index}].label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="options[${instrumentOptionIterator.index}].effDate" component="${component}"  metadataName="protocolConfig.effDate" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="options[${instrumentOptionIterator.index}].expDate" component="${component}" metadataName="protocolConfig.expDate" mode="${fieldMode}"/></tags:listCell>
	<tags:listCell><tags:createField property="options[${instrumentOptionIterator.index}].notes" component="${component}" metadataName="protocolConfig.notes" mode="${fieldMode}"/></tags:listCell>
</tags:listRow>	
</c:forEach>
</tags:tableForm> 
</page:applyDecorator>
    
</c:if>

</page:applyDecorator>    


