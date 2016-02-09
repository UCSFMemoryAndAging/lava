<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="pageName" value="${param.pageName}"/>
<c:set var="instrId" value="${param.instrId}"/>
<c:set var="component" value="attachments"/>

<page:applyDecorator name="component.entity.section">
<page:param name="sectionId">attachments</page:param>
<page:param name="sectionNameKey">assessment.attachments.section</page:param>


<page:applyDecorator name="component.list.content">
      <page:param name="component">${component}</page:param>
      <page:param name="listTitle">Attachments</page:param>
      <page:param name="isSecondary">true</page:param>

	<content tag="customActions">
		<tags:eventButton buttonText="Add" component="assessmentAttachment" action="add" pageName="${pageName}" javascript="submitted=true;" parameters="instrId,${instrId}"/> 
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
					<tags:eventActionButton buttonImage="view" component="assessmentAttachment" action="view" pageName="${pageName}" parameters="id,${item.id}" javascript="submitted=true;" title="View"/> 
					<tags:eventActionButton buttonImage="edit" component="assessmentAttachment" action="edit" pageName="${pageName}" parameters="id,${item.id}" javascript="submitted=true;" title="Edit"/> 
					<tags:eventActionButton buttonImage="delete" component="assessmentAttachment" action="delete" pageName="${pageName}" parameters="id,${item.id}" javascript="submitted=true;" title="Delete"/> 
					<tags:eventActionButton buttonImage="download" component="assessmentAttachment" action="download" pageName="${pageName}" parameters="id,${item.id}" javascript="submitted=true;" title="Download"/> 
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="contentType" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/><br/>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="name" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/><br>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="statusBlock" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/>
			</tags:listCell>
		</tags:listRow>
	</tags:list>

</page:applyDecorator>

</page:applyDecorator>