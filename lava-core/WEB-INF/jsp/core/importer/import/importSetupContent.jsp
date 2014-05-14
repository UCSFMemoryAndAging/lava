<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<%-- TODO: use the flow state to determine whether to display import setup (with an Import button)
or import results (with a Close button) --%>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">setup</page:param>
  <page:param name="sectionNameKey">import.setup.section</page:param>
	<tags:createField property="definitionId" component="${component}"/>
	<c:if test="${componentMode != 'vw'}">
		<tags:fileUpload paramName="uploadFile"  component="${component}"/>
	</c:if>	
	<div class="verticalSpace10">&nbsp;</div>
	<tags:createField property="notes" component="${component}"/>
</page:applyDecorator>

