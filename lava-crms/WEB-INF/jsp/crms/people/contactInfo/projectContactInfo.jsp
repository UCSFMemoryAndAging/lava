<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="projectContactInfo"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${empty currentProject? 'All Projects /' : currentProject.name}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="listFilters">
	
	<tags:contentColumn columnClass="colLeft2Col5050">
	
	<tags:listFilterField property="patient.firstName" component="${component}" entityType="contactInfo"/>
	<tags:listFilterField property="patient.lastName" component="${component}" entityType="contactInfo"/>
	<tags:listFilterField property="contactNameRev" component="${component}" entityType="contactInfo"/>
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
	
	<tags:listFilterField property="address" component="${component}" entityType="contactInfo"/>
	<tags:listFilterField property="city" component="${component}" entityType="contactInfo"/>
	<tags:listFilterField property="state" component="${component}" entityType="contactInfo"/>
	<tags:listFilterField property="zip" component="${component}" entityType="contactInfo"/>
	
	</tags:contentColumn>
</content>

<content tag="customActions">
	<c:if test="${not empty currentPatient}">
		<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.contactInfo.contactInfo" eventId="contactInfo__add" component="${component}" locked="${currentPatient.locked}"/>	    
	</c:if>
</content>

<content tag="listColumns">
<tags:listRow>
<%-- EMORY change: increased Action width from 7% to 9%, as wasn't displaying in one line in Firefox --%>
<tags:componentListColumnHeader component="${component}" label="Action" width="9%"/>
<tags:componentListColumnHeader component="${component}" label="Patient (Contact Name)" width="20%" sort="patient.fullNameRev"/>
<tags:componentListColumnHeader component="${component}"  label="Active" width="5%"/>
<tags:componentListColumnHeader component="${component}" label="Address" width="20%" sort="state,city,address"/>
<tags:componentListColumnHeader component="${component}" label="Phone/Email" width="20%" />
<tags:componentListColumnHeader component="${component}" label="Notes" width="20%" />
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.crms.people.contactInfo.contactInfo" component="contactInfo" idParam="${item.id}" locked="${item.locked}"/>	    	                      
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="patient.fullNameNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="patient" metadataName="patient.fullNameNoSuffix"/>
			
			<c:if test="${not empty item.caregiverId}">
				<br/>(<tags:listField property="contactNameRev" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>)
			</c:if>
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

