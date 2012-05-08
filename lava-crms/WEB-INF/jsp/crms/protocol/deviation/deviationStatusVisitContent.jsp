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
		<c:set var="visitCol" value="16%"/>
		<c:set var="visitOptionalCol" value="5%"/>
		<c:set var="schedStatusCol" value="13%"/>
		<c:set var="collectStatusCol" value="13%"/>
		<c:set var="compStatusCol" value="13%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Summary'}">
		<c:set var="actionCol" value="8%"/>
		<c:set var="patientCol" value="14%"/>
		<c:set var="projectCol" value="14%"/>
		<c:set var="protocolCol" value="14%"/>
		<c:set var="visitCol" value="14%"/>
		<c:set var="visitOptionalCol" value="4%"/>
		<c:set var="schedStatusCol" value="11%"/>
		<c:set var="collectStatusCol" value="11%"/>
		<c:set var="compStatusCol" value="10%"/>
	</c:when> 
	<c:when test="${view == 'Patient' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="4%"/>
		<c:set var="projectCol" value="7%"/>
		<c:set var="protocolCol" value="7%"/>
		<c:set var="timepointCol" value="7%"/>
		<c:set var="tpOptionalCol" value="3%"/>
		<c:set var="visitCol" value="7%"/>
		<c:set var="visitOptionalCol" value="3%"/>
		<c:set var="assignmentCol" value="13%"/>		
		<c:set var="schedWinCol" value="8%"/>
		<c:set var="idealSchedWinCol" value="8%"/>
		<c:set var="schedStatusCol" value="6%"/>
		<c:set var="schedStatusReasonCol" value="5%"/>
		<c:set var="collectWinCol" value="8%"/>
		<c:set var="idealCollectWinCol" value="8%"/>
		<c:set var="collectStatusCol" value="6%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="4%"/>
		<c:set var="patientCol" value="8%"/>
		<c:set var="projectCol" value="6%"/>
		<c:set var="protocolCol" value="6%"/>
		<c:set var="timepointCol" value="6%"/>
		<c:set var="tpOptionalCol" value="3%"/>
		<c:set var="visitCol" value="6%"/>
		<c:set var="visitOptionalCol" value="3%"/>
		<c:set var="assignmentCol" value="13%"/>		
		<c:set var="schedWinCol" value="8%"/>
		<c:set var="idealSchedWinCol" value="8%"/>
		<c:set var="schedStatusCol" value="4%"/>
		<c:set var="schedStatusReasonCol" value="5%"/>
		<c:set var="collectWinCol" value="8%"/>
		<c:set var="idealCollectWinCol" value="8%"/>
		<c:set var="collectStatusCol" value="4%"/>
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
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Timepoint" width="${timepointCol}" sort="timepointConfig.label"/>
	<tags:componentListColumnHeader component="${component}" label="Opt${fmtColumns == 'Expanded' ? 'ional' : ''}" width="${tpOptionalCol}"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="Visit" width="${visitCol}" sort="visitConfig.label"/>
<tags:componentListColumnHeader component="${component}" label="Opt${fmtColumns == 'Expanded' ? 'ional' : ''}" width="${visitOptionalCol}"/>
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Visit Assignment" width="${assignmentCol}"/>
	<tags:componentListColumnHeader component="${component}" label="Scheduling Window" width="${schedWinCol}" sort="schedWinStart"/>
</c:if>
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Ideal Window" width="${idealSchedWinCol}" sort="idealSchedWinStart"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="Scheduling Status" width="${schedStatusCol}" sort="schedWinStatus"/>
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Status Reason" width="${schedStatusReasonCol}"/>
	<tags:componentListColumnHeader component="${component}" label="Collection Window" width="${collectWinCol}" sort="collectWinStart"/>
</c:if>	
<c:if test="${fmtColumns == 'Expanded'}">
	<tags:componentListColumnHeader component="${component}" label="Ideal Window" width="${idealCollectWinCol}" sort="idealCollectWinStart"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="Collection Status" width="${collectStatusCol}" sort="collectWinStatus"/>
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
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="timepointLabel" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepointConfig.label"/>
			</tags:listCell>
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
			<tags:listCell>
				<tags:listField property="tpSchedWinStart" property2="tpSchedWinEnd" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.schedWinStart" separator="&nbsp;-&nbsp;"/>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="tpIdealSchedWinStart" property2="tpIdealSchedWinEnd" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.idealSchedWinStart" separator="&nbsp;-&nbsp;"/>
			</tags:listCell>
		</c:if>
		<tags:listCell>
			<tags:listField property="visitSchedWinStatus" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.schedWinStatus"/>
		</tags:listCell>
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="visitSchedWinStatusReason" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.schedWinStatusReason"/>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="tpCollectWinStart" property2="tpCollectWinEnd" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.collectWinStart" separator="&nbsp;-&nbsp;"/>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="tpIdealCollectWinStart" property2="tpIdealCollectWinEnd" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.idealCollectWinStart" separator="&nbsp;-&nbsp;"/>
			</tags:listCell>
		</c:if>
		<tags:listCell>
			<tags:listField property="visitCollectWinStatus" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.collectWinStatus"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>
