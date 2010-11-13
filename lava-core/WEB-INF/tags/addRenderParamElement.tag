<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%@ attribute name="parameter" required="true" 
              description="render parameter (list type) of RenderParam object in the model to update" %>
<%@ attribute name="paramValue" required="true" type="java.lang.Object"
              description="value for param element (type of Object used to provide support for non-String values" %>

${lavaFn:addRenderParamElement(renderParams,parameter,paramValue)}
