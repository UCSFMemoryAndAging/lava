<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>


<page:applyDecorator name="component.entity.section">
  	<page:param name="sectionNameKey">attachment.linking.section</page:param>
	
<tags:ifComponentPropertyEmpty property="patient" component="${component}">
	<tags:createField property="pidn"  entityType="" component="${component}"/>
</tags:ifComponentPropertyEmpty>
<tags:ifComponentPropertyNotEmpty property="patient" component="${component}">
	<tags:createField property="patient.fullNameRevNoSuffix" entityType="crmsFile" component="${component}"/>
	<tags:createField property="enrollStatId" entityType="crmsFile" component="${component}"/>
	<tags:createField property="visitId" entityType="crmsFile" component="${component}"/>
	<tags:createField property="instrId" entityType="crmsFile" component="${component}"/>

</tags:ifComponentPropertyNotEmpty>	
 
</page:applyDecorator>  

<c:if test="${componentView != 'view' && componentView != 'delete'}">
<tags:ifComponentPropertyEmpty property="location" component="${component}">
	<page:applyDecorator name="component.entity.section">
  		<page:param name="sectionNameKey">lavaFile.upload.section</page:param>
		<tags:fileUpload paramName="uploadFile"  component="${component}"/>
	</page:applyDecorator>
</tags:ifComponentPropertyEmpty>
</c:if>

<c:import url="/WEB-INF/jsp/crms/attachments/attachmentCommonContent.jsp">
	<c:param name="component">${component}</c:param>
</c:import>


<c:if test="${componentView != 'view' && componentView != 'delete'}">

<tags:ifComponentPropertyEmpty property="patient" component="${component}">
	<ui:formGuide ignoreDoOnLoad="true" ignoreUndoOnLoad="true">
    <ui:observe elementIds="pidn" forValue=".+" component="${component}"/>
    <ui:submitForm form="${component}" event="${component}__reRender" />
   </ui:formGuide>
   
</tags:ifComponentPropertyEmpty>
 
<tags:ifComponentPropertyNotEmpty property="patient" component="${component}">

	


<!-- cascade changes and rerender lists -->
<ui:formGuide ignoreDoOnLoad="true" ignoreUndoOnLoad="true">
    <ui:observe elementIds="enrollStatId" forValue=".+" component="${component}"/>
    <ui:setValue elementIds="visitId" value="" component="${component}"/>
    <ui:setValue elementIds="instrId" value="" component="${component}"/>
    <ui:submitForm form="${component}" event="${component}__reRender" />
</ui:formGuide>

<ui:formGuide ignoreDoOnLoad="true" ignoreUndoOnLoad="true">
    <ui:observe elementIds="visitId" forValue=".+" component="${component}"/>
    <ui:setValue elementIds="instrId" value="" component="${component}"/>
   <ui:submitForm form="${component}" event="${component}__reRender" />
</ui:formGuide>

<!-- disable /enable controls as more specific linking is defined -->
<ui:formGuide >
    <ui:depends elementIds="visitId,instrId" component="${component}"/>
    <ui:observe elementIds="enrollStatId" forValue=".+" component="${component}"/>
    <ui:enable elementIds="visitId" component="${component}"/>
</ui:formGuide>


<ui:formGuide >
    <ui:depends elementIds="instrId,enrollStatId" component="${component}"/>
    <ui:observe elementIds="visitId" forValue=".+" component="${component}"/>
    <ui:enable elementIds="instrId" component="${component}"/>
    <ui:disable elementIds="enrollStatId" component="${component}"/>
</ui:formGuide>

<ui:formGuide ignoreUndo="true" ignoreUndoOnLoad="true">
    <ui:depends elementIds="enrollStatId,vid" component="${component}"/>
    <ui:observe elementIds="instrId" forValue=".+" component="${component}"/>
    <ui:disable elementIds="visitId" component="${component}"/>
    <ui:disable elementIds="enrollStatId" component="${component}"/>
</ui:formGuide>


<ui:formGuide ignoreUndo="true" ignoreUndoOnLoad="true">
    <ui:depends elementIds="instrId,visitId" component="${component}"/>
    <ui:observe elementIds="enrollStatId" forValue=".+" component="${component}"/>
    <ui:observe elementIds="instrId" forValue="^$" component="${component}"/>
    <ui:enable elementIds="visitId" component="${component}"/>
</ui:formGuide>

<ui:formGuide  simulateEvents="true" ignoreUndo="true" ignoreUndoOnLoad="true">
    <ui:depends elementIds="visitId,enrollStatId" component="${component}"/>
    <ui:observe elementIds="instrId" forValue=".+" component="${component}"/>
    <ui:disable elementIds="visitId" component="${component}"/>
    <ui:disable elementIds="enrollStatId" component="${component}"/>
</ui:formGuide>

</tags:ifComponentPropertyNotEmpty>



</c:if>
