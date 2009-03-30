<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><decorator:title/> (v ${applicationScope.appInfo.version} db=${applicationScope.appInfo.databaseName})</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

<decorator:head/>

<%@ include file="/WEB-INF/jsp/includes/base.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/style.jsp" %>
<%@ include file="/WEB-INF/jsp/includes/javascript.jsp" %>
<script type="text/javascript" src="javascript/common/mainNav.js"></script>

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

  <div id="logoMAC">
    <a href="<tags:actionURL actionId="lava.crms.mylava.${empty currentPatient ? 'defaultAction' : 'defaultPatientAction'}" idParam="${not empty currentPatient ? currentPatient.id : ''}"/>"><img src="images/local${pageContext.request.contextPath}${pageContext.request.contextPath}_logo.gif" alt=""  border="0"></a>
  </div>

	<%-- if decorating the error page, the command object (formObject) may not exist if the error
		was an exception in setupForm/getBackingObjects, so do not display patient/project context
		because they call createField which blows up on a missing command object --%>
	<c:set var="isErrorPage">
  		<decorator:getProperty property="meta.isErrorPage"/>
	</c:set>  	
	
	<%--Context Box --%>
	<c:import url="/WEB-INF/jsp/navigation/context/${currentAction.scope}Context.jsp"/>
	 
<div id="tabBar">
  	<c:set var="modulesURL"><spring:message code="${pageContext.request.contextPath}.modulesURL" text="navigation/tabBar/modules.jsp"/></c:set>
	<c:import url="/WEB-INF/jsp/${modulesURL}"/>
</div>

<div id="tabBarSpacer">
	<div id="loginInfo">
    	${pageContext.request.remoteUser}&nbsp; 
       <a class="nav2Enabled" href="security/logout.lava">(logout)</a>  	    
         
	</div>

 	<c:set var="sectionsURL"><spring:message code="${pageContext.request.contextPath}.sectionsURL" text="navigation/tabBar/sections.jsp"/></c:set>
	<c:import url="/WEB-INF/jsp/${sectionsURL}"/>

  </div>


</div> <!-- end header -->

<div id="leftNav">
	<c:set var="navActionsURL"><spring:message code="${pageContext.request.contextPath}.${currentAction.scope}.${currentAction.module}.${currentAction.section}.navActionsURL" text="${currentAction.scope}/${currentAction.module}/${currentAction.section}/actions.jsp"/></c:set>
	<c:set var="navReportsURL"><spring:message code="${pageContext.request.contextPath}.${currentAction.scope}.${currentAction.module}.${currentAction.section}.navReportsURL" text="${currentAction.scope}/${currentAction.module}/${currentAction.section}/reports.jsp"/></c:set>
	
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
  	<c:set var="footerURL"><spring:message code="${pageContext.request.contextPath}.footerURL" text="navigation/footer/footer.jsp"/></c:set>
	<c:import url="/WEB-INF/jsp/${footerURL}"/>
</div> <!-- end footer -->
<div id="copyright">
   <spring:message code="${pageContext.request.contextPath}.copyright" text="Copyright &copy; 2005-2009 University of California Regents. All Rights Reserved."/>
</div>
 
</div> <!-- end center column (or right, if only two columns) -->

</div> <!-- end wrapper -->

<!-- DIV for popup calendars -->
<DIV ID="calendarPopup" STYLE="position:absolute;visibility:hidden;background-color:white;"></DIV>
 
</body>
</html>
