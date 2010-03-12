<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.enrollment.consent.consent" eventId="consent__add" component="${component}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Project (Site)" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Consent Type" width="30%"/>
<tags:componentListColumnHeader component="${component}" label="Declined" width="15%"/>
<tags:componentListColumnHeader component="${component}" label="Consent Date" width="18%"/>
</tags:listRow>
</content>


<tags:list component="${component}">
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.enrollment.consent.consent" component="consent" idParam="${item.id}" locked="${item.locked}"/>	    
		</tags:listCell>
	    <tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="consent"/>
		</tags:listCell>
		  <tags:listCell>
		  		<tags:listField property="consentType" component="${component}" listIndex="${iterator.index}" entityType="consent"/>
		  </tags:listCell>
		<tags:listCell>
			<tags:listField property="consentDeclined" component="${component}" listIndex="${iterator.index}" entityType="consent"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="consentDate" component="${component}" listIndex="${iterator.index}" entityType="consent"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>
