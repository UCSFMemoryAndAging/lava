<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<%--javascript file includes JSP so must include it on the server side so
    it will be processed, rather than using standard technique for including
    external javascript (specifying src= ) --%>
<script language="javascript" type="text/javascript">
<%@ include file="/javascript/pagedList/pagedList.js" %>
<%@ include file="/javascript/pagedList/selectListItems.js" %>
</script>

<c:set var="component">
  <decorator:getProperty property="component"/>
</c:set>	
<c:set var="pageName">
  <decorator:getProperty property="pageName"/>
</c:set>
<c:if test="${empty pageName}">
	<c:set var="pageName" value="${component}"/>
</c:if>
<c:set var="listTitle">
  <decorator:getProperty property="listTitle"/>
</c:set>
<%-- get field name used for the calendar control if specified then calendar formatting is applied --%> 
<c:set var="calendarField">
  <decorator:getProperty property="calendarField"/>
</c:set>	

<c:set var="widthClass">
  <decorator:getProperty property="widthClass"/>
</c:set>
<c:if test="${empty widthClass}">
	<c:set var="widthClass" value="normal"/>
</c:if>

<c:if test="${not empty calendarField}">
	
	<c:set var="calendarDateStart">
	  ${calendarField}Start
	</c:set>	
	<c:set var="calendarDateEnd">
	  ${calendarField}End
	</c:set>	

	<c:set var="displayRange">
		${command.components[component].filter.params['displayRange']}
	</c:set>
	<c:set var="displayRangeDesc">
	  <tags:formatDateRange range="${displayRange}" beginDate="${displayRange=='All' ? null : command.components[component].filter.params[calendarDateStart]}" endDate="${displayRange=='All' ? null : command.components[component].filter.params[calendarDateEnd]}"/>
	</c:set>  
</c:if> <%--CalendarField defined--%>


<c:if test="${not empty listTitle}">
<div class="listControlBar">
	<div id="listTitle">
		${listTitle}
	</div>
</div>
</c:if>

<!-- CONTEXTUAL INFO -->
<%-- create a scoped variable for the contextualInfo comma-separated pairs --%>
<c:set var="contextualInfo">
  <decorator:getProperty property="contextualInfo"/>
</c:set>	
<c:if test="${not empty contextualInfo}">
  <fieldset id="contextualInfoBox">
    <c:forTokens items="${contextualInfo}" delims="," var="current" varStatus="status">
      <c:if test="${status.index % 2 == 0}">
        <c:set var="property" value="${current}"/>
      </c:if>
      <c:if test="${status.index % 2 == 1}">
        <tags:createField property="${property}" section="" component="${current}" mode="vw"/>
      </c:if>
    </c:forTokens>
  </fieldset>  
</c:if>

<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS -->
<!-- includes INSTRUCTION TEXT, if any -->
<!-- no page level action/nav buttons on list views right now -->

<%-- for secondary lists, want the page to reposition to that list on its events, so 
create anchor tag to be used as a target. this works in conjunction with javascript 
that submits the form (eventButton, eventLink) --%>
<c:set var="isSecondary">
  <decorator:getProperty property="isSecondary"/>
</c:set>
<c:if test="${not empty isSecondary}">
<a id="${component}"></a>
</c:if>

<div id="listContentBox">

<c:set var="customActions"> 
	<decorator:getProperty property="page.customActions"/>
</c:set>	


<div class="listControlBar">

<%-- need to create variable because does not evaluate inline, maybe because of '#' char --%>
<c:set var="onChangeUrl">#${component}</c:set>

<%-- output the actions --%>
${customActions}
</div> <!-- end top listControlBar -->

<div class="calendarDayBox">

<div class="calendarDayTitleBox"><span class="listControlBarCalendarDisplayRange">&nbsp;&nbsp;${displayRangeDesc}</span><BR/><BR/>
	 &nbsp;&nbsp;&nbsp;&nbsp;<tags:eventLink linkText="Previous" action="prevDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
 	      <tags:eventLink linkText="Next" action="nextDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
		  <tags:eventLink linkText="Today" action="displayNow" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   
   <tags:eventLink linkText="Day" action="displayDay" component="${component}" pageName="${pageName}" className="${displayRange=='Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
   <tags:eventLink linkText="Week" action="displayWeek" component="${component}" pageName="${pageName}" className="${displayRange=='Week'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
    <tags:eventLink linkText="Month" action="displayMonth" component="${component}" pageName="${pageName}" className="${displayRange=='Month'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    
   <tags:eventLink linkText="Work Day" action="showWorkDay" component="${component}" pageName="${pageName}" className="${showRange=='WorkDay'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
   <tags:eventLink linkText="Full Day" action="showFullDay" component="${component}" pageName="${pageName}" className="${showRange=='FullDay'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
  
    </div>

<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">6</span>
		<span class="calendarDayHourLabelAMPMSuffix">AM</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">7</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">8</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">9</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">10</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">11</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">12</span>
		<span class="calendarDayHourLabelAMPMSuffix">PM</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">1</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">2</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">3</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">4</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">5</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">6</span>
		<span class="calendarDayHourLabelAMPMSuffix">PM</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">7</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">8</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>
<div class="calendarDayHourBox">
	<div class="calendarDayHourLabelBox">
		<span class="calendarDayHourLabel">9</span>
		<span class="calendarDayHourLabelSuffix">00</span>
	</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
	<div class="calendarDayHourIntervalBox">&nbsp;</div>
</div>

<div class="testAppointment">10:00 AM - Hesse (415-502-0590)</div>
	<decorator:body/>

</div>  <!-- end calendarDayBox -->
<div class="listControlBar">
	<%-- NOTE: since buttons float right, put them in the opposite order that they should appear --%>
</div> <!--  end bottom listControlBar -->

</div> <!-- end contentBox -->

	


<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS -->
<!-- no page level action/nav buttons on list views right now -->




