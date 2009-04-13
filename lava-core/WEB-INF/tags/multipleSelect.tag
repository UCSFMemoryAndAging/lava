<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%-- multipleSelect

     Output an HTML select box allowing multiple selections for the specified 
     property and populated with the specified list. The specified property is bound 
     to its corresponding command object to obtain its name and value. 
     
--%>


<%@ attribute name="property" required="true"  
              description="the name of the field to bind to" %>
<%@ attribute name="fieldId" required="true" 
              description="id for the field" %>
<%@ attribute name="list" type="java.util.Map" required="true" 
              description="a Map where entry key is list item value and entry value is list item label" %>
<%@ attribute name="size"
              description="[optional] width of the select box. multiplied by 10 to compute a pixel width
              because select box does not have a standard attribute for setting width. defaults to 20
              for a pixel width of 200"%>
<%@ attribute name="length"
              description="[optional] number of options displayed at once. the maxLength metadata attribute is used
              to set the length of multiple select. defaults to 20"%>
<%@ attribute name="attributesText"
              description="[optional] any additional attributes for the HTML select element" %>
<%@ attribute name="styleClass"
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              
              
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             

<%-- usage: if the items in the select list are String such that both option value and output text
 are the same String, then property can bind to a List<String> and the core registered CustomCollectionEditor
 will handle conversion to and fro.
 if the items in the select list are such that option value is item id (e.g. Long) and output
 text is String, then property can bind to List<Bean> where the CustomCollectionEditor needs to override
 convertElement to convert selected id to Bean object (and Bean may need to implement toString to output id
 as String) --%>

<spring:bind path="${property}">
    <select id="${fieldId}" name="${status.expression}" autocomplete="off" STYLE="width: ${size * 10}px" class="${styleClass}" ${attributesText} size="${length}" multiple>
        <c:forEach items="${list}" var="entry">
           <c:if test="${not empty entry.key}">
              <c:forEach items="${status.value}" var="selectedItem">
                   <c:if test="${entry.key == selectedItem}">
                      <c:set var="selected" value="true"/>
                   </c:if>
              </c:forEach>

              <option value="${entry.key}" 
                 <c:if test="${selected}">selected="selected"</c:if>>
                ${entry.value}
              </option>
	   </c:if>	        
           <c:remove var="selected"/>
        </c:forEach>
    </select>
  
	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>


