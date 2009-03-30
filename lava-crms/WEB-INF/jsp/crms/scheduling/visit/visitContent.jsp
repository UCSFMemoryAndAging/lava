<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="componentView">${param.componentView}</c:set>

<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">projectType</page:param>
  <page:param name="sectionNameKey">visit.projectType.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="id" component="${component}"/>
<tags:createField property="patient.fullNameNoSuffix" component="${component}" metadataName="patient.fullNameNoSuffix"/>
<tags:createField property="ageAtVisit" component="${component}"/>
<tags:createField property="projName" component="${component}"/>
<tags:createField property="visitType" component="${component}"/>
</page:applyDecorator>  
</tags:contentColumn>
 
<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">appointmentStatus</page:param>
  <page:param name="sectionNameKey">visit.appointmentStatus.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="visitLocation" component="${component}"/>
<tags:createField property="visitWith" component="${component}"/>
<tags:createField property="visitDate" component="${component}"/>
<tags:createField property="visitStatus" component="${component}"/>
</page:applyDecorator>  
</tags:contentColumn>

<c:if test="${componentView != 'addMany'}">

<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">notes</page:param>
  <page:param name="sectionNameKey">visit.notes.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="visitNote" component="${component}"/>
</page:applyDecorator>    
</tags:contentColumn>

</c:if>

<c:if test="${param.componentMode != 'vw'}">
<ui:formGuide observeAndOr="or" ignoreDoOnLoad="true" simulateEvents="true">
    <ui:observe elementIds="visit_projName" forValue=".+"/>
    <ui:setValue elementIds="visit_visitType" value=""/>
    <ui:setValue elementIds="visit_visitWith" value=""/>
    <ui:submitForm form="visit" event="visit__reRender"/>
</ui:formGuide>
</c:if>


