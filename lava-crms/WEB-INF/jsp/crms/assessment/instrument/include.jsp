<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<%-- note that variables must be set at request scope to be available to the including file --%>
<c:set var="componentMode" scope="request" value="${instrument_mode}"/>
<%-- set componentView, e.g. 'enter', 'doubleEnter', 'status', etc. (same strings as used for events) --%>
<c:set var="componentView" scope="request" value="${instrument_view}"/>
<c:set var="component" scope="request" value="${componentView == 'doubleEnter' ? 'compareInstrument' : 'instrument'}"/>

<%-- focusField settings shared across all instruments --%>
<c:choose>
	<c:when test="${componentView == 'collect' || componentView == 'enter'}">
		<c:if test="${fn:startsWith(param.instrTypeEncoded, 'uds')}">
			<c:set var="focusField" scope="request" value="visitNum"/>
		</c:if>			
	</c:when>
	<c:when test="${componentView == 'status'}">	
		<c:set var="focusField" scope="request" value="dcBy"/>
	</c:when>

	<c:when test="${componentView == 'editStatus'}">	
		<c:set var="focusField" scope="request" value="dcBy"/>
	</c:when>
</c:choose>	
