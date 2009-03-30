<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="lavaSession"/>

<c:set var="formattedUserName"><tags:componentProperty component="${component}" property="username"/></c:set>
<c:set var="formattedUserName" value="${fn:replace(formattedUserName,',',' ')}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="quicklinks"></page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="currentStatus"/>,${formattedUserName},<tags:componentProperty component="${component}" property="hostname"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  
	<c:import url="/WEB-INF/jsp/core/admin/session/lavaSessionContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>
</page:applyDecorator>    
</page:applyDecorator>	    

