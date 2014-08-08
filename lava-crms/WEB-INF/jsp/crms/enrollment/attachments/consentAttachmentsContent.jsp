<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="parentView">${param.parentView}</c:set>

<content tag="listColumns">
<tags:listRow>

<tags:componentListColumnHeader component="${component}" label="Action" width="12%"/>
<tags:componentListColumnHeader component="${component}" label="Type (Source)" width="28%" sort="contentType"/>
<tags:componentListColumnHeader component="${component}" label="Name" width="35%"/>
<tags:componentListColumnHeader component="${component}" label="File Status" width="25%" sort="fileStatusDate"/>

</tags:listRow>
</content>

<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLButton buttonImage="view" actionId="lava.crms.enrollment.attachments.consentAttachment" eventId="consentAttachment__view" idParam="${item.id}" parameters="param,${parentView}"/>	    
			<tags:listActionURLButton buttonImage="edit" actionId="lava.crms.enrollment.attachments.consentAttachment" eventId="consentAttachment__edit" idParam="${item.id}" parameters="param,${parentView}"/>	    
			<tags:listActionURLButton buttonImage="delete" actionId="lava.crms.enrollment.attachments.consentAttachment" eventId="consentAttachment__delete" idParam="${item.id}" parameters="param,${parentView}"/>	    
			<tags:listActionURLButton buttonImage="download" actionId="lava.crms.enrollment.attachments.consentAttachment" eventId="consentAttachment__download" idParam="${item.id}" parameters="param,${parentView}"/>	    
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="contentType" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/><br/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="name" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="statusBlock" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/>
		</tags:listCell>
		
		
	</tags:listRow>
</tags:list>

