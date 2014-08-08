<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionNameKey">lavaFile.fileInfo.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
<tags:createField property="name" entityType="lavaFile" component="${component}"/>
<tags:createField property="fileType" entityType="lavaFile" component="${component}"/>
<tags:createField property="contentType" entityType="crmsFile" component="${component}"/>
<%-- debugging only
<tags:createField property="checksum" entityType="lavaFile" component="${component}"/>
--%>
</page:applyDecorator>   

<%-- debugging only
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionNameKey">lavaFile.repositoryInfo.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
<tags:createField property="repositoryId" entityType="lavaFile" component="${component}"/>
<tags:createField property="fileId" entityType="lavaFile" component="${component}"/>
<tags:createField property="location" entityType="lavaFile" component="${component}"/>
</page:applyDecorator>   
--%>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionNameKey">lavaFile.status.section</page:param>
<tags:createField property="fileStatusBy" entityType="lavaFile"  component="${component}"/>
<tags:createField property="fileStatusDate" entityType="lavaFile"  component="${component}"/>
<tags:createField property="fileStatus" entityType="lavaFile" component="${component}"/>
</page:applyDecorator>

