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
		this.addPropertyToAuditIgnoreList("timepointBase");
		this.addPropertyToAuditIgnoreList("visitConfigBase");
		this.addPropertyToAuditIgnoreList("instrumentsBase");
	}
	
	private ProtocolTimepointBase timepointBase;
	private ProtocolVisitConfigBase visitConfigBase;
	private Set<? extends ProtocolInstrumentBase> instrumentsBase = new LinkedHashSet<ProtocolInstrumentBase>();
	
	public ProtocolTimepointBase getTimepointBase() {
		return timepointBase;
	}

	public void setTimepointBase(ProtocolTimepointBase timepointBase) {
		this.timepointBase = timepointBase;
	}
	
	public ProtocolVisitConfigBase getVisitConfigBase() {
		return visitConfigBase;
	}

	public void setVisitConfigBase(ProtocolVisitConfigBase visitBase) {
		this.visitConfigBase = visitBase;
	}
	
	public Set<? extends ProtocolInstrumentBase> getInstrumentsBase() {
		return instrumentsBase;
	}

	public void setInstrumentsBase(Set<? extends ProtocolInstrumentBase> instrumentsBase) {
		this.instrumentsBase = instrumentsBase;
	}
}
