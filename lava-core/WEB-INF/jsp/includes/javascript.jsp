<c:set var="absoluteContextPath">
<%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%>${pageContext.request.contextPath}
</c:set>
<script language="javascript" type="text/javascript" src="${absoluteContextPath}/javascript/common/common.js"></script>
<script language="javascript" type="text/javascript" src="${absoluteContextPath}/javascript/calendar/includes.js"></script>
<script language="javascript" type="text/javascript" src="${absoluteContextPath}/javascript/calendar/CalendarPopup.js"></script>
<script language="javascript" type="text/javascript" src="${absoluteContextPath}/javascript/calendar/CalendarPopup2.js"></script>
<script language="javascript" type="text/javascript" src="${absoluteContextPath}/javascript/autocomplete/common.js"></script>
<script language="javascript" type="text/javascript" src="${absoluteContextPath}/javascript/autocomplete/acselect.js"></script>
<%-- uitags.js does not exist. rather, it is a servlet mapping to the uitags jsProviderServlet which serves the uitags javascript to the browser --%>
<script language="javascript" type="text/javascript" src="${absoluteContextPath}/uitags.js"></script>
<script language="javascript" type="text/javascript" src="${absoluteContextPath}/javascript/nifty/niftycube.js"></script>

