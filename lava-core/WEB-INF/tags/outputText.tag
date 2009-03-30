<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- outputText

     Outputs text specified either as a key to a resource bundle, or as a string.
     
     Attributes specify whether text should have various characteristics.
--%>

<%@ attribute name="textKey" required="false" 
              description="resource bundle key of text string to output. if not specified, text should be
                           specified and is used" %>
<%@ attribute name="arguments" required="false"
			  description="arguments for textKey as a comma-separated string" %>                           
<%@ attribute name="text" required="false"
              description="if textKey is not specified, text should be specified" %>
<%@ attribute name="styleClass" required="false"
              description="[optional] a style class (or multiple classes, space separated) for the output text" %>              
<%@ attribute name="inline" type="java.lang.Boolean" required="false"
              description="[optional] if true (the default), output text as inline. if false, output text as a block, 
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
                           
<c:if test="${not empty textKey}">
	<c:set var="text">
		<spring:message code="${textKey}" arguments="${arguments}"/>
	</c:set>
</c:if>			
	                     
<c:if test="${not empty inline && !inline}">
	<div class="outputText ${not empty hangingIndent ? 'hangingIndentOutputText' : ''} ${not empty associated ? 'associatedOutputText' : ''} ${not empty indent ? 'indentOutputText' : ''}">
</c:if>	

<c:if test="${not empty styleClass}">
	<span class="${styleClass}">
</c:if>	

${text}

<c:if test="${not empty styleClass}">
	</span>
</c:if>	

<c:if test="${not empty inline && !inline}">
	</div>
</c:if>	
