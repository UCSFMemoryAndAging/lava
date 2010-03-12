<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="projectTasks"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects /' : currentProject.name}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="listFilters">
<tags:contentColumn columnClass="colLeft2Col5050">
	<tags:listFilterField property="patient.firstName" component="${component}" entityType="task"/>
	<tags:listFilterField property="patient.lastName" component="${component}" entityType="task"/>
	<tags:listFilterField property="projName" component="${component}" entityType="task"/>
	<tags:listFilterField property="openedDateStart" component="${component}" entityType="task"/>
	<tags:listFilterField property="openedDateEnd" component="${component}" entityType="task"/>
	<tags:listFilterField property="openedBy" component="${component}" entityType="task"/>
	<tags:listFilterField property="taskType" component="${component}" entityType="task"/>
	<tags:listFilterField property="taskDesc" component="${component}" entityType="task"/>
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
	<tags:listFilterField property="dueDateStart" component="${component}" entityType="task"/>
	<tags:listFilterField property="dueDateEnd" component="${component}" entityType="task"/>
	<tags:listFilterField property="taskStatus" component="${component}" entityType="task"/>
	<tags:listFilterField property="assignedTo" component="${component}" entityType="task"/>
	<tags:listFilterField property="closedDateStart" component="${component}" entityType="task"/>
	<tags:listFilterField property="closedDateEnd" component="${component}" entityType="task"/>
	<tags:listFilterField property="workingNotes" component="${component}" entityType="task"/>
</tags:contentColumn>
</content>

<c:if test="${not empty currentPatient}">
<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.task.task" eventId="task__add" component="${component}"/>	    
</content>
</c:if>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Patient" width="20%" sort="patient.fullNameRev"/>
<tags:componentListColumnHeader component="${component}" label="Project" width="12%" sort="projName"/>
<tags:componentListColumnHeader component="${component}" label="Task Type" width="13%" sort="taskType"/>
<tags:componentListColumnHeader component="${component}" label="Task Description" width="32%"/>
<tags:componentListColumnHeader component="${component}" label="Opened Date<br/>(Task Status)" width="15%" sort="openedDate"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.people.task.task" component="task" idParam="${item.id}" locked="${item.locked}"/>	    	                      
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="patient.fullNameRev" component="${component}" listIndex="${iterator.index}" entityType="task" metadataName="patient.fullNameRev"/>	
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="task"/>	
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="taskType" component="${component}" listIndex="${iterator.index}" entityType="task"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="taskDesc" component="${component}" listIndex="${iterator.index}" entityType="task"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="openedDate" component="${component}" listIndex="${iterator.index}" entityType="task"/><br/>
			(<tags:listField property="taskStatus" component="${component}" listIndex="${iterator.index}"  entityType="task"/>)
			
		</tags:listCell>

		
	</tags:listRow>
</tags:list>

    



</page:applyDecorator>    
</page:applyDecorator>	    

