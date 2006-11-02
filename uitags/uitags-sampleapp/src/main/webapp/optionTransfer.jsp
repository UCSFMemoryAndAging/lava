<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="uic" uri="http://uitags.sf.net/uitags" %>
<c:import url="/common/objects.jsp" />

<c:import url="/common/header.jsp">
  <c:param name="title"><optionTransfer> Example</c:param>
  <c:param name="style">
    select {
      height:90px;
      width:150px;
    }

    #transferAll, #transfer, #return, #returnAll, #transfer2, #return2 {
      height:20px;
      margin:1px 3px 1px 3px;
      width:30px;
    }
  </c:param>
  <c:param name="headContents">
    <script type="text/javascript" src="<c:url value="${requestScope.commonJs}" />"></script>
  </c:param>
</c:import>

<form action="target.jsp" method="post">

<uic:optionTransfer>
  <div>
    <select id="source" multiple="multiple" size="5" style="float:left;">
      <option value="Albania">Albania</option>
      <option value="Andorra">Andorra</option>
      <option value="Armenia">Armenia</option>
      <option value="Austria">Austria</option>
      <option value="Azerbaijan">Azerbaijan</option>
    </select>
    <uic:source injectTo="source" />
    <div style="float:left;">
      <div><input type="button" id="transferAll" value="&gt;&gt;" /></div>
      <uic:transferAll injectTo="transferAll" />
      <div><input type="button" id="transfer" value="&gt;" /></div>
      <uic:transfer injectTo="transfer" />
      <div><input type="button" id="return" value="&lt;" /></div>
      <uic:return injectTo="return" />
      <div><input type="button" id="returnAll" value="&lt;&lt;" /></div>
      <uic:returnAll injectTo="returnAll" />
    </div>
    <select name="target" multiple="multiple" size="5" style="float:left;">
      <option value="Andorra">Andorra</option>
    </select>
    <uic:target injectToName="target" />
  </div>
</uic:optionTransfer>

</form>



<c:import url="/common/footer.jsp" />
