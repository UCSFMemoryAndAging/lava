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
 *
 */

public class PatientProtocolBase extends PatientProtocolNode {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocolBase.class);
	
	public PatientProtocolBase(){
		super();
		this.addPropertyToAuditIgnoreList("timepoints");
	}

	private ProtocolBase protocol;
	// protocolId is part of the mechanism required to set the Protocol association, allowing the user
	// to designate a protocol in the view. the id is used when saving a new PatientProtocol to set 
	// the Protocol association
	private Long protocolId; 
	private Set<PatientProtocolTimepointBase> timepoints = new LinkedHashSet<PatientProtocolTimepointBase>();
	
	public ProtocolBase getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolBase protocol) {
		this.protocol = protocol;
		// this is the paradigm used to set associations via the UI
		if (this.protocol != null) {
			this.protocolId = this.protocol.getId();
		}
	}

	public Long getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Long protocolId) {
		this.protocolId = protocolId;
	}

	public Set<PatientProtocolTimepointBase> getTimepoints() {
		return timepoints;
	}

	public void setTimepoints(Set<PatientProtocolTimepointBase> timepoints) {
		this.timepoints = timepoints;
	}
	
	public void addTimepoint(PatientProtocolTimepoint timepoint) {
		timepoint.setPatientProtocol(this);
		this.timepoints.add(timepoint);
	}
}
