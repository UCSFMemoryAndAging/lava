<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="instrTypeEncoded" value="udsmedications2"/>
<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<c:set var="viewString" value="${component}_view"/>
<!-- set mode variable for list fields -->
<c:set var="componentView" value="${requestScope[viewString]}"/>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:set var="focusField" value="anyMeds"/>

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">Uds Medications</page:param>
  <page:param name="quicklinks"></page:param>


 
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
</c:import>

<c:choose>
	<c:when test="${componentView == 'enter'}">
		<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsmedications2EditContent.jsp">
			<c:param name="component">${component}</c:param>
			<c:param name="componentView">${componentView}</c:param>
			<c:param name="instrTypeEncoded">${instrTypeEncoded}</c:param>	
			
		</c:import>
	</c:when>
	<c:otherwise>
		<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsmedications2ViewContent.jsp">
			<c:param name="component">${component}</c:param>
			<c:param name="componentView">${componentView}</c:param>
			<c:param name="instrTypeEncoded">${instrTypeEncoded}</c:param>	
		</c:import>
	</c:otherwise>
</c:choose>

<page:applyDecorator name="component.instrument.section">
	<page:param name="sectionId">anonymous</page:param>
	<page:param name="view">${componentView}</page:param>
	<page:param name="instructions"> </page:param>
	<tags:createField property="notes['udsmedications']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

</page:applyDecorator> <%-- component.instument.content --%>   

</page:applyDecorator>    <%-- component.content --%>  
	    
