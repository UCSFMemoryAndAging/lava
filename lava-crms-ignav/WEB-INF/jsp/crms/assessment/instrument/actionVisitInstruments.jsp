<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- different sections may have different characteristics for visit instruments --%>
<c:set var="section_name">${param.section_name}</c:set>
<c:if test="${empty section_name}">
	<c:set var="section_name" value="instrument"/> <%-- generic --%>
</c:if>

<%--comment to not include the Last Visit actions <includeLastVisit> --%>

<c:set var="currentInstrument" value="instrument" />
<c:set var="instrumentList" value="${command.components['instrumentGroup']}" />
<c:set var="component" value="${currentAction.target}" />
<c:set var="patientVisits" value="visit.patientVisits" />
<%-- the visit used in the navigation bar will not necessarily be the 'currentVisit',
       e.g. when patientInstruments list is showing (are to ignore the currentVisit value) --%>
<c:set var="visitNav" value="${command.components['visitNavigation']}" />

<%--<c:set var="viewString" value="${component}_view" />--%>
<%--<c:set var="componentView" value="${requestScope[viewString]}" />--%>
<c:set var="instrumentMode" value="${requestScope['instrument_mode']}" />
<c:set var="instrumentView" value="${requestScope['instrument_view']}" />

<%-- Only show this nav list for flows we expect to use it
      e.g. though we could show it for the 'delete' subflow, using it during 'delete' isn't a normal
      usage and possibly could complicate the flow, so just don't show it --%>
<c:set var="enableNavList" value="false" />
<c:if test="${(flowMode == 'list')
			  || (flowMode == 'view')
 			  || (flowMode == 'enter')
 			  || (flowMode == 'enterReview')
 			  || (flowMode == 'status')
 			  || (flowMode == 'editStatus')}">
	<%-- also a quick way of turning the rest off for debugging purposes--%>
	<c:set var="enableNavList" value="true" />
</c:if>

<c:if test="${enableNavList}">
<c:if test="${not empty currentPatient}">
	<c:set var="currentPageName" value="${command.components[currentInstrument].instrTypeEncoded}" />
	<c:if test="${empty currentPageName}">
		<%-- i.e. there is no current instrument, so this is likely the instrument list view --%>
		<c:set var="currentPageName" value="${component}" />
	</c:if>
		
	<%--<c:set var="linkMode" value="edit" />--%>
	<%-- Navigation links will be view or edit depending on instrumentView.  "Edit" will be default for non-locked visits --%>
	<c:set var="linkMode" value="${instrumentView == 'view' ? 'view' : 'edit'}" />
	<c:if test="${empty visitNav || visitNav.locked}">
		<c:set var="linkMode" value="view"/>
	</c:if>
	
	<br>
	<div class="leftNavSectionHeader">
	<spring:message code="actionbar.assessment.instrument.visitInstruments.header"/> 
	<br>
	
	<c:choose>
	<c:when test="${empty visitNav}">
		</div>
		<spring:message code="actionbar.assessment.instrument.visitInstruments.noVisitNav"/>
	</c:when>
	<c:otherwise>

		<%-- To add visit link, would have to change the flow too --%>
		<%--   This is currently not supported.  If we even view a visit, validation/saving
		       is not done on any current instruments. --%>
		<%-- <tags:listActionURLButton buttonImage="view" actionId="lava.crms.scheduling.visit.visit" idParam="${visitNav.id}"/>--%>

		<%-- Determine if the flowMode select box should be disabled.  Disable it if the current instrument
		     is not part of the instrument list that the flowMode controls.  Logically, it became
		     unclear what you would expect to happen if the flowMode changed under certain circumstances.  The
		     most intuitive action would be (say you changed from VIEW to EDIT) the navigation links become
		     EDIT links and the current instrument enters EDIT mode.  But if the instrument's visit was locked
		     (remember this instrument doesn't belong to the visit in the navigation bar), the intuitive
		     reaction would be to give an unauthorized error message at the top, but keep the instrument
		     showing in its VIEW state.  We could not legitimately deny the navigation list mode change
		     though, so there is an inconsistency.  The navigation list mode should match the mode of
		     the current instrument. --%>
		<c:set var="disableFlowModeChanges" value="false"/>
		<%-- use same conditions that determine if displaying '(Not Current Instrument's Visit)'" --%>
		<c:if test="${not empty instrumentList and not empty command.components[currentInstrument]}">
		<c:if test="${instrumentList[0].visit.id != command.components[currentInstrument].visit.id}">
			<c:set var="disableFlowModeChanges" value="true"/>
		</c:if>
		</c:if>

		<%-- add flowMode selectbox --%>
		<c:choose>
		<c:when test="${empty instrumentMode && not visitNav.locked}">
			<%-- there is no current instrument (e.g. at the instrument list), so the flowMode could
			       arguably be any mode since not in a mode.  We choose EDIT, so that any change would
			       result in changing to VIEW.  We want VIEW because we'd have to also select an
			       instrument when changing flowMode, and a VIEW would be quickest to show, allowing
			       immediate changing to another instrument of choice without having to worry about saving --%> 
			<tags:singleSelectNoBind property="flowMode" propertyValue="edit" fieldId="flowMode" list="${command.components['authorizedFlowModeList']}"
				attributesText="${disableFlowModeChanges ? 'disabled ' : '' }onChange=&quot;javascript:document.${currentPageName}.action='${requestUrl}&switchEvent=instrumentGroup__view#instrument';submitForm(document.${currentPageName},'instrument__switch','${target}')&quot;"/>
		</c:when>
		<c:when test="${instrumentMode == 'vw' || visitNav.locked}">
			<tags:singleSelectNoBind property="flowMode" propertyValue="view" fieldId="flowMode" list="${command.components['authorizedFlowModeList']}"
				attributesText="${disableFlowModeChanges ? 'disabled ' : '' }onChange=&quot;javascript:document.${currentPageName}.action='${requestUrl}&id=${command.components[currentInstrument].id}&switchEvent=instrumentGroup__edit#instrument';submitForm(document.${currentPageName},'instrument__switch','${target}')&quot;"/>				
		</c:when>
		<c:otherwise>
			<%-- the default flowMode will be 'edit' --%>
			<tags:singleSelectNoBind property="flowMode" propertyValue="edit" fieldId="flowMode" list="${command.components['authorizedFlowModeList']}"
				attributesText="${disableFlowModeChanges ? 'disabled ' : '' }onChange=&quot;javascript:document.${currentPageName}.action='${requestUrl}&id=${command.components[currentInstrument].id}&switchEvent=instrumentGroup__view#instrument';submitForm(document.${currentPageName},'instrument__switch','${target}')&quot;"/>
		</c:otherwise>
		</c:choose>
		
		<b><spring:message code="actionbar.assessment.instrument.visitInstruments.visitDate"/>: ${visitNav.visitDate}</b>
		</div>

<%-- commented out: was old work in getting a visit select box in place, instead of using default of last visit

		<c:if test="${not empty visitNav.id}">
			${visitNav.visitDate}<br>
		</c:if>

		<form name="visitNavigationJID" method="post">
			<tags:createField component="visitNavigation"
				entity="visitNavigation" property="id"
				metadataName="visitNavigation.id" fieldId="visitNavigation_id"
				mode="de" />
		
			<script type="text/javascript">
		    	//this appears to be triggered unexpected on page load, maybe because the ACS control fires onChange when
			    //its value is set on page load ?. anyway, it does no harm for now, but something to know about.
				function submitVisitChange() {
					//window.location.reload(true);
					document.visitNavigation.submit();
				}
				var visitContextBox = ACS['acs_textbox_visitNavigation_id'];
				visitContextBox.onChangeCallback = submitVisitChange;
				visitContextBox.onChangeCallbackOwner = this;
			</script>
		</form>

		<c:set var="instrumentList" value="" />
		<c:forEach items="${command.components['patientVisitInstrumentGroups']}" var="visitGroup">
			<c:if test="${visitGroup.key == visitNav.id}">
				<c:set var="instrumentList" value="${visitGroup.value}" />
			</c:if>
		</c:forEach>
		<form name="visitNavigationJID" method="post">
			<tags:createField component="visitNavigation"
				entity="visitNavigation" property="id"
				metadataName="visitNavigation.id" fieldId="visitNavigation_id"
				context="c" mode="de" />
		
			<script type="text/javascript">
		    	//this appears to be triggered unexpected on page load, maybe because the ACS control fires onChange when
			    //its value is set on page load ?. anyway, it does no harm for now, but something to know about.
				function submitVisitChange() {
					window.location.reload(true);
					//document.visitNavigationJID.submit();
				}
				var visitContextBox = ACS['acs_textbox_visitNavigation_id'];
				visitContextBox.onChangeCallback = submitVisitChange;
				visitContextBox.onChangeCallbackOwner = this;
			</script>
		</form>
--%>

		<c:choose>
			<c:when test="${empty instrumentList}">
		&nbsp;<i><spring:message code="actionbar.assessment.instrument.visitInstruments.noInstrFound"/></i>
				<br>
			</c:when>
			<c:otherwise>
				<c:if test="${not empty command.components[currentInstrument]}">
					<c:if test="${instrumentList[0].visit.id != command.components[currentInstrument].visit.id}">
					<B><I><spring:message code="actionbar.assessment.instrument.visitInstruments.mismatchVisitAndInstr"/></I></B>
					<br>
					</c:if>
				</c:if>

				<c:if test="${visitNav.locked}">
					<img src="images/lock.png"
								alt="+&nbsp;" height=11 title="Visit is locked"/>
					&nbsp;<B><I><spring:message code="actionbar.assessment.instrument.visitInstruments.visitLocked"/></I></B>
					<br>
				</c:if>

				<%-- only enable the readyForSubmit field if all instruments on list are complete --%>
				<c:forEach items="${instrumentList}" var="instrument">
					<c:if test="${not empty instrumentConfig[instrument.instrTypeEncoded]}">
						<c:choose>
						<c:when test="${visitNav.locked}">
							<img src="images/check_blank.png"
								alt="+&nbsp;" height=11 title="Locked, thus no checks done"/>
						</c:when>
						<c:when test="${not empty instrument.logicCheckSummary.lcStatus}">
							<img src="images/check_red.png"
								alt="+&nbsp;" height=11 title="Saved, but logic check issues"/>
						</c:when>
						<c:when test="${instrument.dataCompleteStatus}">
							<img src="images/check_green.png"
								alt="+&nbsp;" height=11 title="Saved & Complete"/>
						</c:when>
						<c:otherwise>
							<c:choose>
							<c:when test="${not empty instrument.deStatus}">
								<img src="images/check_grey.png"
									alt="+&nbsp;" height=11 title="Saved, but Incomplete"/>
							</c:when>
							<c:otherwise>
								<img src="images/check_blank.png"
									alt="-&nbsp;" height=11 />
							</c:otherwise>
							</c:choose>
						</c:otherwise>
						</c:choose>

						<%-- denote the "current" instrument --%>
						<c:if test="${instrument.id == command.components[currentInstrument].id}">
							<B>
						</c:if>
						<c:choose>
						<c:when test="${empty instrumentMode || instrumentMode == 'vw'}">
							<%-- If in view mode, or if no current instrument, then can switch immediately w/o needing to save possible save data --%>
							<%-- (note: for view mode, we could have also used the eventLink used in the edit mode below) --%>
							<a href="<tags:actionURL actionId="emorylava.crms.assessment.instrument.instrumentGroup" flowExecutionKey="${flowExecutionKey}" eventId="instrument__switch" parameters="switchEvent,instrumentGroup__${linkMode}" idParam="${instrument.id}"/>">
							${instrument.instrTypeExt}</a>
						</c:when>
						<c:otherwise>
							<%--Note: we must use instrTypeEncoded of _current_ instrument here (as pageName) to
					          be able to POST unsaved instrument data when switching
						      this is provided to us by the handler creating componentInstruments--%>
							<tags:eventLink pageName="${currentPageName}"
								component="instrument" action="switch"
								linkText="${instrument.instrTypeExt}"
								parameters="id,${instrument.id},switchEvent,instrumentGroup__${linkMode}" />
						</c:otherwise>
						</c:choose>

						<c:if test="${instrument.id == command.components[currentInstrument].id}">
							</B>
						</c:if>

						<br>
					</c:if>
				</c:forEach>
			</c:otherwise>
		</c:choose>

	</c:otherwise></c:choose> <%--otherwise not <c:when test="${empty visitNav}">--%>
</c:if> <%--<c:if test="${not empty currentPatient}">--%>
</c:if> <%--<c:if test="${enableNavList}">--%>


