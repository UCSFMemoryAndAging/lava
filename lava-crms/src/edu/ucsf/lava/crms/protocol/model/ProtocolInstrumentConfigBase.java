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
		this.addPropertyToAuditIgnoreList("visitBase");
		this.addPropertyToAuditIgnoreList("optionsBase");
	}

	private ProtocolVisitConfigBase visitBase;
	private Set<? extends ProtocolInstrumentOptionConfigBase> optionsBase = new HashSet<ProtocolInstrumentOptionConfigBase>();

	public ProtocolVisitConfigBase getVisitBase() {
		return visitBase;
	}

	public void setVisitBase(ProtocolVisitConfigBase protocolVisitBase) {
		this.visitBase = protocolVisitBase;
	}

	public Set<? extends ProtocolInstrumentOptionConfigBase> getOptionsBase() {
		return optionsBase;
	}

	public void setOptionsBase(Set<? extends ProtocolInstrumentOptionConfigBase> optionsBase) {
		this.optionsBase = optionsBase;
	}
	
}
