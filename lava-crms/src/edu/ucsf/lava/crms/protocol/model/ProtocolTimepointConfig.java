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
	public static String SCHED_WIN_UNITS_DAYS = "Days";
	public static String SCHED_WIN_UNITS_WEEKS = "Weeks";
	public static String SCHED_WIN_UNITS_MONTHS = "Months";
	public static String SCHED_WIN_UNITS_YEARS = "Years";
	
	public ProtocolTimepointConfig(){
		super();
		this.setAuditEntityType("ProtocolTimepointConfig");
		// defaults
		this.setDuration((short)1);
		this.setSchedWinRelativeUnits(SCHED_WIN_UNITS_DAYS);
		this.setSchedWinRelativeWeekend(Boolean.FALSE); // do not count weekend days
		this.setSchedWinRelativeHoliday(Boolean.FALSE); // do not count holidays
		this.setRepeatIntervalUnits(SCHED_WIN_UNITS_DAYS); 
		this.setRepeatIntervalWeekend(Boolean.FALSE); // do not count weekend days
		this.setRepeatIntervalHoliday(Boolean.FALSE); // do not count holidays
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{
				this.getProtocolConfigBase().getProtocolTimepointConfigsBase(), 
				this.getProtocolVisitConfigsBase()};
	}
	

	// scheduling window is the window in which the timepoint must be scheduled
	private ProtocolTimepointConfig schedWinRelativeTimepoint; 
	// schedWinRelativeTimepointId facilitates modifying the schedWinRelativeTimepoint association. the user selects a 
	// schedWinRelativeTimepoint and that schedWinRelativeTimepointId is bound to this property. if it differs from this 
	// entity's existing schedWinRelativeTimepoint id, the schedWinRelativeTimepointId is used to retrieve its schedWinRelativeTimepoint 
	// which is then set on this entity via setSchedWinRelativeTimepoint to change the associated schedWinRelativeTimepoint
	private Long schedWinRelativeTimepointId;
	private Short schedWinRelativeAmount; // 0 for first timepoint
	private String schedWinRelativeUnits; // Days  TODO: unimplemented: Weeks, Months, Years
	private Boolean schedWinRelativeWeekend; // TODO: if flag not set (default) and Units=DAYS, do not count weekends  
	private Boolean schedWinRelativeHoliday; // TODO: if flag not set (default) and Units=DAYS, do not count holidays  
	private Short schedWinDaysFromStart; // computed from schedWinRelativeTimepoint and schedWinDaysFrom, and used for ordering timepoints
	private Short schedWinSize; //days
	// determines the start of the scheduling window, where 0 is equal to schedAnchorDate (calculated in ProtocolTimepoint), 
	// negative value goes backward in time relative to schedAnchorDate, and positive value goes forward in time.
	private Short schedWinOffset; //days
	private Boolean schedAutomatic; // when this timepoint is complete, automatically schedule the next timepoint
	
	// the primary visit for the timepoint.
	// this will serve as the visit to use for calculating the collect window (note that unlike the
	// scheduling window, there is no collectWinDaysFrom offset; this ProtocolVisitConfig's Visit visitDate
	// is the collectWinAnchorDate for the timepoint)
	private ProtocolVisitConfig primaryProtocolVisitConfig;
	// primaryProtocolVisitConfigId facilitates modifying the primaryProtocolVisitConfig association. the user selects a 
	// primaryProtocolVisitConfig and that primaryProtocolVisitConfigId is bound to this property. if it differs from this 
	// entity's existing primaryProtocolVisitConfig id, the primaryProtocolVisitConfigId is used to retrieve its primaryProtocolVisitConfig 
	// which is then set on this entity via setPrimaryProtocolVisitConfig to change the associated primaryProtocolVisitConfig
	private Long primaryProtocolVisitConfigId;
	
	private Boolean collectWindowDefined;
	private Short collectWinSize;
	private Short collectWinOffset;
	
	// duration for the timepoint, if any, in units of days (calendar days). if a timepoint is complete within a day
	// the duration is 1
	private Short duration;

	// repeating timepoint configuration
	// The representation of a Timepoint that that repeats at regular intervals over time. In 
	// the protocol config there is only one instance of this timepoint, but when a patient 
	// is assigned to the protocol, a specified number of timepoints are created. Additional
	// timepoints are created automatically or manually.
	// repeating property is in tracking superclass for efficient querying limited to tracking table
	private String repeatType; // ABSOLUTE (always compute anchor from first) or RELATIVE (always compute anchor from prior repeating timepoint)
	private Short repeatInterval; // days
	private String repeatIntervalUnits; // Days  TODO: unimplemented: Weeks, Months, Years
	private Boolean repeatIntervalWeekend; // TODO: if flag not set (default) and Units=DAYS, do not count weekends  
	private Boolean repeatIntervalHoliday; // TODO: if flag not set (default) and Units=DAYS, do not count holidays  
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
	 * note: using the id property here instead of getFirstProtocolTimepointConfig.getId() means do not
	 * have to eagerly load the first ProtocolTimepointConfig
	 * 
	 * @return true if this is the first timepoint config, false if not.
	 */
	public Boolean isFirstProtocolTimepointConfig() {
		// if adding, no id since it has not been saved yet
		if (this.getId() == null) {
			// if there is not a first timepoint config yet, then this will be the first, as the business rule is that
			// if there is only one timepoint config it must be marked as the first
			if (this.getProtocolConfig().getFirstProtocolTimepointConfigId() == null) {
				return Boolean.TRUE;
			}
			else {
				return Boolean.FALSE;
			}
		}
		else {
			// existing ProtocolTimepointConfig, so can check the ProtocolConfig to see if this is the first 
			return this.getProtocolConfig().getFirstProtocolTimepointConfigId().equals(this.getId());
		}
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
	
	public String getSchedWinRelativeUnits() {
		return schedWinRelativeUnits;
	}
	
	public void setSchedWinRelativeUnits(String schedWinRelativeUnits) {
		this.schedWinRelativeUnits = schedWinRelativeUnits;
	}

	public Boolean getSchedWinRelativeWeekend() {
		return schedWinRelativeWeekend;
	}

	public void setSchedWinRelativeWeekend(Boolean schedWinRelativeWeekend) {
		this.schedWinRelativeWeekend = schedWinRelativeWeekend;
	}

	public Boolean getSchedWinRelativeHoliday() {
		return schedWinRelativeHoliday;
	}

	public void setSchedWinRelativeHoliday(Boolean schedWinRelativeHoliday) {
		this.schedWinRelativeHoliday = schedWinRelativeHoliday;
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
	
	public Short getDuration() {
		return duration;
	}

	public void setDuration(Short duration) {
		this.duration = duration;
	}

	public String getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(String repeatType) {
		this.repeatType = repeatType;
	}

	public Short getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(Short repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	
	public String getRepeatIntervalUnits() {
		return repeatIntervalUnits;
	}

	public void setRepeatIntervalUnits(String repeatIntervalUnits) {
		this.repeatIntervalUnits = repeatIntervalUnits;
	}

	public Boolean getRepeatIntervalWeekend() {
		return repeatIntervalWeekend;
	}

	public void setRepeatIntervalWeekend(Boolean repeatIntervalWeekend) {
		this.repeatIntervalWeekend = repeatIntervalWeekend;
	}

	public Boolean getRepeatIntervalHoliday() {
		return repeatIntervalHoliday;
	}

	public void setRepeatIntervalHoliday(Boolean repeatIntervalHoliday) {
		this.repeatIntervalHoliday = repeatIntervalHoliday;
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
/** this is just a simplified case of the next case, so prob get rid of it to prevent confusion 		
		else if (this.getSchedWinRelativeTimepoint().isFirstProtocolTimepointConfig()) {
			return this.getSchedWinRelativeAmount();
		}
**/		
		else {
			//TODO: if units are other than days, convert to days. 
			// problem is that there is no actual date involved, so can not use the Java Calendar API, so this is not 
			// a perfect system. however, it is merely used to impose a logical ordering on ProtocolTimepointConfigs so
			// they are listed in a logical order for the user. once a patient is assigned to the protocol and a
			// Protocol structure is created, will be dealing in real dates, so then the Calendar API can be used
			// in converting schedWinRelativeAmount 
			//UPDATE: could hypothetically create a Calendar instance for July 1st, 2000, then add schedWinRelativeAmount
			//based on Mode/Units per technique in ProtocolTimepoint calcSchedAnchorDate to get a Date, and then
			//get days between them (where this is just the absolute value of days for ordering purposes so do
			//not have to further convert this number of days to number of working days)
			//HOWEVER, if Days, then this would not work, as have to removed weekends/holidays (if flagged that way)
			//but then when get an end Date, do the diff from beginning Date to get absolute days
			
			// if (this.getSchedWinRelativeUnits().equals(WEEKS)) {
			// }
			// ...
			return (short) (this.getSchedWinRelativeTimepoint().calcDaysFromProtocolStart() + this.getSchedWinRelativeAmount());
		}
	}
	
	protected void updateSummary() {
		StringBuffer block = new StringBuffer();
		if (isFirstProtocolTimepointConfig()) { 
			block.append("First Timepoint\n");
		}
		else {
			block.append(getSchedWinRelativeAmount()).append(" ").append(getSchedWinRelativeUnits()).append(" relative to: ").append(getSchedWinRelativeTimepoint().getLabel()).append("\n");
		}
		if (getCollectWindowDefined()) {
			block.append("Collect Window Size:").append(getCollectWinOffset()).append("  Offset:").append(getCollectWinOffset()).append("\n");
		}
		if (getRepeating() != null && getRepeating()) {
			block.append("Repeating every ").append(getRepeatInterval()).append(" Days").append("\n");
		}
		if (this.getOptional()) {
			block.append("(optional)");
		}
		this.setSummary(block.toString());
	}

	
	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		
		// update the property used for ordering ProtocolTimepointConfigs
		this.setSchedWinDaysFromStart(this.calcDaysFromProtocolStart());
		
		this.updateSummary();
	}

	public void beforeCreate() {
		super.beforeCreate();
		this.setNodeType(TIMEPOINT_NODE);
	}

	public boolean afterCreate() {
		// flag as the first timepoint config if it is the only timepoint config
		// note: have to do in afterCreate because need the id of the newly created entity
		if (this.getProtocolConfig().getFirstProtocolTimepointConfigId() == null) {
			this.getProtocolConfig().setFirstProtocolTimepointConfig(this);
			// this is special case as the only place that firstProtocolTimepointConfig gets set on 
			// ProtocolConfig other than its own handler. have to save ProtocolConfig here
			this.getProtocolConfig().save();
		}
		
		// do not need to save this entity, just the parent explicitly saved above
		return false;
	}

	
	/**
	 * The ProtocolTimepointConfig collection in ProtocolConfig is a java.util.Set (based on O/R 
	 * mapping configuration), i.e. a java.util.TreeSet. This compareTo method implements the Comparable
	 * interface to set the natural ordering as chronological, i.e. by the days from the scheduling 
	 * window of this ProtocolTimepointConfig from the first ProtocolTimepointConfig (which is considered
	 * time 0). 
	 * 
	 *  Note that this natural ordering gets transferred to the listOrder property so that ordering
	 *  is maintained for ProtocolConfigTracking, the lightweight class used to represent all nodes
	 *  in a ProtocolConfig tree (and which only contains base class members, including listOrder, but
	 *  not schedWinDaysFromStart). ProtocolTimepointConfigHandler calls orderTimepoints method to do this.
	 */
	public int compareTo(ProtocolTimepointConfig protocolTimepointConfig) throws ClassCastException {
		if (this.getSchedWinDaysFromStart() == null && protocolTimepointConfig.getSchedWinDaysFromStart() == null) {
			return 0;
		}
		else if (this.getSchedWinDaysFromStart() != null && protocolTimepointConfig.getSchedWinDaysFromStart() == null) {
			return 1;
		}
		else if (this.getSchedWinDaysFromStart() == null && protocolTimepointConfig.getSchedWinDaysFromStart() != null) {
			return -1;
		}
		
        if (this.getSchedWinDaysFromStart() > protocolTimepointConfig.getSchedWinDaysFromStart())
            return 1;
        else if (this.getSchedWinDaysFromStart() < protocolTimepointConfig.getSchedWinDaysFromStart())
            return -1;
        else
            return 0;    
	}
	
	

}
