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
	


<c:set var="numStdEventButtons" value="2"/>
<c:choose>
	<c:when test="${componentView=='change'}">
			<c:set var="button1_text" value="Cancel"/>
			<c:set var="button1_action" value="cancel"/>
			<c:set var="button2_text" value="Change"/>
			<c:set var="button2_action" value="change"/>
	</c:when>
	<c:when test="${componentView=='reset'}">
		<c:set var="button1_text" value="Cancel"/>
		<c:set var="button1_action" value="cancel"/>
		<c:set var="button2_text" value="Reset"/>
		<c:set var="button2_action" value="reset"/>
	</c:when>
</c:choose>


<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS - TOP -->
<div id="pageLevelActionNavButtonTopBox">
	<c:set var="customActions">
		<decorator:getProperty property="page.customActions"/>
	</c:set>	
	<%-- NOTE: for buttons float right, they must go before any buttons to the left, and, put them in the opposite order that they should appear on the right --%>

	
	<c:if test="${numStdEventButtons >= 1}">
		<tags:eventButton buttonText="${button1_text}" action="${button1_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightmostButton" target="${button1_target}"/>
	</c:if>
	<c:if test="${numStdEventButtons >= 2}">
		<tags:eventButton buttonText="${button2_text}" action="${button2_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightButton" target="${button2_target}"/>
	</c:if>
	<c:if test="${numStdEventButtons >= 3}">
		<tags:eventButton buttonText="${button3_text}" action="${button3_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightButton" target="${button3_target}"/>
	</c:if>
	
	${customActions}
</div>



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
	<c:if test="${numStdEventButtons >= 1}">
		<tags:eventButton buttonText="${button1_text}" action="${button1_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightmostButton" target="${button1_target}"/>
	</c:if>
	<c:if test="${numStdEventButtons >= 2}">
		<tags:eventButton buttonText="${button2_text}" action="${button2_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightButton" target="${button2_target}"/>
	</c:if>
	<c:if test="${numStdEventButtons >= 3}">
		<tags:eventButton buttonText="${button3_text}" action="${button3_action}" component="${component}" pageName="${pageName}"  className="pageLevelRightButton" target="${button3_target}"/>
	</c:if>
  
</div>  


