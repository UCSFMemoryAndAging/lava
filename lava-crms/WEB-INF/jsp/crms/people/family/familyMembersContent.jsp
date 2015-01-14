<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="PIDN" width="10%" sort="id"/>
<tags:componentListColumnHeader component="${component}" label="Name" width="30%" sort="fullNameRevNoSuffix" />
<tags:componentListColumnHeader component="${component}" label="Relation to<br/>Proband" width="18%" sort="relationToProband" />
<tags:componentListColumnHeader component="${component}" label="Twin" width="15%" sort="twin"/>	
<tags:componentListColumnHeader component="${component}" label="Date of Birth" width="15%" sort="birthDate"/>
<tags:componentListColumnHeader component="${component}" label="Age" width="8%" sort="birthDate"/>	
</tags:listRow>
</content>

<tags:list component="${component}">
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLButton buttonImage="view" actionId="lava.crms.people.patient.patient" idParam="${item.id}"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="id" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="fullNameRevNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="relationToProband" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="twinZygosity" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="birthDate" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="age" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
</tags:listRow>
</tags:list>
