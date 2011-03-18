<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>

<c:set var="component" value="reportLauncher"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="messageCodeComponent">reportLauncher</page:param>

<tags:contentColumn columnClass="colLeft2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">people</page:param>
  <page:param name="sectionNameKey">reportLauncher.people.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>


</page:applyDecorator>  
<div class="verticalSpace30">&nbsp;</div>
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">enrollment</page:param>
  <page:param name="sectionNameKey">reportLauncher.enrollment.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
<a href="<tags:actionURL actionId="lava.crms.reporting.reports.crmsReportLauncher" parameters="param,projectPatientStatus" flowExecutionKey="${flowExecutionKey}" eventId="reportLauncher__view"/>">
<spring:message code="action.lava.crms.reporting.reports.projectPatientStatus.report"/></a><br/>
</page:applyDecorator>  

</tags:contentColumn>


<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">scheduling</page:param>
  <page:param name="sectionNameKey">reportLauncher.scheduling.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>
  
<a href="<tags:actionURL actionId="lava.crms.reporting.reports.crmsReportLauncher" parameters="param,projectVisitListByDate" flowExecutionKey="${flowExecutionKey}" eventId="reportLauncher__view"/>">
<spring:message code="action.lava.crms.reporting.reports.projectVisitListByDate.report"/></a><br/>
<a href="<tags:actionURL actionId="lava.crms.reporting.reports.crmsReportLauncher" parameters="param,projectVisitListByPatient" flowExecutionKey="${flowExecutionKey}" eventId="reportLauncher__view"/>">
<spring:message code="action.lava.crms.reporting.reports.projectVisitListByPatient.report"/></a><br/>
<!-- These reports depend on MACDiagnosis. 

<a href="<tags:actionURL actionId="lava.crms.reporting.reports.crmsReportLauncher" parameters="param,researchVisitListByDate" flowExecutionKey="${flowExecutionKey}" eventId="reportLauncher__view"/>">
<spring:message code="action.lava.crms.reporting.reports.researchVisitListByDate.report"/></a><br/>
<a href="<tags:actionURL actionId="lava.crms.reporting.reports.crmsReportLauncher" parameters="param,researchVisitListByPatient" flowExecutionKey="${flowExecutionKey}" eventId="reportLauncher__view"/>">
<spring:message code="action.lava.crms.reporting.reports.researchVisitListByPatient.report"/></a><br/>
</page:applyDecorator>  
-->
<div class="verticalSpace30">&nbsp;</div>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">assessment</page:param>
  <page:param name="sectionNameKey">reportLauncher.assessment.section</page:param>
  <page:param name="quicklinkPosition">none</page:param>

</page:applyDecorator>  
</tags:contentColumn>

   
</page:applyDecorator>   
	    

