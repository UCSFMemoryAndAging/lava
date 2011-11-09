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

public class PatientProtocolVisitBase extends PatientProtocolNode {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocolVisitBase.class);
	
	public PatientProtocolVisitBase(){
		super();
		this.addPropertyToAuditIgnoreList("timepoint");
		this.addPropertyToAuditIgnoreList("instruments");
	}
	
	private PatientProtocolTimepointBase patientProtocolTimepoint;
	private ProtocolVisitBase protocolVisit;
	private Set<PatientProtocolInstrumentBase> instruments = new LinkedHashSet<PatientProtocolInstrumentBase>();
	
	public PatientProtocolTimepointBase getPatientProtocolTimepoint() {
		return patientProtocolTimepoint;
	}

	public void setPatientProtocolTimepoint(PatientProtocolTimepointBase patientProtocolTimepoint) {
		this.patientProtocolTimepoint = patientProtocolTimepoint;
	}
	
	public ProtocolVisitBase getProtocolVisit() {
		return protocolVisit;
	}

	public void setProtocolVisit(ProtocolVisitBase protocolVisit) {
		this.protocolVisit = protocolVisit;
	}
	
	public Set<PatientProtocolInstrumentBase> getInstruments() {
		return instruments;
	}

	public void setInstruments(Set<PatientProtocolInstrumentBase> instruments) {
		this.instruments = instruments;
	}

	public void addInstrument(PatientProtocolInstrument instrument) {
		instrument.setPatientProtocolVisit(this);
		this.instruments.add(instrument);
	}
	
}
