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


