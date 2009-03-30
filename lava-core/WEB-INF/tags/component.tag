<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- component
	 abstract out the component implementation from simple references
--%>
<%@ attribute name="name" required="true" 
       description="the component" %>
command.components['${name}']