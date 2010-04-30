<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- singleSelect

     Output an HTML select box allowing a single selection for the specified 
     property and populated with the specified list. The specified property 
     is bound to its corresponding command object to obtain its name and value. 
     
--%>


<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="fieldId" required="true" 
              description="id for the field" %>
<%@ attribute name="list" type="java.util.Map" required="true" 
              description="a Map where entry key is list item value and entry value is list item label" %>
<%@ attribute name="listAttributes" 
              description="attributes which enhance the list" %>
<%@ attribute name="attributesText" 
              description="[optional] attributes for the HTML element" %>
<%@ attribute name="styleClass"
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              
            
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             
 
<spring:bind path="${property}">
    <select id="${fieldId}" name="${status.expression}" autocomplete="off" class="${styleClass}" ${attributesText}>
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
        	<%-- if the value is an instrument error code, display the corresponding error code text (this situation occurs
        		when the user has chosen to hide codes such that the code/text are not in the underlying list). look up the 
        		error code text in the missingCodesMap (only instruments have a missingCodesMap). 
        		there is one exception: when it can be determined that the property being displayed does not have codes 
        		in its list (e.g. because the property can legitimately have a negative value used by an error code) --%>
			<c:choose>
				<c:when test="${not empty missingCodesMap[status.value]}">
					<c:if test="${empty listAttributes || !fn:contains(listAttributes, 'noCodes')}">
			        	<option value="${status.value}" selected>${not empty missingCodesMap[status.value] ? missingCodesMap[status.value] : status.value}</option> 
					</c:if>
					<c:if test="${fn:contains(listAttributes, 'noCodes')}">
			        	<option value="${status.value}" selected>${status.value}</option> 
					</c:if>			
				</c:when>	       		
		       	<c:otherwise>
		        	<option value="${status.value}" selected>${status.value}</option> 
	       		</c:otherwise>	 
	       	</c:choose>
        </c:if>
        	
    </select>

	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>






