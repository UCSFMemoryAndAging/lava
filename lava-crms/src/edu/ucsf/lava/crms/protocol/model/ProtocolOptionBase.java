package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

/**
 * @author ctoohey
 *
 */
public abstract class ProtocolOptionBase extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolOptionBase.class);
	private String projName;
	private String label; 
	private String descrip;
	private Boolean defaultOption;
	private Date effDate;
	private Date expDate;
	
	public ProtocolOptionBase(){
		super();
		setPatientAuth(false);
	}
	
	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public Boolean getDefaultOption() {
		return defaultOption;
	}

	public void setDefaultOption(Boolean defaultOption) {
		this.defaultOption = defaultOption;
	}

	public Date getEffDate() {
		return effDate;
	}

	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}

	public Date getExpDate() {
		return expDate;
	}

	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}

}
