<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">preference</page:param>
  <page:param name="sectionNameKey">preference.details.section</page:param>

	<tags:createField property="context" component="${component}"/>
	<tags:createField property="name" component="${component}"/>

	<%-- check for presence of an entry in viewProperties metadata for this preference 
		using the form of:
		preference.value.[context].[name].value 	for standard preferences, or
		preference.value..[name].value 				for global preferences --%>
	<c:set var="metadataName">preference.value.<tags:componentProperty component="${component}" property="context"/>.<tags:componentProperty component="${component}" property="name"/></c:set>
	<spring:message code="${metadataName}.style" var="style" scope="page" text=""/>
	<c:choose>
		<c:when test="${empty style}">
			<%-- metadata entry not found, so use standard style for field  --%>
			<tags:createField property="value" component="${component}"/>
		</c:when>
		<c:otherwise>
			<%-- metadata entry found, so use style as defined in metadata --%>
			<tags:createField property="value" metadataName="${metadataName}" component="${component}"/>
		</c:otherwise>
	</c:choose>

	
	<tags:createField property="description" component="${component}"/>
	
</page:applyDecorator>  
 