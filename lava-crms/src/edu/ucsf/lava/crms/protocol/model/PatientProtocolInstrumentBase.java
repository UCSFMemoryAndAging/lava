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

public class PatientProtocolInstrumentBase extends PatientProtocolNode {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocolInstrumentBase.class);
	
	public PatientProtocolInstrumentBase(){
		super();
		this.addPropertyToAuditIgnoreList("visit");
	}

	private PatientProtocolVisitBase patientProtocolVisit;
	private ProtocolInstrumentBase protocolInstrument;

	public PatientProtocolVisitBase getPatientProtocolVisit() {
		return patientProtocolVisit;
	}

	public void setPatientProtocolVisit(PatientProtocolVisitBase visit) {
		this.patientProtocolVisit = visit;
	}

	public ProtocolInstrumentBase getProtocolInstrument() {
		return protocolInstrument;
	}
	public void setProtocolInstrument(ProtocolInstrumentBase protocolInstrument) {
		this.protocolInstrument = protocolInstrument;
	}
}
