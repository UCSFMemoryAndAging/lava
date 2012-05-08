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
  <page:param name="pageHeadingArgs">${instrumentConfigLabel},${protocolConfigLabel},<tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/></page:param>
  <page:param name="quicklinks">assignInstr,compStatusSection,collectStatus,collectWin,idealCollectWin</page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<c:if test="${componentView != 'add'}">
<tags:createField property="id" component="${component}"/>
</c:if>
<tags:createField property="protocolInstrumentConfig.label" component="${component}" metadataName="protocolInstrumentConfig.label"/>
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
<c:if test="${componentView == 'edit'}">
	<c:set var="instrId">
		<tags:componentProperty component="${component}" property="instrId"/>
	</c:set>
	<c:if test="${not empty instrId}">
		<c:set var="instrTypeEncoded">
			<tags:componentProperty component="${component}" property="instrument" property2="instrTypeEncoded"/>
		</c:set>
		<c:set var="instrLink">
			<tags:eventActionButton buttonImage="view" component="${instrTypeEncoded}" action="view" pageName="${component}" parameters="id,${instrId}" javascript="submitted=true;"/>
		</c:set>
	</c:if>
</c:if>	
<tags:createField property="instrId" component="${component}" link="${instrLink}"/>
<c:if test="${componentView == 'edit'}">
<tags:eventButton buttonText="Add Instrument" component="instrument" action="add"  pageName="${component}" javascript="submitted=true;"/>
</c:if>
</page:applyDecorator>

<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">compStatusSection</page:param>
  <page:param name="sectionNameKey">${component}.compStatusSection.section</page:param>
<tags:createField property="compStatusOverride" component="${component}"/>
<tags:createField property="compStatus" component="${component}"/>
<tags:createField property="compStatusComputed" component="${component}"/>
<tags:createField property="compReason" component="${component}"/>
<tags:createField property="compNote" component="${component}"/>
</page:applyDecorator>
</tags:contentColumn>

<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">collectStatus</page:param>
  <page:param name="sectionNameKey">${component}.collectStatus.section</page:param>
<tags:createField property="collectWinStatus" component="${component}"/>
<tags:createField property="collectWinReason" component="${component}"/>
<tags:createField property="collectWinNote" component="${component}"/>
</page:applyDecorator>
</tags:contentColumn>


<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">collectWin</page:param>
  <page:param name="sectionNameKey">${component}.collectWin.section</page:param>
<tags:createField property="instrCollectWinAnchorDate" component="${component}"/>
<tags:createField property="instrCollectWinStart" component="${component}"/>
<tags:createField property="instrCollectWinEnd" component="${component}"/>
</page:applyDecorator>
</tags:contentColumn>

<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">idealCollectWin</page:param>
  <page:param name="sectionNameKey">${component}.idealCollectWin.section</page:param>
<tags:createField property="idealInstrCollectWinAnchorDate" component="${component}"/>
<tags:createField property="idealInstrCollectWinStart" component="${component}"/>
<tags:createField property="idealInstrCollectWinEnd" component="${component}"/>
</page:applyDecorator>
</tags:contentColumn>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<tags:createField property="notes" component="${component}"/>
</page:applyDecorator>

</page:applyDecorator>    

<c:if test="${componentView != 'view'}">

<ui:formGuide simulateEvents="true">
	<ui:observeForNull elementIds="compStatusOverride" component="${component}"/>
	<ui:disable elementIds="compStatus" component="${component}"/>
</ui:formGuide>

</c:if>

</page:applyDecorator>    


