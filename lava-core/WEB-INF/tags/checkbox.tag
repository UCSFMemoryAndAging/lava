<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- checkboxes

     Output HTML checkbox for the specified property. The specified property is bound to 
     its corresponding command object to obtain its name and value. 
--%>

<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="fieldId" required="true" 
              description="id for the field" %>
<%@ attribute name="attributesText" 
              description="[optional] attributes for the HTML element" %>
<%@ attribute name="styleClass"
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              
            
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             
 
<spring:bind path="${property}">
	<%-- assuming this checkbox tag is used to set boolean fields, the "value" for the input tag 
	     should match the value of the registered custom editor for Boolean types which is used for
	     binding. at this time, a checked checkbox will submit a value of "1" because that is the
	     value that binds to true (per the registerCustomEditor for Boolean.class in the base class initBinder)
	     if this value may need to be other than "1", or if this tag is to be used for non-boolean 
	     fields, then an attribute could be added to this tag with the value to set, instead of
	     hard-coding "1" --%>
    <%-- when using checkboxes with non-boolean fields, e.g. macdiagnosis.noChgResDx which is a short,
         value 0 means false/no/unchecked, and any value other than 0 means true/yes/checked. therefore,
         just check that the value is not zero, and if not, check the checkbox --%>
    <input type="checkbox" id="${fieldId}" name="${status.expression}" class="${styleClass}" value="1"
	<c:if test="${not empty status.value and status.value != '0'}">
		checked="checked"
	</c:if>
    ${attributesText}
   	>
   	<%-- Spring field marker to deal with situation where box is unchecked so no HTTP request param submitted --%>
    <input type="hidden" name="_${status.expression}">
    
	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>

