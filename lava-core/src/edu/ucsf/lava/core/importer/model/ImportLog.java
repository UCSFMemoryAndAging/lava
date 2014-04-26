package edu.ucsf.lava.core.importer.model;

import java.sql.Timestamp;

import edu.ucsf.lava.core.model.EntityBase;

public class ImportLog extends EntityBase {
	private String filename;
	private String templateName;
	private Timestamp importTimestamp;
	private String importedBy;
//TODO:	decide if want to persist the individual record warning/failure messages. seems unnecessary for long-term
//use but could be very useful in debugging issues
// if so implement collection for individual record-level messages
//	private List<String> msgs; // warnings, errors
	
	public ImportLog(){
		super();
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Timestamp getImportTimestamp() {
		return importTimestamp;
	}

	public void setImportTimestamp(Timestamp importTimestamp) {
		this.importTimestamp = importTimestamp;
	}

	public String getImportedBy() {
		return importedBy;
	}

	public void setImportedBy(String importedBy) {
		this.importedBy = importedBy;
	}

	
}
