<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- calendarheader

	 renders the calendar header and encasulates the logic for displaying a day, week, and month view 
 
--%>

<%@ attribute name="component" required="true"
              description="the component name" %>
<%@ attribute name="pageName" required="true"
              description="the page name" %>
<%@ attribute name="description" required="true"
              description="the description for the calendar title bar" %>
<%@ attribute name="range" required="true"
              description="the range to display, 'Day','Week','Month'" %>
<%@ attribute name="dayLength" required="true"
              description="the length of the day to display 'Work Day','Full Day'" %>

              
<c:choose>
	<c:when test="${range == 'Week'}"> 
		<div class="calendarWeekTitleBox">
	</c:when>
	<c:when test="${range == 'Day'}"> 
		<div class="calendarDayTitleBox">
	</c:when>
</c:choose>			
		
		<span class="listControlBarCalendarDisplayRange">&nbsp;&nbsp;${description}</span><BR/><BR/>
	 	&nbsp;&nbsp;&nbsp;&nbsp;<tags:eventLink linkText="Previous" action="prevDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
 	      <tags:eventLink linkText="Next" action="nextDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
		  <tags:eventLink linkText="Today" action="displayNow" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>
     	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   
   		<tags:eventLink linkText="Day" action="displayDay" component="${component}" pageName="${pageName}" className="${displayRange=='Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
   		<tags:eventLink linkText="Week" action="displayWeek" component="${component}" pageName="${pageName}" className="${displayRange=='Week'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
    	<tags:eventLink linkText="Month" action="displayMonth" component="${component}" pageName="${pageName}" className="${displayRange=='Month'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    
   		<tags:eventLink linkText="Work Day" action="showWorkDay" component="${component}" pageName="${pageName}" className="${showDayLength=='Work Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
   		<tags:eventLink linkText="Full Day" action="showFullDay" component="${component}" pageName="${pageName}" className="${showDayLength=='Full Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
   		
</div>
  
  
  <c:choose>
	<c:when test="${range == 'Week'}"> 
		<div class="calendarWeekHeaderBox">
			<div class="calendarWeekHourColumnSpacerBox">&nbsp;</div>
			<div class="calendarWeekDayLabelBox">Sunday</div>
			<div class="calendarWeekDayLabelBox">Monday</div>
			<div class="calendarWeekDayLabelBox">Tuesday</div>
			<div class="calendarWeekDayLabelBox">Wednesday</div>
			<div class="calendarWeekDayLabelBox">Thursday</div>
			<div class="calendarWeekDayLabelBox">Friday</div>
			<div class="calendarWeekDayLabelBox">Saturday</div>
		</div>
  </c:when>
  
</c:choose>		
  	
	              