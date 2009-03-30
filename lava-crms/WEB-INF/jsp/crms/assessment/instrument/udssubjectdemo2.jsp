<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udssubjectdemo2"/>
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
		<c:set var="focusField" value="inMds"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Subject Demographics</page:param>
  <page:param name="quicklinks">udsa1120</page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
</c:import>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="udsa1120" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssubjectdemo2.1-20.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<c:if test="${packetType != 'F' && packetType != 'T'}">
	<tags:createField property="inMds" entity="${entity}" component="${component}"/>
	<tags:createField property="reason" entity="${entity}" component="${component}"/>
	<tags:createField property="reasonx" entity="${entity}" component="${component}"/>
	<tags:createField property="refer" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="referx" entity="${entity}" component="${component}"/>
	<tags:createField property="preStat" entity="${entity}" component="${component}"/>
	<tags:createField property="presPart" entity="${entity}" component="${component}"/>
	<tags:createField property="source" entity="${entity}" component="${component}"/>
</c:if>
	<tags:createField property="birthMo,birthYr" separator="/" entity="${entity}" component="${component}"/>
	<tags:createField property="sex" entity="${entity}" component="${component}"/>
<c:if test="${packetType != 'F' && packetType != 'T'}">
	<tags:createField property="hispanic" entity="${entity}" component="${component}"/>
	<tags:createField property="hispOr" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="hispOrx" entity="${entity}" component="${component}"/>
	<tags:createField property="race" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="racex" entity="${entity}" component="${component}"/>
	<tags:createField property="raceSec" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="raceSecx" entity="${entity}" component="${component}"/>
	<tags:createField property="raceTer" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="raceTerx" entity="${entity}" component="${component}"/>
	<tags:createField property="primLang" entity="${entity}" component="${component}"/>
	<tags:createField property="primLanx" entity="${entity}" component="${component}"/>
	<tags:createField property="educ" entity="${entity}" component="${component}"/>
</c:if>
	<tags:createField property="livSit" entity="${entity}" component="${component}"/>
	<tags:createField property="livSitx" entity="${entity}" component="${component}"/>
	<tags:createField property="independ" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="residenc" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="residenx" entity="${entity}" component="${component}"/>
	<tags:createField property="zip" entity="${entity}" component="${component}"/>
	<tags:createField property="mariStat" entity="${entity}" component="${component}"/>
	<tags:createField property="mariStax" entity="${entity}" component="${component}"/>

<c:if test="${packetType != 'F' && packetType != 'T'}">
	<tags:createField property="handed" entity="${entity}" component="${component}"/>
	
</c:if>
	<tags:createField property="notes['udssubjectdemo']" entity="${entity}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>

<c:if test="${packetType == 'F' || packetType == 'T'}">
	<tags:createField property="inMds" entity="${entity}" component="${component}"/>
	<tags:createField property="reason" entity="${entity}" component="${component}"/>
	<tags:createField property="reasonx" entity="${entity}" component="${component}"/>
	<tags:createField property="refer" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="referx" entity="${entity}" component="${component}"/>
	<tags:createField property="preStat" entity="${entity}" component="${component}"/>
	<tags:createField property="presPart" entity="${entity}" component="${component}"/>
	<tags:createField property="source" entity="${entity}" component="${component}"/>
	<tags:createField property="hispanic" entity="${entity}" component="${component}"/>
	<tags:createField property="hispOr" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="hispOrx" entity="${entity}" component="${component}"/>
	<tags:createField property="race" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="racex" entity="${entity}" component="${component}"/>
	<tags:createField property="raceSec" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="raceSecx" entity="${entity}" component="${component}"/>
	<tags:createField property="raceTer" entity="${entity}" component="${component}" optionsAlignment="groupTopVertical"/>
	<tags:createField property="raceTerx" entity="${entity}" component="${component}"/>
	<tags:createField property="primLang" entity="${entity}" component="${component}"/>
	<tags:createField property="primLanx" entity="${entity}" component="${component}"/>
	<tags:createField property="educ" entity="${entity}" component="${component}"/>
	<tags:createField property="handed" entity="${entity}" component="${component}"/>
</c:if>

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

<%-- note: the combined effect of the uitags w.r.t. followUp checkbox differs from Lava skip logic. in Lava, skips everything it 
           should when followup is checked, and when followUp unchecked it unskips all of those and then runs the skip logic
           for the individual controls. here, everything is skipped when followUp is checked which is akin to Lava, but when
           followUp is unchecked, only unskip those fields which are only dependent upon followUp (i.e. not the *x fields),
           and then only unskip the *x fields, dependent upon their corresponding question field 
           note: actually, when followUp unchecked, Lava seems to be needlessly unskipping even those fields that were
                 never skipped when followUp was checked and needlessly running skip logic on those fields 
     update: starting with next uds form, made things more lavalike with intro of the depends child tag, so that
             a formGuide tag can set depends elements so that the formGuide tags event handling gets called when
             any of those depends elements receive an event, just like in lava where a field handles its event
             by setting direct dependents and then calls event handling on the fields that depend on its direct
             dependents, and so on --%>

<%-- skip/unskip those elements which are dependent upon followUp checkbox. those elements which are dependent upon
     the followUp checkbox (when checked) AND their associated individual question are also skipped here. this means
     they will get unskipped when followUp unchecked, but then tags below will consider the individual question and 
     skip them even though followUp unchecked, i.e. this behavior is dependent upon the ordering of the formGuide tags --%>
     
<ui:formGuide>
  <ui:observe elementIds="packet" component="instrument" forValue="[F|T]" />
  <ui:skip elementIds="inMds,reason,refer,preStat,presPart,source,hispanic,hispOr,race,raceSec,raceTer,primLang,handed"
  	       component="${componentPrefix}"
           comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="reasonx,referx,hispOrx,racex,raceSecx,raceTerx,primLanx,educ" component="${componentPrefix}"/>
</ui:formGuide>           

<%-- for individual questions that have an "Other(specify)" option, which when selected should unskip a related 
     element (and vice versa),  unskip and skip the related element based on whether "Other(specify)" is selected or
     not, but ignore this skip logic if the followUp checkbox is checked, in which case another formGuide tag
     will skip both the question element and its related element --%>
<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="[F|T]"/>
  <ui:observe elementIds="reason" component="${componentPrefix}" forValue="^3" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="reasonx" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="[F|T]"/>
  <ui:observe elementIds="refer" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="referx" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="[F|T]"/>
  <ui:observe elementIds="hispanic" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="hispOr" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="[F|T]"/>
  <ui:observe elementIds="hispOr" component="${componentPrefix}" forValue="^50" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="hispOrx" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="[F|T]"/>
  <ui:observe elementIds="race" component="${componentPrefix}" forValue="^50" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="racex" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="[F|T]"/>
  <ui:observe elementIds="raceSec" component="${componentPrefix}" forValue="^50" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="raceSecx" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="[F|T]"/>
  <ui:observe elementIds="raceTer" component="${componentPrefix}" forValue="^50" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="raceTerx" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="[F|T]"/>
  <ui:observe elementIds="primLang" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="primLanx" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="livSit" component="${componentPrefix}" forValue="^5" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="livSitx" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="residenc" component="${componentPrefix}" forValue="^5" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="residenx" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:observe elementIds="mariStat" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="mariStax" component="${componentPrefix}"/> 
</ui:formGuide>

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    
	    
