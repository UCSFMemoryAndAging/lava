<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<c:if test="${not empty currentPatient}">
<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.scheduling.visit.visit" eventId="visit__add" component="${component}"/>	    
</content>
</c:if>



<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="11%"/>
<tags:componentListColumnHeader component="${component}"  label="Date" width="10%" sort="visitDate"/>
<tags:componentListColumnHeader component="${component}" label="Project" width="15%" sort="projName"/>
<tags:componentListColumnHeader component="${component}" label="Patient" width="20%" sort="patient.fullNameRev"/>
<tags:componentListColumnHeader component="${component}" label="Type" width="20%" sort="visitType"/>
<tags:componentListColumnHeader component="${component}" label="Location" width="10%" sort="visitLocation"/>
<tags:componentListColumnHeader component="${component}" label="Status" width="12%" sort="visitStatus"/>
</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		 <tags:listCell styleClass="actionButton">
				<tags:listActionURLStandardButtons actionId="lava.crms.scheduling.visit.visit" component="visit" idParam="${item.id}"/>
				<%-- going to visitInstruments list starts a new root flow, so do not specify eventId attribute --%>	
				<tags:listActionURLButton buttonImage="list" actionId="lava.crms.scheduling.visit.visitInstruments" idParam="${item.id}"/>
		</tags:listCell>
	   <tags:listCell>
			<tags:listField property="visitDate" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="patient.fullNameNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="visit" metadataName="patient.fullNameNoSuffix"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="visitType" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="visitLocation" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="visitStatus" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
		</tags:listCell>
		
	</tags:listRow>
</tags:list>
