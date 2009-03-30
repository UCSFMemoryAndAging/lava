<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="authRoles"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs"></page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
	<tags:actionURLButton buttonText="Add Role"  actionId="lava.core.admin.auth.authRole" eventId="authRole__add" component="${component}"/>	    
</content>





<content tag="listColumns">
<tags:listRow>


<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Name" width="30%" sort="userName" />
<tags:componentListColumnHeader component="${component}" label="Notes" width="62%"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLStandardButtons actionId="lava.core.admin.auth.authRole" component="authRole" idParam="${item.id}"/>	    
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="roleName" component="${component}" listIndex="${iterator.index}" entityType="authRole"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="notes" component="${component}" listIndex="${iterator.index}" entityType="authRole"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

