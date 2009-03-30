<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- eventLink
	create a standard event action link
--%>
<%@ attribute name="linkText" required="true" 
       description="the text of the link" %>
<%@ attribute name="action" required="true" 
       description="the action/event to use" %>
<%@ attribute name="component" required="true"
       description="the component for the action"%>
<%@ attribute name="pageName" required="true"
       description="the name of the page/form"%>
<%@ attribute name="className" 
       description="the name of the class to use"%>
<%@ attribute name="styleDef" 
       description="a style definition"%>
<%@ attribute name="target"
       description="the value of the HTML target attribute used to open the response in a new browser window
                    note: 9/25/07 this is not yet supported by Spring Web Flow, so not used yet"%>          
       
<a href="javascript:void" onClick="javascript:document.${pageName}.action='${requestUrl}#${component}';submitForm(document.${pageName}, '${component}__${action}', '${target}');return false" class="${empty className ? '':className}" style="${empty styleDef ? '':styleDef}">${linkText}</a>      
       

