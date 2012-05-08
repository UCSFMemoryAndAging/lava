<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="patientCompletionStatus"/>
<c:set var="fmtGranularity">
	${command.components[component].filter.params['fmtGranularity']}
</c:set>
<c:set var="fmtColumns">
	${command.components[component].filter.params['fmtColumns']}
</c:set>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	<page:param name="widthClass">${fmtColumns == 'Summary' ? '' : (fmtGranularity == 'Instrument' ? 'veryWide' : 'wide')}</page:param>

<content tag="listFormatting">
    <span class="listControlBarCalendarLabels">Format:</span> 
     &nbsp;&nbsp;
    <span class="listControlBarCalendarLabels">Rows:</span> 
    <tags:eventLink linkText="Timepoint" action="timepoint" component="${component}" pageName="${component}" className="${fmtGranularity=='Timepoint'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
    <tags:eventLink linkText="Visit" action="visit" component="${component}" pageName="${component}" className="${fmtGranularity=='Visit'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
    <tags:eventLink linkText="Instrument" action="instrument" component="${component}" pageName="${component}" className="${fmtGranularity=='Instrument'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
     &nbsp;&nbsp;&nbsp;&nbsp;
	<span class="listControlBarCalendarLabels">Columns:
    <tags:eventLink linkText="Summary" action="summary" component="${component}" pageName="${component}" className="${fmtColumns=='Summary'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
    <tags:eventLink linkText="Expanded" action="expanded" component="${component}" pageName="${component}" className="${fmtColumns=='Expanded'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
</content>

<content tag="listQuickFilter">
	<tags:listQuickFilter component="${component}" listItemSource="protocol.completionStatusQuickFilter" label="Show:"/>
</content>

<c:choose>
	<c:when test="${fmtGranularity == 'Timepoint'}">
		<c:import url="/WEB-INF/jsp/crms/protocol/completion/completionStatusTimepointContent.jsp">
			<c:param name="view">Patient</c:param>
			<c:param name="component">${component}</c:param>
			<c:param name="fmtColumns" value="${fmtColumns}"/>
		</c:import>	
	</c:when>
	<c:when test="${fmtGranularity == 'Visit'}">
		<c:import url="/WEB-INF/jsp/crms/protocol/completion/completionStatusVisitContent.jsp">
			<c:param name="view">Patient</c:param>
			<c:param name="component">${component}</c:param>
			<c:param name="fmtColumns" value="${fmtColumns}"/>
		</c:import>	
	</c:when>
	<c:when test="${fmtGranularity == 'Instrument'}">
		<c:import url="/WEB-INF/jsp/crms/protocol/completion/completionStatusInstrumentContent.jsp">
			<c:param name="view">Patient</c:param>
			<c:param name="component">${component}</c:param>
			<c:param name="fmtColumns" value="${fmtColumns}"/>
		</c:import>	
	</c:when>
</c:choose>

</page:applyDecorator>    
</page:applyDecorator>	    

