<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">
  <decorator:getProperty property="component"/>
</c:set>

<c:set var="messageCodeComponent">
  <decorator:getProperty property="messageCodeComponent"/>
</c:set>
<c:if test="${empty messageCodeComponent}">
	<c:set var="messageCodeComponent" value="${component}"/>
</c:if>

<c:set var="isInstrument">
  <decorator:getProperty property="isInstrument"/>
</c:set>

<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>
<%-- note: for instruments, component is ${instrType}${instrVer}, but componentView comes from 
   "instrument_view" not ${instrType}${instrVer}_view --%>
<c:if test="${isInstrument}">
	<c:set var="componentView" value="${instrument_view}"/>
</c:if>	

<%-- all instrument CRUD is modal --%>
<c:if test="${isInstrument || (componentView == 'edit' && component != 'reportSetup') || componentView == 'add' || componentView == 'delete'}">
	<meta name="decorator" content="modal">
	<%-- some modal componentView's do not have editable data (e.g. 'view','status') so do not want user 
	prompted to confirm exit via the javascript popup window because user does not modify any data.
	i.e. even though they use the modal decorator (for screen real estate purposes or to conform in a flow
	conversation) such views do not require enforcement of modal-ness. therefore pass the componentView value
	to the modal decorator via a meta tag (can not just set the "componentView" javascript var directly
	here because it gets initialized by the modal decorator which is applied after this decorator). --%>
	<meta name="componentView" content="${componentView}"/>
</c:if>
<c:set var="pageHeadingArgs">
	<decorator:getProperty property="pageHeadingArgs"/>
</c:set>
<c:if test="${isInstrument}">
	<c:set var="pageHeadingArgs">
		${pageHeadingArgs},${currentPatient.fullNameNoSuffix},<fmt:formatDate value="${currentVisit.visitDate}" pattern="MMMM d yyyy"/>
	</c:set>
</c:if>	


<!-- get page text from metadata -->
<%-- special case: when doing add diagnosis, it is not an instrument, but its component is 'instrument' so change to 'diagnosis' to get correct page title --%>
<c:set var="pageHeading"><spring:message code="${componentView}.${isInstrument ? 'instrument' : (messageCodeComponent == 'instrument' ? 'diagnosis' : messageCodeComponent)}.pageTitle" arguments="${pageHeadingArgs}" text=""/></c:set>
<c:set var="instructions"><spring:message code="${componentView}.${messageCodeComponent}.instructions" text=""/></c:set>
<c:set var="instructionsCol2"><spring:message code="${componentView}.${messageCodeComponent}.instructionsCol2" text=""/></c:set>
<c:set var="deleteWarning"><spring:message code="${componentView}.${isInstrument ? 'instrument' : messageCodeComponent}.deleteWarning" text=""/></c:set>
<c:set var="changeVersionWarning"><spring:message code="${componentView}.${isInstrument ? 'instrument' : messageCodeComponent}.changeVersionWarning" text=""/></c:set>



<!-- set page title and meta property that can be used later for setting the heading at the top of the content area -->
<html>
<head>
<title>${pageHeading}</title>
<meta name="pageHeading" content="${pageHeading}">
</head>

<body>

<!--  page name is used for the name of the form entity.  Use component name if not specifically specified -->
<c:set var="pageName">
  <decorator:getProperty property="pageName"/>
</c:set>
<c:if test="${empty pageName}">
	<c:set var="pageName" value="${component}"/>
</c:if>

<%-- set focus --%>
<c:set var="focusField">
  <decorator:getProperty property="focusField"/>
</c:set>
<c:if test="${not empty focusField}">
  <%-- prepend to the focusField to get the HTML field id (not field name), as constructed in createField, but note that
  the setFocus method does additional prefixing in case the field is an autocomplete field --%> 
  <c:set var="focusField">
    ${not isInstrument ? component : (componentView == 'doubleEnter' ? 'compareInstrument' : 'instrument')}_<decorator:getProperty property="focusField"/>
  </c:set>
</c:if>
<script type="text/javascript">
//appendEventHandler is the uitags method to chain an onload event handler with uitags onload event handlers

// if the URL contains an anchor fragment to position the page, then this takes precedence, i.e. ignore
// the value of focusField, and use the name of the fragment as the field id, i.e. this requires that the
// fragment passed to eventButton, eventAction, etc. tags is the id of the field for the browser to 
// position to, which then is also used here as the field to set focus to
// note: the reason that use of a fragment takes precedence over setting the focus field to the one passed
// to the decorator is that setting the focus to that field would then re-position the page from the fragment
// to the focus field, thereby rendering useless the effect of the fragment 
if (window.location.hash != null && window.location.hash != '') {
  uiHtml_Window.getInstance().appendEventHandler("load", function(e) {setFocus(window.location.hash.substring(1))});
}
else {
  <c:if test="${not empty focusField}">
    uiHtml_Window.getInstance().appendEventHandler("load", function(e) {setFocus('${focusField}')});  
  </c:if>
}
</script>

<c:if test="${isInstrument && componentView == 'collect'}">
<script language="javascript" type="text/javascript" src="javascript/instrument/collect.js"></script>
</c:if>

<!-- QUICKLINKS -->
<%-- the quicklinks are passed to the decorator, as comma-separated strings (sectionId's) representing 
	the HTML anchor tag's link destination. a resource bundle key is then constructed to retrieve the 
	text associated with the quicklink as follows:
	 [component].[sectionId].quicklink  (for instrs, equates to [instrType][instrVer].[sectionId].quicklink)
	 if not found, sectionQuicklink tag tries:
	 [component].[sectionId].section  (for instrs, equates to [instrType][instrVer].[sectionId].section)
	e.g. patient.clinicPatientDetails.section   or  cdr.memory.quicklink
	a sectionId value of '<br/>' by itself can be used to force a newline in the midst of the quicklinks 
	across the top of the page. --%>
<c:set var="quicklinks">
  <decorator:getProperty property="quicklinks"/>
</c:set>	
<c:if test="${isInstrument && (componentView == 'status' || componentView == 'editStatus')}">
	<%-- all instrument views share the same jsp, but status view should not display the quicklinks --%>
	<c:set var="quicklinks" value=""/>
</c:if>	
<c:if test="${not empty quicklinks}">
<div id="quicklinksBox">
  <c:forTokens items="${quicklinks}" delims="," var="sectionId">
	  <tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="${sectionId}" linkTextKey="${messageCodeComponent}.${sectionId}.quicklink" linkTextKey2="${component}.${sectionId}.quicklink"/>
  </c:forTokens>
</div>  
</c:if>


<!-- HTML FORM -->
<form name="${pageName}" method="post">

<c:if test="${isInstrument && componentView == 'upload'}">
<%-- this is necessary for forms which upload files --%>
<script type="text/javascript">
	document.${pageName}.enctype = "multipart/form-data";
</script>
</c:if>

<input name="_eventId" type="hidden" id="_eventId">
<input name="_flowExecutionKey" type="hidden" id="_flowExecutionKey" value="${flowExecutionKey}">
<decorator:getProperty property="page.hiddenFields"/>

<!-- INFO/ERROR MESSAGE BOX -->
<spring:bind path="command">
<div id="messageBox">
<%-- first iterate thru command errors (aka global errors) to display info messages --%>
<%-- command info messages (i.e. non-error) message codes must start with "info.", so they will not
	be styled like error codes --%>
<c:forEach items="${status.errors.globalErrors}" var="error"> 
	<c:if test="${fn:startsWith(error.code, 'info.')}">
	  	<img class="infoMsgIcon" src="images/info.png" alt="error"/>
		<span class="infoMsgHeaderText">Info</span>
 	  	<div class="infoMsgHeaderLine"></div>
	  	<ol class="infoMsgList">
			<li class="infoMsgListItem"><spring:message message="${error}"/></li>
		</ol>
	</c:if>	
</c:forEach>

<%-- create hidden elements to allow javascript to create an info message.
	example of usage:
document.getElementById("javascriptInfoMsgContainer").style.display = "block";
document.getElementById("javascriptInfoMsg").value = "No Prior Diagnosis Record Exists";
--%>
<div id="javascriptInfoMsgContainer">
  	<img class="infoMsgIcon" src="images/info.png" alt="error"/>
	<span class="infoMsgHeaderText">Info</span>
  	<div class="infoMsgHeaderLine"></div>
  	<input type="text" id="javascriptInfoMsg" class="infoMsgListItem infoJavascriptMsg"/>
</div>  	
<script type="text/javascript">
	document.getElementById("javascriptInfoMsgContainer").style.display = "none";
</script>	

<%-- second, iterate thru command errors to display error messages --%>	
<c:forEach items="${status.errors.globalErrors}" var="error"> 
	<c:if test="${not fn:startsWith(error.code, 'info.')}">
	  	<img class="errorIcon" src="images/error.png" alt="error"/>
		<span class="errorHeaderText">Error</span>
 	  	<div class="errorHeaderLine"></div>
	  	<ol class="errorList">
		<c:choose>
			<c:when test="${isInstrument && error.code == 'required.command' 
						&& (componentView == 'enter' || componentView == 'doubleEnter' || componentView == 'collect')}">
			  	<li class="errorListItem"><spring:message message="${error}"/>
				<%-- if there are required field errors, prompt user if they want to mark missing fields 
					as codes, give them a select box to choose the missing data code to use --%>
					<span class="errorListItemPrompt"><spring:message code="required.instrumentResult"/></span>
					<tags:singleSelectNoBind property="missingDataCode" fieldId="missingDataCode" 
						propertyValue="" list="${missingCodesMap}" styleClass="inputData"/>
				</li>
			</c:when>
			<c:otherwise>
				<li class="errorListItem"><spring:message message="${error}"/></li>
	    	</c:otherwise>
   		</c:choose>
		</ol>
	</c:if>		
</c:forEach>					

<%-- create hidden elements to allow javascript to create an error message.
	example of usage:
document.getElementById("javascriptErrorMsgContainer").style.display = "block";
document.getElementById("javascriptErrorMsg").value = "No Prior Diagnosis Record Exists";
--%>
<div id="javascriptErrorMsgContainer">
  	<img class="errorIcon" src="images/error.png" alt="error"/>
	<span class="errorHeaderText">Error</span>
  	<div class="errorHeaderLine"></div>
  	<input type="text" id="javascriptErrorMsg" class="errorListItem errorJavascriptMsg"/>
</div>  	
<script type="text/javascript">
	document.getElementById("javascriptErrorMsgContainer").style.display = "none";
</script>	

</div>
</spring:bind>
    
    <%--Special text to display when deleting an object --%>
    <c:if test="${not empty deleteWarning}">
			<fieldset class="anonymous">
			<div class="deleteWarning">
			    	${deleteWarning}
            </div>
            </fieldset>
	</c:if>
	<%--Special text to display when changing the version of an instrument --%>
    <c:if test="${not empty changeVersionWarning}">
			<fieldset class="anonymous">
			<div class="deleteWarning">
			    	${changeVersionWarning}
            </div>
            </fieldset>
	</c:if>
	<%--Special text to display when a disconnect or expiration event is pending --%>
    <c:if test="${not empty lavaSessionMonitoringMessage}">
			<fieldset class="anonymous">
			<div class="deleteWarning">
			    	${lavaSessionMonitoringMessage}
            </div>
            </fieldset>
	</c:if>

    <c:choose>
       	<c:when test="${not empty instructions && not empty instructionsCol2}">
			<fieldset class="anonymous">
			<div class="sectionInstructions">
			
	            <tags:contentColumn columnClass="colLeft2Cols5050">
    	        	${instructions}
        	    </tags:contentColumn>
            	<tags:contentColumn columnClass="colRight2Cols5050">
            		${instructionsCol2}
	            </tags:contentColumn>
            </div>
            </fieldset>
    	</c:when>
    	<c:when test="${not empty instructions && empty instructionsCol2}">    		
			<fieldset class="anonymous">
			<div class="sectionInstructions">
				${instructions}
			</div>
			</fieldset>
		</c:when>
	</c:choose>

<decorator:body/>
</form>
</body>
</html>
