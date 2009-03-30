<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="authUserGroup"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="quicklinks">details,note</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="user" property2="userNameWithStatus"/>,<tags:componentProperty component="${component}" property="group" property2="groupNameWithStatus"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
 
	<c:import url="/WEB-INF/jsp/core/admin/auth/authUserGroupContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>
	
</page:applyDecorator>    
</page:applyDecorator>	    

