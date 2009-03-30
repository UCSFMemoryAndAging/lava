<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="authRole"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="quicklinks">details,note</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="roleName"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
 
	<c:import url="/WEB-INF/jsp/crms/admin/auth/crmsAuthRoleContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>
	
	<c:if test="${componentView != 'add'}">
			<c:import url="/WEB-INF/jsp/core/admin/auth/authRoleAuthGroupsContent.jsp">
				<c:param name="component">${component}</c:param>
				<c:param name="componentView">${componentView}</c:param>
			</c:import>
			<c:import url="/WEB-INF/jsp/core/admin/auth/authRoleAuthUsersContent.jsp">
				<c:param name="component">${component}</c:param>
				<c:param name="componentView">${componentView}</c:param>
			</c:import>
			<c:import url="/WEB-INF/jsp/core/admin/auth/authRoleAuthPermissionsContent.jsp">
				<c:param name="component">${component}</c:param>
				<c:param name="componentView">${componentView}</c:param>
			</c:import>
	</c:if>
	
</page:applyDecorator>    
</page:applyDecorator>	    

