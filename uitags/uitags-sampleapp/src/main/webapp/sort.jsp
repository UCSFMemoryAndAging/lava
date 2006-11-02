<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="uic" uri="http://uitags.sf.net/uitags" %>
<%@ taglib prefix="ui"  tagdir="/WEB-INF/tags" %>

<c:import url="/common/objects.jsp" />

<c:import url="/common/header.jsp">
  <c:param name="title"><sort> Example</c:param>
  <c:param name="style">
    select {
      width:220px;
      height:300px;
    }
  </c:param>
  <c:param name="headContents">
    <script type="text/javascript" src="<c:url value="${requestScope.commonJs}" />"></script>
  </c:param>
</c:import>

<form action="">
  <div>
    <input type="button" id="sortAsc" value="A-Z" />
    <uic:sort type="ascending" injectTo="sortAsc" applyTo="ids" />

    <%-- The following uses a tag file to make the code less verbose --%>
    <ui:sortDescending applyTo="ids" />
  </div>

  <div style="padding-top:10px;">
    <select id="ids" multiple="multiple">
    <c:forEach var="c" items="${requestScope.countryList}">
      <option value="<c:out value="${c.key}" />"><c:out value="${c.value}" /></option>
    </c:forEach>
    </select>
  </div>
</form>

<c:import url="/common/footer.jsp" />
