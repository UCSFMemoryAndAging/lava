<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- navigable reference list of instruments to be deleted for confirmation purposes --%>
	
<%-- reset component to the details component --%>
<c:set var="component" value="groupList"/>

<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionNameKey">instrumentGroup.deleteAll.section</page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>

<%-- need to use list decorator here to get the navigation controls --%>
<page:applyDecorator name="component.list.content">
  <page:param name="pageName">instrumentGroup</page:param>
  <page:param name="component">${component}</page:param>
  <page:param name="isSecondary">true</page:param>
  
<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}" label="Measure" width="12%"/>
	<tags:componentListColumnHeader component="${component}" label="Collection" width="20%"/>
	<tags:componentListColumnHeader component="${component}" label="Data Entry" width="20%"/>
	<tags:componentListColumnHeader component="${component}" label="Verify" width="20%"/>
	<tags:componentListColumnHeader component="${component}" label="Summary" width="20%"/>
</tags:listRow>
</content>

<tags:list component="${component}">
	<tags:listRow>
		<tags:listCell>
			<tags:listField property="instrType" component="${component}" listIndex="${iterator.index}" entityType="instrument" mode="lv"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="collectionStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument" mode="lv"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="entryStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument" mode="lv"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="verifyStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument" mode="lv"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="summary" component="${component}" listIndex="${iterator.index}" entityType="instrument" mode="lv"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>
</page:applyDecorator> <%-- component.list.content for instrumentDetails list --%>

</page:applyDecorator> <%-- component.instrument.section --%>
