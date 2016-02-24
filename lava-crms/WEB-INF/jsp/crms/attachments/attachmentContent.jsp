<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionNameKey">lavaFile.fileInfo.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
<tags:createField property="name" entityType="lavaFile" component="${component}"/>
<tags:createField property="filePath" entityType="lavaFile" component="${component}"/>
<tags:createField property="fileType" entityType="lavaFile" component="${component}"/>
<tags:outputText textKey="info.attachmentSearch" inline="false" styleClass="italic"/>
<tags:createField property="category" entityType="lavaFile" component="${component}"/>
<tags:outputText textKey="info.attachmentCategory" inline="false" styleClass="italic"/>

<tags:outputText textKey="info.attachmentProject" inline="false" styleClass="italic"/>
<%-- if adding an attachment where projName is not pre-populated, i.e. attaching
at the Patient level, then projName should be editable. Otherwise, the ProjName is dictated
by the specific entity to which the file is being attached and ProjName should be disabled.
Determine if at the Patient level by checking that all other id's are null  --%>
<c:set var="enrollStatId">
	<tags:componentProperty component="${component}" property="enrollStatId"/>
</c:set>	 
<c:set var="consentId">
	<tags:componentProperty component="${component}" property="consentId"/>
</c:set>	 
<c:set var="visitId">
	<tags:componentProperty component="${component}" property="visitId"/>
</c:set>	 
<c:set var="instrId">
	<tags:componentProperty component="${component}" property="instrId"/>
</c:set>
<c:choose>
	<c:when test="${empty enrollStatId && empty consentId && empty visitId && empty instrId}">
		<tags:createField property="projName" entityType="crmsFile" component="${component}"/>
	</c:when>
	<c:otherwise>
		<tags:createField property="projName" entityType="crmsFile" component="${component}" disable="true"/>
	</c:otherwise>
</c:choose>				
<tags:createField property="contentType" entityType="crmsFile" component="${component}"/>
<tags:outputText textKey="info.attachmentContentType" inline="false" styleClass="italic"/>
<%-- debugging only
<tags:createField property="checksum" entityType="lavaFile" component="${component}"/>
--%>
<tags:createField property="notes" entityType="lavaFile" component="${component}"/>
</page:applyDecorator>   

<c:if test="${componentView != 'view' && componentView != 'delete'}">
<tags:ifComponentPropertyEmpty property="location" component="${component}">
	<page:applyDecorator name="component.entity.section">
  		<page:param name="sectionNameKey">lavaFile.upload.section</page:param>
		<tags:fileUpload paramName="uploadFile"  component="${component}"/>
	</page:applyDecorator>
</tags:ifComponentPropertyEmpty>
</c:if>
<c:if test="${componentView == 'view'}">
<tags:ifComponentPropertyNotEmpty property="location" component="${component}">
	<content tag="customActions">
		<tags:eventButton buttonText="Download" action="download" component="${component}" pageName="${component}" className="pageLevelLeftmostButton" target="download"/>
	</content>
</tags:ifComponentPropertyNotEmpty>
</c:if>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionNameKey">lavaFile.status.section</page:param>
<tags:createField property="fileStatusBy" entityType="lavaFile"  component="${component}"/>
<tags:createField property="fileStatusDate" entityType="lavaFile"  component="${component}"/>
<tags:createField property="fileStatus" entityType="lavaFile" component="${component}"/>
</page:applyDecorator>

