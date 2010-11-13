<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ tag body-content="empty" %>



<%-- listField
	Abstracts out the implementation of list and components from the create field statement 
	and from the source jsp files.  Most of the createField data format options are supported.  
--%> 

<%@ attribute name="property" required="true" 
       description="The name of the property.  properties that are subproperties (e.g. patient.birthDate) will be formated as patient_birthdate for metadata lookups" %>
<%@ attribute name="component" required="true" 
       description="All lists are implemented in components" %>
<%@ attribute name="entityType" 
       description="Used to lookkup the metadata for the property.  The format will be $entityType.$property"%>
<%@ attribute name="metadataName"
       description="[optional] override of the standard metadata lookup format.  Useful when you want to use the standard metadata information for 
       				a subproperty of a entity that is itself a property of the list item (e.g. 'patient.fullName' when patient is a property of the list item"%>
<%@ attribute name="mode"
       description="[optional] a mode to use to override the page mode"%>
<%@ attribute name="listIndex"
       description="[optional] list index for entities in a list. Allows unique ID properties to be generated as 'property_[colindex]'"%>
<%@ attribute name="fieldStyle"
       description="[optional] CSS style to use for div block. this is not an override, it is used
                    together with the standard 'field' style (assuming it has the same specificity as
                    the 'field' rule in the stylesheet)"%>
<%@ attribute name="labelAlignment"
       description="[optional] override the computed label alignment. valid values:'left' 'top' 'right'"%>
<%@ attribute name="labelStyle"
       description="[optional] CSS style to use for label CSS style. this is not an override, it
                    is used in addition to the computed label style (assuming rules have the same level
                    of specificity in the stylesheet)."%>
<%@ attribute name="optionsAlignment"
       description="[optional] override the computed options alignment for radio buttons, checkboxes. 
                    valid values:'groupLeftVertical', 'groupTopVertical', 'horizontal' (which
                    applies to both group label to left or on top)"%>
<%@ attribute name="dataStyle"
	   description="[optional] CSS style to use for the data element, in combination with the default
	                'inputData' or 'inputDataNumeric' or 'readonlyData' or 'readonlyDataNumeric' style
	                (assuming that the specificity of the rules in the stylesheet are the same)"%>


<c:set var="escapedProperty"><tags:escapeProperty property="${property}"/></c:set>
<c:if test="${empty metadataName}">
	<c:set var="metadataName" value="${empty entityType ? '':entityType}${empty entityType?'':'.'}${escapedProperty}"/>
</c:if>

<tags:createField property="pageList[${listIndex}].entity.${property}" component="${component}" entityType="${empty entityType?'':entityType}" 
		metadataName="${metadataName}" mode="${empty mode  ? '':mode}" fieldStyle="${empty fieldStyle ?'':fieldStyle}" labelAlignment="${empty labelAlignment ?'':labelAlignment}" 
		labelStyle="${empty labelStyle ?'':labelStyle}"  optionsAlignment="${empty optionsAlignment ?'':optionsAlignment}" dataStyle="${empty dataStyle ?'':dataStyle}"/>