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
		<c:set var="timepointCol" value="16%"/>
		<c:set var="optionalCol" value="5%"/>
		<c:set var="collectWinCol" value="27%"/>
		<c:set var="collectStatusCol" value="12%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Summary'}">
		<c:set var="actionCol" value="8%"/>
		<c:set var="patientCol" value="16%"/>
		<c:set var="projectCol" value="14%"/>
		<c:set var="protocolCol" value="14%"/>
		<c:set var="timepointCol" value="14%"/>
		<c:set var="optionalCol" value="4%"/>
		<c:set var="collectWinCol" value="20%"/>
		<c:set var="collectStatusCol" value="10%"/>
	</c:when> 
	<c:when test="${view == 'Patient' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="6%"/>
		<c:set var="projectCol" value="9%"/>
		<c:set var="protocolCol" value="9%"/>
		<c:set var="protocolStatusCol" value="9%"/>
		<c:set var="protocolStatusReasonCol" value="9%"/>
		<c:set var="protocolStatusNoteCol" value="18%"/>
		<c:set var="timepointCol" value="9%"/>
		<c:set var="optionalCol" value="4%"/>
		<c:set var="collectWinCol" value="11%"/>
		<c:set var="anchorDateCol" value="7%"/>
		<c:set var="collectStatusCol" value="9%"/>
	</c:when> 
	<c:when test="${view == 'Project' && fmtColumns == 'Expanded'}">
		<c:set var="actionCol" value="6%"/>
		<c:set var="patientCol" value="10%"/>
		<c:set var="projectCol" value="8%"/>
		<c:set var="protocolCol" value="8%"/>
		<c:set var="protocolStatusCol" value="8%"/>
		<c:set var="protocolStatusReasonCol" value="8%"/>
		<c:set var="protocolStatusNoteCol" value="14%"/>
		<c:set var="timepointCol" value="8%"/>
		<c:set var="optionalCol" value="4%"/>
		<c:set var="collectWinCol" value="10%"/>
		<c:set var="anchorDateCol" value="7%"/>
		<c:set var="collectStatusCol" value="9%"/>
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
	<tags:componentListColumnHeader component="${component}" label="Protocol Status" width="${protocolStatusCol}"/>
	<tags:componentListColumnHeader component="${component}" label="Status Reason" width="${protocolStatusReasonCol}"/>
	<tags:componentListColumnHeader component="${component}" label="Status Note" width="${protocolStatusNotesCol}"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="Timepoint" width="${timepointCol}" sort="timepointConfig.label"/>
<tags:componentListColumnHeader component="${component}" label="Opt${fmtColumns == 'Expanded' ? 'ional' : ''}" width="${optionalCol}"/>
<tags:componentListColumnHeader component="${component}" label="Collection Window" width="${collectWinCol}" sort="collectWinStart"/>
<c:if test="${fmtColumns == 'Expanded'}">
<tags:componentListColumnHeader component="${component}" label="Anchor Date" width="${anchorDateCol}" sort="collectWinAnchorDate"/>
</c:if>
<tags:componentListColumnHeader component="${component}" label="${fmtColumns == 'Expanded' ? 'Collection ' : ''}Status" width="${collectStatusCol}" sort="collectWinStatus"/>
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
		<tags:listCell>
			<tags:listField property="timepointOptional" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepointConfig.label"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="tpCollectWinStart" property2="tpCollectWinEnd" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.collectWinStart" separator="&nbsp;-&nbsp;"/>
		</tags:listCell>
		<c:if test="${fmtColumns == 'Expanded'}">
			<tags:listCell>
				<tags:listField property="tpCollectWinAnchorDate" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.collectWinAnchorDate"/>
			</tags:listCell>
		</c:if>
		<tags:listCell>
			<tags:listField property="tpCollectWinStatus" component="${component}" listIndex="${iterator.index}" metadataName="protocolTimepoint.collectWinStatus"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>
