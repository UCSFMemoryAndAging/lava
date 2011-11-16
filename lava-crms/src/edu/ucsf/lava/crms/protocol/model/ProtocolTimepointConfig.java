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
public class ProtocolTimepointConfig extends ProtocolTimepointConfigBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolTimepointConfig.class);
	
	public ProtocolTimepointConfig(){
		super();
		this.setAuditEntityType("ProtocolTimepointConfig");	
	}

	private Boolean firstTimepoint;
	private Boolean optional;
	
	// scheduling window is the window in which the anchor must be scheduled
	private ProtocolTimepointConfig schedWinAnchorTimepoint; 
	// schedWinAnchorTimepointId facilitates modifying the schedWinAnchorTimepoint association. the user selects a 
	// schedWinAnchorTimepoint and that schedWinAnchorTimepointId is bound to this property. if it differs from this 
	// entity's existing schedWinAnchorTimepoint id, the schedWinAnchorTimepointId is used to retrieve its schedWinAnchorTimepoint 
	// which is then set on this entity via setSchedWinAnchorTimepoint to change the associated schedWinAnchorTimepoint
	private Long schedWinAnchorTimepointId;
	private Short schedWinDaysFromAnchor; // 0 for first timepoint
	private Short schedWinDaysFromStart; // computed from anchor, daysFromAnchor
	private Short schedWinSize; //days
	private Short schedWinOffset; //days
	
	/**
	 * Convenience methods to convert ProtocolTimepointConfigBase method types to types of 
	 * this subclass, since if an object of this class exists we know we can safely downcast. 
	 */
	public ProtocolConfig getProtocolConfig() {
		return (ProtocolConfig) super.getProtocolConfigBase();
	}
	public void setProtocolConfig(ProtocolConfig protocol) {
		super.setProtocolConfigBase(protocol);
	}
	public Set<ProtocolVisitConfig> getVisits() {
		return (Set<ProtocolVisitConfig>) super.getVisitsBase();
	}
	public void setVisits(Set<ProtocolVisitConfig> protocolVisits) {
		super.setVisitsBase(protocolVisits);
	}
	
	/*
	 * Method to add a ProtocolVisitConfig to a ProtocolTimepointConfig, managing the bi-directional relationship.
	 */	
	public void addVisit(ProtocolVisitConfig protocolVisit) {
		protocolVisit.setTimepointConfig(this);
		this.getVisits().add(protocolVisit);
	}
	
	public Boolean getFirstTimepoint() {
		return firstTimepoint;
	}
	
	/**
	 * Convenience method since Java beans naming conventions prevents using "is" for
	 * type Boolean, i.e. Spring can not bind property if above method is named "isFirstTimepoint"
	 * 
	 * @return
	 */
	public Boolean isFirst() {
		return this.getFirstTimepoint();
	}

	public void setFirstTimepoint(Boolean firstTimepoint) {
		this.firstTimepoint = firstTimepoint;
	}

	public Boolean getOptional() {
		return optional;
	}

	public void setOptional(Boolean optional) {
		this.optional = optional;
	}

	public ProtocolTimepointConfig getSchedWinAnchorTimepoint() {
		return schedWinAnchorTimepoint;
	}

	public void setSchedWinAnchorTimepoint(ProtocolTimepointConfig schedWinAnchorTimepoint) {
		this.schedWinAnchorTimepoint = schedWinAnchorTimepoint;
		if (this.schedWinAnchorTimepoint != null) {
			this.schedWinAnchorTimepointId = this.schedWinAnchorTimepoint.getId();
		}
	}

	public Long getSchedWinAnchorTimepointId() {
		return schedWinAnchorTimepointId;
	}

	public void setSchedWinAnchorTimepointId(Long schedWinAnchorTimepointId) {
		this.schedWinAnchorTimepointId = schedWinAnchorTimepointId;
	}

	public Short getSchedWinDaysFromAnchor() {
		return schedWinDaysFromAnchor;
	}

	public void setSchedWinDaysFromAnchor(Short schedWinDaysFromAnchor) {
		this.schedWinDaysFromAnchor = schedWinDaysFromAnchor;
	}
	
	public Short getSchedWinDaysFromStart() {
		return schedWinDaysFromStart;
	}

	public void setSchedWinDaysFromStart(Short schedWinDaysFromStart) {
		this.schedWinDaysFromStart = schedWinDaysFromStart;
	}

	public Short getSchedWinSize() {
		return schedWinSize;
	}

	public void setSchedWinSize(Short schedWinSize) {
		this.schedWinSize = schedWinSize;
	}

	public Short getSchedWinOffset() {
		return schedWinOffset;
	}

	public void setSchedWinOffset(Short schedWinOffset) {
		this.schedWinOffset = schedWinOffset;
	}

	public Short calcDaysFromProtocolStart() {
		// calculate the absolute scheduling offset in one of these ways:
		// 1) if this is the first timepoint config, then return 0
		// 2) else, return schedDaysFromAnchor plus the daysFromProtocolStart of the timepoint config
		//    to which this timepoint config is relative (i.e. its schedWinTpAnchor)

		if (this.isFirst()) {
			return 0;
		}
		else if (this.getSchedWinAnchorTimepoint().isFirst()) {
			return this.getSchedWinDaysFromAnchor();
		}
		else {
			return (short) (this.getSchedWinAnchorTimepoint().calcDaysFromProtocolStart() + this.getSchedWinDaysFromAnchor());
		}

		/*** FOR TESTING ONLY !! will blow up if no timepoints. in ProtocolHandler addReferenceData:	
		Timepoint tpFirst = protocol.getFirstTimepoint();
		Timepoint tpLast = protocol.getLastTimepoint();
		// getDuration or something should be a method on Protocol
		Short duration = tpLast.daysFromProtocolStart(tpFirst);
		****/	
	}
	
	public boolean afterCreate() {
		this.setSchedWinDaysFromStart(this.calcDaysFromProtocolStart());
		// always save again, as node value has been set
		return true;
	}


	public boolean afterUpdate() {
		this.setSchedWinDaysFromStart(this.calcDaysFromProtocolStart());
		// always save again, as node value has been set
		return true;
	}

	
	public int compareTo(ProtocolTimepointConfig protocolTimepoint) throws ClassCastException {
		if (this.getSchedWinDaysFromStart() == null && protocolTimepoint.getSchedWinDaysFromStart() == null) {
			return 0;
		}
		else if (this.getSchedWinDaysFromStart() != null && protocolTimepoint.getSchedWinDaysFromStart() == null) {
			return 1;
		}
		else if (this.getSchedWinDaysFromStart() == null && protocolTimepoint.getSchedWinDaysFromStart() != null) {
			return -1;
		}
		
        if (this.getSchedWinDaysFromStart() > protocolTimepoint.getSchedWinDaysFromStart())
            return 1;
        else if (this.getSchedWinDaysFromStart() < protocolTimepoint.getSchedWinDaysFromStart())
            return -1;
        else
            return 0;    
	}
	
	

}
