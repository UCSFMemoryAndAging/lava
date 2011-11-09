package edu.ucsf.lava.crms.protocol.model;

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
public class PatientProtocolTimepointBase extends PatientProtocolNode {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocolTimepointBase.class);
	
	public PatientProtocolTimepointBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocol");
		this.addPropertyToAuditIgnoreList("visits");
	}

	private PatientProtocolBase patientProtocol;
	private ProtocolTimepointBase protocolTimepoint;
	private Set<PatientProtocolVisitBase> visits = new LinkedHashSet<PatientProtocolVisitBase>();

	public PatientProtocolBase getPatientProtocol() {
		return patientProtocol;
	}

	public void setPatientProtocol(PatientProtocolBase patientProtocol) {
		this.patientProtocol = patientProtocol;
	}
	
	public ProtocolTimepointBase getProtocolTimepoint() {
		return protocolTimepoint;
	}

	public void setProtocolTimepoint(ProtocolTimepointBase protocolTimepoint) {
		this.protocolTimepoint = protocolTimepoint;
	}

	public Set<PatientProtocolVisitBase> getVisits() {
		return visits;
	}

	public void setVisits(Set<PatientProtocolVisitBase> visits) {
		this.visits = visits;
	}
	
	public void addVisit(PatientProtocolVisit visit) {
		visit.setPatientProtocolTimepoint(this);
		this.visits.add(visit);
	}
}
