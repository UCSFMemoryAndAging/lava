<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="uic" uri="http://uitags.sf.net/uitags" %>
<%@ taglib prefix="ui"  tagdir="/WEB-INF/tags" %>

<c:import url="/common/objects.jsp" />

<c:import url="/common/header.jsp">
  <c:param name="title"><shift> Example</c:param>
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
 <p>
   Without altering the selection in the select box, click the
   <i>Up</i> button. If you hold the mouse button,
   the selected items will keep shifting upwards.
 </p>

  <div>
    <input type="button" id="up" value="Up" />
    <uic:shift type="up" injectTo="up" applyToName="ids" />

    <%-- The following use tag files to make the code less verbose --%>
    <ui:shiftDown  id="down"  applyToName="ids" value="Down" />
    <ui:shiftFirst id="first" applyToName="ids" />
    <ui:shiftLast  id="last"  applyToName="ids" />
  </div>

  <div style="padding-top:10px;">
    <select name="ids" multiple="multiple">
    <c:forEach var="c" items="${requestScope.countryList}" varStatus="status">
      <option value="<c:out value="${c.key}" />" <c:if test="${status.index == 5 || status.index == 7}">selected="selected"</c:if>>
         <c:out value="${c.value}" />
      </option>
    </c:forEach>
    </select>
  </div>
</form>

<c:import url="/common/footer.jsp" />
