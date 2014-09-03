package edu.ucsf.lava.core.importer.model;

import java.io.Serializable;

import edu.ucsf.lava.core.file.model.LavaFile;

/**
 * This class contains properties used during the import of a data file. Note that this
 * is not a persisted class; it just facilitates the import process. 
 *   
 * @author ctoohey
 *
 */public class ImportSetup implements Serializable {
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

	private String mappingCols[];
	private String mappingEntities[];
	private String mappingProps[];
	private String dataCols[];
	private String dataValues[];

	public ImportSetup() {}
	
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
