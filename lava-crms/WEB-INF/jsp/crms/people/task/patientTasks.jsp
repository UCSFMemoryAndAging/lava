<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="patientTasks"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.task.task" eventId="task__add" component="${component}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Project" width="15%" sort="projName"/>
<tags:componentListColumnHeader component="${component}" label="Task Type" width="15%" sort="taskType"/>
<tags:componentListColumnHeader component="${component}" label="Task Description" width="32%"/>
<tags:componentListColumnHeader component="${component}" label="Opened Date<br/>(Task Status)" width="15%" sort="openedDate"/>
<tags:componentListColumnHeader component="${component}" label="Due Date" width="15%" sort="dueDate"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLStandardButtons actionId="lava.crms.people.task.task" component="task" idParam="${item.id}"/>	    	                      
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
		<tags:listCell>
			<tags:listField property="dueDate" component="${component}" listIndex="${iterator.index}" entityType="task"/>
		</tags:listCell>
		
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

