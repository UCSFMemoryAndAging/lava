<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="parentComponent">${param.component}</c:set>
<c:set var="parentComponentId"><tags:componentProperty component="${parentComponent}" property="id"/></c:set>
<c:set var="pageName">${param.component}</c:set>
<c:set var="parentComponentView">${param.componentView}</c:set>
<c:set var="component" value="authRoleAuthPermissions"/>

<page:applyDecorator name="component.list.content">
	<page:param name="pageName">${pageName}</page:param>
	<page:param name="component">${component}</page:param>
	<page:param name="listTitle">Permissions Assigned To Role</page:param>
	<page:param name="isSecondary">true</page:param>



<c:if test="${parentComponentView == 'view'}">
	<content tag="customActions">
		<tags:actionURLButton buttonText="Add Permission"  actionId="lava.core.auth.authPermission" eventId="authPermission__add" component="${component}" parameters="roleId,${parentComponentId}"/>
	</content>
</c:if>
<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Permit<br/>Or Deny" width="10%" sort="permitDeny"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Scope" width="10%" sort="scope"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Module" width="15%" sort="module"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Section" width="15%" sort="section"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Target" width="15%" sort="target"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Mode/<br/>Event" width="15%" sort="mode"/>

</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		
		<tags:listCell styleClass="actionButton">
			<c:if test="${parentComponentView == 'view'}">
				<tags:listActionURLStandardButtons actionId="lava.core.admin.auth.authPermission" component="authPermission" idParam="${item.id}"/>	    	                      
			</c:if>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="permitDeny" component="${component}" listIndex="${iterator.index}" entityType="authPermission"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="scope" component="${component}" listIndex="${iterator.index}" entityType="authPermission"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="module" component="${component}" listIndex="${iterator.index}" entityType="authPermission"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="section" component="${component}" listIndex="${iterator.index}" entityType="authPermission"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="target" component="${component}" listIndex="${iterator.index}" entityType="authPermission"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="mode" component="${component}" listIndex="${iterator.index}" entityType="authPermission"/>
		</tags:listCell>

	</tags:listRow>
</tags:list>

</page:applyDecorator>    
    

