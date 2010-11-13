<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>


<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	
<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="11%"/>
<tags:componentListColumnHeader component="${component}"  label="Context" width="29%" sort="context"/>
<tags:componentListColumnHeader component="${component}" label="Name" width="30%" sort="name"/>
<tags:componentListColumnHeader component="${component}" label="Value" width="30%"/>

</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		 <tags:listCell styleClass="actionButton">
			<tags:listActionURLButton buttonImage="view" actionId="lava.core.home.prefs.preference" eventId="preference__view" idParam="${item.id}"/>	    
			<tags:listActionURLButton buttonImage="edit" actionId="lava.core.home.prefs.preference" eventId="preference__edit" idParam="${item.id}"/>	
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="context" component="${component}" listIndex="${iterator.index}" entityType="${component}" mode="lv"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="name" component="${component}" listIndex="${iterator.index}" entityType="${component}" mode="lv"/>
		</tags:listCell>	
		<tags:listCell>
		
		
			<%-- check for presence of an entry in viewProperties metadata for this preference 
				using the form of:
				preference.value.[context].[name].value 	for standard preferences, or
				preference.value..[name].value 				for global preferences --%>
			<c:set var="metadataName">preference.value.<tags:componentListProperty listIndex="${iterator.index}" component="${component}" property="context"/>.<tags:componentListProperty listIndex="${iterator.index}" component="${component}" property="name"/></c:set>
			<spring:message code="${metadataName}.style" var="style" scope="page" text=""/>
			<c:choose>
				<c:when test="${empty style}">
					<%-- metadata entry not found, so use standard style for field  --%>
					<tags:listField listIndex="${iterator.index}" component="${component}" property="value" mode="lv"/>
				</c:when>
				<c:otherwise>
					<%-- metadata entry found, so use style as defined in metadata --%>
					<tags:listField listIndex="${iterator.index}" component="${component}" property="value" metadataName="${metadataName}" mode="lv"/>
				</c:otherwise>
			</c:choose>
	
			<tags:ifComponentPropertyNotEmpty component="${component}" property="user" listIndex="${iterator.index}">
				<a href="<tags:actionURL actionId="lava.core.home.prefs.UserPreferences" eventId="userPreferences__custom" idParam="${item.id}" flowExecutionKey="${flowExecutionKey}"/>"> (reset)</a>
			</tags:ifComponentPropertyNotEmpty>
		</tags:listCell>		
	</tags:listRow>
</tags:list>
	
</page:applyDecorator>  
 
 
