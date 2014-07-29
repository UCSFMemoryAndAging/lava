package edu.ucsf.lava.core.importer.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.model.EntityBase;

public class ImportLog extends EntityBase {
	public static String DEBUG_MSG = "DEBUG";
	public static String ERROR_MSG = "ERROR";
	public static String WARNING_MSG = "WARNING";
	public static String INFO_MSG = "INFO";
	
	private Timestamp importTimestamp;
	private String importedBy;
	private LavaFile dataFile;
	private String definitionName;
	private Integer totalRecords;
	// record imported (generally means row inserted, though  populating an "empty" record could 
	// qualify as an import instead of an update 
	private Integer imported; 
	// update to existing data, mutually exclusive with imported   							
	private Integer updated; 
	// record already existed so no import or update
	private Integer alreadyExist;
	// record not imported due to an error
	private Integer errors; 
	// record imported or updated, but with a warning
	private Integer warnings; 
	private String notes; // entered by user when doing the import
	private List<ImportLogMessage> messages = new ArrayList<ImportLogMessage>(); // warnings, errors
	
	public ImportLog(){
		super();
		this.totalRecords = 0;
		this.imported = 0;
		this.updated = 0;
		this.alreadyExist = 0;
		this.errors = 0;
		this.warnings = 0;
		this.importTimestamp = new Timestamp(new Date().getTime());
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

	public LavaFile getDataFile() {
		return dataFile;
	}

	public void setDataFile(LavaFile dataFile) {
		this.dataFile = dataFile;
	}

	public String getDefinitionName() {
		return definitionName;
	}

	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Integer getImported() {
		return imported;
	}

	public void setImported(Integer imported) {
		this.imported = imported;
	}

	public Integer getAlreadyExist() {
		return alreadyExist;
	}

	public Integer getUpdated() {
		return updated;
	}

	public void setUpdated(Integer updated) {
		this.updated = updated;
	}

	public void setAlreadyExist(Integer alreadyExist) {
		this.alreadyExist = alreadyExist;
	}

	public Integer getErrors() {
		return errors;
	}

	public void setErrors(Integer errors) {
		this.errors = errors;
	}

	public Integer getWarnings() {
		return warnings;
	}

	public void setWarnings(Integer warnings) {
		this.warnings = warnings;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<ImportLogMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<ImportLogMessage> messages) {
		this.messages = messages;
	}
	
	public void addMessage(String type, Integer lineNum, String msg) {
		ImportLogMessage logMessage = new ImportLogMessage(type, lineNum, msg);
		this.messages.add(logMessage);
	}
	
	public void addDebugMessage(Integer lineNum, String msg) {
		this.addMessage(DEBUG_MSG, lineNum, msg);
	}
	
	public void addErrorMessage(Integer lineNum, String msg) {
		this.addMessage(ERROR_MSG, lineNum, msg);
	}

	public void addWarningMessage(Integer lineNum, String msg) {
		this.addMessage(WARNING_MSG, lineNum, msg);
	}

	public void addInfoMessage(Integer lineNum, String msg) {
		this.addMessage(INFO_MSG, lineNum, msg);
	}
	
	public void incTotalRecords() {
		this.totalRecords++;
	}
	
	public void incImported() {
		this.imported++;
	}

	public void incUpdated() {
		this.updated++;
	}

	public void incAlreadyExist() {
		this.alreadyExist++;
	}
	
	public void incErrors() {
		this.errors++;
	}
	
	public void incWarnings() {
		this.warnings++;
	}
	
	public String getSummaryBlock() {
		StringBuffer sb = new StringBuffer("Total=").append(this.getTotalRecords()).append(", ");
		sb.append("Imported=").append(this.getImported()).append(", ");
		sb.append("Updated=").append(this.getUpdated()).append("\n");
		sb.append("Already Exists=").append(this.getAlreadyExist()).append(", ");
		sb.append("Errors=").append(this.getErrors()).append(", ");
		sb.append("Warnings=").append(this.getWarnings());
		return sb.toString();
	}

	public static class ImportLogMessage implements Serializable {
		private String type;
		private Integer lineNum;
		private String message;
		
		public ImportLogMessage(){
			super();
		}
		
		public ImportLogMessage(String type, Integer lineNum, String message) {
			this.type = type;
			this.lineNum = lineNum;
			this.message = message;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Integer getLineNum() {
			return lineNum;
		}

		public void setLineNum(Integer lineNum) {
			this.lineNum = lineNum;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}

}
