<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolInstrument"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
   
<c:set var="instrumentConfigLabel">
	<tags:componentProperty component="${component}" property="protocolInstrumentConfig" property2="label"/>
</c:set>   
   
<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField"></page:param>  
  <page:param name="pageHeadingArgs">${protocolConfigLabel},${instrumentConfigLabel},<tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/></page:param>
  <page:param name="quicklinks">assignInstr,currStatus,collectWin,collectStatus</page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<c:if test="${componentView != 'add'}">
<tags:createField property="id" component="${component}"/>
</c:if>
<tags:createField property="protocolInstrumentConfig.label" component="${component}" metadataName="protocolConfig.label"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">assignInstr</page:param>
  <page:param name="sectionNameKey">${component}.assignInstr.section</page:param>
<c:if test="${componentView == 'edit'}">
<tags:outputText textKey="protocol.assignInstrument" inline="false" styleClass="italic"/>
<c:forEach items="${instrConfigOptions}" var="option" varStatus="iterator">
<tags:listRow>
	<tags:listCell>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#8226;&nbsp;${option}</tags:listCell>
</tags:listRow>	  
</c:forEach>
<div>&nbsp;</div>
</c:if>
<tags:createField property="instrId" component="${component}"/>
<c:if test="${componentView == 'edit'}">
<tags:actionURLButton buttonText="Add Instrument"  actionId="lava.crms.assessment.instrument.instrument" eventId="instrument__add" component="${component}" locked="${currentPatient.locked}"/>
</c:if>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">currStatus</page:param>
  <page:param name="sectionNameKey">${component}.currStatus.section</page:param>
<tags:createField property="currStatus" component="${component}" entityType="protocol"/>
<tags:createField property="currReason" component="${component}" entityType="protocol"/>
<tags:createField property="currNote" component="${component}" entityType="protocol"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">collectWin</page:param>
  <page:param name="sectionNameKey">${component}.collectWin.section</page:param>
<tags:createField property="collectAnchorDate" component="${component}"/>
<tags:createField property="collectWinStart" component="${component}"/>
<tags:createField property="collectWinEnd" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">collectStatus</page:param>
  <page:param name="sectionNameKey">${component}.collectStatus.section</page:param>
<tags:createField property="collectWinStatus" component="${component}"/>
<tags:createField property="collectWinReason" component="${component}"/>
<tags:createField property="collectWinNote" component="${component}"/>
</page:applyDecorator>

<tags:createField property="notes" component="${component}"/>

</page:applyDecorator>    

</page:applyDecorator>    


