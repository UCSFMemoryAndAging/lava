<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<%-- new usage: pass in sectionId used for the quicklink id, and sectionNameKey
     to obtain the section name from the resource bundle.
     
     3 scenarios:
     sectionId == quicklink id: Return To Top quicklink and quicklink is identified
                  by sectionId
                  if sectionNameKey specified, used to obtain sectionName from
                  resource bundle for fieldset legend
     sectionId not specified: no quicklink. if sectionNameKey specified, used
                  to obtain sectionName from resource bundle for fieldset legend
     sectionId == 'anonymous': no quicklink, and section does
                  not have fieldset border with section name (i.e. sectionNameKey
                  is ignored), but there is no link at top to go to quicklink

     ** it is very important that all createField are decorated by a section for 
     stylesheet purposes in resolving the classic "clearing space beneath floats" issue.
     if field(s) do not belong to a section, then 'anonymous' should be passed to the section
     decorator as the sectionId parameter, in which case the field(s) will be wrapped
     by a fieldset that has no visibility, i.e. no border or legend 
     
     other parameters:
     sectionNameArgs - comma-separated arguments to the sectionNameKey
     
     quicklinkPosition - position the quicklink "Return to Top" above ('top') or below the 
                         section ('bottom'). defaults to 'top'. if fieldset is too long to 
                         fit onto a page, use 'top' so quicklink can be seen. also, can 
                         specify 'none' if do not want quicklink to appear.
                         
     instructions - instructions displayed at the top of the section below the section name
     
     instructionsArgs - comma-separated arguments to instructions (not supported yet. will be
                        supported when instructions is converted to a key into message resource bundle)
     
     instructionsCol2 - if supplied, there are two columns of instructions with the first
                        column populated by the "instructions" parameter and the second column
                        populated by this "instructionsCol2" parameter                         
                        
	 instructionsCol2Args - comma-separated arguments to instructionsCol2Args (not supported yet. will be
                        supported when instructions is converted to a key into message resource bundle)                       

     
     old usage (phasing out): pass in section which is the section name. the
     section id for the quicklink is not passed in; instead, quicklinks are
     created in the content .jsp
--%>
<c:set var="sectionId"> <%-- id for quicklinking --%>
	<decorator:getProperty property="sectionId" default=""/>
</c:set>
<c:set var="sectionNameKey">
	<decorator:getProperty property='sectionNameKey' default=""/>
</c:set>
<%-- set optional message arguments for section name spring:message tag, as a 
comma-delimited String (each String argument can contain JSP EL), an Object 
array (used as argument array), or a single Object (used as single argument). --%>
<c:set var="sectionNameArgs">
	<decorator:getProperty property='sectionNameArgs' default=""/>
</c:set>
<c:set var="quicklinkPosition">
	<decorator:getProperty property='quicklinkPosition' default="top"/>
</c:set>
<c:set var="instructions">
	<decorator:getProperty property="instructions" default=""/>
</c:set>
<c:set var="instructionsArgs">
	<decorator:getProperty property="instructions" default=""/>
</c:set>
<c:set var="instructionsCol2">
	<decorator:getProperty property="instructionsCol2" default=""/>
</c:set>
<c:set var="instructionsCol2Args">
	<decorator:getProperty property="instructionsCol2" default=""/>
</c:set>


<%-- obtain a value for the sectionName variable --%>
<c:choose>
	<%-- new way, where there is no quicklink Return To Top but section is bordered within
	     a fieldset with a legend --%>
	<c:when test="${empty sectionId && !empty sectionNameKey}">
		<c:set var="sectionName">
			<spring:message code="${sectionNameKey}" text="" arguments="${sectionNameArgs}"/>
		</c:set>
	</c:when>

	<%-- new way, when there is a quicklink Return To Top. Section is bordered by a fieldset
	     unless it is anonymous --%>
	<c:when test="${not empty sectionId}">
		<c:if test="${sectionId != 'anonymous'}">
			<c:set var="sectionName">
				<spring:message code="${sectionNameKey}" text="" arguments="${sectionNameArgs}"/>
			</c:set>
		</c:if>
	</c:when>
	
	<%-- support old way. if quicklink Return To Top exists, the tag for it is within content jsp.
	     the section has a fieldset border with a legend (anonymous section without fieldset not supported)
	     value of the section --%>
	<c:when test="${empty sectionId}">
		<c:set var="sectionName">
			<decorator:getProperty property="section" default=""/>
		</c:set>
	</c:when>
</c:choose>	

<c:if test="${not empty sectionId && sectionId != 'anonymous' && quicklinkPosition == 'top'}">
<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="${sectionId}" linkTextKey="top.quicklink"/>
</c:if>

<c:choose>

	<c:when test="${sectionId == 'anonymous'}">
<fieldset class="anonymous">
	<decorator:body/>
</fieldset>
	</c:when>
	

	<c:otherwise>		
<fieldset id="fieldset_${sectionId}">
	<legend>${sectionName}</legend>
	
    <c:choose>
    	<c:when test="${not empty instructions && not empty instructionsCol2}">
			<div class="sectionInstructions">
	            <tags:contentColumn columnClass="colLeft2Col5050">
    	        	${instructions}
        	    </tags:contentColumn>
            	<tags:contentColumn columnClass="colRight2Col5050">
            		${instructionsCol2}
	            </tags:contentColumn>
            </div>
    	</c:when>
    	<c:when test="${not empty instructions && empty instructionsCol2}">    		
			<div class="sectionInstructions">
				${instructions}
			</div>
		</c:when>
	</c:choose>
	
	<decorator:body/>
	
</fieldset>
	</c:otherwise>
</c:choose>

<c:if test="${not empty sectionId && sectionId != 'anonymous' && quicklinkPosition == 'bottom'}">
	<tags:sectionQuicklink requestUrl="${requestUrl}" sectionId="top" sourceSectionId="${sectionId}" linkTextKey="top.quicklink"/>
</c:if>
