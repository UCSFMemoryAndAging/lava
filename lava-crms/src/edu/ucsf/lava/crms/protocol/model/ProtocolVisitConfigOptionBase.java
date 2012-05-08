package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * This class exists to allow protocol tree retrieval queries which only involving the
 * protocol option config base table. This class is mapped such that it is not a subclass of 
 * the protocol option config base table.
 * 
 * @author ctoohey
 * 
 *
 */

public class ProtocolVisitConfigOptionBase extends ProtocolNodeConfigOption {
	public ProtocolVisitConfigOptionBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolVisitConfigBase");
	}

	private ProtocolVisitConfigBase protocolVisitConfigBase;

	public ProtocolVisitConfigBase getProtocolVisitConfigBase() {
		return protocolVisitConfigBase;
	}

	public void setProtocolVisitConfigBase(ProtocolVisitConfigBase protocolVisitConfig) {
		this.protocolVisitConfigBase = protocolVisitConfig;
	}
}
