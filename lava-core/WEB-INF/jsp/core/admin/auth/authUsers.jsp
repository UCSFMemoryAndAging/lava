<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="authUsers"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs"></page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
	<tags:actionURLButton buttonText="Add User"  actionId="lava.core.admin.auth.authUser" eventId="authUser__add" component="${component}"/>	    
</content>


<content tag="listFilters">
<tags:contentColumn columnClass="colLeft2Col5050">
	
	<tags:listFilterField property="userName" component="${component}" entityType="authUser"/>
	<tags:listFilterField property="login" component="${component}" entityType="authUser"/>
	<tags:listFilterField property="accessAgreementDateStart" component="${component}" entityType="authUser"/>
	<tags:listFilterField property="accessAgreementDateEnd" component="${component}" entityType="authUser"/>
	
</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
	
	<tags:listFilterField property="effectiveDateStart" component="${component}" entityType="authUser"/>
	<tags:listFilterField property="effectiveDateEnd" component="${component}" entityType="authUser"/>
	<tags:listFilterField property="expirationDateStart" component="${component}" entityType="authUser"/>
	<tags:listFilterField property="expirationDateEnd" component="${component}" entityType="authUser"/>
	
	
	</tags:contentColumn>
</content>



<content tag="listColumns">
<tags:listRow>


<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Name" width="20%" sort="userName" />
<tags:componentListColumnHeader component="${component}" label="Login" width="20%" sort="login"/>
<tags:componentListColumnHeader component="${component}" label="Auth Type" width="10%"/>
<tags:componentListColumnHeader component="${component}" label="Access Agreement" width="14%" sort="accessAgreementDate"/>
<tags:componentListColumnHeader component="${component}" label="Effective/<br/>Expiration" width="12%" sort="effectiveDate"/>
<tags:componentListColumnHeader component="${component}" label="Notes" width="20%"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLStandardButtons actionId="lava.core.admin.auth.authUser" component="authUser" idParam="${item.id}"/>	    
		</tags:listCell>
		
		<tags:listCell>
			<tags:listField property="userNameWithStatus" component="${component}" listIndex="${iterator.index}" entityType="authUser"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="login" component="${component}" listIndex="${iterator.index}" entityType="authUser"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="authenticationType" component="${component}" listIndex="${iterator.index}" entityType="authUser"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="accessAgreementDate" component="${component}" listIndex="${iterator.index}" entityType="authUser"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="effectiveDate" component="${component}" listIndex="${iterator.index}" entityType="authUser"/><br/>
			<tags:listField property="expirationDate" component="${component}" listIndex="${iterator.index}" entityType="authUser"/><br/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="notes" component="${component}" listIndex="${iterator.index}" entityType="authUser"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

