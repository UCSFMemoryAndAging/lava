<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- calendarWorkHour

	 renders an hour on the calendar and sets a class to render the hour using the style for work hours 
--%>

<%@ attribute name="component" required="true"
              description="the component name" %>
<%@ attribute name="pageName" required="true"
              description="the page name" %>
<%@ attribute name="range" required="true"
              description="the range to display, 'Day','Week','Month'" %>
<%@ attribute name="dayLength" required="true"
              description="the length of the day to display 'Work Day','Full Day'" %>
<%@ attribute name="hour" required="true"
              description="the hour to render, e.g. 12,1,2,3,4,5..." %>
<%@ attribute name="suffix" required="true"
              description="the suffix, e.g. 00 or AM or PM" %>
<%@ attribute name="intervals" 
              description="the number of intervals, defaults to 2" %>

 <tags:calendarHour hour="${hour}" suffix="${suffix}" intervals="${intervals}" intervalClass="calendarIntervalBoxWorkHour" component="${component}" pageName="${pageName}" range="${range}" dayLength="${dayLength}"/>
	             
              