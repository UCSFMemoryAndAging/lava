<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udssymptomsonset1"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="decSub"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Symptoms Onset</page:param>
  <page:param name="quicklinks">memComp,cogSymp,behSymp,motSymp,overall</page:param>
   
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
  <page:param name="section"><spring:message code="udssymptomsonset1.meaningfulChanges.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="b9Chg" entity="${instrTypeEncoded}" component="${component}" optionsAlignment="groupTopVertical"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="memComp" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssymptomsonset1.memComp.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="decSub" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="decIn" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="decClin" entity="${instrTypeEncoded}" component="${component}" optionsAlignment="groupTopVertical"/>
<tags:createField property="decAge" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="notes['memComp']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="cogSymp" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssymptomsonset1.cogSymp.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="cogMem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogJudg" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogLang" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogVis" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogAttn" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOther" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOthrx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogFrst" entity="${instrTypeEncoded}" component="${component}" optionsAlignment="groupTopVertical"/>
<tags:createField property="cogFrstx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogMode" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogModex" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="notes['cogSymp']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="behSymp" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssymptomsonset1.behSymp.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="beApathy" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="bdDep" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beVHall" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beAHall" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beDel" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beDisin" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beIrrit" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beAgit" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="bePerCh" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beOthr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beOthrx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beFrst" entity="${instrTypeEncoded}" component="${component}" optionsAlignment="groupTopVertical"/>
<tags:createField property="beFrstx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beMode" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beModex" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="notes['behSymp']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="motSymp" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssymptomsonset1.motSymp.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="moGait" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moFalls" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moTrem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moSlow" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moFrst" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moMode" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moModex" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="notes['motSymp']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="overall" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssymptomsonset1.overall.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="course" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="frstChg" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="notes['overall']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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

<%-- when packet is initial --%>
<ui:formGuide ignoreUndo="true">
  <ui:observe elementIds="packet" component="instrument" forValue="[I|^$]"/>
  <ui:skip elementIds="b9Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="decSub,decIn,decClin,cogMem,cogJudg,cogLang,cogVis,cogAttn,cogOther,cogFrst,cogMode,
  	beApathy,bdDep,beVHall,beAHall,beDel,beDisin,beIrrit,beAgit,bePerCh,beOthr,beFrst,beMode,
  	moGait,moFalls,moTrem,moSlow,moFrst,moMode,course,frstChg"
   component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>	
  <ui:unskip elementIds="decAge" component="${componentPrefix}"/>	
</ui:formGuide>           

<%-- when packet is follow up type --%>
<ui:formGuide ignoreUndo="true">
  <ui:observe elementIds="packet" component="instrument" forValue="[T|F]"/>
  <ui:unskip elementIds="b9Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           
    

    
<%-- when b9Chg is 1, blank or a skip code (ignoreUndo true so nothing done when not 1) --%>
<ui:formGuide ignoreUndoOnLoad="true" ignoreUndo="true">
  <ui:ignore elementIds="packet" component="instrument" forValue="[I|^$]"/>
  <ui:observe elementIds="b9Chg" component="${componentPrefix}" forValue="^1|^$|-[0-9]" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="decSub,decIn,decClin,cogMem,cogJudg,cogLang,cogVis,cogAttn,cogOther,cogFrst,cogMode,
  	beApathy,bdDep,beVHall,beAHall,beDel,beDisin,beIrrit,beAgit,bePerCh,beOthr,beFrst,beMode,
  	moGait,moFalls,moTrem,moSlow,moFrst,moMode,course,frstChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="decAge" component="${componentPrefix}"/>
</ui:formGuide>           

<%-- when b9Chg is 2 (ignoreUndo true so nothing done when not 2) --%>
<ui:formGuide ignoreUndoOnLoad="true" ignoreUndo="true">
  <ui:ignore elementIds="packet" component="instrument" forValue="[I|^$]"/>
  <ui:observe elementIds="b9Chg" component="${componentPrefix}" forValue="^2" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="decSub,decIn,decClin" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           


<%-- when b9Chg is 2 (ignoreUndo true so nothing done when not 2) --%>
<ui:formGuide ignoreUndoOnLoad="true" ignoreUndo="true">
  <ui:ignore elementIds="packet" component="instrument" forValue="[I|^$]"/>
  <ui:observe elementIds="b9Chg" component="${componentPrefix}" forValue="^2" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="decClin" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="decSub,decIn,decClin,cogMem,cogJudg,cogLang,cogVis,cogAttn,cogOther,cogFrst,cogMode,
  	beApathy,bdDep,beVHall,beAHall,beDel,beDisin,beIrrit,beAgit,bePerCh,beOthr,beFrst,beMode,
  	moGait,moFalls,moTrem,moSlow,moFrst,moMode,course,frstChg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           

<ui:formGuide >
  <depends elementsIds="packet" component="instrument"/>
  <ui:observe elementIds="decClin" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="decAge" component="${componentPrefix}"/>
</ui:formGuide>           


<%-- when b9Chg is 3 (ignoreUndo true so nothing done when not 3) --%>
<ui:formGuide ignoreUndoOnLoad="true" ignoreUndo="true">
  <ui:ignore elementIds="packet" component="instrument" forValue="[I|^$]"/>
  <ui:observe elementIds="b9Chg" component="${componentPrefix}" forValue="^3" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="decSub,decIn,decClin,cogFrst,cogMode,beFrst,beMode,moFrst,moMode,course,frstChg"
	  component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="decAge" component="${componentPrefix}"/>
  <ui:unskip elementIds="cogMem,cogJudg,cogLang,cogVis,cogAttn,cogOther,beApathy,bdDep,beVHall,beAHall,beDel,beDisin,beIrrit,beAgit,bePerCh,beOthr,
  	moGait,moFalls,moTrem,moSlow" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>           

<%-- when decClin is No or anything but No (symmetrical actions so can handle both situations with one tag) 
      decClin can not be changed when b9Chg is 1 or 3, so ignore when b9Chg is 1 or 3 --%>
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:ignore elementIds="b9Chg" component="${componentPrefix}" forValue="^2|-6" negate="true" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="decClin" component="${componentPrefix}" forValue="^1" negate="true" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="cogMem,cogJudg,cogLang,cogVis,cogAttn,cogOther,cogFrst,cogMode,
  	beApathy,bdDep,beVHall,beAHall,beDel,beDisin,beIrrit,beAgit,bePerCh,beOthr,beFrst,beMode,
  	moGait,moFalls,moTrem,moSlow,moFrst,moMode,course,frstChg" component="${componentPrefix}"
  	comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>          


<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="b9Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="cogOther" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogOthrx" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="b9Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="cogFrst" component="${componentPrefix}" forValue="^6" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogFrstx" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="b9Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="cogMode" component="${componentPrefix}" forValue="^4" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogModex" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="b9Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="beOthr" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="beOthrx" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="b9Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="beFrst" component="${componentPrefix}" forValue="^8" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="beFrstx" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="b9Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="beMode" component="${componentPrefix}" forValue="^4" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="beModex" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:depends elementIds="packet" component="instrument"/>
  <ui:depends elementIds="b9Chg" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:observe elementIds="moMode" component="${componentPrefix}" forValue="^4" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="moModex" component="${componentPrefix}"/>
</ui:formGuide>           

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    
	    
