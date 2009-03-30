<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- hiddenInput
	creates a bound hidden field. 
--%>

<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="fieldId" required="true" 
              description="id for the field" %>
 <%@ attribute name="attributesText" 
              description="[optional] attributes for the HTML element" %>
 
<spring:bind path="${property}">
    <input type="hidden" id="${fieldId}" name="${status.expression}" value="${status.value}" ${attributesText}>
</spring:bind>


