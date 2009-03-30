<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udslabsimaging1"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="ctFlm"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Labs Imaging</page:param>
  <page:param name="quicklinks"> </page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
</c:import>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udslabsimaging1.imaging.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>

<tags:tableForm>  

<tags:listRow>
	<tags:listColumnHeader label="" width="40%"/>
	<tags:listColumnHeader labelKey="udslabsimaging1.imaging.filmColHeader" width="30%"/>
	<tags:listColumnHeader labelKey="udslabsimaging1.imaging.digitalColHeader" width="30%"/>
</tags:listRow>
  
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.ct.label"/></tags:listCell>
	<tags:listCell><tags:createField property="ctFlm" entity="udslabsimaging1" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
	<tags:listCell><tags:createField property="ctDig" entity="udslabsimaging1" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
</tags:listRow>	
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.mri1.label"/></tags:listCell>
	<tags:listCell><tags:createField property="mri1Flm" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
	<tags:listCell><tags:createField property="mri1Dig" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
</tags:listRow>	
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.mri2.label"/></tags:listCell>
	<tags:listCell><tags:createField property="mri2Flm" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
	<tags:listCell><tags:createField property="mri2Dig" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
</tags:listRow>	
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.mri3.label"/></tags:listCell>
	<tags:listCell><tags:createField property="mri3Flm" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
	<tags:listCell><tags:createField property="mri3Dig" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
</tags:listRow>	
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.mriSp.label"/></tags:listCell>
	<tags:listCell><tags:createField property="mriSpFlm" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
	<tags:listCell><tags:createField property="mriSpDig" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
</tags:listRow>	
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.spect.label"/></tags:listCell>
	<tags:listCell><tags:createField property="spectFlm" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
	<tags:listCell><tags:createField property="spectDig" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
</tags:listRow>	
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.pet.label"/></tags:listCell>
	<tags:listCell><tags:createField property="petFlm" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
	<tags:listCell><tags:createField property="petDig" entity="${instrTypeEncoded}" component="${component}" fieldStyle="${componentMode == 'dc' ? 'fieldComboRadioSelectInList' : ''}"/></tags:listCell>
</tags:listRow>	
</tags:tableForm>
<tags:createField property="notes['imaging']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udslabsimaging1.specimens.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"></page:param>
  
<tags:tableForm>  

<tags:listRow>
	<tags:listColumnHeader label="" width="40%"/>
	<tags:listColumnHeader label="" width="60%"/>
</tags:listRow>  
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.specimen1.label"/></tags:listCell>
	<tags:listCell><tags:createField property="dna" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>	
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.specimen2.label"/></tags:listCell>
	<tags:listCell><tags:createField property="csfAntem" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.specimen3.label"/></tags:listCell>
	<tags:listCell><tags:createField property="serum" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
</tags:tableForm>
<tags:createField property="notes['specimens']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top"/>
</page:applyDecorator>


<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udslabsimaging1.genotyping.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:tableForm>  

<tags:listRow>
	<tags:listColumnHeader label="" width="40%"/>
	<tags:listColumnHeader label="" width="60%"/>
</tags:listRow>  
<tags:listRow>
	<tags:listCell><spring:message code="udslabsimaging1.geno.label"/></tags:listCell>
	<tags:listCell><tags:createField property="apoe" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>  
</tags:tableForm>
<tags:createField property="notes['genotyping']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top"/>
</page:applyDecorator>

</page:applyDecorator>    
</page:applyDecorator>