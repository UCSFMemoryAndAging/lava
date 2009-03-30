<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>



<tags:contentColumn columnClass="colLeft2Col5050">

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">details</page:param>
  <page:param name="sectionNameKey">authUserRole.details.section</page:param>

	<tags:createField property="id" component="${component}"/>
	<tags:createField property="roleId" component="${component}"/>
	<tags:createField property="groupId" component="${component}"/>
	<tags:createField property="userId" component="${component}"/>
	<tags:createField property="project" component="${component}" entity="crmsAuthUserRole"/>
	<tags:createField property="unit" component="${component}" entity="crmsAuthUserRole"/>
	
</page:applyDecorator>  
 
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">
 
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">note</page:param>
  <page:param name="sectionNameKey">authUserRole.note.section</page:param>
	<tags:createField property="notes" component="${component}"/>

</page:applyDecorator>  




</tags:contentColumn>

