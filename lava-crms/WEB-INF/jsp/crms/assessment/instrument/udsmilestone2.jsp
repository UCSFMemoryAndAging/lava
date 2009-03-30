<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsmilestone2"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="deceased"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Milestone</page:param>
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
  <page:param name="sectionId">anonymous</page:param>
  <tags:outputText textKey="udsmilestone2.milestone.instructions" inline="false"/>  
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
	<page:param name="sectionId"></page:param>
	<tags:createField property="deceased" component="${component}" entity="${instrTypeEncoded}"/>
	<tags:createField property="deathMo"  component="${component}" entity="${instrTypeEncoded}"/>
	<tags:createField property="deathDy"  component="${component}" entity="${instrTypeEncoded}"/>
	<tags:createField property="deathYr"  component="${component}" entity="${instrTypeEncoded}"/>
	<tags:createField property="autopsy" component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">anonymous</page:param>
		<tags:createField property="discont" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="discMo"  component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="discDy"  component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="discYr"  component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="discReas" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="discReax" component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId"></page:param>
		<tags:createField property="rejoined" component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">anonymous</page:param>
		<tags:createField property="nurseHome" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="nurseMo"  component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="nurseDy"  component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="nurseYr"  component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId"></page:param>
		<tags:createField property="protocol" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:outputText textKey="udsmilestone2.protocol.instructions" inline="false"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">anonymous</page:param>
		<tags:createField property="npsyTest" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="npCogImp" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="npPhyIll" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="npHomen" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="npRefus" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="npOthRea" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="npOthRex" component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId"></page:param>
		<tags:createField property="phynData" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="phyCog" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="phyIll" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="phyHome" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="phyRefus" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="phyOth" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="phyOthx" component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
	<page:param name="sectionId">anonymous</page:param>
	<tags:createField property="notes['udsmilestone']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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

<%--Default value of 0/No when the top level fields are null -- mimicks checkbox behavior --%>

<ui:formGuide>
  <ui:observe elementIds="deceased" component="${componentPrefix}" forValue="^$" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:setValue elementIds="deceased" component="${componentPrefix}" value="0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="discont" component="${componentPrefix}" forValue="^$" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:setValue elementIds="discont" component="${componentPrefix}" value="0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="rejoined" component="${componentPrefix}" forValue="^$" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:setValue elementIds="rejoined" component="${componentPrefix}" value="0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="nurseHome" component="${componentPrefix}" forValue="^$" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:setValue elementIds="nurseHome" component="${componentPrefix}" value="0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>
 
<ui:formGuide>
  <ui:observe elementIds="protocol" component="${componentPrefix}" forValue="^$" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:setValue elementIds="protocol" component="${componentPrefix}" value="0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="npsyTest" component="${componentPrefix}" forValue="^$" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:setValue elementIds="npsyTest" component="${componentPrefix}" value="0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="phynData" component="${componentPrefix}" forValue="^$" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:setValue elementIds="phynData" component="${componentPrefix}" value="0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="nurseHome" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="nurseMo,nurseDy,nurseYr" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="npsyTest" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="npCogImp,npPhyIll,npHomen,npRefus,npOthRea" component="${componentPrefix}" unskipValue="0"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:depends elementIds="npsyTest" component="${componentPrefix}"/>
  <ui:observe elementIds="npOthRea" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="npOthRex" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="phynData" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="phyCog,phyIll,phyHome,phyRefus,phyOth" component="${componentPrefix}" unskipValue="0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:depends elementIds="phynData" component="${componentPrefix}"/>
  <ui:observe elementIds="phyOth" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="phyOthx" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:observe elementIds="discont" component="${componentPrefix}" forValue="^1"/>
  <ui:unskip elementIds="discMo,discDy,discYr" component="${componentPrefix}" /> 
  <ui:unskip elementIds="discReas"component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>

<ui:formGuide>
  <ui:depends elementIds="discont" component="${componentPrefix}"/>
  <ui:observe elementIds="discReas" component="${componentPrefix}" forValue="^8"/>
  <ui:unskip elementIds="discReax" component="${componentPrefix}"/> 
</ui:formGuide>

<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:observe elementIds="deceased" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="deathMo,deathDy,deathYr" component="${componentPrefix}" /> 
  <ui:unskip elementIds="autopsy" component="${componentPrefix}" unskipValue="0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    
	    
