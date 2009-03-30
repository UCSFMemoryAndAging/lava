<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="mdsstatus"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>


<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'collect' || componentView == 'enter' || componentView == 'doubleEnter' || componentView == 'compare'}">

		<c:set var="focusField" value="mdsdec"/>

 		<c:set var="labelStyle" value=""/>
	</c:when>
</c:choose>	


<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">MDS Status Form</page:param>
  <page:param name="quicklinks">info</page:param>

<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>


<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">info</page:param>
  <page:param name="sectionNameKey">mdsstatus.info.section</page:param>

			<tags:createField property="mdsdec" component="${component}" entity="${instrTypeEncoded}" labelAlignment="top"/>
			<tags:createField property="mdsdecdt" component="${component}" entity="${instrTypeEncoded}" labelAlignment="top"/>
			<tags:createField property="mdsdecaut" component="${component}" entity="${instrTypeEncoded}" labelAlignment="top"/>
			<tags:createField property="mdsdisc" component="${component}" entity="${instrTypeEncoded}" labelAlignment="top"/>
			<tags:createField property="mdsdiscdt" component="${component}" entity="${instrTypeEncoded}" labelAlignment="top" />
			<tags:createField property="mdsdiscrea" component="${component}" entity="${instrTypeEncoded}" labelAlignment="top" />
			<tags:createField property="mdsdiscreao" component="${component}" entity="${instrTypeEncoded}" labelAlignment="top" labelStyle="shortRight"/>
			<tags:createField property="mdsstat" component="${component}" entity="${instrTypeEncoded}" labelAlignment="top" labelStyle="shortRight"/>

</page:applyDecorator>


<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">anonymous</page:param>
<tags:createField property="notes['mdsstatus']" component="${component}" entity="${instrTypeEncoded}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
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

<ui:formGuide>
    <ui:observe elementIds="mdsdec" component="${component}" forValue="1" />
    <ui:unskip elementIds="mdsdecdt" component="${component}" skipValue=""/>
    <ui:unskip elementIds="mdsdecaut" component="${component}" />
</ui:formGuide>

<ui:formGuide>
    <ui:observe elementIds="mdsdisc" component="${component}" forValue="1" />
    <ui:unskip elementIds="mdsdiscdt" component="${component}" skipValue=""/>
     <ui:unskip elementIds="mdsdiscrea" component="${component}"/>
</ui:formGuide>

<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
  <ui:depends elementIds="mdsdisc" component="${component}"/>
    <ui:observe elementIds="mdsdiscrea" component="${component}" forValue="^6" />
    <ui:unskip elementIds="mdsdiscreao" component="${component}"/>
</ui:formGuide>


</c:if>
</c:forEach>
</c:if>

</page:applyDecorator>    
</page:applyDecorator>    

