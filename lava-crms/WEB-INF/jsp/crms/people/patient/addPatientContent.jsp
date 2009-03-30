<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="deidentified"><tags:componentProperty component="${component}" property="deidentified"/></c:set>


<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">idcore</page:param>
  <page:param name="sectionNameKey">addPatient.idcore.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>

	<tags:contentColumn columnClass="colLeft2Col5050">
	    <tags:createField property="deidentified" component="${component}"/>
		<c:choose>
			<c:when test="${deidentified == 'false' or empty deidentified}">	    
		  		<tags:createField property="patient.firstName" component="${component}"/>
	  			<tags:createField property="patient.middleInitial" component="${component}"/>
				<tags:createField property="patient.lastName" component="${component}"/>
				<tags:createField property="patient.suffix" component="${component}"/>
				<tags:createField property="patient.degree" component="${component}"/>
			</c:when>
			<c:otherwise>
				<tags:createField property="subjectId" component="${component}"/>
			</c:otherwise>
		</c:choose>
		<tags:createField property="patient.birthDate" component="${component}"/>
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">
	<tags:createField property="enrollmentStatus.projName" component="${component}"/>
	<tags:createField property="status" component="${component}"/>
	<tags:createField property="statusDate" component="${component}"/>
</tags:contentColumn>
</page:applyDecorator>    



<ui:formGuide ignoreDoOnLoad="true">
    <ui:observe elementIds="addPatient_enrollmentStatus_projName" forValue=".+"/>
    <ui:setValue elementIds="addPatient_status" value=""/>
    <ui:submitForm form="addPatient" event="addPatient__reRender"/>
</ui:formGuide>
  
<ui:formGuide observeAndOr="or" ignoreDoOnLoad="true">
    <ui:observe elementIds="addPatient_deidentified" forValue="1"/>
    <ui:observeForNull elementIds="addPatient_deidentified"/>
    <ui:submitForm form="addPatient" event="addPatient__reRender"/>
</ui:formGuide>



