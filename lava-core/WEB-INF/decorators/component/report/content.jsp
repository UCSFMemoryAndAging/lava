<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- default report handler defines the "reportSetup" entity, but subclasses could override so use
the component value passed by the individual report. this entity must have a LavaDaoFilter "filter" property --%>
<c:set var="component">
  <decorator:getProperty property="component"/>
</c:set>	
<c:set var="pageName">
  <decorator:getProperty property="pageName"/>
</c:set>
<c:if test="${empty pageName}">
	<c:set var="pageName" value="${component}"/>
</c:if>

<div id="pageLevelActionNavButtonTopBox">
	<tags:eventButton buttonText="Print" action="generate" component="${component}" pageName="${pageName}"/>
	<tags:eventButton buttonText="Close" action="close" component="${component}" pageName="${pageName}"/>
	<c:set var="customActions">
		<decorator:getProperty property="page.customActions"/>
	</c:set>	
	${customActions}
</div>


<div id="contentBox">

<%-- put all criteria within a section  --%>
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionNameKey">reportSetup.criteria.section</page:param>
  
<%-- if only date criteria, or both date and project criteria, use two column layout
where date is in the left column --%>  
<c:if test="${dateCriteria && projectCriteria}">
<tags:contentColumn columnClass="colLeft2Col5050">
	<c:set var="displayRange">
		${command.components[component].filter.params['displayRange']}
	</c:set>

	<c:if test="${displayRange == 'All'}">
		<div class="field ">
			Date Range: <span class="bold">All Dates</span>
		</div>
	</c:if>
	<c:if test="${displayRange != 'All'}">
		<tags:listFilterField property="customDateStart" component="${component}" entityType="${component}"/>
		<tags:listFilterField property="customDateEnd" component="${component}" entityType="${component}"/>
	</c:if>

	<%-- quick date selection (enclose in div.field to give same indent) --%>           
	<c:if test="${displayRange != 'All'}">
		<div class="field">           
			<span class="bold">Quick Date Selection:</span>
			<tags:eventLink linkText="Prev ${displayRange}" action="prevDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
			<tags:eventLink linkText="Next ${displayRange}" action="nextDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
			<tags:eventLink linkText="Today" action="displayNow" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>
		</div>	
	</c:if>
	<div class="field">           
		<span class="bold">Quick Date Range:</span> 
		<tags:eventLink linkText="Day" action="displayDay" component="${component}" pageName="${pageName}" className="${displayRange=='Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Week" action="displayWeek" component="${component}" pageName="${pageName}" className="${displayRange=='Week'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Month" action="displayMonth" component="${component}" pageName="${pageName}" className="${displayRange=='Month'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Year" action="displayYear" component="${component}" pageName="${pageName}" className="${displayRange=='Year'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="All" action="displayAll" component="${component}" pageName="${pageName}" className="${displayRange=='All'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
	</div>
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">
<tags:listFilterField property="projectList" component="${component}" entityType="${component}"/> 
</tags:contentColumn>
</c:if>

<%-- if only date criteria do not use two column layout --%>
<c:if test="${dateCriteria && !projectCriteria}">
	<c:set var="displayRange">
		${command.components[component].filter.params['displayRange']}
	</c:set>

	<c:if test="${displayRange == 'All'}">
		<div class="field ">
			Date Range: <span class="bold">All Dates</span>
		</div>
	</c:if>
	<c:if test="${displayRange != 'All'}">
		<tags:listFilterField property="customDateStart" component="${component}" entityType="${component}"/>
		<tags:listFilterField property="customDateEnd" component="${component}" entityType="${component}"/>
	</c:if>

	<%-- quick date selection (enclose in div.field to give same indent) --%>           
	<c:if test="${displayRange != 'All'}">
		<div class="field">           
			<span class="bold">Quick Date Selection:</span>
			<tags:eventLink linkText="Prev ${displayRange}" action="prevDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
			<tags:eventLink linkText="Next ${displayRange}" action="nextDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
			<tags:eventLink linkText="Today" action="displayNow" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>
		</div>	
	</c:if>
	<div class="field">           
		<span class="bold">Quick Date Range:</span> 
		<tags:eventLink linkText="Day" action="displayDay" component="${component}" pageName="${pageName}" className="${displayRange=='Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Week" action="displayWeek" component="${component}" pageName="${pageName}" className="${displayRange=='Week'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Month" action="displayMonth" component="${component}" pageName="${pageName}" className="${displayRange=='Month'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="Year" action="displayYear" component="${component}" pageName="${pageName}" className="${displayRange=='Year'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
		<tags:eventLink linkText="All" action="displayAll" component="${component}" pageName="${pageName}" className="${displayRange=='All'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
	</div>
</c:if>

<%-- if only project criteria do not use two column layout --%>
<c:if test="${projectCriteria && !dateCriteria}">
	<tags:listFilterField property="projectList" component="${component}" entityType="${component}"/> 
</c:if>

<%-- custom criteria fields defined in jsp --%>
<decorator:getProperty property="page.customCriteria"/>


</page:applyDecorator> <!-- end reportSetup.criteria.section -->


<%-- put format in anonymous section just to give some vertical spacing --%>
<c:set var="customFormat">
	<decorator:getProperty property="page.customFormat"/>
</c:set>	
<page:applyDecorator name="component.entity.section">
	<page:param name="sectionId">anonymous</page:param>
	<c:if test="${empty customFormat}">
		<tags:createField property="format" component="${component}"/>
	</c:if>
	<c:if test="${not empty customFormat}">
		${customFormat}
	</c:if>		
</page:applyDecorator>
</div>

<div id="skiBox">
</div>

<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS - BOTTOM -->
<div id="pageLevelActionNavButtonBottomBox">           
	<tags:eventButton buttonText="Print" action="generate" component="${component}" pageName="${pageName}"/>
	<tags:eventButton buttonText="Close" action="close" component="${component}" pageName="${pageName}"/>
    ${customActions}
</div>  


	




