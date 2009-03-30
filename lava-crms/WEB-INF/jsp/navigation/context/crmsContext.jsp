 <%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
 
 <c:if test="${empty isErrorPage}">
       <tags:ifComponentExists component="patientContext">
	  		 <div id="globalFilters">
	    	 	<c:import url="/WEB-INF/jsp/navigation/context/patientContext.jsp"/>
	  		 	<c:import url="/WEB-INF/jsp/navigation/context/projectContext.jsp"/>
			 </div>
		 </tags:ifComponentExists>
 </c:if>