<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>

<c:set var="component" value="reportSetup"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs"><spring:message code="projectPatientStatus.pageTitle"/></page:param>

<page:applyDecorator name="component.report.content">
	<page:param name="component">${component}</page:param>
	
<content tag="customCriteria">
	<%-- put any custom criteria inputs here (beyond project and date range) --%>
</content>
   
</page:applyDecorator> 
</page:applyDecorator>   
	    

