<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<tags:contentColumn columnClass="colLeft2Col5050">

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">caregiver</page:param>
  <page:param name="sectionNameKey">caregiver.caregiver.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="title" component="${component}"/>
<tags:createField property="firstName" component="${component}"/>
<tags:createField property="lastName" component="${component}"/>
<tags:createField property="relation" component="${component}"/>
<tags:createField property="active" component="${component}"/>

<tags:createField property="birthDate" component="${component}"/>
<tags:createField property="gender" component="${component}"/>
<tags:createField property="education" component="${component}"/>
<tags:createField property="race" component="${component}"/>
<tags:createField property="maritalStatus" component="${component}"/>
<tags:createField property="occupation" component="${component}"/>
<tags:createField property="age" component="${component}"/>
</page:applyDecorator>  
 

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">language</page:param>
  <page:param name="sectionNameKey">caregiver.language.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="primaryLanguage" component="${component}"/>
<tags:createField property="transNeeded" component="${component}"/>
<tags:createField property="transLanguage" component="${component}"/>
</page:applyDecorator>
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">roles</page:param>
  <page:param name="sectionNameKey">caregiver.roles.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="livesWithPatient" component="${component}"/>
<tags:createField property="isPrimaryContact" component="${component}"/>
<tags:createField property="isContact" component="${component}"/>
<tags:createField property="isContactNotes" component="${component}"/>
<tags:createField property="isCaregiver" component="${component}"/>
<tags:createField property="isInformant" component="${component}"/>
<tags:createField property="isNextOfKin" component="${component}"/>
<tags:createField property="isResearchSurrogate" component="${component}"/>
<tags:createField property="isPowerOfAttorney" component="${component}"/>
<tags:createField property="isOtherRole" component="${component}"/>
<tags:createField property="otherRoleDesc" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">notes</page:param>
  <page:param name="sectionNameKey">caregiver.notes.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="note" component="${component}"/>
</page:applyDecorator>   
</tags:contentColumn>



<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>

<c:if test="${componentMode != 'vw'}">
<ui:formGuide>
    <ui:observe elementIds="${component}_isContact" forValue="[^1]|^$"/>
   	<ui:disable elementIds="${component}_isContactNotes"/>
   	<ui:setValue elementIds="${component}_isContactNotes" value=""/>
</ui:formGuide>
<ui:formGuide>
    <ui:observe elementIds="${component}_isOtherRole" forValue="[^1]|^$"/>
   	<ui:disable elementIds="${component}_otherRoleDesc"/>
   	<ui:setValue elementIds="${component}_otherRoleDesc" value=""/>
</ui:formGuide>

<ui:formGuide simulateEvents="true">
    <ui:observe elementIds="${component}_transNeeded" forValue="[^1]|^$"/>
   	<ui:disable elementIds="${component}_transLanguage"/>
   	<ui:setValue elementIds="${component}_transLanguage" value=""/>
</ui:formGuide>
</c:if>
