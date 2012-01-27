<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- eventButton
	create a standard action button
--%>


<%@ attribute name="action" required="true" 
       description="the action/event to use" %>
<%@ attribute name="component" required="true"
       description="the component used to construct the event name"%>
<%@ attribute name="pageName" required="false"
       description="the name of the page/form, used for POST, not GET"%>
<%@ attribute name="buttonText" 
       description="alternate to image"%>
<%@ attribute name="className" 
       description="the name of the class to use"%>
<%@ attribute name="styleDef" 
       description="a style definition"%>   
<%@ attribute name="fragment"
	description="HTML anchor fragment for positioning the page, where fragment is the id of an HTML element. additionally,
	if the fragment is the id of a createField element, then in addition to positioning the page to that element, 
	the decorator will set the focus to that element" %>       
<%@ attribute name="target"
       description="the value of the HTML target attribute used to open the response in a new browser window
                    note: 9/25/07 this is not yet supported by Spring Web Flow, so not used yet"%>          
<%@ attribute name="locked" 
       description="[optional] whether modifying actions would be disabled" %>
<%@ attribute name="javascript" 
       description="[optional] javascript to execute onClick, must end in semi-colon" %>
<%@ attribute name="parameters" required="false"
       description="extra parameters, must be supplied as comma delimited pairs of param names and values
       				e.g. paramname1,paramvalue1,paramname2,paramvalue2"%>

<c:if test="${empty locked}">
	<c:set var="locked" value="false"/>
</c:if>

<c:choose>
<c:when test="${locked}">
<%-- mimic the non-locked state except
     1) there is no action upon click
     2) a locked icon is added
     3) the mouse hover text is set to "LOCKED"
     4) TODO: consider removing the hover background or having an "Action Locked" popup message upon clicking
     Note: adding the locked icon may bump us up to a larger size background icon than when without 
           the icon (set at 10x10) and trailing space adds about 2 characters in width --%>
<a href="javascript:void" class="${(fn:length(buttonText) < 5) ? 'eventButtonSmall ' : (fn:length(buttonText) < 8) ? 'eventButtonMedium ' : (fn:length(buttonText) < 11) ? 'eventButtonLarge ' : 'eventButtonXLarge '} ${empty className ? 'eventButton':className}" style="${empty styleDef ? '':styleDef}" title="LOCKED"><img
   src="images/lock.png" width="10" height="10" border="0"/>&nbsp;${buttonText}</a>
</c:when>
<c:otherwise>
<c:set var="target" value=""/> <%-- somehow in some case this was getting set to something --%>
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
<%-- onClick return false means do not go to the href destination. rather, the javascript submitForm function is doing an HTTP Post --%>
<a href="javascript:void" onClick="javascript:${javascript}document.${pageName}.action='${requestUrl}#${not empty fragment ? fragment : component}';submitForm(document.${pageName}, '${component}__${action}', '${target}'); return false" class="${(fn:length(buttonText) < 7) ? 'eventButtonSmall ' : (fn:length(buttonText) < 10) ? 'eventButtonMedium ' : (fn:length(buttonText) < 13) ? 'eventButtonLarge ' : 'eventButtonXLarge '} ${empty className ? 'eventButton':className}" style="${empty styleDef ? '':styleDef}">${buttonText}</a>
</c:otherwise>
</c:choose>
