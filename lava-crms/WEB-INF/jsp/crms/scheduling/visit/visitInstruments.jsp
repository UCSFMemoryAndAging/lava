<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>


<c:set var="component" value="visitInstruments"/>


<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix},${currentVisit.visitDescrip}</page:param>

	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="listFilters">
</content>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add" actionId="lava.crms.assessment.instrument.instrument" eventId="instrument__add" component="${component}"/>	    
</content>

<content tag="groupActions">
<tags:listInstrumentPrototypeGroupRow groupPrototype="${groupPrototype}" groupComponent="instrumentGroup" pageName="${component}"/>
<tags:listSelectedItemsGroupRow listComponent="${component}" groupComponent="instrumentGroup" pageName="${component}" selectedCountMsgKey="instrument.selectedCount"/>
</content>

<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}"  label="" width="2%"/>
	<tags:componentListColumnHeader component="${component}"  label="Action" width="10%"/>
	<tags:componentListColumnHeader component="${component}"  label="Measure"width="11%"/>
	<tags:componentListColumnHeader component="${component}"  label="Collection" width="20%"/>
	<tags:componentListColumnHeader component="${component}"  label="Data Entry" width="19%"/>
	<tags:componentListColumnHeader component="${component}"  label="Validation" width="19%"/>
	<tags:componentListColumnHeader component="${component}"  label="Summary" width="19%"/>
</tags:listRow>
</content>


<tags:list component="${component}">
	
	<tags:listRow>
		<tags:listCell styleClass="actionButton"> 
			<tags:listItemSelect component="${component}" listIndex="${iterator.index}" entityType="instrument" mode="le"/>
		</tags:listCell>
		<tags:listCell styleClass="actionButton">
		   <c:choose>
		        <c:when test="${fn:startsWith(item.instrTypeEncoded, 'macdiagnosis')}">
					<tags:listInstrumentActionURLStandardButtons actionId="lava.crms.assessment.diagnosis.${item.instrTypeEncoded}" idParam="${item.id}" instrTypeEncoded="${item.instrTypeEncoded}"/>
				</c:when>
			    <c:when test="${not empty instrumentConfig[item.instrTypeEncoded]}">
					<tags:listInstrumentActionURLStandardButtons actionId="lava.crms.assessment.instrument.${item.instrTypeEncoded}" idParam="${item.id}" instrTypeEncoded="${item.instrTypeEncoded}"/>
				</c:when>
				<c:otherwise>
					<tags:listActionURLButton buttonImage="delete" actionId="lava.crms.assessment.instrument.instrument" eventId="instrument__delete" idParam="${item.id}"/>	    
				</c:otherwise>
			</c:choose>					  
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

</page:applyDecorator> 
</page:applyDecorator>   
	    

