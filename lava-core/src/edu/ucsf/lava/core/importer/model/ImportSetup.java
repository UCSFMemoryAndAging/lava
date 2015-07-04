package edu.ucsf.lava.core.importer.model;

import java.io.Serializable;

/**
 * This class contains properties used during the import of a data file. Note that this
 * is not a persisted class; it just facilitates the import process. 
 *   
 * @author ctoohey
 *
 */public class ImportSetup implements Serializable {
	// storing the ID instead of definition name because the handler must load the ImportDefinition from the 
	// database to process the import
	private Long definitionId; 
	private String notes; // for importLog

	// these are made class properties to facilitate sharing with subclasses
	private ImportDefinition importDefinition;
	public ImportDefinition getImportDefinition() {
		return importDefinition;
	}

	public void setImportDefinition(ImportDefinition importDefinition) {
		this.importDefinition = importDefinition;
	}
	
	// The mapping file of the import definition maps the variable names that are used as column headers in 
	// the data file to LAVA entity property names, where the variable names are specified on row 1, and below
	// each variable name on rows 2 and 3 are the entity name and property name, respectively.

	// The entity name denotes the type of object and is used to find an existing entity of that type or create
	// a new entity of that type. The entity name can be interpreted in various ways during an import by 
	// subclasses of the ImportHandler, in order to map it to a specific LAVA entity type.

	// row 1 of the mapping file
	private String mappingCols[]; // array of the mapping file variable names (i.e. equivalent to data file variable name column headers)
	// row 2 of the mapping file
	private String mappingEntities[]; // array of the mapping file entity names (for instruments this is the instrument name 
									  // and must match the name in the Instrument table)
	// row 3 of the mapping file
	private String mappingProps[]; // array of the mapping file Java property names (case insensitive)
	// row 1 of the data file
	private String dataCols[]; // array of the data file variable names, i.e. column names in the data file
	// row 3 of the data file
	private String dataValues[]; // array of the data file variable values 

	public ImportSetup() {}
	
	public void reset(){};
	
	public Long getDefinitionId() {
		return definitionId;
	}
	
	public void setDefinitionId(Long definitionId) {
		this.definitionId = definitionId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String[] getMappingCols() {
		return mappingCols;
	}

	public void setMappingCols(String[] mappingCols) {
		this.mappingCols = mappingCols;
	}
	
	public String[] getMappingEntities() {
		return mappingEntities;
	}

	public void setMappingEntities(String[] mappingEntities) {
		this.mappingEntities = mappingEntities;
	}

	public String[] getMappingProps() {
		return mappingProps;
	}

	public void setMappingProps(String[] mappingProps) {
		this.mappingProps = mappingProps;
	}

	public String[] getDataCols() {
		return dataCols;
	}

	public void setDataCols(String[] dataCols) {
		this.dataCols = dataCols;
	}

	public String[] getDataValues() {
		return dataValues;
	}

	public void setDataValues(String[] dataValues) {
		this.dataValues = dataValues;
	}
	
	
}
