<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="uic" uri="http://uitags.sf.net/uitags" %>
<%@ taglib prefix="ui"  tagdir="/WEB-INF/tags" %>

<c:import url="/common/objects.jsp" />

<c:import url="/common/header.jsp">
  <c:param name="title"><select> Example</c:param>
  <c:param name="style">
    .check {
      padding:0px;
      margin:0px;
      list-style-type:none;
    }
  </c:param>
  <c:param name="headContents">
    <script type="text/javascript" src="<c:url value="${requestScope.commonJs}" />"></script>
  </c:param>
</c:import>

<form action="">
  <div>
    <input type="checkbox" id="toggle" />
    <uic:select type="all-none" injectTo="toggle" applyToName="ids" />

    <%-- The following use tag files to make the code less verbose --%>
    <ui:selectInverse id="inverse" applyToName="ids" />
    <ui:selectRange   id="range"   applyToName="ids" />
    <ui:selectAll     id="all"     applyToName="ids" />
    <ui:selectNone    id="none"    applyToName="ids" />
  </div>

  <ul class="check">
  <c:forEach var="c" items="${requestScope.countryList}">
    <li>
      <input type="checkbox" id="<c:out value="${c.key}" />"
          name="ids" value="<c:out value="${c.key}" />" />
      <label for="<c:out value="${c.key}" />">
        <c:out value="${c.value}" />
      </label>
    </li>
  </c:forEach>
  </ul>
</form>

<c:import url="/common/footer.jsp" />
