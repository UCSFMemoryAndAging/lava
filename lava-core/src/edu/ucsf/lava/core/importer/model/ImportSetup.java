package edu.ucsf.lava.core.importer.model;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

public class ImportSetup implements Serializable {
	private Long definitionId;
	private MultipartFile dataFileInput;
	private String notes; // for importLog

	public ImportSetup() {}
	
	public Long getDefinitionId() {
		return definitionId;
	}
	
	public void setDefinitionId(Long definitionId) {
		this.definitionId = definitionId;
	}

	public MultipartFile getDataFileInput() {
		return dataFileInput;
	}

	public void setDataFileInput(MultipartFile dataFileInput) {
		this.dataFileInput = dataFileInput;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
