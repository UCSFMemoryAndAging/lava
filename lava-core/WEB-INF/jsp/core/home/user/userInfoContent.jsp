<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<content tag="customActions">
	<c:if test="${componentView=='view'}">
		<tags:actionURLButton buttonText="Change Password"  actionId="lava.core.home.user.changePassword" eventId="changePassword__change" component="${component}" />	    
	</c:if>
</content>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">contactInfo</page:param>
  <page:param name="sectionNameKey">userInfo.contactInfo.section</page:param>

	<tags:createField property="email" component="${component}"/>
	<tags:createField property="phone" component="${component}"/>
	
</page:applyDecorator>  
 
 
