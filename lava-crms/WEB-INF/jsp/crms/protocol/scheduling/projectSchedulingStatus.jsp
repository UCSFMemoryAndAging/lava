<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="projectSchedulingStatus"/>
<c:set var="fmtGranularity">
	${command.components[component].filter.params['fmtGranularity']}
</c:set>
<c:set var="fmtColumns">
	${command.components[component].filter.params['fmtColumns']}
</c:set>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Project ' : currentProject.name}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	<page:param name="widthClass">${fmtColumns == 'Summary' ? '' : 'wide'}</page:param>
	
<content tag="listFilters">
	<tags:contentColumn columnClass="colLeft2Col5050">
		<tags:listFilterField property="protocolConfig.label" component="${component}" entityType="protocolStatusList"/>
		<tags:listFilterField property="timepointConfig.label" component="${component}" entityType="protocolStatusList"/>
		<c:if test="${fmtGranularity == 'Visit'}">
			<tags:listFilterField property="visitConfig.label" component="${component}" entityType="protocolStatusList"/>
		</c:if>
		<tags:listFilterField property="schedWinStatus" component="${component}" entityType="protocolStatusList"/>
		<tags:listFilterField property="schedWinReason" component="${component}" entityType="protocolStatusList"/>
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
		<tags:listFilterField property="schedWinStartStart" component="${component}" entityType="protocolStatusList"/>
		<tags:listFilterField property="schedWinStartEnd" component="${component}" entityType="protocolStatusList"/>
		<tags:listFilterField property="schedWinEndStart" component="${component}" entityType="protocolStatusList"/>
		<tags:listFilterField property="schedWinEndEnd" component="${component}" entityType="protocolStatusList"/>
	</tags:contentColumn>
</content>

<content tag="listFormatting">
    <span class="listControlBarCalendarLabels">Format:</span> 
     &nbsp;&nbsp;
    <span class="listControlBarCalendarLabels">Rows:</span> 
    <tags:eventLink linkText="Timepoint" action="timepoint" component="${component}" pageName="${component}" className="${fmtGranularity=='Timepoint'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
    <tags:eventLink linkText="Visit" action="visit" component="${component}" pageName="${component}" className="${fmtGranularity=='Visit'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
     &nbsp;&nbsp;&nbsp;&nbsp;
	<span class="listControlBarCalendarLabels">Columns:
    <tags:eventLink linkText="Summary" action="summary" component="${component}" pageName="${component}" className="${fmtColumns=='Summary'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
    <tags:eventLink linkText="Expanded" action="expanded" component="${component}" pageName="${component}" className="${fmtColumns=='Expanded'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
</content>

<content tag="listQuickFilter">
	<tags:listQuickFilter component="${component}" listItemSource="protocol.schedulingStatusQuickFilter" label="Show:"/>
</content>

<c:choose>
	<c:when test="${fmtGranularity == 'Timepoint'}">
		<c:import url="/WEB-INF/jsp/crms/protocol/scheduling/schedulingStatusTimepointContent.jsp">
			<c:param name="view">Project</c:param>
			<c:param name="component">${component}</c:param>
			<c:param name="fmtColumns" value="${fmtColumns}"/>
		</c:import>	
	</c:when>
	<c:when test="${fmtGranularity == 'Visit'}">
		<c:import url="/WEB-INF/jsp/crms/protocol/scheduling/schedulingStatusVisitContent.jsp">
			<c:param name="view">Project</c:param>
			<c:param name="component">${component}</c:param>
			<c:param name="fmtColumns" value="${fmtColumns}"/>
		</c:import>	
	</c:when>
</c:choose>

</page:applyDecorator>    
</page:applyDecorator>	    

