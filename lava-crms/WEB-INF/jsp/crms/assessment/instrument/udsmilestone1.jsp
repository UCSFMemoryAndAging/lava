<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsmilestone1"/>
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
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"><spring:message code="udsmilestone1.milestone.instructions"/></page:param>

		<tags:createField property="npsyTest" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="npCogImp" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="npPhyIll" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="npHomen" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="npOthRea" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="npOthRex" component="${component}" entity="${instrTypeEncoded}"/><br/>
</page:applyDecorator>


<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId"></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"></page:param>

		<tags:createField property="phynData" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="phyCog" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="phyIll" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="phyHome" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="phyOth" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="phyOthx" component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>


<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"></page:param>

		<tags:createField property="nurseHome" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:outputText textKey="udsmilestone1.nurseDate.instruction" inline="false"/>
		<tags:createField property="nurseMo"  component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="nurseDy"  component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="nurseYr"  component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId"></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"></page:param>
  
		<tags:createField property="discont" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:outputText textKey="udsmilestone1.discDate.instruction" inline="false"/>
		<tags:createField property="discMo"  component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="discDy"  component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="discYr"  component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="discReas" component="${component}" entity="${instrTypeEncoded}"/><br/>
		<tags:createField property="discReax" component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>


<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"></page:param>

		<tags:createField property="deceased" component="${component}" entity="${instrTypeEncoded}"/>
		<tags:outputText textKey="udsmilestone1.deathDate.instruction" inline="false"/>
		<tags:createField property="deathMo"  component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="deathDy"  component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="deathYr"  component="${component}" entity="${instrTypeEncoded}"/>
		<tags:createField property="autopsy" component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>


<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId"></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"></page:param>

		<tags:createField property="udsActiv" component="${component}" entity="${instrTypeEncoded}"/>
</page:applyDecorator>


<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">anonymous</page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"></page:param>

		<tags:createField property="notes['milestone']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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


<ui:formGuide >
  <ui:observe elementIds="nurseHome" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="nurseMo,nurseDy,nurseYr"component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>

<ui:formGuide >
  <ui:observe elementIds="npsyTest" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="npCogImp,npPhyIll,npHomen,npOthRea" component="${componentPrefix}" /> 
</ui:formGuide>

<ui:formGuide >
  <ui:depends elementIds="npsyTest" component="${componentPrefix}"/>
  <ui:observe elementIds="npOthRea" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="npOthRex" component="${componentPrefix}" /> 
</ui:formGuide>

<ui:formGuide >
  <ui:observe elementIds="phynData" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="phyCog,phyIll,phyHome,phyOth" component="${componentPrefix}"  comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>

<ui:formGuide >
  <ui:depends elementIds="phynData" component="${componentPrefix}"/>
  <ui:observe elementIds="phyOth" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="phyOthx" component="${componentPrefix}"/> 
</ui:formGuide>


<ui:formGuide >
  <ui:observe elementIds="discont" component="${componentPrefix}" forValue="^1"/>
  <ui:unskip elementIds="discMo,discDy,discYr"component="${componentPrefix}"  comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
  <ui:unskip elementIds="discReas"component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>

<ui:formGuide >
  <ui:depends elementIds="discont" component="${componentPrefix}"/>
  <ui:observe elementIds="discReas" component="${componentPrefix}" forValue="^8"/>
  <ui:unskip elementIds="discReax" component="${componentPrefix}"/> 
</ui:formGuide>


<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:observe elementIds="deceased" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="deathMo,deathDy,deathYr"component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}" /> 
  <ui:unskip elementIds="autopsy" component="${componentPrefix}"  comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
 
</ui:formGuide>

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    
	    
