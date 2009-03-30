<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- outputBlankLines

     Outputs n blank lines.
--%>

<%@ attribute name="n" type="java.lang.Short" required="true"
              description="specify the number of blank lines to output" %>

<c:forEach begin="1" end="${n}">
<div class="outputText">&nbsp;</div>
</c:forEach>
