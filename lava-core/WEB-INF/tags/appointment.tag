<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ attribute name="appointment" type="edu.ucsf.lava.core.calendar.model.Appointment" required="true" 
              description="the appointment to render" %>
<%@ attribute name="displayMode" required="true"  
              description="the displayMode (e.g. day, week, month) to target the rendering to" %>
<%@ attribute name="showMode" required="true" 
              description="the showMode(e.g. work day / full day, work week, full week) to target the rendering to  " %>
<%@ attribute name="hoverDetails" required="false" 
              description="whether to show full appointment details on hover" %>
              