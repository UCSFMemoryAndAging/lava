<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.doctor.patientDoctor" eventId="patientDoctor__add" component="${component}" locked="${currentPatient.locked}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Doctor Name" width="15%"/>
<tags:componentListColumnHeader component="${component}" label="Relation To PT<br/>(Doctor Type)" width="15%"/>
<tags:componentListColumnHeader component="${component}" label="Location" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Phone/Email" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Notes" width="20%"/>
</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.people.doctor.patientDoctor" component="patientDoctor" idParam="${item.id}" locked="${item.locked}"/>	    
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="doctor.fullNameRev" component="${component}" listIndex="${iterator.index}" entityType="patientDoctor" metadataName="doctor.fullNameRev"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="docStat" component="${component}" listIndex="${iterator.index}" entityType="patientDoctor"/>		
			<br/>(<tags:listField property="doctor.docType" component="${component}" listIndex="${iterator.index}" entityType="patientDoctor" metadataName="doctor.docType"/>)
		</tags:listCell>
		<tags:listCell>
				<tags:listField property="doctor.addressBlock" component="${component}" listIndex="${iterator.index}" entityType="patientDoctor" metadataName="doctor.addressBlock"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="doctor.phoneEmailBlock" component="${component}" listIndex="${iterator.index}" entityType="patientDoctor" metadataName="doctor.phoneEmailBlock"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="docNote" component="${component}" listIndex="${iterator.index}" entityType="patientDoctor"/>		
		</tags:listCell>
	</tags:listRow>
</tags:list>
