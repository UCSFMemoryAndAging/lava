<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<c:set var="viewString" value="${component}_view"/>
<!-- set mode variable for list fields -->
<c:set var="componentView" value="${requestScope[viewString]}"/>
<c:choose>
	<c:when test="${componentView == 'edit'}">
		<c:set var="statusMode" value="le"/>
	</c:when>
	<c:otherwise>
		<c:set var="statusMode" value="lv"/>
	</c:otherwise>
</c:choose>


<page:applyDecorator name="component.entity.section">
<page:param name="sectionId">statusHistory</page:param>
 <page:param name="sectionNameKey">enrollmentStatus.statusHistory.section</page:param>
<div class="inlineTable">
<table class="listing" >
	<tr>
	<th width="12%">Status</th>
	<th width="19%">Date</th>
	<th width="45%">Note</th>
	</tr>
<c:if test="${not empty command.components[component].referredDesc}">
	<tr>
	<td><tags:createField property="referredDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="referredDate" component="${component}" mode="${statusMode}"/></td>
	<td><tags:createField property="referredNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>
<c:if test="${not empty command.components[component].deferredDesc}">
	<tr>
	<td><tags:createField property="deferredDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="deferredDate" component="${component}" mode="${statusMode}"/></td>
	<td><tags:createField property="deferredNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>

<c:if test="${not empty command.components[component].eligibleDesc}">
	<tr>
	<td><tags:createField property="eligibleDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="eligibleDate" component="${component}" mode="${statusMode}"/></td>
	<td><tags:createField property="eligibleNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>

<c:if test="${not empty command.components[component].ineligibleDesc}">
	<tr>
	<td><tags:createField property="ineligibleDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="ineligibleDate" component="${component}" mode="${statusMode}"/></td>
	<td><tags:createField property="ineligibleNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>

<c:if test="${not empty command.components[component].declinedDesc}">
	<tr>
<td><tags:createField property="declinedDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="declinedDate" component="${component}" mode="${statusMode}"/></td>
<td><tags:createField property="declinedNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>


<c:if test="${not empty command.components[component].enrolledDesc}">
	<tr>
<td><tags:createField property="enrolledDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="enrolledDate" component="${component}" mode="${statusMode}"/></td>
<td><tags:createField property="enrolledNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>

<c:if test="${not empty command.components[component].excludedDesc}">
	<tr>
<td><tags:createField property="excludedDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="excludedDate" component="${component}" mode="${statusMode}"/></td>
<td><tags:createField property="excludedNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>


<c:if test="${not empty command.components[component].withdrewDesc}">
	<tr>
<td><tags:createField property="withdrewDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="withdrewDate" component="${component}" mode="${statusMode}"/></td>
<td><tags:createField property="withdrewNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>


<c:if test="${not empty command.components[component].inactiveDesc}">
	<tr>
	<td><tags:createField property="inactiveDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="inactiveDate" component="${component}" mode="${statusMode}"/></td>
	<td><tags:createField property="inactiveNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>


<c:if test="${not empty command.components[component].deceasedDesc}">
	<tr>
<td><tags:createField property="deceasedDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="deceasedDate" component="${component}" mode="${statusMode}"/></td>
<td><tags:createField property="deceasedNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>


<c:if test="${not empty command.components[component].autopsyDesc}">
	<tr>
<td><tags:createField property="autopsyDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="autopsyDate" component="${component}" mode="${statusMode}"/></td>
	<td><tags:createField property="autopsyNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>


<c:if test="${not empty command.components[component].closedDesc}">
	<tr>
<td><tags:createField property="closedDesc" component="${component}" mode="lv"/></td>
	<td><tags:createField property="closedDate" component="${component}" mode="${statusMode}"/></td>
<td><tags:createField property="closedNote" component="${component}" mode="${statusMode}"/></td>
	</tr>
</c:if>
</table>
</div>
</page:applyDecorator>