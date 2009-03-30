<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsupdrs1"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="pdNormal"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS UPDRS</page:param>
  <page:param name="quicklinks">udsb317,udsb3814</page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
</c:import>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="udsb317" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsupdrs1.1-7.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="pdNormal" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="speech" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="faceXp" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="trestFac" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="trestRHd" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="trestLHd" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="trestRFt" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="trestLFt" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="trActRHd" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="trActLHd" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rigdNeck" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rigdUpRt" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rigdUpLf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rigdLoRt" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rigdLoLf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tapsRt" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tapsRtx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tapsLf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="tapsLfx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="handMovR" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="handMvRx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="handMovL" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="handMvLx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="notes['udsb317']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="udsb3814" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsupdrs1.8-14.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="handAltR" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="handAtRx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="handAltL" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="handAtLx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="legRt" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="legRtx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="legLf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="legLfx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="arising" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="arisingx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="posture" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="posturex" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="gait" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="gaitx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="posStab" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="posStabx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="bradykin" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="notes['udsb3814']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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
  <ui:observe elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="speech,faceXp,trestFac,trestRHd,trestLHd,trestRFt,trestLFt,trActRHd,trActLHd,
	rigdNeck,rigdUpRt,rigdUpLf,rigdLoRt,rigdLoLf,tapsRt,tapsLf,handMovR,handMovL,handAltR,handAltL,
	legRt,legLf,arising,posture,gait,posStab,bradykin"
	component="${componentPrefix}"
	comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="tapsRtx,tapsLfx,handMvRx,handMvLx,handAtRx,handAtLx,legRtx,legLfx,arisingx,posturex,gaitx,posStabx" component="${componentPrefix}"/>
</ui:formGuide>	
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="tapsRt" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="tapsRtx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="tapsLf" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="tapsLfx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="handMovR" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="handMvRx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="handMovL" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="handMvLx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="handAltR" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="handAtRx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="handAltL" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="handAtLx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="legRt" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="legRtx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="legLf" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="legLfx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="arising" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="arisingx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="posture" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="posturex" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="gait" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="gaitx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide ignoreDoOnLoad="true" simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:ignore elementIds="pdNormal" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="posStab" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="posStabx" component="${componentPrefix}"/> 
</ui:formGuide>

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    
	    
