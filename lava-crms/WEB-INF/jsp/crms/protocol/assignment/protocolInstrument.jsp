<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolInstrument"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
   
<c:set var="instrumentConfigLabel">
	<tags:componentProperty component="${component}" property="instrumentConfig" property2="label"/>
</c:set>   
   
<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField"></page:param>  
  <page:param name="pageHeadingArgs">${protocolConfigLabel},${instrumentConfigLabel},<tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/></page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<c:if test="${componentView != 'add'}">
<tags:createField property="id" component="${component}"/>
</c:if>
<tags:createField property="instrumentConfig.label" component="${component}" metadataName="protocolConfig.label"/>
<tags:createField property="instrId" component="${component}"/>
<tags:createField property="currStatus" component="${component}" entityType="protocol"/>
<tags:createField property="currReason" component="${component}" entityType="protocol"/>
<tags:createField property="currNote" component="${component}" entityType="protocol"/>
<tags:createField property="collectWinStatus" component="${component}"/>
<tags:createField property="collectWinReason" component="${component}"/>
<tags:createField property="collectWinNote" component="${component}"/>
<tags:createField property="customCollectWinStart" component="${component}"/>
<tags:createField property="customCollectWinEnd" component="${component}"/>
<tags:createField property="notes" component="${component}"/>

</page:applyDecorator>

</page:applyDecorator>    

</page:applyDecorator>    


