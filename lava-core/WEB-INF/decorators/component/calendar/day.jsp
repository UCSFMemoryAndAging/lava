<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<%--javascript file includes JSP so must include it on the server side so
    it will be processed, rather than using standard technique for including
    external javascript (specifying src= ) --%>
<script language="javascript" type="text/javascript">
<%@ include file="/javascript/pagedList/pagedList.js" %>
<%@ include file="/javascript/pagedList/selectListItems.js" %>
</script>

<c:set var="component">
  <decorator:getProperty property="component"/>
</c:set>	
<c:set var="pageName">
  <decorator:getProperty property="pageName"/>
</c:set>
<c:if test="${empty pageName}">
	<c:set var="pageName" value="${component}"/>
</c:if>
<c:set var="listTitle">
  <decorator:getProperty property="listTitle"/>
</c:set>
<%-- get field name used for the calendar control if specified then calendar formatting is applied --%> 
<c:set var="calendarField">
  <decorator:getProperty property="calendarField"/>
</c:set>	

<c:set var="widthClass">
  <decorator:getProperty property="widthClass"/>
</c:set>
<c:if test="${empty widthClass}">
	<c:set var="widthClass" value="normal"/>
</c:if>

<c:if test="${not empty calendarField}">
	
	<c:set var="calendarDateStart">
	  ${calendarField}Start
	</c:set>	
	<c:set var="calendarDateEnd">
	  ${calendarField}End
	</c:set>	

	<c:set var="displayRange">
		${command.components[component].filter.params['displayRange']}
	</c:set>
	<c:set var="displayRangeDesc">
	  <tags:formatDateRange range="${displayRange}" beginDate="${displayRange=='All' ? null : command.components[component].filter.params[calendarDateStart]}" endDate="${displayRange=='All' ? null : command.components[component].filter.params[calendarDateEnd]}"/>
	</c:set>  
</c:if> <%--CalendarField defined--%>


<c:if test="${not empty listTitle}">
<div class="listControlBar">
	<div id="listTitle">
		${listTitle}
	</div>
</div>
</c:if>

<!-- CONTEXTUAL INFO -->
<%-- create a scoped variable for the contextualInfo comma-separated pairs --%>
<c:set var="contextualInfo">
  <decorator:getProperty property="contextualInfo"/>
</c:set>	
<c:if test="${not empty contextualInfo}">
  <fieldset id="contextualInfoBox">
    <c:forTokens items="${contextualInfo}" delims="," var="current" varStatus="status">
      <c:if test="${status.index % 2 == 0}">
        <c:set var="property" value="${current}"/>
      </c:if>
      <c:if test="${status.index % 2 == 1}">
        <tags:createField property="${property}" section="" component="${current}" mode="vw"/>
      </c:if>
    </c:forTokens>
  </fieldset>  
</c:if>

<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS -->
<!-- includes INSTRUCTION TEXT, if any -->
<!-- no page level action/nav buttons on list views right now -->

<%-- for secondary lists, want the page to reposition to that list on its events, so 
create anchor tag to be used as a target. this works in conjunction with javascript 
that submits the form (eventButton, eventLink) --%>
<c:set var="isSecondary">
  <decorator:getProperty property="isSecondary"/>
</c:set>
<c:if test="${not empty isSecondary}">
<a id="${component}"></a>
</c:if>

<div id="listContentBox">

<c:set var="customActions"> 
	<decorator:getProperty property="page.customActions"/>
</c:set>	

<c:if test="${not empty calendarField}">
<!-- calendar control bar -->
<div class="listControlBar">
    
    
    <span class="listControlBarCalendarDisplayRange">&nbsp;&nbsp;${displayRangeDesc}</span><br/><br/>
  	<span class="listControlBarCalendarLabels">&nbsp;&nbsp;Select:&nbsp;&nbsp; <tags:eventLink linkText="Prev ${displayRange}" action="prevDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
 	      <tags:eventLink linkText="Next ${displayRange}" action="nextDateRange" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>&nbsp;|&nbsp;
		  <tags:eventLink linkText="Today" action="displayNow" component="${component}" pageName="${pageName}" className="listControlBarCalendarAction"/>
     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <span class="listControlBarCalendarLabels">View:&nbsp;&nbsp;</span> 
   <tags:eventLink linkText="Day" action="displayDay" component="${component}" pageName="${pageName}" className="${displayRange=='Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
   <tags:eventLink linkText="24 Hour Day" action="display24Day" component="${component}" pageName="${pageName}" className="${displayRange=='24Day'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
   <tags:eventLink linkText="Week" action="displayWeek" component="${component}" pageName="${pageName}" className="${displayRange=='Week'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>&nbsp;|&nbsp;
    <tags:eventLink linkText="Month" action="displayMonth" component="${component}" pageName="${pageName}" className="${displayRange=='Month'? 'listControlBarCalendarActionCurrent' : 'listControlBarCalendarAction'}"/>
    

</div>
</c:if>
<div class="listControlBar">

<%-- output buttons before actions, since they float --%>
<%-- NOTE: since buttons float right, put them in the opposite order that they should appear --%>

<%-- need to create variable because does not evaluate inline, maybe because of '#' char --%>
<c:set var="onChangeUrl">#${component}</c:set>

<c:if test="${command.components[component].nrOfElements > 5}">
<tags:singleSelect property="command.components[${component}].pageSizeHolder" fieldId="pageSize" list="${lists['navigation.listPageSize']}" attributesText="onChange=&quot;document.${pageName}.action='${onChangeUrl}';submitForm(document.${pageName},'${component}__pageSize')&quot; " styleClass="listControlBarPageSizeSelector"/> 
</c:if>
<c:if test="${command.components[component].nrOfElements > command.components[component].pageSizeHolder}">
	<c:if test="${command.components[component].lastPage != true}">
		<tags:eventLink linkText="Next" action="nextPage" component="${component}" pageName="${pageName}" className="listControlBarPageAction"/>
	</c:if>		
	<tags:singleSelect property="command.components[${component}].pageHolder" fieldId="page" list="${command.components[component].recordNavigation}"attributesText="onChange=&quot;document.${pageName}.action='${onChangeUrl}';submitForm(document.${pageName},'${component}__recordNav')&quot; " styleClass="listControlBarPageSelector"/> 
	<c:if test="${command.components[component].firstPage != true}">		
				<tags:eventLink linkText="Previous" action="prevPage" component="${component}" pageName="${pageName}" className="listControlBarPageAction"/>
	</c:if>		
</c:if>			
<%-- output the actions --%>
${customActions}
<c:if test="${not empty listFilters && command.components[component].filterOn == false}">
	<tags:eventButton buttonText="Filter" action="toggleFilter" component="${component}" pageName="${pageName}"/>
</c:if>
<c:if test="${not empty command.components[component].filter.sort}">
	<tags:eventButton buttonText="Clear Sort" action="clearSort" component="${component}" pageName="${pageName}"/>
</c:if>
</div> <!-- end top listControlBar -->

<%--Determine width of table content area--%>
<c:choose>
	<c:when test="${widthClass == 'short'}">
		<table class="shortListing">
	</c:when>
	<c:when test="${widthClass == 'wide'}">
		<table class="wideListing">
	</c:when>
	<c:when test="${widthClass == 'veryWide'}">
		<table class="veryWideListing">
	</c:when>
	<c:otherwise>
		<table class="listing">
	</c:otherwise>
</c:choose>

<tags:componentListHasResults component="${component}">
	<decorator:getProperty property="page.groupActions"/>
</tags:componentListHasResults>
	
	<decorator:getProperty property="page.listColumns"/>
	<!-- CONTENT -->
	<decorator:body/>
</table>
	<tags:componentListHasNoResults component="${component}">
		<div class="listControlBar">
			<c:choose>
				<c:when test="${command.components[component].filterOn==true}">
					0 records match the filter criteria
				</c:when>
				<c:otherwise>
					0 records
				</c:otherwise>
			</c:choose>
			</div>
		</tags:componentListHasNoResults>

<div class="listControlBar">
	<%-- NOTE: since buttons float right, put them in the opposite order that they should appear --%>
<c:if test="${command.components[component].nrOfElements > 5}">
	<tags:singleSelectNoBind property="bottomPageSize" propertyValue="${command.components[component].pageSizeHolder}" fieldId="bottomPageSize" list="${lists['navigation.listPageSize']}" attributesText="onChange=&quot;document.${pageName}.action='${onChangeUrl}';submitForm(document.${pageName},'${component}__pageSize','',bottomRecordNavSetPageSize)&quot;" styleClass="listControlBarPageSizeSelector"/> 
</c:if>
<c:if test="${command.components[component].nrOfElements > command.components[component].pageSizeHolder}">
	<c:if test="${command.components[component].lastPage != true}">
		<tags:eventLink linkText="Next" action="nextPage" component="${component}" pageName="${pageName}" className="listControlBarPageAction"/>
	</c:if>		
	<tags:singleSelectNoBind property="bottomRecordNav" propertyValue="${command.components[component].pageHolder}" fieldId="bottomRecordNav" list="${command.components[component].recordNavigation}" attributesText="onChange=&quot;document.${pageName}.action='${onChangeUrl}';submitForm(document.${pageName},'${component}__pageSize','',bottomRecordNavSetPage)&quot;" styleClass="listControlBarPageSelector"/> 
	<c:if test="${command.components[component].firstPage != true}">		
		<tags:eventLink linkText="Previous" action="prevPage" component="${component}" pageName="${pageName}" className="listControlBarPageAction"/>
	</c:if>	
</c:if>	
</div> <!--  end bottom listControlBar -->

</div> <!-- end contentBox -->

	
<%-- if the list can have items selected, this code supports selecting all of the
items on the page and updating the display of the number of items selected without
the need for an HTTP request. this code must come after the decorator body is
included, since that is where the HTML div element within which to display the count
of selected items is created --%>	
<script language="javascript" type="text/javascript">
<!--
var firstElement = ${command.components[component].firstElementOnPage};
var lastElement = ${command.components[component].lastElementOnPage};
var numSelected = ${command.components[component].numSelected};
updateNumSelectedDisplay();
//-->
</script>


<!-- PAGE LEVEL ACTION/NAVIGATION BUTTONS -->
<!-- no page level action/nav buttons on list views right now -->




