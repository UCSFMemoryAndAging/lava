<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%@ tag import="java.util.Map,java.util.HashMap" %>

<%@ attribute name="metadataKey" required="true" 
              description="metadata Cache key for this set of attributes" %>
<%@ attribute name="property" required="true" 
              description="name of the property" %>
<%@ attribute name="section" required="true"  
              description="name of the entity section containing the property" %>
<%@ attribute name="entity" required="true" 
              description="name of the entity containing the property, e.g. 'cdr'" %>
<%@ attribute name="entityType" required="true" 
              description="name the type of entity containing the property, e.g. 'instrument'" %>
<%@ attribute name="metadataName" 
              description="[optional] override of the portion of the key into the metadata that
                           precedes the metadata attribute" %>


<%-- note: defaults could also be set up to come from the resource bundle defaults --%>
<%@ variable name-given="label" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>
<%@ variable name-given="indentLevel" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>
<%@ variable name-given="required" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>
<%@ variable name-given="style" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>
<%@ variable name-given="defaultContext" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>
<%@ variable name-given="listRequestId" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>
<%@ variable name-given="listName" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>
<%@ variable name-given="listAttributes" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>
<%@ variable name-given="widgetAttributes" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>
<%@ variable name-given="maxTextLength" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>
<%@ variable name-given="textBoxSize" variable-class="java.lang.String" scope="AT_END"
              description="value of the label metadata attribute" %>


		


<% 

  

	Map cachedData = (Map)jspContext.getAttribute(metadataKey,PageContext.APPLICATION_SCOPE);
	if(cachedData!=null){
%>
	

	
	<c:set var="label"><%= cachedData.get("label") %></c:set>
	<c:set var="indentLevel"><%= cachedData.get("indentLevel") %></c:set>
	<c:set var="required"><%= cachedData.get("required") %></c:set>
	<c:set var="style"><%= cachedData.get("style") %></c:set>
	<c:set var="defaultContext"><%= cachedData.get("defaultContext") %></c:set>
	<c:set var="listRequestId"><%= cachedData.get("listRequestId") %></c:set>
	<c:set var="listName"><%= cachedData.get("listName") %></c:set>
	<c:set var="listAttributes"><%= cachedData.get("listAttributes") %></c:set>
	<c:set var="widgetAttributes"><%= cachedData.get("widgetAttributes") %></c:set>
	<c:set var="maxTextLength"><%= cachedData.get("maxTextLength") %></c:set>
	<c:set var="textBoxSize"><%= cachedData.get("textBoxSize") %></c:set>
<%		
	}else{
%>
		<tags:metadataValue attribName="label" property="${property}" section="${section}" 
                    entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal=""/>
		    <%-- indentLevel metadata is set in the metadata.properties file, not in the ViewProperty table --%>
				
		<tags:metadataValue attribName="indentLevel"  property="${property}" section="${section}" 
                    entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal="0"/>
		<tags:metadataValue attribName="required"  property="${property}" section="${section}" 
                    entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal="No"/>
	
		<tags:metadataValue attribName="style"  property="${property}" section="${section}" 
                   entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal="string"/>
		<tags:metadataValue attribName="context"   property="${property}" section="${section}" 
			                    entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal="i"/>
		<%-- need to use defaultContext as the variable name to not overwrite the use of context as an overriding variable in the createfield tag --%>
		<c:set var="defaultContext" value="${context}" scope="page"/>
		
		<tags:metadataValue attribName="listRequestId"  property="${property}" section="${section}" 
		             entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal=""/>
	
		<tags:metadataValue attribName="listName"  property="${property}" section="${section}" 
		             entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal=""/>
	
		<tags:metadataValue attribName="listAttributes"  property="${property}" section="${section}" 
		             entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal=""/>
	
		<tags:metadataValue attribName="widgetAttributes"  property="${property}" section="${section}" 
	                 entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal=""/>
		<%-- used to set the max length of text that can be input in textBox and textarea widgets --%>
		<tags:metadataValue attribName="maxTextLength"   property="${property}" section="${section}" 
	          entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal=""/>
		<tags:metadataValue attribName="textBoxSize"  property="${property}" section="${section}" 
	           entity="${entity}" entityType="${entityType}" metadataName="${metadataName}" defaultVal=""/>


	
<%
		cachedData = new HashMap<String,String>();

		
		cachedData.put("label",jspContext.getAttribute("label",PageContext.PAGE_SCOPE));
		cachedData.put("indentLevel",jspContext.getAttribute("indentLevel",PageContext.PAGE_SCOPE));
		cachedData.put("required",jspContext.getAttribute("required",PageContext.PAGE_SCOPE));
		cachedData.put("style",jspContext.getAttribute("style",PageContext.PAGE_SCOPE));
		cachedData.put("defaultContext",jspContext.getAttribute("defaultContext",PageContext.PAGE_SCOPE));
		cachedData.put("listRequestId",jspContext.getAttribute("listRequestId",PageContext.PAGE_SCOPE));
		cachedData.put("listName",jspContext.getAttribute("listName",PageContext.PAGE_SCOPE));
		cachedData.put("listAttributes",jspContext.getAttribute("listAttributes",PageContext.PAGE_SCOPE));
		cachedData.put("widgetAttributes",jspContext.getAttribute("widgetAttributes",PageContext.PAGE_SCOPE));
		cachedData.put("maxTextLength",jspContext.getAttribute("maxTextLength",PageContext.PAGE_SCOPE));
		cachedData.put("textBoxSize",jspContext.getAttribute("textBoxSize",PageContext.PAGE_SCOPE));
		
		jspContext.setAttribute(metadataKey,cachedData,PageContext.APPLICATION_SCOPE);
	}
%>	    