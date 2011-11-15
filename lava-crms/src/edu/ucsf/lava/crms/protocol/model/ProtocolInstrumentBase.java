package edu.ucsf.lava.crms.protocol.model;


/**
 * 
 * @author ctoohey
 * 
 *
 */

public abstract class ProtocolInstrumentBase extends ProtocolNode {
	public ProtocolInstrumentBase(){
		super();
		this.addPropertyToAuditIgnoreList("visitBase");		
		this.addPropertyToAuditIgnoreList("instrumentBase");		
	}

	private ProtocolVisitBase visitBase;
	private ProtocolInstrumentConfigBase instrumentConfigBase;

	public ProtocolVisitBase getVisitBase() {
		return visitBase;
	}

	public void setVisitBase(ProtocolVisitBase visitBase) {
		this.visitBase = visitBase;
	}

	public ProtocolInstrumentConfigBase getInstrumentConfigBase() {
		return instrumentConfigBase;
	}
	public void setInstrumentConfigBase(ProtocolInstrumentConfigBase instrumentConfigBase) {
		this.instrumentConfigBase = instrumentConfigBase;
	}
	
}
