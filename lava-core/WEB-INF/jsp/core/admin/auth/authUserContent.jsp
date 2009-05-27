<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<c:set var="authUserId"><tags:componentProperty component="${component}" property="id"/></c:set>
<c:if test="${authUserId != currentUser.id}">
	
	<content tag="customActions">
		<c:if test="${componentView=='view'}">
		<tags:actionURLButton buttonText="Reset Password"  actionId="lava.core.admin.auth.resetPassword" eventId="resetPassword__reset" component="${component}" idParam="${authUserId}"/>	    
		</c:if>
	</content>
</c:if>

<tags:contentColumn columnClass="colLeft2Col5050">

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">details</page:param>
  <page:param name="sectionNameKey">authUser.details.section</page:param>

	<tags:createField property="id" component="${component}"/>
	<tags:createField property="userName" component="${component}"/>
	<tags:createField property="shortUserName" component="${component}"/>
	<tags:createField property="shortUserNameRev" component="${component}"/>
	<tags:createField property="login" component="${component}"/>
	<tags:createField property="email" component="${component}"/>
	<tags:createField property="phone" component="${component}"/>
	<tags:createField property="authenticationType" component="${component}"/>

</page:applyDecorator>  
 
<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">note</page:param>
  <page:param name="sectionNameKey">authUser.note.section</page:param>
	<tags:createField property="notes" component="${component}"/>

</page:applyDecorator>  
 
</tags:contentColumn>
<tags:contentColumn columnClass="colRight2Col5050">

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">status</page:param>
  <page:param name="sectionNameKey">authUser.status.section</page:param>

	<tags:createField property="accessAgreementDate" component="${component}"/>
	<tags:createField property="effectiveDate" component="${component}"/>
	<tags:createField property="expirationDate" component="${component}"/>
	<tags:createField property="disabled" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">local</page:param>
  <page:param name="sectionNameKey">authUser.local.section</page:param>

	<tags:createField property="password" component="${component}"/>
	<tags:createField property="passwordExpiration" component="${component}"/>
	<tags:createField property="passwordResetToken" component="${component}"/>
	<tags:createField property="passwordResetExpiration" component="${component}"/>
	<tags:createField property="failedLoginCount" component="${component}"/>
	<tags:createField property="lastFailedLogin" component="${component}"/>
	<tags:createField property="accountLocked" component="${component}"/>
</page:applyDecorator>


</tags:contentColumn>

