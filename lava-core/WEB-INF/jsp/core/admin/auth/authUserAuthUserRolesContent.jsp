<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="parentComponent">${param.component}</c:set>
<c:set var="parentComponentId"><tags:componentProperty component="${parentComponent}" property="id"/></c:set>
<c:set var="pageName">${param.component}</c:set>
<c:set var="parentComponentView">${param.componentView}</c:set>
<c:set var="component" value="authUserAuthUserRoles"/>

<page:applyDecorator name="component.list.content">
	<page:param name="pageName">${pageName}</page:param>
	<page:param name="component">${component}</page:param>
	<page:param name="listTitle">Authorization Roles Assigned to User</page:param>
	<page:param name="isSecondary">true</page:param>



<c:if test="${parentComponentView == 'view'}">
	<content tag="customActions">
		<tags:actionURLButton buttonText="Add Role"  actionId="lava.core.admin.auth.authUserRole" eventId="authUserRole__add" component="${component}" parameters="uid,${parentComponentId}"/>
	</content>
</c:if>
<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Role / Context" width="40%" sort="role.roleName"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Role Granted To" width="52%"/>

</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		
		<tags:ifComponentPropertyEmpty component="${component}" property="user" listIndex="${iterator.index}">
			<tags:listCell styleClass="actionButton">
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="role.roleName" component="${component}" listIndex="${iterator.index}" entityType="authUserRole" metadataName="authRole.roleName"/>
					<tags:listActionURLButton buttonImage="view" actionId="lava.core.admin.auth.authRole" eventId="authRole__view" idParam="${item.role.id}"/>/
				<tags:listField property="summaryInfo" component="${component}" listIndex="${iterator.index}" entityType="authUserRole"/>
			</tags:listCell>	
			<tags:listCell>	
				<tags:outputText textKey="authUser.grantedToGroup.label" inline="true" styleClass="bold"/>
				<tags:listField property="group.groupNameWithStatus" component="${component}" listIndex="${iterator.index}" entityType="authUserRole" metadataName="authGroup.groupNameWithStatus"/>
			</tags:listCell>
		</tags:ifComponentPropertyEmpty>
		<tags:ifComponentPropertyNotEmpty component="${component}" property="user" listIndex="${iterator.index}">
			<tags:listCell styleClass="actionButton">
				<c:if test="${parentComponentView == 'view'}">
					<tags:listActionURLStandardButtons actionId="lava.admin.auth.authUserRole" component="authUserRole" idParam="${item.id}"/>    	                      
				</c:if>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="role.roleName" component="${component}" listIndex="${iterator.index}" entityType="authUserRole" metadataName="authRole.roleName"/>
					<tags:listActionURLButton buttonImage="view" actionId="lava.admin.auth.authRole" eventId="authRole__view" idParam="${item.role.id}"/>/
					<tags:listField property="summaryInfo" component="${component}" listIndex="${iterator.index}" entityType="authUserRole"/>
				</tags:listCell>	
			<tags:listCell>
				<tags:outputText textKey="authUser.grantedToUser.label" inline="true" styleClass="bold"/>
			</tags:listCell>
		</tags:ifComponentPropertyNotEmpty>
		
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
    

