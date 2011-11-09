package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * This class exists to allow protocol tree retrieval queries which only involving the protocol node and protocol option
 * base tables. This class is mapped such that it is not a subclass of the protocol option base table.
 * 
 * @author ctoohey
 * 
 *
 */

public class ProtocolInstrumentOptionBase extends ProtocolOptionBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolInstrumentOptionBase.class);
	
	public ProtocolInstrumentOptionBase(){
		super();
		this.addPropertyToAuditIgnoreList("instrument");
	}

	private ProtocolInstrumentBase instrument;

	public ProtocolInstrumentBase getInstrument() {
		return instrument;
	}

	public void setInstrument(ProtocolInstrumentBase instrument) {
		this.instrument = instrument;
	}
}
