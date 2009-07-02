<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<c:set var="component" value="reservation"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="quicklinks">details,note</page:param>
  <page:param name="pageHeadingArgs"></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
 
	<c:import url="/WEB-INF/jsp/core/calendar/appointment/reservationContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>
	
	
</page:applyDecorator>    
</page:applyDecorator>	    

