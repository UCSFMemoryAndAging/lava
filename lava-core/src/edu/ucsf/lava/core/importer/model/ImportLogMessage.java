package edu.ucsf.lava.core.importer.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ImportLogMessage extends EntityBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ImportLogMessage.class);

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

