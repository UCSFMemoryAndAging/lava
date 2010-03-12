<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>


<%-- filters not supported on secondary lists yet by the flow definition
<content tag="listFilters">
	<tags:listFilterField property="patient.firstName" component="${component}" entityType="patientDoctor"/>
	<tags:listFilterField property="patient.lastName" component="${component}" entityType="patientDoctor"/>
</content>


<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.doctor.doctor" startMode="add" component="${component}"/>	    
</content>
--%>

<content tag="listColumns">
<tags:listRow>
<%-- secondary list CRUD actions are not yet supported by the flow
<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
--%>
<tags:componentListColumnHeader component="${component}" label="Patient Name" width="25%" sort="patient.fullNameRevNoSuffix"/>
<tags:componentListColumnHeader component="${component}" label="Relation To PT<br/>(Doctor Type)" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Notes" width="40%"/>
</tags:listRow>
</content>
<tags:list component="doctorPatients">
	<tags:listRow>
<%--	
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.people.patient.patient" component="patient" idParam="${item.patient.id}" locked="${item.locked}"/>	    
		</tags:listCell>
--%>		
		<tags:listCell>
			<tags:listField property="patient.fullNameRevNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="patientDoctor" metadataName="patient.fullNameNoSuffix"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="docStat" component="${component}" listIndex="${iterator.index}" entityType="patientDoctor"/>
			<br/>(<tags:listField property="doctor.docType" component="${component}" listIndex="${iterator.index}" entityType="patientDoctor" metadataName="doctor.docType"/>)
		</tags:listCell>
		<tags:listCell>
				<tags:listField property="docNote" component="${component}" listIndex="${iterator.index}" entityType="patientDoctor"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>