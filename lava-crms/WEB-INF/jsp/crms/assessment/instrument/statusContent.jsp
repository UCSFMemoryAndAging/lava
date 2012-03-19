<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:if test="${hasMissingOrIncompleteFields == 'true'}">
	<br/><div style="text-align:center;"><span class="errorListItem"><b>You have entered one or more fields as MISSING or INCOMPLETE.<br/>Please either click on the "Revise" button to complete the data, or set the "Collection Status" field below to "Incomplete" or "Complete - Partially"</b></span></div><br/>
</c:if>

<tags:contentColumn columnClass="colLeft2Col5050">

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="instrument.dc.section"/></page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="dcBy" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="dcDate" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="dcStatus" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="dcNotes" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument" labelAlignment="top"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="instrument.dv.section"/></page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="dvBy" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="dvDate" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="dvStatus" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="dvNotes" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument" labelAlignment="top"/>
</page:applyDecorator>

</tags:contentColumn>

<tags:contentColumn columnClass="colRight2Col5050">

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="instrument.de.section"/></page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="deBy" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="deDate" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="deStatus" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="deNotes" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument" labelAlignment="top"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="instrument.research.section"/></page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="researchStatus" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="qualityIssue" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="qualityIssue2" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="qualityIssue3" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument"/>
<tags:createField property="qualityNotes" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument" labelAlignment="top"/>
</page:applyDecorator>

</tags:contentColumn>

<!-- add UDS Status fields if needed-->
<c:if test="${fn:startsWith(param.instrTypeEncoded, 'uds')}">
	<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsStatus.jsp">
		<c:param name="instrTypeEncoded" value="${param.instrTypeEncoded}"/>
	</c:import>
</c:if>

<c:if test="${componentMode != 'vw'}">
<ui:formGuide>
    <ui:observe elementIds="instrument_qualityIssue" forValue=".+"/>
    <ui:removeOption elementIds="instrument_qualityIssue2"/>
</ui:formGuide>

<ui:formGuide observeAndOr="or">
    <ui:observe elementIds="instrument_qualityIssue" forValue=".+"/>
    <ui:observe elementIds="instrument_qualityIssue2" forValue=".+"/>
    <ui:removeOption elementIds="instrument_qualityIssue3"/>
</ui:formGuide>

<ui:formGuide observeAndOr="or">
    <ui:observe elementIds="instrument_qualityIssue" forValue="NONE, DATA ARE VALID"/>
    <ui:observe elementIds="instrument_qualityIssue" forValue="^$"/>
    <ui:skip elementIds="instrument_qualityIssue2" skipValue="" skipOptionText=""/>
</ui:formGuide>

<ui:formGuide observeAndOr="or" simulateEvents="true">
    <ui:observe elementIds="instrument_qualityIssue" forValue="NONE, DATA ARE VALID"/>
    <ui:observe elementIds="instrument_qualityIssue" forValue="^$"/>
    <ui:observe elementIds="instrument_qualityIssue2" forValue="^$"/>
    <ui:skip elementIds="instrument_qualityIssue3" skipValue="" skipOptionText=""/>
</ui:formGuide>
</c:if>

