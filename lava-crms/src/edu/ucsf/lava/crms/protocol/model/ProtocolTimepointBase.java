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
     	this.addPropertyToAuditIgnoreList("patientProtocolBase");
		this.addPropertyToAuditIgnoreList("protocolTimepointBase");
		this.addPropertyToAuditIgnoreList("visitsBase");
	}

	private ProtocolBase protocolBase;
	private ProtocolTimepointConfigBase timepointConfigBase;
	private Set<? extends ProtocolVisitBase> visitsBase = new LinkedHashSet<ProtocolVisitBase>();

	public ProtocolBase getProtocolBase() {
		return protocolBase;
	}

	public void setProtocolBase(ProtocolBase protocolBase) {
		this.protocolBase = protocolBase;
	}
	
	public ProtocolTimepointConfigBase getTimepointConfigBase() {
		return timepointConfigBase;
	}

	public void setTimepointConfigBase(ProtocolTimepointConfigBase timepointConfigBase) {
		this.timepointConfigBase = timepointConfigBase;
	}

	public Set<? extends ProtocolVisitBase> getVisitsBase() {
		return visitsBase;
	}

	public void setVisitsBase(Set<? extends ProtocolVisitBase> visitsBase) {
		this.visitsBase = visitsBase;
	}
}
