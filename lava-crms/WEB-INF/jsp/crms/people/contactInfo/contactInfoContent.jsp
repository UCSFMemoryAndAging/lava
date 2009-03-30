<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="isCaregiver"><tags:componentProperty component="${component}" property="isCaregiver"/></c:set>

<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">contactDetails</page:param>
  <page:param name="sectionNameKey">contactInfo.contactDetails.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
 <tags:createField property="id" component="${component}"/>
 <tags:createField property="patient.fullNameNoSuffix" component="${component}" metadataName="patient.fullNameNoSuffix"/>
   <tags:createField property="isCaregiver" component="${component}"/>
	<tags:createField property="caregiverId" component="${component}" fieldStyle="${isCaregiver ? '' : 'invisible'}"/>
	<tags:createField property="contactPatient" component="${component}"/>
	<tags:createField property="isPatientResidence" component="${component}"/>

<tags:createField property="active" component="${component}"/>
</page:applyDecorator>  

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">address</page:param>
  <page:param name="sectionNameKey">contactInfo.address.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
	<tags:createField property="address" component="${component}"/>
	<tags:createField property="address2" component="${component}"/>
		<tags:createField property="city" component="${component}"/>
		<tags:createField property="state" component="${component}"/>
		<tags:createField property="zip" component="${component}"/>
		<tags:createField property="country" component="${component}"/>

</page:applyDecorator>   
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">phoneEmail</page:param>
  <page:param name="sectionNameKey">contactInfo.phoneEmail.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
		<tags:createField property="phone1" component="${component}"/>
		<tags:createField property="phoneType1" component="${component}"/>
		<tags:createField property="phone2" component="${component}"/>
		<tags:createField property="phoneType2" component="${component}"/>
		<tags:createField property="phone3" component="${component}"/>
		<tags:createField property="phoneType3" component="${component}"/>
		<tags:createField property="email" component="${component}"/>
</page:applyDecorator>   

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">notes</page:param>
  <page:param name="sectionNameKey">contactInfo.notes.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="notes" component="${component}"/>
</page:applyDecorator>   

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">OptOut</page:param>
  <page:param name="sectionNameKey">contactInfo.optOut.section</page:param>
  <page:param name="quicklinkPosition">top</page:param>
<tags:createField property="optOutMac" component="${component}"/>
<tags:createField property="optOutAffiliates" component="${component}"/>
</page:applyDecorator>   

</tags:contentColumn>

<%-- when isCaregiver is checked or unchecked, submit the form, so that the
 caregiverId displays or not --%>
<ui:formGuide observeAndOr="or" ignoreDoOnLoad="true" >
    <ui:observe elementIds="${component}_isCaregiver" forValue="1"/>
    <ui:observeForNull elementIds="${component}_isCaregiver"/>
    <ui:submitForm form="${component}" event="${component}__reRender"/>
</ui:formGuide>



