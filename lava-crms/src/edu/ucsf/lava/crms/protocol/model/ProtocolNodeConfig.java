package edu.ucsf.lava.crms.protocol.model;

import java.util.Calendar;
import java.util.Date;

import edu.ucsf.lava.crms.model.CrmsEntity;

/**
 * This is the base class of all components that make up the Protocol tree. A Protocol
 * is a hierarchical structure so this class is named ProtocolNodeConfig rather than 
 * ProtocolBaseConfig to emphasize that point.
 * 
 * The tree hierarchy is:
 * ProtocolConfig
 *   ProtocolTimepointConfig
 *     ProtocolVisitConfig (with a collection of ProtocolVisitConfigOption elements)
 *       ProtocolInstrumentConfig (with a collection of ProtocolInstrumentConfigOption elements)
 *       
 * Note that between each of the above classes and this class, there is a ..Base class for each
 * protocol component. This is necessitated by the OR mapping layer. At that layer, the protocol
 * component subclasses do not subclass a common base class because if they did it would 
 * result in polymorphic joins to every protocol subclass when retrieving an object or 
 * objects of just one of the subclasses (because the base mapping is self-referencing via
 * a parent reference, which must generically be mapped to the base class -- and any query involving
 * a mapping with a base class joins to all of its subclasses). Instead, a separate base class exists 
 * for each protocol component type (where each base class plus the ProtocolConfigTracking class map
 * the same base table) and the common properties are included rather than inherited.  
 * 
 * @author ctoohey
 *
 */
public abstract class ProtocolNodeConfig extends CrmsEntity {
	// because every type of protocolConfig tree node subclasses this class, projName is 
	// denormalized (which makes for simpler configuration of project authorization filtering)
	private String projName;
	private String label;
	private String notes;
	private String defaultStaff;
	private Short listOrder;
	private Date effDate;
	private Date expDate;
	
	public ProtocolNodeConfig(){
		super();
		setPatientAuth(false);
		Calendar cal = Calendar.getInstance();
		cal.set(2000, 0, 1);
		this.setEffDate(cal.getTime());
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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
