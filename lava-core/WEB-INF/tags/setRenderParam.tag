<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ attribute name="parameter" required="true" 
              description="render parameter within RenderParam object in the model to update" %>
<%@ attribute name="paramValue" required="true" type="java.lang.Object"
              description="value for param (type of Object used to provide support for non-String values" %>

${lavaFn:setRenderParam(renderParams,parameter,paramValue)}
