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
public class ProtocolTimepoint extends ProtocolTimepointBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolTimepoint.class);
	
	public ProtocolTimepoint(){
		super();
	}

	private Boolean firstTimepoint;
	private Boolean optional;
	
	// scheduling window is the window in which the anchor must be scheduled
	private ProtocolTimepoint schedWinAnchorTimepoint; 
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
	 * Convenience method to return Protocol instead of ProtocolBase.
	 */
//	public Protocol getProtocol() {
//		return (Protocol) super.getProtocolBase();
//	}

	public Boolean getFirstTimepoint() {
		return firstTimepoint;
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

	public ProtocolTimepoint getSchedWinAnchorTimepoint() {
		return schedWinAnchorTimepoint;
	}

	public void setSchedWinAnchorTimepoint(ProtocolTimepoint schedWinAnchorTimepoint) {
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
		// 1) if this is the first timepoint, then return 0
		// 2) else, return schedDaysFromAnchor plus the daysFromProtocolStart of the timepoint
		//    to which this timepoint is relative (i.e. its schedWinTpAnchor)

		if (this.getFirstTimepoint()) {
			return 0;
		}
		else if (this.getSchedWinAnchorTimepoint().getFirstTimepoint()) {
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

	
	public int compareTo(ProtocolTimepointBase protocolTimepointBase) throws ClassCastException {
		ProtocolTimepoint protocolTimepoint = (ProtocolTimepoint) protocolTimepointBase;
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
