<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsfamilyhistory1"/>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="a3chg"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Family History</page:param>
  <page:param name="quicklinks">udsa315,udsa368</page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
	<c:param name="followUpCheckbox" value="true"/>
</c:import>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="udsa315" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsfamilyhistory1.1-5.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="a3Chg" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="parChg" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputBlankLines n="1"/>
<tags:createField property="momDem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="momOnset" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="momAge" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="momDAge" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputBlankLines n="1"/>
<tags:createField property="dadDem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="dadOnset" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="dadAge" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="dadDAge" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputBlankLines n="1"/>
<tags:createField property="sibChg" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="twin" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="twinType" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputBlankLines n="1"/>
<tags:createField property="sibs" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="sibsDem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.siblings.sibling1" inline="false"/>
<tags:createField property="sib1Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="sib1Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.siblings.sibling2" inline="false"/>
<tags:createField property="sib2Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="sib2Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.siblings.sibling3" inline="false"/>
<tags:createField property="sib3Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="sib3Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.siblings.sibling4" inline="false"/>
<tags:createField property="sib4Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="sib4Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.siblings.sibling5" inline="false"/>
<tags:createField property="sib5Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="sib5Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.siblings.sibling6" inline="false"/>
<tags:createField property="sib6Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="sib6Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="notes['udsa315']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="udsa368" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsfamilyhistory1.6-8.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="kidChg" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="kids" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="kidsDem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.children.child1" inline="false"/>
<tags:createField property="kids1Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="kids1Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.children.child2" inline="false"/>
<tags:createField property="kids2Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="kids2Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.children.child3" inline="false"/>
<tags:createField property="kids3Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="kids3Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.children.child4" inline="false"/>
<tags:createField property="kids4Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="kids4Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.children.child5" inline="false"/>
<tags:createField property="kids5Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="kids5Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.children.child6" inline="false"/>
<tags:createField property="kids6Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="kids6Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputBlankLines n="2"/>
<tags:createField property="relChg" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="relsDem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.relatives.rel1" inline="false"/>
<tags:createField property="rel1Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rel1Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.relatives.rel2" inline="false"/>
<tags:createField property="rel2Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rel2Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.relatives.rel3" inline="false"/>
<tags:createField property="rel3Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rel3Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.relatives.rel4" inline="false"/>
<tags:createField property="rel4Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rel4Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.relatives.rel5" inline="false"/>
<tags:createField property="rel5Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rel5Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:outputText textKey="udsfamilyhistory1.relatives.rel6" inline="false"/>
<tags:createField property="rel6Ons" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="rel6Age" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="notes['udsa368']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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

<%-- when packet is unchecked --%>
<ui:formGuide>
  <ui:observe elementIds="packet" component="instrument" forValue="^I"/>
  <ui:skip elementIds="a3Chg,parChg,sibChg,kidChg,relChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="momDem,dadDem,twin,twinType"
   component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>	
  <ui:unskip elementIds="momOnset,momAge,momDAge,dadOnset,dadAge,dadDAge,
  	sibs,sibsDem,sib1Ons,sib1Age,sib2Ons,sib2Age,sib3Ons,sib3Age,sib4Ons,sib4Age,sib5Ons,sib5Age,sib6Ons,sib6Age,
  	kids,kidsDem,kids1Ons,kids1Age,kids2Ons,kids2Age,kids3Ons,kids3Age,kids4Ons,kids4Age,kids5Ons,kids5Age,kids6Ons,kids6Age,
  	relsDem,rel1Ons,rel1Age,rel2Ons,rel2Age,rel3Ons,rel3Age,rel4Ons,rel4Age,rel5Ons,rel5Age,rel6Ons,rel6Age" 
   component="${componentPrefix}"/>	
</ui:formGuide>           

<%-- when packet is follow up--%>
<ui:formGuide>
  <ui:observe elementIds="packet" component="instrument" forValue="[F|T]"/>
  <ui:unskip elementIds="a3Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           
    
<%-- all *Chg properties can not be changed when packet is initial --%>    
    
<%-- when a3Chg is anything but Yes (1) (ignoreUndo true so nothing done when is Yes) --%>
<ui:formGuide ignoreUndoOnLoad="true" ignoreUndo="true">
  <ui:ignore elementIds="packet" component="instrument" forValue="I|^$"/>
  <ui:observe elementIds="a3Chg" component="${componentPrefix}" forValue="^1" negate="true" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="parChg,momDem,dadDem,twin,twinType"
   component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>	
  <ui:skip elementIds="momOnset,momAge,momDAge,dadOnset,dadAge,dadDAge,
  	sibChg,sibs,sibsDem,sib1Ons,sib1Age,sib2Ons,sib2Age,sib3Ons,sib3Age,sib4Ons,sib4Age,sib5Ons,sib5Age,sib6Ons,sib6Age,
  	kidChg,kids,kidsDem,kids1Ons,kids1Age,kids2Ons,kids2Age,kids3Ons,kids3Age,kids4Ons,kids4Age,kids5Ons,kids5Age,kids6Ons,kids6Age,
  	relChg,relsDem,rel1Ons,rel1Age,rel2Ons,rel2Age,rel3Ons,rel3Age,rel4Ons,rel4Age,rel5Ons,rel5Age,rel6Ons,rel6Age" 
   component="${componentPrefix}"/>	
</ui:formGuide>           

<%-- when a3Chg is Yes=1 (ignoreUndo true so nothing done when anything but Yes) --%>
<ui:formGuide ignoreUndoOnLoad="true" ignoreUndo="true">
  <ui:ignore elementIds="packet" component="instrument" forValue="I|^$"/>
  <ui:observe elementIds="a3Chg" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="parChg,sibChg,kidChg,relChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>	
</ui:formGuide>           

<%-- when parChg is Yes=1, or not (i.e. this tag handles both situations since the actions are symmetrical --%>
<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="I|^$"/>
  <ui:depends elementIds="a3Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="parChg" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="momDem,dadDem" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="momOnset,momAge,momDAge,dadOnset,dadAge,dadDAge" component="${componentPrefix}"/>
</ui:formGuide>           

<%-- all non *Chg properties should have dependencies on any properties which when observed can change them, e.g.
 in the case of momDem, the properties followUp, a3Chg, parChg can change its value based on the tags above, so 
 they all appear in depends tags --%>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,parChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="momDem" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="momOnset,momAge,momDAge" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,parChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="dadDem" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="dadOnset,dadAge,dadDAge" component="${componentPrefix}"/>
</ui:formGuide>           

<ui:formGuide>
  <ui:depends elementIds="a3Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:ignore elementIds="packet" component="instrument" forValue="I|^$"/>
  <ui:observe elementIds="sibChg" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="twin,twinType" 
  	component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="sibs,sibsDem,sib1Ons,sib1Age,sib2Ons,sib2Age,sib3Ons,sib3Age,sib4Ons,sib4Age,sib5Ons,sib5Age,sib6Ons,sib6Age" 
  	component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="twin" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="twinType" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="sibs" component="${componentPrefix}" forValue="^[1-9]$|^1[0-9]|^20"/>
  <ui:unskip elementIds="sibsDem" component="${componentPrefix}"/>
</ui:formGuide>           
<%-- alternatively, instead of the ignore/negate below, could have set ignoreUndo and ignoreUndoOnLoad true on formGuide tag --%>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="sibsDem" component="${componentPrefix}" forValue="^$|-.+|^0$|88|99"/>
  <ui:ignore elementIds="sibsDem" component="${componentPrefix}" forValue="^$|-.+|^0$|88|99" negate="true"/>
  <ui:skip elementIds="sib1Ons,sib1Age,sib2Ons,sib2Age,sib3Ons,sib3Age,sib4Ons,sib4Age,sib5Ons,sib5Age,sib6Ons,sib6Age" 
  	skipValue="888" skipOptionText="888" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="sibsDem" component="${componentPrefix}" forValue="^1"/>
  <ui:ignore elementIds="sibsDem" component="${componentPrefix}" forValue="^1" negate="true"/>
  <ui:unskip elementIds="sib1Ons,sib1Age" component="${componentPrefix}"/>
  <ui:skip elementIds="sib2Ons,sib2Age,sib3Ons,sib3Age,sib4Ons,sib4Age,sib5Ons,sib5Age,sib6Ons,sib6Age"
  	component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="sibsDem" component="${componentPrefix}" forValue="^2"/>
  <ui:ignore elementIds="sibsDem" component="${componentPrefix}" forValue="^2" negate="true"/>
  <ui:unskip elementIds="sib1Ons,sib1Age,sib2Ons,sib2Age" component="${componentPrefix}"/>
  <ui:skip elementIds="sib3Ons,sib3Age,sib4Ons,sib4Age,sib5Ons,sib5Age,sib6Ons,sib6Age"
  	component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="sibsDem" component="${componentPrefix}" forValue="^3"/>
  <ui:ignore elementIds="sibsDem" component="${componentPrefix}" forValue="^3" negate="true"/>
  <ui:unskip elementIds="sib1Ons,sib1Age,sib2Ons,sib2Age,sib3Ons,sib3Age" component="${componentPrefix}"/>
  <ui:skip elementIds="sib4Ons,sib4Age,sib5Ons,sib5Age,sib6Ons,sib6Age" component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="sibsDem" component="${componentPrefix}" forValue="^4"/>
  <ui:ignore elementIds="sibsDem" component="${componentPrefix}" forValue="^4" negate="true"/>
  <ui:unskip elementIds="sib1Ons,sib1Age,sib2Ons,sib2Age,sib3Ons,sib3Age,sib4Ons,sib4Age" component="${componentPrefix}"/>
  <ui:skip elementIds="sib5Ons,sib5Age,sib6Ons,sib6Age" skipValue="888" skipOptionText="888" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="sibsDem" component="${componentPrefix}" forValue="^5"/>
  <ui:ignore elementIds="sibsDem" component="${componentPrefix}" forValue="^5" negate="true"/>
  <ui:unskip elementIds="sib1Ons,sib1Age,sib2Ons,sib2Age,sib3Ons,sib3Age,sib4Ons,sib4Age,sib5Ons,sib5Age" component="${componentPrefix}"/>
  <ui:skip elementIds="sib6Ons,sib6Age" component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="sibsDem" component="${componentPrefix}" forValue="^[6-9]$|^1[0-9]|20"/>
  <ui:ignore elementIds="sibsDem" component="${componentPrefix}" forValue="^[6-9]$|^1[0-9]|20" negate="true"/>
 <ui:unskip elementIds="sib1Ons,sib1Age,sib2Ons,sib2Age,sib3Ons,sib3Age,sib4Ons,sib4Age,sib5Ons,sib5Age,sib6Ons,sib6Age" component="${componentPrefix}"/>
</ui:formGuide>           

<ui:formGuide>
  <ui:ignore elementIds="packet" component="instrument" forValue="I|^$"/>
  <ui:depends elementIds="a3Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="kidChg" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="kids,kidsDem,kids1Ons,kids1Age,kids2Ons,kids2Age,kids3Ons,kids3Age,kids4Ons,kids4Age,kids5Ons,kids5Age,kids6Ons,kids6Age" 
  	component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="kids" component="${componentPrefix}" forValue="^[1-9]$|^1[0-9]|^20"/>
  <ui:unskip elementIds="kidsDem" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="kidsDem" component="${componentPrefix}" forValue="^$|-.+|^0$|88|99"/>
  <ui:ignore elementIds="kidsDem" component="${componentPrefix}" forValue="^$|-.+|^0$|88|99" negate="true"/>
  <ui:skip elementIds="kids1Ons,kids1Age,kids2Ons,kids2Age,kids3Ons,kids3Age,kids4Ons,kids4Age,kids5Ons,kids5Age,kids6Ons,kids6Age" 
  	skipValue="888" skipOptionText="888" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide> 
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="kidsDem" component="${componentPrefix}" forValue="^1"/>
  <ui:ignore elementIds="kidsDem" component="${componentPrefix}" forValue="^1" negate="true"/>
  <ui:unskip elementIds="kids1Ons,kids1Age" component="${componentPrefix}"/>
  <ui:skip elementIds="kids2Ons,kids2Age,kids3Ons,kids3Age,kids4Ons,kids4Age,kids5Ons,kids5Age,kids6Ons,kids6Age" 
  	component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="kidsDem" component="${componentPrefix}" forValue="^2"/>
  <ui:ignore elementIds="kidsDem" component="${componentPrefix}" forValue="^2" negate="true"/>
  <ui:unskip elementIds="kids1Ons,kids1Age,kids2Ons,kids2Age" component="${componentPrefix}"/>
  <ui:skip elementIds="kids3Ons,kids3Age,kids4Ons,kids4Age,kids5Ons,kids5Age,kids6Ons,sib6Age" 
  	component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="kidsDem" component="${componentPrefix}" forValue="^3"/>
  <ui:ignore elementIds="kidsDem" component="${componentPrefix}" forValue="^3" negate="true"/>
  <ui:unskip elementIds="kids1Ons,kids1Age,kids2Ons,kids2Age,kids3Ons,kids3Age" component="${componentPrefix}"/>
  <ui:skip elementIds="kids4Ons,kids4Age,kids5Ons,kids5Age,kids6Ons,sib6Age" component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="kidsDem" component="${componentPrefix}" forValue="^4"/>
  <ui:ignore elementIds="kidsDem" component="${componentPrefix}" forValue="^4" negate="true"/>
  <ui:unskip elementIds="kids1Ons,kids1Age,kids2Ons,kids2Age,kids3Ons,kids3Age,kids4Ons,kids4Age" component="${componentPrefix}"/>
  <ui:skip elementIds="kids5Ons,kids5Age,kids6Ons,kids6Age" component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="kidsDem" component="${componentPrefix}" forValue="^5"/>
  <ui:ignore elementIds="kidsDem" component="${componentPrefix}" forValue="^5" negate="true"/>
  <ui:unskip elementIds="kids1Ons,kids1Age,kids2Ons,kids2Age,kids3Ons,kids3Age,kids4Ons,kids4Age,kids5Ons,kids5Age" component="${componentPrefix}"/>
  <ui:skip elementIds="kids6Ons,kids6Age" component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="kidsDem" component="${componentPrefix}" forValue="^[6-9]$|^1[0-9]|20"/>
  <ui:ignore elementIds="kidsDem" component="${componentPrefix}" forValue="^[6-9]$|^1[0-9]|20" negate="true"/>
  <ui:unskip elementIds="kids1Ons,kids1Age,kids2Ons,kids2Age,kids3Ons,kids3Age,kids4Ons,kids4Age,kids5Ons,kids5Age,kids6Age,kids6Ons" 
  	component="${componentPrefix}"/>
</ui:formGuide>           

<ui:formGuide>
  <ui:depends elementIds="a3Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:ignore elementIds="packet" component="instrument" forValue="I|^$"/>
  <ui:observe elementIds="relChg" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="relsDem,rel1Ons,rel1Age,rel2Ons,rel2Age,rel3Ons,rel3Age,rel4Ons,rel4Age,rel5Ons,rel5Age,rel6Ons,rel6Age" 
  	component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,relChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="relsDem" component="${componentPrefix}" forValue="^$|-.+|^0$|88|99"/>
  <ui:ignore elementIds="relsDem" component="${componentPrefix}" forValue="^$|-.+|^0$|88|99" negate="true"/>
  <ui:skip elementIds="rel1Ons,rel1Age,rel2Ons,rel2Age,rel3Ons,rel3Age,rel4Ons,rel4Age,rel5Ons,rel5Age,rel6Ons,rel6Age" 
  	skipValue="888" skipOptionText="888" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,relChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="relsDem" component="${componentPrefix}" forValue="^1"/>
  <ui:ignore elementIds="relsDem" component="${componentPrefix}" forValue="^1" negate="true"/>
  <ui:unskip elementIds="rel1Ons,rel1Age" component="${componentPrefix}"/>
  <ui:skip elementIds="rel2Ons,rel2Age,rel3Ons,rel3Age,rel4Ons,rel4Age,rel5Ons,rel5Age,rel6Ons,rel6Age" 
  	component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,relChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="relsDem" component="${componentPrefix}" forValue="^2"/>
  <ui:ignore elementIds="relsDem" component="${componentPrefix}" forValue="^2" negate="true"/>
  <ui:unskip elementIds="rel1Ons,rel1Age,rel2Ons,rel2Age" component="${componentPrefix}"/>
  <ui:skip elementIds="rel3Ons,rel3Age,rel4Ons,rel4Age,rel5Ons,rel5Age,rel6Ons,rel6Age" 
  	component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,relChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="relsDem" component="${componentPrefix}" forValue="^3"/>
  <ui:ignore elementIds="relsDem" component="${componentPrefix}" forValue="^3" negate="true"/>
  <ui:unskip elementIds="rel1Ons,rel1Age,rel2Ons,rel2Age,rel3Ons,rel3Age" component="${componentPrefix}"/>
  <ui:skip elementIds="rel4Ons,rel4Age,rel5Ons,rel5Age,rel6Ons,rel6Age" component="${componentPrefix}" 
  	skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,relChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="relsDem" component="${componentPrefix}" forValue="^4"/>
  <ui:ignore elementIds="relsDem" component="${componentPrefix}" forValue="^4" negate="true"/>
  <ui:unskip elementIds="rel1Ons,rel1Age,rel2Ons,rel2Age,rel3Ons,rel3Age,rel4Ons,rel4Age" component="${componentPrefix}"/>
  <ui:skip elementIds="rel5Ons,rel5Age,rel6Ons,rel6Age" component="${componentPrefix}" 
  	skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,relChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="relsDem" component="${componentPrefix}" forValue="^5"/>
  <ui:ignore elementIds="relsDem" component="${componentPrefix}" forValue="^5" negate="true"/>
  <ui:unskip elementIds="rel1Ons,rel1Age,rel2Ons,rel2Age,rel3Ons,rel3Age,rel4Ons,rel4Age,rel5Ons,rel5Age" component="${componentPrefix}"/>
  <ui:skip elementIds="rel6Ons,rel6Age" component="${componentPrefix}" skipValue="888" skipOptionText="888"/>
</ui:formGuide>           
<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="a3Chg,relChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="relsDem" component="${componentPrefix}" forValue="^[6-9]$|^1[0-9]|20"/>
  <ui:ignore elementIds="relsDem" component="${componentPrefix}" forValue="^[6-9]$|^1[0-9]|20" negate="true"/>
  <ui:unskip elementIds="rel1Ons,rel1Age,rel2Ons,rel2Age,rel3Ons,rel3Age,rel4Ons,rel4Age,rel5Ons,rel5Age,rel6Age,rel6Ons" 
  	component="${componentPrefix}"/>
</ui:formGuide>           

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    
	    
