<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<head><title>Welcome to LAVAweb</title>
<meta name="pageHeading" content="Welcome to LAVAweb">
</head>
<body>

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

<%-- second, iterate thru command errors to display error messages --%>	
<c:forEach items="${status.errors.globalErrors}" var="error"> 
	<c:if test="${not fn:startsWith(error.code, 'info.')}">
	  	<img class="errorIcon" src="images/error.gif" alt="error"/>
		<span class="errorHeaderText">Error</span>
 	  	<div class="errorHeaderLine"></div>
	  	<ol class="errorList">
			<li class="errorListItem"><spring:message message="${error}"/></li>
		</ol>
	</c:if>		
</c:forEach>					

</div>
</spring:bind>


<div style="margin-left: 30px">
		<br/>

<ul>
	<li>LAVAweb is the new web-based data management system for the UCSF Memory and Aging Center.  It will fully replace the existing LAVA data management system by the end of 2007.  Until then, all data can be accessed and entered using either system (all data is stored in the same database). </li><br/>
<!--	<li>The web URL for LAVAweb is <a href="https://128.218.85.232:5282/lava">https://128.218.85.232:5282/lava</a>  (Bookmark this)</li><br/> -->
	<li>The web site can only be accessed from Memory and Aging Center offices and examination areas at UCSF.  Access from other computers requires the use of the <a href="http://its.ucsf.edu/information/network/vpn/">UCSF VPN</a>.</li><br>
	<li><b> The only fully supported browser for LAVAweb is Firefox</b> (if possible, use the latest version 2.0.x. -- version 1.5.x seems to work fine as well). Firefox is available for all major operating systems (Windows, Linux, Mac OS X).  If you do not have Firefox installed on your machine, contact your system administrator.  If the only browser available is Internet Explorer, you may attempt to use it, but not all functionality is guaranteed to work. 
  	</li>
	<BR/>
	<li>If you are having trouble using the site or connecting, you can email or call:<br>
	<ul style="margin-left: 60px">
	<li>Charlie Toohey (LAVAweb System Admin) - ctoohey@memory.ucsf.edu, 476-1872</li>
	<li> or Joe Hesse (Technology Director) - jhesse@memory.ucsf.edu, 502-0590</li>
	
	</ul><br>
<li>Please feel free to provide any feedback or suggestions about LAVAweb via email to Charlie or Joe.</li>
</ul>

<BR/><BR/>

<h3>Major Known Issues / Problems (July 25, 2007)</h3>
<ol style="margin-left: 60px">
	<li>Required Fields not indicated visually.  All required fields should be obvious before submitting any screen.</li>
	
</ol><br/><br/>

<h3>Major Functionality Not Implemented (July 25, 2007) -- Use the old LAVA for these items </h3>
<ul style="margin-left: 60px">
	<li>Specimens</li>
	<li>Many of the Instruments</li>
	<li>Patient Project Details</li>
	<li>Clinic Calendar</li>
	<li>Reporting</li>
</ul>

<br>
<br>

</div>
</body>
</html>
