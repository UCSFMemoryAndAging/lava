package edu.ucsf.lava.crms.protocol.model;

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
public abstract class ProtocolTimepointBase extends ProtocolNode {
	public ProtocolTimepointBase(){
		super();
     	this.addPropertyToAuditIgnoreList("protocolBase");
		this.addPropertyToAuditIgnoreList("protocolTimepointConfigBase");
		this.addPropertyToAuditIgnoreList("protocolVisitsBase");
	}

	private ProtocolBase protocolBase;
	private ProtocolTimepointConfigBase protocolTimepointConfigBase;
	private Set<? extends ProtocolVisitBase> protocolVisitsBase = new LinkedHashSet<ProtocolVisitBase>();

	public ProtocolBase getProtocolBase() {
		return protocolBase;
	}

	public void setProtocolBase(ProtocolBase protocol) {
		this.protocolBase = protocol;
	}
	
	public ProtocolTimepointConfigBase getProtocolTimepointConfigBase() {
		return protocolTimepointConfigBase;
	}

	public void setProtocolTimepointConfigBase(ProtocolTimepointConfigBase timepointConfig) {
		this.protocolTimepointConfigBase = timepointConfig;
	}

	public Set<? extends ProtocolVisitBase> getProtocolVisitsBase() {
		return protocolVisitsBase;
	}

	public void setProtocolVisitsBase(Set<? extends ProtocolVisitBase> protocolVisitsBase) {
		this.protocolVisitsBase = protocolVisitsBase;
	}
}
