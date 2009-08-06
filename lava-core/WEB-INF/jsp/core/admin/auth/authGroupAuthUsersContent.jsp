<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="parentComponent">${param.component}</c:set>
<c:set var="parentComponentId"><tags:componentProperty component="${parentComponent}" property="id"/></c:set>
<c:set var="pageName">${param.component}</c:set>
<c:set var="parentComponentView">${param.componentView}</c:set>
<c:set var="component" value="authGroupAuthUsers"/>

<page:applyDecorator name="component.list.content">
	<page:param name="pageName">${pageName}</page:param>
	<page:param name="component">${component}</page:param>
	<page:param name="listTitle">Users Assigned to Group</page:param>
	<page:param name="isSecondary">true</page:param>



<c:if test="${parentComponentView == 'view'}">
	<content tag="customActions">
		<tags:actionURLButton buttonText="Add User"  actionId="lava.core.admin.auth.authUserGroup" eventId="authUserGroup__add" component="${component}" parameters="gid,${parentComponentId}"/>
	</content>
</c:if>
<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="User" width="30%" sort="user.userName"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Login" width="20%" sort="user.login"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Notes" width="42%" sort="notes"/>
</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<c:if test="${parentComponentView == 'view'}">
				<tags:listActionURLStandardButtons actionId="lava.core.admin.auth.authUserGroup" component="authUserGroup" idParam="${item.id}"/>	    	                      
			</c:if>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="user.userNameWithStatus" component="${component}" listIndex="${iterator.index}" entityType="authUserGroup" metadataName="authUser.userNameWithStatus"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="user.login" component="${component}" listIndex="${iterator.index}" entityType="authUserGroup" metadataName="user.login"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="notes" component="${component}" listIndex="${iterator.index}" entityType="authUserGroup"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
    

