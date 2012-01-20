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
		this.addPropertyToAuditIgnoreList("protocolInstrumentConfigOptionsBase");
	}

	private ProtocolVisitConfigBase protocolVisitConfigBase;
	private Set<? extends ProtocolInstrumentConfigOptionBase> protocolInstrumentConfigOptionsBase = new HashSet<ProtocolInstrumentConfigOptionBase>();

	public ProtocolVisitConfigBase getProtocolVisitConfigBase() {
		return protocolVisitConfigBase;
	}

	public void setProtocolVisitConfigBase(ProtocolVisitConfigBase protocolVisitConfigBase) {
		this.protocolVisitConfigBase = protocolVisitConfigBase;
	}

	public Set<? extends ProtocolInstrumentConfigOptionBase> getProtocolInstrumentConfigOptionsBase() {
		return protocolInstrumentConfigOptionsBase;
	}

	public void setProtocolInstrumentConfigOptionsBase(Set<? extends ProtocolInstrumentConfigOptionBase> protocolInstrumentConfigOptionsBase) {
		this.protocolInstrumentConfigOptionsBase = protocolInstrumentConfigOptionsBase;
	}
	
}
