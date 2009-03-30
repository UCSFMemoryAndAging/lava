<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<page:applyDecorator name="component.instrument.section">
  <page:param name="section"><spring:message code="instrument.verify.section"/></page:param>
  <page:param name="instructions"> </page:param>
<tags:createField property="dvBy" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument" mode="dc"/>
<tags:createField property="dvDate" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument" mode="dc"/>
<tags:createField property="dvStatus" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument" mode="dc"/>
<tags:createField property="dvNotes" entity="${param.instrTypeEncoded}" component="instrument" entityType="instrument" labelAlignment="top" dataStyle="instrNote" mode="dc"/>
</page:applyDecorator>




