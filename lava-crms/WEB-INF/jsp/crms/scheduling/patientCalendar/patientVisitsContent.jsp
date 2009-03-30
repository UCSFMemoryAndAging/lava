<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<c:if test="${not empty currentPatient}">
<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.scheduling.visit.visit" eventId="visit__add" component="${component}"/>	    
</content>
</c:if>

<content tag="listFilters">
	
		<!-- Standard calendar filters -->
		<tags:contentColumn columnClass="colLeft2Col5050">
			<tags:listFilterField property="customDateStart" component="${component}" entityType="visit"/>
			<tags:listFilterField property="customDateEnd" component="${component}" entityType="visit"/>
			<tags:listFilterField property="projName" component="${component}" entityType="visit"/>
			</tags:contentColumn>
		<tags:contentColumn columnClass="colRight2Col5050">
			<tags:listFilterField property="visitType" component="${component}" entityType="visit"/>
			<tags:listFilterField property="visitLocation" component="${component}" entityType="visit"/>
			<tags:listFilterField property="visitStatus" component="${component}" entityType="visit"/>
			<tags:listFilterField property="visitWith" component="${component}" entityType="visit"/>
			<!-- custom filters -->
		</tags:contentColumn>
</content>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="11%"/>
<tags:componentListColumnHeader component="${component}"  label="Date" width="10%" sort="visitDate"/>
<tags:componentListColumnHeader component="${component}" label="Project" width="20%" sort="projName"/>
<tags:componentListColumnHeader component="${component}" label="Type" width="20%" sort="visitType"/>
<tags:componentListColumnHeader component="${component}" label="Location" width="10%" sort="visitLocation"/>
<tags:componentListColumnHeader component="${component}" label="Status" width="12%" sort="visitStatus"/>
<tags:componentListColumnHeader component="${component}" label="Staff" width="15%" sort="visitWith"/>
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
			<tags:listField property="visitType" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="visitLocation" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="visitStatus" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="visitWith" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
		</tags:listCell>
		
	</tags:listRow>
</tags:list>
