<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>


<c:set var="component" value="patientUdsInstruments"/>



<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix}</page:param>

	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
   
<content tag="listFilters">
<tags:contentColumn columnClass="colLeft2Col5050">
	
	<tags:listFilterField property="instrType" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="visit.projName" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="visit.visitType" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="formId" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="formVer" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="packet" component="${component}" entityType="udsinstrument"/>
	<tags:listFilterField property="visitNum" component="${component}" entityType="udsinstrument"/>
	
	
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
	<tags:listFilterField property="dcStatus" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="deStatus" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="dcDateStart" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="dcDateEnd" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="dcBy" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="deDateStart" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="deDateEnd" component="${component}" entityType="instrument"/>
	<tags:listFilterField property="deBy" component="${component}" entityType="instrument"/>
	
	
	</tags:contentColumn>
</content>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.assessment.instrument.instrument" eventId="instrument__add" component="${component}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}"  label="Action" width="11%"/>
	<tags:componentListColumnHeader component="${component}"  label="Measure"width="12%" sort="formId"/>
	<tags:componentListColumnHeader component="${component}"  label="Visit" width="19%" sort="visit.visitDate"/>
	<tags:componentListColumnHeader component="${component}"  label="Collection" width="13%" sort="dcDate"/>
	<tags:componentListColumnHeader component="${component}"  label="Data Entry" width="13%" sort="deDate"/>
	<tags:componentListColumnHeader component="${component}"  label="Validation" width="13%" sort="dvDate"/>
	<tags:componentListColumnHeader component="${component}"  label="UDS Visit" width="19%" sort="visitNum"/>
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
			<tags:listField property="formId" component="${component}" listIndex="${iterator.index}" entityType="udsinstrument"/>: <tags:listField property="instrType" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="visit.visitDate" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>
			<%-- because eventId is not specified, this will result in a new root flow, until the list flow definition allows entity CRUD
			     subflows on entity types other than the list type, at which time will use eventId="visit__view" --%>
			<tags:listActionURLButton buttonImage="view" actionId="lava.crms.scheduling.visit.visit" idParam="${item.visit.id}"/><BR/>
			<tags:listField property="visit.projName" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>:			
			<tags:listField property="visit.visitType" component="${component}" listIndex="${iterator.index}" entityType="instrument"/><BR/>			

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
			<tags:listField property="udsVisitBlock" component="${component}" listIndex="${iterator.index}" entityType="udsinstrument"/>
		</tags:listCell>
</tags:listRow>
</tags:list>

</page:applyDecorator> 
</page:applyDecorator>   
	    

