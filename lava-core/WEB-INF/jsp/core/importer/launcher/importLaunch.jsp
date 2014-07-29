<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="staticPage"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="messageCodeComponent">importLaunch</page:param>
  <page:param name="pageHeadingArgs"></page:param>
 
	<page:applyDecorator name="component.entity.content">
		<page:param name="component">${component}</page:param>
 
		<page:applyDecorator name="component.entity.section">
	  		<page:param name="sectionId">launcher</page:param>
  			<page:param name="sectionNameKey">import.launcher.section</page:param>
  			
  			<%-- links/content goes here --%>
  			
		</page:applyDecorator>
	</page:applyDecorator>    
</page:applyDecorator>	    

