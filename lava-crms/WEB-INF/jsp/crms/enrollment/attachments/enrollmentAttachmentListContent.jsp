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

	<c:import url="/WEB-INF/jsp/crms/enrollment/attachments/enrollmentAttachmentsContent.jsp">
		<c:param name="component">${component}</c:param>
		<c:param name="parentView">specificEntityList</c:param>
	</c:import>

</page:applyDecorator>

</page:applyDecorator>