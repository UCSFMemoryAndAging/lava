<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<div id="patientContext">
<form name="patientContext" method="post">
	<input type="hidden" name="_eventId" id="_eventId" value="patientContext__contextChange"/>
	<input type="hidden" name="searchBy" value="NameRev"/> 
	
	<div id="patientContextLabel">
	Current Patient &nbsp;&nbsp;&nbsp;&nbsp;<a class="patientContextLabel" href="javascript:void" onClick="javascript:submitForm(document.forms['patientContext'], 'patientContext__contextClear');return false;" >(clear)</a>
	</div>
	<tags:createField property="patientSearch" component="patientContext" mode="dc"
	fieldStyle="patientContextField" labelAlignment="none" labelStyle="patientContext" dataStyle="patientContext"/>
 
 	<script language="javascript" type="text/javascript">
			acs_object_patientContext_patientSearch.listWidth="355";
			acs_object_patientContext_patientSearch.columnHeaders=['Patient'];
			acs_object_patientContext_patientSearch.listRows=30;
			
			if(document.getElementById("patientContext_patientSearch").options.length > 2){
				
				setTimeout("acs_object_patientContext_patientSearch.toggleList()",50);
			}
	</script>
	<script type="text/javascript">
    	//this appears to be triggered unexpected on page load, maybe because the ACS control fires onChange when
	    //its value is set on page load ?. anyway, it does no harm for now, but something to know about.
		function submitPatientChange() {
			document.patientContext.submit();
		}
		var patientContextBox = ACS['acs_textbox_patientContext_patientSearch'];
		patientContextBox.onChangeCallback = submitPatientChange;
		patientContextBox.onChangeCallbackOwner = this;
	</script>
	
</form>
</div>
