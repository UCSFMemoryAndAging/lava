<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="authGroups"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs"></page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
	<tags:actionURLButton buttonText="Add Group"  actionId="lava.core.admin.auth.authGroup" eventId="authGroup__add" component="${component}"/>	    
</content>





<content tag="listColumns">
<tags:listRow>


<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Group Name" width="30%" sort="userName" />
<tags:componentListColumnHeader component="${component}" label="Effective/<br/>Expiration" width="12%" sort="effectiveDate"/>
<tags:componentListColumnHeader component="${component}" label="Notes" width="50%"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLStandardButtons actionId="lava.core.admin.auth.authGroup" component="authGroup" idParam="${item.id}"/>	    
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="groupNameWithStatus" component="${component}" listIndex="${iterator.index}" entityType="authGroup"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="effectiveDate" component="${component}" listIndex="${iterator.index}" entityType="authGroup"/><br/>
			<tags:listField property="expirationDate" component="${component}" listIndex="${iterator.index}" entityType="authGroup"/><br/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="notes" component="${component}" listIndex="${iterator.index}" entityType="authGroup"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

