<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- readonlyText

     Output HTML text element with the value of the specified property. The specified 
     property is bound to its corresponding command object to obtain its value. 
     
     If the value can be translated using a list associated with the property, then 
     use the value as the key to the list to obtain the label and display the label, 
     which will make more sense to the user. 
     
     If the value can not be translated because the property does not have an associated 
     list or the value does not exist in the associated list, then just display the value.
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

<% jspContext.setAttribute("newline", "\n"); %>
<spring:bind path="${property}">
    <c:if test="${inlineData}">
    <span class="${styleClass} inlineData" >
    </c:if>
    <c:if test="${!inlineData}">
    <span class="${styleClass} ${alignment == 'longLeft' ? 'readonlyLongLeft' : (alignment == 'left' ? 'readonlyLeft' : '')}" >
    </c:if>
    	<c:choose>
    		<c:when test="${empty status.value}">
				<%-- some browsers do not space the data item properly if the value is empty
				     so output a space to compensate --%> 
    			&nbsp;
    		</c:when>
    		<c:when test="${not empty list[status.value]}">
				${fn:replace(list[status.value],newline,"<br>")}
			</c:when>
			<c:when test="${not empty status.value}">
	        	${fn:replace(status.value,newline,'<br>')}
			</c:when>
<%--			
    	    <c:when test="${isDate}">
--%>    	    
    	        <%-- for now, solution to formatting dates that are not properties of the command object.
    	        
    	             such properties do not get formatted using the registered property editors for the command 
    	             object. normally, spring:bind converts all properties to String using the registered 
    	             property editors, (i.e. status.value is always a String for command properties) but 
    	             these editors are only registered for the command object. so, command date properties 
    	             get formatted per the property editor, while non-command properties get formatted by 
    	             the default JSTL date format, resulting in an inconsistent look.
    	             
    	             at the moment, this is used for context objects on listing pages, where the command object
    	             is the list holder, while the context info is from a bean in context (e.g. currentVisit)
    	             that is not the command object. 
    	             
    	             it is determined that this is not a command object binding if the object is not
    	             a String yet. check the type, and if it is a date, then format it the same way that
    	             the registered property editor is formatting dates, to be consistent. 
    	             
    	             the problem with this solution is that the formatting string is defined in two
    	             places: in initBinder for command properties, and in here, for non-command properties.
    	             
    	             possible other solution is to have a controller register property editors for any
    	             non-command objects that will be used in the view.

				     note: using spring:transform will not work, because the spring:bind which encloses
				           it must be binding to a command object
				           
                     note: while date properties are currently declared java.util.Date, they are mapped using the
                           Hibernate timestamp type, so Hibernate returns java.sql.Timestamp objects which are
                           assigned to these properties, so their run-time type is actually java.sql.Timestamp
                --%>
<%--                
				<% Object statusObject = jspContext.findAttribute("status");
				   Object statusValue = ((org.springframework.web.servlet.support.BindStatus) statusObject).getValue(); 
    	           if (statusValue instanceof java.sql.Timestamp) { %>
	    	         <fmt:formatDate value="${status.value}" pattern="MM/dd/yyyy"/>
         	    <% } else { %>
      	            ${status.value}
   	            <% } %>
    	    </c:when>
--%>   	            
		</c:choose>								        	
	</span>
	
	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
</spring:bind>


