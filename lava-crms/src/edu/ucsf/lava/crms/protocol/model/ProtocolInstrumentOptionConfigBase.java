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
		this.addPropertyToAuditIgnoreList("instrumentBase");
	}

	private ProtocolInstrumentConfigBase instrumentBase;

	public ProtocolInstrumentConfigBase getInstrumentBase() {
		return instrumentBase;
	}

	public void setInstrumentBase(ProtocolInstrumentConfigBase protocolInstrumentBase) {
		this.instrumentBase = protocolInstrumentBase;
	}
}
