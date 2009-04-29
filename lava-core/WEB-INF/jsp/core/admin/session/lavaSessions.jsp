<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="lavaSessions"/>
<jsp:useBean id="now" class="java.util.Date" />
 
<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${lavaServerInstance.serverDescription}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
</content>

<content tag="listColumns">
<tags:listRow>

<tags:componentListColumnHeader component="${component}" label="Action" width="6%"/>
<tags:componentListColumnHeader component="${component}" label="Session ID" width="10%"/>
<tags:componentListColumnHeader component="${component}" label="Created" width="20%" sort="createTimestamp"/>
<tags:componentListColumnHeader component="${component}" label="Accessed" width="20%" sort="accessTimestamp"/>
<tags:componentListColumnHeader component="${component}" label="Username" width="13%" sort="username"/>
<tags:componentListColumnHeader component="${component}" label="Hostname" width="13%" sort="hostname"/>
<tags:componentListColumnHeader component="${component}" label="Status" width="18%" sort="currentStatus"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLButton buttonImage="view" actionId="lava.admin.session.lavaSession" eventId="lavaSession__view" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="edit" actionId="lava.admin.session.lavaSession" eventId="lavaSession__edit" idParam="${item.id}"/>	
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="id" component="${component}" listIndex="${iterator.index}" entityType="lavaSession"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="createTimestamp" component="${component}" listIndex="${iterator.index}" entityType="lavaSession"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="accessTimestamp" component="${component}" listIndex="${iterator.index}" entityType="lavaSession"/>
			</tags:listCell>
		<tags:listCell>
			<tags:listField property="username" component="${component}" listIndex="${iterator.index}" entityType="lavaSession"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="hostname" component="${component}" listIndex="${iterator.index}" entityType="lavaSession"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="currentStatus" component="${component}" listIndex="${iterator.index}" entityType="lavaSession"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

