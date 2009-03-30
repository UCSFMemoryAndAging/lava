<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>

<c:set var="component">${param.component}</c:set>
<c:set var="componentView">${param.componentView}</c:set>
<c:set var="instrTypeEncoded">${param.instrTypeEncoded}</c:set>

<%-- using a DTO as the command object, rather than the model object directly as with other instruments.
see MedicationsHandler for reasons behind this. --%>
<c:set var="dtoComponent" value="udsMedications2Dto"/>

<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">details</page:param>
  <page:param name="sectionNameKey">udsmedications2.details.section</page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions"><spring:message code="udsmedications2.details.instructions"/></page:param>
	<tags:createField property="anyMeds" entity="${instrTypeEncoded}" component="${dtoComponent}" mode="dc"/>
</page:applyDecorator>

<c:forEach items="${command.components[dtoComponent].details}" var="detail" varStatus="iterator">


<page:applyDecorator name="component.instrument.section">
  <page:param name="sectionId">detail</page:param>
  <page:param name="view">${componentView}</page:param>
  <page:param name="instructions">Medication Line ${iterator.index+1}</page:param>
  	<tags:contentColumn columnClass="colLeft2Col5050">
		<tags:createField property="details[${iterator.index}].drugLookup" component="${dtoComponent}" entity="${instrTypeEncoded}" metadataName="medications.drugLookup${iterator.index != 0 ? 'Clone':''}" mode="dc"/>
		<tags:createField property="details[${iterator.index}].drugId" component="${dtoComponent}" entity="${instrTypeEncoded}" metadataName="medications.drugId" mode="dc"/>
		
	</tags:contentColumn>
	<tags:contentColumn columnClass="colRight2Col5050">
		<tags:createField property="details[${iterator.index}].generic" component="${dtoComponent}" entity="${instrTypeEncoded}" metadataName="medications.generic" mode="dc"/>
		<tags:createField property="details[${iterator.index}].brand" component="${dtoComponent}" entity="${instrTypeEncoded}" metadataName="medications.brand"  mode="dc"/>
		<tags:createField property="details[${iterator.index}].notListed" component="${dtoComponent}" entity="${instrTypeEncoded}" metadataName="medications.notListed"  mode="dc"/>
	</tags:contentColumn>
</page:applyDecorator>
</c:forEach>



<script type="text/javascript">
	
//callback function used to populate drug lookup select lists
		
function initializeMedicationFields(){
	var key;
	for(var i = 0; i <= 39; i++){
		key = 'acs_textbox_udsMedications2Dto_details_' + i + '_drugLookup';
		if (ACS[key] != null){
			ACS[key].listWidth = 600;
			ACS[key].columnCount = 3;
			ACS[key].columnWidths = ['300','250','50'];
			ACS[key].columnHeaders = ['Medication','Generic Name','Drug ID'];
			ACS[key].select = document.getElementById("udsMedications2Dto_details_0_drugLookup")
			
			functionDefinition = "var i = "+ i +";"+
					"lookupSelect = document.getElementById('udsMedications2Dto_details_0_drugLookup');"+
					"lookup = document.getElementById('acs_textbox_udsMedications2Dto_details_'+i+'_drugLookup');"+
					"drugId = document.getElementById('udsMedications2Dto_details_'+i+'_drugId');"+
					"generic = document.getElementById('udsMedications2Dto_details_'+i+'_generic');"+
					"brand = document.getElementById('udsMedications2Dto_details_'+i+'_brand');"+
					"notListedTextboxId = document.getElementById('udsMedications2Dto_details_'+i+'_notListed');"+			
					"selectedText = lookupSelect.options[lookupSelect.selectedIndex].text;"+
					"columns = selectedText.split('|');"+
					"if(columns.length == 3){"+
						"drugId.value = columns[2];"+
						"generic.value = columns[1];"+
						"if (columns[1].toLowerCase() != columns[0].toLowerCase()){"+
						"	brand.value = columns[0];"+
						"}else{"+
						"	brand.value = '';"+
						"}"+
						"lookupSelect.selectedIndex = 0;"+
						"lookup.value = '';"+
						"};";
			callbackFunction = new Function(functionDefinition);
			ACS[key].appendOnChangeCallback(callbackFunction,this);
		}			
		
		
	}	
}	
initializeMedicationFields();

</script>

<%--Section 1 UITAGS --%>
<c:forEach begin="0" end="39" var="item">


<%--If any meds question <> yes then all detail controls are disabled --%>
<ui:formGuide>
	<ui:observe component="${dtoComponent}" elementIds="anyMeds" forValue="^$|0|[\-7]|[\-8]|[\-9]|[\-6]" comboRadioSelect="${componentMode == 'dc' ? 'true' : 'false'}"/>
   	<ui:skip component="${dtoComponent}" elementIds="details_${item}_drugLookup" skipValue=""/>
   	<ui:setValue component="${dtoComponent}" elementIds="details_${item}_drugId" value=""/>
</ui:formGuide>



<%--if drug id is not set (or is cleared), then skip most fields--%>
<ui:formGuide>
	<ui:depends component="${dtoComponent}" elementIds="anyMeds"/>
	<ui:depends component="${dtoComponent}" elementIds="details_${item}_drugLookup"/>
    <ui:observe component="${dtoComponent}" elementIds="details_${item}_drugId" forValue="^$|00000"/>
   	<ui:setValue component="${dtoComponent}" elementIds="details_${item}_drugId,details_${item}_generic,details_${item}_brand" value=""/> 
</ui:formGuide>



<%--Handle the notListed Field specially (only enable when DrugId = 99999) --%>
<ui:formGuide simulateEvents="${(current == 0 && componentView != 'compare' && item==39) || (current == 1 && item==39) ? 'true' : ''}">
 	<ui:depends component="${dtoComponent}" elementIds="anyMeds"/>
	<ui:depends component="${dtoComponent}" elementIds="details_${item}_drugLookup"/>
	<ui:observe component="${dtoComponent}" elementIds="details_${item}_drugId" forValue="99999"/>
   	<ui:unskip component="${dtoComponent}" elementIds="details_${item}_notListed" skipValue=""/>
</ui:formGuide>





</c:forEach>

