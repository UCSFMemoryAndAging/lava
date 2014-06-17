<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- because action target is 'familyPatient' component must be 'familyPatient' so flow transitions
work --%>
<c:set var="component" value="familyPatient"/>


<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="focusField">familyStatus</page:param>
  <page:param name="quicklinks"></page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="fullNameNoSuffix"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components[component].locked}</page:param>
  
		<c:import url="/WEB-INF/jsp/crms/people/family/familyPatientContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>
	
</page:applyDecorator>    

 
<c:set var="component" value="familyMembers"/>

<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageName">familyPatient</page:param>
	<page:param name="listTitle">Family Members</page:param>
    <page:param name="contextualInfo"> </page:param>
	
	<c:import url="/WEB-INF/jsp/crms/people/family/familyMembersContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>

</page:applyDecorator>    

</page:applyDecorator>	  

