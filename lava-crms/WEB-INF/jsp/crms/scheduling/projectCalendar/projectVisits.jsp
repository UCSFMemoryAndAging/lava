<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="projectVisits"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
 	<page:param name="quicklinks"></page:param>
     <page:param name="pageHeadingArgs">${empty currentProject? 'All Project ' : currentProject.name}</page:param>
	
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	<page:param name="calendarField">visitDate</page:param>

	<c:import url="/WEB-INF/jsp/crms/scheduling/projectCalendar/projectVisitsContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>

</page:applyDecorator>    

</page:applyDecorator>    
	    
