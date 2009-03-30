<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- create an HTML input field for uploading a file. this field will present a Browse
     button to the user to allow them to select the file to be uploaded. the file is
     submitted to the request using the specified request parameter name. 
     
     note: for uploading files, the "enctype" attribute of <form> must be set to
           "multipart/form-data", and the framework (e.g. Spring) automatically handles
           this encoding type --%>

<%@ attribute name="paramName" required="true"
              description="used in constructing the request parameter name for the upload file" %>
<%@ attribute name="component"
              description="[optional] used in constructing the request parameter name for the upload file" %>
              
<c:if test="${empty component}">              
<input type="file" name="${paramName}">
</c:if>

<c:if test="${not empty component}">              
<input type="file" name="${component}_${paramName}">
</c:if>

