package edu.ucsf.lava.core.importer.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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
	private String mappingCols[]; // array of the mapping file variable names
	// row 2 of the mapping file
	private String mappingEntities[]; // array of the mapping file entity names (for instruments this is the instrument name 
					  // and must match the name in the Instrument table). if this is blank/null then it defaults
					  // to the first instrument name in the ImportDefinition
	// row 3 of the mapping file
	private String mappingProps[]; // array of the mapping file Java property names (case insensitive)
	// row 1 of the data file
	private String dataCols[]; // array of the data file variable names, i.e. column names in the data file
	// row 3 of the data file
	private String dataValues[]; // array of the data file variable values 

	// map of mapping file index to a data file index, to be used in setting key data indices and setting the data 
	// file variable values on entity properties.
	// note: multiple mapping file indices could map to the same data file index, meaning that a given imported
	//       data variable value could be set on multiple entity properties
	// note: the index for any default value mappings in the mapping file will not be mapped to a data file index
	private Map<Integer,Integer> mappingColDataCol = new HashMap<Integer,Integer>();

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
	
	public Map<Integer, Integer> getMappingColDataCol() {
		return mappingColDataCol;
	}

	public void setMappingColDataCol(Map<Integer, Integer> mappingColDataCol) {
		this.mappingColDataCol = mappingColDataCol;
	}	
}
