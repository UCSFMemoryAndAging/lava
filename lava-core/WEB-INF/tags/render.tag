<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ attribute name="entity" required="true" type="java.lang.Object"
              description="the entity to render" %>
<%@ attribute name="renderObjectClassName" required="false" 
              description="the class name of object to render (e.g. edu.ucsf.lava.core.view.model.CssBox)
              		if left empty, the default render object type will be rendered" %> 
<%@ attribute name="parameters" required="false" 
              description="comma delimited list of render property/value pairs used to
              		update the RenderParam object in the model. If not included, the 
              		RenderParam object is passed unaltered" %>

<c:if test="${!empty parameters}">
	<tags:setRenderParams parameters="${parameters}"/>
</c:if>

<c:choose>
	<c:when test="${empty renderObjectClassName}">
		${lavaFn:generateDefaultObjectRenderCode(entity,renderParams)}
	</c:when>
	<c:otherwise>
		${lavaFn:generateRenderCode(entity,renderObjectClassName,renderParams)}
	</c:otherwise>
</c:choose>
