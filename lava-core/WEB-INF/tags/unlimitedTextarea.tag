<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- unlimited textarea

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
                   
<%-- note: the size of textarea widgets can be set using rows and cols in the widgetAttributes metadata, or 
alternatively, by creating a style with sizing and passing it into the dataStyle attribute of this tag, 
e.g. the "instrNote" style --%>
            
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             
 
<spring:bind path="${property}">
    <textarea id="${fieldId}" name="${status.expression}" class="${styleClass}" wrap="virtual"  ${attributesText}>${status.value}</textarea>

	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>
