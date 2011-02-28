<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- list

	 Iterate Over the items of a list, each item in the list is put in "item" and iteration status variable is put in 'iterator'
	 
	 The list must be a ScrollablePagedListHolder.
--%>
<%@ attribute name="component" required="true"  
              description="the list component" %>
<%@ variable name-given="item" scope="NESTED" %>
<%@ variable name-given="iterator" scope="NESTED" %>

<c:forEach items="${command.components[component].pageList}" var="theVar" varStatus="theStatusVar">
<c:set var="item" value="${theVar.entity}"/>
<c:set var="iterator" value="${theStatusVar}"/>
  <jsp:doBody/>
</c:forEach>
