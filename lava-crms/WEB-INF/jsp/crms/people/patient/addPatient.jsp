<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="addPatient"/>

<%-- complex logic to determine focus field due to the fact that it varies 
depending upon whether deidentified is checked or not and whether the
initialProject was set --%>
<c:choose>
	<%-- set focus to first field, which is either First Name or Subject ID --%>
	<c:when test="${command.components[component].deidentified == 'true'}">
		<c:set var="focusField" value="subjectId"/>
	</c:when>
	<c:otherwise>
		<c:set var="focusField" value="firstName"/>
	</c:otherwise>	
</c:choose>
<%-- but if user just set the Initial Project, want focus set to Initial Status --%>
<c:if test="${flowEvent == 'addPatient__reRender'}">
	<c:if test="${not empty command.components[component].enrollmentStatus.projName}">
		<c:set var="focusField" value="status"/>
	</c:if>
</c:if>		
		

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField">${focusField}</page:param>

<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>

		<c:import url="/WEB-INF/jsp/crms/people/patient/addPatientContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
		
</page:applyDecorator>   

	<c:import url="/WEB-INF/jsp/crms/people/patient/addPatientLookupContent.jsp">
			<c:param name="pageName">${component}</c:param>
			
	</c:import> 
	
</page:applyDecorator>	  

