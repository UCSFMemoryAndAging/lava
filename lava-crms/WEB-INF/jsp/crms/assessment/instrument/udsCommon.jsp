<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="${param.entity}${param.alternateLanguage}.pageTitle"/></page:param>
  <page:param name="view">${param.view}</page:param>
  <page:param name="instructions"> </page:param>
<tags:contentColumn columnClass="colLeft2Col5050">
<tags:createField property="packet" component="instrument" entity="udsinstrument"/>
<tags:createField property="formId" component="instrument" entity="udsinstrument"/>
<tags:createField property="formVer" component="instrument" entity="udsinstrument"/>
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">
<tags:createField property="visitNum" component="instrument" entity="udsinstrument"/>
<tags:createField property="initials" component="instrument" entity="udsinstrument"/>
</tags:contentColumn>
</page:applyDecorator>


<%-- TODO: NEED TO RERENDER BASED ON A CHANGE TO FORMID.  NO RERENDER ON INSTUMENT FLOW --%>


<c:if test="${componentMode != 'vw'}">
<c:forEach begin="0" end="1" var="current">
  <c:choose>
    <c:when test="${componentView == 'doubleEnter' || (componentView == 'compare' && current == 1)}">
      <c:set var="componentPrefix" value="compareInstrument"/>
    </c:when>
    <c:otherwise>
      <c:set var="componentPrefix" value="instrument"/>
    </c:otherwise>
  </c:choose>
  <c:if test="${current == 0 || (current == 1 && componentView == 'compare')}">

	<c:if test="${fn:startsWith(instrTypeEncoded, 'udsmilestone')}">
		<ui:formGuide >
		  <ui:disable elementIds="packet" component="instrument"/>
		  <ui:setValue elementIds="packet" component="instrument" value="M"/>
		</ui:formGuide>
		<ui:formGuide >
		  <ui:disable elementIds="visitNum" component="instrument"/>
		  <ui:setValue elementIds="visitNum" component="instrument" value="-8"/>
		</ui:formGuide>
	</c:if>
	
	
</c:if>
</c:forEach>
</c:if>
