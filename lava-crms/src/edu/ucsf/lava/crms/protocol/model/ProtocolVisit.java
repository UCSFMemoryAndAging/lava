package edu.ucsf.lava.crms.protocol.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 * 
 * ProtocolVisit is a protocol node which defines the requirements for a patient visit
 * at a defined protocol timepoint. The eligible visit types which could fulfill the requirements 
 * are instances of the ProtocolVisitOption class, where any one ProtocolVisitOption could fulfill 
 * a ProtocolVisit definition.
 * 
 * A ProtocolVisit should be something that will be scheduled. A ProtocolTimepoint defines a Scheduling 
 * window within which all of its visits should be scheduled. Its counterpart class, PatientProtocolVisit,
 * contains a method can be called to determine whether it satisfies the ProtocolTimepoint
 * Scheduling window.
 *
 */

public class ProtocolVisit extends ProtocolVisitBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolVisit.class);
	
	public ProtocolVisit(){
		super();
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.getInstruments(), this.getOptions()};
	}
	
	private Boolean optional;
	
	/**
	 * Convenience method to return ProtocolTimepoint instead of ProtocolTimepointBase.
	 */
//	public ProtocolTimepoint getTimepoint() {
//		return (ProtocolTimepoint) super.getTimepoint();
//	}

	public Boolean getOptional() {
		return optional;
	}
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}

}
