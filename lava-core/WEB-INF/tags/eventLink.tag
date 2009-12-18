<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- eventLink
	create a standard event action link
--%>
<%@ attribute name="linkText" required="true" 
       description="the text of the link" %>
<%@ attribute name="action" required="true" 
       description="the action/event to use" %>
<%@ attribute name="component" required="true"
       description="the component for the action"%>
<%@ attribute name="pageName" required="true"
       description="the name of the page/form"%>
<%@ attribute name="className" 
       description="the name of the class to use"%>
<%@ attribute name="styleDef" 
       description="a style definition"%>
<%@ attribute name="target"
       description="the value of the HTML target attribute used to open the response in a new browser window
                    note: 9/25/07 this is not yet supported by Spring Web Flow, so not used yet"%>          
<%@ attribute name="parameters" required="false"
       description="extra parameters, must be supplied as comma delimited pairs of param names and values
       				e.g. paramname1,paramvalue1,paramname2,paramvalue2"%>

<%-- TODO: the following code assumes there is already a query string parameter, such that the connector
is always '&' and never the '?' which precedes the initial request query string parameter. since eventLink
is used to submit, it is generally on pages that are subflows, and so the _flowExecutionKey will be 
defined. but if eventLink is ever needed on a root flow page and the URL does not have any query string
parameters to begin with, then this will break --%>
<c:forTokens items="${parameters}" delims="," var="current" varStatus="status">
   	<c:if test="${status.index % 2 == 0}">
       	<c:set var="paramName" value="${current}"/>
   	</c:if>
   	<c:if test="${status.index % 2 == 1}">
   		<c:set var="requestUrl" value="${requestUrl}&${paramName}=${current}"/>
   </c:if>
</c:forTokens>
       
<a href="javascript:void" onClick="javascript:document.${pageName}.action='${requestUrl}#${component}';submitForm(document.${pageName}, '${component}__${action}', '${target}');return false" class="${empty className ? '':className}" style="${empty styleDef ? '':styleDef}">${linkText}</a>      
       

