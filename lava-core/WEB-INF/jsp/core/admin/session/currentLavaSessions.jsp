<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="currentLavaSessions"/>
<jsp:useBean id="now" class="java.util.Date" />
 
<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs"><fmt:formatDate  pattern="MMM dd yyyy - h:mm:ss a" value="${now}"/></page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
</content>

<content tag="listColumns">
<tags:listRow>

<tags:componentListColumnHeader component="${component}" label="Action" width="6%"/>
<tags:componentListColumnHeader component="${component}" label="Session ID" width="32%"/>
<tags:componentListColumnHeader component="${component}" label="Created" width="19%" sort="createTimestamp"/>
<tags:componentListColumnHeader component="${component}" label="Accessed" width="19%" sort="accessTimestamp"/>
<tags:componentListColumnHeader component="${component}" label="Username" width="12%" sort="username"/>
<tags:componentListColumnHeader component="${component}" label="Hostname" width="12%" sort="hostname"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLButton buttonImage="view" actionId="lava.admin.session.lavaSession" eventId="lavaSession__view" parameters="sessionId,${item.httpSessionId}"/>	    
				<tags:listActionURLButton buttonImage="edit" actionId="lava.admin.session.lavaSession" eventId="lavaSession__edit" parameters="sessionId,${item.httpSessionId}"/>	
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="httpSessionId" component="${component}" listIndex="${iterator.index}" metadataName="lavaSession.id"/>
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
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

