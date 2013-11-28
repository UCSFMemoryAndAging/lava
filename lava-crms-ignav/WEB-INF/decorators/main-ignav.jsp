<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><decorator:title/> (v ${applicationScope.appInfo.version} db=${applicationScope.appInfo.databaseName})</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<decorator:head/>

<%@ include file="/WEB-INF/jsp/includes/base.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/style.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/javascript.jsp" %>

<script type="text/javascript">
// this technique is required to chain onload event handlers because uitags has several onload handlers
uiHtml_Window.getInstance().appendEventHandler("load", function(e) {
  Nifty("div#tabBar a","medium transparent top");
});
</script>

</head>

<body>

<div id="centeringAndSizingWrapper">
<!-- div purely for anchor to return to top -->
<div id="top"></div> 
<div id="header">

	<%-- SWITCHABLE: use modal or non-modal version of headers (context-box, modules, and sections)
			purely depending on whether in an instrument edit mode, instead of all the prior requirements
			(in content.jsp).  So this may act differently than the rest of the page.  This allows
			us to disallow non-instrument flow changes when an instrument edit page is displayed,
			yet allow instrument flow changes.
		Note: For instruments, componentView will be not be 'edit', but 'enter', 'doubleEnter', etc.
		      And if we wish to use modal context and we're here, only need to check if 
		      instrument and if in an "editing" mode --%>
		      
	<%--
	    do not allow clicking of logo when in instrument edit mode, but if error enable the logo regardless
	    if decorating the error page, the command object (formObject) may not exist if the error
		was an exception in setupForm/getBackingObjects, so do not display patient/project context
		because they call createField which blows up on a missing command object --%>
	<c:set var="isErrorPage">
  		<decorator:getProperty property="meta.isErrorPage"/>
	</c:set>  	

	<c:if test="${empty isErrorPage}">
		<%-- transfer the componentView and isInstrument values to a javascript variable --%>
		<c:set var="componentView">
		  <decorator:getProperty property="meta.componentView"/>
		</c:set>
		<c:set var="isInstrument">
		  <decorator:getProperty property="meta.isInstrument"/>
		</c:set>
	</c:if>
		
  	<c:if test="${empty isErrorPage}">
  		<c:if test="${isInstrument && (componentView != 'view')}">
  			<c:set var="logoHREF" value="javascript:void"/>
  		</c:if>
  	</c:if>

  	<div id="logoMAC">
		<c:choose><c:when test="${empty isErrorPage && (isInstrument && (componentView != 'view'))}">
			<img src="images/local/${webappInstance}/${webappInstance}_logo.gif" alt=""  border="0">			
  		</c:when>
  		<c:otherwise>
		    <a href="<tags:actionURL actionId="lava.defaultScope.home.${empty currentPatient ? 'defaultAction' : 'defaultPatientAction'}" idParam="${not empty currentPatient ? currentPatient.id : ''}"/>"><img src="images/local/${webappInstance}/${webappInstance}_logo.gif" alt=""  border="0"></a>
  		</c:otherwise></c:choose>
  	</div>
  	

	<c:if test="${empty isErrorPage}">
		<%-- when determining if componentView is modal or not, lean on safe side; i.e. assume instruments are modal and only let a few non-modal states in --%>
		<c:set var="inModalMode" value="${isInstrument}"/>
		<c:if test="${isInstrument && (componentView == 'view' || componentView == 'status' || componentView == 'delete')}">
			<c:set var="inModalMode" value="false"/>
		</c:if>
		
		<c:choose>
		  <c:when test="${inModalMode}">
		  	<%--Context Box --%>
		  	<c:import url="/WEB-INF/jsp/navigation/context/${currentAction.scope}ModalContext.jsp"/>
		  </c:when>
		  <c:otherwise>
			<%--Context Box --%>
			<c:import url="/WEB-INF/jsp/navigation/context/${currentAction.scope}Context.jsp"/>
		  </c:otherwise>
		</c:choose>
	</c:if> 

	<c:choose>
	  <c:when test="${empty isErrorPage && inModalMode}">
		<div id="tabBar">
		  <c:set var="modalModulesURL"><spring:message code="${webappInstance}.modalModulesURL" text="navigation/tabBar/modalModules.jsp"/></c:set>
		  <c:import url="/WEB-INF/jsp/${modalModulesURL}"/>
		</div>

		<div id="tabBarSpacer">
		  <div id="loginInfo">
		    ${pageContext.request.remoteUser} 
		  </div>
		  <c:set var="modalSectionsURL"><spring:message code="${webappInstance}.modalSectionsURL" text="navigation/tabBar/modalSections.jsp"/></c:set>
		  <c:import url="/WEB-INF/jsp/${modalSectionsURL}"/>
		</div>
	  </c:when>
	  <c:otherwise>
		<div id="tabBar">
		  	<c:set var="modulesURL"><spring:message code="${webappInstance}.modulesURL" text="navigation/tabBar/modules.jsp"/></c:set>
			<c:import url="/WEB-INF/jsp/${modulesURL}"/>
		</div>
		
		<div id="tabBarSpacer">
			<div id="loginInfo">
		    	${pageContext.request.remoteUser}&nbsp; 
		       <a class="nav2Enabled" href="security/logout.lava">(logout)</a>  	    
			</div>
		
		 	<c:set var="sectionsURL"><spring:message code="${webappInstance}.sectionsURL" text="navigation/tabBar/sections.jsp"/></c:set>
			<c:import url="/WEB-INF/jsp/${sectionsURL}"/>
		</div>
	  </c:otherwise>
	</c:choose>
		 
</div> <!-- end header -->

<div id="leftNav">
	<c:set var="navActionsURL"><spring:message code="${webappInstance}.${currentAction.scope}.${currentAction.module}.${currentAction.section}.navActionsURL" text="${currentAction.scope}/${currentAction.module}/${currentAction.section}/actions.jsp"/></c:set>
	<c:set var="navReportsURL"><spring:message code="${webappInstance}.${currentAction.scope}.${currentAction.module}.${currentAction.section}.navReportsURL" text="${currentAction.scope}/${currentAction.module}/${currentAction.section}/reports.jsp"/></c:set>
	
	<page:applyDecorator name="panel.actions">
		<page:applyDecorator name="panel.actionsSection" page="/WEB-INF/jsp/${navActionsURL}"/>
	</page:applyDecorator>

	<page:applyDecorator name="panel.reports">
		<page:applyDecorator name="panel.reportsSection" page="/WEB-INF/jsp/${navReportsURL}"/>
	</page:applyDecorator>
	
	
	<!-- No SHORTCUTS YET
	<div class="panelHeader">
        MY SHORTCUTS
    </div>
    <div class="panelBody">
       None right now
    </div>
	-->
</div> <!-- end left column -->

<!-- if right column, it goes before center because it is a float -->


<div id="centerLeftNav">
	<div id="pageHeading">
  		<decorator:getProperty property="meta.pageHeading"/>
    </div>
 
   <decorator:body />
 


 
<div id="footer">
<div id="skiBox">
</div>
  	<c:set var="footerURL"><spring:message code="${webappInstance}.footerURL" text="navigation/footer/footer.jsp"/></c:set>
	<c:import url="/WEB-INF/jsp/${footerURL}"/>
</div> <!-- end footer -->
<div id="copyright">
   <spring:message code="${webappInstance}.copyright" text="Copyright &copy; 2005-2013 University of California Regents. All Rights Reserved."/>
</div>
 
</div> <!-- end center column (or right, if only two columns) -->

</div> <!-- end wrapper -->

<!-- DIV for popup calendars -->
<DIV ID="calendarPopup" STYLE="position:absolute;visibility:hidden;background-color:white;"></DIV>
 
</body>
</html>
