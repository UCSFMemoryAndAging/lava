<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- textBox

     Output HTML text box form element for the specified property. The specified 
     property is bound to its corresponding command object to obtain its name and value. 
     Any HTML attributes for the input tag are passed in as an attribute.
--%>

<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="fieldId" required="true" 
              description="id for the field" %>
<%@ attribute name="attributesText" 
              description="[optional] attributes for the HTML element" %>
<%@ attribute name="styleClass"
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              
<%@ attribute name="textBoxSize"
	   description="[optional] HTML 'size' attribute for text boxes. Units are characters. This is used to 
	   				size the commonly used autocomplete elements, as well as date/time text boxes and free form 
	   				text boxes (metadata style='string'). If size is not specified in the metadata, the default 
	   				is approx. 20 chars for the font family/size assigned to the input element by the stylesheet"%>
<%@ attribute name="maxLength" 
              description="[optional] HTML 'maxlength' attribute for text boxes, i.e. the max number of characters 
              		allowed in the text box, enforced by the browser. If maxLength is not defined in the metadata,
              		defaults to 255."%>
            
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             

<spring:bind path="${property}">
    <input type="text" id="${fieldId}" name="${status.expression}" value="${status.value}" autocomplete="off" 
		class="${styleClass}" ${attributesText} size="${empty textBoxSize ? 20 : textBoxSize}" maxlength="${empty maxLength ? 255 : maxLength}">

	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>


