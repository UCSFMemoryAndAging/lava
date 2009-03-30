<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%@ tag import="java.util.List" %>
<%@ tag import="java.util.ArrayList" %>

<%@ tag body-content="empty" %>

<%-- note: tag files have access to the request, session and servletContext. As such, they have access
           to the model. Specifically, the model objects used here are:
           - ${component}_mode
             'dc' = data collection mode
             'de' = data entry mode
             'vw' = view mode
             'lv' = list view mode
             'le' = list edit mode
           - ${component}_view
           - staticLists
           - dynamicLists
           - missingCodesMap 
           
           NOTE: the ${component}_mode model object may be overridden by passing in a mode attribute, which
                 takes precedence. this is useful for pages which exhibit multiple modes.
--%>         

<%-- createField

   Generate the HTML to create a field for a given property. A field generally consists of a
   label, and a data element, either some kind of input element or readonly text. 
   
   If the property attribute specifies more than one property, the data elements for each property
   are created one after the other (separated by the string passed in as the separator attribute), and
   the label from the last property is used.
   
   The kind of data element to create is based on the following:
     - the current view "mode", obtained from the "mode" String in the model, or optionally
       overridden by the "mode" attibute
     - the "style" metadata value for the property
       style of the property for which a field is being created, where the style represents the 
        nature of the values that the property can assume, i.e. style represents several structures
        which characterize the values which a property can assume, as follows:
		     "scale" = list of less than N possible values, where N = ?
		             technically split between continuous (e.g. 1..10) and non-continuous
		             (e.g. 0,0.5,1,2,3,4), but for our purposes, based on the maximum number of possible
		             values N
		     "range" = list of greater than N possible values, where N = ?
		     "suggest" = a list of suggested values, a user supplied value is acceptable. 
		     "string" = value less than 50 chars
		     "numeric" = numeric value, treated same as "string" except right aligned
		     "text" = value greater than 50 characters  
		     "date" = date value
		     "datetime" = date with time value
		     "toggle" = boolean value
		     "multiple" = list from which multiple values can be selected (the data is bound
		     	as a comma-separated string)
		     
		A given "style" can result in different presentation based on the current mode.
		     
     - "context" metadata value describes the context of the property
         "c" = contextual data, i.e. readonly display data, e.g. the current patient name
         "i" = informational data, e.g. instrument data collection quality issues
         "r" = result data, i.e. instrument data
         "h" = hidden field, e.g. a hidden input. 
         
       note about "r": for some styles, result context is used to determine which input control
         to use, e.g. for style=scale, autoComplete is used, but in collect mode ('dc'), a comboRadioSelect
         is used. 
         additionally, all result context fields have two input fields on the instrument
         "enter" flow, compare page (whereas context "i" fields only have one, i.e. for fields
         that should never be involved in the compare), with the exception of those with the
         "disabled" within the value of the widgetAttributes metadata, whch is typically a
         non-editable total and should only appear once on double enter compare

   Additionally, other metadata values may be used when creating the field:
     - "label" is a String of the label to use for the field 
     - "indentLevel" an integer (as String) the indent level for the field, where "0" means
                     do not indent, "1" means indent one level, etc.  e.g. if the field is 
                     question 3a. it should be indented from question 3 and its indentLevel=1
     - "widgetListName" the name of the list for fields represented by an input widget that
                        use a list. the name is used as the key into the "staticLists"
                        or "dynamicLists" Maps in the model to obtain a list which is a
                        Map<String,String> structure where the Map entry keys are the list item
                        values and the Map entry values are the list item labels
     - "widgetAttributes" for fields represented by an input widget, the attributes as a String
                          that are used as HTML attributes for the input widget HTML tag, e.g.
                          the HTML attributes for a textarea: rows="14" cols="40"
     - "required" whether the property is a required field, i.e. purely whether the property label 
                  should visually indicate that the field is a required field ("Yes" or "No"). the
                  enforcement of required fields is done in the controllers/handlers and is 
                  completely independent this metadata field.
     - "size"     set the size of widgets that use an HTML text box, i.e. style=string, numeric, date,
                  datetime, scale, range, suggest
		          note: the size of style=text widgets can be set using rows and 
		          cols in the widgetAttributes (attribues column) metadata, or alternatively, by 
		          creating a style with sizing and passing it into the dataStyle attribute of this 
		          tag,  e.g. the "instrNote" style
      - "maxlength" used to set the max length of text in widgets where user can type text, i.e.
                  style=string, suggest, text
   
   The tag file attributes are used to construct the key to obtain the value for each metadata
   attribute of a given property.
   
--%> 

<%@ attribute name="property" required="true" 
       description="the name of the property, which could be compound, e.g. given the entity 'cdr', the 
                    property could be 'think' or 'visit.patient.fullName'
                    it can contain a comma-separated list of properties, in which case the data element for each 
                    property is output, separated by the string in the separator attribute, and the label from the 
                    first property is used. e.g. this can be used for putting multiple data elements together with
                    a single label (see month/year example in udsinformantdemo)." %>
<%@ attribute name="section"
       description="[optional] if the property is within a section, the section name is specified, e.g. 'orientation'
                    section is a concept only known to the view - since much of the metadata for a property
                    concerns the view, it is useful to define attributes at the section level instead
                    of the property level if many properties in a section share the same attribute values" %>
<%@ attribute name="component" 
       description="[optional] when specified, the component overrides binding behavior to work with component based
       				controllers, and also may be used to form part of the lookup key for retrieving field metadata" %>
<%@ attribute name="entity"
       description="[optional] the name of the entity domain object to which the property belongs used for retrieving 
       				metadata. if specified, it overrides the use of the component attribute for creating the metadata
       				lookup key" %>
<%@ attribute name="entityType"
       description="[optional] the type of the entity domain object to which the property belongs, e.g. 'instrument',
        			used for retrieving metadata"%>
<%@ attribute name="mode"
       description="[optional] a mode to use to override the page mode"%>
<%@ attribute name="context"
       description="[optional] context to use to override the metadata context"%>
<%@ attribute name="metadataName"
       description="[optional] override of the property portion of the key used to look up 
                    metatdata attributes (useful when the property name is too complex to be a lookup key).
                    if multiple properties are specified for the property attribute, multiple names can be specified
                    here which must correlate 1:1 with the list of property names."%>
<%@ attribute name="fieldId"
       description="[optional] an explicitly specified ID for the input control"%>
<%@ attribute name="fieldStyle"
       description="[optional] CSS style to use for div block. this is not an override, it is used
                    together with the standard 'field' style (assuming it has the same specificity as
                    the 'field' rule in the stylesheet)."%>
<%@ attribute name="labelAlignment"
       description="[optional] override the computed label alignment. valid values:'left' 'top' 'right'
        			for anything beyond these, use labelStyle"%>
<%@ attribute name="labelStyle"
       description="[optional] CSS style to use for label CSS style. this is not an override, it
                    is used in addition to the computed label style (assuming rules have the same level
                    of specificity in the stylesheet). Use this if labelAlignment does not cut it."%>
<%@ attribute name="optionsAlignment"
       description="[optional] override the computed options alignment for radio buttons groups. 
                    valid values:'groupLeftVertical', 'groupTopVertical', 'horizontal' (which
                    applies to both group label to left or on top)"%>
<%@ attribute name="dataStyle"
	   description="[optional] CSS style to use for the data element, in combination with the default
	                'inputData' or 'inputDataNumeric' or 'readonlyData' or 'readonlyDataNumeric' style
	                (assuming that the specificity of the rules in the stylesheet are the same)"%>
<%@ attribute name="disable" type="java.lang.Boolean"
	   description="[optional] value 'true' will disable the data element (has no effect on readonly
	                data). defaults to 'false'.
	                note: to make the data element invisible, set the fieldStyle attribute to 'invisible' 
	                      (no screen real estate used) or 'hidden' (screen real estate still used)"%>
<%@ attribute name="separator"
	   description="[optional] output to separate the property data elements when more than one property
                        is specified in the property attribute (see description for property attribute)." %>



	<%-- various initializations prior to outputting anything --%>

	<%-- set flag to denote if multiple properties are being output --%>
	<c:forTokens items="${property}" delims="," var="current" varStatus="status">
		<c:if test="${status.last && status.index > 0}">
			<c:set var="multipleProps" value="true"/>
			<%-- if multiple properties, want them to flow naturally inline, so set a flag to make them inline, not block 
					note: only pertains to readonly data which is normally block so it wraps properly. input elements do not
			    		  wrap so they are inlne --%>
			<c:set var="inlineReadonlyData" value="true"/> 
		</c:if>
	</c:forTokens>

	<%-- metadata is most often associated with the key ${component}.${currentProperty}
	     however, for legacy reasons, keys based on ${entity} and ${entityType} can be used to retrieve metadata. these
	     are optional and ${entity} defaults to ${component} if not specified. additionally, the optional ${metadataName} 
	     attribute can be used for retrieving metadata using a specific lookup key --%>
	<c:if test="${empty entity}">
		 <c:set var="entity" value="${component}"/>
	</c:if>
	     
	<%-- in component design, mode is component specific, so determine the mode from the specified component --%>
	<c:if test="${empty mode}">
		<c:set var="mode_string" value="${component}_mode"/>
		<c:set var="mode" value="${requestScope[mode_string]}"/>
	</c:if>

	<%-- obtain the componentView --%>
	<c:set var="view_string" value="${component}_view"/>
	<c:set var="componentView" value="${requestScope[view_string]}"/>


	<%-- if instrument data entry, setup to iterate over property twice --%>
	<c:set var="inputFields" value="1"/> 
	<c:if test="${componentView == 'compare'}">
	    <c:set var="inputFields" value="2"/>
	</c:if>

	<%-- since it is possible to output data elements for multiple properties, and since error messaging
	     is not done until all data elements are output, need to store the error messages and then output
	     them at the end. to achieve this, drop into JSP expressions to create a list and then during iteration,
	     add error messages to that list as they are detected --%>
	<c:set var="anyErrors" value="false"/>
	<% List allErrorMessages = new ArrayList();
	   jspContext.setAttribute("allErrorMessages", allErrorMessages, PageContext.PAGE_SCOPE); %>



	<%-- begin outputting HTML --%>
	<%-- note that with the exception of instrument double enter compare, and when multiple properties
	     are output with a single label, the vast majority of the time there is just a single iteration
	     of the two loops below to output a single property label and value --%>
	   
	<%-- this loop is used to support double enter when there are two instances of each property --%>   
	<c:forEach begin="1" end="${inputFields}" varStatus="status">

		<%-- this loop supports outputting multiple property data elements with a single label --%>
		<c:forTokens items="${property}" delims="," var="currentBindingProperty" varStatus="propertyStatus">
			
			<%--create an escaped copy of the current property to use for field id's, metadata lookups, etc.  --%>
			<c:set var="currentProperty"><tags:escapeProperty property="${currentBindingProperty}"/></c:set>
			
			<%-- obtain metadata for the property --%>
			<c:if test="${not multipleProps}">
				<c:set var="currentMetadataName" value="${metadataName}"/>			
			</c:if>
			<c:if test="${multipleProps}">
				<%-- determine if multiple metadataNames have been specified, and if so, choose the one
				     to use for the current property --%>						
				<c:forTokens items="${metadataName}" delims="," var="thisMetadataName" varStatus="metadataNameStatus">
					<c:if test="${metadataNameStatus.count == propertyStatus.count}">
						<c:set var="currentMetadataName" value="${thisMetadataName}"/>
					</c:if>						
				</c:forTokens>
			</c:if>					                				     
			
			<%-- label-related metadata is only needed for the first property, since there is only one label --%>
			<c:if test="${status.first && propertyStatus.first}">
				<tags:metadataValue attribName="label" property="${currentProperty}" section="${section}" 
				                    entity="${entity}" entityType="${entityType}" metadataName="${currentMetadataName}" defaultVal=""/>
			    <%-- indentLevel metadata is set in the metadata.properties file, not in the ViewProperty table --%>
				<tags:metadataValue attribName="indentLevel" property="${currentProperty}" section="${section}" 
				                    entity="${entity}" entityType="${entityType}" metadataName="${currentMetadataName}" defaultVal="0"/>
				<tags:metadataValue attribName="required" property="${currentProperty}" section="${section}" 
				                    entity="${entity}" entityType="${entityType}" metadataName="${currentMetadataName}" defaultVal="No"/>
			</c:if>			
			
			<tags:metadataValue attribName="style" property="${currentProperty}" section="${section}" 
			                    entity="${entity}" entityType="${entityType}" metadataName="${currentMetadataName}" defaultVal="string"/>
			<c:if test="${empty context}"> <%-- context can be overridden by an attribute --%>
				<tags:metadataValue attribName="context" property="${currentProperty}" section="${section}" 
			                    entity="${entity}" entityType="${entityType}" metadataName="${currentMetadataName}" defaultVal="i"/>
			</c:if>	                   	


			<%-- note: even numeric style may have a list to translate the numeric value to a text value for display --%>
			<tags:metadataValue attribName="listRequestId" property="${currentProperty}" section="${section}" 
			                      entity="${entity}" entityType="${entityType}" metadataName="${currentMetadataName}" defaultVal=""/>
			
			<tags:metadataValue attribName="listName" property="${currentProperty}" section="${section}" 
			                      entity="${entity}" entityType="${entityType}" metadataName="${currentMetadataName}" defaultVal=""/>
			                      
			<%-- use the listRequestId or listName to obtain its list data structure, which is of type java.util.Map<String,String> --%>			   
			<c:if test="${not empty listRequestId || not empty listName}">
				<%--first try to find a static list--%>			
			    <c:set var="widgetList" value="${lists[listRequestId]}"/>
				<%--if not found try to find a dynamic list (only use listName because configuration parameters only set at runtime -- not in metadata) --%>
				<c:if test="${empty widgetList}">
					<c:set var="widgetList" value="${dynamicLists[listName]}"/>
				</c:if>        
			</c:if>
			
			<tags:metadataValue attribName="widgetAttributes" property="${currentProperty}" section="${section}" 
	                    entity="${entity}" entityType="${entityType}" metadataName="${currentMetadataName}" defaultVal=""/>
			<c:if test="${disable}">
				<c:set var="widgetAttributes" value="${widgetAttributes} disabled"/>
			</c:if>
			<%-- used to set the max length of text that can be input in textBox and textarea widgets --%>
			<tags:metadataValue attribName="maxTextLength" property="${currentProperty}" section="${section}" 
	                    entity="${entity}" entityType="${entityType}" metadataName="${currentMetadataName}" defaultVal=""/>
		    <%-- used to set the size of widgets that use an HTML text box, i.e. textBox, dateTextBox, datetimeTextBox,
		         autoComplete and autoCompleteSuggest.
		         note: the size of textarea widgets can be set using rows and cols in the widgetAttributes (attribues column)
                         metadata, or alternatively, by creating a style with sizing and passing it into the dataStyle attribute of 
                         this tag,  e.g. the "instrNote" style 
                 also used to size the number of options displayed in a multiple select box --%>
			<tags:metadataValue attribName="textBoxSize" property="${currentProperty}" section="${section}" 
	                    entity="${entity}" entityType="${entityType}" metadataName="${currentMetadataName}" defaultVal=""/>
	                    
			<%-- debugging output of metadata values and mode --%>
			<%--
		    <p>
		    currentMetadataName=${currentMetadataName}<br>
		    mode=${mode}<br>
		    showCodes=${showCodes}<br>
		    style=${style}<br>
		    context=${context}<br>
		    label=${label}<br>
		    indentLevel=${indentLevel}<br>
		    required=${required}<br>
		    widgetListName=${widgetListName}<br>
            widgetAttributes=${widgetAttributes}<br>
   		    maxTextLength=${maxTextLength}<br>
   		    textBoxSize=${textBoxSize}<br>
		    </p>
			--%>

			<%-- determine the kind of data element to be used for the data value for this property, e.g. readonlyText,
			     checkbox, autocomplete control, etc.
			     note: knowledge of which kind of data element will be used is also needed for outputting the 
			           label, which is why it is necessary to determine this before outputting the label --%>
			<c:choose>
			    <%-- contextual data fields are displayed readonly --%>
			    <c:when test="${context == 'c'}">
			    	<c:set var="dataElement" value="readonlyText"/>
				</c:when>
				<%-- hidden context handling --%>
			    <c:when test="${context == 'h'}">
			    	<c:set var="dataElement" value="hidden"/>
				</c:when>
				<%-- informational and result data fields are displayed based on a combination of their style 
				     and the current view mode --%>
				<c:when test="${style == 'scale'}">
					<c:choose>
				    	<c:when test="${mode == 'dc'}">
				    		<c:choose>
					    		<c:when test="${context == 'r'}">
					    		    <%-- result context is specific to instruments --%>
							    	<c:set var="dataElement" value="comboRadioSelect"/>
							    </c:when>
							    <c:otherwise>
							    	<c:set var="dataElement" value="radioButtons"/>
							    </c:otherwise>
							 </c:choose>
			    		</c:when>
			        	<c:when test="${mode == 'de' || mode == 'le'}">
					    	<c:set var="dataElement" value="autoComplete"/>
				        </c:when>
				        <c:when test="${mode == 'vw' || mode == 'lv'}">
		        	    	<c:set var="dataElement" value="readonlyText"/>
			    	    </c:when>
					</c:choose>
				</c:when>
				<c:when test="${style == 'range'}">
					<c:choose>
				    	<c:when test="${mode == 'dc' || mode == 'de' || mode == 'le'}">
					    	<c:set var="dataElement" value="autoComplete"/>
			    		</c:when>
				        <c:when test="${mode == 'vw'|| mode == 'lv'}">
		        	    	<c:set var="dataElement" value="readonlyText"/>
			    	    </c:when>
					</c:choose>
				</c:when>
				<c:when test="${style == 'suggest'}">
					<c:choose>
				    	<c:when test="${mode == 'dc' || mode == 'de' || mode == 'le'}">
					    	<c:set var="dataElement" value="autoCompleteSuggest"/>
			    		</c:when>
				        <c:when test="${mode == 'vw'|| mode == 'lv'}">
		        	    	<c:set var="dataElement" value="readonlyText"/>
			    	    </c:when>
					</c:choose>
				</c:when>
				<c:when test="${style == 'multiple'}">
			    	<c:set var="dataElement" value="multipleSelect"/>
				</c:when>
				<c:when test="${style == 'string' || style == 'numeric'}">
					<c:choose>
				    	<c:when test="${mode == 'dc' || mode == 'de' || mode == 'le'}">
		        	    	<c:set var="dataElement" value="textBox"/>
			    		</c:when>
				        <c:when test="${mode == 'vw' || mode == 'lv'}">
		        	    	<c:set var="dataElement" value="readonlyText"/>
			    	    </c:when>
					</c:choose>
				</c:when>
				<c:when test="${style == 'text'}">
					<c:choose>
				    	<c:when test="${mode == 'dc' || mode == 'de' }">
		        	    	<c:set var="dataElement" value="textarea"/>
			    		</c:when>
				        <c:when test="${mode == 'vw'}">
		        	    	<c:set var="dataElement" value="textarea"/>
			    	    </c:when>
			    	    <%-- for list modes (view and edit), do not want textarea box in list, so 
			    	         effectively truncate as readonlyText or textBox --%>
				        <c:when test="${mode == 'lv'}">
		        	    	<c:set var="dataElement" value="truncatedReadonlyText"/>
			    	    </c:when>
				        <c:when test="${mode == 'le'}">
		        	    	<c:set var="dataElement" value="textarea"/>
			    	    </c:when>
					</c:choose>
				</c:when>
				<c:when test="${style == 'unlimitedtext'}">
					<c:choose>
				    	<c:when test="${mode == 'dc' || mode == 'de' }">
		        	    	<c:set var="dataElement" value="unlimitedTextarea"/>
			    		</c:when>
				        <c:when test="${mode == 'vw'}">
		        	    	<c:set var="dataElement" value="unlimitedTextarea"/>
			    	    </c:when>
			    	    <%-- for list modes (view and edit), do not want textarea box in list, so 
			    	         effectively truncate as readonlyText or textBox --%>
				        <c:when test="${mode == 'lv'}">
		        	    	<c:set var="dataElement" value="truncatedReadonlyText"/>
			    	    </c:when>
				        <c:when test="${mode == 'le'}">
		        	    	<c:set var="dataElement" value="unlimitedTextarea"/>
			    	    </c:when>
					</c:choose>
				</c:when>
				<c:when test="${style == 'date'}">
					<c:choose>
				    	<c:when test="${mode == 'dc' || mode == 'de' || mode == 'le'}">
				    	    <%-- TODO: create date widget element and tag file --%>
		        	    	<c:set var="dataElement" value="dateTextBox"/>
			    		</c:when>
				        <c:when test="${mode == 'vw'|| mode == 'lv'}">
				            <%-- TODO: format the date appropriately. probably need another input element type
				                       and tag file called readonlyDate --%>
		        	    	<c:set var="dataElement" value="readonlyText"/>
			    	    </c:when>
					</c:choose>
				</c:when>
				<c:when test="${style == 'datetime'}">
					<c:choose>
				    	<c:when test="${mode == 'dc' || mode == 'de' || mode == 'le'}">
				    	    <%-- TODO: create date widget element and tag file --%>
		        	    	<c:set var="dataElement" value="datetimeTextBox"/>
			    		</c:when>
				        <c:when test="${mode == 'vw'|| mode == 'lv'}">
				            <%-- TODO: format the date appropriately. probably need another input element type
				                       and tag file called readonlyDate --%>
		        	    	<c:set var="dataElement" value="readonlyText"/>
			    	    </c:when>
					</c:choose>
				</c:when>
				<c:when test="${style == 'toggle'}">
				   	<c:set var="dataElement" value="checkbox"/>
				</c:when>
			</c:choose>		
			
			
			<%-- bindProperty and fieldId
			
			    define a variable as needed for binding to the corresponding command object using the
				spring:bind macro, and define a fieldId needed for both the "id" attribute of the data 
				element HTML tag as well as the "for" attribute of the HTML label tag, to associate the 
				HTML label with its form input field --%>
			<c:choose>
				<%-- when on the instrument compare view, there will be two sets for inputs for the result
					fields. the first set should bind to the 'instrument' component of the command object,
					and the second set should bind to 'compareInstrument. note that the component attribute 
					will have been passed in with value 'instrument' for the instrument compare view. the
					loop iteration determines which component to use. --%>
				<c:when test="${component == 'instrument' && componentView == 'compare'}">
					<c:choose>
						<%-- there is a special situation for the contextual fields, which only have one set of visual
							fields (and are readonly). for the 'compareInstrument' component, these fields have not been 
							fully initialized so can only bind to them on the 'instrument' component. contextual fields 
							are distinguished by presence of entityType attribute --%>
						<c:when test="${status.count == 1 || entityType == 'instrument'}">
							<c:set var="bindProperty" value="command.components['instrument'].${currentBindingProperty}"/>
							<c:set var="fieldId" value="instrument_${currentProperty}"/>
						</c:when>
						<c:when test="${status.count == 2}">
							<c:set var="bindProperty" value="command.components['compareInstrument'].${currentBindingProperty}"/>
							<c:set var="fieldId" value="compareInstrument_${currentProperty}"/>
						</c:when>			
					</c:choose>
				</c:when>

				<%-- standard component based binding --%>
				<c:when test="${not empty component}">
					<%-- to accomodate entity components which are lists, do not include the '.' after the component []
					     e.g. studyKitTracking is a list, and currentBindingProperty could be [0].siteRec, so the
					     bindProperty should be "command.components['studyKitTracking'][0].siteRec --%>
					<c:choose>
						<c:when test="${fn:startsWith(currentBindingProperty, '[')}">
							<c:set var="bindProperty" value="command.components['${component}']${currentBindingProperty}"/>
						</c:when>
						<c:otherwise>
							<c:set var="bindProperty" value="command.components['${component}'].${currentBindingProperty}"/>
						</c:otherwise>
					</c:choose>													
					<%-- the fieldId could be supplied as an attribute to override the value generated here, so only 
						define it if it has not been supplied. note: do not currently support multiple fieldId's 
						supplied as attributes corresponding to multiple property's supplied as attributes --%>
					<c:if test="${empty fieldId}">
						<c:set var="fieldId" value="${component}_${currentProperty}"/>
					</c:if>
				</c:when>	
			</c:choose>		
			

			<%-- output div block and field label ONLY on the first iteration of these loops --%>		

			<%-- this is not done until now, within the loops that iterate over properties (i.e. double enter properties,
 			     and multiple properties), because the HTML for the block and label depends upon the type of data element which
			     will be output, and this is not known until now because now the metadata has been read for the
			     property (if there are multiple data elements, the first data element is used to make determinations 
			     on the HTML used for block and label output) --%>
			<c:if test="${status.first && propertyStatus.first}">
	
				<%-- output div field block --%>
			    <c:choose>
					<%-- note that if multiple class styles are used, and their rules in the stylesheet have the same specificity,
					     the latter takes precendence --%>
					<%-- if making the field invisible via 'invisible' fieldStyle, must use 'invisible' class by itself because
					     the 'field' class has more specificity and its display:block trumps 'invisible' display:none. also, if
					     field invisible and will not take up any screen real estate, standard field styling does not matter anyway --%>
					<c:when test="${fieldStyle == 'invisible'}">
						<div class="${fieldStyle}">
					</c:when>
					<%-- special case for comboRadioSelect when inside of a list cell, because the dropdown does not work when
						inside of a float div for some reason (and assume do not need floating div in this case because usually
						there is no label). use a field class style that does not float, 'comboRadioSelectInList' --%>
					<c:when test="${dataElement == 'comboRadioSelect' && fieldStyle == 'fieldComboRadioSelectInList'}">
						<div class="fieldComboRadioSelectInList cr">
					</c:when>
					<c:when test="${dataElement == 'comboRadioSelect' || dataElement == 'radioButtons'}">
						<div class="field cr ${not empty fieldStyle ? fieldStyle : ''} ${indentLevel == 1 ? 'indentField' : ''}">
					</c:when>
					<c:otherwise>
			            <div class="field ${not empty fieldStyle ? fieldStyle : ''} ${indentLevel == 1 ? 'indentField' : ''}">
					</c:otherwise>
			  	</c:choose>
			
			
				<%-- output field label --%>    
				<%-- note: this creates an "optionsAlignment" variable used in radio/checkbox data element creation tags called below, and
			        	   an "alignment" variable, used in readonlyText tag below --%>
				<%-- note: even if label is empty, call this to put the &nbsp; where the label belongs --%>
				<tags:fieldLabel fieldId="${dataElement == 'autoComplete' || dataElement == 'autoCompleteSuggest' ? 'acs_textbox_' : ''}${fieldId}" label="${label}" indentLevel="${indentLevel}" mode="${mode}" context="${context}" dataElement="${dataElement}" requiredField="${required}" labelAlignment="${labelAlignment}" labelStyle="${labelStyle}" inOptionsAlignment="${optionsAlignment}"/>
				
			</c:if>
		

			<%-- special handing for instrument double enter compare view:
				 all context 'r' fields should be displayed with two input fields, with the exception of those with
				 "disabled" in its widgetAttributes, in which case the field is not editable (e.g. a calculated total)
				 and should not appear twice.
				 note that the set of fields that are actually compare is determined by getRequiredResultFields method
				 in the specific instrument model class. this method may use logic to conditionally set required fields,
				 e.g. if totalOnly checkbox is checked, it is only necessary to compare the total fields (although in 
				 this example it would not hurt to also compare the individual result fields since skip logic has 
				 probably set them all to the same skip value and they woulc compare equally). --%>
			<c:if test="${componentView == 'compare' && status.count == 2 && (context == 'i' || fn:contains(widgetAttributes, 'disabled')) }">
				<c:set var="dataStyle" value="${dataStyle} invisible"/>
			</c:if>


			<%-- output the data element --%>
			<%-- for instrument compare, do not output data element for 'c' fields, since it would be redundant. 
			     'i' data fields are created but are invisible per above, so only 'r' fields appear twice --%>
			<c:if test="${status.count == 1 || (status.count == 2 && context != 'c')}">
				<c:choose>	
					<c:when test="${dataElement == 'readonlyText'}">	
						<tags:readonlyText property="${bindProperty}" alignment="${alignment}" list="${widgetList}" isDate="${style == 'date' ? 'true' : false}" styleClass="${style == 'numeric' ? 'readonlyDataNumeric' : 'readonlyData'} ${not empty dataStyle ? dataStyle :''}" inlineData="${inlineReadonlyData}"/>
					</c:when>
					<c:when test="${dataElement == 'truncatedReadonlyText'}">	
						<tags:truncatedReadonlyText property="${bindProperty}" alignment="${alignment}" list="${widgetList}" isDate="${style == 'date' ? 'true' : false}" styleClass="${style == 'numeric' ? 'readonlyDataNumeric' : 'readonlyData'} ${not empty dataStyle ? dataStyle :''}" inlineData="${inlineReadonlyData}"/>
					</c:when>
					<c:when test="${dataElement == 'textBox'}">
			   			<tags:textBox property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}" styleClass="${style == 'numeric' ? 'inputDataNumeric' : 'inputData'} ${not empty dataStyle ? dataStyle :''}" textBoxSize="${textBoxSize}" maxLength="${maxTextLength}"/>
					</c:when>
					<c:when test="${dataElement == 'dateTextBox'}">
	   					<tags:dateTextBox property="${bindProperty}"  fieldId="${fieldId}" attributesText="${widgetAttributes}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}" textBoxSize="${textBoxSize}"/>
					</c:when>
					<c:when test="${dataElement == 'datetimeTextBox'}">
	   					<tags:datetimeTextBox property="${bindProperty}"  fieldId="${fieldId}" attributesText="${widgetAttributes}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}" textBoxSize="${textBoxSize}"/>
					</c:when>
					<c:when test="${dataElement == 'textarea'}">
	   			    	<tags:textarea property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}${mode == 'vw'? ' readonly':''}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}" maxLength="${maxTextLength}"/>
					</c:when>
					<c:when test="${dataElement == 'unlimitedTextarea'}">
	   			    	<tags:unlimitedTextarea property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}${mode == 'vw'? ' readonly':''}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}"/>
					</c:when>
					<c:when test="${dataElement == 'singleSelect'}">
						<tags:singleSelect property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}" list="${widgetList}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}"/>
					</c:when>
					<c:when test="${dataElement == 'autoComplete'}">
		   				<tags:autoComplete property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}" list="${widgetList}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}" textBoxSize="${textBoxSize}"/>
					</c:when>
					<c:when test="${dataElement == 'autoCompleteSuggest'}">
		   				<tags:autoCompleteSuggest property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}" list="${widgetList}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}" textBoxSize="${textBoxSize}" maxLength="${maxTextLength}"/>
					</c:when>
					<%-- for multipleSelect the textBoxSize for select boxes is set in pixels, so the value passed in is multipled by 10. maxLength is passed in as the number of options to display in the select box at one time. --%>
					<c:when test="${dataElement == 'multipleSelect'}">
					    <tags:multipleSelect property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}" list="${widgetList}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}" size="${textBoxSize}" length="${maxTextLength}"/>
					</c:when>
					<c:when test="${dataElement == 'radioButtons'}">
						<tags:radioButtons property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}" list="${widgetList}" optionsAlignment="${optionsAlignment}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}"/>
					</c:when>
					<%-- used when there are multiple checkboxes corresponding to a list --%>		
					<c:when test="${dataElement == 'checkboxes'}">
						<tags:checkboxes property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}" list="${widgetList}" optionsAlignment="${optionsAlignment}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}"/>
					</c:when>
					<%-- used when there is a single checkbox representing a flag --%>
					<c:when test="${dataElement == 'checkbox'}">
						<tags:checkbox property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}${mode == 'vw'?' disabled':''}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}"/>
					</c:when>
					<c:when test="${dataElement == 'comboRadioSelect'}">
						<tags:comboRadioSelect property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}" list="${widgetList}" optionsAlignment="${optionsAlignment}" styleClass="inputData ${not empty dataStyle ? dataStyle :''}"/>
					</c:when>
					<c:when test="${dataElement == 'hidden'}">
			   			<tags:hiddenInput property="${bindProperty}" fieldId="${fieldId}" attributesText="${widgetAttributes}"/>
					</c:when>
				</c:choose>		
			</c:if>

			<%-- if outputting multiple data elements, output the separator after each data element except for the last --%>			
			<c:if test="${!propertyStatus.last}">
				${separator}
			</c:if>
			
			<%-- errorFlag is an output variable from the data element tags --%>
			<c:if test="${errorFlag}"> 
				<c:set var="anyErrors" value="true"/>
				<c:forEach items="${errorMessages}" var="errorMessage">
					<% allErrorMessages.add(jspContext.getAttribute("errorMessage")); %>
				</c:forEach>	
			</c:if>	
			
			<%-- reset the fieldId for the next iteration --%>
			<c:set var="fieldId" value=""/>
		</c:forTokens>		
		<%-- instrument double enter compare, add a line break so secondary value is on next line.
		     note: this may not be neccessary depending upon the label styling. e.g label.right has a width that spans
		     the page, so the next data element is automatically put onto the next line. this is the typical case
		     with autoComplete elements, and these are the most common type of data element used by instruments
		     in enter mode. therefore, do not add the line break for the autoComplete data elements --%>
		<c:if test="${componentView == 'compare' && context == 'r' && dataElement != 'autoComplete' && dataElement != 'autoCompleteSuggest' && status.count == 1 && !status.last}">		     
			<br>
		</c:if>
		<%-- instrument double compare: while the compare field should not have a label as it would be redundant, to align the 
		     secondary field horizontally with the first, output a label and make it hidden so it is not displayed. do not give it 
		     a fieldId since the label for the primary field does.
		     for non-result fields, nothing output for the compare field --%>
		<c:if test="${status.count == 1 && !status.last && context == 'r'}">
			<tags:fieldLabel fieldId="" label="${label}" indentLevel="${indentLevel}" mode="${mode}" context="${context}" dataElement="${dataElement}" requiredField="${required}" labelAlignment="${labelAlignment}" labelStyle="${labelStyle} hidden" inOptionsAlignment="${optionsAlignment}"/>
		</c:if>

		<%-- reset the fieldId for the next iteration --%>
		<c:set var="fieldId" value=""/>
	</c:forEach>

	<%-- field errors displayed for input modes --%>
	<%-- what about errors (warnings) for view/review modes ? could be informative --%>
	<%-- no errors at the field display level for list modes.  Will need to put to the side of the list row. --%>
	<%-- joe modified this to include errors in editable lists at this point we have a couple of tables on data entry 
			screens that use the 'le' mode to hide labels....--%>
	<c:if test="${mode == 'dc' || mode == 'de' || mode=='le'}">
	    <c:if test="${anyErrors}"> 
   		    <tags:fieldErrors errorMessages="${allErrorMessages}"/>
    	</c:if>
	</c:if>

<%-- end of div field block --%>
</div>

    		
