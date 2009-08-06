<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="projectUdsSubmissions"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
 	<page:param name="quicklinks"></page:param>
     <page:param name="pageHeadingArgs">${empty currentProject? 'All Project ' : currentProject.name}</page:param>
 
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	<page:param name="calendarField">dcDate</page:param>
	
	
<content tag="listFilters">
<tags:contentColumn columnClass="colLeft2Col5050">
		<tags:listFilterField property="instrType" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="formId" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="formVer" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="packet" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="visitNum" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="packetStatus" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="packetReason" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="packetDateStart" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="packetDateEnd" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="packetBy" component="${component}" entityType="udsinstrument"/>
	
	
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
	<tags:listFilterField property="dsStatus" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="dsReason" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="dsDateStart" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="dsDateEnd" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="dsBy" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="lcStatus" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="lcReason" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="lcDateStart" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="lcDateEnd" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="lcBy" component="${component}" entityType="udsinstrument"/>
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
	<tags:componentListColumnHeader component="${component}"  label="Measure"width="12%" sort="formId"/>
	<tags:componentListColumnHeader component="${component}"  label="UDS Visit" width="19%" sort="visitNum"/>
	<tags:componentListColumnHeader component="${component}"  label="Logic Check" width="19%" sort="lcDate"/>
	<tags:componentListColumnHeader component="${component}"  label="Submission" width="19%" sort="dsDate"/>
	
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
			<tags:listField property="patient.fullNameNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="instrument" metadataName="patient.fullNameNoSuffix"/>			
			<%-- because eventId is not specified, this will result in a new root flow, until the list flow definition allows entity CRUD
			     subflows on entity types other than the list type, at which time will use eventId="visit__view" --%>
			<tags:listActionURLButton buttonImage="view" actionId="lava.crms.people.patient.patient" idParam="${item.patient.id}"/>	    
		</tags:listCell>
	
		<tags:listCell>
 		    <tags:listField property="formId" component="${component}" listIndex="${iterator.index}" entityType="udsinstrument"/>:		
			<tags:listField property="instrType" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="udsVisitBlock" component="${component}" listIndex="${iterator.index}" entityType="udsinstrument"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="logicCheckStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="udsinstrument"/>			
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="submissionStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="udsinstrument"/>			
		</tags:listCell>
</tags:listRow>
</tags:list>
	

</page:applyDecorator>    

</page:applyDecorator>    
	    
