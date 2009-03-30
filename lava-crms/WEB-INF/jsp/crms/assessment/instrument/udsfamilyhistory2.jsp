<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<c:set var="instrTypeEncoded" value="udsfamilyhistory2"/>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/include.jsp">
	<c:param name="instrTypeEncoded" value="${instrTypeEncoded}"/>
</c:import>

<%-- instrument-specific focusField setting (include.jsp has common focusField settings) --%>
<c:choose>
	<c:when test="${componentView == 'doubleEnter' || componentView == 'compare'}">
		<c:set var="focusField" value="a3chg"/>
	</c:when>
</c:choose>	

<page:applyDecorator name="component.content">
  <page:param name="isInstrument">true</page:param> 
  <page:param name="component">${instrTypeEncoded}</page:param> 
  <page:param name="focusField">${focusField}</page:param>
  <page:param name="pageHeadingArgs">UDS Family History</page:param>
  <page:param name="quicklinks">parents,siblings,children,relatives</page:param>
   
<page:applyDecorator name="component.instrument.content">
  <page:param name="instrTypeEncoded">${instrTypeEncoded}</page:param>
  <page:param name="view">${componentView}</page:param>

<c:import url="/WEB-INF/jsp/crms/assessment/instrument/udsCommon.jsp">
	<c:param name="entity" value="${instrTypeEncoded}"/>
	<c:param name="view" value="${componentView}"/>
	<c:param name="component" value="${component}"/>
</c:import>


<page:applyDecorator name="component.instrument.section">
  	<page:param name="sectionId">anonymous</page:param> 
  	<page:param name="view">${componentView}</page:param>
  	<page:param name="instructions"> </page:param>
  	<tags:createField property="a3Chg" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  	<page:param name="sectionId">parents</page:param> 
  	<page:param name="sectionNameKey">udsfamilyhistory2.parents.section</page:param> 
  	<page:param name="view">${componentView}</page:param>
  	<page:param name="instructions"> </page:param>
	<tags:createField property="parChg" entity="${instrTypeEncoded}" component="${component}"/>
  </page:applyDecorator>
 

<page:applyDecorator name="component.instrument.section">
  	<page:param name="sectionId">anonymous</page:param> 
  	<page:param name="view">${componentView}</page:param>
  	<page:param name="instructions"></page:param>
	<tags:tableForm>  
		<tags:listRow>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.parents.labelHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.parents.yobHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.parents.livHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.parents.yodHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.parents.demHeader" width="25%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.parents.onsetHeader" width="15%"/>
		</tags:listRow>
		<tags:listRow>
			<tags:listCell><tags:outputText textKey="udsfamilyhistory2.parents.mother" inline="false"/></tags:listCell>
			<tags:listCell><tags:createField property="momYob" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="momLiv" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="momYod" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="momDem" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="momOnset" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
		</tags:listRow>
		<tags:listRow>
			<tags:listCell><tags:outputText textKey="udsfamilyhistory2.parents.father" inline="false"/></tags:listCell>
			<tags:listCell><tags:createField property="dadYob" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="dadLiv" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="dadYod" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="dadDem" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="dadOnset" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
		</tags:listRow>
	</tags:tableForm>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  	<page:param name="sectionId">siblings</page:param> 
  	<page:param name="sectionNameKey">udsfamilyhistory2.siblings.section</page:param> 
  	<page:param name="view">${componentView}</page:param>
  	<page:param name="instructions"> </page:param>
	<tags:createField property="sibChg" entity="${instrTypeEncoded}" component="${component}"/>
  	<tags:createField property="twin" entity="${instrTypeEncoded}" component="${component}"/>
  	<tags:createField property="twinType" entity="${instrTypeEncoded}" component="${component}"/>
  	<tags:createField property="sibs" entity="${instrTypeEncoded}" component="${component}"/>
  </page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  	<page:param name="sectionId">anonymous</page:param> 
  	<page:param name="view">${componentView}</page:param>
  	<page:param name="instructions"> </page:param>  	
  	<tags:tableForm>  
		<tags:listRow>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.siblings.labelHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.siblings.yobHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.siblings.livHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.siblings.yodHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.siblings.demHeader" width="25%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.siblings.onsetHeader" width="15%"/>
		</tags:listRow>

		<c:forEach begin="1" end="20" var="item">
		<tags:listRow>
			<tags:listCell><tags:outputText textKey="udsfamilyhistory2.siblings.sibling${item}" inline="false"/></tags:listCell>
			<tags:listCell><tags:createField property="sib${item}Yob" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="sib${item}Liv" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="sib${item}Yod" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="sib${item}Dem" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="sib${item}Ons" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
		</tags:listRow>
		</c:forEach>
	</tags:tableForm>
</page:applyDecorator>


<page:applyDecorator name="component.instrument.section">
  	<page:param name="sectionId">children</page:param> 
  	<page:param name="sectionNameKey">udsfamilyhistory2.children.section</page:param> 
  	<page:param name="view">${componentView}</page:param>
  	<page:param name="instructions"> </page:param>
	<tags:createField property="kidChg" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="kids" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  	<page:param name="sectionId">anonymous</page:param> 
  	<page:param name="view">${componentView}</page:param>
  	<page:param name="instructions"> </page:param>  	
  	<tags:tableForm>  
  	
		<tags:listRow>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.children.labelHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.children.yobHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.children.livHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.children.yodHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.children.demHeader" width="25%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.children.onsetHeader" width="15%"/>
		</tags:listRow>

		<c:forEach begin="1" end="15" var="item">
		<tags:listRow>
			<tags:listCell><tags:outputText textKey="udsfamilyhistory2.children.child${item}" inline="false"/></tags:listCell>
			<tags:listCell><tags:createField property="kid${item}Yob" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="kid${item}Liv" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="kid${item}Yod" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="kid${item}Dem" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="kid${item}Ons" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
		</tags:listRow>
		</c:forEach>
	</tags:tableForm>
</page:applyDecorator>  

<page:applyDecorator name="component.instrument.section">
  	<page:param name="sectionId">relatives</page:param> 
  	<page:param name="sectionNameKey">udsfamilyhistory2.relatives.section</page:param> 
  	<page:param name="view">${componentView}</page:param>
  	<page:param name="instructions"> </page:param>
	 <tags:createField property="relChg" entity="${instrTypeEncoded}" component="${component}"/>
	<tags:createField property="relsDem" entity="${instrTypeEncoded}" component="${component}"/>
</page:applyDecorator>

<page:applyDecorator name="component.instrument.section">
  	<page:param name="sectionId">anonymous</page:param> 
  	<page:param name="view">${componentView}</page:param>
  	<page:param name="instructions"> </page:param>  	
  	<tags:tableForm>  
		<tags:listRow>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.relatives.labelHeader" width="15%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.relatives.yobHeader" width="20%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.relatives.livHeader" width="20%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.relatives.yodHeader" width="20%"/>
			<tags:listColumnHeader labelKey="udsfamilyhistory2.relatives.onsetHeader" width="25%"/>
		</tags:listRow>

		<c:forEach begin="1" end="15" var="item">
		<tags:listRow>
			<tags:listCell><tags:outputText textKey="udsfamilyhistory2.relatives.relative${item}" inline="false"/></tags:listCell>
			<tags:listCell><tags:createField property="rel${item}Yob" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="rel${item}Liv" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="rel${item}Yod" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
			<tags:listCell><tags:createField property="rel${item}Ons" entity="${instrTypeEncoded}" component="${component}"/></tags:listCell>
		</tags:listRow>
		</c:forEach>
	</tags:tableForm>
</page:applyDecorator>  

<page:applyDecorator name="component.instrument.section">
	<page:param name="sectionId">anonymous</page:param>
	<page:param name="view">${componentView}</page:param>
	<page:param name="instructions"> </page:param>
	<tags:createField property="notes['udsfamilyhistory']" entity="${instrTypeEncoded}" component="${component}" metadataName="instrument.sectionNote" labelAlignment="top" dataStyle="instrNote"/>
</page:applyDecorator>

<c:if test="${componentMode != 'vw'}">
<c:forEach begin="0" end="1" var="current">
  <c:choose>
    <c:when test="${componentView == 'doubleEnter' || (componentView == 'compare' && current == 1)}">
      <c:set var="componentPrefix" value="compareInstrument"/>
    </c:when>
    <c:otherwise>
      <c:set var="componentPrefix" value="instrument"/>
    </c:otherwise>
  </c:choose>
  <c:if test="${current == 0 || (current == 1 && componentView == 'compare')}">

	
	
	<%-- skip changed fields and unskip twin, and top level section fields packet = initial--%>
	<ui:formGuide>
	  <ui:observe elementIds="packet" component="instrument" forValue="[I|^$]"/>
	  <ui:setValue elementIds="a3Chg,parChg,sibChg,kidChg,relChg" component="${componentPrefix}" value="0" />
	  <ui:disable elementIds="a3Chg,parChg,sibChg,kidChg,relChg" component="${componentPrefix}" />
	  <ui:unskip elementIds="twin,twinType" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>	
	</ui:formGuide>           


	<%-- when a3Chg <> 1 then unskip other changed fields (ignore on initial packets) --%>
	<ui:formGuide>
	  <ui:ignore elementIds="packet" component="instrument" forValue="[I|^$]"/>
	  <ui:observe elementIds="a3Chg" component="${componentPrefix}" forValue="1"/>
  	  <ui:setValue elementIds="parChg,sibChg,kidChg,relChg" component="${componentPrefix}" value="0" />
  	  <ui:disable elementIds="parChg,sibChg,kidChg,relChg" component="${componentPrefix}"/>	
	</ui:formGuide>           
	
	<%-- twintype skip logic--%>
	<ui:formGuide>
	  <ui:observe elementIds="twin" component="${componentPrefix}" forValue="^1"/>
	 	  <ui:unskip elementIds="twinType" component="${componentPrefix}" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>	
	</ui:formGuide>           
		
	
	
<%--skip all fields when A3Chg = 1 --%>
	<%--skip parent fields if A3chg is = 1 (ignore on initial packets)--%>
	<ui:formGuide >
	  <ui:ignore elementIds="packet" component="instrument" forValue="[I|^$]"/>
	  <ui:observe elementIds="a3Chg" component="${componentPrefix}" forValue="1"/>
	  <ui:skip elementIds="momYob,dadYob" component="${componentPrefix}"/>	
	  <ui:skip elementIds="momYob,dadYob,sibs,kids,relsDem" component="${componentPrefix}"/>	
	</ui:formGuide>           
	
	<c:forEach begin="1" end="20" var="item">
		<%-- skip all sibling fields if A3chg = 1 (ignore on initial packets)--%>
		<ui:formGuide >
		  <ui:ignore elementIds="packet" component="instrument" forValue="[I|^$]"/>
		  <ui:observe elementIds="a3Chg" component="${componentPrefix}" forValue="1" />
		  <ui:skip elementIds="sib${item}Yob" component="${componentPrefix}"/>	
		</ui:formGuide>           
	</c:forEach>
	
	<c:forEach begin="1" end="15" var="item">
		<%-- skip all children and relatives fields if A3chg = 1 (ignore on initial packets)--%>
		<ui:formGuide >
		 	<ui:ignore elementIds="packet" component="instrument" forValue="[I|^$]"/>
		  	<ui:observe elementIds="a3Chg" component="${componentPrefix}" forValue="1" />
		   	<ui:skip elementIds="kid${item}Yob,rel${item}Yob" component="${componentPrefix}"/>	
		</ui:formGuide>           
	</c:forEach>
	
	
	
	<%-- when parChg is checked skip parent yob otherwise enable--%>
	<ui:formGuide >
	  <ui:ignore elementIds="a3Chg" forValue = "1" component="${componentPrefix}" />
	  <ui:depends elementIds="packet" component="instrument"/>
	  <ui:depends elementIds="a3Chg" component="${componentPrefix}" />
	  <ui:observe elementIds="parChg" component="${componentPrefix}" forValue="1" />
	  <ui:skip elementIds="momYob,dadYob" component="${componentPrefix}"/>	
	</ui:formGuide>           
	
	
	<c:forEach begin="1" end="20" var="item">
		<%--if sibChg = 1 then skip sibling rows and sibs field --%>
		<ui:formGuide>
			<ui:ignore elementIds="a3Chg" forValue = "1" component="${componentPrefix}" />
	 	    <ui:depends elementIds="packet" component="instrument"/>
	 	 	<ui:depends elementIds="a3Chg" component="${componentPrefix}" />
	  		<ui:observe elementIds="sibChg" component="${componentPrefix}" forValue="1" />
	        <ui:skip elementIds="sib${item}Yob" component="${componentPrefix}"/>	
		</ui:formGuide>           
	</c:forEach>
	
	<ui:formGuide >
		<ui:ignore elementIds="a3Chg" forValue = "1" component="${componentPrefix}" />
	    <ui:depends elementIds="packet" component="instrument"/>
	 	<ui:depends elementIds="a3Chg" component="${componentPrefix}" />
		<ui:observe elementIds="sibChg" component="${componentPrefix}" forValue="1" />
	    <ui:skip elementIds="sibs" component="${componentPrefix}"/>
	</ui:formGuide>     
	
	<c:forEach begin="1" end="15" var="item">
		<%--if kidChg = 1 then skip child rows and kids field --%>
		<ui:formGuide >
		    <ui:ignore elementIds="a3Chg" forValue = "1" component="${componentPrefix}" />
	  		<ui:depends elementIds="packet" component="instrument"/>
	 	 	<ui:depends elementIds="a3Chg" component="${componentPrefix}" />
	  		<ui:observe elementIds="kidChg" component="${componentPrefix}" forValue="1" />
	        <ui:skip elementIds="kid${item}Yob" component="${componentPrefix}"/>	
		</ui:formGuide>           
	</c:forEach>
	<ui:formGuide>
	    <ui:ignore elementIds="a3Chg" forValue = "1" component="${componentPrefix}" />
	    <ui:depends elementIds="packet" component="instrument"/>
	 	<ui:depends elementIds="a3Chg" component="${componentPrefix}" />
		<ui:observe elementIds="kidChg" component="${componentPrefix}" forValue="1" />
	    <ui:skip elementIds="kids" component="${componentPrefix}"/>	
	</ui:formGuide>   
	
	<c:forEach begin="1" end="15" var="item">
		<%--if relChg = 1 then skip relatives rows and relsdem field --%>
		<ui:formGuide>
     	    <ui:ignore elementIds="a3Chg" forValue = "1" component="${componentPrefix}" />
     	    <ui:depends elementIds="packet" component="instrument"/>
	 	 	<ui:depends elementIds="a3Chg" component="${componentPrefix}" />
	  		<ui:observe elementIds="relChg" component="${componentPrefix}" forValue="1"/>
	        <ui:skip elementIds="rel${item}Yob" component="${componentPrefix}"/>	
		</ui:formGuide>           
	</c:forEach>
	
	<ui:formGuide>
        <ui:ignore elementIds="a3Chg" forValue = "1" component="${componentPrefix}" />
	    <ui:depends elementIds="packet" component="instrument"/>
     	<ui:depends elementIds="a3Chg" component="${componentPrefix}" />
		<ui:observe elementIds="relChg" component="${componentPrefix}" forValue="1" />
	    <ui:skip elementIds="relsDem" component="${componentPrefix}"/>	
	</ui:formGuide>   
	
	
	
	
	<c:forEach begin="1" end="20" var="item">
	
		<%-- if sibling YOB completed then unskip living and demented --%>
		<ui:formGuide>
		  <ui:depends elementIds="packet" component="instrument"/>
		  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}"/>
		  <ui:observe elementIds="sib${item}Yob" component="${componentPrefix}" forValue="^$|-[0-9]" negate="true"/>
		  <ui:unskip elementIds="sib${item}Liv,sib${item}Dem" component="${componentPrefix}"/>	
		</ui:formGuide>   
			
		<%-- if sibling still living is no then unskip year of death --%>
		<ui:formGuide>
		  <ui:depends elementIds="packet" component="instrument"/>
		  <ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}"/>
		  <ui:observe elementIds="sib${item}Liv" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
		  <ui:unskip elementIds="sib${item}Yod" component="${componentPrefix}"/>	
		</ui:formGuide>     
			
		<%-- if sibling is demented then unskip onset --%>
		<ui:formGuide>
			<ui:depends elementIds="packet" component="instrument"/>
			<ui:depends elementIds="a3Chg,sibChg" component="${componentPrefix}"/>
			<ui:observe elementIds="sib${item}Dem" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
			<ui:unskip elementIds="sib${item}Ons" component="${componentPrefix}"/>	
		</ui:formGuide>           
			      
	</c:forEach>
	
	
	<c:forEach begin="1" end="15" var="item">
	
			<%-- if child YOB completed then unskip living and demented --%>
			<ui:formGuide>
		   	  <ui:depends elementIds="packet" component="instrument"/>
			  <ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}" />
			  <ui:observe elementIds="kid${item}Yob" component="${componentPrefix}" forValue="^$|-[0-9]" negate="true"/>
			  <ui:unskip elementIds="kid${item}Liv,kid${item}Dem" component="${componentPrefix}"/>	
			</ui:formGuide>   
			
			<%-- if child still living is no then unskip year of death --%>
			<ui:formGuide>
			  <ui:depends elementIds="packet" component="instrument"/>
			  <ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}"/>
			  <ui:observe elementIds="kid${item}Liv" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
			  <ui:unskip elementIds="kid${item}Yod" component="${componentPrefix}"/>	
			</ui:formGuide>          
			<%-- if child is demented then unskip onset --%>
			<ui:formGuide>
				<ui:depends elementIds="packet" component="instrument"/>
				<ui:depends elementIds="a3Chg,kidChg" component="${componentPrefix}"/>
				<ui:observe elementIds="kid${item}Dem" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
				<ui:unskip elementIds="kid${item}Ons" component="${componentPrefix}"/>	
			</ui:formGuide>      
			          		
	</c:forEach>
	
	<c:forEach begin="1" end="15" var="item">
	
			<%-- if relative YOB completed then unskip living and onset --%>
			<ui:formGuide>
			  <ui:depends elementIds="packet" component="instrument"/>
			  <ui:depends elementIds="a3Chg,relChg" component="${componentPrefix}"/>
			  <ui:observe elementIds="rel${item}Yob" component="${componentPrefix}" forValue="^$|-[0-9]" negate="true"/>
			  <ui:unskip elementIds="rel${item}Liv,rel${item}Ons" component="${componentPrefix}"/>	
			</ui:formGuide>       
			
			<%-- if relative still living is no then unskip year of death --%>
			<ui:formGuide>
			  <ui:depends elementIds="packet" component="instrument"/>
			  <ui:depends elementIds="a3Chg,relChg" component="${componentPrefix}"/>
			  <ui:observe elementIds="rel${item}Liv" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
			  <ui:unskip elementIds="rel${item}Yod" component="${componentPrefix}"/>	
			</ui:formGuide>           
				       		
	</c:forEach>
	
	<%-- if parent YOB completed then unskip living and demented --%>
	<ui:formGuide>
	  <ui:depends elementIds="packet" component="instrument"/>
	  <ui:depends elementIds="a3Chg,parChg" component="${componentPrefix}" />
	  <ui:observe elementIds="mom${item}Yob" component="${componentPrefix}" forValue="^$|-[0-9]" negate="true"/>
	  <ui:unskip elementIds="mom${item}Liv,mom${item}Dem" component="${componentPrefix}"/>	
	</ui:formGuide>       
	<ui:formGuide>
	  <ui:depends elementIds="packet" component="instrument"/>
	  <ui:depends elementIds="a3Chg,parChg" component="${componentPrefix}"/>
	  <ui:observe elementIds="dad${item}Yob" component="${componentPrefix}" forValue="^$|-[0-9]" negate="true"/>
	  <ui:unskip elementIds="dad${item}Liv,dad${item}Dem" component="${componentPrefix}"/>	
	</ui:formGuide>       	
	<%-- if parent still living is no then unskip year of death --%>
	<ui:formGuide>
	  <ui:depends elementIds="packet" component="instrument"/>
	  <ui:depends elementIds="a3Chg,parChg" component="${componentPrefix}"/>
	  <ui:observe elementIds="momLiv" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
	  <ui:unskip elementIds="momYod" component="${componentPrefix}"/>	
	</ui:formGuide> 
	          
	<ui:formGuide>
	  <ui:depends elementIds="packet" component="instrument"/>
	  <ui:depends elementIds="a3Chg,parChg" component="${componentPrefix}"/>
	  <ui:observe elementIds="dadLiv" component="${componentPrefix}" forValue="^0" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
	  <ui:unskip elementIds="dadYod" component="${componentPrefix}"/>	
	</ui:formGuide>           
	
	<%-- if parent is demented then unskip onset --%>
	<ui:formGuide>
	  <ui:depends elementIds="packet" component="instrument"/>
	  <ui:depends elementIds="a3Chg,parChg" component="${componentPrefix}"/>
	  <ui:observe elementIds="momDem" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
	  <ui:unskip elementIds="momOnset" component="${componentPrefix}"/>	
	</ui:formGuide>           
	
	<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare') || (current == 1) ? 'true' : ''}">
	  <ui:depends elementIds="packet" component="instrument"/>
	  <ui:depends elementIds="a3Chg,parChg" component="${componentPrefix}"/>
	  <ui:observe elementIds="dadDem" component="${componentPrefix}" forValue="^1" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
	  <ui:unskip elementIds="dadOnset" component="${componentPrefix}"/>	
	</ui:formGuide>     
	
</c:if>
</c:forEach>
</c:if>




</page:applyDecorator>    
</page:applyDecorator>    
	    
