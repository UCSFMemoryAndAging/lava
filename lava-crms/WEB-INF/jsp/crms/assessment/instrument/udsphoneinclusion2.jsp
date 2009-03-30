<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsphoneinclusion2"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="cogImp"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Phone Inclusion</page:param>
  <page:param name="quicklinks"> </page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
</c:import>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsphoneinclusion2.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>

<tags:tableForm>

<tags:listRow>
	<tags:listColumnHeader width="75%" labelKey="udsphoneinclusion2.1"/>
	<tags:listCell/>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsphoneinclusion2.1a" inline="false" indent="true"/></tags:listCell>
	<tags:listCell><tags:createField property="cogImp" component="${component}" entity="${instrTypeEncoded}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsphoneinclusion2.1b" inline="false" indent="true"/></tags:listCell>
	<tags:listCell><tags:createField property="physImp" component="${component}" entity="${instrTypeEncoded}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsphoneinclusion2.1c" inline="false" indent="true"/></tags:listCell>
	<tags:listCell><tags:createField property="home" component="${component}" entity="${instrTypeEncoded}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsphoneinclusion2.1d" inline="false" indent="true"/></tags:listCell>
	<tags:listCell><tags:createField property="refused" component="${component}" entity="${instrTypeEncoded}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsphoneinclusion2.1e" inline="false" indent="true"/></tags:listCell>
	<tags:listCell><tags:createField property="other" component="${component}" entity="${instrTypeEncoded}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsphoneinclusion2.1ex" inline="false" indent="true"/></tags:listCell>
	<tags:listCell><tags:createField property="otherx" component="${component}" entity="${instrTypeEncoded}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listColumnHeader labelKey="udsphoneinclusion2.2"/>
	<tags:listCell><tags:createField property="milestone" component="${component}" entity="${instrTypeEncoded}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listColumnHeader labelKey="udsphoneinclusion2.3"/>
	<tags:listCell><tags:createField property="inperson" component="${component}" entity="${instrTypeEncoded}"/></tags:listCell>
</tags:listRow>

</tags:tableForm>

</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
	<page:param name="sectionId">anonymous</page:param>
	<page:param name="view">${componentView}</page:param>
	<page:param name="instructions"> </page:param>
	<tags:createField property="notes['udsphoneinclusion']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

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

<ui:formGuide>
  <ui:observe elementIds="other" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="otherx" component="${componentPrefix}"/>
</ui:formGuide>

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>