package edu.ucsf.lava.core.importer.model;

import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ImportDefinition extends EntityBase {
	
	public static EntityManager MANAGER = new EntityBase.Manager(ImportDefinition.class);

	private String name;
	private String category; // not currently used. available for future use.
	
	private LavaFile mappingFile;
	
	//TODO: may need a format variable for TAB or CSV but first can try to see if can determine 
	//it automatically by reading first line of data file and search for tabs or commas (but 
	//then, could have commas within the data which causes problems, so probably yes on a 
	//format property
	
	private String notes;

	public ImportDefinition(){
		super();
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
	
	public LavaFile getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(LavaFile mappingFile) {
		this.mappingFile = mappingFile;
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
