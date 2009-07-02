<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>




<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">details</page:param>
  <page:param name="sectionNameKey">appointment.details.section</page:param>

	<tags:createField property="id" component="${component}"/>
	<tags:createField property="calendar.name" component="${component}"/>
	<tags:createField property="organizerId" component="${component}" />
	<tags:createField property="status" component="${component}" mode="vw"/>
	<tags:createField property="startDate,startTime" component="${component}"/>
	<tags:createField property="endDate,endTime" component="${component}"/>
	

</page:applyDecorator>  
 
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">note</page:param>
  <page:param name="sectionNameKey">appointment.note.section</page:param>
	<tags:createField property="description" component="${component}"/>
	<tags:createField property="location" component="${component}"/>
    <tags:createField property="notes" component="${component}"/>

</page:applyDecorator>  
 

