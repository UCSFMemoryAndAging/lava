<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="patientContactLog"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix}</page:param>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>


<content tag="customActions">
	<tags:actionURLButton buttonText="Add"  actionId="lava.crms.people.contactLog.contactLog" eventId="contactLog__add" component="${component}"/>	    
</content>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="8%"/>
<tags:componentListColumnHeader component="${component}" label="Project" width="10%"/>
<tags:componentListColumnHeader component="${component}" label="Contact" width="17%"/>
<tags:componentListColumnHeader component="${component}" label="Log Date<br/>(Method)" width="10%"/>
<tags:componentListColumnHeader component="${component}" label="Staff Member<br/>(Staff Initiated)" width="15%"/>
<tags:componentListColumnHeader component="${component}" label="Note" width="40%"/>
</tags:listRow>
</content>





<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLStandardButtons actionId="lava.crms.people.contactLog.contactLog" component="contactLog" idParam="${item.id}"/>	    	                      
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="projName" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>	
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="contact" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="logDate" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
			<br/><tags:listField property="method" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
			</tags:listCell>
		<tags:listCell>
			<tags:listField property="staff" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
			<br/><tags:listField property="staffInit" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="note" component="${component}" listIndex="${iterator.index}" entityType="contactLog"/>
		</tags:listCell>
		
	</tags:listRow>
</tags:list>

</page:applyDecorator>    
</page:applyDecorator>	    

