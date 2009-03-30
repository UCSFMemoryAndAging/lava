<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">add</page:param>
  <page:param name="sectionNameKey">add.addEnrollmentStatus.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="patient.fullNameRevNoSuffix" component="${component}" metadataName="patient.fullNameNoSuffix"/>
<tags:createField property="enrollmentStatus.projName" component="${component}"/>
<tags:createField property="status" component="${component}"/>
<tags:createField property="statusDate" component="${component}"/>
</page:applyDecorator>    

<ui:formGuide ignoreDoOnLoad="true">
    <ui:observe elementIds="addEnrollmentStatus_enrollmentStatus_projName" forValue=".+"/>
    <ui:setValue elementIds="addEnrollmentStatus_status" value=""/>
    <ui:submitForm form="addEnrollmentStatus" event="addEnrollmentStatus__reRender"/>
</ui:formGuide>
  




