<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>

<%-- conditionally display the Reports section. the module.section.reports.jsp controls 
this by setting a 'reports' content tag to 'none', or not --%>

<c:set var="reports"> 
	<decorator:getProperty property="page.reports"/>
</c:set>	

<c:if test="${reports != 'none'}">
<div class="leftNavSectionHeader">
	Reports
</div>
  <decorator:body/>
</c:if>
