<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- datetime

     Output HTML text box form elements for the specified property including a date control with a data picker object and link
     as well as a time selection "suggest" control.  Binds to the datePart and timePart subproperties of the property. 
     
     uses standard date format of mm/dd/yyyy and time format of hh:mm AM
--%>

<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="fieldId" required="true"  
              description="the id for the input field" %>            
<%@ attribute name="attributesText"
              description="[optional] attributes for the HTML element" %>
<%@ attribute name="styleClass"
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              
<%@ attribute name="textBoxSize"
              description="[optional] size of the text box, in character units. defaults to 15"%>
<%@ attribute name="timeList" 
              description="[optional] the list to use for the time box (defaults to 30 minute interval list)" %>             
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             



<tags:autoCompleteSuggest property="${property}" fieldId="${fieldId}"  list="${empty timeList ? lists['datetime.timeInterval30Minute']: timeList}"
	attributesText="${attributesText}" styleClass="${styleClass}" textBoxSize="${empty textBoxSize ? 10 : textBoxSize}" maxLength="8" />
		
              
