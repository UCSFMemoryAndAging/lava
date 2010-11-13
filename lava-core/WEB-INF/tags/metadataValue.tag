<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- metadataValue

	/* Get the value of a metadata attribute for a given property. 
	 * 
	 * Currently, the following attributes are defined in the resource bundle, messages.properties:
	 * 		style = the nature of the values the property can assume, e.g. "date", "scale", "range", "string", "text" 
	 * 		context = the context of the property, e.g. contextual data ("c"), result data ("r"), informational data ("i")
	 * 		label = the label associated with the property
	 * 		indentLevel = the indent level for the property as an integer, where 0 means no indent
	 *      required = whether the field should be visually marked as being required (only if context is 'i')
	 * 		widgetAttributes = special attributes for the input widget used by the property
	 * 		widgetListName = the name of a list of values for the input widget used by the property
	 * 
	 * The value is obtained using a key into the resource bundle properties structure. Attribute
	 * values may be defined at various levels:
	 * 1. the property level
	 * 2. the section level, where the section contains the property
	 * 3. the entity level, where the entity contains the property
	 * 4. for a property across a given entity type (this is useful for properties which are common
	 *     across all entities of a given entity type, e.g. the qualityIssue property applies to
	 *     all "instrument" entity types)
	 *  
	 * The list is from most specific to least specific attribute value definition, and this
	 * is the order in which the properties structure is searched to find the attribute value.
	 * The arguments passed in are used to construct a key for each level until a match is found
	 * and the attribute value is returned.
	 * 
	 * e.g. given the following arguments:
	 *  entityType="instrument"
	 *  entity="cdr"
	 *  property="think"
	 *  section="memory"
	 *  attribute="label"
	 *  
	 * The method would construct keys in the following order, searching the metadata properties 
	 * data structure for each key. 
	 *     cdr.think.label
	 *     cdr.memory.label
	 *     cdr.label   
	 *     instrument.think.label
	 * 
	 * When a key matches, no more keys are constructed and the attribute value is returned.
	 * 
	 * note: the notion of "section" applies generally to instruments, but could be useful for other 
	 *       entities as well. 
	 *       
	 *       instruments are often split up into sections in the view, and since much of the metadata 
	 *       are view characteristics, and many properties within the same section could share the 
	 *       same characteristics, it is useful to specify attribute values at the section level 
	 *       
	 *       all instruments inherit from a common class Instrument as a number of core properties are
	 *       shared among all instruments. thus, the "global" definition levels facilitate the metadata
	 *       attribute definitions for common instrument properties
	 */
--%>

<%@ attribute name="attribName" type="java.lang.String" required="true" rtexprvalue="false"
              description="name of the metadata attribute" %>
<%@ attribute name="property" required="true" 
              description="name of the property" %>
<%@ attribute name="section" required="true"  
              description="name of the entity section containing the property" %>
<%@ attribute name="entity" required="true" 
              description="name of the entity containing the property, e.g. 'cdr'" %>
<%@ attribute name="entityType" required="true" 
              description="name the type of entity containing the property, e.g. 'instrument'" %>
<%@ attribute name="defaultVal" required="true" 
              description="default value if metadata attribute not found" %>
<%@ attribute name="metadataName" 
              description="[optional] override of the portion of the key into the metadata that
                           precedes the metadata attribute" %>
<%-- note: defaults could also be set up to come from the resource bundle defaults --%>

<%@ variable name-from-attribute="attribName" variable-class="java.lang.String" alias="attribValue" scope="AT_END"
              description="value of the metadata attribute" %>

<%-- use spring:message to obtain values from the resource bundle --%>

<%-- first check if the user has overridden the pre-attribute portion of the key into the resource bundle --%>
<c:if test="${not empty metadataName}">
	<spring:message code="${metadataName}.${attribName}" var="attribValue" scope="page" text=""/>
</c:if>

<c:if test="${empty metadataName}">
	<%-- try entity.property.attribName --%>
	
	<spring:message code="${entity}.${property}.${attribName}" var="attribValue" scope="page" text=""/>
		<c:if test="${empty attribValue}">
		
	    <%-- try entity.section.attribName --%>
    	<spring:message code="${entity}.${section}.${attribName}" var="attribValue" scope="page" text=""/>

		<c:if test="${empty attribValue}">
			<%-- try entity.attribName --%>
	    	<spring:message code="${entity}.${attribName}" var="attribValue" scope="page" text=""/>

			<c:if test="${empty attribValue}">
				<%-- try entityType.property.attribName --%>
			    <spring:message code="${entityType}.${property}.${attribName}" var="attribValue" scope="page" text=""/>

				<%-- give it the default attribValue --%>
				<c:if test="${empty attribValue}">
					<c:set var="attribValue" value="${defaultVal}"/>
				</c:if>
			</c:if>
		</c:if>
	</c:if>
</c:if>
