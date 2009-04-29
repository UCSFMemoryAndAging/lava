<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- this page is used for add instrument, because the type of instrument is unknown until the
 add is submitted, so can not use an instrument-specific page. 
 note: this page is currently also being used for delete instrument for unimplemented instruments
  because these do not have an instrument-specific page yet. those instruments which are implemented
  use the instrument-specific page for delete --%>

<c:set var="component" value="instrument"/>

<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
<c:if test="${componentView == 'add'}">
	<c:set var="componentView" value="addMany"/>
</c:if>
   
<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${component}</page:param>
  <page:param name="focusField">instrType</page:param>  
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="instrType"/>,${currentPatient.fullNameNoSuffix},<fmt:formatDate value="${currentVisit.visitDate}" pattern="MMMM d yyyy"/></page:param>

<%-- note: add instrument uses the general component.entity.content decorator, as opposed
  to instrument-specific pages which use component.instrument.content which is specialized
  to support the instrument-specific flows --%>
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
  <page:param name="componentView">${componentView}</page:param>
  <page:param name="contextualInfo">${componentView == 'addMany' ? 'patient.fullName,instrument' : 'id,instrument,instrType,instrument,projName,instrument,visit.patient.fullName,instrument,visit.visitDescrip,instrument'}</page:param>
	
	<c:import url="/WEB-INF/jsp/crms/assessment/instrument/instrumentContent.jsp">
		<c:param name="component">${component}</c:param>
		<c:param name="componentView">${componentView}</c:param>
	</c:import>
</page:applyDecorator>  


<c:if test="${componentView == 'addMany'}">

<!-- Visit Instruments List, i.e. all instruments for the selected visit -->
<c:set var="component" value="visitInstruments"/>

<%-- get visit descrip from selected visit --%>
<%-- the following would not work:
${command.components['instrument'].visit.id} works, e.g. 15536
${dynamicLists['visit.patientVisits']['15536']} works
but this pair together does not work:
<c:set var="visitId" value="${command.components['instrument'].visit.id}"/> works
${dynamicLists['visit.patientVisits'][visitId]} does not work
or
<c:set var="visitDescrip" value="${dynamicLists['visit.patientVisits'][visitId]}"/> does not work
todo: use spring:bind to get visitDescrip in status.value to use as visitDescrip instead of this loop
--%>
<c:forEach items="${dynamicLists['visit.patientVisits']}" var="entry">
  <c:if test="${entry.key == command.components['instrument'].visit.id}">
     <c:set var="visitDescrip" value="${entry.value}"/>
  </c:if>
</c:forEach>
<%-- if no visit descrip selected, do not list visit instruments --%>
<c:if test="${not empty visitDescrip}">

<page:applyDecorator name="component.list.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageName">instrument</page:param>
	<page:param name="listTitle">Instruments associated with Visit ${visitDescrip}</page:param>
    <page:param name="contextualInfo"> </page:param>
    <page:param name="isSecondary">true</page:param>

	<c:import url="/WEB-INF/jsp/crms/assessment/instrument/visitInstrumentsContent.jsp">
		<c:param name="component">${component}</c:param>
	</c:import>

</page:applyDecorator>    

</c:if>  

</c:if>
</page:applyDecorator>    


