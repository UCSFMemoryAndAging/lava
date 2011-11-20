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
		this.addPropertyToAuditIgnoreList("protocolVisitBase");		
		this.addPropertyToAuditIgnoreList("protocolInstrumentConfigBase");		
	}

	private ProtocolVisitBase protocolVisitBase;
	private ProtocolInstrumentConfigBase protocolInstrumentConfigBase;

	public ProtocolVisitBase getProtocolVisitBase() {
		return protocolVisitBase;
	}

	public void setProtocolVisitBase(ProtocolVisitBase protocolVisit) {
		this.protocolVisitBase = protocolVisit;
	}

	public ProtocolInstrumentConfigBase getProtocolInstrumentConfigBase() {
		return protocolInstrumentConfigBase;
	}
	public void setProtocolInstrumentConfigBase(ProtocolInstrumentConfigBase protocolInstrumentConfig) {
		this.protocolInstrumentConfigBase = protocolInstrumentConfig;
	}
	
}
