<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- outputValueTranslation

     Outputs text from the messages resource based on the entity, property, and property value. 
     Alternatively specify a translation method to access alternative rendering of a property value.
     
     message code is formed in the following precedence order based on the presence of attributes
     
     [entity].[metadataName].[method].[value]
     [entity].[property].[property2].[method].[value]
     [entity].[property].[method].[value]
     [entity].[metadataName].[value]
     [entity].[property].[value]
     [component].[metadataName].[method].[value]
     [component].[property].[property2].[method].[value]
     [component].[property].[method].[value]
     [component].[metadataName].[value]
     [component].[property].[value]
     
     
--%>
<%@ attribute name="component" 
       description="[optional] when specified, the component overrides binding behavior to work with component based
       				controllers, and also may be used to form part of the lookup key for retrieving field metadata" %>

<%@ attribute name="property" required="true" 
       description="the name of the property must be a single level of dereferencing (e.g. patient not patient.fullName)" %>

<%@ attribute name="property2" required="false" 
       description="a second level property (e.g. 'fullName' of 'patient.fullName' property" %>

<%@ attribute name="method"
       description="[optional] a translation method (differentiates between alternative renderings of a value)"%>

<%@ attribute name="arguments" required="false"
			  description="[optional] arguments for the message as a comma-separated string" %>                           

<%@ attribute name="defaultText" required="false"
              description="[optional] default text to output if the messagecode is not found" %>

<%@ attribute name="entity"
       description="[optional] the name of the entity domain object to which the property belongs used for retrieving 
       				metadata. if specified, it overrides the use of the component attribute for creating the metadata
       				lookup key" %>

<%@ attribute name="metadataName"
       description="[optional] override of the property portion of the key used to for the 
       				messageCode (useful when the property name is too complex to be a lookup key)."%>


<%@ attribute name="styleClass" required="false"
              description="[optional] a style class (or multiple classes, space separated) for the output text" %>              

<%@ attribute name="inline" type="java.lang.Boolean" required="false"
              description="[optional] if true output text as inline. if false, output text as a block, 
                           in which case the text is output on a new line and is followed by a newline." %>              

<%@ attribute name="hangingIndent" type="java.lang.Boolean" required="false"
              description="[optional] (defaults to false) if true, the first line of output text is a hanging indent,
                           e.g. if the text begins with a question number, so the question number stands out.
                           this attribute only applies to text output in a block, i.e. inline=false" %>

<%@ attribute name="associated" type="java.lang.Boolean" required="false"
              description="[optional] (defaults to false) if true, output text is positioned immediately below preceding
                           block, to give it the appearance that it is associated with the preceding block, which is
                           typically a field. otherwise, the bottom margin of the previous block applies. 
                           this attribute only applies to text output in a block, i.e. inline=false" %>

<%@ attribute name="indent" type="java.lang.Boolean" required="false"
              description="[optional] (defaults to false) if true, indent text 20px. this only applies to text output 
                           in a block, i.e. inline=false" %>

<c:choose>
	<c:when test="${not empty entity}">
		<c:set var="messageCode">${entity}</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="messageCode">${component}</c:set>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${not empty metadataName}">
		<c:set var="messageCode">${messageCode}.${metadataName}</c:set>
	</c:when>
	<c:when test="${not empty property2}">
		<c:set var="messageCode">${messageCode}.${property}.${property2}</c:set>
	</c:when>
	<c:otherwise>
		<c:set var="messageCode">${messageCode}.${property}</c:set>
	</c:otherwise>
</c:choose>

<c:if test="${not empty method}">
		<c:set var="messageCode">${messageCode}.${method}</c:set>
</c:if>

<c:choose>
	<c:when test="${empty property2}">
		<c:set var="messageCode">${messageCode}.${command.components[component][property]}</c:set>
	</c:when>
	<c:otherwise>
		<c:if test="${not empty command.components[component][property]}">
			<c:set var="messageCode">${messageCode}.${command.components[component][property][property2]}</c:set>
		</c:if>
	</c:otherwise>
</c:choose>

                           
<c:if test="${not empty messageCode}">
	<c:choose>
		<c:when test="${empty defaultText}">
			<c:set var="text">
				<spring:message code="${messageCode}" arguments="${arguments}" text=""/>
			</c:set>
		</c:when>
		<c:otherwise>
			<c:set var="text">
				<spring:message code="${messageCode}" arguments="${arguments}" text="${defaultText}"/>
			</c:set>
		</c:otherwise>
	</c:choose>
</c:if>			
	                     
<c:if test="${empty inline || !inline}">
	<div class="outputText ${not empty hangingIndent ? 'hangingIndentOutputText' : ''} ${not empty associated ? 'associatedOutputText' : ''} ${not empty indent ? 'indentOutputText' : ''}">
</c:if>	

<c:if test="${not empty styleClass}">
	<span class="${styleClass}">
</c:if>	

${text}

<c:if test="${not empty styleClass}">
	</span>
</c:if>	

<c:if test="${empty inline || !inline}">
	</div>
</c:if>	
