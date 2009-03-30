<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%@ tag import="java.util.Iterator" %>
<%@ tag import="java.util.Map" %>
<%@ tag import="java.util.LinkedHashMap" %>
<%@ tag import="java.util.Map.Entry" %>

<%-- comboRadioSelect

     Output HTML input fields for the specified property, where radio buttons are used
     for showing valid options from the specified list, and a select box is used for showing
     code options (skip codes, error codes, etc.) from the list. The two groupings can be 
     distinguished because the value of valid options are greater than 0 and the value 
     of all code options are less than 0, i.e. their string values start with '-'.

     Because two fields can not be submitted with the same name, the select box with codes
     is named by appending "_CODE" to the property name. Because a group of radio buttons 
     must have at least one button selected, have a hidden button with input property name
     "missingDataCode" which indicates that the value from the select box should be used. 
     The value used for this "missingDataCode" radio button must be a string that is convertible 
     to a numeric, because properties could be numeric or string, and the controller will flag 
     the property with an invalid property field error if it cannot do the conversion. Also,
     a value should be chosen which is unlikely to ever be a valid result field value. Thus,
     the value -9999 was chosen. 
     The controller contains logic to process both the radio buton and select box and to assign 
     the correct value to a property, or to flag a required field error if the "missingDataCode"
     radio button is chosen and the select box is blank.
  
     The groups are mutually exclusive, i.e. selecting a value from the radio buttons should
     deselect any value from the select box, and vice versa. This is implemented via onclick
     event handler for each radio button, and the onchange event handler for the select box.
     For this reason, the $attributesText passed in can not contain these attributes.

     Whatever $attributesText are passed in are applied to both the radio buttons and the
     select box. This may not make sense, and two sets of attributes may need to be supplied,
     one for radio buttons and one for the select box.
--%>

<%@ attribute name="property" required="true" 
              description="the name of the field to bind to" %>
<%@ attribute name="fieldId" required="true" 
              description="id for the field" %>
<%@ attribute name="list" type="java.util.Map" required="true" 
              description="a Map where entry key is list item value and entry value is list item label" %>
<%@ attribute name="optionsAlignment" required="true"
              description="alignment of radio button options: 'groupLeftVertical', 
                           'groupTopVertical' or 'horizontal'" %>              
<%@ attribute name="attributesText"
              description="[optional] attributes for the HTML element" %>
<%@ attribute name="styleClass"
              description="[optional] the style class for the select box (or multiple classes, space separated)" %>              
              
<%@ variable name-given="errorFlag" variable-class="java.lang.Boolean" scope="AT_END" 
              description="flag indicating property error(s) exist" %>             
<%@ variable name-given="errorMessages" variable-class="java.lang.String[]" scope="AT_END"
              description="array of error messages, if any" %>             
              
              
<%-- ???? for combo radio button/select box widgets, the attributesText are currently applied to both widgets, 
 does this make sense ?? --%>
<c:set var="radioIndex" value="0"/>   		        
<spring:bind path="${property}">
  <%-- wrap the comboRadioSelect in a div so that if it wraps, it will not wrap under a left label --%>
  <div class="comboRadioSelect">
    <%-- display the radio buttons for valid options --%>
	<c:set var="firstOption" value="true"/>
    <c:forEach items="${list}" var="entry">
     	<%-- if the list item is a regular list item, create a radio button for it, i.e. if the
     	     list item is not a code or the blank list item
     	     note: can determine that list item is not a code if it is not in the missingCodesMap --%>
		<c:if test="${(entry.key != '') and (empty missingCodesMap[entry.key])}">
			<%-- in order for vertically stacked radio buttons to align horizontally, have to put some
  			     content in the left float block. for first button this is the group label, but each
  			     successive button needs content --%>
  			<c:if test="${firstOption == 'false' && optionsAlignment == 'groupLeftVertical'}">
  				<p class="left">
  					&nbsp;
  				</p>
  			</c:if>
			<c:set var="firstOption" value="false"/>
		    <label for="${fieldId}${radioIndex}" class="${optionsAlignment}">
	            <input type="radio" id="${fieldId}${radioIndex}" name="${status.expression}" value="${entry.key}" class="${styleClass}"
	    	        <c:if test="${status.value == entry.key}">
    	    	        checked="checked"
        	    	    <c:set var="hasValue" value="true"/>
		            </c:if>
					onclick="deselectSingleSelectBox('${fieldId}_CODE')"		            
        		    ${attributesText}
            	>
	            ${entry.value}
			</label>            	
			<c:set var="radioIndex" value="${radioIndex + 1}"/>
        </c:if>
        <%-- set flag if codes are found, because if no codes then no select box below --%>
        <c:if test="${not empty missingCodesMap[entry.key]}">
            <c:set var="listHasCodes" value="true"/>
   	    </c:if>            
    </c:forEach>
    
   	<%-- if hiding codes (indicated by listHasCodes, as the list will not have any codes if hiding, so listHasCodes
   	     will end up false) but current value is a code, add that code value as a checked radio button --%>
   	<c:if test="${(!listHasCodes) and (not empty missingCodesMap[status.value])}">
    	<label for="${fieldId}${radioIndex}" class="${optionsAlignment}">
        	<input type="radio" id="${fieldId}${radioIndex}" name="${status.expression}" value="${status.value}" class="${styleClass}" checked="checked" ${attributesText}>
            ${missingCodesMap[status.value]}
		</label>            	
	</c:if>    

    <%-- do not create the missing data code radio button if hide codes, because if a property has
         a missing data code, then this invisible button will be checked, but the corresponding select
         box will not have any missing data codes so on submission, the property will appear to 
         be null. by leaving the missing data code button out, no radio button will be checked so
         nothing will be submitted for the property, and it will retain its missing data code value
         in the command object on the server --%>    
         
    <c:if test="${listHasCodes}">

		<%-- create the "missingDataCode" radio button, so that if the property value is a missing
		     data code, the radio button widget will submit a value indicating that the missing
		     data code from the associated select box should be used as the property value.
		     hide this radio button from the user --%>
		<c:if test="${optionsAlignment == 'groupLeftVertical'}">
			<p class="left">
				&nbsp;
			</p>
		</c:if>
	    <label for="${fieldId}${radioIndex}" class="${optionsAlignment}" style="display:none">
	        <%-- for "missingDataCode" button, use a value that can be converted to a numeric, in case
	             the property is numeric --%>
			<input type="radio" id="${fieldId}${radioIndex}" name="${status.expression}" value="-9999" class="${styleClass}"
				<c:if test="${!hasValue}">
				   checked
				</c:if>
				${attributesText}
			>
			Other :
        </label>
		<c:if test="${optionsAlignment == 'groupLeftVertical'}">
			<p class="left">
				&nbsp;
			</p>
		</c:if>
	</c:if>        

   	<%-- display the select box with codes and the blank entry
   	     NOTE: the select box is still created even when there are no codes but not displayed. this is
   	           for javascript which relies on the existence of the select box (the select box will only
   	           have the blank entry because there are no codes) --%>
	
<%-- issues with calling autoComplete tag from here:
	 0. javascript to coordinate radiobuttons and select does not work: 
	    - clicking radiobutton calls deselectSingleSelectBox but that select box is hidden; need to clear
	       the acs_textbox_ as well and who knows what if anything else
	    - choosing autoselect does not work presumably because onChange event not getting to the selectbox
	       which would call deselectRadio
	 0a. IE is flagging error on the hidden <select> onChange='deselectRadio('think') for each property
	     (this does not happen on other pages, because they do not have attributes for their autoselect select
	      box)
	     UPDATE: since this writing, deselectRadio is passed the fieldId, and field names use HashMapCommand
	             object syntax
     1. need to create the list of missing data codes because autocomplete just uses the full list and can not 
        do filtering out like we do here to just get the blank entry and missing data code entries from original
        list
     2. we have already done spring:bind, and autocomplete does it again. probably no harm done
     3. need to pass in additional attribute, '_CODE' (fieldIdSuffix) because autoComplete takes the name for its 
        select field from spring:bind status.expression, which is the name for the radio buttons, so it needs to 
        append _CODE to that (i.e. append selectNameSuffix if not empty) (<select> name and id do not need to be
        equal (see below example with patientMudsDemog), but id is important and the same id must be used as the
        root of all element id's, which are then differentiated via acs_...prefixes)
        note: fieldId is almost always the same thing as spring:bind status.expression, as that is what the generated
              fieldId is set to in createField if none supplied. the only place that supplies a fieldId as of this
              writing is filterContext.jsp and that seems to be because one of the properties is nested, patient.fullName
              and the autoselect tag can not use a name with a '.' character for an id 
              so perhaps, could not have a fieldId and use status.expression for the fieldId, replacing '.' with '_'
               for example 
              Joe is getting around this a lot by making the "entity" portion of the bindProperty nested, e.g.
               'patient.patientMudsDemog', and the "property" as just the property, e.g. 'race', so his generated
               fieldId is the "property" which is just 'race', whereas the spring:bind status.expression is
               'patientMudsDemog.race' (meaning that <select name='patientMudsDemog.race' diff than id='race')
               and changing all of his stuff so entity is the primary entity and property contains all nesting
               syntax would presumably break the autoselect. THIS PROBABLY EXPLAINS why he is always using the
               entity for the nesting and last property just for property.
               
     4. since pass in radiobutton field as property so spring:bind will succeed, it will use the radiobutton value
         and add it to the selectbox and select this, so need to alter this behavior in autoselect tag --%>

<%--	
	<% Map widgetList = (Map) jspContext.getAttribute("list");
	   Map missingCodesMap = (Map) request.getAttribute("missingCodesMap");
	   Map codesList = new LinkedHashMap();
	   for (Iterator iter = widgetList.entrySet().iterator(); iter.hasNext(); ) {
			Entry entry = (Entry) iter.next();
	        // only include list items that are codes or the blank list item
			if (((String) entry.getKey()).equals("") || missingCodesMap.containsKey(entry.getKey())) {
				codesList.put(entry.getKey(), entry.getValue());
			}
	   }
	   jspContext.setAttribute("codesList", codesList);
	   String bindProperty = (String) jspContext.getAttribute("property");
	   jspContext.setAttribute("fieldId", bindProperty.substring(bindProperty.lastIndexOf(".")+1));
    %>	
--%>    
    
<%--         
		<tags:autoComplete property="${property}" attributesText="onchange='deselectRadio('${fieldId}')'" list="${codesList}" fieldId="${fieldId}_CODE" selectNameSuffix="_CODE" styleClass="${styleClass}" />
--%>		
	<c:set var="styleAttribute" value=""/>
    <c:if test="${!listHasCodes}">
      	<c:set var="styleAttribute" value="style='display:none'"/>
    </c:if>
    <select id="${fieldId}_CODE" name="${status.expression}_CODE" autocomplete="off" class="${styleClass}" onchange="deselectRadio('${fieldId}')" ${attributesText} ${styleAttribute}>
   	    <c:forEach items="${list}" var="entry">
            <%-- only include list items that are codes or the blank list item --%>    	        
    		<c:if test="${(entry.key == '') or (not empty missingCodesMap[entry.key])}">
           	    <option value="${entry.key}"
 	               	    <c:if test="${status.value == entry.key}">selected</c:if>>
                   	${entry.value}
	            </option>
	        </c:if>
       	</c:forEach>
    </select>

	<c:set var="errorFlag" value="${status.error}"/>
    <c:set var="errorMessages" value="${status.errorMessages}"/>
  </div>
</spring:bind>

