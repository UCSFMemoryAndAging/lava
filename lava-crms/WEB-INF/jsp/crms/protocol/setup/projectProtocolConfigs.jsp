<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="projectProtocolConfigs"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects ' : currentProject.name}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.protocol.setup.protocolConfig" eventId="protocolConfig__add" component="${component}" locked="${currentPatient.locked}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="10%"/>
<tags:componentListColumnHeader component="${component}" label="Protocol" width="30%"/>
<tags:componentListColumnHeader component="${component}" label="Project" width="30%" />
<tags:componentListColumnHeader component="${component}" label="Notes" width="30%" />
</tags:listRow>
</content>

<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.protocol.setup.protocolConfig" component="protocolConfig" idParam="${item.id}" locked="${item.locked}"/>	    	                      
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="label" component="${component}" listIndex="${iterator.index}" entityType="protocolConfig"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="protocolConfig"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="notes" component="${component}" listIndex="${iterator.index}" entityType="protocolConfig"/>
		</tags:listCell>
		
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

