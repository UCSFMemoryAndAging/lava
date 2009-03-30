<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udshealthhistory2"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="cvHAtt"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Health History</page:param>
  <page:param name="quicklinks">cardio,cerebro,parkinson,otherNeuro,mediMeta,<br/>,depression,subAbuPsych</page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
</c:import>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="cardio" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udshealthhistory2.cardio.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
 <tags:createField property="cvHAtt" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cvAFib" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cvAngio" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cvBypass" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cvPace" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cvChf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cvOthr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cvOthrx" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="cerebro" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udshealthhistory2.cerebro.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="cbStroke" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="strok1Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="strok2Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="strok3Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="strok4Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="strok5Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="strok6Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cbTia" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tia1Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tia2Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tia3Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tia4Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tia5Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tia6Yr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cbOthr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cbOthrx" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="parkinson" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udshealthhistory2.parkinson.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="pd" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="pdYr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="pdOthr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="pdOthrYr" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="otherneuro" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udshealthhistory2.otherNeuro.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="seizures" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="traumBrf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="traumExt" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="traumChr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="ncOthr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="ncOthrx" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="mediMeta" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udshealthhistory2.mediMeta.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="hyperten" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="hyperCho" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="diabetes" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="b12Def" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="thyroid" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="incontU" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="incontF" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="depression" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udshealthhistory2.depression.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="dep2Yrs" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="depOthr" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="subAbuPsych" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udshealthhistory2.subAbuPsych.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="alcohol" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tobac30" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tobac100" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="smokYrs" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="packsPer" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="quitSmok" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="abusOthr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="abusx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="psycDis" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="psycDisx" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
	<page:param name="sectionId">anonymous</page:param>
	<page:param name="view">${componentView}</page:param>
	<page:param name="instructions"> </page:param>
	<tags:createField property="notes['udshealthhistory']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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
  <ui:observe elementIds="cvOthr" component="${componentPrefix}" forValue="^1|^2" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cvOthrx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="cbStroke" component="${componentPrefix}" forValue="^1|^2" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="strok1Yr,strok2Yr,strok3Yr,strok4Yr,strok5Yr,strok6Yr" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="cbTia" component="${componentPrefix}" forValue="^1|^2" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="tia1Yr,tia2Yr,tia3Yr,tia4Yr,tia5Yr,tia6Yr" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="cbOthr" component="${componentPrefix}" forValue="^1|^2" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cbOthrx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="pd" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="pdYr" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="pdOthr" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="pdOthrYr" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="ncOthr" component="${componentPrefix}" forValue="^1|^2" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="ncOthrx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="abusOthr" component="${componentPrefix}" forValue="^1|^2" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="abusx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:observe elementIds="psycDis" component="${componentPrefix}" forValue="^1|^2" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="psycDisx" component="${componentPrefix}"/> 
</ui:formGuide>

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>
</page:applyDecorator>