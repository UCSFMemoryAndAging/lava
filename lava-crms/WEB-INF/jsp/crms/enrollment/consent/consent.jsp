<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="consent"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
   <page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix},<tags:componentProperty component="${component}" property="consentType"/></page:param>
   
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components['consent'].locked}</page:param>
  
	<c:import url="/WEB-INF/jsp/crms/enrollment/consent/consentContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>
	
	<%-- associated attachments listing --%>
	<c:if test="${componentView != 'add'}">
		<c:set var="id"><tags:componentProperty component="${component}" property="id"/></c:set>
		<c:import url="/WEB-INF/jsp/crms/enrollment/attachments/consentAttachmentListContent.jsp">
			<c:param name="pageName">${component}</c:param>
			<c:param name="consentId">${id}</c:param>
		</c:import>
	</c:if>

</page:applyDecorator>  

</page:applyDecorator>    


