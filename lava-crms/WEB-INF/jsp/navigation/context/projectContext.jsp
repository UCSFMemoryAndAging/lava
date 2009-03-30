<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>


<div id="projectContext">
<form  name="projectContext" method="post">
	<input type="hidden" name="_eventId" id="_eventId" value="projectContext__contextChange"/>
	
	
		<div id="projectContextLabel">
	Project Filter &nbsp;&nbsp;&nbsp;&nbsp;<a class="projectContextLabel" href="javascript:void" onClick="javascript:submitForm(document.forms['projectContext'], 'projectContext__contextClear');return false;" >(clear)</a>
	</div>
	<tags:createField property="projectName" component="projectContext" mode="dc"
	  fieldStyle="projectContextField" labelAlignment="none" labelStyle="projectContext" dataStyle="projectContext"/>

	<script language="javascript" type="text/javascript">
		acs_object_projectContext_projectName.listWidth="190";
	</script>
</form>  
</div>

<script type="text/javascript">
    //this appears to be triggered unexpected on page load, maybe because the ACS control fires onChange when
    //its value is set on page load ?. anyway, it does no harm for now, but something to know about.
	function submitProjectChange() {
		document.projectContext.submit();
	}
	var projectContextBox = ACS['acs_textbox_projectContext_projectName'];
	projectContextBox.onChangeCallback = submitProjectChange;
	projectContextBox.onChangeCallbackOwner = this;
</script>

