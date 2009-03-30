<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
<c:set var="deidentified"><tags:componentProperty component="${component}" property="deidentified"/></c:set>


<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">idcore</page:param>
  <page:param name="sectionNameKey">patient.idcore.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="id" component="${component}"/>  
<tags:createField property="deidentified" component="${component}"/>
	
<c:choose>
	<%--If not deidentified then show fullname in view mode or name fields in edit mode --%>
	<c:when test="${deidentified == 'false' or empty deidentified}">	    
		<c:choose>
			<c:when test="${componentView == 'view'}">
				<tags:createField property="fullName" component="${component}"/>
			</c:when>
			<c:otherwise>	
				<tags:createField property="firstName" component="${component}"/>
	  			<tags:createField property="middleInitial" component="${component}"/>
				<tags:createField property="lastName" component="${component}"/>
				<tags:createField property="suffix" component="${component}"/>
				<tags:createField property="degree" component="${component}"/>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>  <%--Deidentified record so use subjectID--%>
			<tags:createField property="subjectId" component="${component}"/>
	</c:otherwise>
</c:choose>

<tags:createField property="gender" component="${component}"/>
<tags:createField property="birthDate" component="${component}"/>
<tags:createField property="age" component="${component}"/>
<tags:createField property="hand" component="${component}"/>
<tags:createField property="deceased" component="${component}"/>
<tags:createField property="deathDate" component="${component}"/>


</page:applyDecorator> 

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">recordManagement</page:param>
  <page:param name="sectionNameKey">patient.recordManagement.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="enterBy" component="${component}"/>
<tags:createField property="dupNameFlag" component="${component}"/>
</page:applyDecorator>

<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>


<c:if test="${componentMode != 'vw'}">
<ui:formGuide simulateEvents="true">
    <ui:observe elementIds="${component}_deceased" forValue="[^1]|^$"/>
   	<ui:disable elementIds="${component}_deathDate"/>
   	<ui:setValue elementIds="${component}_deathDate" value=""/>
</ui:formGuide>
<ui:formGuide observeAndOr="or" ignoreDoOnLoad="true">
    <ui:observe elementIds="${component}_deidentified" forValue="1"/>
    <ui:observeForNull elementIds="${component}_deidentified"/>
    <ui:submitForm form="${component}" event="${component}__reRender"/>
</ui:formGuide>
</c:if>   


