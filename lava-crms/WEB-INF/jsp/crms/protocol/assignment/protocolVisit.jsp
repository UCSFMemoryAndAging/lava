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
  <page:param name="pageHeadingArgs">${protocolConfigLabel},${visitConfigLabel},<tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/></page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<c:if test="${componentView != 'add'}">
<tags:createField property="id" component="${component}"/>
</c:if>
<tags:createField property="protocolVisitConfig.label" component="${component}" metadataName="protocolConfig.label"/>
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
	<tags:listCell>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&#8226;&nbsp;${option.visitType}&nbsp;-&nbsp;${visitTypeProjName}&nbsp;(${option.effectiveEffDate}&nbsp;-&nbsp;-${option.effectiveExpDate})</tags:listCell>
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
<tags:createField property="visitId" component="${component}"/>
<c:if test="${componentView == 'edit'}">
<tags:eventButton buttonText="Add Visit" component="visit" action="add"  pageName="${component}" javascript="submitted=true;"/>
</c:if>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">status</page:param>
  <page:param name="sectionNameKey">${component}.status.section</page:param>
<tags:createField property="currStatus" component="${component}" entityType="protocol"/>
<tags:createField property="currReason" component="${component}" entityType="protocol"/>
<tags:createField property="currNote" component="${component}" entityType="protocol"/>
<tags:createField property="notes" component="${component}"/>
</page:applyDecorator>

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
  <page:param name="sectionNameKey">protocol.visitTree.section</page:param>
  
<tags:tableForm> 

<tags:listRow>
	<tags:listColumnHeader label="Action" width="10%"/>
	<tags:listColumnHeader label="Protocol Component" width="30%"/>
	<tags:listColumnHeader label="Configuration" width="12%" />
	<tags:listColumnHeader label="Assignment" width="48%" />
</tags:listRow>

<c:forEach items="${command.components[component].children}" var="instrument" varStatus="instrumentIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolInstrument" component="protocolInstrument" idParam="${instrument.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell>
		<tags:createField property="children[${instrumentIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
	</tags:listCell>
	<tags:listCell>
		Instrument
		<tags:listActionURLButton buttonImage="view" actionId="lava.crms.protocol.setup.protocolInstrumentConfig" eventId="protocolInstrumentConfig__view" idParam="${instrument.config.id}"/>
	</tags:listCell>
	<tags:listCell><tags:createField property="children[${instrumentIterator.index}].assignDescrip" component="${component}" metadataName="protocol.assignDescrip" mode="${fieldMode}"/></tags:listCell>
</tags:listRow>
</c:forEach>
	
</tags:tableForm>  
</page:applyDecorator>

</c:if>

</page:applyDecorator>    


