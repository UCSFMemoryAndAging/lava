<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<%-- note that the 'importSetup' component is the primary component but the result
data is in the 'importResult' component--%>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">result</page:param>
  <page:param name="sectionNameKey">import.result.section</page:param>
</page:applyDecorator>

