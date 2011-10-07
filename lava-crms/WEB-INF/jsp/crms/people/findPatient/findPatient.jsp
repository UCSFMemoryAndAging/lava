<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>


<c:set var="component" value="findPatient"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>

	<page:param name="pageHeadingArgs">${empty currentProject ? 'Any Project' : currentProject.name}</page:param>


<!-- determine the number of columns in the resultset based on the number of filters applied -->
<c:set var="extraColumns" value="${0}"/>
<tags:ifListFilterFieldUsed component="${component}" property="caregiverFirstName">
	<c:set var="caregiverFilter" value="true"/>
	<c:set var="extraColumns" value="${extraColumns+1}"/>
</tags:ifListFilterFieldUsed>

<tags:ifListFilterFieldUsed component="${component}" property="caregiverLastName">
	<c:if test="${empty caregiverFilter}">
		<c:set var="caregiverFilter" value="true"/>
		<c:set var="extraColumns" value="${extraColumns+1}"/>
	</c:if>
</tags:ifListFilterFieldUsed>

<tags:ifListFilterFieldUsed component="${component}" property="contactInfoPhone">
	<c:set var="phoneFilter" value="true"/>
	<c:set var="extraColumns" value="${extraColumns+3}"/>
</tags:ifListFilterFieldUsed>
<tags:ifListFilterFieldUsed component="${component}" property="contactInfoEmail">
	<c:set var="emailFilter" value="true"/>
	<c:set var="extraColumns" value="${extraColumns+1}"/>
</tags:ifListFilterFieldUsed>

<tags:ifListFilterFieldUsed component="${component}" property="specimenId">
	<c:set var="specimenFilter" value="true"/>
	<c:set var="extraColumns" value="${extraColumns+4}"/>
</tags:ifListFilterFieldUsed>
<tags:ifListFilterFieldUsed component="${component}" property="neuroimagingId">
	<c:set var="neuroimagingFilter" value="true"/>
	<c:set var="extraColumns" value="${extraColumns+3}"/>
</tags:ifListFilterFieldUsed>
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	<c:choose>
		<c:when test="${empty extraColumns || extraColumns <= 1}">
			<!-- use default table width -->
		</c:when>
		<c:when test="${extraColumns <= 4}">
			<page:param name="widthClass">medium</page:param>
		</c:when>
		<c:otherwise>
			<page:param name="widthClass">wide</page:param>
		</c:otherwise>
	</c:choose>


<content tag="listFilters">


<tags:tableForm>
		<tags:listRow>
			<tags:listColumnHeader  label="" width="20%"/>
			<tags:listColumnHeader  label="" width="10%"/>
			<tags:listColumnHeader  label="" width="30%"/>
			<tags:listColumnHeader  label="" width="10%"/>
			<tags:listColumnHeader  label="" width="30%"/>
		</tags:listRow>
		<tags:listRow>
			<tags:listCell>
				<tags:outputText text="Patient / Subject Name" styleClass="bold"/>
				<tags:outputBlankLines n="1"/>
			</tags:listCell>
			<tags:listCell>
				<tags:outputText text="First<br/>(or de-ident.)" />
			</tags:listCell>
			<tags:listCell>
					<tags:listFilterField property="firstName"  component="${component}" entityType="findPatient"/>
			</tags:listCell>
			<tags:listCell>
				<tags:outputText text="Last"  />
			</tags:listCell>
			<tags:listCell>
				<tags:listFilterField property="lastName"  component="${component}" entityType="findPatient"/>
			</tags:listCell>
			
			
		</tags:listRow>
		<tags:listRow>
			<tags:listCell>
				<tags:outputText text="Date of Birth"  styleClass="bold"/>
				<tags:outputBlankLines n="1"/>
			</tags:listCell>
			<tags:listCell>
				<tags:outputText text="DOB is<br/>(or after)" />
			</tags:listCell>
			<tags:listCell>
				<tags:listFilterField property="birthDateStart"  component="${component}" entityType="findPatient"/>
			</tags:listCell>
			<tags:listCell>
				<tags:outputText text="and before" inline="true" />
			</tags:listCell>
			<tags:listCell>
				<tags:listFilterField property="birthDateEnd"  component="${component}" entityType="findPatient"/>
			</tags:listCell>
		</tags:listRow>
		<tags:listRow>
			<tags:listCell>
				<tags:outputText text="Date of Death"  styleClass="bold"/>
				<tags:outputBlankLines n="1"/>
			</tags:listCell>
			<tags:listCell>
				<tags:outputText text="DOD is<br/>(or after)" />
			</tags:listCell>
			<tags:listCell>
				<tags:listFilterField property="deathDateStart"  component="${component}" entityType="findPatient"/>
			</tags:listCell>
			<tags:listCell>
				<tags:outputText text="and before" inline="true" />
			</tags:listCell>
			<tags:listCell>
				<tags:listFilterField property="deathDateEnd"  component="${component}" entityType="findPatient"/>
			</tags:listCell>
		</tags:listRow>
		<tags:listRow>
			<tags:listCell>
				<tags:outputText text="Caregiver / Informant Name"  styleClass="bold"/>
			</tags:listCell>
			<tags:listCell>
				<tags:outputText text="First" />
			</tags:listCell>
			<tags:listCell>
				<tags:listFilterField property="caregiverFirstName"  component="${component}" entityType="findPatient"/>
			</tags:listCell>
			<tags:listCell>
				<tags:outputText text="Last" />
			</tags:listCell>
			<tags:listCell>
				<tags:listFilterField property="caregiverLastName"  component="${component}" entityType="findPatient"/>
			</tags:listCell>
		</tags:listRow>
		<tags:listRow>
			<tags:listCell>
				<tags:outputText text="Contact Information<br/>(Patient or Caregiver)"  styleClass="bold"/>
			</tags:listCell>
			<tags:listCell>
				<tags:outputText text="Phone" />
			</tags:listCell>
			<tags:listCell>
				<tags:listFilterField property="contactInfoPhone"  component="${component}" entityType="findPatient"/>
			</tags:listCell>
			<tags:listCell>
				<tags:outputText text="Email" />
			</tags:listCell>
			<tags:listCell>
				<tags:listFilterField property="contactInfoEmail"  component="${component}" entityType="findPatient"/>
			</tags:listCell>
		</tags:listRow>
</tags:tableForm>
<tags:outputBlankLines n="1"/>
</content>




<tags:componentListHasResults component="${component}">

<content tag="listColumns">
<tags:listRow>
	<tags:componentListColumnHeader component="${component}" label="Action" width="20px"/>
	<tags:componentListColumnHeader component="${component}" label="PIDN" width="20px"sort="id"/>
	<tags:componentListColumnHeader component="${component}" label="Name" width="80px" sort="fullNameRevNoSuffix"/>
	<tags:componentListColumnHeader component="${component}" label="Date of Birth" width="20px" sort="birthDate"/>
	<tags:componentListColumnHeader component="${component}" label="Deceased" width="30px" sort="deathDate"/>
	<c:if test="${caregiverFilter == true}">
		<tags:componentListColumnHeader component="${component}" label="Caregiver" width="80px" sort="caregiver.fullNameRev"/>
	</c:if>
	<c:if test="${phoneFilter == true}">
		<tags:componentListColumnHeader component="${component}" label="Phone1" width="75px" sort="contactInfo.phone1"/>
		<tags:componentListColumnHeader component="${component}" label="Phone2" width="75px" sort="contactInfo.phone2"/>
		<tags:componentListColumnHeader component="${component}" label="Phone3" width="75px" sort="contactInfo.phone3"/>
	</c:if>
	<c:if test="${emailFilter == true}">
		<tags:componentListColumnHeader component="${component}" label="Email" width="100px" sort="contactInfo.email"/>
	</c:if>

</tags:listRow>
</content>



<tags:list component="${component}">
	
	<tags:listRow>
		<tags:listCell styleClass="actionButton">
			<tags:listActionURLStandardButtons actionId="lava.people.patient.patient" component="patient" idParam="${item.id}"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="id" component="${component}" listIndex="${iterator.index}" entityType="patient" />		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="fullNameRevNoSuffix" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="birthDate" component="${component}" listIndex="${iterator.index}" entityType="patient"/>		
		</tags:listCell>
		<tags:listCell>
			<c:if test="${item.deceased == true}">
			<tags:listField property="deceased" component="${component}" listIndex="${iterator.index}" entityType="patient"/>
				<c:if test="${not empty item.deathDate}">		
				:<tags:listField property="deathDate" component="${component}" listIndex="${iterator.index}" entityType="patient" />		
				</c:if>
			</c:if>
		</tags:listCell>
		<c:if test="${caregiverFilter == true}">
			<tags:listCell>
				<tags:listField property="caregiverFullNameRev" component="${component}" listIndex="${iterator.index}" entityType="findPatient"/>		
			</tags:listCell>
		</c:if>
		<c:if test="${phoneFilter == true}">
			<tags:listCell>
				<tags:listField property="phone1" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>
				<c:if test="${not empty item.phoneType1}">		
				(<tags:listField property="phoneType1" component="${component}" listIndex="${iterator.index}" entityType="contactInfo" />)	
				</c:if>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="phone2" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>
				<c:if test="${not empty item.phoneType2}">		
				(<tags:listField property="phoneType2" component="${component}" listIndex="${iterator.index}" entityType="contactInfo" />)	
				</c:if>
			</tags:listCell>
			<tags:listCell>
				<tags:listField property="phone3" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>
				<c:if test="${not empty item.phoneType3}">		
				(<tags:listField property="phoneType3" component="${component}" listIndex="${iterator.index}" entityType="contactInfo" />)	
				</c:if>
			</tags:listCell>
		</c:if>
		<c:if test="${emailFilter == true}">
			<tags:listCell>
				<tags:listField property="email" component="${component}" listIndex="${iterator.index}" entityType="contactInfo"/>
			</tags:listCell>
		</c:if>
</tags:listRow>
</tags:list>
</tags:componentListHasResults>

</page:applyDecorator> 
</page:applyDecorator>   
	    

