<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>


<c:set var="supportsChange"><tags:componentProperty component="${component}" property="supportsChange"/></c:set>
<c:set var="changeMessageCode"><tags:componentProperty component="${component}" property="changeMessageCode"/></c:set>


<tags:outputText textKey="${changeMessageCode}" inline="false"/>

<page:applyDecorator name="component.password.section">

<c:if test="${supportsChange == true}">
	<tags:createField property="oldPassword" component="${component}" entity="authUserPasswordDto"/>
	<tags:createField property="newPassword" component="${component}" entity="authUserPasswordDto"/>
	<tags:createField property="newPasswordConfirm" component="${component}"  entity="authUserPasswordDto"/>
</c:if>
</page:applyDecorator>
 
