package edu.ucsf.lava.crms.protocol.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
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
		this.setAuditEntityType("ProtocolTimepoint");	
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{
				// note that its parent (Protocol) and configuration (ProtocolTimepointConfig) are eagerly fetched
				// so no need to initialize those
				this.getPatient(), 
				this.getProtocolVisitsBase(),
				this.getProtocolBase().getProtocolTimepointsBase()};
	}

	// the following should not be in the base class with the other custom collection window properties 
	// because it references ProtocolVisit and so ProtocolTracking queries would then always query 
	// ProtocolVisit, which sets off a chain of other queries
	private ProtocolVisit primaryProtocolVisit; // this is set in ProtocolHandler doSaveAdd when Protocol assigned to a Patient
	
	private Short newRepeatNum; // this is not persisted. only for user input.
	
	/**
	 * Convenience methods to convert ProtocolTimepointBase method types to types of this subclass,
	 * since if an object of this class exists we know we can safely downcast. 
	 */
	public Protocol getProtocol() {
		return (Protocol) super.getProtocolBase();
	}
	public void setProtocol(Protocol protocol) {
		super.setProtocolBase(protocol);
	}
	public ProtocolTimepointConfig getProtocolTimepointConfig() {
		return (ProtocolTimepointConfig) super.getProtocolTimepointConfigBase();
	}
	public void setProtocolTimepointConfig(ProtocolTimepointConfig protocolTimepointConfig) {
		super.setProtocolTimepointConfigBase(protocolTimepointConfig);
	}
	public Set<ProtocolVisit> getProtocolVisits() {
		return (Set<ProtocolVisit>) super.getProtocolVisitsBase();
	}
	public void setProtocolVisits(Set<ProtocolVisit> protocolVisits) {
		super.setProtocolVisitsBase(protocolVisits);
	}

	/*
	 * Method to add a ProtocolVisit to a ProtocolTimepoint, managing the bi-directional relationship.
	 */
	public void addProtocolVisit(ProtocolVisit protocolVisit) {
		protocolVisit.setProtocolTimepoint(this);
		this.getProtocolVisits().add(protocolVisit);
	}
	
	public ProtocolVisit getPrimaryProtocolVisit() {
		return primaryProtocolVisit;
	}

	public void setPrimaryProtocolVisit(ProtocolVisit primaryProtocolVisit) {
		this.primaryProtocolVisit = primaryProtocolVisit;
	}

	public Short getNewRepeatNum() {
		return newRepeatNum;
	}

	public void setNewRepeatNum(Short newRepeatNum) {
		this.newRepeatNum = newRepeatNum;
	}

	/**
	 * Calculate the date that the timepoint would be anchored on for scheduling visits. This calculation 
	 * should be done whenever the configuration is changed. The date is persisted in the ProtocolTimepoint 
	 * schedWinAnchorDate property, and is used to determine the scheduling window.
	 * 
	 * If this is the first ProtocolTimepoint, it is the beginning of the protocol, so its schedWinAnchorDate
	 * is the visit date of the Visit assigned to this timepoint's primary ProtocolVisit (designated 
	 * by primaryProtocolVisit). If this visit is not scheduled, then the first timepoint and any subsequent 
     * timepoints can NOT have a schedWinAnchorDate.
     * 
     * If this is not the first timepoint, then its schedWinAnchorDate is relative to an earlier timepoint,
     * (per the ProtocolTimepointConfig for this timepoint) and its schedWinAnchorDate is the 
     * schedWinAnchorDate of the timepoint to which is relative plus the amount of time between timeoints
     * (also per the ProtocolTimepointConfig for this timepoint). 
     * 
     * If the timepoint to which this timepoint is relative does not have a Visit assigned, then this method
     * is called recursively to compute the schedWinAnchorDate for that timepoint, and so on. If the 
     * recursive calls continue until this method is called on the first timepoint, and the first timepoint
     * does not have an assigned Visit, then the patient has not started the protocol, and the 
     * schedWinAnchorDate can not be computed for any timepoints.  
     * 
     * Primary Visit
     * Timepoints can have multiple visits, so which visit should be used to compute the schedWinAnchorDate?
     * Only one can be used, and so the one that is flagged as the primary visit for the timepoint (per the
     * ProtocolTimepointConfig for a timepoint).
     * 
     * Repeating Timepoints
     * Repeating timepoints are handled differently. All repeating timepoints share the same timepoint
     * configuration. The first repeating timepoint is relative to a timepoint in the same way a regular
     * timepoint is relative, and so the computation for schedWinAnchorDate is the same for the first
     * repeating timepoint (except that the amount of time added is the repeatInterval). For subsequent 
     * repeating timepoints there is a flag, repeatType, that is used in the computation. 
     * If repeatType = 'relative' then the repeating timepoint is relative to the prior repeating 
     * timepoint, so a special algorithm finds the prior timepoint. 
     * If repeatType = 'absolute' then the repeating timepoint is computed base on the timepoint to 
     * which the first repeating timepoint is relative (i.e. the relative timepoint defined in the
     * timepoint config for the repeating timepoints).
     * e.g. 
     * Repeating Timepoint Configuration is relative to Timepoint "Treament" with repeatInterval of
     * 1 month
     * Timepoint "Treatment" has Visit assigned with VisitDate '4/1/12'
     * 1st repeating Timepoint has schedWinAnchorDate '5/1/12' and Visit assigned with VisitDate '5/3/12'
     * For 2nd repeating timepoint, if repeatType = 'absolute', schedWinAnchorDate is '6/1/12'. if 
     * repeatType = 'relative', schedWinAnchorDate is '6/3/12'. 
     * 
     * 'ideal' argument
     * The ideal argument instructs this method to compute what would be the ideal schedWinAnchorDate
     * vs. the actual schedWinAnchorDate. The difference is that the ideal calculation only uses the 
     * assigned Visit for the first timepoint and then uses timepoint scheduling window configuration data
     * to compute the schedWinAnchorDate for all other timepoints, whereas the actual calculation 
     * will use the Visit assignment for any involved timepoint, but if an involved timepoint does not
     * have a Visit assigned, then it uses the scheduling window config data.
     * e.g.
     * First Timepoint = "Baseline"
     * Second Timepoint = "1 Year" which is 12 months from "Baseline"
     * Third Timepoint = "2 Year" which is 12 months from "1 Year"
     * "Baseline" Visit assigned with VisitDate = '3/1/12'
     * "1 Year" Visit assigned with VisitDate = '4/20/13'
     * For "2 Year", actual schedWinAnchorDate is '4/20/14', the ideal schedWinAnchorDate is '3/1/14'
     * The ideal calculated values are shown in the deviation status list to see how a given patient
     * protocol deviated from the configured protocol. 
     * 
     * Duration
     * Timepoints can be configured with a duration. When the schedWinAnchorDate is being computed, it
     * should be computed from the end of the timepoint to which it is relative. For timepoints which
     * are completed in a single day, the duration is 1.
     *  
     * @param   ideal calculate the ideal scheduling window anchor date instead of the actual 
	 * @return The calculated value of schedWinAnchorDate for this timepoint, as a java.util.Date, or null
	 * 			if the primary visit for the first timepoint has not been scheduled, i.e. the patient
	 * 			has not started the protocol
	 */
	public Date calcSchedWinAnchorDate(boolean ideal) {
		
		// if this is a subsequent timepoint, i.e. not the first, then the date is the date of the timepoint 
		// to which this timepoint's scheduling window is relative, plus the schedWinRelativeAmount (units 
		// in schedWinRelativeUnits)
		
		if (this.getProtocolTimepointConfig().isFirstProtocolTimepointConfig()) {
			if (this.getPrimaryProtocolVisit() == null || this.getPrimaryProtocolVisit().getVisit() == null) {
				return null;
			}
			else{
				return this.getPrimaryProtocolVisit().getVisit().getVisitDate();
			}
		} else {
			// determine the timepoint to which this timepoint is relative
			Long protocolRelativeTimepointId = null;
			
			ProtocolTimepointConfig relativeProtocolTimepointConfig = null; 
			
			if (this.getProtocolTimepointConfig().getRepeating() != null 
					&& this.getProtocolTimepointConfig().getRepeating() && this.getRepeatNum() > 1) {
				// this is a repeating timepoint that is not the first in the chain. the prior repeating timepoint
				// is timepoint to which this timepoint is relative, so find it. repeating timepoints share the
				// same ProtocolTimepointConfig, so find the first timepoint with the same config, then keep
				// iterating until find the timepoint with a repeatNum of one less than this timepoint

				// note: repeating timepoints that are the first in the chain are relative to another timepoint
				// in the usual way, so drop down below to determine its relative timepoint
				
				// all repeating timepoints share the same timepoint config. this is used below in case there is a
				// duration defined for the timepoint (for the primary visit of the timepoint)
				relativeProtocolTimepointConfig = this.getProtocolTimepointConfig();
				
				for (ProtocolTimepoint protocolTimepoint : this.getProtocol().getProtocolTimepoints()) {
					if (this.getProtocolTimepointConfig().getId().equals(protocolTimepoint.getProtocolTimepointConfig().getId()) 
							&& protocolTimepoint.getRepeatNum().equals((short)(this.getRepeatNum() - 1))) {
						protocolRelativeTimepointId = protocolTimepoint.getId();
						break;
					}
				}
			}
			else {
				relativeProtocolTimepointConfig = this.getProtocolTimepointConfig().getSchedWinRelativeTimepoint();
				// to get from the ProtocolTimepointConfig to its corresponding ProtocolTimepoint, iterate thru the latter 
				// until find it
				for (ProtocolTimepoint protocolTimepoint : this.getProtocol().getProtocolTimepoints()) {
					if (relativeProtocolTimepointConfig.getId().equals(protocolTimepoint.getProtocolTimepointConfig().getId())) {
						protocolRelativeTimepointId = protocolTimepoint.getId();
						break;
					}
				}
			}
			
			// other than the first ProtocolTimepointConfig, schedWinRelativeTimepoint should not be null because it
			// is conditionally enforced as a required field. if here, this is not the first timepoint, so should not have
			// to check this for null

			// if the timepoint to which this is relative has an assigned Visit, then that visit's date is the anchor
			// from which this timepoint anchor should be computed. if it does not have an assigned Visit, call 
			// recursively with the timepoint to which this is relative. 
	 		
			// in other words, the decision as to whether a timepoint should be relative to the first timepoint or to 
			// a subsequent timepoint matters here: e.g. if all timepoints relative to the first timepoint, once the 
			// first timepoint has an assigned Visit all of these timepoints will have a schedWinAnchorDate relative to 
			// that first timepoint regardless of when the scheduled Visits of intervening timepoints are --- but if each 
			// timepoint is relative to the prior timepoint, it will get an initial "ideal" schedWinAnchorDate when the 
			// first timepoint Visit is assigned, but its schedWinAnchorDate would change once the timepoint to which it 
			// is relative gets a Visit assigned
			
			// even though this entity is already loaded due to the current object graph in memory, need to retrieve the 
			// entity to initialize its object graph which will be used in this recursive call 
			ProtocolTimepoint protocolRelativeTimepoint = (ProtocolTimepoint) MANAGER.getById(protocolRelativeTimepointId);
			Date relativeTimepointSchedAnchorDate = null;

			// the ideal calculation is used if the 'ideal' flag is set, if this is a repeating timepoint and repeatType
			// is configured as "absolute", or if there is no Visit assigned to timepoint to which this timepoint is 
			// relative such that can not compute relative to that timepoint, so have to compute the ideal date
			// using scheduling window configuration and going back relative to a timepoint that does have a Visit
			// assigned (if go all the way back to the first timepoint and there is no Visit assigned, then the patient
			// has not started the protocol and can not compute schedWinAnchorDate)
			if (ideal || 
				(this.getProtocolTimepointConfig().getRepeating() && this.getProtocolTimepointConfig().getRepeatType().equals("absolute")) ||
				(protocolRelativeTimepoint.getPrimaryProtocolVisit() == null || protocolRelativeTimepoint.getPrimaryProtocolVisit().getVisit() == null)) {
				// ideal timepoint anchor calculation
				// for idealSchedWinAnchorDate, always do this else, never do about then where use an actual visit date				
				relativeTimepointSchedAnchorDate = protocolRelativeTimepoint.calcSchedWinAnchorDate(ideal);
			}
			else {
				relativeTimepointSchedAnchorDate = protocolRelativeTimepoint.getPrimaryProtocolVisit().getVisit().getVisitDate();
			}

			// now that have a date for the timepoint to which this timepoint is relative, get the configured
			// amount of time of this timepoint from the relative timepoint, and add it to the relative timepoint
			// date to get the date of this timepoint
			if (relativeTimepointSchedAnchorDate != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(relativeTimepointSchedAnchorDate);
				
				// if the timepoint has a duration, add it to the date, because this timepoint is relative to the 
				// end of the timepoint, not the beginning. if a timepoint lasts just a single day, the duration
				// is 1. the units for duration is days (calendar days)
				if (relativeProtocolTimepointConfig.getDuration() != null) {
					// subtract 1 from duration since it is 1-based
					cal.add(Calendar.DATE, relativeProtocolTimepointConfig.getDuration()-1);
				}
				
				//TODO: convert schedWinRelativeAmount to days using schedWinRelativeUnits
				//      for schedWinRelativeMode == calendar days, should be just a matter of picking
				//      between Calendar.DATE, calendar.WEEK_OF_MONTH/YEAR, calendar.MONTH, calendar.YEAR
				
				//      for schedWinRelativeMode == working days, amount is in days, so iterate from
				//      relative Date by amount, skipping Saturdays/Sundays/holidays (i.e. convert
				//      relative Date to a Calendar object to do this)
				
				//TODO: same for repeatingTimepoint mode/units

				// alternatively: could have checkboxes for "skip weekends", "skip holidays". 
				//      then, if units=Days, iterate forward, skipping if checked
				//            if units=Weeks/Months/Years, do add, then check if Date is 
				//            a weekend or holiday, and if skip, then advance by day to next
				//            Date that is not a weekend or holiday
				
				if (protocolRelativeTimepoint.getProtocolTimepointConfig().getRepeating() != null
						&& protocolRelativeTimepoint.getProtocolTimepointConfig().getRepeating()
						 && this.getRepeatNum() > 1) {
					// if this is a repeating timepoint relative to a prior repeating timepoint in the chain,
					// add the repeat interval
					cal.add(Calendar.DATE, protocolRelativeTimepoint.getProtocolTimepointConfig().getRepeatInterval());
				}
				else {
					// otherwise, add the relative amount
					cal.add(Calendar.DATE, this.getProtocolTimepointConfig().getSchedWinRelativeAmount());
				}
				return cal.getTime();
			}
			else {
				return null;
			}
		}
	}

	
	/**
	 * calcSchedWinAnchorDate convenience method.
	 * 
	 * invokes calcSchedWinAnchorDate(false)
	 * 
	 * @return the scheduling window anchor date for this timepoint
	 */
	public Date calcSchedWinAnchorDate() {
		return this.calcSchedWinAnchorDate(false);
	}

	
	/**
	 * calcSchedWinAnchorDate convenience method.
	 * 
	 * Invokes calcSchedWinAnchorDate(true)
	 * 
	 * @return the ideal scheduling window anchor date for this timepoint
	 */
	public Date calcIdealSchedWinAnchorDate() {
		return this.calcSchedWinAnchorDate(true);
	}
	
	
	/**
	 * Given the calculation done by calcSchedAnchorDate, calculate a window defined in this timepoint's
	 * ProtocolTimepointConfig representing the acceptable window within which the actual visits for this 
	 * timepoint should be scheduled. 
	 * 
	 * The timepoint configuration's schedWinOffset determines the beginning of the scheduling window,
	 * schedWinStart, as follows:
	 *  0 equals the schedWinAnchorDate
	 *  negative value represents number of days before schedWinAnchorDate
	 *  positive value represents number of days after schedWinAnchorDate
	 *  
	 * Once the beginning of the scheduling window is determined, schedWinSize represents the size of
	 * the window and can be used to determine schedWinEnd.
	 * 
	 * Note that because timepoints can have multiple visits, a scheduling window for the first timepoint 
	 * still makes sense, as visits other than the primary visit must be scheduled within the window 
	 * anchored by the primary visit. Since schedWinOffset and schedWinSize are required fields the user
	 * must set a scheduling window that makes sense for these multiple visits. 
	 * If the first timepoint only has a single visit, the user can set them to 0 and the window will be 
	 * satisfied since the only visit is the primary visit whose visitDate will be schedWinAnchorDate. 
	 */
	public void calcSchedWinDates() {
		this.setSchedWinAnchorDate(calcSchedWinAnchorDate());
		this.setIdealSchedWinAnchorDate(calcIdealSchedWinAnchorDate());
		// scheduling window calcs fall back to ideal calcs if Visit assignment does not 
		// allow actual calc, so only need to check null on schedWinAnchorDate here, not ideal too
		if (this.getSchedWinAnchorDate() != null) {
			Calendar schedWinCal = Calendar.getInstance();
			schedWinCal.setTime(getSchedWinAnchorDate());
			Calendar idealSchedWinCal = Calendar.getInstance();
			idealSchedWinCal.setTime(getIdealSchedWinAnchorDate());
			ProtocolTimepointConfig protocolTimepointConfig = getProtocolTimepointConfig();
			// schedWinOffset is typically 0 or negative. it is negative to balance the scheduling
			// window around schedWinAnchorDate
			schedWinCal.add(Calendar.DATE, protocolTimepointConfig.getSchedWinOffset());
			this.setSchedWinStart(schedWinCal.getTime());
			idealSchedWinCal.add(Calendar.DATE, protocolTimepointConfig.getSchedWinOffset());
			this.setIdealSchedWinStart(idealSchedWinCal.getTime());
			// note: window start/end just need date elements so the time component is irrelevant. when
			// comparing whether the current time is within a window, the time component of the current date
			// is zeroed out, so just dealing with straight date comparison
			schedWinCal.add(Calendar.DATE, protocolTimepointConfig.getSchedWinSize());
			this.setSchedWinEnd(schedWinCal.getTime());
			idealSchedWinCal.add(Calendar.DATE, protocolTimepointConfig.getSchedWinSize());
			this.setIdealSchedWinEnd(idealSchedWinCal.getTime());
		}
		else {
			this.setSchedWinStart(null);
			this.setSchedWinEnd(null);
			this.setIdealSchedWinStart(null);
			this.setIdealSchedWinEnd(null);
		}
	}


	/**
	 * Calculate a default ideal date on which all instruments in this timepoint would be collected. The 
	 * date is persisted in the ProtocolTimepoint collectWinAnchorDate property.
	 * 
	 * A ProtocolTimepointConfig defines a collection window relative to the 
	 * timepoint's primary visit, which serves as the default collection window for all of the 
	 * timepoint's instruments. Note that each instrument can override this collection window
	 * by defining its own custom collection window in ProtocolInstrumentOptionConfig. That
	 * calculation is done in ProtocolInstrument. 
	 *  
	 * If a Visit has not yet been associated with the primary ProtocolVisit, then the 
	 * collectWinAnchorDate can not be calculated, and this method returns null. 
	 * 
     * @param   ideal calculate the ideal scheduling window anchor date instead of the actual 
	 * @return        the calculated value of collectWinAnchorDate for this timepoint
	 */
	public Date calcCollectWinAnchorDate(boolean ideal) {
		if (ideal) {
			return this.getIdealSchedWinAnchorDate();
		}
		else if (this.getProtocolTimepointConfig().getCollectWindowDefined()) {
			if (this.getPrimaryProtocolVisit() != null && this.getPrimaryProtocolVisit().getVisit() != null) {
				// the collection window is based on the visitDate of a Visit assigned to the
				// primary ProtocolVisit of this ProtocolTimepoint
				return this.getPrimaryProtocolVisit().getVisit().getVisitDate();
			}		
			else {
				return null;
			}
		}
		else {
			// if collection window not defined, default to the scheduling window
			return this.getSchedWinAnchorDate();
		}
	}

	/**
	 * calcCollectWinAnchorDate convenience method.
	 * 
	 * invokes calcCollectWinAnchorDate(false)
	 * 
	 * @return the collection window anchor date for this instrument
	 */
	public Date calcCollectWinAnchorDate() {
		return this.calcCollectWinAnchorDate(false);
	}

		
	/**
	 * calcCollectWinAnchorDate convenience method.
	 * 
	 * Invokes calcCollectWinAnchorDate(true)
	 * 
	 * @return the ideal collection window anchor date for this timepoint
	 */
	public Date calcIdealCollectWinAnchorDate() {
		return this.calcCollectWinAnchorDate(true);
	}
	
	
	/**
	 * Each timepoint has an optionally defined collection window which is the default for all
	 * instruments that are data collected as part of the timepoint. Additionally, a given instrument
	 * can be configured to have its own custom collection window. This method computes the start
	 * and end dates for the collection window for this instrument.
	 * 
	 * The configuration of a collection window is optional. Collection windows are typically defined
	 * in the following scenarios:
	 * 1) a timepoint default collection window specifies that all data for the timepoint is collected within
	 *    the configured window, relative to the primary visit of the timepoint, i.e. at one point should
	 *    data be excluded from analysis because it is to far from the primary visit
	 * 2) a custom collection window defined for the instrument specifies that the data for the instrument
	 *    is collected within a configured window relative to some visit of the timepoint, to ensure 
	 *    that data for two or more instruments within the same timepoint is collected within a 
	 *    certain timeframe relative to each other, i.e. one or more instruments have a level of time
	 *    sensitivity among them 
	 * 
	 * Given the calculation done by calcCollectWinAnchorDate, calculate a window defined in this protocol's
	 * configuration representing the acceptable window within which data collection for the Instrument
	 * assigned to this ProtocolInstrument should occur.
	 * 
	 * The configuration's collectWinOffset determines the beginning of the collection window,
	 * collectWinStart, as follows:
	 *  0 equals the collectWinAnchorDate
	 *  negative value represents number of days before collectWinAnchorDate
	 *  positive value represents number of days after collectWinAnchorDate
	 *  
	 * Once the beginning of the collection window is determined, collectWinSize represents the size of
	 * the window and can be used to determine collectWinEnd.
	 */
	public void calcCollectWinDates() {
		if (this.getProtocolTimepointConfig().getCollectWindowDefined()) {
			this.setCollectWinAnchorDate(this.calcCollectWinAnchorDate());
			this.setIdealCollectWinAnchorDate(this.calcIdealCollectWinAnchorDate());
			
			if (this.getCollectWinAnchorDate() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.getCollectWinAnchorDate());
				// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
				// window around collectWinAnchorDate
				cal.add(Calendar.DATE, this.getProtocolTimepointConfig().getCollectWinOffset());
				this.setCollectWinStart(cal.getTime());
				cal.add(Calendar.DATE, this.getProtocolTimepointConfig().getCollectWinSize());
				this.setCollectWinEnd(cal.getTime());
			}
			else {
				this.setCollectWinStart(null);
				this.setCollectWinEnd(null);
			}
			
			if (this.getIdealCollectWinAnchorDate() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.getIdealCollectWinAnchorDate());
				// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
				// window around collectWinAnchorDate
				cal.add(Calendar.DATE, this.getProtocolTimepointConfig().getCollectWinOffset());
				this.setIdealCollectWinStart(cal.getTime());
				cal.add(Calendar.DATE, this.getProtocolTimepointConfig().getCollectWinSize());
				this.setIdealCollectWinEnd(cal.getTime());
			}
			else {
				this.setIdealCollectWinStart(null);
				this.setIdealCollectWinEnd(null);
			}
		}
		else {
			this.setCollectWinAnchorDate(this.getSchedWinAnchorDate());
			this.setCollectWinStart(this.getSchedWinStart());
			this.setCollectWinEnd(this.getSchedWinEnd());
			this.setIdealCollectWinAnchorDate(this.getIdealSchedWinAnchorDate());
			this.setIdealCollectWinStart(this.getIdealSchedWinStart());
			this.setIdealCollectWinEnd(this.getIdealSchedWinEnd());
		}
	}
	
	

	public void updateStatus() {
		// Completion Status
		
		// determine the compStatus by iterating thru the visits of this timepoint and
		// using the most severe visit compStatus as the overall compStatus for the timepoint
		String updatedCompStatus = null;
		for (ProtocolVisit protocolVisit : this.getProtocolVisits()) {
			// visits configured as optional should not be considered
			if (protocolVisit.getProtocolVisitConfig().getOptional() == null
					|| !protocolVisit.getProtocolVisitConfig().getOptional()) {
				updatedCompStatus = this.rollupCompStatusHelper(protocolVisit, updatedCompStatus);
			}
		}
		if (updatedCompStatus == null) {
			// there are no (non-optional) visits/instruments from which to roll up a status
			updatedCompStatus = N_A;
		}
		if (this.getCompStatusOverride() == null || !this.getCompStatusOverride()) {
			this.setCompStatus(updatedCompStatus);
			// store the computed status so if user decides to override they know computed status
			this.setCompStatusComputed(updatedCompStatus);
		}
		else {
			// do not update the compStatus if there has been a user override. but stored the computed
			// value in a separate property
			this.setCompStatusComputed(updatedCompStatus);
		}
			
		
		// Scheduling Status
		
		// determine the schedWinStatus by iterating thru the visits of this timepoint and
		// using the most severe visit schedWinStatus as the overall schedWinStatus for the timepoint
		String updatedSchedWinStatus = null;
		for (ProtocolVisit protocolVisit : this.getProtocolVisits()) {
			// visits configured as optional should not be considered
			if (protocolVisit.getProtocolVisitConfig().getOptional() == null
					|| !protocolVisit.getProtocolVisitConfig().getOptional()) {
				updatedSchedWinStatus = this.rollupSchedWinStatusHelper(protocolVisit, updatedSchedWinStatus);
			}
		}
		if (updatedSchedWinStatus == null) {
			// there are no (non-optional) visits from which to roll up a status
			this.setSchedWinStatus(N_A);
		}
		else {
			this.setSchedWinStatus(updatedSchedWinStatus);
		}

		
		// Collection Status
		
		// determine the collectWinStatus by iterating thru the visits of this timepoint and
		// using the most severe visit collectWinStatus as the overall collectWinStatus for the timepoint
		String updatedCollectWinStatus = null;
		for (ProtocolVisit protocolVisit : this.getProtocolVisits()) {
			// visits configured as optional should not be considered
			if (protocolVisit.getProtocolVisitConfig().getOptional() == null 
					|| !protocolVisit.getProtocolVisitConfig().getOptional()) {
				updatedCollectWinStatus = this.rollupCollectWinStatusHelper(protocolVisit, updatedCollectWinStatus);
			}
		}
		if (updatedCollectWinStatus == null) {
			// there are no (non-optional) visits/instruments from which to roll up a status
			this.setCollectWinStatus(N_A);
		}
		else {
			this.setCollectWinStatus(updatedCollectWinStatus);
		}
	}
	
	
	/** 
jhesse data dictionary: Whether to automatically schedule the timepoint when the prior schedule from TP is scheduled.
?? does not seem like want to create Visit(s) automatically (on the schedWinAnchorDate) without knowing whether
the patient can make that Visit. better is to have in the Scheduling module/section a status showing that the Visit
should be scheduled and what the ideal date is, i.e. schedWinAnchorDate  		
	 *  When this timepoint is scheduled (? schedWinStatus=Scheduled or compStatus=Complete), check all timepoints that are relative 
	 *  to this timepoint, and any that have schedAutomatic set should be scheduled, i.e. ? Visit(s) created/assigned
	 *  
	 */
	public void doSchedAutomatic() {
	}

	/**
	 * Helper method for repeating timepoints which detects whether the following repeating
	 * timepoint exists or not.
	 * 
	 * @return true or false
	 */
	public boolean nextRepeatingExists() {
		boolean exists = false;
		// check if a following repeating timepoint exists
		// it will share the same ProtocolTimepointConfig as this ProtocolTimepoint and have 
		// a repeatNum one greater 
		for (ProtocolTimepoint protocolTimepoint : this.getProtocol().getProtocolTimepoints()) {
			if (this.getProtocolTimepointConfig().getId().equals(protocolTimepoint.getProtocolTimepointConfig().getId()) 
					&& protocolTimepoint.getRepeatNum().equals(this.getRepeatNum()+1)) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	/**
	 * Helper method for creating timepoints.
	 *  
	 * @return ProtocolTimepoint created from 
	 */
	public static ProtocolTimepoint createTimepointFromConfig(Protocol protocol, ProtocolTimepointConfig timepointConfig) {
		ProtocolTimepoint protocolTimepoint = new ProtocolTimepoint();
		protocolTimepoint.setProtocolTimepointConfig(timepointConfig);
		protocolTimepoint.setConfSchedWinDaysFromStart(timepointConfig.getSchedWinDaysFromStart());
		protocolTimepoint.setNodeType(timepointConfig.getNodeType());
		protocolTimepoint.setPatient(protocol.getPatient());
		protocolTimepoint.setProjName(timepointConfig.getProjName());
		for (ProtocolVisitConfig visitConfig: timepointConfig.getProtocolVisitConfigs()) {
			ProtocolVisit protocolVisit = new ProtocolVisit();
			protocolTimepoint.addProtocolVisit(protocolVisit);
			protocolVisit.setProtocolVisitConfig(visitConfig);
			protocolVisit.setNodeType(visitConfig.getNodeType());
			protocolVisit.setPatient(protocol.getPatient());
			protocolVisit.setProjName(visitConfig.getProjName());
			for (ProtocolInstrumentConfig instrumentConfig: visitConfig.getProtocolInstrumentConfigs()) {
				ProtocolInstrument protocolInstrument = new ProtocolInstrument();
				protocolVisit.addProtocolInstrument(protocolInstrument);
				protocolInstrument.setProtocolInstrumentConfig(instrumentConfig);
				protocolInstrument.setNodeType(instrumentConfig.getNodeType());
				protocolInstrument.setPatient(protocol.getPatient());
				protocolInstrument.setProjName(instrumentConfig.getProjName());
			}
		}
		// now that all protocolVisits have been added for the current protocolTimepoint, set the 
		// primaryProtocolVisit for protocolTimepoint based on the primaryProtocolVisitConfig
		// of timepointConfig
		
		// get the id of the primaryProtocolVisitConfig
		Long primaryProtocolVisitConfigId = timepointConfig.getPrimaryProtocolVisitConfigId();
		// iterate thru the protocolVisits until find the one whose protocolVisitConfig id matches this
		for (ProtocolVisit protocolVisit: protocolTimepoint.getProtocolVisits()) {
			if (protocolVisit.getProtocolVisitConfig().getId().equals(primaryProtocolVisitConfigId)) {
				protocolTimepoint.setPrimaryProtocolVisit(protocolVisit);
				break;
			}
		}
		
		// now that all protocolVisits have been added for the current protocolTimepoint, need to set
		// the custom collection window protocolVisit for each protocolInstrument
		for (ProtocolVisit protocolVisit: protocolTimepoint.getProtocolVisits()) {
			for (ProtocolInstrument protocolInstrument: protocolVisit.getProtocolInstruments()) {
				// set the collectWinProtocolVisit for this protocolInstrument based on the 
				// customCollectWinProtocolVisitConfig of instrumentConfig
				Long collectWinProtocolVisitConfigId = protocolInstrument.getProtocolInstrumentConfig().getCustomCollectWinProtocolVisitConfigId();
				// iterate thru the protocolVisits until find one whose protocolVisitConfig id matches this
				for (ProtocolVisit protocolVisit2: protocolTimepoint.getProtocolVisits()) {
					if (protocolVisit2.getProtocolVisitConfig().getId().equals(collectWinProtocolVisitConfigId)) {
						protocolInstrument.setInstrCollectWinProtocolVisit(protocolVisit2);
						break;
					}
				}
			}
		}
		return protocolTimepoint;
	}

	
	/**
	 * Helper method for creating repeating timepoints.
	 *  
	 * @return ProtocolTimepoint created from 
	 */
	public static ProtocolTimepoint createRepeatingTimepointFromConfig(Protocol protocol, ProtocolTimepointConfig timepointConfig, Short repeatNum) {
		ProtocolTimepoint protocolTimepoint = ProtocolTimepoint.createTimepointFromConfig(protocol, timepointConfig);
		protocolTimepoint.setRepeatNum(repeatNum);
		return protocolTimepoint;
	}

	
	/**
	 * If this is a repeating timepoint, if the repeatCreateAutomatic flag is set, when this
	 * timepoint is complete create the next repeating timepoint.
	 */
	public void doRepeatCreateAutomatic() {
		if (this.getProtocolTimepointConfig().getRepeating() != null 
				&& this.getProtocolTimepointConfig().getRepeating()) {
			if (this.getCompStatus() != null && (this.getCompStatus().equals(COMPLETED) || this.getCompStatus().equals(PARTIAL))) {
				if (!this.nextRepeatingExists()) {
					// create the following repeating timepoint

					// retrieve the entire ProtocolTimepointConfig tree as it will be needed in createTimepointFromConfig (could have these
					// retrieved in getAssociationsToInitialize but since only needed for adding repeating timepoints, not need to do it
					// always)
					LavaDaoFilter filter = EntityBase.newFilterInstance();
					filter.addDaoParam(filter.daoNamedParam("protocolTimepointConfigId", this.getProtocolTimepointConfig().getId()));
					ProtocolTimepointConfig completeProtocolTimepointConfig = (ProtocolTimepointConfig) EntityBase.MANAGER.findOneByNamedQuery("protocol.completeProtocolTimepointConfigTree", filter);
					ProtocolTimepoint newRepeatingTimepoint = ProtocolTimepoint.createRepeatingTimepointFromConfig(this.getProtocol(), completeProtocolTimepointConfig, (short)(this.getRepeatNum()+1));
					// add the timepoint to the protocol for the patient
					getProtocol().addProtocolTimepoint(newRepeatingTimepoint);
				}
			}
		}
		
	}
	
	
	/** 
	 * Perform any calculations done in ProtocolTimepoint. This method should be called 
	 * whenever anything that may affect these calculations occurs, such as changes in the
	 * Protocol configuration, and assigning a Visit to the Protocol.
	 */
	public void calculate() {
		this.calcSchedWinDates();
		this.calcCollectWinDates();
	}

	
	/**
	 * The ProtocolTimepoint collection should be ordered chronologically. If the patient has started
	 * the protocol, i.e. a Visit has been assigned as the primary visit of the first timepoint, then
	 * the schedWinAnchorDate can be calculated for every timepoint and since this is a persisted
	 * property the collection could be retrieved in this order. However, if the patient has not started
	 * the protocol, schedWinAnchorDate will be null. Still want to have an ordered collection though,
	 * for proper display of the protocol. So must instead order the timepoints collection based on the 
	 * protocol configuration. In particular, order on the ProtocolTimepointConfig schedWinDaysFromStart 
	 * property of the timepoint's corresponding protocol timepoint configuration. In order to do this,
	 * must implement the compareTo method.    
	 */
	public int compareTo(ProtocolTimepoint protocolTimepoint) throws ClassCastException {
		// first try to order on schedWinAnchorDate
		if (this.getSchedWinAnchorDate() != null && protocolTimepoint.getSchedWinAnchorDate() != null) {
	        if (this.getSchedWinAnchorDate().after(protocolTimepoint.getSchedWinAnchorDate()))
	            return 1;
	        else if (this.getSchedWinAnchorDate().before(protocolTimepoint.getSchedWinAnchorDate()))
	            return -1;
	        else
	            return 0;
		}
		else {
			// weirdness: in some cases for unknown reasons, the standard refresh done following saving an 
			// entity results in a ProtocolTimepoint's ProtocolTimepointConfig being set to null, so have to 
			// check for that here. note the result is that the sort can not be done properly. however, since
			// this is only happening on save/refresh and not on retrieval, it is not a factor
			if (this.getProtocolTimepointConfig() == null || protocolTimepoint.getProtocolTimepointConfig() == null) {
				return 0;
			}
			if (this.getConfSchedWinDaysFromStart() == null 
					&& protocolTimepoint.getConfSchedWinDaysFromStart() == null) {
				return 0;
			}
			else if (this.getConfSchedWinDaysFromStart() != null 
					&& protocolTimepoint.getConfSchedWinDaysFromStart() == null) {
				return 1;
			}
			else if (this.getProtocolTimepointConfig().getSchedWinDaysFromStart() == null 
					&& protocolTimepoint.getConfSchedWinDaysFromStart() != null) {
				return -1;
			}
			
	        if (this.getConfSchedWinDaysFromStart() > protocolTimepoint.getConfSchedWinDaysFromStart())
	            return 1;
	        else if (this.getConfSchedWinDaysFromStart() < protocolTimepoint.getConfSchedWinDaysFromStart())
	            return -1;
	        else {
	        	if (this.getProtocolTimepointConfig().getRepeating() != null && this.getProtocolTimepointConfig().getRepeating() 
	        			&& protocolTimepoint.getProtocolTimepointConfig().getRepeating() != null && protocolTimepoint.getProtocolTimepointConfig().getRepeating()) {
	        		if (this.getRepeatNum() > protocolTimepoint.getRepeatNum()) {
	        			return 1;
	        		}
	        		else if (this.getRepeatNum() < protocolTimepoint.getRepeatNum()) {
	        			return -1;
	        		}
	        		else { 
	        			// should never get here, i.e. repeating timepoints with same repeatNum 
	        			return 0; 
	        		}
	        	}
	        	else {
	        		return 0;
	        	}
	        }
		}
	}

	
	public void postUpdateStatus() {
// not sure about doSchedAutomatic b/c adding a Visit to the following Timepoint will alter status on 
// that timepoint so will need to updateStatus on that timepoint (other questions about schedAutomatic
// in notes)
		doSchedAutomatic();
		doRepeatCreateAutomatic();
	}
	
}
