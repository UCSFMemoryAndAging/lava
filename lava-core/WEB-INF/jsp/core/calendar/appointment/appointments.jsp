<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="appointments"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
 	<page:param name="quicklinks"></page:param>
     <page:param name="pageHeadingArgs"><tags:componentProperty component="calendar" property="name"/></page:param>
 
	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	<page:param name="calendarField">startDate</page:param>
	<c:import url="/WEB-INF/jsp/core/calendar/appointment/appointmentsContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>

</page:applyDecorator>    

</page:applyDecorator>    
	    
