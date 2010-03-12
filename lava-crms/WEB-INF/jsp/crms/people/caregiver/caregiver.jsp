<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="caregiver"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="quicklinks">caregiver,language,roles,notes</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="fullName"/>,<tags:componentProperty component="${component}" property="patient" property2="fullNameNoSuffix"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="contextualInfo">patient.fullNameNoSuffix,caregiver</page:param>
  <page:param name="locked">${command.components['caregiver'].locked}</page:param>
 
	<c:import url="/WEB-INF/jsp/crms/people/caregiver/caregiverContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>
</page:applyDecorator>    
</page:applyDecorator>	    

