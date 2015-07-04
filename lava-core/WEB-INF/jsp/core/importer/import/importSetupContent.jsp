<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">setup</page:param>
  <page:param name="sectionNameKey">import.setup.section</page:param>
  <page:param name="instructions"><spring:message code="import.instructions"/></page:param>
	<%-- if this is an instance customization action the component is 'crmsImport' but if not it
		is 'import' so explicitly use entityType to get 'import' metadata to cover all cases --%>
	<tags:createField property="definitionId" component="${component}" entityType="import"/>
	<c:if test="${componentMode != 'vw'}">
		<tags:fileUpload paramName="uploadFile"  component="${component}"/>
	</c:if>	
	<div class="verticalSpace10">&nbsp;</div>
	<tags:createField property="notes" component="${component}" entityType="import"/>
</page:applyDecorator>

