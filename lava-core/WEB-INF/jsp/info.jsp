<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- page for user info messages when there is no other applicable page
e.g. used by CalculateController --%>

<html>
<head>
<meta name="decorator" content="main">
<%-- signal to the main decorator, for special handling when it is decorating the error page --%>
<meta name="isErrorPage" content="true">
</head>
<body>
<%-- set javascript submitted=true so does not prompt about leaving page without saving, which normally happens
     on modal pages --%>
<script type="text/javascript">
	submitted = true;
</script>

<p>&nbsp;</p>
<p>&nbsp;</p>

<div id="messageBox">
	<img class="infoMsgIcon" src="images/1uparrow.png" alt="error"/>
	<span class="infoMsgHeaderText">Info</span>
  	<div class="infoMsgHeaderLine"></div>
  	<ol class="infoMsgList">
		${infoMessage}
	</ol>
</div>

</body>
</html>
