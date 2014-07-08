package edu.ucsf.lava.core.importer.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
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
	private Integer imported;
	private Integer alreadyExist;
	private Integer errors; 
	private Integer warnings; 
	private String notes; // entered by user when doing the import
	private List<ImportLogMessage> messages = new ArrayList<ImportLogMessage>(); // warnings, errors
	
	public ImportLog(){
		super();
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

	public void incAlreadyExist() {
		this.alreadyExist++;
	}
	
	public void incErrors() {
		this.errors++;
	}
	
	public void incWarnings() {
		this.warnings++;
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
