<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- create an HTML input field for uploading a file. this field will present a Browse
     button to the user to allow them to select the file to be uploaded.
     
     this binds to property of type MultiPartFile 
     
     note: for uploading files, the "enctype" attribute of <form> must be set to
           "multipart/form-data", and the framework (e.g. Spring) automatically handles
           this encoding type --%>

<%@ attribute name="property" required="true"
              description="the name of the property to bind to" %>
<%@ attribute name="fieldId" required="true" 
              description="id for the field" %>
<%@ attribute name="attributesText" 
              description="[optional] attributes for the HTML element" %>
<%@ attribute name="styleClass"
              description="[optional] the style class for HTML element (optional, space separated))" %>              
              
<spring:bind path="${property}">          
	<input type="file" id="${fieldId}" name="${status.expression} class="${styleClass}" ${attributesText}>
</spring:bind>

