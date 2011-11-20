package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * @author ctoohey
 * 
 *
 */

public abstract class ProtocolInstrumentOptionConfigBase extends ProtocolOptionConfigBase {
	public ProtocolInstrumentOptionConfigBase(){
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
