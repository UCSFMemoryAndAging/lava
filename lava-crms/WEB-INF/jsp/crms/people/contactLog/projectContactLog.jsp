<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="projectContactLog"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects /' : currentProject.name}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="listFilters">
<tags:contentColumn columnClass="colLeft2Col5050">
	<tags:listFilterField property="patient.firstName" component="${component}" entityType="contactLog"/>
	<tags:listFilterField property="patient.lastName" component="${component}" entityType="contactLog"/>
	<tags:listFilterField property="contact" component="${component}" entityType="contactLog"/>
	<c:if test="${empty currentProject}">
		<tags:listFilterField property="projName" component="${component}" entityType="contactLog"/>
	</c:if>
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
	<tags:listFilterField property="staff" component="${component}" entityType="contactLog"/>
	<tags:listFilterField property="logDateStart" component="${component}" entityType="contactLog"/>
	<tags:listFilterField property="logDateEnd" component="${component}" entityType="contactLog"/>
	</tags:contentColumn>
</content>

<c:if test="${not empty currentPatient}">
<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.contactLog.contactLog" eventId="contactLog__add" component="${component}"/>	    
</content>
</c:if>

<content tag="listColumns">
<c:choose>
	<c:when test="${empty currentProject}">
		<tags:listRow>
		<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
		<tags:componentListColumnHeader component="${component}" label="Patient (Contact)" width="25%" sort="patient.fullNameRev"/>
		<tags:componentListColumnHeader component="${component}" label="Project" width="15%" sort="projName"/>
		<tags:componentListColumnHeader component="${component}" label="Log Date<br/>(Method)" width="10%" sort="logDate"/>
		<tags:componentListColumnHeader component="${component}" label="Staff Member<br/>(Staff Initiated)" width="15%" sort="staff"/>
		<tags:componentListColumnHeader component="${component}" label="Note" width="27%"/>
		</tags:listRow>
	</c:when>
	<c:otherwise>
		<tags:listRow>
		<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
		<tags:componentListColumnHeader component="${component}" label="Patient (Contact)" width="27%" sort="patient.fullNameRev"/>
		<tags:componentListColumnHeader component="${component}" label="Log Date<br/>(Method)" width="10%" sort="logDate"/>
		<tags:componentListColumnHeader component="${component}" label="Staff Member<br/>(Staff Initiated)" width="15%" sort="staff"/>		
		<tags:componentListColumnHeader component="${component}" label="Note" width="40%"/>
		</tags:listRow>
	</c:otherwise>
</c:choose>



</content>




<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLStandardButtons actionId="lava.crms.people.contactLog.contactLog" component="contactLog" idParam="${item.id}"/>	    	                      
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="patient.fullNameNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="contactLog" metadataName="patient.fullNameNoSuffix"/>	
			<br/>(<tags:listField property="contact" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>)
		</tags:listCell>
	<c:if test="${empty currentProject}">
		<tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>	
		</tags:listCell>
	</c:if>
		<tags:listCell>
			<tags:listField property="logDate" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
			<tags:listField property="logTime" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
			<br/><tags:listField property="method" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
			</tags:listCell>
		<tags:listCell>
			<tags:listField property="staff" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
			<br/><tags:listField property="staffInit" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="note" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
		</tags:listCell>
		
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

