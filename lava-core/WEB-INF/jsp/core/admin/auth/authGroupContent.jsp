<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>



<tags:contentColumn columnClass="colLeft2Col5050">

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">details</page:param>
  <page:param name="sectionNameKey">authGroup.details.section</page:param>

	<tags:createField property="id" component="${component}"/>
	<tags:createField property="groupName" component="${component}"/>

</page:applyDecorator>  
 
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">note</page:param>
  <page:param name="sectionNameKey">authGroup.note.section</page:param>
	<tags:createField property="notes" component="${component}"/>

</page:applyDecorator>  
 
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">status</page:param>
  <page:param name="sectionNameKey">authGroup.status.section</page:param>
	<tags:createField property="effectiveDate" component="${component}"/>
	<tags:createField property="expirationDate" component="${component}"/>
	<tags:createField property="disabled" component="${component}"/>
</page:applyDecorator>



</tags:contentColumn>

