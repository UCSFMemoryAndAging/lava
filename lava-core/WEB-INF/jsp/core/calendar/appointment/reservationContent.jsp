<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>




<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">details</page:param>
  <page:param name="sectionNameKey">reservation.details.section</page:param>

	<tags:createField property="id" component="${component}" entity="appointment"/>
	<tags:createField property="calendar.name" component="${component}" entity="appointment"/>
	<tags:createField property="organizerId" component="${component}" entity="reservation" mode="vw"/>
	
	<tags:createField property="status" component="${component}" mode="vw" entity="appointment"/>
	<tags:createField property="startDate,startTime" component="${component}" entity="appointment"/>
	<tags:createField property="endDate,endTime" component="${component}" entity="appointment"/>
	

</page:applyDecorator>  
 
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">note</page:param>
  <page:param name="sectionNameKey">reservation.note.section</page:param>
	<tags:createField property="description" component="${component}" entity="appointment"/>
	<tags:createField property="location" component="${component}" entity="appointment"/>
    <tags:createField property="notes" component="${component}" entity="appointment"/>

</page:applyDecorator>  
 

