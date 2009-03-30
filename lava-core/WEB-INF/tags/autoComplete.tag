<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- autocompleteSelect

     
--%>


<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="attributesText" required="true"  
              description="attributes for the HTML element" %>
<%@ attribute name="list" type="java.util.Map" required="true" 
              description="a Map where entry key is list item value and entry value is list item label" %>
<%@ attribute name="styleClass"
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              
<%@ attribute name="textBoxSize"
              description="[optional] size of the visible text box, in character units. defaults to 20"%>
<%@ attribute name="fieldId" required="true"  
              description="the id for the autoselect field" %>    
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             
 
<input type="text" autocomplete="off" name="acs_textbox_${fieldId}" id="acs_textbox_${fieldId}"  class="${styleClass}" ${attributesText}
  size="${empty textBoxSize ? 20 : textBoxSize}"
  onkeydown="return acs_object_${fieldId}.handleKeyDown(event)"
  onkeypress="return acs_object_${fieldId}.handleKeyPress(event)"
  onblur="return acs_object_${fieldId}.handleBlur(event)"
  onchange="return acs_object_${fieldId}.handleChange(event)"
  />
<a  onclick="acs_object_${fieldId}.toggleList()" class="${styleClass}"><img src="images/1downarrow.png" border="0"/></a>
<spring:bind path="${property}">

<div id="acs_hidden_block_${fieldId}" class="acsHiddenBlock">
       &nbsp; 
 	   <select name="${status.expression}" id="${fieldId}" style="display:none">
        
        <c:forEach items="${list}" var="entry">
            <option value="${entry.key}" 
               <c:if test="${entry.key == status.value}">selected
               		<c:set var="valueFoundInList" value="1"/>
               </c:if>>
               ${entry.value}
            </option>
        </c:forEach>
        <%-- if the entity has a value that is not in the list, add it to the list --%>
        <c:if test="${empty valueFoundInList}">
            <%-- if currently hiding missing data codes, but value is a missing data code, still
                 want to show the text of that missing data code as the option value, not the code itself --%>
        	<option value="${status.value}" selected>${not empty missingCodesMap[status.value] ? missingCodesMap[status.value] : status.value}</option> 
        </c:if>
        	
    </select>
</div>

	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>

<script language="javascript" type="text/javascript">
		var acs_object_${fieldId} = new acselect(document.getElementById("acs_textbox_${fieldId}"),document.getElementById("${fieldId}"));
</script>





