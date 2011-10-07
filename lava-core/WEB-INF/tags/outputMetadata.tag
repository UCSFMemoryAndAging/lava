<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%@ tag import="java.util.Map,java.util.HashMap" %>

<%@ attribute name="attribName" type="java.lang.String" required="true"
              description="name of the metadata attribute" %>    
<%@ attribute name="property" required="true" 
              description="name of the property" %>
<%@ attribute name="section"
              description="name of the entity section containing the property" %>              
<%@ attribute name="entity" required="true" 
              description="name of the entity containing the property, e.g. 'cdr'" %>
<%@ attribute name="entityType"
              description="name the type of entity containing the property, e.g. 'instrument'" %>

<c:set var="metadataKey">
	<tags:metadataKey property="${property}" section="${section}" entity="${entity}" entityType="${entityType}" />
</c:set>

<tags:createFieldMetadata metadataKey="${metadataKey}" property="${property}" section="${section}" entity="${entity}" entityType="${entityType}" />

<c:if test="${attribName == 'label'}">${label}</c:if>
<c:if test="${attribName == 'label2'}">${label2}</c:if>

