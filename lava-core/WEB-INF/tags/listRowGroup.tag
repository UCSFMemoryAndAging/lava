<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- listRowGroup

	 Row group in a table
--%>
<%@ attribute name="style" required="false"  
              description="the style class to be applied to the row group" %>
<tbody ${not empty style ? 'class="${style}"' : 'class="rowGroup"'}>
  <jsp:doBody/>
</tbody>
