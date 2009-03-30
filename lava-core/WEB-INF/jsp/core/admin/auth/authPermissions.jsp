<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="authPermissions"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs"></page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add Permission"  actionId="lava.core.admin.auth.authPermission" eventId="authPermission__add" component="${component}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>


<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Role" width="20%" sort="role.roleName"/>
<tags:componentListColumnHeader component="${component}" label="Permit<br/>Or Deny" width="10%" sort="permitDeny"/>
<tags:componentListColumnHeader component="${component}" label="Scope" width="10%" sort="scope"/>
<tags:componentListColumnHeader component="${component}" label="Module" width="12%" sort="module"/>
<tags:componentListColumnHeader component="${component}" label="Section" width="12%" sort="section"/>
<tags:componentListColumnHeader component="${component}" label="Target" width="12%" sort="target"/>
<tags:componentListColumnHeader component="${component}"  label="Mode/<br/>Event" width="12%" sort="mode"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.admin.auth.authPermission" component="authPermission" idParam="${item.id}"/>	    	                      
		</tags:listCell>
		
		<tags:listCell>
			<tags:listField property="role.roleName" component="${component}" listIndex="${iterator.index}" entityType="authPermission"/>
			<tags:listActionURLButton buttonImage="view" actionId="lava.admin.auth.authRole" eventId="authRole__view" idParam="${item.role.id}"/>	    	                      
			
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
</page:applyDecorator>	    

