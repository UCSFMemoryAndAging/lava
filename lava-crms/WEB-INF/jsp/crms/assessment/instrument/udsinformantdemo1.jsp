<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsinformantdemo1"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<c:set var="entity" value="${instrTypeEncoded}"/>
<c:set var="packetType">
	<tags:componentProperty component="instrument" property="packet"/>
</c:set>
<c:if test="${packetType == 'F' || packetType == 'T'}">
	<c:set var="entity" value="followup.${instrTypeEncoded}"/>
</c:if>



<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="inBirMo"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Informant Demographics</page:param>
  <page:param name="quicklinks"> </page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
	<c:param name="followUpCheckbox" value="true"/>
</c:import>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsinformantdemo1.informantDemo.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>

<tags:createField property="inBirMo,inBirYr" separator="/" entity="${entity}" component="${component}"/>
<tags:createField property="inSex" entity="${entity}" component="${component}"/>
<tags:createField property="newInf" entity="${entity}" component="${component}"/>
<tags:createField property="inHisp" entity="${entity}" component="${component}"/>
<tags:createField property="inHispOr" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
<tags:createField property="inHispOx" entity="${entity}" component="${component}"/>
<tags:createField property="inRace" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
<tags:createField property="inRacex" entity="${entity}" component="${component}"/>
<tags:createField property="inRaSec" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
<tags:createField property="inRaSecx" entity="${entity}" component="${component}"/>
<tags:createField property="inRaTer" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
<tags:createField property="inRaTerx" entity="${entity}" component="${component}"/>
<tags:createField property="inEduc" entity="${entity}" component="${component}" labelAlignment="${componentMode == 'dc' ? 'top' :''}"/>
<tags:createField property="inRelTo" entity="${entity}" component="${component}"/>
<tags:createField property="inRelTox" entity="${entity}" component="${component}"/>
<tags:createField property="inLivWth" entity="${entity}" component="${component}"/>
<tags:createField property="inVisits" entity="${entity}" component="${component}"/>
<tags:createField property="inCalls" entity="${entity}" component="${component}"/>
<tags:createField property="inRely" entity="${entity}" component="${component}"/>
<tags:createField property="notes['informantDemo']" entity="${entity}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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

<ui:formGuide ignoreDoOnLoad="true">
    <ui:observe elementIds="instrument_packet" forValue=".+|^$"/>
    <ui:submitForm form="${instrTypeEncoded}" event="instrument__reRender"/>
</ui:formGuide>


<ui:formGuide>
  <ui:observe elementIds="packet" component="instrument" forValue="[F|T]"/>
  <ui:unskip elementIds="newInf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="[F|T]"/>
  <%-- when followUp unchecked --%>
  <ui:unskip elementIds="inHisp,inHispOr,inRace,inRaSec,inRaTer" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
  <ui:unskip elementIds="inEduc" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="I"/>
  <ui:observe elementIds="newInf" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <%-- when followUp checked and newInf Yes --%>
  <ui:unskip elementIds="inHisp,inHispOr,inRace,inRaSec,inRaTer" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
  <ui:unskip elementIds="inEduc" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="newInf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="inHisp" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="inHispOr" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="newInf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="inHispOr" component="${componentPrefix}" forValue="^50" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="inHispOx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="newInf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="inRace" component="${componentPrefix}" forValue="^50" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="inRacex" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="newInf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="inRaSec" component="${componentPrefix}" forValue="^50" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="inRaSecx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="newInf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="inRaTer" component="${componentPrefix}" forValue="^50" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="inRaTerx" component="${componentPrefix}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="newInf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="inRelTo" component="${componentPrefix}" forValue="^7" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="inRelTox" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:observe elementIds="inLivWth" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="inVisits" component="${componentPrefix}"/> 
  <ui:unskip elementIds="inCalls" component="${componentPrefix}"/> 
</ui:formGuide>

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    
	    
