package edu.ucsf.lava.crms.assessment.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


/**
 * InstrumentMetaData
 * 
 * Maps to the Instrument table so that the metadata is accessible.
 */
public class InstrumentMetadata extends EntityBase {

	public static EntityManager MANAGER = new EntityBase.Manager(InstrumentMetadata.class);
	private String instrName; // not null
	private String tableName; // not null
	private String formName;
	private String category;
	private Boolean hasVersion; // not null
	
	public InstrumentMetadata() {
		super();
		this.setAudited(false);
	}
	
	
	public String getInstrName() {return instrName;}
	public void setInstrName(String instrName) {this.instrName = instrName;}

	public String getTableName() {return tableName;}
	public void setTableName(String tableName) {this.tableName = tableName;}

	public String getFormName() {return formName;}
	public void setFormName(String formName) {this.formName = formName;}

	public String getCategory() {return category;}
	public void setCategory(String category) {this.category = category;}

	public Boolean getHasVersion() {return hasVersion;}
	public void setHasVersion(Boolean hasVersion) {this.hasVersion = hasVersion;}


}
