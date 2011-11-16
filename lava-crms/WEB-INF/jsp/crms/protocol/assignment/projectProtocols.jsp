<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="projectProtocols"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects /' : currentProject.name}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="listFilters">
	
	<tags:contentColumn columnClass="colLeft2Col5050">
	
	<tags:listFilterField property="patient.firstName" component="${component}" entityType="protocol"/>
	<tags:listFilterField property="patient.lastName" component="${component}" entityType="protocol"/>
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
	</tags:contentColumn>
</content>

<content tag="customActions">
	<c:if test="${not empty currentPatient}">
		<tags:actionURLButton buttonText="Add"  actionId="lava.crms.protocol.assignment.protocol" eventId="protocol__add" component="${component}" locked="${currentPatient.locked}"/>	    
	</c:if>
</content>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Patient" width="30%"/>
<tags:componentListColumnHeader component="${component}" label="Protocol" width="20%" />
<tags:componentListColumnHeader component="${component}" label="Project" width="30%" />
<tags:componentListColumnHeader component="${component}" label="Enrolled Date" width="12%"/>
</tags:listRow>
</content>

<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocol" component="protocol" idParam="${item.id}" locked="${item.locked}"/>	    	                      
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="patient.fullNameNoSuffix" component="${component}" listIndex="${iterator.index}" metadataName="patient.fullNameNoSuffix"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="protocolConfig.label" component="${component}" listIndex="${iterator.index}" metadataName="protocolConfig.label"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="protocol"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="enrolledDate" component="${component}" listIndex="${iterator.index}"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

