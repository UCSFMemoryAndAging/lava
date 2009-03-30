<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- columnHeader

	Header for a column in a table based on a component list
--%>
<%@ attribute name="labelKey" required="false"  
              description="the column label key to use to look up the label text in the resource bundle. if this
                           is not specified, label is used as the label text, and should be specified" %>
<%@ attribute name="label" required="false"  
              description="the column label. this is deprecated. use labelKey instead" %>
<%@ attribute name="component" required="true" 
              description="the component name for component lists" %>
<%@ attribute name="pageName" required="false" 
              description="the page name for submitting events" %>
              
<%@ attribute name="width" required="false"  
              description="the width of the column--anything that can be evaluted in a TD width attribute"  %>
<%@ attribute name="sort" required="false"  
              description="the sort attribute" %>
<%@ attribute name="colspan" required="false" type="java.lang.Integer"
              description="the HTML colspan attribute for spanning multiple columns" %>

<c:if test="${not empty labelKey}">
	<c:set var="label">
		<spring:message code="${labelKey}"/>
	</c:set>
</c:if>			

<c:if test="${empty pageName}">
	<c:set var="pageName" value="${component}"/>
</c:if>
		

<th <c:if test="${not empty colspan}">colspan="${colspan}"</c:if> <c:if test="${not empty width}">width="${width}"</c:if>>
<c:choose>
	<c:when test="${empty sort}">
		${label}
	</c:when>
	<c:when test="${empty command.components[component].filter.sort[sort]}">
			${label}&nbsp;<a href="javascript:void" onClick="javascript:document.${pageName}._eventId.value='${component}__sort_${sort}';document.${pageName}.submit(); return false;"><img src="images/BUTT_sortAscend.png" alt="Sort" name="${component}__Sort_${sort}" border="0"></a>
	</c:when>
	<c:otherwise>
	    	${label}&nbsp;<a href="javascript:void" onClick="javascript:document.${pageName}._eventId.value='${component}__sort_${sort}';document.${pageName}.submit(); return false;"><img src="${command.components[component].filter.sort[sort]=='true' ? 'images/BUTT_sortedAscend.png' : 'images/BUTT_sortedDescend.png'}" alt="Toggle Sort" name="${component}__Sort_${sort}" border="0"></a>
	</c:otherwise>
</c:choose>
</th>

    
