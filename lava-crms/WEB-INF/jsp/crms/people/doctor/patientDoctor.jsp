<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- the relationship between a patient and doctor --%>
<c:set var="component" value="patientDoctor"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>


<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField">${componentView == 'add' ? 'doctor_id': 'docStat'}</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/></page:param>
  
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="componentView">${componentView}</page:param>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
	<tags:createField property="patient.fullNameRev" component="${component}" metadataName="patient.fullNameNoSuffix"/>
	<%-- fieldId must be defined for nested properties of autocomplete controls --%>
<c:choose>
	<c:when test="${componentView == 'add'}">
	<tags:createField property="doctor.id" component="${component}" fieldId="${component}_doctor_id"/>
	</c:when>
	<c:otherwise>
	<tags:createField property="doctor.id" component="${component}" context="c"/>
	</c:otherwise>
</c:choose>		
	<tags:createField property="docStat" component="${component}"/>
	<tags:createField property="docNote" component="${component}"/>
</page:applyDecorator>  

<c:if test="${componentView != 'add'}">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">name</page:param>
  <page:param name="sectionNameKey">doctor.name.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="doctor.id"  component="${component}" context="c"/>
<tags:createField property="doctor.firstName"  component="${component}" context="c"/>
<tags:createField property="doctor.middleInitial" component="${component}" context="c"/>
<tags:createField property="doctor.lastName" component="${component}" context="c"/>
<tags:createField property="doctor.docType" component="${component}" context="c"/>
</page:applyDecorator>    

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">location</page:param>
  <page:param name="sectionNameKey">doctor.location.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="doctor.address" component="${component}" context="c"/>
<tags:createField property="doctor.city"  component="${component}" context="c"/>
<tags:createField property="doctor.state"  component="${component}" context="c"/>
<tags:createField property="doctor.zip"  component="${component}" context="c"/>
<tags:createField property="doctor.phone1" component="${component}" context="c"/>
<tags:createField property="doctor.phoneType1"  component="${component}" context="c"/>
<tags:createField property="doctor.phone2"  component="${component}" context="c"/>
<tags:createField property="doctor.phoneType2"  component="${component}" context="c"/>
<tags:createField property="doctor.phone3" component="${component}" context="c"/>
<tags:createField property="doctor.phoneType3"  component="${component}" context="c"/>
<tags:createField property="doctor.email"  component="${component}" context="c"/>
</page:applyDecorator>    
</c:if>

</page:applyDecorator>  

</page:applyDecorator>    


