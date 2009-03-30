<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- NavTag

	 Create a navigation tab that links to the given action.
	 --%>
<%@ attribute name="text" required="true"
				description="the text for the tab" %>              
<%@ attribute name="actionId" required="true"  
              description="the action that the tab links to" %>
<%@ attribute name="idParam" required="false"  
              description="optional id param for the action" %>
<%@ attribute name="selected" required="true"  
              description="indicates that the tab is the currently selected tab" %>
<%@ attribute name="disabled" required="false"  
              description="indicates that the tab should be disabled (e.g. modal state)" %>
 
     
 <c:choose>
 	<c:when test="${disabled == true}">
 		<li class="${selected == true ? 'selected' : 'disabled'}">
  			<a href="javascript:void;">${text}</a>
  		</li>
  	</c:when>
  	<c:otherwise>
  		<c:set var="actionUrl">
  			<tags:actionURL actionId="${actionId}" idParam="${idParam}"/>
  		</c:set>
  		<li class="${selected == true ? 'selected' : 'enabled'}">
  			<a href="${actionUrl}">${text}</a>
  		</li>
  	</c:otherwise>
  </c:choose>
 