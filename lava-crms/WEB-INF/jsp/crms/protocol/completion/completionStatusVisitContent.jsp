<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="view" value="${param.view}"/>
<c:set var="component" value="${param.component}"/>
<c:set var="fmtColumns" value="${param.fmtColumns}"/>

<%-- set list column widths --%>
<c:choose>
	<c:when test="${view == 'Patient' && fmtColumns == 'Summary'}">
		<c:set var="actionCol" value="8%"/>
		<c:set var="projectCol" value="16%"/>
		<c:set var="protocolCol" value="16%"/>
		<c:set var="currStatusCol" value="11%"/>
		<c:set var="timepointCol" value="16%"/>
		<c:set var="visitCol" value="16%"/>
		<c:set var="optionalCol" value="5%"/>
		<c:set var="compStatusCol" value="12%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Summary'}">
		<c:set var="actionCol" value="8%"/>
		<c:set var="patientCol" value="16%"/>
		<c:set var="protocolCol" value="15%"/>
		<c:set var="currStatusCol" value="11%"/>
		<c:set var="timepointCol" value="15%"/>
		<c:set var="visitCol" value="15%"/>
		<c:set var="optionalCol" value="5%"/>
		<c:set var="compStatusCol" value="12%"/>
	</c:when> 
	<c:when test="${view == 'Patient' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="6%"/>
		<c:set var="patientCol" value="8%"/>
		<c:set var="projectCol" value="6%"/>
		<c:set var="protocolCol" value="6%"/>
		<c:set var="protocolStatusCol" value="8%"/>
		<c:set var="protocolStatusReasonCol" value="9%"/>
		<c:set var="protocolStatusNoteCol" value="13%"/>
		<c:set var="timepointCol" value="6%"/>
		<c:set var="timepointOptionalCol" value="3%"/>
		<c:set var="visitCol" value="6%"/>
		<c:set var="visitOptionalCol" value="3%"/>
		<c:set var="assignmentCol" value="18%"/>		
		<c:set var="compStatusCol" value="8%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="6%"/>
		<c:set var="patientCol" value="8%"/>
		<c:set var="projectCol" value="6%"/>
		<c:set var="protocolCol" value="6%"/>
		<c:set var="protocolStatusCol" value="8%"/>
		<c:set var="protocolStatusReasonCol" value="9%"/>
		<c:set var="protocolStatusNoteCol" value="13%"/>
		<c:set var="timepointCol" value="6%"/>
		<c:set var="timepointOptionalCol" value="3%"/>
		<c:set var="visitCol" value="6%"/>
		<c:set var="visitOptionalCol" value="3%"/>
		<c:set var="assignmentCol" value="18%"/>		
		<c:set var="collectStatusCol" value="8%"/>
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
<tags:componentListColumnHeader component="${component}" label="Protocol Status" width="${protocolStatusCol}"/>
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Status Reason" width="${protocolStatusReasonCol}"/>
	<tags:componentListColumnHeader component="${component}" label="Status Note" width="${protocolStatusNotesCol}"/>
</c:if>	
<tags:componentListColumnHeader component="${component}" label="Timepoint" width="${timepointCol}" sort="timepointConfig.label"/>
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Optional" width="${timepointOptionalCol}"/>
</c:if>	
<tags:componentListColumnHeader component="${component}" label="Visit" width="${visitCol}" sort="visitConfig.label"/>
<tags:componentListColumnHeader component="${component}" label="Opt${fmtColumns == 'Expanded' ? 'ional' : ''}" width="${visitOptionalCol}"/>
<c:if test="${fmtColumns == 'Expanded'}">
<tags:componentListColumnHeader component="${component}" label="Visit Assignment" width="${assignmentCol}"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="${fmtColumns == 'Expanded' ? 'Completion ' : ''}Status" width="${compStatusCol}" sort="visit.compStatus"/>
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
			<tags:listField property="currStatus" component="${component}" listIndex="${iterator.index}" entityType="protocol"/>
		</tags:listCell>
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="currReason" component="${component}" listIndex="${iterator.index}" entityType="protocol"/>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="currNote" component="${component}" listIndex="${iterator.index}" entityType="protocol"/>
			</tags:listCell>
		</c:if>
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
			<tags:listField property="visitCompStatus" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.compStatus"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>
