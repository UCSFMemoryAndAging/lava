<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>

<c:set var="reports"> 
	<decorator:getProperty property="page.reports"/>
</c:set>	

<c:if test="${reports != 'none'}">
<div class="leftNavSectionHeader">
	Reports
</div>
  <decorator:body/>
</c:if>
