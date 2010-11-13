<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ attribute name="parameters" required="true" 
              description="comma delimited list of render parameter/value pairs used to
              		update the RenderParam object in the model." %>

${lavaFn:setRenderParams(renderParams,parameters)}
