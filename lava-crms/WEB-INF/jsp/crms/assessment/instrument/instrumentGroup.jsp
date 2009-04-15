<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="instrumentGroup"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
   
<page:applyDecorator name="component.group.content">
  <page:param name="component">${component}</page:param>

<c:choose>
	<c:when test="${flowState == 'missing'}">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
<tags:outputText text="${missingMsg}" inline="false"/>
</page:applyDecorator>	
	</c:when>
	
	<c:when test="${flowState == 'bulkDelete'}">
		<c:import url="/WEB-INF/jsp/crms/assessment/instrument/instrumentGroupBulkDeleteContent.jsp">
		</c:import>
	</c:when>
	
	<c:when test="${flowState == 'groupError'}">
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">anonymous</page:param>
<tags:outputText text="${groupErrorMsg}" inline="false"/>
</page:applyDecorator>	
	</c:when>

</c:choose>


</page:applyDecorator>    
</page:applyDecorator>	  

