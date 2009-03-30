<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">
  <decorator:getProperty property="component"/>
</c:set>	
<c:set var="pageName">
  <decorator:getProperty property="pageName"/>
</c:set>
<c:if test="${empty pageName}">
	<c:set var="pageName" value="${component}"/>
</c:if>

<%-- prep for page level action/navigation buttons --%>
<c:choose>
	<c:when test="${flowState == 'missing'}">
		<c:set var="numLeftButtons" value="0"/>

        <c:set var="numRightButtons" value="2"/>
		<c:set var="rightbutton1_text" value="Add"/>
		<c:set var="rightbutton1_action" value="addMissing"/>
		<c:set var="rightbutton2_text" value="Defer"/>
		<c:set var="rightbutton2_action" value="skipMissing"/>
	</c:when>

	<c:when test="${flowState == 'groupError'}">
		<c:set var="numLeftButtons" value="0"/>

        <c:set var="numRightButtons" value="1"/>
		<c:set var="rightbutton1_text" value="Close"/>
		<c:set var="rightbutton1_action" value="close"/>
	</c:when>
	
	<c:when test="${flowState == 'deleteAll'}">
        <c:set var="numRightButtons" value="2"/>
		<c:set var="rightbutton1_text" value="Cancel"/>
		<c:set var="rightbutton1_action" value="cancelDeleteAll"/>
		<c:set var="rightbutton2_text" value="Delete"/>
		<c:set var="rightbutton2_action" value="confirmDeleteAll"/>
	</c:when>
	
</c:choose>	

<%-- PAGE LEVEL ACTION/NAVIGATION BUTTONS - TOP --%>
<c:set var="actionNavButtons">
	<%--  note: since right buttons float right, they must go before any buttons to the left, and, put 
			them in the opposite order that they should appear on the right --%>
	<c:forTokens items="right,left" delims="," var="position">
		<c:set var="numButtons" value="${position == 'right' ? numRightButtons : numLeftButtons}"/>
		<c:forEach begin="1" end="${numButtons}" var="current">
			<c:set var="stringText" value="${position}button${current}_text"/>
			<c:set var="text" value="${pageScope[stringText]}"/>
			<c:set var="stringAction" value="${position}button${current}_action"/>
			<c:set var="action" value="${pageScope[stringAction]}"/>
			<c:set var="stringDoGet" value="${position}button${current}_doGet"/>
			<c:set var="doGet" value="${pageScope[stringDoGet]}"/>
			<c:if test="${empty doGet}">
				<tags:eventButton buttonText="${text}" action="${action}" component="${component}" pageName="${pageName}" className="${position == 'left' ? (current == 1 ? 'pageLevelLeftmostButton' : 'pageLevelLeftButton') : (current == 1 ? 'pageLevelRightmostButton' : 'pageLevelRightButton')}"/>
			</c:if>
			<c:if test="${not empty doGet}">
				<%-- if need button to do a GET instead of POST for some reason, use actionURLButton.tag and first
				     do this to get the value for idParam:
				<spring:bind path="command.components['${component}'].id"> 
				use this for the actionId attribute:
				actionId="lava.assessment.instrument.${instrTypeEncoded}"
				and action="${action} 
				also, may need to add onClick="submitted=true" to actionURLButton.tag --%>
			</c:if>			
		</c:forEach>	
	</c:forTokens>
	
	<%-- assumption is that customAction event buttons are on the left --%>
	<c:set var="customActions">
		<decorator:getProperty property="page.customActions"/>
	</c:set>	
	${customActions}
</c:set>

<div id="pageLevelActionNavButtonTopBox">
	${actionNavButtons}
</div>

<!-- CONTEXTUAL INFO -->

<fieldset id="contextualInfoBox">
</fieldset>  

<!-- SPECIFIC CONTENT -->
<div id="contentBox">
	<decorator:body/>
</div>

<!-- BOTTOM HORIZONTAL RULE -->
<div id="skiBox">
</div>

<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS - BOTTOM -->
<div id="pageLevelActionNavButtonBottomBox">    
   ${actionNavButtons}
</div>

  
