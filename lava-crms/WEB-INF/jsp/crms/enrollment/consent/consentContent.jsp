<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">ConsentDetails</page:param>
  <page:param name="sectionNameKey">consent.consentDetails.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="id" component="${component}"/>
<tags:createField property="patient.fullNameRevNoSuffix" component="${component}" entityType="patient" metadataName="patient.fullNameNoSuffix"/>
<tags:createField property="projName" component="${component}"/>
<tags:createField property="consentType" component="${component}"/>
<tags:createField property="hipaa" component="${component}"/>
<tags:createField property="consentRevision" component="${component}"/>
<tags:createField property="consentDeclined" component="${component}"/>
<tags:createField property="caregiverId" component="${component}"/>
<tags:createField property="consentDate" component="${component}"/>
<tags:createField property="expirationDate" component="${component}"/>
<tags:createField property="withdrawlDate" component="${component}"/>
<tags:createField property="capacityReviewBy" component="${component}"/>
<tags:createField property="note" component="${component}"/>
</page:applyDecorator>

<ui:formGuide ignoreDoOnLoad="true">
    <ui:observe elementIds="consent_projName" forValue=".+"/>
    <ui:setValue elementIds="consent_consentType" value=""/>
    <ui:setValue elementIds="consent_capacityReviewBy" value=""/>
    <ui:submitForm form="consent" event="consent__reRender"/>
</ui:formGuide>






      
