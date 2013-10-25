<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">
  <decorator:getProperty property="component"/>
</c:set>	
<c:set var="pageName">
  <decorator:getProperty property="pageName"/>
</c:set>

<c:set var="componentView">
  <decorator:getProperty property="componentView"/>
</c:set>	

<c:if test="${empty componentView}">
	<c:set var="viewString" value="${component}_view"/>
	<c:set var="componentView" value="${requestScope[viewString]}"/>
</c:if>

<c:if test="${empty pageName}">
	<c:set var="pageName" value="${component}"/>
</c:if>

<c:set var="locked">
  <decorator:getProperty property="locked"/>
</c:set>

<%-- add logic check issues, connecting it to the top navigation buttons --%>
<page:applyDecorator name="component.logiccheck.content">
  <page:param name="component">${component}</page:param>


<c:set var="numStdEventButtons" value="2"/>
<c:choose>
	<c:when test="${componentView=='view'}">
	<%-- hide the close button if this is the root flow. this assumes that all view views in the system
		that can be root flows are also the defaultPatientAction, where if there were a close button, the 
		close action would result in going to the same view. so far, this appears to be the case, 
		e.g. these view views can be root flows if they are navigated to via tabs, subtabs or left nav 
		actions, and are also the defaultPatientAction for their module.section: View Patient, (nam53) 
		View Randomization. 
		note that these view views can also be navigated to from lists in which case they are subflows, not
		the root flow, so the close button appears, taking the user back to the list.
		note that the other types of views, edit, add, etc. are not the defaultPatientAction anywhere in the
		system, so this does not apply to them --%>
		<c:choose>
			<c:when test="${flowIsRoot}">
				<c:set var="button1_text" value="Edit"/>
				<c:set var="button1_action" value="edit"/>
				<c:set var="numStdEventButtons" value="1"/>
				<c:set var="button1_locked" value="${locked}"/>
			</c:when>
			<c:otherwise>			
				<c:set var="button1_text" value="Close"/>
				<c:set var="button1_action" value="close"/>
				<c:set var="button2_text" value="Edit"/>
				<c:set var="button2_action" value="edit"/>
				<c:set var="button2_locked" value="${locked}"/>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:when test="${componentView=='edit'}">
		<c:set var="button1_text" value="Cancel"/>
		<c:set var="button1_action" value="cancel"/>
		<c:set var="button2_text" value="Save"/>
		<c:set var="button2_action" value="save"/>
	</c:when>
	<c:when test="${componentView=='add'}">
		<c:set var="button1_text" value="Cancel"/>
		<c:set var="button1_action" value="cancelAdd"/>
		<c:set var="button2_text" value="Add"/>
		<c:set var="button2_action" value="saveAdd"/>
	</c:when>
	<c:when test="${componentView=='addMany'}">
		<c:set var="button1_text" value="Close"/>
		<c:set var="button1_action" value="close"/>
		<c:set var="button2_text" value="Add"/>
		<c:set var="button2_action" value="applyAdd"/>
	</c:when>
	<c:when test="${componentView=='delete'}">
		<c:set var="button1_text" value="Cancel"/>
		<c:set var="button1_action" value="cancelDelete"/>
		<c:set var="button2_text" value="Delete"/>
		<c:set var="button2_action" value="confirmDelete"/>
		<c:set var="button2_locked" value="${locked}"/> <%-- should never be true though --%>
	</c:when>
</c:choose>

<%-- for secondary entities, want the page to reposition to that entity on its events, so 
create anchor tag to be used as a target. this works in conjunction with javascript 
that submits the form (eventButton, eventLink) --%>
<c:set var="isSecondary">
  <decorator:getProperty property="isSecondary"/>
</c:set>
<c:if test="${not empty isSecondary}">
<a id="${component}"></a>
</c:if>

<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS - TOP -->
<div id="pageLevelActionNavButtonTopBox">
	<c:set var="customActions">
		<decorator:getProperty property="page.customActions"/>
	</c:set>	
	<%-- NOTE: for buttons float right, they must go before any buttons to the left, and, put them in the opposite order that they should appear on the right --%>

	
	<c:choose>
		<%-- see earlier remarks about testing flowIsRoot --%>
		<c:when test="${numStdEventButtons == 1}">
			<tags:eventButton buttonText="${button1_text}" action="${button1_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightmostButton" target="${button1_target}" locked="${button1_locked}"/>
		</c:when>
		<c:otherwise>			
			<tags:eventButton buttonText="${button1_text}" action="${button1_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightmostButton" target="${button1_target}" locked="${button1_locked}"/>
			<tags:eventButton buttonText="${button2_text}" action="${button2_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightButton" target="${button2_target}" locked="${button2_locked}"/>
		</c:otherwise>
	</c:choose>
	${customActions}
</div>

</page:applyDecorator> <%-- component.logiccheck.content --%>


<!-- CONTEXTUAL INFO -->
<%-- scoped variable for the contextualInfo comma-separated pairs was created above --%>
<c:set var="contextualInfo">
  <decorator:getProperty property="contextualInfo"/>
</c:set>	
<c:if test="${not empty contextualInfo}">
  <fieldset id="contextualInfoBox">
    <c:forTokens items="${contextualInfo}" delims="," var="current" varStatus="status">
      <c:if test="${status.index % 2 == 0}">
        <c:set var="property" value="${current}"/>
      </c:if>
      <c:if test="${status.index % 2 == 1}">
        <tags:createField property="${property}" section="" component="${current}"/>
      </c:if>
    </c:forTokens>
  </fieldset>  
</c:if>

<!-- SPECIFIC CONTENT -->
<div id="contentBox">
	<decorator:body/>
</div>

<div id="skiBox">
</div>

<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS - BOTTOM -->
<div id="pageLevelActionNavButtonBottomBox">           
	<c:choose>
		<%-- see earlier remarks about testing flowIsRoot --%>
		<c:when test="${numStdEventButtons == 1}">
			<tags:eventButton buttonText="${button1_text}" action="${button1_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightmostButton" target="${button1_target}" locked="${button1_locked}"/>
		</c:when>
		<c:otherwise>			
			<tags:eventButton buttonText="${button1_text}" action="${button1_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightmostButton" target="${button1_target}" locked="${button1_locked}"/>
			<tags:eventButton buttonText="${button2_text}" action="${button2_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightButton" target="${button2_target}" locked="${button2_locked}"/>
		</c:otherwise>
	</c:choose>
  
</div>  


