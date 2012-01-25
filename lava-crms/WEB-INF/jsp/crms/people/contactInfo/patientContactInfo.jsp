<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="patientContactInfo"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.contactInfo.contactInfo" eventId="contactInfo__add" component="${component}" locked="${currentPatient.locked}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
<%-- EMORY change: increased Action width from 7% to 9%, as wasn't displaying in one line in Firefox --%>
<tags:componentListColumnHeader component="${component}" label="Action" width="9%"/>
<tags:componentListColumnHeader component="${component}" label="Contact Name (Status)" width="20%"/>
<tags:componentListColumnHeader component="${component}"  label="Active" width="5%"/>
<tags:componentListColumnHeader component="${component}" label="Address" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Phone/Email" width="20%"/>
<tags:componentListColumnHeader component="${component}" label="Notes" width="20%"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.people.contactInfo.contactInfo" component="contactInfo" idParam="${item.id}" locked="${item.locked}"/>	    	                      
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="contactNameRev" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>
			<br/>(<tags:listField property="contactDesc" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>)
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="active" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="addressBlock" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>
			</tags:listCell>
		<tags:listCell>
			<tags:listField property="phoneEmailBlock" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="notes" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>
		</tags:listCell>
		
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

