package edu.ucsf.lava.crms.protocol.model;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author ctoohey
 * 
 *
 */

public abstract class ProtocolBase extends ProtocolNode {
	public ProtocolBase(){
		super();
		this.addPropertyToAuditIgnoreList("protocolConfigBase");
		this.addPropertyToAuditIgnoreList("timepointsBase");
	}

	private ProtocolConfigBase protocolConfigBase;
	// protocolConfigId is part of the mechanism required to set the ProtocolConfig association, allowing 
	// the user to designate a protocolConfig in the view. the id is used when saving a new Protocol to
	// create the Protocol tree structure
	private Long protocolConfigId; 
	private Set<? extends ProtocolTimepointBase> timepointsBase = new LinkedHashSet<ProtocolTimepointBase>();
	
	public ProtocolConfigBase getProtocolConfigBase() {
		return protocolConfigBase;
	}

	public void setProtocolConfigBase(ProtocolConfigBase protocolBase) {
		this.protocolConfigBase = protocolBase;
		// this is the paradigm used to set associations via the UI
		if (protocolBase != null) {
			this.protocolConfigId = protocolBase.getId();
		}
	}
	
	public Long getProtocolConfigId() {
		return protocolConfigId;
	}

	public void setProtocolConfigId(Long protocolConfigId) {
		this.protocolConfigId = protocolConfigId;
	}

	public Set<? extends ProtocolTimepointBase> getTimepointsBase() {
		return timepointsBase;
	}

	public void setTimepointsBase(Set<? extends ProtocolTimepointBase> timepointsBase) {
		this.timepointsBase = timepointsBase;
	}

	
}
