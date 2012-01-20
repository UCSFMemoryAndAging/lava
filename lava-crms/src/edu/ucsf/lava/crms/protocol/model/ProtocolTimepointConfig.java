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
		this.setDuration((short)0);
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{
				this.getProtocolConfigBase().getProtocolTimepointConfigsBase(), 
				this.getProtocolVisitConfigsBase()};
	}
	

	private Boolean optional;
	// scheduling window is the window in which the timepoint must be scheduled
	private ProtocolTimepointConfig schedWinRelativeTimepoint; 
	// schedWinRelativeTimepointId facilitates modifying the schedWinRelativeTimepoint association. the user selects a 
	// schedWinRelativeTimepoint and that schedWinRelativeTimepointId is bound to this property. if it differs from this 
	// entity's existing schedWinRelativeTimepoint id, the schedWinRelativeTimepointId is used to retrieve its schedWinRelativeTimepoint 
	// which is then set on this entity via setSchedWinRelativeTimepoint to change the associated schedWinRelativeTimepoint
	private Long schedWinRelativeTimepointId;
	private Short schedWinRelativeAmount; // 0 for first timepoint
	private Short schedWinRelativeUnits; // days, weeks, months ??
	private Short schedWinRelativeMode; // working days, calendar days, etc.
	private Short schedWinDaysFromStart; // computed from schedWinRelativeTimepoint and schedWinDaysFrom, and used for ordering timepoints
	private Short schedWinSize; //days
	// determines the start of the scheduling window, where 0 is equal to schedAnchorDate (calculated in ProtocolTimepoint), 
	// negative value goes backward in time relative to schedAnchorDate, and positive value goes forward in time.
	private Short schedWinOffset; //days
	private Short duration; // duration of the timepoint in days, where null or 0 indicates same day duration
	private Boolean schedAutomatic; // when this timepoint is complete, automatically schedule the next timepoint
	
	// the primary visit for the timepoint.
	// this will serve as the visit to use for calculating the collect window (note that unlike the
	// scheduling window, there is no collectWinDaysFrom offset; this ProtocolVisitConfig's Visit visitDate
	// is the collectAnchorDate for the timepoint)
	private ProtocolVisitConfig primaryProtocolVisitConfig;
	// primaryProtocolVisitConfigId facilitates modifying the primaryProtocolVisitConfig association. the user selects a 
	// primaryProtocolVisitConfig and that primaryProtocolVisitConfigId is bound to this property. if it differs from this 
	// entity's existing primaryProtocolVisitConfig id, the primaryProtocolVisitConfigId is used to retrieve its primaryProtocolVisitConfig 
	// which is then set on this entity via setPrimaryProtocolVisitConfig to change the associated primaryProtocolVisitConfig
	private Long primaryProtocolVisitConfigId;
	
	private Boolean collectWindowDefined;
	private Short collectWinSize;
	private Short collectWinOffset;

	// repeating timepoint configuration
	// The representation of a Timepoint that that repeats at regular intervals over time. In 
	// the protocol config there is only one instance of this timepoint, but when a patient 
	// is assigned to the protocol, a specified number of timepoints are created. Additional
	// timepoints are created automatically or manually.
	private Boolean repeating;
	private Short repeatInterval; // days
	// initial num timepoints to create when patient assigned to protocol
	private Short repeatInitialNum; 
	// automatically create the next timepoint when the current timepoint is complete
	private Boolean repeatCreateAutomatic;
	
	
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
	public Set<ProtocolVisitConfig> getProtocolVisitConfigs() {
		return (Set<ProtocolVisitConfig>) super.getProtocolVisitConfigsBase();
	}
	public void setProtocolVisitConfigs(Set<ProtocolVisitConfig> protocolVisitConfigs) {
		super.setProtocolVisitConfigsBase(protocolVisitConfigs);
	}
	
	/*
	 * Method to add a ProtocolVisitConfig to a ProtocolTimepointConfig, managing the bi-directional relationship.
	 */	
	public void addProtocolVisitConfig(ProtocolVisitConfig protocolVisitConfig) {
		protocolVisitConfig.setProtocolTimepointConfig(this);
		this.getProtocolVisitConfigs().add(protocolVisitConfig);
	}
	
	/**
	 * Convenience method to check if this is the first timepoint. An association to the first timepoint 
	 * is stored in the ProtocolConfig parent of the ProtocolTimepointConfig collection (as opposed to
	 * having a flag on each ProtocolTimepointConfig to designate itself as the first, which would 
	 * require synchronization among all sibling ProtocolTimepointConfigs)
	 * 
	 * @return true if this is the first timepoint config, false if not.
	 */
	public Boolean isFirstProtocolTimepointConfig() {
		// using the id property here instead of getFirstProtocolTimepointConfig.getId() means do not
		// have to eagerly load the first ProtocolTimepointConfig 
		return this.getProtocolConfig().getFirstProtocolTimepointConfigId().equals(this.getId());
	}

	public Boolean getOptional() {
		return optional;
	}

	public void setOptional(Boolean optional) {
		this.optional = optional;
	}
	
	public ProtocolTimepointConfig getSchedWinRelativeTimepoint() {
		return schedWinRelativeTimepoint;
	}
	
	public void setSchedWinRelativeTimepoint(ProtocolTimepointConfig schedWinRelativeTimepoint) {
		this.schedWinRelativeTimepoint = schedWinRelativeTimepoint;
		if (this.schedWinRelativeTimepoint != null) {
			this.schedWinRelativeTimepointId = this.schedWinRelativeTimepoint.getId();
		}
	}
	
	public Long getSchedWinRelativeTimepointId() {
		return schedWinRelativeTimepointId;
	}
	
	public void setSchedWinRelativeTimepointId(Long schedWinRelativeTimepointId) {
		this.schedWinRelativeTimepointId = schedWinRelativeTimepointId;
	}
	
	public Short getSchedWinRelativeAmount() {
		return schedWinRelativeAmount;
	}

	public void setSchedWinRelativeAmount(Short schedWinRelativeAmount) {
		this.schedWinRelativeAmount = schedWinRelativeAmount;
	}
	
	public Short getSchedWinRelativeUnits() {
		return schedWinRelativeUnits;
	}
	
	public void setSchedWinRelativeUnits(Short schedWinRelativeUnits) {
		this.schedWinRelativeUnits = schedWinRelativeUnits;
	}

	public Short getSchedWinRelativeMode() {
		return schedWinRelativeMode;
	}
	public void setSchedWinRelativeMode(Short schedWinRelativeMode) {
		this.schedWinRelativeMode = schedWinRelativeMode;
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
	
	public Short getDuration() {
		return duration;
	}
	
	public void setDuration(Short duration) {
		this.duration = duration;
	}
	
	public Boolean getSchedAutomatic() {
		return schedAutomatic;
	}
	
	public void setSchedAutomatic(Boolean schedAutomatic) {
		this.schedAutomatic = schedAutomatic;
	}
	
	public ProtocolVisitConfig getPrimaryProtocolVisitConfig() {
		return primaryProtocolVisitConfig;
	}

	public void setPrimaryProtocolVisitConfig(ProtocolVisitConfig primaryProtocolVisitConfig) {
		this.primaryProtocolVisitConfig = primaryProtocolVisitConfig;
		if (this.primaryProtocolVisitConfig != null) {
			this.primaryProtocolVisitConfigId = this.primaryProtocolVisitConfig.getId();
		}
	}

	public Long getPrimaryProtocolVisitConfigId() {
		return primaryProtocolVisitConfigId;
	}

	public void setPrimaryProtocolVisitConfigId(Long primaryProtocolVisitConfigId) {
		this.primaryProtocolVisitConfigId = primaryProtocolVisitConfigId;
	}

	public Boolean getCollectWindowDefined() {
		return collectWindowDefined;
	}

	public void setCollectWindowDefined(Boolean collectWindowDefined) {
		this.collectWindowDefined = collectWindowDefined;
	}

	public Short getCollectWinSize() {
		return collectWinSize;
	}
	
	public void setCollectWinSize(Short collectWinSize) {
		this.collectWinSize = collectWinSize;
	}
	
	public Short getCollectWinOffset() {
		return collectWinOffset;
	}
	
	public void setCollectWinOffset(Short collectWinOffset) {
		this.collectWinOffset = collectWinOffset;
	}
	
	public Short getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(Short repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public Short getRepeatInitialNum() {
		return repeatInitialNum;
	}

	public void setRepeatInitialNum(Short repeatInitialNum) {
		this.repeatInitialNum = repeatInitialNum;
	}

	public Boolean getRepeatCreateAutomatic() {
		return repeatCreateAutomatic;
	}

	public void setRepeatCreateAutomatic(Boolean repeatCreateAutomatic) {
		this.repeatCreateAutomatic = repeatCreateAutomatic;
	}

	public Boolean getRepeating() {
		return repeating;
	}

	public void setRepeating(Boolean repeating) {
		this.repeating = repeating;
	}

	/**
	 * Calculate the scheduling offset of this timepoint relative to earlier timepoints, in one of these ways:
	 *   1) if this is the first timepoint config, then return 0
	 *   2) else, return schedDaysFromStart + WinRelativeAmount (converted to Days) of the timepoint config to which this timepoint 
	 *      config is relative (i.e. its schedWinRelativeTimepoint), by calling this method recursively.
	 * 
	 * @return The days from the protocol start for this timepoint.
	 */
	public Short calcDaysFromProtocolStart() {

		if (this.isFirstProtocolTimepointConfig()) {
			return 0;
		}
		else if (this.getSchedWinRelativeTimepoint().isFirstProtocolTimepointConfig()) {
			return this.getSchedWinRelativeAmount();
		}
		else {
			//TODO: if units are other than days, convert to days. 
			// problem is that there is no actual date involved, so can not use the Java Calendar API, so this is not 
			// a perfect system. however, it is merely used to impose a logical ordering on ProtocolTimepointConfigs so
			// they are listed in a logical order for the user. once a patient is assigned to the protocol and a
			// Protocol structure is created, will be dealing in real dates, so then the Calendar API can be used
			// in converting schedWinRelativeAmount 
			
			// if (this.getSchedWinRelativeUnits().equals(WEEKS)) {
			// }
			// ...
			return (short) (this.getSchedWinRelativeTimepoint().calcDaysFromProtocolStart() + this.getSchedWinRelativeAmount());
		}

		/*** FOR TESTING ONLY !! will blow up if no timepoints. in ProtocolHandler addReferenceData:	
		Timepoint tpFirst = protocol.getFirstTimepoint();
		Timepoint tpLast = protocol.getLastTimepoint();
		// getDuration or something should be a method on Protocol
		Short duration = tpLast.daysFromProtocolStart(tpFirst);
		****/	
	}
	
	public boolean afterCreate() {
		// flag as the first timepoint config if it is the only timepoint config
		if (this.getProtocolConfig().getProtocolTimepointConfigs().size() == 1) {
			this.getProtocolConfig().setFirstProtocolTimepointConfig(this);
			// this is special case as the only place that firstProtocolTimepointConfig gets set on 
			// ProtocolConfig other than its own handler. have to save ProtocolConfig here
			this.getProtocolConfig().save();
		}
		
		// update the property used for ordering ProtocolTimepointConfigs
		this.setSchedWinDaysFromStart(this.calcDaysFromProtocolStart());
		
		// always save again, as node value has been set
		return true;
	}


	public boolean afterUpdate() {
		// update the property used for ordering ProtocolTimepointConfigs
		this.setSchedWinDaysFromStart(this.calcDaysFromProtocolStart());
		// always save again, as node value has been set
		return true;
	}

	
	/**
	 * The ProtocolTimepointConfig collection in ProtocolConfig is a java.util.SortedSet (based on O/R 
	 * mapping configuration), i.e. a java.util.TreeMap. This compareTo method implements the Comparable
	 * interface to set the natural ordering as chronological, i.e. by the days from the scheduling 
	 * window of this ProtocolTimepointConfig from the first ProtocolTimepointConfig (which is considered
	 * time 0). 
	 * 
	 *  Note that this natural ordering gets transferred to the listOrder property so that ordering
	 *  is maintained for ProtocolConfigTracking, the lightweight class used to represent all nodes
	 *  in a ProtocolConfig tree (and which only contains base class members, including listOrder, but
	 *  not schedWinDaysFromStart). ProtocolTimepointConfigHandler calls orderTimepoints method to do this.
	 */
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
