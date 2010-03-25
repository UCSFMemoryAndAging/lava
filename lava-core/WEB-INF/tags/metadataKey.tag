<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- metadataValue

	/* 
	 *  Create a unique key for storing metadata values for this unique combination
	 *  of field settings in a application cache. 
	 */
--%>

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

<c:choose>
	<c:when test="${fn:startsWith(property,'pageList')}">
		edu.ucsf.lava.metadataCacheKey.${section}_${entity}_${entityType}_${metadataName}
	</c:when>
	<c:otherwise>
		edu.ucsf.lava.metadataCacheKey.${property}_${section}_${entity}_${entityType}_${metadataName}
	</c:otherwise>
</c:choose>