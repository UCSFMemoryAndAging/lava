<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- ifHasRole
	
	 only do body of tag if one of the current user's effective roles matches
	 one of the roles passed in. 
--%>
<%@ attribute name="roles" required="true" 
       description="comma separated list of roles" %>

<c:set var="hasRole" value="false"/>

<c:forEach items="${currentUser.effectiveRoles}" var="authUserRole" varStatus="authUserRoleStatus">
	<c:forTokens items="${roles}" delims="," var="roleName" varStatus="roleStatus">
		<c:if test="${authUserRole.role.roleName == roleName}">
			<c:set var="hasRole" value="true"/>
		</c:if>
	</c:forTokens>
 
</c:forEach>
<c:if test="${hasRole == true}">
 	<jsp:doBody/>
</c:if>  