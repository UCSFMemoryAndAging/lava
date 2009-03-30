<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ tag body-content="empty" %>

<%-- listItemSelect
	Abstracts out the implementation of list item select checkbox from the create field
	statement and from the source jsp files.   
--%> 

<%@ attribute name="component" required="true" 
       description="All lists are implemented in components" %>
<%@ attribute name="entityType" 
       description="Used to create the metaDataName property to lookup metadata for the filter field
       				the format of the metaDataName will be filter.$entityType.$property. If not specified, just use filter.$property"%>
<%@ attribute name="mode"
       description="[optional] a mode to use to override the page mode"%>
<%@ attribute name="listIndex" required="true"
       description="list index for entities in a list. Allows unique ID properties to be generated as 'property_[colindex]'"%>

<tags:createField property="pageList[${listIndex}].selected"
		component="${component}"
		metadataName="${empty entityType ? '':entityType}${empty entityType?'':'.'}selected"
                mode="${mode}"
                />


	