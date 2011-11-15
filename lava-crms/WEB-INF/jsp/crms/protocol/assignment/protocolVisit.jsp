<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolVisit"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<c:set var="visitConfigLabel">
	<tags:componentProperty component="${component}" property="visitConfig" property2="label"/>
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
<tags:createField property="visitConfig.label" component="${component}" metadataName="protocolConfig.label"/>
<tags:createField property="visitId" component="${component}"/>
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
	<tags:listColumnHeader label="Protocol Component" width="25%"/>
	<tags:listColumnHeader label="Type" width="25%" />
	<tags:listColumnHeader label="Notes" width="40%" />
</tags:listRow>

<c:forEach items="${command.components[component].children}" var="instrument" varStatus="instrumentIterator">
<tags:listRow>
	<tags:listCell><tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolInstrument" component="protocolInstrument" idParam="${instrument.id}" locked="${item.locked}"/></tags:listCell>
	<tags:listCell>
		<c:forEach begin="1" end="16">&nbsp;</c:forEach>
		<tags:createField property="children[${instrumentIterator.index}].config.label" component="${component}"  metadataName="protocolConfig.label" mode="${fieldMode}"/>
	</tags:listCell>
	<tags:listCell>Instrument</tags:listCell>
	<tags:listCell><tags:createField property="children[${instrumentIterator.index}].notes" component="${component}" metadataName="protocol.notes" mode="${fieldMode}"/></tags:listCell>
</tags:listRow>
</c:forEach>
	
</tags:tableForm>  
</page:applyDecorator>

</c:if>

</page:applyDecorator>    


