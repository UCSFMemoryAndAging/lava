<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="allImportLogs"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs"></page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
	<tags:componentListColumnHeader component="${component}" label="Timestamp" width="12%" sort="importTimestamp" />
	<tags:componentListColumnHeader component="${component}" label="ImportedBy" width="20%" sort="importedBy"/>
	<tags:componentListColumnHeader component="${component}" label="Data File" width="30%"/>
	<tags:componentListColumnHeader component="${component}" label="Definition" width="30%"/>
</tags:listRow>
</content>

<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLButton buttonImage="view" actionId="lava.core.importer.log.importLog" eventId="importLog__view" idParam="${item.id}"/>	    
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="importTimestamp" component="${component}" listIndex="${iterator.index}" entityType="importLog"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="importedBy" component="${component}" listIndex="${iterator.index}" entityType="importLog"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="filename" component="${component}" listIndex="${iterator.index}" entityType="importLog"/>
<%-- add download icon / action --%>			
		</tags:listCell>
<%-- TODO: prob replace definitionName with import results summary --%>		
		<tags:listCell>
			<tags:listField property="definition.name" component="${component}" listIndex="${iterator.index}" entityType="importLog"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

