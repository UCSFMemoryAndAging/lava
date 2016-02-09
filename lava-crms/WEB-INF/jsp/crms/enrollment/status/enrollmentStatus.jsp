<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="enrollmentStatus"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
   <page:param name="quicklinks">statusHistory</page:param>
   <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="projName"/>,${currentPatient.fullNameNoSuffix}</page:param>
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components['enrollmentStatus'].locked}</page:param>
 
	<c:import url="/WEB-INF/jsp/crms/enrollment/status/enrollmentStatusContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>
	<c:import url="/WEB-INF/jsp/crms/enrollment/status/enrollmentStatusHistoryContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>
	
	<%-- associated attachments listing --%>
	<%-- uncomment after create repository folders for msc and smd
	<c:if test="${componentView != 'add' && componentView != 'delete'}">
		<c:set var="id"><tags:componentProperty component="${component}" property="id"/></c:set>
		<c:import url="/WEB-INF/jsp/crms/enrollment/attachments/enrollmentAttachmentListContent.jsp">
			<c:param name="propertyValues">enrollStatId,${id}</c:param>
		</c:import>
	</c:if>	
	 --%>

</page:applyDecorator>  

</page:applyDecorator>    


