<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>

<a href="<tags:actionURL actionId="lava.core.admin.auth.authUsers"/>">
<spring:message code="action.lava.core.admin.auth.authUsers.list"/></a><br/>
<a href="<tags:actionURL actionId="lava.core.admin.auth.authUser"  startMode="add"/>">
<spring:message code="action.lava.core.admin.auth.authUser.add"/></a><br/>
<br/>
<a href="<tags:actionURL actionId="lava.core.admin.auth.authGroups"/>">
<spring:message code="action.lava.core.admin.auth.authGroups.list"/></a><br/>
<a href="<tags:actionURL actionId="lava.core.admin.auth.authGroup"  startMode="add"/>">
<spring:message code="action.lava.core.admin.auth.authGroup.add"/></a><br/>
<br/>
<a href="<tags:actionURL actionId="lava.core.admin.auth.authRoles"/>">
<spring:message code="action.lava.core.admin.auth.authRoles.list"/></a><br/>
<a href="<tags:actionURL actionId="lava.core.admin.auth.authRole"  startMode="add"/>">
<spring:message code="action.lava.core.admin.auth.authRole.add"/></a><br/>
<br/>
<a href="<tags:actionURL actionId="lava.core.admin.auth.authPermissions"/>">
<spring:message code="action.lava.core.admin.auth.authPermissions.list"/></a><br/>
<a href="<tags:actionURL actionId="lava.core.admin.auth.authPermission"  startMode="add"/>">
<spring:message code="action.lava.core.admin.auth.authPermission.add"/></a><br/>
</body>
</html>