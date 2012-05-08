package edu.ucsf.lava.crms.protocol.model;


/**
 * @author ctoohey
 * 
 *
 */

public abstract class ProtocolInstrumentConfigOptionBase extends ProtocolNodeConfigOption {
	public ProtocolInstrumentConfigOptionBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolInstrumentConfigBase");
	}

	private ProtocolInstrumentConfigBase protocolInstrumentConfigBase;

	public ProtocolInstrumentConfigBase getProtocolInstrumentConfigBase() {
		return protocolInstrumentConfigBase;
	}

	public void setProtocolInstrumentConfigBase(ProtocolInstrumentConfigBase protocolInstrumentConfigBase) {
		this.protocolInstrumentConfigBase = protocolInstrumentConfigBase;
	}
}
