<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="udsPacketMode" value="${fn:startsWith(param.instrTypeEncoded, 'udsformchecklist')?'dc':'vw'}"/>

<tags:contentColumn columnClass="colLeft2Col5050">

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsinstrument.header.section"/></page:param>
  <page:param name="instructions"></page:param>
<tags:createField property="packet" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="formId" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="formVer" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument" />
<tags:createField property="adcId" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="ptid" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="visitNum" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="initials" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsinstrument.packet.section"/></page:param>
  <page:param name="instructions"><spring:message code="udsinstrument.packet.${udsPacketMode == 'dc' ? 'formChecklistInstructions':'instructions'}"/></page:param>
<tags:createField property="packetDate" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument" mode="${udsPacketMode}"/>
<tags:createField property="packetBy" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument" mode="${udsPacketMode}"/>
<tags:createField property="packetStatus" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument" mode="${udsPacketMode}"/>
<tags:createField property="packetReason" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument" mode="${udsPacketMode}"/>
<tags:createField property="packetNotes" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument" mode="${udsPacketMode}"/>
</page:applyDecorator>

</tags:contentColumn>

<tags:contentColumn columnClass="colRight2Col5050">
<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsinstrument.ds.section"/></page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="dsBy" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="dsDate" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="dsStatus" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="dsReason" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="dsNotes" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
</page:applyDecorator>


<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="udsinstrument.lc.section"/></page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="lcBy" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="lcDate" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="lcStatus" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="lcReason" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
<tags:createField property="lcNotes" entity="${param.instrTypeEncoded}" component="instrument" entityType="udsinstrument"/>
</page:applyDecorator>


</tags:contentColumn>

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

	<c:if test="${fn:startsWith(instrTypeEncoded, 'udsmilestone')}">
		<ui:formGuide >
		  <ui:disable elementIds="packet" component="instrument"/>
		  <ui:setValue elementIds="packet" component="instrument" value="M"/>
		</ui:formGuide>
		<ui:formGuide >
		  <ui:disable elementIds="visitNum" component="instrument"/>
		  <ui:setValue elementIds="visitNum" component="instrument" value="-8"/>
		</ui:formGuide>
	</c:if>
	
	
</c:if>
</c:forEach>
</c:if>
