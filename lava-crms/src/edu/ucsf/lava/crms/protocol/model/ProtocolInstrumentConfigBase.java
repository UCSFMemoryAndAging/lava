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

public abstract class ProtocolInstrumentConfigBase extends ProtocolNodeConfig {
	public ProtocolInstrumentConfigBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolVisitConfigBase");
		this.addPropertyToAuditIgnoreList("protocolInstrumentOptionConfigsBase");
	}

	private ProtocolVisitConfigBase protocolVisitConfigBase;
	private Set<? extends ProtocolInstrumentOptionConfigBase> protocolInstrumentOptionConfigsBase = new HashSet<ProtocolInstrumentOptionConfigBase>();

	public ProtocolVisitConfigBase getProtocolVisitConfigBase() {
		return protocolVisitConfigBase;
	}

	public void setProtocolVisitConfigBase(ProtocolVisitConfigBase protocolVisitConfigBase) {
		this.protocolVisitConfigBase = protocolVisitConfigBase;
	}

	public Set<? extends ProtocolInstrumentOptionConfigBase> getProtocolInstrumentOptionConfigsBase() {
		return protocolInstrumentOptionConfigsBase;
	}

	public void setProtocolInstrumentOptionConfigsBase(Set<? extends ProtocolInstrumentOptionConfigBase> protocolInstrumentOptionConfigsBase) {
		this.protocolInstrumentOptionConfigsBase = protocolInstrumentOptionConfigsBase;
	}
	
}
