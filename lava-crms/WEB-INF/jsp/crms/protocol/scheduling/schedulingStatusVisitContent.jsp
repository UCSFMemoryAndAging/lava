<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="view" value="${param.view}"/>
<c:set var="component" value="${param.component}"/>
<c:set var="fmtColumns" value="${param.fmtColumns}"/>

<%-- set list column widths --%>
<c:choose>
	<c:when test="${view == 'Patient' && fmtColumns == 'Summary'}">
		<c:set var="actionCol" value="8%"/>
		<c:set var="protocolCol" value="16%"/>
		<c:set var="timepointCol" value="16%"/>
		<c:set var="visitCol" value="16%"/>
		<c:set var="optionalCol" value="5%"/>
		<c:set var="schedWinCol" value="27%"/>
		<c:set var="schedStatusCol" value="12%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Summary'}">
		<c:set var="actionCol" value="8%"/>
		<c:set var="patientCol" value="16%"/>
		<c:set var="protocolCol" value="14%"/>
		<c:set var="timepointCol" value="14%"/>
		<c:set var="visitCol" value="14%"/>
		<c:set var="optionalCol" value="4%"/>
		<c:set var="schedWinCol" value="20%"/>
		<c:set var="schedStatusCol" value="10%"/>
	</c:when> 
	<c:when test="${view == 'Patient' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="6%"/>
		<c:set var="patientCol" value="8%"/>
		<c:set var="projectCol" value="6%"/>
		<c:set var="protocolCol" value="6%"/>
		<c:set var="timepointCol" value="6%"/>
		<c:set var="timepointOptionalCol" value="3%"/>
		<c:set var="visitCol" value="6%"/>
		<c:set var="visitOptionalCol" value="3%"/>
		<c:set var="assignmentCol" value="16%"/>		
		<c:set var="schedWinCol" value="8%"/>
		<c:set var="anchorDateCol" value="5%"/>
		<c:set var="schedStatusCol" value="7%"/>
		<c:set var="schedStatusReasonCol" value="7%"/>
		<c:set var="schedStatusNoteCol" value="13%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="6%"/>
		<c:set var="patientCol" value="8%"/>
		<c:set var="projectCol" value="6%"/>
		<c:set var="protocolCol" value="6%"/>
		<c:set var="timepointCol" value="6%"/>
		<c:set var="timepointOptionalCol" value="3%"/>
		<c:set var="visitCol" value="6%"/>
		<c:set var="visitOptionalCol" value="3%"/>
		<c:set var="assignmentCol" value="16%"/>		
		<c:set var="schedWinCol" value="8%"/>
		<c:set var="anchorDateCol" value="5%"/>
		<c:set var="schedStatusCol" value="7%"/>
		<c:set var="schedStatusReasonCol" value="7%"/>
		<c:set var="schedStatusNoteCol" value="13%"/>
	</c:when> 
</c:choose>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="${actionCol}"/>
<c:if test="${view == 'Project'}">
<tags:componentListColumnHeader component="${component}" label="Patient" width="${patientCol}" sort="patient.fullNameRevNoSuffix"/>
</c:if>	
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Project" width="${projectCol}" sort="projName"/>
</c:if>	
<tags:componentListColumnHeader component="${component}" label="Protocol" width="${protocolCol}" sort="protocolConfig.label"/>
<tags:componentListColumnHeader component="${component}" label="Timepoint" width="${timepointCol}" sort="timepointConfig.label"/>
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Optional" width="${timepointOptionalCol}"/>
</c:if>	
<tags:componentListColumnHeader component="${component}" label="Visit" width="${visitCol}" sort="visitConfig.label"/>
<tags:componentListColumnHeader component="${component}" label="Opt${fmtColumns == 'Expanded' ? 'ional' : ''}" width="${visitOptionalCol}"/>
<c:if test="${fmtColumns == 'Expanded'}">
<tags:componentListColumnHeader component="${component}" label="Visit Assignment" width="${assignmentCol}"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="Scheduling Window" width="${schedWinCol}" sort="schedWinStart"/>
<c:if test="${fmtColumns == 'Expanded'}">
<tags:componentListColumnHeader component="${component}" label="Anchor Date" width="${anchorDateCol}" sort="schedWinAnchorDate"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="${fmtColumns == 'Expanded' ? 'Scheduling ' : ''}Status" width="${schedStatusCol}" sort="schedWinStatus"/>
<c:if test="${fmtColumns == 'Expanded'}">
<tags:componentListColumnHeader component="${component}" label="Status Reason" width="${schedStatusReasonCol}" sort="schedWinReason"/>
<tags:componentListColumnHeader component="${component}" label="Status Note" width="${schedStatusNoteCol}"/>
</c:if>	
</tags:listRow>
</content>


<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolVisit" component="protocolVisit" idParam="${item.visitId}" noDelete="true"/>	    	                      
		</tags:listCell>
		<c:if test="${view == 'Project'}">
			<tags:listCell>
				<tags:listField property="fullNameRevNoSuffix" component="${component}" listIndex="${iterator.index}" metadataName="patient.fullNameRevNoSuffix"/>
			</tags:listCell>
		</c:if>			
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="protocol"/>
			</tags:listCell>
		</c:if>	
		<tags:listCell>
			<tags:listField property="protocolLabel" component="${component}" listIndex="${iterator.index}" metadataName="protocolConfig.label"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="timepointLabel" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepointConfig.label"/>
		</tags:listCell>
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="timepointOptional" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepointConfig.label"/>
			</tags:listCell>
		</c:if>	
		<tags:listCell>
			<tags:listField property="visitLabel" component="${component}" listIndex="${iterator.index}" metadataName="protocolVisitConfig.label"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="visitOptional" component="${component}" listIndex="${iterator.index}" metadataName="protocolVisitConfig.label"/>
		</tags:listCell>
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<c:set var="visitLink">
					<tags:listActionURLButton buttonImage="view" actionId="lava.crms.scheduling.visit.visit" eventId="visit__view" idParam="${item.assignedVisitId}"/>	    
				</c:set>
				<tags:listField property="visitSummary" component="${component}" listIndex="${iterator.index}" metadataName="protocol.summary" link="${visitLink}"/>
			</tags:listCell>
		</c:if>			
		<tags:listCell>
			<tags:listField property="tpSchedWinStart" property2="tpSchedWinEnd" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.schedWinStart" separator="&nbsp;-&nbsp;"/>
		</tags:listCell>
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="tpSchedWinAnchorDate" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.schedWinAnchorDate"/>
			</tags:listCell>
		</c:if>
		<tags:listCell>
			<tags:listField property="visitSchedWinStatus" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.schedWinStatus"/>
		</tags:listCell>
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="visitSchedWinReason" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.schedWinReason"/>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="visitSchedWinNote" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.schedWinNote"/>
			</tags:listCell>
		</c:if>	
	</tags:listRow>
</tags:list>
