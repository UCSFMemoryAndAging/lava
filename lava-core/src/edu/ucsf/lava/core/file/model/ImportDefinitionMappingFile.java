package edu.ucsf.lava.core.file.model;

public class ImportDefinitionMappingFile extends LavaFile {
	// non-persistent field used to carry import definitionName to be used in
	// the ImportRepositoryStrategy generateId to incorporate definitionName into
	// the directory path where file is saved
	private String definitionName;
	
	public ImportDefinitionMappingFile() {
		super();
	}

	public String getDefinitionName() {
		return definitionName;
	}

	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}
	
}
