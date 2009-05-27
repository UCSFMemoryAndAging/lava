
<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- readonlyPassword

   output a "hidden" text represntation of the password.  Obfuscate length. 
--%>

<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="alignment" 
              description="knowing the alignment of the label may be necessary for readonlyText styling" %>
<%@ attribute name="list" type="java.util.Map" 
              description="a Map where entry key is list item value and entry value is list item label" %>
<%@ attribute name="isDate" type="java.lang.Boolean"
              description="[optional] if the data value is a date" %>              
<%@ attribute name="styleClass"
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              
<%@ attribute name="inlineData" type="java.lang.Boolean"
              description="[optional] if the data element is inline, for multiple properties on same line" %>              
          
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             

<spring:bind path="${property}">
    <c:if test="${inlineData}">
    <span class="${styleClass} inlineData" >
    </c:if>
    <c:if test="${!inlineData}">
    <span class="${styleClass} ${alignment == 'longLeft' ? 'readonlyLongLeft' : (alignment == 'left' ? 'readonlyLeft' : '')}" >
    </c:if>
    **********	
   	</span>
	
	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>

