package edu.ucsf.lava.crms.protocol.model;

import java.util.HashSet;
import java.util.TreeSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 * 
 *
 */

public class ProtocolBase extends ProtocolNode {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolBase.class);
	
	public ProtocolBase(){
		super();
		this.addPropertyToAuditIgnoreList("timepoints");
	}
	
	public Set<ProtocolTimepointBase> timepoints = new TreeSet<ProtocolTimepointBase>();
	
	public Set<ProtocolTimepointBase> getTimepoints() {
		return timepoints;
	}

	public void setTimepoints(Set<ProtocolTimepointBase> timepoints) {
		this.timepoints = timepoints;
	}
	
	// note the argument needs to be ProtocolTimepoint not ProtocolTimepointBase due to
	// some reflection weirdness
	public void addTimepoint(ProtocolTimepoint timepoint) {
		timepoint.setProtocol(this);
		this.timepoints.add(timepoint);
	}
	
	public void orderTimepoints() {
		// iterate thru the sorted timepoints and assign values to listOrder so the sorting
		// is persisted
		short i = 1;
		for (ProtocolTimepointBase protocolTimepoint : this.getTimepoints()) {
			protocolTimepoint.setListOrder(i++);
		}
	}
	
}
