<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="patientProtocols"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.protocol.assignment.protocol" eventId="protocol__add" component="${component}" locked="${currentPatient.locked}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>

<tags:componentListColumnHeader component="${component}" label="Action" width="10%"/>
<tags:componentListColumnHeader component="${component}" label="Protocol" width="45%"/>
<tags:componentListColumnHeader component="${component}" label="Enrolled Date" width="45%"/>
</tags:listRow>
</content>


<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocol" component="protocol" idParam="${item.id}" locked="${item.locked}"/>	    
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="protocolConfig.label" component="${component}" listIndex="${iterator.index}" metadataName="protocolConfig.label"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="enrolledDate" component="${component}" listIndex="${iterator.index}" entityType="protocol"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

