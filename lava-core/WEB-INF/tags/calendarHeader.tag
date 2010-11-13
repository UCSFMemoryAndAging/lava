<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- calendarheader

	 renders the calendar header and encapsulates the logic for displaying a day, week, and month view 
 
--%>

<%@ attribute name="component" required="true"
              description="the component name" %>
<%@ attribute name="pageName" required="true"
              description="the page name" %>
<%@ attribute name="description" required="true"
              description="the description for the calendar title bar" %>
<%@ attribute name="calendarDateRange" required="true"
              description="the range to display, 'Day','Week','Month'" %>
<%@ attribute name="dayLength" required="true"
              description="the length of the day to display 'Work Day','Full Day'" %>

              
<div class="calendarTitleBox">		
		<span class="listControlBarCalendarDisplayRange">&nbsp;&nbsp;${description}</span><BR/><BR/>
	 	&nbsp;&nbsp;&nbsp;&nbsp;<tags:eventLink linkText="Previous" action="prevDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
 	      <tags:eventLink linkText="Next" action="nextDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
		  <tags:eventLink linkText="Today" action="displayNow" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>
     	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   
   		<tags:eventLink linkText="Day" action="displayDay" component="${component}" pageName="${pageName}" className="${calendarDateRange=='Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
   		<tags:eventLink linkText="Week" action="displayWeek" component="${component}" pageName="${pageName}" className="${calendarDateRange=='Week'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
    	<tags:eventLink linkText="Month" action="displayMonth" component="${component}" pageName="${pageName}" className="${calendarDateRange=='Month'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    
    	<tags:ifComponentPropertyNotEmpty component="calendar" property="workDays">
    	
   			<tags:eventLink linkText="Work Day" action="showWorkDay" component="${component}" pageName="${pageName}" className="${dayLength=='Work Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
   			<tags:eventLink linkText="Full Day" action="showFullDay" component="${component}" pageName="${pageName}" className="${dayLength=='Full Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
   		
   		</tags:ifComponentPropertyNotEmpty>
</div>
  

  	
	              