<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>

<content tag="reports">
	<decorator:getProperty property="page.reports"/>
</content>

<div class="leftNavSection">
  <decorator:body/>
</div>
