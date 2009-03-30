 <%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
 

    <tags:ifComponentExists component="patientContext">
	  		 <div id="globalFilters">
	    	 	<c:import url="/WEB-INF/jsp/navigation/context/modalPatientContext.jsp"/>
	  		 	<c:import url="/WEB-INF/jsp/navigation/context/modalProjectContext.jsp"/>
			 </div>
	</tags:ifComponentExists>
 