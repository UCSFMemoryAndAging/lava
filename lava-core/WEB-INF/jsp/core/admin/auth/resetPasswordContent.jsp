<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="modeString" value="${component}_mode"/>
<c:set var="componentMode" value="${requestScope[modeString]}"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>


<c:set var="supportsReset"><tags:componentProperty component="${component}" property="supportsReset"/></c:set>
<c:set var="resetMessageCode"><tags:componentProperty component="${component}" property="resetMessageCode"/></c:set>


<tags:outputText textKey="${resetMessageCode}" inline="false"/>

<page:applyDecorator name="component.password.section">

<c:if test="${supportsReset == true}">
	<tags:createField property="newPassword" component="${component}" entity="authUserPasswordDto"/>
	<tags:createField property="newPasswordConfirm" component="${component}"  entity="authUserPasswordDto"/>
</c:if>
</page:applyDecorator>
 
