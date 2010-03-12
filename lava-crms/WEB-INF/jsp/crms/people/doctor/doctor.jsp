<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="doctor"/>


<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="doctor" property="fullName"/></page:param>
  
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="locked">${command.components['doctor'].locked}</page:param>

	<c:import url="/WEB-INF/jsp/crms/people/doctor/doctorContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>
</page:applyDecorator>  


<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
<c:if test="${componentView == 'view'}">

<!-- Doctor Patients List -->
<c:set var="component" value="doctorPatients"/>

<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageName">doctor</page:param>
	<page:param name="listTitle">Patients associated with <tags:componentProperty component="doctor" property="fullName"/></page:param>
	
	<c:import url="/WEB-INF/jsp/crms/people/doctor/doctorPatientsContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>

</page:applyDecorator>    
</c:if>
</page:applyDecorator>    


