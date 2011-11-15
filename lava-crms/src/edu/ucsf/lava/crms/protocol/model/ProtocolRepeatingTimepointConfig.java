package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 *   
 * @author ctoohey
 *
 * The representation of a Timepoint that that repeats at regular intervals over time. In 
 * the protocol config there is only one instance of this timepoint, but when a patient 
 * is assigned to the protocol, the entire sequence of timepoints are created (TODO).
 * 
 *
 */
public class ProtocolRepeatingTimepointConfig extends ProtocolTimepointConfig {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolRepeatingTimepointConfig.class);
	
	public ProtocolRepeatingTimepointConfig(){
		super();
	}
	
	private Short total; // total timepoints
	private Short interval;
	private Boolean weekends;
	private Boolean holidays;
	
	public Short getTotal() {
		return total;
	}

	public void setTotal(Short total) {
		this.total = total;
	}

	public Short getInterval() {
		return interval;
	}

	public void setInterval(Short interval) {
		this.interval = interval;
	}

	public Boolean getWeekends() {
		return weekends;
	}

	public void setWeekends(Boolean weekends) {
		this.weekends = weekends;
	}

	public Boolean getHolidays() {
		return holidays;
	}

	public void setHolidays(Boolean holidays) {
		this.holidays = holidays;
	}

}
