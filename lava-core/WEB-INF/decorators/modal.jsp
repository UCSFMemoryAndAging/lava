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
<script type="text/javascript" src="javascript/common/modal.js"></script>

<script type="text/javascript">
// this technique is required to chain onload event handlers because uitags has several onload handlers
uiHtml_Window.getInstance().appendEventHandler("load", function(e) {
  Nifty("div#tabBar a","medium transparent top");
});
</script>

<%-- transfer the componentView value to a javascript variable for use in the javascript
confirmExit function to decide whether to prompt user before existing or not --%>
<c:set var="componentView">
  <decorator:getProperty property="meta.componentView"/>
</c:set>
<%-- set componentView in javascript to all decision about confirm exit prompt --%>
<script type="text/javascript">
	componentView = '${componentView}';
</script>

</head>

<body>

<div id="centeringAndSizingWrapper">
<!-- div purely for anchor to return to top -->
<div id="top"></div> 
<div id="header">


  <div id="logoMAC">
    <img src="images/local/${webappInstance}/${webappInstance}_logo.gif" alt="" border="0">
  </div>

	<%--Context Box --%>
	<c:import url="/WEB-INF/jsp/navigation/context/${currentAction.scope}ModalContext.jsp"/>
	
  
  
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


</div> <!-- end header -->
<div id="centerModal">
	<div id="pageHeading">
  		<decorator:getProperty property="meta.pageHeading"/>
    </div>

	<decorator:body />

<div id="footer">
<div id="skiBox">
</div>
	<c:set var="modalFooterURL"><spring:message code="${webappInstance}.modalFooterURL" text="navigation/footer/modalFooter.jsp"/></c:set>
	<c:import url="/WEB-INF/jsp/${modalFooterURL}"/>
</div> <!-- end footer -->
<div id="copyright">
  <spring:message code="${webappInstance}.modalCopyright" text="Copyright &copy; 2005-2012 University of California Regents. All Rights Reserved."/>
</div>

</div> <!-- end center -->


</div> <!-- end wrapper -->
<!-- DIV for popup calendars -->
<DIV ID="calendarPopup" STYLE="position:absolute;visibility:hidden;background-color:white;"></DIV>
 

</body>
</html>
