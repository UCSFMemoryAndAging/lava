<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- textarea

     Output HTML textarea form element for the specified property. The specified property is 
     bound to its corresponding command object to obtain its name and value. Any textarea
     tag attributes are passed in. 
--%>

<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="fieldId" required="true" 
              description="id for the field" %>
<%@ attribute name="attributesText"  
              description="[optional] attributes for the HTML textarea element, e.g. wrap='virtual' rows='3' cols='35'" %>
<%@ attribute name="styleClass"
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              
<%@ attribute name="maxLength"
	   description="[optional] this is used to limit the number of characters input into a textarea widget. 
                   in HTML there is no limit to the amount of text input into a textarea box. javascript is used 
                   to notify the user when they leave the textarea box if they have entered too many characters, i.e.
                   this does not actually limit the text that can be entered. it just notifies the user when they
                   have gone over. the plan is to modify the javascript used so that the user will be limited
                   and will not be able to type anything when they are at the limit.
                   defaults to 255"%>
                   
<%-- note: the size of textarea widgets can be set using rows and cols in the widgetAttributes metadata, or 
alternatively, by creating a style with sizing and passing it into the dataStyle attribute of this tag, 
e.g. the "instrNote" style --%>
            
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             
 
<spring:bind path="${property}">
    <textarea id="${fieldId}" name="${status.expression}" class="${styleClass}" wrap="virtual" onchange="textareaMaxLength(this,${empty maxLength ? 255 : maxLength})" ${attributesText}>${status.value}</textarea>

	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>
