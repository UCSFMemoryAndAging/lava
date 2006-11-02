<%--
  - Demo homepage.
  --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*" %>
<%@ page import="net.sf.uitags.util.ObjectPair" %>
<%
List tags = new ArrayList();
tags.add(new ObjectPair("formGuide",      "formGuide.jsp"));
tags.add(new ObjectPair("calendar",       "calendar.jsp"));
tags.add(new ObjectPair("optionTransfer", "optionTransfer.jsp"));
tags.add(new ObjectPair("panel",          "panel.jsp"));
tags.add(new ObjectPair("select",         "select.jsp"));
tags.add(new ObjectPair("shift",          "shift.jsp"));
tags.add(new ObjectPair("sort",           "sort.jsp"));

request.setAttribute("tags", tags);
%>

<c:import url="/common/header.jsp">
  <c:param name="title">uitags Sample Application</c:param>
  <c:param name="style">
    td, th {
      border:1px solid #aaaaaa;
    }
  </c:param>
  <c:param name="viewSource">false</c:param>
</c:import>


<p style="width:50%; text-align:justify;">
  Follow the links below to view the custom tags in action. Each demo page
  allows you to <b>view its source</b>, we <b>recommend</b> that you do this
  while reading the explanation on the page.
</p>

<ul>
<c:forEach var="tag" items="${requestScope.tags}">
  <li><a href="<c:url value="/${tag.secondObject}" />"><c:out value="${tag.firstObject}" /></a></li>
</c:forEach>
</ul>

<br />
<br />

<%
List combos = new ArrayList();
combos.add(new ObjectPair("calendar + panel = date picker", "comboDatePicker.jsp"));
combos.add(new ObjectPair("optionTransfer on steroids", "comboOptionTransfer.jsp"));

request.setAttribute("combos", combos);
%>

<p style="width:50%; text-align:justify;">
  You can combine different custom tags to create powerful UI components.
</p>

<ul>
<c:forEach var="combo" items="${requestScope.combos}">
  <li><a href="<c:url value="/${combo.secondObject}" />"><c:out value="${combo.firstObject}" /></a></li>
</c:forEach>
</ul>

<c:import url="/common/footer.jsp" />