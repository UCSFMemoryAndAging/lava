<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>


<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">ConsentDetails</page:param>
  <page:param name="sectionNameKey">consent.consentDetails.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="id" component="${component}"/>
<tags:createField property="patient.fullNameRevNoSuffix" component="${component}" entityType="patient" metadataName="patient.fullNameNoSuffix"/>
<tags:createField property="projName" component="${component}"/>
<tags:createField property="consentType" component="${component}"/>
<tags:createField property="consentDeclined" component="${component}"/>
<tags:createField property="consentRevision" component="${component}"/>
<tags:createField property="caregiverId" component="${component}"/>
<tags:createField property="consentDate" component="${component}"/>
<tags:createField property="expirationDate" component="${component}"/>
<tags:createField property="withdrawlDate" component="${component}"/>
<tags:createField property="capacityReviewBy" component="${component}"/>
<tags:createField property="note" component="${component}"/>

</page:applyDecorator>
</tags:contentColumn>

<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">ConsentTo</page:param>
  <page:param name="sectionNameKey">consent.consentTo.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="research" component="${component}"/>
<tags:createField property="neuro" component="${component}"/>
<tags:createField property="dna" component="${component}"/>
<tags:createField property="genetic" component="${component}"/>
<tags:createField property="geneticShare" component="${component}"/>
<tags:createField property="lumbar" component="${component}"/>
<tags:createField property="video" component="${component}"/>
<tags:createField property="audio" component="${component}"/>
<tags:createField property="mediaEdu" component="${component}"/>
<tags:createField property="t1_5mri" component="${component}"/>
<tags:createField property="t4mri" component="${component}"/>
<tags:createField property="otherStudy" component="${component}"/>
<tags:createField property="followup" component="${component}"/>
<tags:createField property="music" component="${component}"/>
<tags:createField property="part" component="${component}"/>
<tags:createField property="carepart" component="${component}"/>

</page:applyDecorator>  
</tags:contentColumn>


<ui:formGuide ignoreDoOnLoad="true">
    <ui:observe elementIds="consent_projName" forValue=".+"/>
    <ui:setValue elementIds="consent_consentType" value=""/>
    <ui:setValue elementIds="consent_capacityReviewBy" value=""/>
    <ui:submitForm form="consent" event="consent__reRender"/>
</ui:formGuide>






      
