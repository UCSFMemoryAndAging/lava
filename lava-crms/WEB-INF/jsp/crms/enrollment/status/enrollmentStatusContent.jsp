<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>



<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
<page:param name="sectionId">patientProject</page:param>
<page:param name="sectionNameKey">enrollmentStatus.patientProject.section</page:param>
<tags:createField property="patient.fullNameNoSuffix" component="${component}" entityType="patient" metadataName="patient.fullNameNoSuffix"/>
<tags:createField property="id" component="${component}"/>
<tags:createField property="projName" component="${component}"/>
<tags:createField property="subjectStudyId" component="${component}"/>
<tags:createField property="referralSource" component="${component}"/>
</page:applyDecorator>
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">latestStatus</page:param>
  <page:param name="sectionNameKey">enrollmentStatus.latestStatus.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="latestDesc" component="${component}"/>
<tags:createField property="latestDate" component="${component}"/>
<tags:createField property="latestNote" component="${component}"/>
</page:applyDecorator>
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">notes</page:param>
  <page:param name="sectionNameKey">enrollmentStatus.notes.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
	<tags:createField property="enrollmentNotes" component="${component}"/>
</page:applyDecorator>  
</tags:contentColumn>









      
