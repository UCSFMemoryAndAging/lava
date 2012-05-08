<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolVisit"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<c:set var="visitConfigLabel">
	<tags:componentProperty component="${component}" property="protocolVisitConfig" property2="label"/>
</c:set>   
   
<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField"></page:param>  
  <page:param name="pageHeadingArgs">${visitConfigLabel},${protocolConfigLabel},<tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/></page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<c:if test="${componentView != 'add'}">
<tags:createField property="id" component="${component}"/>
</c:if>
<tags:createField property="protocolVisitConfig.label" component="${component}" metadataName="protocolVisitConfig.label"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">assignVisit</page:param>
  <page:param name="sectionNameKey">${component}.assignVisit.section</page:param>
<c:if test="${componentView == 'edit'}">
<tags:outputText textKey="protocol.assignVisit" inline="false" styleClass="italic"/>
<c:forEach items="${visitConfigOptions}" var="option" varStatus="iterator">
<tags:listRow>
	<tags:listCell>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#8226;&nbsp;${option}</tags:listCell>
</tags:listRow>	  
</c:forEach>
<c:forEach items="${protocolVisit.options}" var="option" varStatus="iterator">
<tags:listRow>
	<tags:listCell>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#8226;&nbsp;${option.visitType}&nbsp;-&nbsp;${option.visitTypeProjName}&nbsp;(${option.effectiveEffDate}&nbsp;-&nbsp;-${option.effectiveExpDate})</tags:listCell>
</tags:listRow>	  
</c:forEach>
<div>&nbsp;</div>
<%-- would be good if could add a bulleted style to listCell so would not have to create structure to do the above
<c:forEach items="${command.components['visitConfigOptions']}" var="option" varStatus="iterator">
<tags:listRow>
	<tags:listCell><tags:createField property="[${iterator.index}].visitTypeProjName,[${iterator.index}].visitType" separator=" - " component="visitConfigOptions" metadataName="protocolVisitConfigOption.visitTypeInList" mode="vw"/></tags:listCell>
</tags:listRow>	  
</c:forEach>
--%>
</c:if>
<c:if test="${componentView == 'edit'}">
	<c:set var="visitId">
		<tags:componentProperty component="${component}" property="visitId"/>
	</c:set>
	<c:if test="${not empty visitId}">
		<c:set var="visitLink">   
			<tags:eventActionButton buttonImage="view" component="visit" action="view" pageName="${component}" parameters="id,${visitId}" javascript="submitted=true;"/>
		</c:set>
	</c:if>
</c:if>	
<tags:createField property="visitId" component="${component}" link="${visitLink}"/>
<c:if test="${componentView == 'edit'}">
<tags:eventButton buttonText="Add Visit" component="visit" action="add"  pageName="${component}" javascript="submitted=true;"/>
</c:if>
</page:applyDecorator>

<%-- not showing scheduling window here as that is at the timepoint level --%>
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">compStatusSection</page:param>
  <page:param name="sectionNameKey">${component}.compStatusSection.section</page:param>
<%-- show rolled up status as readonly --%>
<tags:createField property="compStatusOverride" component="${component}"/>
<tags:createField property="compStatus" component="${component}"/>
<tags:createField property="compStatusComputed" component="${component}"/>
</page:applyDecorator>

<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">schedStatus</page:param>
  <page:param name="sectionNameKey">${component}.schedStatus.section</page:param>
<%-- if allow admin user to edit status here, how to prevent status calcs from overwriting user override of status? --%>
<tags:createField property="schedWinStatus" component="${component}"/>
<tags:createField property="schedWinReason" component="${component}"/>
<tags:createField property="schedWinNote" component="${component}"/>
</page:applyDecorator>
</tags:contentColumn>

<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">collectStatus</page:param>
  <page:param name="sectionNameKey">${component}.collectStatus.section</page:param>
<%-- show rolled up status as readonly --%>
<tags:createField property="collectWinStatus" component="${component}"/>
</page:applyDecorator>
</tags:contentColumn>

</page:applyDecorator>    


<c:if test="${componentView == 'view'}">

<%-- displaying the protocol tree as a list so set mode so properties are not editable --%>
<c:set var="fieldMode" value="lv"/>

<c:set var="visitId">
	<tags:componentProperty component="${component}" property="id"/>
</c:set>
<c:set var="component" value="protocolVisitTree"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">visitTree</page:param>
  <page:param name="sectionNameKey">protocolVisit.visitTree.section</page:param>
  
<tags:tableForm> 

<tags:listRow>
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Protocol Component" width="26%"/>
	<tags:listColumnHeader label="Configuration" width="12%" />
	<tags:listColumnHeader label="Assignment" width="52%" />
</tags:listRow>

<c:forEach items="${command.components[component].children}" var="instrument" varStatus="instrumentIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolInstrument" component="protocolInstrument" idParam="${instrument.id}" noDelete="true" locked="${item.locked}"/></tags:listCell>
	<tags:listCell>
		<tags:createField property="children[${instrumentIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
	</tags:listCell>
	<tags:listCell>
		Instrument
		<tags:listActionURLButton buttonImage="view" actionId="lava.crms.protocol.setup.protocolInstrumentConfig" eventId="protocolInstrumentConfig__view" idParam="${instrument.config.id}"/>
	</tags:listCell>
	<tags:listCell>
		<tags:createField property="children[${instrumentIterator.index}].summary" component="${component}" metadataName="protocol.summary" mode="${fieldMode}"/>
		<tags:listActionURLButton buttonImage="view" actionId="lava.crms.assessment.instrument.${instrument.instrument.instrTypeEncoded}" eventId="${instrument.instrument.instrTypeEncoded}__view" idParam="${instrument.instrument.id}"/>
	</tags:listCell>
</tags:listRow>
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


