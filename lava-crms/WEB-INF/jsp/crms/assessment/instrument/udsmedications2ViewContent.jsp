<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="componentView">${param.componentView}</c:set>
<c:set var="instrTypeEncoded">${param.instrTypeEncoded}</c:set>

<%-- reset component to the details component --%>
<c:set var="component" value="details"/>

<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">section1</page:param>
  <page:param name="sectionNameKey">udsmedications2.details.section</page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"><spring:message code="udsmedications2.details.instructions"/></page:param>

<%-- need to use list decorator here to get the navigation controls --%>
<page:applyDecorator name="component.list.content">
  <page:param name="pageName">medications</page:param>
  <page:param name="component">${component}</page:param>
  <page:param name="isSecondary">true</page:param>
  
<content tag="listColumns">
<tags:listRow>
	
	<tags:listColumnHeader labelKey="udsmedications2.details.drugColHeader" width="25%"/>
	<tags:listColumnHeader labelKey="udsmedications2.details.brandColHeader" width="25%"/>
	<tags:listColumnHeader labelKey="udsmedications2.details.genericColHeader" width="25%"/>
	<tags:listColumnHeader labelKey="udsmedications2.details.notListedColHeader" width="25%"/>
</tags:listRow>
</content>

<tags:list component="${component}">
	
	<tags:listRow>
		<tags:listCell>
			<tags:listField property="drugId" component="${component}" listIndex="${iterator.index}" entityType="udsmedications2" mode="lv"/>			
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="brand" component="${component}" listIndex="${iterator.index}" entityType="udsmedications2" mode="lv"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="generic" component="${component}" listIndex="${iterator.index}" entityType="udsmedications2" mode="lv"/>
			</tags:listCell>
		<tags:listCell>
			<tags:listField property="notListed" component="${component}" listIndex="${iterator.index}" entityType="udsmedications2" mode="lv"/>			
		</tags:listCell>
	</tags:listRow>
</tags:list>
</page:applyDecorator> <%-- component.list.content for instrumentDetails list --%>

</page:applyDecorator> <%-- component.instrument.section --%>

