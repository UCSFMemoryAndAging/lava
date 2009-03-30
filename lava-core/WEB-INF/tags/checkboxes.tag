<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- checkboxes

     Output HTML checkboxes for the specified property and with labels and
     values from the specified list. The specified property is bound to its
     corresponding command object to obtain its name and value. 
     
     NOTE: use the checkbox.tag instead for single checkboxes, i.e. not 
           corresponding to a specified list, just a single field
--%>


<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="fieldId" required="true" 
              description="id for the field" %>
<%@ attribute name="list" type="java.util.Map" required="true" 
              description="a Map where entry key is list item value and entry value is list item label" %>
<%@ attribute name="optionsAlignment" required="true"
              description="alignment of checkbox options. valid values:              
                           'groupLeftVertical', 'groupTopVertical', 'horizontal' (which applies to both 
                           group label to left or on top)"%>
<%@ attribute name="attributesText"
              description="[optional] attributes for the HTML element" %>
<%@ attribute name="styleClass"
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              
              
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             

<c:set var="checkboxIndex" value="0"/>   		        
<spring:bind path="${property}">
	<c:set var="firstOption" value="true"/>
    <c:forEach items="${list}" var="entry">
		<%-- in order for vertically stacked checkbox to align horizontally, have to put some
		     content in the left float block. for first checkbox this is the group label, but each
		     successive checkbox needs one --%>
		<c:if test="${firstOption == 'false' && optionsAlignment == 'groupLeftVertical'}">
			<p class="left">
				&nbsp;
			</p>
		</c:if>
		<c:set var="firstOption" value="false"/>
  	    <%-- internal loop below is because status.value could contain multple comma-separated values, 
  	         to represent the case where multiple boxes are checked --%>
		<label for="${fieldId}${checkboxIndex}" class="${optionsAlignment}">
	        <input type="checkbox" id="${fieldId}${checkboxIndex}" name="${status.expression}" value="${entry.key}" class="${styleClass}"
	        <c:forTokens items="${status.value}" delims="," var="checkboxValue">
		            <c:if test="${checkboxValue == $entry.key}">
	    	            checked="checked"
    	    	    </c:if>
		    </c:forTokens>
    	    ${attributesText}
        	>
	        ${entry.value}
      	</label>    
		<c:set var="checkboxIndex" value="${checkboxIndex + 1}"/>
    </c:forEach>
   	<%-- Spring field marker to deal with situation where no checkboxes are checked so no HTTP request param submitted --%>
   	<input type="hidden" name="_${status.expression}">
    
	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>

