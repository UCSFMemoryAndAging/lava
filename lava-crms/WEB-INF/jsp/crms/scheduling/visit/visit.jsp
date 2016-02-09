<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="visit"/>

<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>

<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
<c:if test="${componentView == 'add'}">
	<c:set var="componentView" value="addMany"/>
</c:if>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField">${flowEvent == 'visit__reRender' ? 'visitType' : 'projName'}</page:param>
  <page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix},${currentVisit.visitDescrip}</page:param>
   
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="componentView">${componentView}</page:param>
  <page:param name="locked">${command.components['visit'].locked}</page:param>
  
	<c:import url="/WEB-INF/jsp/crms/scheduling/visit/visitContent.jsp">
		<c:param name="component">${component}</c:param>
		<c:param name="componentMode">${componentMode}</c:param>
		<c:param name="componentView">${componentView}</c:param>
	</c:import>
	
	<%-- associated attachments listing --%>
	<%-- comment out until ready, i.e. would need to have a repository configured for a specific
		application for this to be used
	<c:if test="${componentView != 'add' && componentView != 'delete'}">
		<c:set var="id"><tags:componentProperty component="${component}" property="id"/></c:set>
		<c:import url="/WEB-INF/jsp/crms/scheduling/attachments/visitAttachmentListContent.jsp">
			<c:param name="propertyValues">visitId,${id}</c:param>
		</c:import>
	</c:if>	
	 --%>
	
</page:applyDecorator>  


<%-- if adding visit, show the list of other visits for the patient associated with primary visit 
     note: add visit is not accessible unless there is a patient in context, so that is guaranteed --%>
<c:if test="${componentView == 'addMany'}">

<c:set var="component" value="patientVisits"/>

<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageName">visit</page:param>
	<page:param name="listTitle">Visits associated with <tags:componentProperty component="visit" property="patient" property2="fullName"/></page:param>
    <page:param name="contextualInfo"> </page:param>
	
	<c:import url="/WEB-INF/jsp/crms/scheduling/visit/patientVisitsContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>

</page:applyDecorator>    
</c:if>

</page:applyDecorator>    


