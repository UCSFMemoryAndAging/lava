<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.ucsf.lava.core.action.model.Action" %>
<%@ page import="edu.ucsf.lava.core.action.ActionUtils" %>

<%-- error page designed for exceptions during flows which are not handled such
 that the CustomFlowExceptionHandler handle method is invoked, which sets the
 rootCauseException in the model, among other things --%>

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
  <img class="errorIcon" src="images/error.png" alt="error"/>
  <span class="errorHeaderText">Error</span>
  <div class="errorHeaderLine"></div>
  <ol class="errorList">
    <c:choose>
	    <%-- rootCauseException and stateException are added to model by CustomFlowExceptionHandler 
	        (in superclass TransitionExecutingStateException handle method --%>
		<c:when test="${not empty rootCauseException}">
			root cause:
			${rootCauseException}
		</c:when>
		<%-- exceptionMessage is added to model by the CustomExceptionHandler for handling
		    exceptions outside of flows --%>
		<c:when test="${not empty exceptionMessage}">
			${exceptionMessage}
		</c:when>
	</c:choose>		
  </ol>
</div>

</body>
</html>
