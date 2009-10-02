<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- eventButton
	create a standard action button
--%>


<%@ attribute name="action" required="true" 
       description="the action/event to use" %>
<%@ attribute name="component" required="true"
       description="the component for the action. if supplied by empty, indicates an instrument eventButton
                    until instrument is refactored to component/handler design"%>
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

<c:set var="target" value=""/> <%-- somehow in some case this was getting set to something --%>
<a href="javascript:void" onClick="javascript:document.${pageName}.action='${requestUrl}#${not empty fragment ? fragment : component}';submitForm(document.${pageName}, '${not empty component ? component : ''}${not empty component ? '__' : ''}${action}', '${target}'); return false" class="${(fn:length(buttonText) < 7) ? 'eventButtonSmall ' : (fn:length(buttonText) < 10) ? 'eventButtonMedium ' : (fn:length(buttonText) < 13) ? 'eventButtonLarge ' : 'eventButtonXLarge '} ${empty className ? 'eventButton':className}" style="${empty styleDef ? '':styleDef}">${buttonText}</a>
