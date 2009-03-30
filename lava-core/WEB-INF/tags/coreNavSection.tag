<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- coreNavSection

	 Create a navigation section links for the default action for the module.
	 --%>
<%@ attribute name="module" required="true"  
              description="the module that the section is within" %>
<%@ attribute name="section" required="true"  
              description="the section that the link represents" %>
<%@ attribute name="text" required="false"
				description="the text for the section" %>    
<%@ attribute name="textCode" required="false"
				description="a lookup code for the text for the section. If present overrides text" %>            
         
<%@ attribute name="defaultTargetIdentifier" required="false"  
              description="the default target identifier to use for the default module action" %>
<%@ attribute name="idParam" required="false"  
              description="optional id param for the action" %>
<%@ attribute name="disabled" required="false"  
              description="indicates that the section link should be disabled (e.g. modal state)" %>
<%@ attribute name="lastSection" required="false" 
			  description="indicates that this section is the last one for the module and no trailing '|' should be displayed"%>
     
 
<c:if test="${currentAction.module == module}">    
	<tags:navSection text="${text}" textCode="${textCode}" selected="${currentAction.section == section ? true : false}" 
			actionId="lava.defaultScope.${module}.${section}.${empty defaultTargetIdentifier ? 'defaultAction' : defaultTargetIdentifier}" 
			idParam="${idParam}" disabled="${disabled}" lastSection="${lastSection}"/>
</c:if>
