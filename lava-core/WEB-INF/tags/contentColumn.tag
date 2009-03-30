<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- contentColumn

	 Enclosing block for a column in a multiple column content layout. The 
	 designated column will wrap the content enclosed by this tag.
	 
	 The column attribute designates the column and its layout.

 
--%>

<%@ attribute name="columnClass" required="true"
              description="designates the column and its layout in terms of the actual style 
              	class for the block which will enclose the content" %>

<%-- wrap the the column divs in a fieldset so that in Mozilla, a non-float div block will enclose
     the float content (i.e. the left column, which floats) so that it will end with a newline
     and following content will start below it. this is the solution to the classic "clearing
     space beneath floated elements, which in this case occurs when the left float column
     content is longer than the right non-float column content.
     
     start the wrapper prior to the left column, and end it following the right column --%>
<c:if test="${columnClass == 'colLeft2Col5050'}">
<fieldset class="colWrapper">
</c:if>

<%-- create the block for the column --%>
	<div class="${columnClass}">

<%-- output the content wrapped by this contentColumntag --%>
		<jsp:doBody/>

<%-- end the column block --%>
	</div>

<%-- for all columns that are the last (rightmost) column in a layout, end the wrapper block --%>
<c:if test="${columnClass == 'colRight2Col5050'}">
</fieldset>
</c:if>
