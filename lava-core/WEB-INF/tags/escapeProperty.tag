<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- escape property
	 remove characters used to index into lists/maps or derefernce subcomponents from
	 property names so that they can be used as input field id's, etc. 
	 
	 remove in the following order
	 [ = _
	 ].= _
	 ] = _
	 . = _
	 ' = ""
--%>
<%@ attribute name="property" required="true" 
       description="the property to escape" %>
<% jspContext.setAttribute("singlequote", "'"); %>
<c:set var="buffer">${fn:replace(property,'[','_')}</c:set>
<c:set var="buffer">${fn:replace(buffer,'].','_')}</c:set>
<c:set var="buffer">${fn:replace(buffer,']','_')}</c:set>
<c:set var="buffer">${fn:replace(buffer,'.','_')}</c:set>
<c:set var="buffer">${fn:replace(buffer,singlequote,'')}</c:set>
${buffer}

