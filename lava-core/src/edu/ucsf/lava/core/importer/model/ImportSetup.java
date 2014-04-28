package edu.ucsf.lava.core.importer.model;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class ImportSetup implements Serializable {
	private String templateName;
/**	
	<c:if test="${componentView == 'enter'}">
	<tags:fileUpload paramName="rnflFile" component="${component}"/>
this is what fileUpload tag does:	
<input type="file" name="${component}_${paramName}">
	<tags:eventButton buttonText="Upload" action="upload" component="instrument" pageName="${instrTypeEncoded}" className="pageLevelLeftmostButton" locked="${command.components['instrument'].locked}" javascript="document.${instrTypeEncoded}.enctype = 'multipart/form-data';"/>
	<div>&nbsp;</div>
	</c:if>
	<tags:createField property="rnflFilename" component="${component}" entity="${instrTypeEncoded}" labelStyle="shortLeft"/>
	<div>&nbsp;</div>
**/
// above shows that currently we do not bind the MultipartFile to a component property. 
// what is done is the file input control (which includes the Browse.. button) is bound to a general request parameter
// outside the context of the component, and then the following is done:
// MultipartFile rnflFile = context.getRequestParameters().getRequiredMultipartFile("instrument_rnflFile");
// validation checks:
// me:
//	if (rnflFile.getSize() == 0
// jhesse:
//	protected boolean uploadFileParameterExists(RequestContext context, Object command, BindingResult errors){
//		return (null != context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile"));
//	}
	
	
// surely Spring can bind a MultipartFile so look at creating a property here of type MultipartFile and any
// PropertyEditor that may be required
	
	private MultipartFile dataFile;
	private String notes; // for importLog

	public ImportSetup() {}
	
	public String getTemplateName() {
		return templateName;
	}
	
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public MultipartFile getDataFile() {
		return dataFile;
	}

	public void setDataFile(MultipartFile dataFile) {
		this.dataFile = dataFile;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
