<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="componentView">${param.componentView}</c:set>

<c:if test="${componentView == 'addMany'}">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="sectionNameKey"> </page:param>
<tags:contentColumn columnClass="colLeft2Col5050">
<tags:createField property="visit.id" component="${component}" context="i" fieldId="${component}_visit_id"/>
<tags:createField property="instrType" component="${component}" context="i"/>
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">
<tags:createField property="dcDate" component="${component}"/>
<tags:createField property="dcStatus" component="${component}"/>
</tags:contentColumn>
</page:applyDecorator>    

<ui:formGuide ignoreDoOnLoad="true">
    <ui:observe elementIds="instrument_visit_id" forValue=".+"/>
    <ui:submitForm form="instrument" event="instrument__reRender"/>
</ui:formGuide>

</c:if>


<c:if test="${componentView == 'delete'}">

<tags:contentColumn columnClass="colLeft2Col5050">

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="instrument.dc.section"/></page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="dcBy" component="${component}"/>
<tags:createField property="dcDate" component="${component}"/>
<tags:createField property="dcStatus" component="${component}"/>
<tags:createField property="dcNotes" component="${component}" labelAlignment="top"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="instrument.dv.section"/></page:param>
  <page:param name="instructions"> </page:param>
 <!-- note the dv fields also appear on verifyContent.jsp -->
<tags:createField property="dvBy" component="${component}"/>
<tags:createField property="dvDate" component="${component}"/>
<tags:createField property="dvStatus" component="${component}"/>
<tags:createField property="dvNotes" component="${component}" labelAlignment="top"/>
</page:applyDecorator>

</tags:contentColumn>


<tags:contentColumn columnClass="colRight2Col5050">

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="instrument.de.section"/></page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="deBy" component="${component}"/>
<tags:createField property="deDate" component="${component}"/>
<tags:createField property="deStatus" component="${component}"/>
<tags:createField property="deNotes" component="${component}" labelAlignment="top"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="instrument.research.section"/></page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="researchStatus" component="${component}"/>
<tags:createField property="qualityIssue" component="${component}"/>
<tags:createField property="qualityIssue2" component="${component}"/>
<tags:createField property="qualityIssue3" component="${component}"/>
<tags:createField property="qualityNotes" component="${component}" labelAlignment="top"/>
</page:applyDecorator>

</tags:contentColumn>

</c:if>
