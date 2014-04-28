<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component" value="importLog"/>
<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.content">
  <page:param name="component">${component}</page:param>
  <page:param name="pageHeadingArgs"><tags:componentProperty component="${component}" property="importTimestamp"/>,<tags:componentProperty component="${component}" property="filename"/></page:param>
 
<page:applyDecorator name="component.entity.content">
  <page:param name="component">${component}</page:param>
 
		<c:import url="/WEB-INF/jsp/core/importer/log/importLogContent.jsp">
			<c:param name="component">${component}</c:param>
		</c:import>

		<page:applyDecorator name="component.entity.section">
  			<page:param name="sectionId">log</page:param>
  			<page:param name="sectionNameKey">importLog.crmsLog.section</page:param>
			<tags:createField property="projName" component="${component}"/>
<%-- actual fields here are TBD			
			<tags:createField property="dupPatients" component="${component}"/>
			<tags:createField property="newPatients" component="${component}"/>
			<tags:createField property="dupCaregivers" component="${component}"/>
			<tags:createField property="newCaregivers" component="${component}"/>
			<tags:createField property="dupContactInfo" component="${component}"/>
			<tags:createField property="newContactInfo" component="${component}"/>
			<tags:createField property="dupEnrollments" component="${component}"/>
			<tags:createField property="newEnrollments" component="${component}"/>
			<tags:createField property="dupVisits" component="${component}"/>
			<tags:createField property="newVisits" component="${component}"/>
			<tags:createField property="dupInstruments" component="${component}"/>
			<tags:createField property="newInstruments" component="${component}"/>
 --%>			
		</page:applyDecorator>
	
	</page:applyDecorator>    
</page:applyDecorator>	    

