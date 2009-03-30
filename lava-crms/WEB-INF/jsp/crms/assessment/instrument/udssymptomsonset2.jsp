<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udssymptomsonset2"/>
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

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="memComp" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssymptomsonset2.memComp.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"><spring:message code="udssymptomsonset2.memComp.instructions"/></page:param>
<tags:createField property="decSub" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="decIn" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="decClin" entity="${instrTypeEncoded}" component="${component}" optionsAlignment="groupTopVertical"/>
<tags:createField property="decAge" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="cogSymp" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssymptomsonset2.cogSymp.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"><spring:message code="udssymptomsonset2.cogSymp.instructions"/></page:param>
<tags:createField property="cogMem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogJudg" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogLang" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogVis" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogAttn" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogFluc" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOther" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogOthrx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogFrst" entity="${instrTypeEncoded}" component="${component}" optionsAlignment="groupTopVertical"/>
<tags:createField property="cogFrstx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogMode" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="cogModex" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="behSymp" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssymptomsonset2.behSymp.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"><spring:message code="udssymptomsonset2.behSymp.instructions"/></page:param>
<tags:createField property="beApathy" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="bdDep" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beVHall" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beVWell" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beAHall" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beDel" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beDisin" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beIrrit" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beAgit" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="bePerCh" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beRem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beOthr" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beOthrx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beFrst" entity="${instrTypeEncoded}" component="${component}" optionsAlignment="groupTopVertical"/>
<tags:createField property="beFrstx" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beMode" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="beModex" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="motSymp" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssymptomsonset2.motSymp.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"><spring:message code="udssymptomsonset2.motSymp.instructions"/></page:param>
<tags:createField property="moGait" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moFalls" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moTrem" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moSlow" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moFrst" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moMode" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moModex" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="moMoPark" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="overall" linkTextKey="top.quicklink"/>
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udssymptomsonset2.overall.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="course" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="frstChg" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
	<page:param name="sectionId">anonymous</page:param>
	<page:param name="view">${componentView}</page:param>
	<page:param name="instructions"> </page:param>
	<tags:createField property="notes['udssymptomsonset']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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
  <ui:observe elementIds="decClin" component="${componentPrefix}" forValue="^1" negate="true" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:skip elementIds="decAge,cogMem,cogJudg,cogLang,cogVis,cogAttn,cogFluc,cogOther,cogFrst,cogMode,
  	beApathy,bdDep,beVHall,beVWell,beAHall,beDel,beDisin,beIrrit,beAgit,bePerCh,beRem,beOthr,beFrst,beMode,
  	moGait,moFalls,moTrem,moSlow,moFrst,moMode,moMoPark,course,frstChg" component="${componentPrefix}"
  	comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>          



<ui:formGuide>
  <ui:observe elementIds="beVHall" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="beVWell" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
</ui:formGuide>       
  
<ui:formGuide>
  <ui:observe elementIds="cogOther" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogOthrx" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:observe elementIds="cogFrst" component="${componentPrefix}" forValue="^6" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogFrstx" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:observe elementIds="cogMode" component="${componentPrefix}" forValue="^4" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="cogModex" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
   <ui:observe elementIds="beOthr" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="beOthrx" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:observe elementIds="beFrst" component="${componentPrefix}" forValue="^8$" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="beFrstx" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide>
  <ui:observe elementIds="beMode" component="${componentPrefix}" forValue="^4" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="beModex" component="${componentPrefix}"/>
</ui:formGuide>           
<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:observe elementIds="moMode" component="${componentPrefix}" forValue="^4" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
  <ui:unskip elementIds="moModex" component="${componentPrefix}"/>
</ui:formGuide>

  </c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    
	    
