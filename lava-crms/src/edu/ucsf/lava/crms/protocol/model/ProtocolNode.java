package edu.ucsf.lava.crms.protocol.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

/**
 * This is the base class of all nodes which make up a protocol definition. A protocol
 * definition is a tree, so this class is named ProtocolNode rather than ProtocolBase
 * to emphasize that point.
 * 
 * The tree hierarchy is:
 * Protocol
 *   Timepoint (subclass: AssessmentTimepoint or RepeatingTimepoint)
 *     Visit (subclass: Visit) with collection of VisitOption
 *       Instrument (subclass: Instrument) with collection of InstrumentOption
 * 
 * @author ctoohey
 *
 */
public abstract class ProtocolNode extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolNode.class);
	// projName is denormalized and stored in the node so every level of the protocol tree stores
	// the projName for efficient project authorization filtering
	private String projName;
	private String label;
	private String descrip;
	private String defaultStaff;
	private Short listOrder;
	private Date effDate;
	private Date expDate;
	
	public ProtocolNode(){
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

	public String getDefaultStaff() {
		return defaultStaff;
	}

	public void setDefaultStaff(String defaultStaff) {
		this.defaultStaff = defaultStaff;
	}
	
	public Short getListOrder() {
		return listOrder;
	}

	public void setListOrder(Short listOrder) {
		this.listOrder = listOrder;
	}

}
