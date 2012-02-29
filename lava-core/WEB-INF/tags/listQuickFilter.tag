<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ tag body-content="empty" %>

<%-- listField
	Abstracts out the implementation of the quick filter from the singleSelect statement
	
	IMPORTANT: even though the singleSelect tag used does not use metadata, still need to have
	a metadata record for the quickFilter so that the list used to populate it will be accessible
	(the metadata record entity value must match the entity used by the list, and the list value
	should be set to the configured list. no other metadata properties matter) 
--%> 

<%@ attribute name="component" required="true" 
       description="All lists are implemented in components" %>
<%@ attribute name="listItemSource" required="true" 
       description="static list name providing the items in the dropdown list"%>
<%@ attribute name="label" required="false"
       description="left-justified label for the selection box"%>
<%@ attribute name="pageName" required="false"
       description="the name of the page/form, used for POST, not GET"%>

${empty label ? '':label}&nbsp;
<tags:singleSelect property="command.components[${component}].filter.activeQuickFilter" fieldId="activeQuickFilter" 
		list="${lists[listItemSource]}" attributesText="onChange=&quot;document.${empty pageName ? component : pageName}.action='#${component}';submitForm(document.${empty pageName ? component : pageName},'${component}__applyQuickFilter')&quot; "/>
<BR>