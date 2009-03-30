<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="authUserRole"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<c:set var="pageHeadingParam1">[Select User or Group]</c:set>
<c:set var="pageHeadingParam2">[Select Role]</c:set>
<tags:ifComponentPropertyNotEmpty component="${component}" property="group">
	<c:set var="pageHeadingParam1">Group '<tags:componentProperty component="${component}" property="group" property2="groupNameWithStatus"/>'</c:set>
</tags:ifComponentPropertyNotEmpty>
<tags:ifComponentPropertyNotEmpty component="${component}" property="user">
	<c:set var="pageHeadingParam1">User '<tags:componentProperty component="${component}" property="user" property2="userNameWithStatus"/>'</c:set>
</tags:ifComponentPropertyNotEmpty>
<tags:ifComponentPropertyNotEmpty component="${component}" property="role">
	<c:set var="pageHeadingParam2">Role '<tags:componentProperty component="${component}" property="role" property2="roleName"/>'</c:set>
</tags:ifComponentPropertyNotEmpty>


<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="quicklinks">details,note</page:param>
  <page:param name="pageHeadingArgs">${pageHeadingParam1},${pageHeadingParam2}</page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
 
	<c:import url="/WEB-INF/jsp/crms/admin/auth/crmsAuthUserRoleContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>
	
</page:applyDecorator>    
</page:applyDecorator>	    

