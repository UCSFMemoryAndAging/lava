<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="uic" uri="http://uitags.sf.net/uitags" %>
<%@ taglib prefix="ui"  tagdir="/WEB-INF/tags" %>
<c:import url="/common/objects.jsp" />

<c:import url="/common/header.jsp">
  <c:param name="title"><optionTransfer> Plus Other Tags Example</c:param>
  <c:param name="style">
    select {
      height:180px;
      width:150px;
    }

    #transferAll, #transfer, #return, #returnAll {
      height:20px;
      margin:1px 3px 1px 3px;
      width:30px;
    }

    .uiShift_up, .uiShift_down, .uiSort_ascending, .uiSort_descending {
      height:20px;
      margin:1px;
      width:30px;
    }
  </c:param>
  <c:param name="headContents">
    <script type="text/javascript" src="<c:url value="${requestScope.comboOptionTransferJs}" />"></script>
  </c:param>
</c:import>

<form action="target.jsp" method="post">

<uic:optionTransfer>
  <div style="padding-left:190px;">
    <ui:shiftUp applyTo="target" /> <ui:shiftDown applyTo="target" />
    <ui:sortAscending applyTo="target" /> <ui:sortDescending applyTo="target" />
  </div>
  <div>
    <select id="source" multiple="multiple" size="10" style="float:left;">
      <c:forEach var="c" items="${requestScope.countryList}">
        <option value="${c.value}">${c.key}</option>
      </c:forEach>
    </select>
    <uic:source injectTo="source" />
    <div style="float:left;">
      <div><ui:otTransferAll id="transferAll" /></div>
      <div><ui:otTransfer id="transfer" /></div>
      <div><ui:otReturn id="return" /></div>
      <div><ui:otReturnAll id="returnAll" /></div>
    </div>
    <div style="float:left;">
      <select id="target" name="target" multiple="multiple" size="10" style="float:left;">
        <option value="Andorra">Andorra</option>
      </select>
      <uic:target injectTo="target" />
    </div>
  </div>

  <div style="clear:left; padding-top:3px;"><input type="submit" value="Submit" /></div>
</uic:optionTransfer>
</form>

<c:import url="/common/footer.jsp" />
