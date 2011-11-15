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
		return new Object[]{this.getTimepointsBase()};
	}

	/**
	 * Convenience methods to convert ProtocolConfigBase method types to types of this
	 * subclass,  since if an object of this class exists we know we can safely downcast. 
	 */
	public Set<ProtocolTimepointConfig> getTimepoints() {
		return (Set<ProtocolTimepointConfig>) super.getTimepointsBase();
	}
	public void setTimepoints(Set<ProtocolTimepointConfig> protocolTimepoints) {
		super.setTimepointsBase(protocolTimepoints);
	}

	/*
	 * Method to add ProtocolTmepointConfig to this ProtocolConfig, managing 
	 * the bi-directional relationship.
	 */
	public void addTimepoint(ProtocolTimepointConfig protocolTimepointConfig) {
		protocolTimepointConfig.setProtocolConfig(this);
		this.getTimepoints().add(protocolTimepointConfig);
	}	

	private String category;

    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public void orderTimepoints() {
		// iterate thru the timepointConfigs, which have been sorted chronologically by schedWinDaysFromStart, and 
		// assign values to listOrder so the sorting is persisted at the ProtocolConfigTracking level
		// note: the TreeSet structure is used to sort ProtocolTimepointConfig via the ProtocolTimepointConfig
		// compareTo method. the sort is "transferred" to ProtocolTracking by populating listOrder 
		// in the same order. then when a ProtocolTracking query is done, the timepointConfigs can be
		// retrieved using an orderBy on listOrder
		short i = 1;
		for (ProtocolTimepointConfig protocolTimepoint : this.getTimepoints()) {
			protocolTimepoint.setListOrder(i++);
		}
	}
		
}
