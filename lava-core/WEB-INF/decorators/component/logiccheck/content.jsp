<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">
  <decorator:getProperty property="component"/>
</c:set>	

<c:set var="componentView">
  <decorator:getProperty property="componentView"/>
</c:set>	

<c:if test="${empty componentView}">
	<c:set var="viewString" value="${component}_view"/>
	<c:set var="componentView" value="${requestScope[viewString]}"/>
</c:if>

<c:set var="componentLogicCheckIssues" value="logiccheckissues"/>
<c:set var="componentConfirmLogic" value="confirmLogic"/>
<c:set var="componentLogicChecksExist" value="logicchecksExist"/>

LOGIC CHECKS HERE...

<!-- LOGICCHECK MESSAGE BOX -->
<%-- first iterate thru existing logiccheck issues to display the messages --%>
<%-- command info messages (i.e. non-error) message codes must start with "info.", so they will not be styled like error codes --%>
<spring:bind path="command">
<div id="messageBox">
<%--  Display error/alert headers only if there is at least one logic check of that type --%>
<!-- List LogicCheck errors -->
<c:set var="headerAlreadyDisplayed" value="false"/>
<c:forEach items="${command.components[componentLogicCheckIssues]}" var="logiccheck"> 
	<c:if test="${not headerAlreadyDisplayed && not logiccheck.isalert && (empty logiccheck.invalidDef || logiccheck.invalidDef==0)}">
	  	<img class="infoMsgIcon" src="images/info.png" alt="logiccheck"/>
		<span class="infoMsgHeaderText"><spring:message code="logiccheck.error.header"/></span>
 	  	<div class="infoMsgHeaderLine"></div>
 	  	<c:set var="headerAlreadyDisplayed" value="true"/>
	</c:if>
</c:forEach>
<c:forEach items="${command.components[componentLogicCheckIssues]}" var="logiccheck"> 
	<c:if test="${not logiccheck.isalert && (empty logiccheck.invalidDef || logiccheck.invalidDef==0)}">
	  	<ol class="infoMsgList">
		<li class="infoMsgListItem">
		<spring:message code="logiccheck.error.item" argumentSeparator=";;;" arguments="${logiccheck.field1itemNum};;;${logiccheck.checkCode};;;${logiccheck.checkDescOutput}"/>
		<BR/>
		</li>
		</ol>
	</c:if>	
</c:forEach>
<!--  List LogicCheck alerts -->
<c:set var="headerAlreadyDisplayed" value="false"/>
<c:forEach items="${command.components[componentLogicCheckIssues]}" var="logiccheck"> 
	<c:if test="${not headerAlreadyDisplayed && logiccheck.isalert && (empty logiccheck.invalidDef || logiccheck.invalidDef==0)}">
	  	<img class="infoMsgIcon" src="images/info.png" alt="logiccheck"/>
		<span class="infoMsgHeaderText"><spring:message code="logiccheck.alert.header"/></span>
 	  	<div class="infoMsgHeaderLine"></div>
 	  	<c:set var="headerAlreadyDisplayed" value="true"/>
	</c:if>
</c:forEach>
<c:forEach items="${command.components[componentLogicCheckIssues]}" var="logiccheck" varStatus="iterator"> 
	<c:if test="${logiccheck.isalert&& (empty logiccheck.invalidDef || logiccheck.invalidDef==0)}">
	  	<ol class="infoMsgList">
		<tags:checkbox property="command.components[${componentLogicCheckIssues}][${iterator.index}].verified" fieldId="${componentLogicCheckIssues}_verified" attributesText="${(componentView == 'view' || componentView == 'status')?' disabled':''}" styleClass="inputData "/>
		<spring:message code="logiccheck.alert.item" argumentSeparator=";;;" arguments="${logiccheck.field1itemNum};;;${logiccheck.checkCode};;;${logiccheck.checkDescOutput}"/>
		<BR/>
 		</ol>
	</c:if>	
</c:forEach>
<!--  List LogicCheck issues that are invalid -->
<c:set var="headerAlreadyDisplayed" value="false"/>
<c:forEach items="${command.components[componentLogicCheckIssues]}" var="logiccheck"> 
	<c:if test="${not headerAlreadyDisplayed && logiccheck.invalidDef==1}">
	  	<img class="infoMsgIcon" src="images/info.png" alt="logiccheck"/>
		<span class="infoMsgHeaderText"><spring:message code="logiccheck.invalidDef.header"/></span>
 	  	<div class="infoMsgHeaderLine"></div>
 	  	<c:set var="headerAlreadyDisplayed" value="true"/>
	</c:if>
</c:forEach>
<c:forEach items="${command.components[componentLogicCheckIssues]}" var="logiccheck" varStatus="iterator"> 
	<c:if test="${logiccheck.invalidDef==1}">
	  	<ol class="infoMsgList">
		<li class="infoMsgListItem">
		<spring:message code="logiccheck.invalidDef.item" argumentSeparator=";;;" arguments="${logiccheck.field1itemNum};;;${logiccheck.checkCode};;;${logiccheck.definition.id}"/>
		<BR/>
		</li>
 		</ol>
	</c:if>	
</c:forEach>

</div>
</spring:bind>

<!-- CONFIRM LOGIC CHECKBOX -->
<%-- add confirmLogic checkbox above any Save/Cancel buttons --%>
<%-- do not show this if no logic check definitions exist for this entity --%>
<c:if test="${componentView != 'view' && componentView != 'status'}">
<c:if test="${not empty command.components[componentLogicChecksExist] && command.components[componentLogicChecksExist]=='1'}">
	<c:set var="confirmLogic_label"><spring:message code="logiccheck.confirmLogic.label"/></c:set>
	<%-- TODO: move the "right alignment" into the styles.css --%>
	<div align="right">
	<tags:fieldLabel fieldId="required_but_not_used" label="${confirmLogic_label}" mode="de" context="i" dataElement="checkbox" requiredField="No" indentLevel="0"/>
	<tags:checkbox property="command.components[${componentConfirmLogic}]" fieldId="logiccheck_confirmLogic" attributesText="" styleClass="inputData "/>
	<%-- add spaces to line up the right side with the buttons --%>
	&nbsp;&nbsp;
	</div>
</c:if>
</c:if>

<!-- SPECIFIC CONTENT: buttons at the top -->
<decorator:body/>





