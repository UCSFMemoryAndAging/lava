<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- dateTextBox

     Output HTML text box form element for the specified property including a data picker object and link.
     Property is bound to its corresponding command object to obtain its name and value. 
     Any HTML attributes for the input tag are passed in as an attribute.
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
              description="[optional] size of the text box, in character units. defaults to 20"%>

<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             

<spring:bind path="${property}">
    <%--Popup Calendar  --%>
	<script type="text/javascript">
		var cal_${fieldId} = new CalendarPopup("calendarPopup");
		cal_${fieldId}.showYearNavigation();
		cal_${fieldId}.showYearNavigationInput();

		
	</script>

	<%-- setting javascript submitted=true will prevent alert regarding leaving page without saving that shows up in IE6 --%>    
    <input type="text" name="${status.expression}" id="${fieldId}" value="${status.value}" autocomplete="off" class="${styleClass}" ${attributesText} 
        size="${empty textBoxSize ? 20 : textBoxSize}"
    	onchange="javascript:validateDateEntry(document.getElementById('${fieldId}'));return false;" onBlur="javascript: validateDateEntry(document.getElementById('${fieldId}'));return false;">
    <A href=""  onClick="submitted=true;cal_${fieldId}.select(document.getElementById('${fieldId}'),'${fieldId}_Calendar','MM/dd/yyyy'); return false;"  NAME="${fieldId}_Calendar" ID="${fieldId}_Calendar">
    <img src="images/1day.png" border="0"/></A>
 
	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>


