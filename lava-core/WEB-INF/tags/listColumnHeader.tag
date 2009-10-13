<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- listColumnHeader

	Header for a column in a table where the list does not have filtering or sorting.
	
	This is useful for tables for which the component data does not come from a ScrollablePagedListHolder,
	e.g. the component is not a list, but rather an entity whose data is formatted into a table (i.e.
	there is paging needed, nor filters or sorts)
	
--%>
<%@ attribute name="labelKey" required="false"  
              description="the column label key to use to look up the label text in the resource bundle. if this
                           is not specified, label is used as the label text, and should be specified" %>
<%@ attribute name="arguments" required="false"
			  description="arguments for textKey as a comma-separated string" %>                           
<%@ attribute name="label" required="false"  
              description="the column label. if not specified, labelKey should be specified" %>
<%@ attribute name="width" required="false"  
              description="the width of the column--anything that can be evaluted in a TD width attribute"  %>
<%@ attribute name="colspan" required="false" type="java.lang.Integer"
              description="the HTML colspan attribute for spanning multiple columns" %>

<c:if test="${not empty labelKey}">
	<c:set var="label">
		<spring:message code="${labelKey}" arguments="${arguments}"/>
	</c:set>
</c:if>			
	
<th <c:if test="${not empty colspan}">colspan="${colspan}"</c:if> <c:if test="${not empty width}">width="${width}"</c:if>>
${label}
</th>

    
