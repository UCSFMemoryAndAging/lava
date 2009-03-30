<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- radioButtons

     Output HTML radio buttons for the specified property and with labels and
     values from the specified list. The specified property is bound to its
     corresponding command object to obtain its name and value. 

     If the list contains any codes (i.e. values that are negative), do not show
     these items, because it would not make sense to use a radio button widget that
     includes codes as there would be too many options. Look at singleSelect
     or comboRadioSelect tags for widgets that are more appropriate for showing codes.
--%>


<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="fieldId" required="true" 
              description="id for the field" %>
<%@ attribute name="list" type="java.util.Map" required="true" 
              description="a Map where entry key is list item value and entry value is list item label" %>
<%@ attribute name="optionsAlignment" required="true"
              description="alignment of radio button options. valid values:
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

<c:set var="radioIndex" value="0"/>   		        
<spring:bind path="${property}">
	<c:set var="firstOption" value="true"/>
    <c:forEach items="${list}" var="entry" varStatus="loopStatus">
        <%-- do not show blank list entry, and do not show missing data code entries (see comments above) --%>
  		<c:if test="${(entry.key != '') and (empty missingCodesMap[entry.key])}">
  			<%-- in order for vertically stacked radio buttons to align horizontally, have to put some
  			     content in the left float block. for first button this is the group label, but each
  			     successive button needs one --%>
  			<c:if test="${firstOption == 'false' && optionsAlignment == 'groupLeftVertical'}">
  				<p class="left">
  					&nbsp;
  				</p>
  			</c:if>
			<c:set var="firstOption" value="false"/>
			<label for="${fieldId}${radioIndex}" class="${optionsAlignment}">
            	<input type="radio" id="${fieldId}${radioIndex}" name="${status.expression}" value="${entry.key}" class="${styleClass}"
		            <c:if test="${status.value == entry.key}">
        		        checked="checked"
		            </c:if>
		            ${attributesText}
        	    >
	            ${entry.value}
            </label>
			<c:set var="radioIndex" value="${radioIndex + 1}"/>
    	</c:if>
    </c:forEach>
    
	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>

