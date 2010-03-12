<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>


<content tag="listColumns">
<tags:listRow>
<%-- <tags:componentListColumnHeader component="${component}" label="Action" width="11%"/> --%>
<tags:componentListColumnHeader component="${component}" label="Date" width="14%" sort="visitDate"/>
<tags:componentListColumnHeader component="${component}" label="Project" width="20%" sort="projName"/>
<tags:componentListColumnHeader component="${component}" label="Type" width="21%" sort="visitType"/>
<tags:componentListColumnHeader component="${component}" label="Location" width="13%" sort="visitLocation"/>
<tags:componentListColumnHeader component="${component}" label="Status" width="15%" sort="visitStatus"/>
<tags:componentListColumnHeader component="${component}" label="Staff" width="17%" sort="visitWith"/>
</tags:listRow>
</content>
<tags:list component="${component}">
	<tags:listRow>
<%-- remove actions until these CRUD subflows are supported in the CRUD entity flows. for now, this list just serves as
     as secondary component reference list, which is fine; these actions may never be added back in	
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.scheduling.visit.visit" component="visit" idParam="${item.id}" locked="${item.locked}"/>
			   was commented: going to visitInstruments list starts a new root flow, so do not specify eventId attribute 
			<tags:listActionURLButton buttonImage="list" actionId="lava.crms.scheduling.visit.visitInstruments" idParam="${item.id}"/>
		</tags:listCell>
--%>		
		<tags:listCell>
			<tags:listField property="visitDate" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
		    <tags:listField property="visitTime" component="${component}" listIndex="${iterator.index}" entityType="visit"/>
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