<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>


<c:set var="component" value="visitInstruments"/>


<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs">${currentPatient.fullNameNoSuffix},${currentVisit.visitDescrip}</page:param>

	
<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	
	<c:import url="/WEB-INF/jsp/crms/scheduling/visit/visitInstrumentsContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>

</page:applyDecorator> 
</page:applyDecorator>   
	    

