<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="allImportDefinitions"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs"></page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.core.importer.definition.importDefinition" eventId="importDefinition__add" component="${component}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
	<tags:componentListColumnHeader component="${component}" label="Name" width="12%" sort="name" />
	<tags:componentListColumnHeader component="${component}" label="Mapping File" width="30%" sort="mappingFile.name"/>
	<tags:componentListColumnHeader component="${component}" label="Notes" width="50%"/>
</tags:listRow>
</content>

<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.core.importer.definition.importDefinition" component="importDefinition" idParam="${item.id}"/>	    
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="name" component="${component}" listIndex="${iterator.index}" entityType="importDefinition"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="mappingFile.name" component="${component}" listIndex="${iterator.index}" metadataName="lavaFile.name"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="notes" component="${component}" listIndex="${iterator.index}" entityType="importDefinition"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

