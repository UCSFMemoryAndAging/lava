<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>

<c:set var="viewString" value="${component}_view"/>
<c:set var="componentView" value="${requestScope[viewString]}"/>

<page:applyDecorator name="component.entity.section">
  <page:param name="sectionId">data</page:param>
  <page:param name="sectionNameKey"></page:param>
  <page:param name="quicklinkPosition">top</page:param>
	<tags:createField property="familyStatus" component="${component}" entityType="patient"/>
	<tags:createField property="familyStudy" component="${component}" entityType="patient"/>
	<tags:createField property="relationToProband" component="${component}" entityType="patient"/>
	<tags:createField property="familyId" component="${component}" entityType="patient"/>
	<tags:createField property="probandCandidate" component="${component}" entityType="patient"/>
	<tags:createField property="twinZygosity" component="${component}" entityType="patient"/>
	<tags:createField property="twinId" component="${component}" entityType="patient"/>
	<tags:createField property="projSiteId" component="${component}" entityType="patient"/>
	<tags:createField property="relationNotes" component="${component}" entityType="patient"/>
</page:applyDecorator> 

<c:if test="${componentMode != 'vw'}">
<ui:formGuide ignoreUndo="true" ignoreUndoOnLoad="true">
    <ui:observe elementIds="${component}_familyStatus" forValue="Individual|^$"/>
	<ui:disable elementIds="${component}_familyStudy"/>
	<ui:disable elementIds="${component}_relationToProband"/>
	<ui:disable elementIds="${component}_familyId"/>
	<ui:disable elementIds="${component}_probandCandidate"/>
	<ui:disable elementIds="${component}_twinZygosity"/>
	<ui:disable elementIds="${component}_projSiteId"/>
	<ui:disable elementIds="${component}_relationNotes"/>
	<ui:setValue elementIds="${component}_familyStudy" value=""/>
    <ui:setValue elementIds="${component}_relationToProband" value=""/>
	<ui:setValue elementIds="${component}_probandCandidate" value=""/>
	<ui:setValue elementIds="${component}_twinZygosity" value=""/>
	<ui:setValue elementIds="${component}_projSiteId" value=""/>
    <ui:setValue elementIds="${component}_relationNotes" value=""/>
    <ui:setValue elementIds="${component}_familyId" value=""/>
</ui:formGuide>

<ui:formGuide ignoreUndo="true" ignoreUndoOnLoad="true">
    <ui:observe elementIds="${component}_familyStatus" forValue="Family$"/>
    <ui:disable elementIds="${component}_familyStudy"/>
    <ui:enable elementIds="${component}_relationToProband"/>
	<ui:enable elementIds="${component}_probandCandidate"/>
	<ui:enable elementIds="${component}_twinZygosity"/>
	<ui:enable elementIds="${component}_projSiteId"/>
    <ui:enable elementIds="${component}_relationNotes"/>  
    <ui:setValue elementIds="${component}_familyStudy" value=""/>
</ui:formGuide>

<ui:formGuide ignoreUndo="true" ignoreUndoOnLoad="true">
    <ui:observe elementIds="${component}_familyStatus" forValue="Family Study"/>
    <ui:enable elementIds="${component}_familyStudy"/>
    <ui:enable elementIds="${component}_relationToProband"/>
	<ui:enable elementIds="${component}_probandCandidate"/>
	<ui:enable elementIds="${component}_twinZygosity"/>
	<ui:enable elementIds="${component}_projSiteId"/>
    <ui:enable elementIds="${component}_relationNotes"/>  
</ui:formGuide>

<ui:formGuide>
	<ui:depends elementIds="${component}_familyStatus"/>
    <ui:observe elementIds="${component}_relationToProband" forValue="Proband|^$"/>
    <ui:disable elementIds="${component}_familyId"/>
	<ui:disable elementIds="${component}_probandCandidate"/>
    <ui:disable elementIds="${component}_relationNotes"/>
    <ui:setValue elementIds="${component}_familyId" value=""/>
	<ui:setValue elementIds="${component}_probandCandidate" value=""/>
    <ui:setValue elementIds="${component}_relationNotes" value=""/>
</ui:formGuide>

<ui:formGuide simulateEvents="true">
	<ui:depends elementIds="${component}_familyStatus"/>
    <ui:observe elementIds="${component}_twinZygosity" forValue="1|2|3" negate="true"/>
	<ui:disable elementIds="${component}_twinId"/>
	<ui:setValue elementIds="${component}_twinId" value=""/>
</ui:formGuide>
</c:if>


