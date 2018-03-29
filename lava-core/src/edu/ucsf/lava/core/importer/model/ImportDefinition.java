package edu.ucsf.lava.core.importer.model;

import java.util.Date;

import edu.ucsf.lava.core.file.model.ImportFile;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ImportDefinition extends EntityBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ImportDefinition.class);

	public static String CSV_FORMAT = "CSV";
	public static String TAB_FORMAT = "TAB";
	// M/d/yyyy seems to be the most common for hand entered data files, and MM/dd/yyyy for computer-generated data files 	
	public static String DEFAULT_DATE_FORMAT = "M/d/yyyy";
	// default time format to 24 hour time and which should be default (note that time values could be in their own column in
	// the data file, or in the same column as the date)
	public static String DEFAULT_TIME_FORMAT = "HH:mm";
	public static String SKIP_INDICATOR = "SKIP:";
	public static String STATIC_INDICATOR = "STATIC:";
	
	private String name;
	private String category; // not currently used. available for future use.
	private ImportFile mappingFile;
	private String dataFileFormat; // "CSV", "TAB", etc.
	private Short startDataRow; // the first row of data, following column header row and possibly other rows
	// assuming all date fields in an import data file would have same format, e.g. birthDate, visitDate, dcDate
	private String dateFormat; // default: MM/dd/yyyy
	private String timeFormat; // default hh:mm
	//TODO: access metadata for max length of string/text properties, and then implement this flag whether 
	//imported textual data that is too long should be automatically truncated to the max length, or whether
	//there should be an error and the record is not imported
	private Short truncate;  
	private Date created;
	private String createdBy;
	private String notes;

	public ImportDefinition(){
		super();
		this.setAuditEntityType("ImportDefinition");
		this.dataFileFormat = CSV_FORMAT;
		this.startDataRow = 2; // default
		this.dateFormat = DEFAULT_DATE_FORMAT;
		this.timeFormat = DEFAULT_TIME_FORMAT;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public ImportFile getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(ImportFile mappingFile) {
		this.mappingFile = mappingFile;
	}

	public String getDataFileFormat() {
		return dataFileFormat;
	}

	public void setDataFileFormat(String dataFileFormat) {
		this.dataFileFormat = dataFileFormat;
	}
	
	public Short getStartDataRow() {
		return startDataRow;
	}

	public void setStartDataRow(Short startDataRow) {
		this.startDataRow = startDataRow;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	
	public Short getTruncate() {
		return truncate;
	}

	public void setTruncate(Short truncate) {
		this.truncate = truncate;
	}

	
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public static ImportDefinition findOneById(Long definitionId){
		return (ImportDefinition) MANAGER.getById(ImportDefinition.class, definitionId);
	}
	
}
