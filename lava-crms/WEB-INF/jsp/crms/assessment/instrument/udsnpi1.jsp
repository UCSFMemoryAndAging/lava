<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsnpi1"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="npiqInf"/>
	</c:when>
</c:choose>	

<c:set var="alternateLanguage"><tags:componentProperty component="instrument" property="alternateLanguage"/></c:set>


<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS NPI ${not empty alternateLanguage ? ' - ':''}${alternateLanguage}</page:param>
  <page:param name="quicklinks"> </page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
	<c:param name="alternateLanguage" value="${alternateLanguage}"/>
</c:import>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsnpi1.npi.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
	
	<tags:createField property="npiqInf" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="npiqInfx" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="del" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="delSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="hall" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="hallSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="agit" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="agitSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="depD" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="depDSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="anx" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="anxSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="elat" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="elatSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="apa" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="apaSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="disn" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="disnSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="irr" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="irrSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="mot" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="motSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="nite" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="niteSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="app" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="appSev" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="notes['npi']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top"/>
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
  <ui:observe elementIds="npiqInf" component="${componentPrefix}" forValue="^3" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="npiqInfx" component="${componentPrefix}"  comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide >
  <ui:observe elementIds="del" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="delSev" component="${componentPrefix}"  comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide >
  <ui:observe elementIds="hall" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="hallSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide >
  <ui:observe elementIds="agit" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="agitSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="depD" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="depDSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide >
  <ui:observe elementIds="anx" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="anxSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide >
  <ui:observe elementIds="elat" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="elatSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide >
  <ui:observe elementIds="apa" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="apaSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide >
  <ui:observe elementIds="disn" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="disnSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide >
  <ui:observe elementIds="irr" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="irrSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide >
  <ui:observe elementIds="mot" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="motSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide >
  <ui:observe elementIds="nite" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="niteSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
<ui:formGuide  simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:observe elementIds="app" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="appSev" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/> 
</ui:formGuide>
</c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    
	    
