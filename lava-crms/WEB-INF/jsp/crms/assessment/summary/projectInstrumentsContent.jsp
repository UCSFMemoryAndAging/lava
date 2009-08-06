<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<content tag="listFilters">
<tags:contentColumn columnClass="colLeft2Col5050">
			<tags:listFilterField property="customDateStart" component="${component}" entityType="instrument"/>
			<tags:listFilterField property="customDateEnd" component="${component}" entityType="instrument"/>
			<tags:listFilterField property="patient.firstName" component="${component}" entityType="instrument"/>
			<tags:listFilterField property="patient.lastName" component="${component}" entityType="instrument"/>
			<tags:listFilterField property="dcStatus" component="${component}" entityType="instrument"/>
			<tags:listFilterField property="dcBy" component="${component}" entityType="instrument"/>
		</tags:contentColumn>
		<tags:contentColumn columnClass="colRight2Col5050">
			<tags:listFilterField property="instrType" component="${component}" entityType="instrument"/>
			<tags:listFilterField property="deStatus" component="${component}" entityType="instrument"/>
			<tags:listFilterField property="deBy" component="${component}" entityType="instrument"/>
			<tags:listFilterField property="deDateStart" component="${component}" entityType="instrument"/>
			<tags:listFilterField property="deDateEnd" component="${component}" entityType="instrument"/>
		</tags:contentColumn>
</content>

<c:if test="${not empty currentPatient}">
<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.assessment.instrument.instrument" eventId="instrument__add" component="${component}"/>	    
</content>
</c:if>

<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}"  label="Action" width="11%"/>
	<tags:componentListColumnHeader component="${component}"  label="Patient" width="19%" sort="patient.fullNameRevNoSuffix"/>
	<tags:componentListColumnHeader component="${component}"  label="Measure"width="12%" sort="instrType"/>
	<tags:componentListColumnHeader component="${component}"  label="Collection" width="13%" sort="dcDate"/>
	<tags:componentListColumnHeader component="${component}"  label="Data Entry" width="13%" sort="deDate"/>
	<tags:componentListColumnHeader component="${component}"  label="Verify" width="13%" sort="dvDate"/>
	<tags:componentListColumnHeader component="${component}"  label="Summary" width="19%"/>
</tags:listRow>
</content>


<tags:list component="${component}">
	
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
		   <c:choose>
		    	<c:when test="${not empty instrumentConfig[item.instrTypeEncoded]}"> <%-- implemented instrument --%>
		    		<tags:listInstrumentActionURLStandardButtons actionId="lava.crms.assessment.instrument.${item.instrTypeEncoded}" idParam="${item.id}" instrTypeEncoded="${item.instrTypeEncoded}"/>
				</c:when>
				<c:otherwise>
					<tags:listActionURLButton buttonImage="delete" actionId="lava.crms.assessment.instrument.instrument" eventId="instrument__delete" idParam="${item.id}"/>	    
				</c:otherwise>
			</c:choose>					  
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="patient.fullNameNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="instrument" metadataName="patient.fullNameNoSuffix""/>			
			<%-- because eventId is not specified, this will result in a new root flow, until the list flow definition allows entity CRUD
			     subflows on entity types other than the list type, at which time will use eventId="visit__view" --%>
			<tags:listActionURLButton buttonImage="view" actionId="lava.crms.people.patient.patient" idParam="${item.patient.id}"/>	    
		</tags:listCell>
	
		<tags:listCell>
			<tags:listField property="instrType" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="collectionStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="entryStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>			
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="verifyStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>			
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="summary" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>			
		</tags:listCell>
</tags:listRow>
</tags:list>

