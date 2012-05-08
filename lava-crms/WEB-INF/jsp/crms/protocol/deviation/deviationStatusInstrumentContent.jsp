<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="view" value="${param.view}"/>
<c:set var="component" value="${param.component}"/>
<c:set var="fmtColumns" value="${param.fmtColumns}"/>

<%-- set list column widths --%>
<c:choose>
	<c:when test="${view == 'Patient' && fmtColumns == 'Summary'}">
		<c:set var="actionCol" value="8%"/>
		<c:set var="projectCol" value="17%"/>
		<c:set var="protocolCol" value="17%"/>
		<c:set var="timepointCol" value="17%"/>
		<c:set var="instrCol" value="17%"/>
		<c:set var="instrOptionalCol" value="5%"/>
		<c:set var="collectStatusCol" value="10%"/>
		<c:set var="collectStatusReasonCol" value="9%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Summary'}">
		<c:set var="actionCol" value="6%"/>
		<c:set var="patientCol" value="18%"/>
		<c:set var="projectCol" value="13%"/>
		<c:set var="protocolCol" value="13%"/>
		<c:set var="timepointCol" value="13%"/>
		<c:set var="instrCol" value="13%"/>
		<c:set var="instrOptionalCol" value="4%"/>
		<c:set var="collectStatusCol" value="10%"/>
		<c:set var="collectStatusReasonCol" value="10%"/>
	</c:when> 
	<c:when test="${view == 'Patient' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="3%"/>
		<c:set var="projectCol" value="7%"/>
		<c:set var="protocolCol" value="7%"/>
		<c:set var="timepointCol" value="7%"/>
		<c:set var="tpOptionalCol" value="3%"/>
		<c:set var="visitCol" value="7%"/>
		<c:set var="visitOptionalCol" value="3%"/>
		<c:set var="visitAssignmentCol" value="11%"/>
		<c:set var="instrCol" value="7%"/>
		<c:set var="instrOptionalCol" value="3%"/>
		<c:set var="instrAssignmentCol" value="11%"/>
		<c:set var="collectWinCol" value="9%"/>
		<c:set var="idealCollectWinCol" value="9%"/>
		<c:set var="collectStatusCol" value="7%"/>
		<c:set var="collectStatusReasonCol" value="6%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="3%"/>
		<c:set var="patientCol" value="9%"/>
		<c:set var="projectCol" value="6%"/>
		<c:set var="protocolCol" value="6%"/>
		<c:set var="timepointCol" value="6%"/>
		<c:set var="tpOptionalCol" value="3%"/>
		<c:set var="visitCol" value="6%"/>
		<c:set var="visitOptionalCol" value="3%"/>
		<c:set var="visitAssignmentCol" value="10%"/>
		<c:set var="instrCol" value="6%"/>
		<c:set var="instrOptionalCol" value="3%"/>
		<c:set var="instrAssignmentCol" value="10%"/>
		<c:set var="collectWinCol" value="8%"/>
		<c:set var="idealCollectWinCol" value="8%"/>
		<c:set var="collectStatusCol" value="7%"/>
		<c:set var="collectStatusReasonCol" value="6%"/>
	</c:when> 
</c:choose>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="${actionCol}"/>
<c:if test="${view == 'Project'}">
	<tags:componentListColumnHeader component="${component}" label="Patient" width="${patientCol}" sort="patient.fullNameRevNoSuffix"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="Project" width="${projectCol}" sort="projName"/>
<tags:componentListColumnHeader component="${component}" label="Protocol" width="${protocolCol}" sort="protocolConfig.label"/>
<tags:componentListColumnHeader component="${component}" label="Timepoint" width="${timepointCol}" sort="timepointConfig.label"/>
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Optional" width="${tpOptionalCol}"/>
	<tags:componentListColumnHeader component="${component}" label="Visit" width="${visitCol}" sort="visitConfig.label"/>
	<tags:componentListColumnHeader component="${component}" label="Optional" width="${visitOptionalCol}"/>
	<tags:componentListColumnHeader component="${component}" label="Visit Assignment" width="${visitAssignmentCol}"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="Instrument" width="${instrCol}" sort="instrConfig.label"/>
<tags:componentListColumnHeader component="${component}" label="Opt${fmtColumns == 'Expanded' ? 'ional' : ''}" width="${instrOptionalCol}"/>
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Instrument Assignment" width="${instrAssignmentCol}"/>
	<tags:componentListColumnHeader component="${component}" label="Collection Window" width="${collectWinCol}" sort="collectWinStart"/>
	<tags:componentListColumnHeader component="${component}" label="Ideal Window" width="${idealCollectWinCol}" sort="idealCollectWinStart"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="Collection Status" width="${collectStatusCol}" sort="collectWinStatus"/>
<tags:componentListColumnHeader component="${component}" label="Status Reason" width="${collectStatusReasonCol}"/>
</tags:listRow>
</content>

<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.protocol.assignment.protocolTimepoint" component="protocolTimepoint" idParam="${item.id}" noDelete="true"/>	    	                      
		</tags:listCell>
		<c:if test="${view == 'Project'}">
			<tags:listCell>
				<tags:listField property="fullNameRevNoSuffix" component="${component}" listIndex="${iterator.index}" metadataName="patient.fullNameRevNoSuffix"/>
			</tags:listCell>
		</c:if>			
		<tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="protocol"/>
		</tags:listCell>
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
			<tags:listCell>
				<tags:listField property="instrCollectWinStart" property2="instrCollectWinEnd" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.collectWinStart" separator="&nbsp;-&nbsp;"/>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="instrIdealInstrCollectWinStart" property2="instrIdealInstrCollectWinEnd" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.idealCollectWinStart" separator="&nbsp;-&nbsp;"/>
			</tags:listCell>
		</c:if>
		<tags:listCell>
			<tags:listField property="instrCollectWinStatus" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.collectWinStatus"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="instrCollectWinReason" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.collectWinStatusReason"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>
