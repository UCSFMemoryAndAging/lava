<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>


<content tag="listColumns">
<tags:listRow>
<%-- <tags:componentListColumnHeader component="${component}" label="Action" width="8%"/> --%>
<tags:componentListColumnHeader component="${component}" label="Measure" width="12%" sort="instrType"/>
<tags:componentListColumnHeader component="${component}" label="Collection" width="20%" sort="collectionStatusBlock"/>
<tags:componentListColumnHeader component="${component}" label="Data Entry" width="20%" sort="entryStatusBlockvisitType"/>
<tags:componentListColumnHeader component="${component}" label="Verify" width="20%" sort="verifyStatusBlock"/>
<tags:componentListColumnHeader component="${component}" label="Summary" width="20%" sort="summary"/>
</tags:listRow>
</content>
<tags:list component="${component}">
	<tags:listRow>
<%-- remove actions until these CRUD subflows are supported in the CRUD entity flows. for now, this list just serves as
     as secondary component reference list, which is fine; these actions may never be added back in	
		 <tags:listCell styleClass="actionButton">
		 		<c:choose>
			    <c:when test="${not empty implInstrs[this.instrTypeEncoded]}">
					<tags:listInstrumentActionURLStandardButtons actionId="lava.crms.assessment.instrument.${item.instrTypeEncoded}" idParam="${item.id}" locked="${item.locked}"/>
				</c:when>
				<c:otherwise>
					<tags:listActionURLButton buttonImage="delete" actionId="lava.crms.assessment.instrument.instrument" eventId="instrument__delete" idParam="${item.id}" locked="${item.locked}"/>	    
				</c:otherwise>
				</c:choose>
		</tags:listCell>
--%>		
		<tags:listCell>
			<tags:listField property="instrType" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="collectionStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="entryStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="verifyStatusBlock" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>
		</tags:listCell>
		<tags:listCell>
			<tags:listField property="summary" component="${component}" listIndex="${iterator.index}" entityType="instrument"/>
		</tags:listCell>
	</tags:listRow>
</tags:list>
