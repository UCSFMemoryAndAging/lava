<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsfaq2"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="bills"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS FAQ</page:param>
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
  <page:param name="section"><spring:message code="udsfaq2.faq.section"/></page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"> </page:param>
  <tags:createField property="bills" entity="${instrTypeEncoded}" component="${component}"/>
 <tags:createField property="taxes" entity="${instrTypeEncoded}" component="${component}"/>

<tags:createField property="shopping" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="games" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="stove" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="mealPrep" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="events" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="payAttn" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="remDates" entity="${instrTypeEncoded}" component="${component}"/>
<tags:createField property="travel" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
	<page:param name="sectionId">anonymous</page:param>
	<page:param name="view">${componentView}</page:param>
	<page:param name="instructions"> </page:param>
	<tags:createField property="notes['udsfaq']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

</page:applyDecorator>    
</page:applyDecorator>    
	    
