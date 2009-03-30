<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
 <tags:createField property="id" component="${component}"/>
 <tags:createField property="patient.fullNameNoSuffix" component="${component}" metadataName="patient.fullNameNoSuffix"/>
 <tags:createField property="projName" component="${component}"/>
 <tags:createField property="logDate" component="${component}"/>
</page:applyDecorator>  
</tags:contentColumn>

<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
<page:param name="sectionId">anonymous</page:param>
<tags:createField property="method" component="${component}"/>
<tags:createField property="staff" component="${component}"/>
<tags:createField property="contact" component="${component}"/>
<tags:createField property="staffInit"  component="${component}"/>
</page:applyDecorator>  
</tags:contentColumn>
<page:applyDecorator name="component.entity.section">
<page:param name="sectionId">anonymous</page:param>
<tags:createField property="note" component="${component}" labelAlignment="top"/>
</page:applyDecorator>


<ui:formGuide ignoreDoOnLoad="true">
    <ui:observe elementIds="contactLog_projName" forValue=".+"/>
    <ui:setValue elementIds="contactLog_staff" value=""/>
     <ui:submitForm form="contactLog" event="contactLog__reRender"/>
</ui:formGuide>

