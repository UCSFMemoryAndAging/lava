<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsformchecklist2"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="a1Sub"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Form Checklist</page:param>
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
  <page:param name="section"><spring:message code="udsformchecklist2.formchecklist.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>

  
<tags:tableForm>

<tags:listRow>
	<tags:listColumnHeader labelKey="udsformchecklist2.FormColHeader" width="5%"/>
	<tags:listColumnHeader labelKey="udsformchecklist2.DescColHeader" width="30%"/>
	<tags:listColumnHeader labelKey="udsformchecklist2.SubColHeader" width="17%"/>
	<tags:listColumnHeader labelKey="udsformchecklist2.IfNotSubColHeader" width="25%"/>
	<tags:listColumnHeader labelKey="udsformchecklist2.CommColHeader" width="23%"/>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.A1Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.A1Desc"/></tags:listCell>
	<tags:listCell><b><tags:outputText textKey="udsformchecklist2.Required"/></b></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.A2Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.A2Desc"/></tags:listCell>
	<tags:listCell><tags:createField property="a2Sub" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="a2Not" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="a2Comm" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.A3Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.A3Desc"/></tags:listCell>
	<tags:listCell><tags:createField property="a3Sub" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="a3Not" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="a3Comm" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.A4Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.A4Desc"/></tags:listCell>
	<tags:listCell><tags:createField property="a4Sub" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="a4Not" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="a4Comm" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.A5Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.A5Desc"/></tags:listCell>
	<tags:listCell><b><tags:outputText textKey="udsformchecklist2.Required"/></b></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B1Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B1Desc"/></tags:listCell>
	<tags:listCell><tags:createField property="b1Sub" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b1Not" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b1Comm" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B2Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B2Desc"/></tags:listCell>
	<tags:listCell><tags:createField property="b2Sub" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b2Not" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b2Comm" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B3Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B3Desc"/></tags:listCell>
	<tags:listCell><tags:createField property="b3Sub" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b3Not" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b3Comm" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B4Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B4Desc"/></tags:listCell>
	<tags:listCell><b><tags:outputText textKey="udsformchecklist2.Required"/></b></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B5Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B5Desc"/></tags:listCell>
	<tags:listCell><tags:createField property="b5Sub" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b5Not" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b5Comm" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B6Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B6Desc"/></tags:listCell>
	<tags:listCell><tags:createField property="b6Sub" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b6Not" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b6Comm" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B7Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B7Desc"/></tags:listCell>
	<tags:listCell><tags:createField property="b7Sub" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b7Not" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b7Comm" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B8Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B8Desc"/></tags:listCell>
	<tags:listCell><tags:createField property="b8Sub" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b8Not" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell><tags:createField property="b8Comm" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B9Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.B9Desc"/></tags:listCell>
	<tags:listCell><b><tags:outputText textKey="udsformchecklist2.Required"/></b></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.C1Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.C1Desc"/></tags:listCell>
	<tags:listCell><b><tags:outputText textKey="udsformchecklist2.Required"/></b></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.D1Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.D1Desc"/></tags:listCell>
	<tags:listCell><b><tags:outputText textKey="udsformchecklist2.Required"/></b></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.E1Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.E1Desc"/></tags:listCell>
	<tags:listCell><b><tags:outputText textKey="udsformchecklist2.Required"/></b></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.T1Form"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.T1Desc"/></tags:listCell>
	<tags:listCell><b><tags:outputText textKey="udsformchecklist2.Required"/></b></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
	<tags:listCell><tags:outputText textKey="udsformchecklist2.NA"/></tags:listCell>
</tags:listRow>

</tags:tableForm>

</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
	<page:param name="sectionId">anonymous</page:param>
	<page:param name="view">${componentView}</page:param>
	<page:param name="instructions"> </page:param>
	<tags:createField property="notes['udsformchecklist']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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
  <ui:observe elementIds="packet" component="instrument" forValue="T" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="a2Sub,b1Sub,b2Sub,b3Sub,b6Sub,b8Sub" component="${componentPrefix}"/>
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:observe elementIds="a2Sub" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="a2Not,a2Comm" component="${componentPrefix}"/>
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="a3Sub" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="a3Not,a3Comm" component="${componentPrefix}"/>
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="a4Sub" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="a4Not,a4Comm" component="${componentPrefix}"/>
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:observe elementIds="b1Sub" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="b1Not,b1Comm" component="${componentPrefix}"/>
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:observe elementIds="b2Sub" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="b2Not,b2Comm" component="${componentPrefix}"/>
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:observe elementIds="b3Sub" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="b3Not,b3Comm" component="${componentPrefix}"/>
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="b5Sub" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="b5Not,b5Comm" component="${componentPrefix}"/>
</ui:formGuide>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:observe elementIds="b6Sub" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="b6Not,b6Comm" component="${componentPrefix}"/>
</ui:formGuide>
<ui:formGuide>
  <ui:observe elementIds="b7Sub" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="b7Not,b7Comm" component="${componentPrefix}"/>
</ui:formGuide>
<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:observe elementIds="b8Sub" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="b8Not,b8Comm" component="${componentPrefix}"/>
</ui:formGuide>


  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>