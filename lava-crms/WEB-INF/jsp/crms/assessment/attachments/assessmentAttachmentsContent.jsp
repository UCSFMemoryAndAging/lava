<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

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
				<tags:listActionURLButton buttonImage="view" actionId="lava.crms.assessment.attachments.assessmentAttachment" eventId="assessmentAttachment__view" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="edit" actionId="lava.crms.assessment.attachments.assessmentAttachment" eventId="assessmentAttachment__edit" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="delete" actionId="lava.crms.assessment.attachments.assessmentAttachment" eventId="assessmentAttachment__delete" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="download" actionId="lava.crms.assessment.attachments.assessmentAttachment" eventId="assessmentAttachment__download" idParam="${item.id}"/>	    
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

