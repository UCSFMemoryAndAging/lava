<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- this is included by the standard Patient and Project Patient attachment lists that have section links
in the People, Enrollment, Scheduling and Assessment modules --%>

<c:set var="component" value="${param.component}"/>

<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>

<content tag="listFilters">
	<tags:contentColumn columnClass="colLeft2Col5050">
		<tags:listFilterField property="category" component="${component}" entityType="crmsFile"/>
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
		<tags:listFilterField property="contentType" component="${component}" entityType="crmsFile"/>
	</tags:contentColumn>
</content>

<c:if test="${not empty currentPatient}">
<content tag="customActions">
	<tags:actionURLButton buttonText="Add" actionId="lava.crms.people.attachments.patientAttachment" eventId="patientAttachment__add" component="${component}"/>	    
	
</content>
</c:if>

<content tag="listColumns">
<tags:listRow>
<tags:componentListColumnHeader component="${component}" label="Action" width="12%"/>
<tags:componentListColumnHeader component="${component}" label="Patient" width="20%" sort="patient.fullNameRevNoSuffix"/>
<tags:componentListColumnHeader component="${component}" label="Category" width="24%" sort="category"/>
<tags:componentListColumnHeader component="${component}" label="Type" width="24%" sort="contentType"/>
<tags:componentListColumnHeader component="${component}" label="File Status" width="20%" sort="fileStatusDate"/>
</tags:listRow>
</content>

<tags:list component="${component}" >
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
				<tags:listActionURLButton buttonImage="view" actionId="lava.crms.people.attachments.patientAttachment" eventId="patientAttachment__view" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="edit" actionId="lava.crms.people.attachments.patientAttachment" eventId="patientAttachment__edit" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="delete" actionId="lava.crms.people.attachments.patientAttachment" eventId="patientAttachment__delete" idParam="${item.id}"/>	    
				<tags:listActionURLButton buttonImage="download" actionId="lava.crms.people.attachments.patientAttachment" eventId="patientAttachment__download" idParam="${item.id}"/>	    
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="patient.fullNameRevNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="crmsFile"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="category" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="contentType" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/><br/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="statusBlock" component="${component}" listIndex="${iterator.index}" entityType="lavaFile"/>
		</tags:listCell>
		
	</tags:listRow>
</tags:list>

</page:applyDecorator>    

