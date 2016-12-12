<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- default import handler defines the "importSetup" entity, but subclasses could override so use
the component value passed --%>
<c:set var="component">
  <decorator:getProperty property="component"/>
</c:set>	
<c:set var="pageName">
  <decorator:getProperty property="pageName"/>
</c:set>
<c:if test="${empty pageName}">
	<c:set var="pageName" value="${component}"/>
</c:if>

<div id="pageLevelActionNavButtonTopBox">
	<c:if test="${flowState == 'edit'}">
		<tags:eventButton buttonText="Import" action="import" component="${component}" pageName="${pageName}"/>
	</c:if>		
	<c:set var="customActions">
		<decorator:getProperty property="page.customActions"/>
	</c:set>	
	${customActions}
</div>

<div id="contentBox">
	<decorator:body/>
</div>

<div id="skiBox">
</div>

<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS - BOTTOM -->
<div id="pageLevelActionNavButtonBottomBox">           
	<c:if test="${flowState == 'edit'}">
		<tags:eventButton buttonText="Import" action="import" component="${component}" pageName="${pageName}"/>
	</c:if>		
    ${customActions}
</div>  


	




