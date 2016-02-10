<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="patient"/>


<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField">firstName</page:param>
  <page:param name="quicklinks">idcore,recordManagement</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="fullNameNoSuffix"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components['patient'].locked}</page:param>
  
		<c:import url="/WEB-INF/jsp/crms/people/patient/patientContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
		
	<%-- associated attachments listing --%>
	<%-- uncomment after create repository folders for msc and smd
	<c:if test="${componentView != 'add' && componentView != 'delete'}">
		<c:set var="id"><tags:componentProperty component="${component}" property="id"/></c:set>
		<c:import url="/WEB-INF/jsp/crms/people/attachments/patientAttachmentListContent.jsp">
			<c:param name="pageName">${component}</c:param>
			<c:param name="pidn">${id}</c:param>
		</c:import>
	</c:if>	
	 --%>
	
</page:applyDecorator>    
</page:applyDecorator>	  

