<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="importLogMessages"/>
<c:set var="pageName">${param.pageName}</c:set>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionNameKey">importLog.errors.section</page:param>
  <page:param name="view">view</page:param>
  <page:param name="instructions"> </page:param>

<%-- need to use list decorator here to get the navigation controls --%>
<page:applyDecorator name="component.list.content">
  <page:param name="pageName">${pageName}</page:param>
  <page:param name="component">${component}</page:param>
  <page:param name="listTitle">Individual Record Errors / Warnings / Info</page:param>
  <page:param name="isSecondary">true</page:param>
  
<%-- TODO: quickFilter on message type --%>  
  
<content tag="listColumns">
<tags:listRow>
	<tags:listColumnHeader labelKey="importLog.message.typeColHeader" width="10%"/>
	<tags:listColumnHeader labelKey="importLog.message.lineNumColHeader" width="10%"/>
	<tags:listColumnHeader labelKey="importLog.message.messageColHeader" width="80%"/>
</tags:listRow>
</content>

<tags:list component="${component}">
	<tags:listRow>
		<tags:listCell>
			<tags:listField property="type" component="${component}" listIndex="${iterator.index}" entityType="importLog" mode="lv"/>			
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="lineNum" component="${component}" listIndex="${iterator.index}" entityType="importLog" mode="lv"/>			
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="message" component="${component}" listIndex="${iterator.index}" entityType="importLog" mode="lv"/>			
		</tags:listCell>
	</tags:listRow>
</tags:list>
</page:applyDecorator> <%-- component.list.content for importLogErrors list --%>

</page:applyDecorator> <%-- component.instrument.section --%>

