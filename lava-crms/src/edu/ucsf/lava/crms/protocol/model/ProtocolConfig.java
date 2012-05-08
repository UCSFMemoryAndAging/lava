package edu.ucsf.lava.crms.protocol.model;

import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 *
 *
 */
public class ProtocolConfig extends ProtocolConfigBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolConfig.class);
	
	public ProtocolConfig(){
		super();
		this.setAuditEntityType("ProtocolConfig");
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.getProtocolTimepointConfigsBase()};
	}

	/**
	 * Convenience methods to convert ProtocolConfigBase method types to types of this
	 * subclass,  since if an object of this class exists we know we can safely downcast. 
	 */
	public Set<ProtocolTimepointConfig> getProtocolTimepointConfigs() {
		return (Set<ProtocolTimepointConfig>) super.getProtocolTimepointConfigsBase();
	}
	public void setProtocolTimepointConfigs(Set<ProtocolTimepointConfig> protocolTimepointConfigs) {
		super.setProtocolTimepointConfigsBase(protocolTimepointConfigs);
	}

	/*
	 * Method to add ProtocolTmepointConfig to this ProtocolConfig, managing 
	 * the bi-directional relationship.
	 */
	public void addProtocolTimepointConfig(ProtocolTimepointConfig protocolTimepointConfig) {
		protocolTimepointConfig.setProtocolConfig(this);
		this.getProtocolTimepointConfigs().add(protocolTimepointConfig);
	}	

	private String descrip;
	private String category;
	private ProtocolTimepointConfig firstProtocolTimepointConfig;
	// firstProtocolTimepointConfigId facilitates modifying the firstProtocolTimepointConfig association. the user selects a 
	// firstProtocolTimepointConfig and that firstProtocolTimepointConfigId is bound to this property. if it differs from this 
	// entity's existing firstProtocolTimepointConfig id, the firstProtocolTimepointConfigId is used to retrieve its 
	// firstProtocolTimepointConfig which is then set on this entity via setFirstProtocolTimepointConfig to change the 
	// associated firstProtocolTimepointConfig
	private Long firstProtocolTimepointConfigId;

    public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public ProtocolTimepointConfig getFirstProtocolTimepointConfig() {
		return firstProtocolTimepointConfig;
	}

	public void setFirstProtocolTimepointConfig(ProtocolTimepointConfig firstProtocolTimepointConfig) {
		this.firstProtocolTimepointConfig = firstProtocolTimepointConfig;
		if (this.firstProtocolTimepointConfig != null) {
			this.firstProtocolTimepointConfigId = this.firstProtocolTimepointConfig.getId();
		}
		else {
			this.firstProtocolTimepointConfigId = null;
		}
	}

	public Long getFirstProtocolTimepointConfigId() {
		return firstProtocolTimepointConfigId;
	}

	public void setFirstProtocolTimepointConfigId(Long firstProtocolTimepointConfigId) {
		this.firstProtocolTimepointConfigId = firstProtocolTimepointConfigId;
	}

	public void orderTimepoints() {
		// iterate thru the timepointConfigs, which have been sorted chronologically by schedWinDaysFromStart, and 
		// assign values to listOrder so the sorting is persisted at the ProtocolConfigTracking level
		// note: the TreeSet structure is used to sort ProtocolTimepointConfig via the ProtocolTimepointConfig
		// compareTo method. the sort is "transferred" to ProtocolTracking by populating listOrder 
		// in the same order. then when a ProtocolTracking query is done, the timepointConfigs can be
		// retrieved using an orderBy on listOrder
		short i = 1;
		for (ProtocolTimepointConfig protocolTimepointConfig : this.getProtocolTimepointConfigs()) {
			protocolTimepointConfig.setListOrder(i++);
		}
	}

	public void beforeCreate() {
		super.beforeCreate();
		this.setNodeType(PROTOCOL_NODE);
	}

}
