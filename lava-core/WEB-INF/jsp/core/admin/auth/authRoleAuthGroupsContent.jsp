<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="parentComponent">${param.component}</c:set>
<c:set var="parentComponentId"><tags:componentProperty component="${parentComponent}" property="id"/></c:set>
<c:set var="pageName">${param.component}</c:set>
<c:set var="parentComponentView">${param.componentView}</c:set>
<c:set var="component" value="authRoleAuthGroups"/>

<page:applyDecorator name="component.list.content">
	<page:param name="pageName">${pageName}</page:param>
	<page:param name="component">${component}</page:param>
	<page:param name="listTitle">Groups Associated With Role</page:param>
	<page:param name="isSecondary">true</page:param>



<c:if test="${parentComponentView == 'view'}">
	<content tag="customActions">
		<tags:actionURLButton buttonText="Add Group"  actionId="lava.core.admin.auth.authUserRole" eventId="authUserRole__add" component="${component}" parameters="roleId,${parentComponentId}"/>
	</content>
</c:if>
<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Group" width="25%" sort="group.groupName"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Summary" width="25%"/>
<tags:componentListColumnHeader component="${component}" pageName="${pageName}" label="Notes" width="42%" sort="notes"/>

</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		
			<tags:listCell styleClass="actionButton">
				<c:if test="${parentComponentView == 'view'}">
					<tags:listActionURLStandardButtons actionId="lava.core.admin.auth.authUserRole" component="authUserRole" idParam="${item.id}"/>	    	                      
				</c:if>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="group.groupNameWithStatus" component="${component}" listIndex="${iterator.index}" entityType="authUserRole" metadataName="group.groupNameWithStatus"/>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="summaryInfo" component="${component}" listIndex="${iterator.index}" entityType="authUserRole"/>
			</tags:listCell>
 			<tags:listCell>
			<tags:listField property="notes" component="${component}" listIndex="${iterator.index}" entityType="authUserRole"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
    

