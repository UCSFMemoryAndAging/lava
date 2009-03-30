<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="naccpathology9"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>



<c:set var="controlMode" value="lv"/>
<c:set var="domainControlMode" value="vw"/>
<c:set var="labelStyle" value="tightRight"/>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'collect' || componentView == 'enter' || componentView == 'doubleEnter' || componentView == 'compare'}">

		<c:set var="focusField" value="npid"/>
		<c:set var="controlMode" value="le"/>
		<c:set var="labelStyle" value=""/>		
	</c:when>
</c:choose>	





<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">NACC Pathology (V9)</page:param>
  <page:param name="quicklinks">tracking,q7_19,q20,q21_24</page:param>

<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>


<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">tracking</page:param>
  <page:param name="sectionNameKey">naccpathology9.tracking.section</page:param>

<tags:contentColumn columnClass="colLeft2Col5050">

			<tags:createField property="ptid" component="${component}" entity="${instrTypeEncoded}" labelAlignment="right" labelStyle="${labelStyle}"/>
			<tags:createField property="npform" component="${component}" entity="${instrTypeEncoded}" labelAlignment="right" labelStyle="${labelStyle}"/>
			<tags:createField property="npid" component="${component}" entity="${instrTypeEncoded}" labelAlignment="right" labelStyle="${labelStyle}"/>
			<tags:createField property="npsex" component="${component}" entity="${instrTypeEncoded}" labelAlignment="right" labelStyle="${labelStyle}"/>
			<tags:createField property="npdage" component="${component}" entity="${instrTypeEncoded}" labelAlignment="right" labelStyle="${labelStyle}"/>
			<tags:createField property="npdod" component="${component}" entity="${instrTypeEncoded}" labelAlignment="right" labelStyle="${labelStyle}"/>
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050"> 

			<tags:createField property="submissionstatus" component="${component}" entity="${instrTypeEncoded}"  labelAlignment="top"/>
			<tags:createField property="subdate" component="${component}" entity="${instrTypeEncoded}" labelAlignment="top"/>
			<tags:createField property="statnote" component="${component}" entity="${instrTypeEncoded}" labelAlignment="top"/>
</tags:contentColumn>

</page:applyDecorator>



<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">q7_19</page:param>
  <page:param name="sectionNameKey">naccpathology9.q7_19.section</page:param>

			<tags:createField property="npgross" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:outputText textKey="naccpathology9.q8.label" inline="false"/>
			<tags:createField property="npnit" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npcerad" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npadrda" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npocrit" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npbraak" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npneur" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npdiff" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npvasc" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="nplinf" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npmicro" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="nplac" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="nphem" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npart" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npnec" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npscl" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npavas" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="nparter" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npamy" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npoang" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npvoth" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="nplewy" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="nplewycs" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:outputText textKey="naccpathology9.q14.label" inline="false"/>
			<tags:createField property="nppick" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npcort" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npprog" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npfront" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="nptau" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npftd" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npftdno" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npftdspc" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:outputText textKey="naccpathology9.q15.label" inline="false"/>
			<tags:createField property="npcj" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npprion" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:outputText textKey="naccpathology9.q16.label" inline="false"/>
			<tags:createField property="npmajor" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npmpath1" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npmpath2" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npmpath3" component="${component}" entity="${instrTypeEncoded}"/>
	
			<tags:outputText textKey="naccpathology9.q17.label" inline="false"/>
			<tags:createField property="npgene" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npfhspec" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:outputText textKey="naccpathology9.q18.label" inline="false"/>
			<tags:createField property="npapoe" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="nptauhap" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npprnp" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npchrom" component="${component}" entity="${instrTypeEncoded}"/>


</page:applyDecorator>



<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">q20</page:param>
  <page:param name="sectionNameKey">naccpathology9.q20.section</page:param>

<tags:outputText textKey="naccpathology9.q20.label" styleClass="bold" inline="false"/>
<tags:outputBlankLines n="1"/>


<tags:tableForm>

	<tags:listRow>
		<tags:listColumnHeader labelKey="naccpathology9.primaryHeader" width="20%"/>
		<tags:listColumnHeader labelKey="naccpathology9.contributingHeader" width="20%"/>
		<tags:listColumnHeader label="" width="60%"/>
	</tags:listRow>
	<tags:listRow>
		<tags:listCell><tags:createField property="nppnorm" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npcnorm" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:outputText textKey="naccpathology9.npcnorm.label" inline="false"/> </tags:listCell>
	</tags:listRow>
	<tags:listRow>
		<tags:listCell><tags:createField property="nppadp" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npcadp" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:outputText textKey="naccpathology9.npcadp.label" inline="false"/></tags:listCell>
	</tags:listRow>	
	<tags:listRow>
		<tags:listCell><tags:createField property="nppad" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npcad" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:outputText textKey="naccpathology9.npcad.label" inline="false"/></tags:listCell>
	</tags:listRow>	
	<tags:listRow>
		<tags:listCell><tags:createField property="npplewy" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npclewy" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:outputText textKey="naccpathology9.npclewy.label" inline="false"/></tags:listCell>
	</tags:listRow>		
	<tags:listRow>
		<tags:listCell><tags:createField property="nppvasc" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npcvasc" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:outputText textKey="naccpathology9.npcvasc.label" inline="false"/></tags:listCell>
	</tags:listRow>	
	<tags:listRow>
		<tags:listCell><tags:createField property="nppftld" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npcftld" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:outputText textKey="naccpathology9.npcftld.label" inline="false"/></tags:listCell>
	</tags:listRow>		
	<tags:listRow>
		<tags:listCell><tags:createField property="npphipp" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npchipp" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:outputText textKey="naccpathology9.npchipp.label" inline="false"/></tags:listCell>
	</tags:listRow>		
	<tags:listRow>
		<tags:listCell><tags:createField property="nppprion" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npcprion" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:outputText textKey="naccpathology9.npcprion.label" inline="false"/></tags:listCell>
	</tags:listRow>	
	
	<tags:listRow>
		<tags:listCell><tags:createField property="nppoth1" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npcoth1" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell>
			<tags:outputText textKey="naccpathology9.npcoth1.label" />
			<tags:createField property="npoth1x" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/>
		</tags:listCell>
	</tags:listRow>	
		<tags:listRow>
		<tags:listCell><tags:createField property="nppoth2" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npcoth2" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell>
			<tags:outputText textKey="naccpathology9.npcoth2.label" />
			<tags:createField property="npoth2x" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/>
		</tags:listCell>
	</tags:listRow>	
		<tags:listRow>
		<tags:listCell><tags:createField property="nppoth3" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell><tags:createField property="npcoth3" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/></tags:listCell>
		<tags:listCell>
			<tags:outputText textKey="naccpathology9.npcoth3.label"/>
			<tags:createField property="npoth3x" component="${component}" entity="${instrTypeEncoded}" mode="${controlMode}"/>
		</tags:listCell>
	</tags:listRow>	
	</tags:tableForm>
		
</page:applyDecorator>



<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">q21_24</page:param>
  <page:param name="sectionNameKey">naccpathology9.q21_24.section</page:param>

			<tags:createField property="npbrfrzn" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npbrfrm" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npbrparf" component="${component}" entity="${instrTypeEncoded}"/>
			<tags:createField property="npcsfant" component="${component}" entity="${instrTypeEncoded}"/>

</page:applyDecorator>



<c:if test="${componentMode != 'vw'}">
<c:forEach begin="0" end="1" var="current">
  <c:choose>
    <c:when test="${componentView == 'doubleEnter' || (componentView == 'compare' && current == 1)}">
      <c:set var="component" value="compareInstrument"/>
    </c:when>
    <c:otherwise>
      <c:set var="component" value="instrument"/>
    </c:otherwise>
  </c:choose>
  <c:if test="${current == 0 || (current == 1 && componentView == 'compare')}">


<ui:formGuide >
    <ui:observe elementIds="npgene" component="${component}" forValue="^2|^4" />
    <ui:unskip elementIds="npfhspec" component="${component}"/>
</ui:formGuide>

<ui:formGuide >
    <ui:observe elementIds="npmajor" component="${component}" forValue="^1" />
    <ui:unskip elementIds="npmpath1,npmpath2,npmpath3" component="${component}"/>
</ui:formGuide>


<ui:formGuide observeAndOr="Or">
    <ui:observe elementIds="nppoth1" component="${component}" forValue="^1" />
    <ui:observe elementIds="npcoth1" component="${component}" forValue="^1" />
    <ui:unskip elementIds="npoth1x" component="${component}"/>
</ui:formGuide>


<ui:formGuide observeAndOr="Or">
    <ui:observe elementIds="nppoth2" component="${component}" forValue="^1" />
    <ui:observe elementIds="npcoth2" component="${component}" forValue="^1" />
    <ui:unskip elementIds="npoth2x" component="${component}"/>
</ui:formGuide>

<ui:formGuide observeAndOr="Or" simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
    <ui:observe elementIds="nppoth3" component="${component}" forValue="^1" />
    <ui:observe elementIds="npcoth3" component="${component}" forValue="^1" />
    <ui:unskip elementIds="npoth3x" component="${component}"/>
</ui:formGuide>



</c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    

