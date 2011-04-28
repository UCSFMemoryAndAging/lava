<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- fieldLabel

     Output field label.
--%>

<%@ attribute name="fieldId" required="true"
              description="the id of the input property to which the label refers" %>
<%@ attribute name="label" required="true"  
              description="the field label" %>
<%@ attribute name="indentLevel" required="true"
              description="integer representing how many levels to indent the label, where a level is a style decision
              				note: currently not used internally, as styles key off of the field div have the indentField class" %>              
<%@ attribute name="mode" required="true"  
              description="the view mode in effect, e.g. data collection" %>
<%@ attribute name="context" required="true" 
              description="the context of the property, e.g. result data" %>
<%@ attribute name="dataElement" required="true"
              description="the input element used for the data for the property" %>              
<%@ attribute name="requiredField" required="true"
              description="value of 'true' means to denote the field as required" %>
<%@ attribute name="labelAlignment"
              description="[optional] override the label alignment that would ordinarily be computed. it is
              				important to note that 'shortRight' and 'tightRight' embellish 'right' so must
              				be used in combination e.g. 'right shortRight'. the same is true for 'longLeft'
              				and 'longLongLeft' with respect to 'left'" %>
<%@ attribute name="labelStyle"
              description="[optional] CSS style to style the label" %>
<%@ attribute name="inOptionsAlignment"
              description="[optional] override the options alignment that would ordinarily be computed.
                          valid values:'groupLeftVertical', 'groupTopVertical', 'horizontal' (which
                          applies to both group label to left or on top)"%>            
              
<%@ variable name-given="alignment" variable-class="java.lang.String" scope="AT_END" 
              description="the resulting alignment of the label, output variable in case needed by caller" %>
<%@ variable name-given="optionsAlignment" variable-class="java.lang.String" scope="AT_END" 
              description="only applies to data elements with radio buttons (incl. comboRadioSelect), and designates
                           their group label position (if needed), and whether they should be aligned vertically
                           or horizontally" %>

<%-- NOTE: createField does not call this method if the label is blank --%>
              
<%-- perform logic based on mode, context and nature attributes to determine whether
     or not there should be a label, and where it should be placed --%>              
     
<%-- list modes have labels as table column headers and do not have individual property 
     labels, so do not output a label for those modes --%>     
<c:if test="${mode != 'le' && mode != 'lv'}">
	<%--hide the labels for hidden fields  --%>
	<c:if test="${dataElement=='hidden'}">
		<c:set var="label" value=""/>
	</c:if>
	<%-- determine the alignment --%>
	<c:choose>
		<%-- override takes precedence --%>
		<c:when test="${not empty labelAlignment}">
			<c:set var="alignment" value="${labelAlignment}"/>
		</c:when>
		<c:when test="${dataElement == 'checkbox'}">
			<%-- the default label alignment for a checkbox is the natural browser alignment, i.e.
				the label is to the left of the checkbox immediately followed by the checkbox. this
				works fine for individual checkboxes, but if there are multiple checkboxes in a row
				the checkboxes will not be vertically aligned (unless the labels all happen to be of
				the same length. to align multiple checkboxes, pass in one of the following for
				labelAlignment to override the default behavior:
				'right checkboxRight'
			--%>
		</c:when>
		<%-- if context is info data, put label to left, and if context is result data put 
    	     label to top or right, depending upon the data element (unless in vw mode, in
    	     which case result data labels are to the left) --%>
		<c:when test="${context == 'c' || context == 'i'}">
			<c:set var="alignment" value="left"/>
		</c:when>
		<c:when test="${context == 'r'}">	
		    <c:choose>
			<c:when test="${mode == 'vw'}">
			    <%-- result data is often associated with long labels (e.g. questions), so
			          set alignment to reflect this. --%>
				<c:set var="alignment" value="longLeft"/>
			</c:when>
			    <c:when test="${dataElement == 'comboRadioSelect' || dataElement == 'radioButtons'}">
			    	<c:set var="alignment" value="top"/>
		    	</c:when>
		    	<c:otherwise>
				    <c:set var="alignment" value="right"/>
				</c:otherwise> 
			</c:choose>
		</c:when>
	</c:choose>		

	<%-- determine the options alignment (only applies to widgets involving radio buttons) --%>
    <c:if test="${dataElement == 'comboRadioSelect' || dataElement == 'radioButtons'}">
		<c:choose>
			<%-- override takes precedence --%>
			<c:when test="${not empty inOptionsAlignment}">
				<c:set var="optionsAlignment" value="${inOptionsAlignment}"/>
			</c:when>
			<%-- if context is context/info data, group label is to left, make options vertical --%>
			<c:when test="${context == 'c' || context == 'i'}">
	    		<c:set var="optionsAlignment" value="groupLeftVertical" /> 
	  		</c:when>
			<%-- if context is result data, group label is top or right, make options horizontal --%>
			<c:when test="${context == 'r'}">	
				<c:set var="optionsAlignment" value="horizontal" /> 
			</c:when>
		</c:choose>		
	</c:if>		
	
    <%-- output the label tag, with the alignment as a style. 
         add any additional labelStyle(s) passed into the labelStyle attribute --%>
    <c:if test="${not empty labelStyle}">     
	    <c:set var="labelStyle" value=" ${labelStyle}"/>     <!-- need to make sure there is a space in there when added to the alignment value -->
	</c:if>
	<c:choose>
		
		
		<%-- special case for radio buttons. the label for these is a group label for all of the
		     radio buttons. additionally, each radio button has an indivdual label. it is these 
		     individual labels that should have the <label> tags. therefore, the group label which 
		     this tag creates does not use the <label> tag. rather, use a <p> tag --%>
		<c:when test="${dataElement == 'comboRadioSelect' || dataElement == 'radioButtons'}">
			<p class="${alignment} ${not empty labelStyle ? labelStyle : ''}">
		</c:when>
		<c:otherwise>
            <label for="${fieldId}" class="${alignment} ${not empty labelStyle ? labelStyle : ''}">
 		</c:otherwise>
  	</c:choose>		

    <c:choose>
		<c:when test="${not empty label}">
			${label}
		</c:when>
		<c:when test="${empty label}">
		    <%-- for labels with float style, i.e. left and right labels, some content must be present for the 
		         browser (some browsers) to align the label and value columns correctly. this may or may not
		         be necessary for right alignment, not sure, but definitely for left alignment.
		         note: if alignment is top, do not output content as it is not necessary and would create 
		               extra space above the label --%>
		    <c:if test="${alignment == 'left' || alignment == 'right'}">
				&nbsp;
			</c:if>
		</c:when>
	</c:choose>
	<%-- mark required fields with an asterisk. instrument result fields (context 'r') are all implicitly required, so do
	     not mark them. context fields ('c') are readonly, so not required. only info fields ('i') should be marked --%>
    <%-- putting entire <c:if> for required asterisk contiguous on same line as label output will move asterisk closer 
    	to label. really ? which browser ?--%>
    <c:if test="${(mode == 'dc' || mode == 'de') && context == 'i' && requiredField == 'Yes'}">
    	*
    </c:if>	
   
	<c:choose>
		<c:when test="${dataElement == 'comboRadioSelect' || dataElement == 'radioButtons'}">
			</p>
		</c:when>
		<c:otherwise>
			</label>	
 		</c:otherwise>
  	</c:choose>		
</c:if>
     
     





    
