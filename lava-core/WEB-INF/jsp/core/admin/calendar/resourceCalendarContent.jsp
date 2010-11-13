<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>




<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">details</page:param>
  <page:param name="sectionNameKey">resourceCalendar.details.section</page:param>

	<tags:createField property="id" component="${component}" entity="calendar"/>
	<tags:createField property="name" component="${component}" entity="calendar"/>
	<tags:createField property="resourceType" component="${component}"/>
	<tags:createField property="location" component="${component}"/>
	<tags:createField property="description" component="${component}" entity="calendar"/>
	<tags:createField property="workDays" component="${component}" entity="calendar"/>
	<tags:createField property="workBeginTime" component="${component}" entity="calendar"/>
	<tags:createField property="workEndTime" component="${component}" entity="calendar"/>
	<tags:createField property="peakUsageDays" component="${component}"/>
	<tags:createField property="peakUsageBeginTime" component="${component}"/>
	<tags:createField property="peakUsageEndTime" component="${component}"/>
	<tags:createField property="contactId" component="${component}"/>
	<tags:createField property="contact.phone" component="${component}"/>
	<tags:createField property="contact.email" component="${component}"/>
	
</page:applyDecorator>  
 
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">note</page:param>
  <page:param name="sectionNameKey">resourceCalendar.note.section</page:param>
	<tags:createField property="notes" component="${component}" entity="calendar"/>

</page:applyDecorator>  
 
 
<ui:formGuide ignoreDoOnLoad="true" >
	  <ui:observe elementIds="contactId" component="${component}" forValue=".+"/>
	<ui:submitForm form="${component}" event="${component}__reRender"/>
</ui:formGuide>
