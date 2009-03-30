<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- tableCell

	 Cell in a table for data, selectors, actions
--%>
<%@ attribute name="colspan" required="false" type="java.lang.Integer"
              description="the HTML colspan attribute for spanning multiple columns" %>
<%@ attribute name="rowspan" required="false" type="java.lang.Integer"
              description="the HTML rowspan attribute for spanning multiple rows" %>
<%@ attribute name="width" required="false"  
              description="the width of the column--anything that can be evaluted in a TD width attribute"  %>
<%@ attribute name="styleClass" required="false"  
              description="the style class to be applied to the row" %>
<%@ attribute name="labelSize" required="false"
              description="[optional] (defaults to short)
                           if 'short', 5% of cell width used by label 
              	           if 'medium', 25% of cell width used by the label
                           if 'wide', 50% of cell width used by label
                           note: the label is to the left of the data element in all cases" %>
              
<jsp:doBody var="tagBody"/>              
<td <c:if test="${not empty colspan}">colspan="${colspan}"</c:if> <c:if test="${not empty rowspan}">rowspan="${rowspan}"</c:if> <c:if test="${not empty width}">width="${width}"</c:if> <c:if test="${not empty styleClass}">class="${styleClass}"</c:if> class="${labelSize == 'medium' ? 'mediumLabels' : (labelSize == 'wide' ? 'wideLabels' : '')}">
  <c:if test="${empty tagBody}">
	  &nbsp;
  </c:if>
  <c:if test="${not empty tagBody}">	  
	  ${tagBody}
  </c:if>	  
</td>
