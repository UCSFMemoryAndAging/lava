<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<page:applyDecorator name="component.entity.section">
  	<page:param name="sectionId">details</page:param>
  	<page:param name="sectionNameKey">task.details.section</page:param>
    <page:param name="quicklinkPosition">none</page:param>
 <tags:contentColumn columnClass="colLeft2Col5050">
	 <tags:createField property="id" component="${component}"/>
	 <tags:createField property="patient.fullNameRev" component="${component}"/>
	 <tags:createField property="projName" component="${component}"/>
	 <tags:createField property="openedDate" component="${component}"/>
	 <tags:createField property="openedBy" component="${component}"/>
	 <tags:createField property="taskType" component="${component}"/>
	 <tags:createField property="taskDesc" component="${component}"/>
 </tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">
	<tags:createField property="assignedTo" component="${component}"/>
	<tags:createField property="dueDate" component="${component}"/>
	<tags:createField property="taskStatus" component="${component}"/>
	<tags:createField property="closedDate"  component="${component}"/>
	<tags:createField property="workingNotes"  component="${component}"/>

</tags:contentColumn>
</page:applyDecorator>  

<ui:formGuide ignoreDoOnLoad="true">
    <ui:observe elementIds="task_projName" forValue=".+"/>
    <ui:submitForm form="task" event="task__reRender"/>
</ui:formGuide>

