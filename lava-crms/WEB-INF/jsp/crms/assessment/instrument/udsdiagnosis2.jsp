<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsdiagnosis2"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="whoDidDx"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Diagnosis</page:param>
  <page:param name="quicklinks">udsd114,udsd1512,udsd11327</page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
</c:import>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="udsd114" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsdiagnosis2.1-4.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="whoDidDx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="normCog" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsdiagnosis2.normCog.instructions" inline="false" associated="true"/>
<tags:createField property="demented" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsdiagnosis2.noNormCog_noDemented.instructions" inline="false" hangingIndent="true"/>

<tags:tableForm>  
<tags:listRow>
<tags:listColumnHeader labelKey="udsdiagnosis2.noNormCog_noDemented.presentColHeader" width="58%"/>
<tags:listColumnHeader labelKey="udsdiagnosis2.noNormCog_noDemented.domainsColHeader" width="42%"/>
</tags:listRow>

<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="mciaMem" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell/>
</tags:listRow>

<tags:listRow>
	<tags:listCell rowspan="4" labelSize="wide"><tags:createField property="mciaPlus" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell labelSize="wide"><tags:createField property="mciaPLan" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="mciaPAtt" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="mciaPEx" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="mciaPVis" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>

<tags:listRow>
	<tags:listCell rowspan="4" labelSize="wide"><tags:createField property="mciNon1" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell labelSize="wide"><tags:createField property="mciN1Lan" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="mciN1Att" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="mciN1Ex" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="mciN1Vis" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>

<tags:listRow>
	<tags:listCell rowspan="4" labelSize="wide"><tags:createField property="mciNon2" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell labelSize="wide"><tags:createField property="mciN2Lan" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="mciN2Att" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="mciN2Ex" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>
<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="mciN2Vis" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
</tags:listRow>

<tags:listRow>
	<tags:listCell labelSize="wide"><tags:createField property="impNoMci" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
	<tags:listCell/>
</tags:listRow>

</tags:tableForm>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="udsd1512" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsdiagnosis2.5-12.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="probAd" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="probAdIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="possAd" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="possAdIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="dlb" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="dlbIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="vasc" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="vascIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="vascPs" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="vascPsIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="alcDem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="alcDemIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="demUn" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="demUnIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="ftd" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="ftdIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="ppAph" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="ppAphIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="pnAph" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="semDemAn" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="semDemAg" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="ppAOthr" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="udsd11327" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsdiagnosis2.13-27.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="psp" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="pspIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cort" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cortIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="hunt" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="huntIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="prion" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="prionIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="meds" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="medsIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="dysIll" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="dysIllIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="dep" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="depIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="othPsy" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="othPsyIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="downs" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="downsIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="park" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="parkIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="stroke" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="strokIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="hyceph" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="hycephIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="brnInj" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="brnInjIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="neop" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="neopIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOth" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOthIf" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOthx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOth2" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOth2If" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOth2x" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOth3" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOth3If" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOth3x" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
	<page:param name="sectionId">anonymous</page:param>
	<page:param name="view">${componentView}</page:param>
	<page:param name="instructions"> </page:param>
	<tags:createField property="notes['udsdiagnosis']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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
  <ui:observe elementIds="normCog" component="${componentPrefix}" forValue="^1" />
  <ui:skip elementIds="demented,mciaMem,mciaPlus,mciaPLan,mciaPAtt,mciaPEx,mciaPVis,mciNon1,mciN1Lan,mciN1Att,mciN1Ex,mciN1Vis,
  	mciNon2,mciN2Lan,mciN2Att,mciN2Ex,mciN2Vis,impNoMci,probAd,probAdIf,possAd,possAdIf,dlb,dlbIf,vasc,vascIf,vascPs,vascPsIf,alcDem,alcDemIf,
  	demUn,demUnIf,ftd,ftdIf,ppAph,ppAphIf,pnAph,semDemAn,semDemAg,ppAOthr" component="${componentPrefix}" 
  	comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="demented" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="mciaMem,mciaPlus,mciaPLan,mciaPAtt,mciaPEx,mciaPVis,mciNon1,mciN1Lan,mciN1Att,mciN1Ex,mciN1Vis,
  	mciNon2,mciN2Lan,mciN2Att,mciN2Ex,mciN2Vis,impNoMci" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           
<ui:formGuide>
  <%-- ignore if demented is anything other than No (0) --%>
  <ui:ignore elementIds="demented" component="${componentPrefix}" forValue="^0" negate="true" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="mciaPlus" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="mciaPLan,mciaPAtt,mciaPEx,mciaPVis" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="demented" component="${componentPrefix}" forValue="^0" negate="true" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="mciNon1" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="mciN1Lan,mciN1Att,mciN1Ex,mciN1Vis" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="demented" component="${componentPrefix}" forValue="^0" negate="true" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="mciNon2" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="mciN2Lan,mciN2Att,mciN2Ex,mciN2Vis" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="probAd" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="possAd" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="probAdIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:depends elementIds="probAd" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="possAd" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="possAdIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="dlb" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="dlbIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="vasc" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="vascIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="vasc" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="vascPs" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="vascIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:depends elementIds="vascPs" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="vascPs" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="vascPsIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      


<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="alcDem" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="alcDemIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="demUn" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="demUnIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="ftd" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="ftdIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:ignore elementIds="normCog" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="ppAph" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="ppAphIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="pnAph" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="semDemAn" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="semDemAg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="ppAOthr" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="psp" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="pspIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      

<ui:formGuide>
  <ui:observe elementIds="cort" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cortIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="hunt" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="huntIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="prion" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="prionIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="meds" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="medsIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="dysIll" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="dysIllIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="dep" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="depIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="othPsy" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="othPsyIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="downs" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="downsIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="park" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="parkIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="stroke" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="strokIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="hyceph" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="hycephIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="brnInj" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="brnInjIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="neop" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="neopIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>      
<ui:formGuide>
  <ui:observe elementIds="cogOth" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogOthIf" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogOthx" component="${componentPrefix}"/>
</ui:formGuide>    
<ui:formGuide>
  <ui:observe elementIds="cogOth2" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogOth2If" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogOth2x" component="${componentPrefix}"/>
</ui:formGuide>   
<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:observe elementIds="cogOth3" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogOth3If" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogOth3x" component="${componentPrefix}"/>
</ui:formGuide>     

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    
	    
