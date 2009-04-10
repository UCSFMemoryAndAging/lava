<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>

<%-- propagate the flag up to the reports.jsp decorator --%>
<content tag="reports">
	<decorator:getProperty property="page.reports"/>
</content>

<div class="leftNavSection">
  <decorator:body/>
</div>
