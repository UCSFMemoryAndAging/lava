package edu.ucsf.lava.crms.protocol.model;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * This class offers a lightweight version of each of the Protocol component types, such that
 * the entire tree for a given Protocol can be retrieved by just querying the base (of course, 
 * the objects in the tree only contain the properties common across all component types, i.e.
 * the properties in ProtocolNode). 
 * 
 * @author ctoohey
 * 
 *
 */

public class ProtocolTracking extends ProtocolNode /*implements Comparable<ProtocolTracking> */ {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolTracking.class);
	
	public ProtocolTracking(){
		super();
		this.setProjectAuth(true);
	}

	private ProtocolTracking parent;
	private ProtocolConfigTracking config; 
	// use TreeSet so collection can be sorted chronologically in subclasses, if desired
	private Set<ProtocolTracking> children = new LinkedHashSet<ProtocolTracking>();

	public ProtocolTracking getParent() {
		return parent;
	}

	public void setParent(ProtocolTracking parent) {
		this.parent = parent;
	}
	
	public ProtocolConfigTracking getConfig() {
		return config;
	}

	public void setConfig(ProtocolConfigTracking config) {
		this.config = config;
	}

	public Set<ProtocolTracking> getChildren() {
		return children;
	}

	public void setChildren(Set<ProtocolTracking> children) {
		this.children = children;
	}
	
	/**
	 * ProtocolTracking does not do any calculations as it is a lightweight representation
	 * used for all types of Protocol components and does not have the properties required
	 * for calculations, as these live in the subclasses.
	 */
	public void calculate() {
		
	}
	
	/**
	 * ProtocolTracking does not do any calculations as it is a lightweight representation
	 * used for all types of Protocol components and does not have the properties required
	 * for calculations, as these live in the subclasses.
	 */
	public void updateStatus() {
		
	}
	
	
	/**
	 * The ProtocolTimepoint collection should be ordered chronologically. If the patient has started
	 * the protocol, i.e. a Visit has been assigned as the primary visit of the first timepoint, then
	 * the schedWinAnchorDate can be calculated for every timepoint and since this is a persisted
	 * property the collection could be retrieved in this order. However, if the patient has not started
	 * the protocol, schedWinAnchorDate will be null. Still want to have an ordered collection though,
	 * for proper display of the protocol. So must instead order the timepoints collection based on the 
	 * protocol configuration. In particular, order on the ProtocolTimepointConfig listOrder 
	 * property of the timepoint's corresponding protocol timepoint configuration. In order to do this,
	 * must implement the compareTo method.    
	 * 
	 * Note that while this sorts ProtocolTracking objects of all types, only TIMEPOINT instances
	 * will have the schedWinAnchorDate and/or listOrder properties populated, so essentially this is
	 * a timepoint sort.
	 */
	public int compareTo(ProtocolTracking protocolTracking) throws ClassCastException {
		// first try to order on schedWinAnchorDate
		if (this.getSchedWinAnchorDate() != null && protocolTracking.getSchedWinAnchorDate() != null) {
	        if (this.getSchedWinAnchorDate().after(protocolTracking.getSchedWinAnchorDate()))
	            return 1;
	        else if (this.getSchedWinAnchorDate().before(protocolTracking.getSchedWinAnchorDate()))
	            return -1;
	        else
	            return 0;
		}
		else {
			if (this.getConfig().getListOrder() == null 
					&& protocolTracking.getConfig().getListOrder() == null) {
				return 0;
			}
			else if (this.getConfig().getListOrder() != null 
					&& protocolTracking.getConfig().getListOrder() == null) {
				return 1;
			}
			else if (this.getConfig().getListOrder() == null 
					&& protocolTracking.getConfig().getListOrder() != null) {
				return -1;
			}
			
	        if (this.getConfig().getListOrder() 
	        		> protocolTracking.getConfig().getListOrder())
	            return 1;
	        else if (this.getConfig().getListOrder() 
	        		< protocolTracking.getConfig().getListOrder())
	            return -1;
	        else {
	        	if (this.getConfig().getRepeating() != null && this.getConfig().getRepeating() 
	        			&& protocolTracking.getConfig().getRepeating() != null && protocolTracking.getConfig().getRepeating()) {
	        		if (this.getRepeatNum() > protocolTracking.getRepeatNum()) {
	        			return 1;
	        		}
	        		else if (this.getRepeatNum() < protocolTracking.getRepeatNum()) {
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

}
