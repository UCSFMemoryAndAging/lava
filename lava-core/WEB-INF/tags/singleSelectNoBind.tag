<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- singleSelectNoBind

     Displays an HTML select box for the specified property and populated 
     with the specified list. The specified property is not associated with
     a commmand object, so it is not bound. Instead its value is passed
     in as an attribute.
     
--%>

<%@ attribute name="property" required="true" 
              description="the name of the field" %>
<%@ attribute name="fieldId"
              description="[optional] id for the field. not required because if not binding to command probably do not need an id" %>
<%@ attribute name="propertyValue" required="true" 
              description="the value of the field" %>
<%@ attribute name="list" type="java.util.Map" required="true" 
              description="a Map where entry key is list item value and entry value is list item label" %>
<%@ attribute name="attributesText"   
              description="[optional] attributes for the HTML element" %>
<%@ attribute name="styleClass" 
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              

<select id="${fieldId}" name="${property}" autocomplete="off" class="${styleClass}" ${attributesText}>
  <c:forEach items="${list}" var="entry">
    <option value="${entry.key}" 
       <c:if test="${entry.key == propertyValue}"> selected</c:if>>
       ${entry.value}
    </option>
  </c:forEach>
</select>


