<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>


<c:set var="component" value="patientUdsSubmissions"/>



<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix}</page:param>

	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
   
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

<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.assessment.instrument.instrument" eventId="instrument__add" component="${component}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}"  label="Action" width="11%"/>
	<tags:componentListColumnHeader component="${component}"  label="Measure" width="19%" sort="formId"/>
	<tags:componentListColumnHeader component="${component}"  label="UDS Visit" width="20%" sort="visitNum"/>
	<tags:componentListColumnHeader component="${component}"  label="Logic Check" width="20%" sort="lcDate"/>
	<tags:componentListColumnHeader component="${component}"  label="Submission" width="20%" sort="dsDate"/>
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
	    

