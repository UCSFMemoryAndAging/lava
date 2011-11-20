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

public abstract class ProtocolVisitBase extends ProtocolNode {
	public ProtocolVisitBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolTimepointBase");
		this.addPropertyToAuditIgnoreList("protocolVisitConfigBase");
		this.addPropertyToAuditIgnoreList("protocolInstrumentsBase");
	}
	
	private ProtocolTimepointBase protocolTimepointBase;
	private ProtocolVisitConfigBase protocolVisitConfigBase;
	private Set<? extends ProtocolInstrumentBase> protocolInstrumentsBase = new LinkedHashSet<ProtocolInstrumentBase>();
	
	public ProtocolTimepointBase getProtocolTimepointBase() {
		return protocolTimepointBase;
	}

	public void setProtocolTimepointBase(ProtocolTimepointBase protocolTimepointBase) {
		this.protocolTimepointBase = protocolTimepointBase;
	}
	
	public ProtocolVisitConfigBase getProtocolVisitConfigBase() {
		return protocolVisitConfigBase;
	}

	public void setProtocolVisitConfigBase(ProtocolVisitConfigBase protocolVisitBase) {
		this.protocolVisitConfigBase = protocolVisitBase;
	}
	
	public Set<? extends ProtocolInstrumentBase> getProtocolInstrumentsBase() {
		return protocolInstrumentsBase;
	}

	public void setProtocolInstrumentsBase(Set<? extends ProtocolInstrumentBase> protocolInstrumentsBase) {
		this.protocolInstrumentsBase = protocolInstrumentsBase;
	}
}
