<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="view" value="${param.view}"/>
<c:set var="component" value="${param.component}"/>
<c:set var="fmtColumns" value="${param.fmtColumns}"/>

<%-- set list column widths --%>
<c:choose>
	<c:when test="${view == 'Patient' && fmtColumns == 'Summary'}">
		<c:set var="actionCol" value="8%"/>
		<c:set var="protocolCol" value="17%"/>
		<c:set var="timepointCol" value="17%"/>
		<c:set var="instrCol" value="17%"/>
		<c:set var="instrOptionalCol" value="5%"/>
		<c:set var="compStatusCol" value="12%"/>
		<c:set var="compStatusReasonCol" value="24%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Summary'}">
		<c:set var="actionCol" value="8%"/>
		<c:set var="patientCol" value="16%"/>
		<c:set var="protocolCol" value="14%"/>
		<c:set var="timepointCol" value="14%"/>
		<c:set var="instrCol" value="14%"/>
		<c:set var="instrOptionalCol" value="4%"/>
		<c:set var="compStatusCol" value="10%"/>
		<c:set var="compStatusReasonCol" value="20%"/>
	</c:when> 
	<c:when test="${view == 'Patient' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="5%"/>
		<c:set var="projectCol" value="5%"/>
		<c:set var="protocolCol" value="5%"/>
		<c:set var="protocolStatusCol" value="5%"/>
		<c:set var="protocolStatusReasonCol" value="5%"/>
		<c:set var="protocolStatusNoteCol" value="9%"/>
		<c:set var="timepointCol" value="5%"/>
		<c:set var="timepointOptionalCol" value="2%"/>
		<c:set var="visitCol" value="5%"/>
		<c:set var="visitOptionalCol" value="2%"/>
		<c:set var="visitAssignmentCol" value="12%"/>
		<c:set var="instrCol" value="5%"/>
		<c:set var="instrOptionalCol" value="2%"/>
		<c:set var="instrAssignmentCol" value="7%"/>
		<c:set var="compStatusCol" value="6%"/>
		<c:set var="compStatusReasonCol" value="6%"/>
		<c:set var="compStatusNoteCol" value="14%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="5%"/>
		<c:set var="patientCol" value="6%"/>
		<c:set var="projectCol" value="5%"/>
		<c:set var="protocolCol" value="5%"/>
		<c:set var="protocolStatusCol" value="5%"/>
		<c:set var="protocolStatusReasonCol" value="5%"/>
		<c:set var="protocolStatusNoteCol" value="8%"/>
		<c:set var="timepointCol" value="5%"/>
		<c:set var="timepointOptionalCol" value="2%"/>
		<c:set var="visitCol" value="5%"/>
		<c:set var="visitOptionalCol" value="2%"/>
		<c:set var="visitAssignmentCol" value="10%"/>
		<c:set var="instrCol" value="5%"/>
		<c:set var="instrOptionalCol" value="2%"/>
		<c:set var="instrAssignmentCol" value="7%"/>
		<c:set var="compStatusCol" value="6%"/>
		<c:set var="compStatusReasonCol" value="6%"/>
		<c:set var="compStatusNoteCol" value="11%"/>
	</c:when> 
</c:choose>


<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="${actionCol}"/>
<c:if test="${view == 'Project'}">
	<tags:componentListColumnHeader component="${component}" label="Patient" width="${patientCol}" sort="patient.fullNameRev"/>
</c:if>	
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Project" width="${projectCol}" sort="projName"/>
</c:if>	
<tags:componentListColumnHeader component="${component}" label="Protocol" width="${protocolCol}" sort="protocolConfig.label"/>
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Protocol Status" width="${protocolStatusCol}"/>
	<tags:componentListColumnHeader component="${component}" label="Status Reason" width="${protocolStatusReasonCol}"/>
	<tags:componentListColumnHeader component="${component}" label="Status Note" width="${protocolStatusNotesCol}"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="Timepoint" width="${timepointCol}" sort="timepointConfig.label"/>
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Optional" width="${timepointOptionalCol}"/>
</c:if>	
<c:if test="${fmtColumns == 'Expanded'}">
<tags:componentListColumnHeader component="${component}" label="Visit" width="${visitCol}" sort="visitConfig.label"/>
<tags:componentListColumnHeader component="${component}" label="Optional" width="${visitOptionalCol}"/>
<tags:componentListColumnHeader component="${component}" label="Visit Assignment" width="${visitAssignmentCol}"/>
</c:if>	
<tags:componentListColumnHeader component="${component}" label="Instrument" width="${instrCol}" sort="instrConfig.label"/>
<tags:componentListColumnHeader component="${component}" label="Opt${fmtColumns == 'Expanded' ? 'ional' : ''}" width="${instrOptionalCol}"/>
<c:if test="${fmtColumns == 'Expanded'}">
<tags:componentListColumnHeader component="${component}" label="Instrument Assignment" width="${instrAssignmentCol}"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="${fmtColumns == 'Expanded' ? 'Completion ' : ''}Status" width="${compStatusCol}" sort="instr.compStatus"/>
<tags:componentListColumnHeader component="${component}" label="Status Reason" width="${compStatusReasonCol}" sort="instr.compReason"/>
<c:if test="${fmtColumns == 'Expanded'}">
<tags:componentListColumnHeader component="${component}" label="Status Note" width="${compStatusNoteCol}"/>
</c:if>	
</tags:listRow>
</content>


<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolInstrument" component="protocolInstrument" idParam="${item.visitId}" noDelete="true"/>	    	                      
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
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="currStatus" component="${component}" listIndex="${iterator.index}" entityType="protocol"/>
			</tags:listCell>
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
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="visitLabel" component="${component}" listIndex="${iterator.index}" metadataName="protocolVisitConfig.label"/>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="visitOptional" component="${component}" listIndex="${iterator.index}" metadataName="protocolVisitConfig.label"/>
			</tags:listCell>
			<tags:listCell>
				<c:set var="visitLink">
					<tags:listActionURLButton buttonImage="view" actionId="lava.crms.scheduling.visit.visit" eventId="visit__view" idParam="${item.assignedVisitId}"/>	    
				</c:set>
				<tags:listField property="visitSummary" component="${component}" listIndex="${iterator.index}" metadataName="protocol.summary" link="${visitLink}"/>
			</tags:listCell>
		</c:if>	
		<tags:listCell>
			<tags:listField property="instrLabel" component="${component}" listIndex="${iterator.index}" metadataName="protocolInstrumentConfig.label"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="instrOptional" component="${component}" listIndex="${iterator.index}" metadataName="protocolInstrumentConfig.label"/>
		</tags:listCell>
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<c:set var="instrLink">
					<tags:listActionURLButton buttonImage="view" actionId="lava.crms.assessment.instrument.${item.assignedInstrTypeEncoded}" eventId="${item.assignedInstrTypeEncoded}__view" idParam="${item.assignedInstrId}"/>			
				</c:set>
				<tags:listField property="instrSummary" component="${component}" listIndex="${iterator.index}" metadataName="protocol.summary" link="${instrLink}"/>
			</tags:listCell>
		</c:if>	
		<tags:listCell>
			<tags:listField property="instrCompStatus" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.compStatus"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="instrCompReason" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.compReason"/>
		</tags:listCell>
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="instrCompNote" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.compNote"/>
			</tags:listCell>
		</c:if>	
	</tags:listRow>
</tags:list>
