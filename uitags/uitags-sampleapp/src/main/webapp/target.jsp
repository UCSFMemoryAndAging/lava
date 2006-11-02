<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ page import="net.sf.uitags.util.UiString" %>

<c:import url="/common/header.jsp">
  <c:param name="title">uitags Request Information</c:param>
  <c:param name="style">
    td, th {
      border:1px solid #aaaaaa;
    }
  </c:param>
  <c:param name="viewSource">false</c:param>
</c:import>

<body>

<p style="width:50%;" align="justify">


<%!

public void printParameter(JspWriter out, String name,
    HttpServletRequest request) throws java.io.IOException {

  String[] values = request.getParameterValues(name);
  if (values != null) {
    if (values.length > 0) {
      out.print(name + ": {");
      
      UiString formatter = new UiString("{0}",
         UiString.OPTION_HTML_ENCODING | UiString.OPTION_AUTO_SURROUND);
      formatter.bind(values[0]);
      out.print(formatter.construct());

      formatter = new UiString(", {0}",
         UiString.OPTION_HTML_ENCODING | UiString.OPTION_AUTO_SURROUND);
      for (int i = 1; i < values.length; ++i) {
        formatter.clearBindParameters();
        formatter.bind(values[i]);
        out.print(formatter.construct());
      }
      out.println("}");
    }
    else {
      out.println(name + ": NULL");
    }
  }
}

%>

<b>General</b>
<br />
<%

out.println("Remote host: " + request.getRemoteHost());

%>

<br />
<br />

<b>Parameters</b>
<br />
<%

java.util.Enumeration paramEnum = request.getParameterNames();
if (!paramEnum.hasMoreElements()) {
  out.println("(No request parameter specified)");
}
else {
  while (paramEnum.hasMoreElements()) {
    String name = (String) paramEnum.nextElement();
    printParameter(out, name, request);
    out.println("<br />");
  }
}

%>


</p>

</body>

<c:import url="/common/footer.jsp" />
