<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- sectionQuicklink

     Create a hyperlink for a section of a page. A hyperlink which refers to an anchor 
     within the same resource must be identical to the hyperlink that was requested 
     by the browser. 
     
     For GET requests, this is ensured by recording the request URL in a request event
     listener in the requestUrl variable, and using that to form the href attribute value.
     
     For POST requests, this is ensured by virtue of the fact that the <form> action attribute 
     is not used with web flow, so that the action defaults to the current URL. 
     
     This creates both hyperlinks at the top of the page that go to sections,
     and inline hyperlinks at the top of a section that go back to the top.
     
     The text for the quicklinks is retrieved using a linkTextKey resource 
     bundle key in the format:
     	[component].[sectionId].quicklink
     	or, if not found
     	[component].[sectionId].section
     where [sectionid] is also used as the HTML anchor tag link destination name.
     
     If linkTextKey does not result in a message being found, then linkTextKey2
     is tried.
--%>

<%@ attribute name="requestUrl" required="true" 
              description="the URL for the anchor tag" %>
<%@ attribute name="sectionId" required="true" 
              description="the anchor identifier for local anchors" %>
<%@ attribute name="linkTextKey" required="true" 
              description="the text for the hyperlink" %>
<%@ attribute name="linkTextKey2" required="false" 
              description="the text for the hyperlink" %>
<%@ attribute name="sourceSectionId" 
              description="[optional] for section quicklinks to top. the id of that section to 
                 use to make the hyperlink itself the target for the top quicklinks" %>

<c:choose>
	<c:when test="${sectionId == '<br/>'}"> <%-- special handling for wrapping quicklinks --%>
		${sectionId}
	</c:when>
	<c:otherwise>
<spring:message var="linkText" code="${linkTextKey}" text=""/>
<c:if test="${empty linkText}">
	<%-- facilitate using the same name for quicklinks and sections --%>
	<c:set var="linkTextKey" value="${fn:replace(linkTextKey, '.quicklink', '.section')}"/>
	<spring:message var="linkText" code="${linkTextKey}" text=""/>
	<c:if test="${empty linkText}">
		<spring:message var="linkText" code="${linkTextKey2}" text=""/>
		<c:if test="${empty linkText}">
			<c:set var="linkTextKey2" value="${fn:replace(linkTextKey2, '.quicklink', '.section')}"/>
			<spring:message var="linkText" code="${linkTextKey2}" text=""/>
		</c:if>
	</c:if>					
</c:if>

<c:set var="anchorUrl">
${requestUrl}#${sectionId}
</c:set>
	
<c:if test="${sectionId != 'top'}">
  <a class="quicklinkDownIconLink" href="<c:url value='${anchorUrl}'/>" onClick="quicklink=true" tabindex="-1"><img src="images/BUTT_down.png" alt="${linkText}" border="0"></a>
  <a class="quicklinkDownLink" href="<c:url value='${anchorUrl}'/>" onClick="quicklink=true" tabindex="-1">${linkText}</a>
</c:if>

<c:if test="${sectionId == 'top'}">
	<a id="${sourceSectionId}" class="quicklinkUpIconLink" href="<c:url value='${anchorUrl}'/>" onClick="quicklink=true" tabindex="-1"><img src="images/BUTT_up.png" alt="Return to Top" border="0"></a>
	<a class="quicklinkUpLink" href="<c:url value='${anchorUrl}'/>" onClick="quicklink=true" tabindex="-1">${linkText}</a>
</c:if>
	</c:otherwise>
</c:choose>	