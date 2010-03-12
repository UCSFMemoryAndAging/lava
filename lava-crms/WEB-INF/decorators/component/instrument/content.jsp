<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="instrTypeEncoded"><decorator:getProperty property="instrTypeEncoded"/></c:set>
<%-- get componentView, e.g. 'enter', 'doubleEnter', 'status', etc.
	it is used for two purposes: 
	1) determines what buttons (and their events) are displayed, where this determination may be made
	in conjunction with the flow, as multiple flows may use the same componentView but have different
	buttons/events
	2) it is used to categorize the nature of a view such that the decorators/jsps/tags can use
	it their logic to do different things 
	
	note that componentView does not correspond to the flow, e.g. both the "enter" and "enterReview"
	flows set the componentView to 'enter' for their data entry view --%>

<c:set var="componentView"><decorator:getProperty property="view"/></c:set>
<%--  page name is used for the name of the form entity.  Use component name if not specifically specified --%>
<c:set var="pageName">
  <decorator:getProperty property="pageName"/>
</c:set>
<c:if test="${empty pageName}">
	<c:set var="pageName" value="${instrTypeEncoded}"/>
</c:if>

<%-- prep for page level action/navigation buttons --%>
<c:choose>
	<%-- the 'enter' componentView is used in both the 'enter' and 'enterReview' flows, but
		the left/right buttons differ in each flow --%>
	<c:when test="${componentView == 'enter'}">
		<c:choose>
			<c:when test="${flowMode == 'enter'}">
				<%-- left buttons --%>
				<c:choose>
					<%-- macdiagnosis property's lists do not have codes so no need for hide/show buttons --%>
					<c:when test="${not fn:startsWith(instrTypeEncoded, 'macdiagnosis')}">
						<c:set var="hideShowCodes" value="true"/>
					</c:when>					
					<c:otherwise>						
						<c:set var="hideShowCodes" value="false"/>
					</c:otherwise>
				</c:choose>					
			
				<c:choose>
					<%-- hide/show codes button and double enter button --%>
					<c:when test="${hideShowCodes && instrumentConfig[instrTypeEncoded].verify}">
						<c:set var="numLeftButtons" value="2"/>
						<c:if test="${showCodes}">
							<c:set var="leftbutton1_text" value="Hide Codes"/>
							<c:set var="leftbutton1_action" value="hideCodes"/>
						</c:if>			
						<c:if test="${!showCodes}">
							<c:set var="leftbutton1_text" value="Show Codes"/>
							<c:set var="leftbutton1_action" value="showCodes"/>
						</c:if>
						<c:set var="leftbutton2_text" value="Double Entry"/>
						<c:set var="leftbutton2_action" value="enterVerify"/>
					</c:when>
					
					<%--  verify button only --%>
					<c:when test="${!hideShowCodes && instrumentConfig[instrTypeEncoded].verify}">
						<c:set var="numLeftButtons" value="1"/>
						<c:set var="leftbutton2_text" value="Double Entry"/>
						<c:set var="leftbutton2_action" value="enterVerify"/>
					</c:when>
				
					<%--  hide/show codes button only --%>
					<c:when test="${hideShowCodes && !instrumentConfig[instrTypeEncoded].verify}">
						<c:set var="numLeftButtons" value="1"/>
						<c:if test="${showCodes}">
							<c:set var="leftbutton1_text" value="Hide Codes"/>
							<c:set var="leftbutton1_action" value="hideCodes"/>
						</c:if>			
						<c:if test="${!showCodes}">
							<c:set var="leftbutton1_text" value="Show Codes"/>
							<c:set var="leftbutton1_action" value="showCodes"/>
						</c:if>
					</c:when>
					
					<%-- no buttons --%>
					<c:when test="${!hideShowCodes && !instrumentConfig[instrTypeEncoded].verify}">
						<c:set var="numLeftButtons" value="0"/>
					</c:when>					
				</c:choose>			
	
		        <c:set var="numRightButtons" value="2"/>
				<c:set var="rightbutton1_text" value="Cancel"/>
				<c:set var="rightbutton1_action" value="cancel"/>
				<c:set var="rightbutton2_text" value="Save"/>
				<c:set var="rightbutton2_action" value="enterSave"/>
			</c:when>
			<c:when test="${flowMode == 'enterReview'}">
				<%-- the enterReview flow/enter state differs from the enter flow/enter state in that a) the
					flow transitions to a review state (as opposed to the doubleEnter or editStatus states) and
					b) there is no verify button on the left, because it appears in the review state, i.e. if
					the user choose to double enter, it is after the review --%>
				<c:choose>
					<%-- macdiagnosis property's lists do not have codes so no need for hide/show buttons --%>
					<c:when test="${not fn:startsWith(instrTypeEncoded, 'macdiagnosis')}">
						<c:set var="hideShowCodes" value="true"/>
					</c:when>					
					<c:otherwise>						
						<c:set var="hideShowCodes" value="false"/>
					</c:otherwise>
				</c:choose>					
				
				<c:choose>
					<%--  hide/show codes button only --%>
					<c:when test="${hideShowCodes}">
						<c:set var="numLeftButtons" value="1"/>
						<c:if test="${showCodes}">
							<c:set var="leftbutton1_text" value="Hide Codes"/>
							<c:set var="leftbutton1_action" value="hideCodes"/>
						</c:if>			
						<c:if test="${!showCodes}">
							<c:set var="leftbutton1_text" value="Show Codes"/>
							<c:set var="leftbutton1_action" value="showCodes"/>
						</c:if>
					</c:when>
					
					<%-- no buttons --%>
					<c:when test="${!hideShowCodes}">
						<c:set var="numLeftButtons" value="0"/>
					</c:when>					
				</c:choose>			
		
		        <c:set var="numRightButtons" value="2"/>
				<c:set var="rightbutton1_text" value="Cancel"/>
				<c:set var="rightbutton1_action" value="cancel"/>
				<c:set var="rightbutton2_text" value="Review"/>
				<c:set var="rightbutton2_action" value="enterSave"/>
			</c:when>
		</c:choose>
	</c:when>

	<%-- the 'doubleEnter' componentView is used in both the 'enter' and 'enterReview' flows, and
		has the same left/right buttons in each flow --%>	
	<c:when test="${componentView == 'doubleEnter'}">
		<%-- macdiagnosis property's lists do not have codes so no need for hide/show buttons --%>
		<c:if test="${not fn:startsWith(instrTypeEncoded, 'macdiagnosis')}">
			<c:set var="numLeftButtons" value="1"/>
			<c:if test="${showCodes}">
				<c:set var="leftbutton1_text" value="Hide Codes"/>
				<c:set var="leftbutton1_action" value="hideCodesDoubleEnter"/>
			</c:if>
			<c:if test="${!showCodes}">
				<c:set var="leftbutton1_text" value="Show Codes"/>
				<c:set var="leftbutton1_action" value="showCodesDoubleEnter"/>
			</c:if>			
		</c:if>			

        <c:set var="numRightButtons" value="2"/>
		<c:set var="rightbutton1_text" value="Defer"/>
		<c:set var="rightbutton1_action" value="doubleEnterDefer"/>
		<c:set var="rightbutton2_text" value="Verify"/>
		<c:set var="rightbutton2_action" value="doubleEnterSave"/>
	</c:when>
	
	<%-- the 'compare' componentView is used in both the 'enter' and 'enterReview' flows, and
		has the same left/right buttons in each flow --%>	
	<c:when test="${componentView == 'compare'}">	
		<c:set var="numLeftButtons" value="1"/>
		<c:if test="${showCodes}">
			<c:set var="leftbutton1_text" value="Hide Codes"/>
			<c:set var="leftbutton1_action" value="hideCodes"/>
		</c:if>
		<c:if test="${!showCodes}">
			<c:set var="leftbutton1_text" value="Show Codes"/>
			<c:set var="leftbutton1_action" value="showCodes"/>
		</c:if>			

        <c:set var="numRightButtons" value="2"/>
		<c:set var="rightbutton1_text" value="Cancel"/>
		<c:set var="rightbutton1_action" value="cancel"/>
		<c:set var="rightbutton2_text" value="Verified"/>
		<c:set var="rightbutton2_action" value="doubleEnterCompare"/>
	</c:when>	

	<c:when test="${componentView == 'upload'}">
		<c:set var="numLeftButtons" value="0"/>

        <c:set var="numRightButtons" value="2"/>
		<c:set var="rightbutton1_text" value="Cancel"/>
		<c:set var="rightbutton1_action" value="cancel"/>
		<c:set var="rightbutton2_text" value="Save"/>
		<c:set var="rightbutton2_action" value="upload"/>
	</c:when>
	
	<c:when test="${componentView == 'status'}">
		<c:set var="numLeftButtons" value="0"/>
		<c:if test="${command.components['instrument'].versioned}">
			<c:set var="numLeftButtons" value="1"/>
			<c:set var="leftbutton1_text" value="Change Version"/>
			<c:set var="leftbutton1_action" value="changeVersion"/>
		</c:if>
		<c:set var="numRightButtons" value="2"/>
		<c:set var="rightbutton1_text" value="Close"/>
		<c:set var="rightbutton1_action" value="close"/>
		<c:set var="rightbutton2_text" value="Edit"/>
		<c:set var="rightbutton2_action" value="editStatus"/>
	</c:when>
	
	<c:when test="${componentView == 'editStatus'}">	
		<c:set var="numLeftButtons" value="0"/>
        <c:set var="numRightButtons" value="2"/>
		<c:set var="rightbutton1_text" value="Cancel"/>
		<c:set var="rightbutton1_action" value="cancel"/>
		<c:set var="rightbutton2_text" value="Save"/>
		<c:set var="rightbutton2_action" value="statusSave"/>
	</c:when>

	<%-- NOTE: at this time, collect flow is not used --%>
	<c:when test="${componentView == 'collect'}">
		<c:choose>
			<%-- macdiagnosis property's lists do not have codes so no need for hide/show buttons --%>
			<c:when test="${not fn:startsWith(instrTypeEncoded, 'macdiagnosis')}">
				<c:set var="hideShowCodes" value="true"/>
			</c:when>					
			<c:otherwise>						
				<c:set var="hideShowCodes" value="false"/>
			</c:otherwise>
		</c:choose>					
		<c:choose>
			<%-- hide/show codes button and switch to enter flow button --%>
			<c:when test="${hideShowCodes && instrumentConfig[instrTypeEncoded].enterFlow}">
				<c:set var="numLeftButtons" value="2"/>
				<c:if test="${showCodes}">
					<c:set var="leftbutton1_text" value="Hide Codes"/>
					<c:set var="leftbutton1_action" value="hideCodes"/>
				</c:if>			
				<c:if test="${!showCodes}">
					<c:set var="leftbutton1_text" value="Show Codes"/>
					<c:set var="leftbutton1_action" value="showCodes"/>
				</c:if>
				<c:set var="leftbutton2_text" value="Data Entry"/>
				<c:set var="leftbutton2_action" value="collectEnter"/>
			</c:when>
			
			<%--  switch to enter flow button only --%>
			<c:when test="${!hideShowCodes && instrumentConfig[instrTypeEncoded].enterFlow}">
				<c:set var="numLeftButtons" value="1"/>
				<c:set var="leftbutton2_text" value="Data Entry"/>
				<c:set var="leftbutton2_action" value="collectEnter"/>
			</c:when>
			
			<%--  hide/show codes button only --%>
			<c:when test="${hideShowCodes && !instrumentConfig[instrTypeEncoded].enterFlow}">
				<c:set var="numLeftButtons" value="1"/>
				<c:if test="${showCodes}">
					<c:set var="leftbutton1_text" value="Hide Codes"/>
					<c:set var="leftbutton1_action" value="hideCodes"/>
				</c:if>			
				<c:if test="${!showCodes}">
					<c:set var="leftbutton1_text" value="Show Codes"/>
					<c:set var="leftbutton1_action" value="showCodes"/>
				</c:if>
			</c:when>
			
			<%-- no buttons --%>
			<c:otherwise>	
				<c:set var="numLeftButtons" value="0"/>
			</c:otherwise>					
		</c:choose>			

        <%-- right buttons pertain to the current flow --%>
        <c:set var="numRightButtons" value="2"/>
		<c:set var="rightbutton1_text" value="Cancel"/>
		<c:set var="rightbutton1_action" value="cancel"/>
		<%-- if collect mode brought back, this image will probably change to "save" and the dv* fields
			will not be modified in collect flow --%>		
		<c:set var="rightbutton2_text" value="Verify"/>
		<c:set var="rightbutton2_action" value="collectSave"/>
	</c:when>
	
	<%-- the 'review' componentView is used in the 'enterReview', 'upload' and 'collect' flows, but the
		left/right buttons differ among these flows --%>
	<c:when test="${componentView == 'review'}">
		<c:choose>
			<c:when test="${flowMode == 'enterReview'}">
				<c:choose>
					<%-- with enterReview, if thee user choose to double enter, it is after the review. if
						double enter is not applicable, the "verify" property should be explicitly set to
						false in the intrumentConfig --%>
					<c:when test="${instrumentConfig[instrTypeEncoded].verify}">
						<c:set var="numLeftButtons" value="1"/>
						<c:set var="leftbutton1_text" value="Double Entry"/>
						<c:set var="leftbutton1_action" value="enterReviewVerify"/>
					</c:when>
					
					<%-- no buttons --%>
					<c:when test="${not instrumentConfig[instrTypeEncoded].verify}">
						<c:set var="numLeftButtons" value="0"/>
					</c:when>					
				</c:choose>			
			
		        <c:set var="numRightButtons" value="2"/>
				<c:set var="rightbutton1_text" value="Revise"/>
				<c:set var="rightbutton1_action" value="enterReviewRevise"/>
				<c:set var="rightbutton2_text" value="Save"/>
				<c:set var="rightbutton2_action" value="enterReviewSave"/>
			</c:when>
			<c:when test="${flowMode == 'upload'}">
				<c:set var="numLeftButtons" value="0"/>
		        <c:set var="numRightButtons" value="2"/>
				<c:set var="rightbutton1_text" value="Revise"/>
				<c:set var="rightbutton1_action" value="uploadReviewRevise"/>
				<c:set var="rightbutton2_text" value="Save"/>
				<c:set var="rightbutton2_action" value="uploadReviewSave"/>
			</c:when>
			<%-- NOTE: at this time, collect flow is not used --%>
			<c:when test="${flowMode == 'collect'}">
				<c:set var="numLeftButtons" value="0"/>
		        <c:set var="numRightButtons" value="3"/>
				<c:set var="rightbutton1_text" value="Revise"/>
				<c:set var="rightbutton1_action" value="collectReviewRevise"/>
				<%-- if collect mode brought back, review will probably not correspond to verify, so
					the following may no longer apply, and may have "save" instead --%>		
				<c:set var="rightbutton2_text" value="Defer"/>
				<c:set var="rightbutton2_action" value="collectReviewDefer"/>
				<c:set var="rightbutton3_text" value="Verified"/>
				<c:set var="rightbutton3_action" value="collectReviewSave"/>
			</c:when>
		</c:choose>					
	</c:when>		
	
	<c:when test="${componentView == 'view'}">	
	    <%-- left buttons --%>
    	<c:set var="numLeftButtons" value="1"/>
    	<c:set var="leftbutton1_text" value="Status"/>
		<c:set var="leftbutton1_action" value="status"/>
	    <c:choose>
	   	 	<c:when test="${instrumentConfig[instrTypeEncoded].crudReport && command.components['instrument'].versioned}">
				<c:set var="numLeftButtons" value="3"/>
				<c:set var="leftbutton2_text" value="Print"/>
				<c:set var="leftbutton2_action" value="print"/>
				<c:set var="leftbutton3_text" value="Change Version"/>
				<c:set var="leftbutton3_action" value="changeVersion"/>
			</c:when>
			<c:when test="${instrumentConfig[instrTypeEncoded].crudReport}">
				<c:set var="numLeftButtons" value="2"/>
				<c:set var="leftbutton2_text" value="Print"/>
				<c:set var="leftbutton2_action" value="print"/>
			</c:when>
			<c:when test="${command.components['instrument'].versioned}">
				<c:set var="numLeftButtons" value="2"/>
				<c:set var="leftbutton2_text" value="Change Version"/>
				<c:set var="leftbutton2_action" value="changeVersion"/>
			</c:when>
		</c:choose>		        
	    <%-- right buttons --%>
		<c:choose>
			<c:when test="${instrumentConfig[instrTypeEncoded].uploadFlow}">
				<c:set var="numRightButtons" value="2"/>
				<c:set var="rightbutton1_text" value="Close"/>
				<c:set var="rightbutton1_action" value="close"/>
				<c:set var="rightbutton2_text" value="Edit"/>
				<c:set var="rightbutton2_action" value="upload"/>
			</c:when> 
			<c:when test="${instrumentConfig[instrTypeEncoded].collectFlow && instrumentConfig[instrTypeEncoded].enterFlow}">
				<c:set var="numRightButtons" value="3"/>
				<c:set var="rightbutton1_text" value="Close"/>
				<c:set var="rightbutton1_action" value="close"/>
				<c:set var="rightbutton2_text" value="Collect"/>
				<c:set var="rightbutton2_action" value="collect"/>
				<c:set var="rightbutton3_text" value="Data Entry"/>
				<c:set var="rightbutton3_action" value="enter"/>
			</c:when>
			<c:when test="${instrumentConfig[instrTypeEncoded].collectFlow}">
				<c:set var="numRightButtons" value="2"/>
				<c:set var="rightbutton1_text" value="Close"/>
				<c:set var="rightbutton1_action" value="close"/>
				<c:set var="rightbutton2_text" value="Collect"/>
				<c:set var="rightbutton2_action" value="collect"/>
			</c:when> 
			<c:when test="${instrumentConfig[instrTypeEncoded].enterFlow}">
				<c:set var="numRightButtons" value="2"/>
				<c:set var="rightbutton1_text" value="Close"/>
				<c:set var="rightbutton1_action" value="close"/>
				<c:set var="rightbutton2_text" value="Data Entry"/>
				<c:set var="rightbutton2_action" value="enter"/>
			</c:when> 
			<c:when test="${instrumentConfig[instrTypeEncoded].enterReviewFlow}">
				<c:set var="numRightButtons" value="2"/>
				<c:set var="rightbutton1_text" value="Close"/>
				<c:set var="rightbutton1_action" value="close"/>
				<c:set var="rightbutton2_text" value="Data Entry"/>
				<c:set var="rightbutton2_action" value="enterReview"/>
			</c:when> 
		</c:choose>		
	</c:when>			

	<c:when test="${componentView == 'delete'}">	
		<c:set var="numLeftButtons" value="0"/>
        <c:set var="numRightButtons" value="2"/>
		<c:set var="rightbutton1_text" value="Cancel"/>
		<c:set var="rightbutton1_action" value="cancelDelete"/>
		<c:set var="rightbutton2_text" value="Delete"/>
		<c:set var="rightbutton2_action" value="confirmDelete"/>
	</c:when>
	
	<c:when test="${componentView == 'changeVersion'}">	
		<c:set var="hideShowCodes" value="true"/>
		<c:set var="numLeftButtons" value="0"/>
        <c:set var="numRightButtons" value="2"/>
		<c:set var="rightbutton1_text" value="Cancel"/>
		<c:set var="rightbutton1_action" value="cancelChangeVersion"/>
		<c:set var="rightbutton2_text" value="Change Version"/>
		<c:set var="rightbutton2_action" value="confirmChangeVersion"/>
	</c:when>
	
</c:choose>	


<%-- PAGE LEVEL ACTION/NAVIGATION BUTTONS - TOP --%>
<c:set var="actionNavButtons">
	<%--  note: since right buttons float right, they must go before any buttons to the left, and, put 
			them in the opposite order that they should appear on the right --%>
	<c:forTokens items="right,left" delims="," var="position">
		<c:set var="numButtons" value="${position == 'right' ? numRightButtons : numLeftButtons}"/>
		<c:forEach begin="1" end="${numButtons}" var="current">
			<c:set var="stringText" value="${position}button${current}_text"/>
			<c:set var="text" value="${pageScope[stringText]}"/>
			<c:set var="stringAction" value="${position}button${current}_action"/>
			<c:set var="action" value="${pageScope[stringAction]}"/>
			<c:set var="stringDoGet" value="${position}button${current}_doGet"/>
			<c:set var="doGet" value="${pageScope[stringDoGet]}"/>
			<%-- Determine locked status for this action
			       Could have done this while setting up the button, but done here to consolidate the reasoning --%>
			<c:set var="locked" value="false"/>
			<c:if test="${fn:startsWith(action, 'enter') || fn:startsWith(action, 'doubleEnter') || fn:startsWith(action, 'collect') || fn:startsWith(action, 'upload') || (action == 'changeVersion') || (action == 'delete') || (action == 'editStatus')}">
				<c:set var="locked" value="${command.components['instrument'].locked}"/>
			</c:if>
			<c:if test="${empty doGet}">
				<tags:eventButton buttonText="${text}" action="${action}" component="instrument" pageName="${pageName}" className="${position == 'left' ? (current == 1 ? 'pageLevelLeftmostButton' : 'pageLevelLeftButton') : (current == 1 ? 'pageLevelRightmostButton' : 'pageLevelRightButton')}" locked="${locked}"/>
			</c:if>
			<c:if test="${not empty doGet}">
				<%-- if need button to do a GET instead of POST for some reason, use actionURLButton.tag and first
				     do this to get the value for idParam:
				<spring:bind path="command.components['${component}'].id"> 
				use this for the actionId attribute:
				actionId="lava.crms.assessment.instrument.${instrTypeEncoded}"
				and action="${action} 
				also, may need to add onClick="submitted=true" to actionURLButton.tag --%>
			</c:if>			
		</c:forEach>	
	</c:forTokens>
	
	<%-- assumption is that customAction event buttons are on the left --%>
	<c:set var="customActions">
		<decorator:getProperty property="page.customActions"/>
	</c:set>	
	${customActions}
</c:set>

<div id="pageLevelActionNavButtonTopBox">
	${actionNavButtons}
</div>

<!-- CONTEXTUAL INFO -->
<%-- output instrument contextual data fields. 
note: these are defined as context fields in the metadata so are output readonly --%>

<fieldset id="contextualInfoBox">
	<%-- use "instrument" for component attribute of createField, even if componentView is doubleEnter
		because contextual fields are readonly, so binding for read purposes only, and even
		when component is "compareInstrument" on compare view, the "instrument" component is still available, and
		furthermore, the "instrument" component is full populated, in terms of patient and visit whereas
		the "compareInstrument" component is not, so must use "instrument" to access properties of those entities. --%>
		
	<%-- if this decorator will be used for add, will need to have logic to make instrType editable, or, leave it out
	    of contextual info here, and have it editable as part of generic add instrument jsp. right now, add
		is in the component handler design, so uses the decorator --%>
	<c:if test="${componentView != 'add'}"> 
		<tags:createField property="id" entity="${instrTypeEncoded}" component="instrument" entityType="instrument"/>
		<tags:createField property="instrType" entity="${instrTypeEncoded}" component="instrument" entityType="instrument"/>
		<c:if test="${command.components['instrument'].versioned || componentView == 'changeVersion'}">
			<tags:createField property="instrVer" entity="${instrTypeEncoded}" component="instrument" entityType="instrument" mode="${(componentView=='changeVersion') ? 'dc':'vw'}"/>
		</c:if>		
	</c:if>
	<tags:createField property="projName" entity="${instrTypeEncoded}" component="instrument" entityType="instrument"/>
    <tags:createField property="patient.fullNameNoSuffix" entity="${instrTypeEncoded}" component="instrument" entityType="instrument"/>
	<tags:createField property="visit.visitDescrip" entity="${instrTypeEncoded}" component="instrument" metadataName="instrument.visit.visitDescrip"/>
</fieldset>  

<!-- SPECIFIC CONTENT -->
<div id="contentBox">
<c:choose>
	<c:when test="${componentView == 'collect' || componentView == 'enter' || componentView == 'upload'}">
		<decorator:body/>
	</c:when>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<decorator:body/>
	</c:when>
	<c:when test="${componentView == 'review' || componentView == 'view'}">	
		<decorator:body/>
	</c:when>	
	<c:when test="${componentView == 'editStatus' || componentView == 'status'}">	
		<c:import url="/WEB-INF/jsp/crms/assessment/instrument/statusContent.jsp">
			<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
		</c:import>
	</c:when>
	<c:when test="${componentView == 'delete'}">	
		<c:import url="/WEB-INF/jsp/crms/assessment/instrument/statusContent.jsp">
			<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
		</c:import>
		<decorator:body/>
	</c:when>

	<c:when test="${componentView == 'changeVersion'}">
	</c:when>
</c:choose>	

</div>

<!-- BOTTOM HORIZONTAL RULE -->
<div id="skiBox">
</div>

<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS - BOTTOM -->
<div id="pageLevelActionNavButtonBottomBox">    
	${actionNavButtons}
</div>

  
