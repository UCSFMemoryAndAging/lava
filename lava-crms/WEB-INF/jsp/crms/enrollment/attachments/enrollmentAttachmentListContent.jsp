<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="attachments"/>
<c:set var="propertyValues" value="${param.propertyValues}"/>


<page:applyDecorator name="component.entity.section">
<page:param name="sectionId">attachments</page:param>
<page:param name="sectionNameKey">enrollment.attachments.section</page:param>


<page:applyDecorator name="component.list.content">
      <page:param name="component">${component}</page:param>
      <page:param name="listTitle">Attachments</page:param>
      <page:param name="isSecondary">true</page:param>

	<content tag="customActions">
		<tags:actionURLButton buttonText="Add" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__add" component="${component}" parameters="${propertyValues},param,specificEntityList"/>
	</content>
	
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
				<tags:listActionURLButton buttonImage="view" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__view" idParam="${item.id}" parameters="param,${parentView}"/>	    
				<tags:listActionURLButton buttonImage="edit" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__edit" idParam="${item.id}" parameters="param,${parentView}"/>	    
				<tags:listActionURLButton buttonImage="delete" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__delete" idParam="${item.id}" parameters="param,${parentView}"/>	    
				<tags:listActionURLButton buttonImage="download" actionId="lava.crms.enrollment.attachments.enrollmentAttachment" eventId="enrollmentAttachment__download" idParam="${item.id}" parameters="param,${parentView}"/>	    
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

</page:applyDecorator>

</page:applyDecorator>