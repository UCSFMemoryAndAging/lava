<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="protocolVisitOptionConfig"/>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
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
<tags:createField property="notes" component="${component}"/>

<tags:createField property="defaultOption" component="${component}"/>
<%-- since altProject has not had mode set on it because it is not the default component, pass in mode  --%>
<tags:createField property="project" component="altProject" metadataName="protocolVisitOptionConfig.project" mode="${componentMode}"/>
<tags:createField property="visitType" component="${component}"/>

<tags:createField property="effDate" component="${component}"/>
<tags:createField property="expDate" component="${component}"/>

</page:applyDecorator>

<c:if test="${componentMode != 'vw'}">
<ui:formGuide observeAndOr="or" ignoreDoOnLoad="true" simulateEvents="true">
    <ui:observe elementIds="altProject_project" forValue=".+"/>
    <ui:setValue elementIds="protocolVisitOptionConfig_visitType" value=""/>
    <ui:submitForm form="protocolVisitOptionConfig" event="protocolVisitOptionConfig__reRender"/>
</ui:formGuide>
</c:if>

</page:applyDecorator>    

</page:applyDecorator>    


