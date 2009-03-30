<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- since tracking fields not involved in doubleEnter/compare, pass instrument for component --%>
<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionNameKey">instrument.tracking.section</page:param>
  <page:param name="view">${param.view}</page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="careId" entity="${param.instrTypeEncoded}" component="instrument"/>
${param.instrSpecificContent}
<tags:createField property="notes['tracking']" entity="${param.instrTypeEncoded}" component="instrument" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>





