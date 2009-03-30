<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udshachinski1"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="abrupt"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Hachinski</page:param>
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
  <page:param name="section"><spring:message code="udshachinski1.hachinski.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="abrupt" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="stepwise" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="somatic" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="emot" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="hxHyper" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="hxStroke" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="foclSym" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="foclSign" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="hachin" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="notes['hachinski']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

</page:applyDecorator>    
</page:applyDecorator>