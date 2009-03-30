<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%--  lookup component view from the requestScope --%>
<%@ attribute name="component" required="true"
       description="the component to use" %>

<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
$componentView

