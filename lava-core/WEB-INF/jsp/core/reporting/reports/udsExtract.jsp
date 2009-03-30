<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>>

<c:set var="component" value="reportSetup"/>

<page:applyDecorator name="component.content">
	<page:param name="component">${component}</page:param>
	<page:param name="pageHeadingArgs"><spring:message code="udsExtract.pageTitle"/></page:param>

<page:applyDecorator name="component.report.content">
	<page:param name="component">${component}</page:param>
	
	
<content tag="customCriteria">
	<%-- this is a custom setup which is used instead of the standard dateStart/dateEnd which is
		supported by the report decorator. here there is only dateStart (dateEnd is ignored) and there
		is no concept of displayRange == 'All' --%>
	<div>&nbsp;</div>
	<tags:createField property="filter.params['customDateStart']" component="${component}" 
		metadataName="udsExtract.${component}.customDateStart" fieldId="${component}_${component}_customDateStart"/>
		
	<%-- quick date selection (enclose in div.field to give same indent) --%>           
	<c:set var="displayRange">
		${command.components[component].filter.params['displayRange']}
	</c:set>
	
	<div class="field">           
		<span class="bold">Quick Date Selection:</span>
		<tags:eventLink linkText="Prev ${displayRange}" action="prevDateRange" component="${component}" pageName="${component}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Next ${displayRange}" action="nextDateRange" component="${component}" pageName="${component}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Today" action="displayNow" component="${component}" pageName="${component}" className="listControlBarCalendarAction"/>
	</div>	
	<div class="field">           
		<span class="bold">Quick Date Unit:</span> 
		<tags:eventLink linkText="Day" action="displayDay" component="${component}" pageName="${component}" className="${displayRange=='Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Week" action="displayWeek" component="${component}" pageName="${component}" className="${displayRange=='Week'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Month" action="displayMonth" component="${component}" pageName="${component}" className="${displayRange=='Month'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Year" action="displayYear" component="${component}" pageName="${component}" className="${displayRange=='Year'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
	</div>


	<%-- the patientId report parameter --%>
	<div class="verticalSpace30">&nbsp;</div>
	<tags:listFilterField property="patientId" component="${component}" entityType="${component}"/>
	
</content>

<content tag="customFormat">
	<tags:createField property="format" component="${component}" metadataName="udsExtract.${component}.format"/>
</content>

   
</page:applyDecorator> 
</page:applyDecorator>   
	    

