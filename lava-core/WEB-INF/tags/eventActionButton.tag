<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- eventActionButton
	create an event button
	 
	it behaves like eventButton in that there is a POST to bind form values, but like
	a listActionURLButton in that it appears in a list (not at the top and bottom of 
	a page) and uses action images ("ACTION_" prefix instead of "BUTT_", and no 
	rollover, no class, style attributes)
--%>

<%@ attribute name="buttonImage" required="true"
       description="the part of the button image name that varies...e.g. cancel, delete, etc. "%>
<%@ attribute name="action" required="true" 
       description="the action/event to use" %>
<%@ attribute name="component" required="true"
       description="the component for the action. if supplied by empty, indicates an instrument eventButton
                    until instrument is refactored to component/handler design"%>
<%@ attribute name="pageName" required="false"
       description="the name of the page/form, used for POST, not GET"%>
<%@ attribute name="fragment"
	description="HTML anchor fragment for positioning the page, where fragment is the id of an HTML element. additionally,
	if the fragment is the id of a createField element, then in addition to positioning the page to that element, 
	the decorator will set the focus to that element" %>       

<c:set var="target" value=""/> <%-- somehow in some case this was getting set to something --%>
<c:set var="eventId" value="${not empty component ? component : ''}${not empty component ? '__' : ''}${action}"/>
<a href="javascript:void" onClick="javascript:document.${pageName}.action='${requestUrl}#${not empty fragment ? fragment : component}';submitForm(document.${pageName}, '${eventId}', '${target}'); return false"}"><img 
	src="images/ACTION_${buttonImage}.png"  width="16" height="16" border="0" title="${eventId}"/></a>

	