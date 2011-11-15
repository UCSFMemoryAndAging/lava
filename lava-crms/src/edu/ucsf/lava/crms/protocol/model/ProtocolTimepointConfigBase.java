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
public abstract class ProtocolTimepointConfigBase extends ProtocolNodeConfig {
	
	public ProtocolTimepointConfigBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolConfigBase");
		this.addPropertyToAuditIgnoreList("visitsBase");
	}

	private ProtocolConfigBase protocolConfigBase;
	private Set<? extends ProtocolVisitConfigBase> visitsBase = new LinkedHashSet<ProtocolVisitConfigBase>();

	public ProtocolConfigBase getProtocolConfigBase() {
		return protocolConfigBase;
	}

	public void setProtocolConfigBase(ProtocolConfigBase protocolBase) {
		this.protocolConfigBase = protocolBase;
	}

	public Set<? extends ProtocolVisitConfigBase> getVisitsBase() {
		return visitsBase;
	}

	public void setVisitsBase(Set<? extends ProtocolVisitConfigBase> visits) {
		this.visitsBase = visits;
	}

/***	
	public int compareTo(ProtocolTimepointBase protocolTimepoint) throws ClassCastException {
		if (this.getListOrder() > protocolTimepoint.getListOrder()) {
			return 1;
		}
		else if (this.getListOrder() < protocolTimepoint.getListOrder()) {
			return -1;
		}
		else {
			return 0;
		}
	}
***/	
	
}
