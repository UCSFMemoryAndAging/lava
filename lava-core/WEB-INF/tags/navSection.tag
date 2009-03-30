<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- NavSection

	 Create a navigation section link that links to the given action.
	 --%>
<%@ attribute name="text" required="false"
				description="the text for the section" %>    
<%@ attribute name="textCode" required="false"
				description="a lookup code for the text for the section.  If present overrides text" %>                         
<%@ attribute name="actionId" required="true"  
              description="the action that the tab links to" %>
<%@ attribute name="idParam" required="false"  
              description="optional id param for the action" %>
<%@ attribute name="selected" required="true"  
              description="indicates that the tab is the currently selected tab" %>
<%@ attribute name="disabled" required="false"  
              description="indicates that the tab should be disabled (e.g. modal state)" %>
<%@ attribute name="lastSection" required="false" 
			  description="indicates that this section is the last one for the module and no trailing '|' should be displayed"%>
 
 <c:if test="${not empty textCode}">
 	<c:set var="text">
 		<spring:message code="${textCode}" text="${text}"/>
 	</c:set>
 </c:if>
 
 <c:choose>
 	<c:when test="${disabled == true}">
 	  <a class="${selected == true ? 'nav2Selected' : 'nav2Disabled'}" href="javascript:void;">${text}</a>${lastSection == true ? '' : '&nbsp;&nbsp;|&nbsp;'}
 	</c:when>   
 	<c:otherwise>
 		<c:set var="actionUrl">
 			<tags:actionURL actionId="${actionId}" idParam="${idParam}"/>
 		</c:set>
 		<a class="${selected == true ? 'nav2Selected' : 'nav2Enabled'}" href="${actionUrl}">${text}</a>${lastSection == true ? '' : '&nbsp;&nbsp;|&nbsp;'} 
 	</c:otherwise>	
  </c:choose>
 