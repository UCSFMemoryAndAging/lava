<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ tag body-content="empty" %>



<%-- listFilterField
	Abstracts out the implementation of list filters from the create field statement 
	and from the source jsp files.  Most of the createField options are not supported.  If 
	any of the options are needed, they can be added. 
--%> 

<%@ attribute name="property" required="true" 
       description="The name of the property in the filter.params[] map'" %>
<%@ attribute name="component" required="true" 
       description="All lists are implemented in components" %>
<%@ attribute name="entityType" 
       description="Used to create the metaDataName property to lookup metadata for the filter field
       				the format of the metaDataName will be filter.$entityType.$property. If not specified, just use filter.$property"%>


<c:set var="escapedProperty"><tags:escapeProperty property="${property}"/></c:set>


<tags:createField property="filter.params[${property}]" 
		component="${component}" 
		metadataName="filter.${empty entityType ? '':entityType}${empty entityType?'':'.'}${escapedProperty}" 
		mode="dc" 
		fieldId="${component}_${empty entityType?'':entityType}_${escapedProperty}"/>
	
